import * as breadcrumbs from 'shared/util/breadcrumbs';
import BasePage from 'shared/components/base-page';
import React from 'react';

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
}) => (
	<BasePage
		className='create-event-analysis-root'
		documentTitle={Liferay.Language.get('event-analysis')}
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
				title={Liferay.Language.get('event-analysis')}
			/>
		</BasePage.Header>

		<BasePage.Body>{'card goes here'}</BasePage.Body>
	</BasePage>
);

export default CreateEventAnalysis;
