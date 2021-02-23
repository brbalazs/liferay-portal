import BasePage from 'settings/components/BasePage';
import EventDetailsCard from '../components/EventDetailsCard';
import React from 'react';
import {getDefinitions, getEvents} from 'shared/util/breadcrumbs';
import {HasModal} from 'shared/types';
import {Routes, setUriQueryValues, toRoute} from 'shared/util/router';
import {useParams} from 'react-router-dom';

interface IEventViewProps extends React.HTMLAttributes<HTMLElement>, HasModal {
	groupId: string;
}

const EventView: React.FC<IEventViewProps> = ({groupId}) => {
	// TODO: Use useQuery hook and the isEditing to open modal => const isEditing = useQuery('edit');
	const {eventId} = useParams();

	// TODO: When able o fetch the event, use the fetched event here instead
	const event = {
		description: 'somedescription',
		displayName: 'View Article',
		name: 'viewArticle'
	};

	const {description, displayName, name} = event;

	const viewEventPageActions = [
		{
			href: setUriQueryValues(
				{edit: true},
				toRoute(Routes.SETTINGS_DEFINITIONS_EVENTS_VIEW, {
					eventId,
					groupId
				})
			),
			label: Liferay.Language.get('edit')
		}
	];

	return (
		<BasePage
			breadcrumbItems={[
				getDefinitions({groupId}),
				getEvents({groupId}),
				{active: true, label: displayName}
			]}
			groupId={groupId}
			pageActions={viewEventPageActions}
			pageDescription={
				description || Liferay.Language.get('no-description')
			}
			pageTitle={name}
			subTitle={displayName}
		>
			<EventDetailsCard eventName={name} groupId={groupId} />
		</BasePage>
	);
};

export default EventView;
