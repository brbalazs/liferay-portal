import * as breadcrumbs from 'shared/util/breadcrumbs';
import BasePage from 'shared/components/base-page';
import EventAnalysisEditor from '../components/event-analysis-editor';
import EventAnalysisToolbar from '../components/EventAnalysisToolbar';
import React, {useState} from 'react';
import {DEVELOPER_MODE} from 'shared/util/constants';

type RouterParams = {
	channelId: string;
	groupId: string;
};

type Router = {
	params: RouterParams;
	query: object;
};

interface ICreateEventAnalysisProps
	extends React.HTMLAttributes<HTMLDivElement> {
	router: Router;
}

const CreateEventAnalysis: React.FC<ICreateEventAnalysisProps> = ({
	router: {
		params: {channelId, groupId}
	}
}) => {
	const [eventAnalysisName, setEventAnalysisName] = useState<string>('');

	return (
		<BasePage
			className='create-event-analysis-root'
			documentTitle={Liferay.Language.get('events')}
		>
			<BasePage.Header
				breadcrumbs={[
					breadcrumbs.getHome({
						channelId,
						groupId,
						label: Liferay.Language.get('home')
					})
				]}
				groupId={groupId}
			>
				<BasePage.Header.TitleSection
					title={Liferay.Language.get('events')}
				/>
			</BasePage.Header>

			{/* TODO: LRAC-9959 Remove condition after deleting feature flag */}
			{DEVELOPER_MODE && (
				<BasePage.SubHeader>
					<EventAnalysisToolbar
						name={eventAnalysisName}
						onSubmit={({name}) => setEventAnalysisName(name)}
					/>
				</BasePage.SubHeader>
			)}

			{/* TODO: LRAC-9841 Create onSubmit on EventAnalysisEditor to save it */}
			<BasePage.Body>
				<EventAnalysisEditor channelId={channelId} />
			</BasePage.Body>
		</BasePage>
	);
};

export default CreateEventAnalysis;
