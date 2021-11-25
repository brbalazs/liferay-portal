import * as breadcrumbs from 'shared/util/breadcrumbs';
import BasePage from 'shared/components/base-page';
import React from 'react';
import {Routes, toRoute} from 'shared/util/router';
import {useParams} from 'react-router-dom';

const EventAnalysis: React.FC = () => {
	const {channelId, groupId} = useParams();

	const pageAction = [
		{
			display: 'primary',
			href: toRoute(Routes.EVENT_ANALYSIS_CREATE, {
				channelId,
				groupId
			}),
			label: Liferay.Language.get('create-analysis')
		}
	];

	return (
		<BasePage documentTitle={Liferay.Language.get('event-analysis')}>
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
				<BasePage.Row>
					<BasePage.Header.TitleSection
						title={Liferay.Language.get('event-analysis')}
					/>

					<BasePage.Header.Section>
						<BasePage.Header.PageActions actions={pageAction} />
					</BasePage.Header.Section>
				</BasePage.Row>
			</BasePage.Header>
			{/* TODO: LRAC-9831 Add event analysis list table */}
		</BasePage>
	);
};

export default EventAnalysis;
