import BasePage from 'settings/components/BasePage';
import React from 'react';
import {getDefinitions, getEvents} from 'shared/util/breadcrumbs';
import {HasModal} from 'shared/types';
import {Routes, setUriQueryValues, toRoute} from 'shared/util/router';
import {useParams} from 'react-router-dom';

interface IEventViewProps extends React.HTMLAttributes<HTMLElement>, HasModal {
	groupId: string;
}

const EventView: React.FC<IEventViewProps> = ({groupId}) => {
	const event = {
		description: 'somedescription',
		displayName: 'View Article',
		id: 'myid',
		name: 'viewArticle'
	};

	const {eventId} = useParams();
	// TODO: Use useQuery hook and the isEditing to open modal => const isEditing = useQuery('edit');

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
				{active: true, label: event.displayName}
			]}
			groupId={groupId}
			pageActions={viewEventPageActions}
			pageDescription={
				event.description || Liferay.Language.get('no-description')
			}
			pageTitle={event.name}
			subTitle={event.displayName}
		>
			<div>{`CARD GOES HERE ${eventId}`}</div>
		</BasePage>
	);
};

export default EventView;
