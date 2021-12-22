import BaseEventAnalysisPage from './BaseEventAnalysisPage';
import ErrorPage from 'shared/pages/ErrorPage';
import React from 'react';
import Spinner from 'shared/components/Spinner';
import useEventAnalysisData from 'event-analysis/hooks/useEventAnalysisData';
import {AttributesProvider} from '../components/event-analysis-editor/context/attributes';
import {Routes, toRoute} from 'shared/util/router';
import {useParams} from 'react-router-dom';

const EventAnalysisEdit: React.FC<React.HTMLAttributes<HTMLElement>> = () => {
	const {channelId, groupId, id: eventAnalysisId} = useParams();
	const {
		attributesState,
		error,
		loading,
		...eventAnalysisData
	} = useEventAnalysisData(eventAnalysisId);

	if (loading) {
		return <Spinner alignCenter key='LOADING_DISPLAY' />;
	}

	if (error) {
		return (
			<ErrorPage
				href={toRoute(Routes.EVENT_ANALYSIS, {
					channelId,
					groupId
				})}
				linkLabel={Liferay.Language.get('go-to-event-analysis')}
				message={Liferay.Language.get(
					'the-analysis-you-are-looking-for-does-not-exist'
				)}
				subtitle={Liferay.Language.get('analysis-not-found')}
			/>
		);
	}

	return (
		<AttributesProvider initialState={attributesState}>
			<BaseEventAnalysisPage
				{...attributesState}
				{...eventAnalysisData}
			/>
		</AttributesProvider>
	);
};

export default EventAnalysisEdit;
