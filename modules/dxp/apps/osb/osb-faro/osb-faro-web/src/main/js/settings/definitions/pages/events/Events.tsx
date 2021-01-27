import BasePage from 'settings/components/BasePage';
import EventsCard from './components/EventsCard';
import React from 'react';
import {getDefinitions} from 'shared/util/breadcrumbs';

interface IEventsProps {
	groupId: string;
}

const Events: React.FC<IEventsProps> = ({groupId}) => (
	<BasePage
		breadcrumbItems={[
			getDefinitions({groupId}),
			{active: true, label: Liferay.Language.get('events')}
		]}
		groupId={groupId}
		pageDescription={Liferay.Language.get(
			'this-is-the-data-model-of-events-sent-to-analytics-cloud'
		)}
		pageTitle={Liferay.Language.get('events')}
	>
		<EventsCard groupId={groupId} />
	</BasePage>
);

export default Events;
