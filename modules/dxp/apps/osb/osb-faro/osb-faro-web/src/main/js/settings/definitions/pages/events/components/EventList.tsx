import React from 'react';
import {eventListColumns} from 'shared/util/table-columns';
import {withBaseResults} from 'shared/hoc';

// TODO: LRAC-7329 Use the graphql query instead of mocked data
const withData = () => WrapperComponent => props => (
	<WrapperComponent {...props}></WrapperComponent>
);

const TableWithData = withBaseResults(withData, {
	emptyDescription:
		'visit-our-documentation-to-learn-how-to-add-custom-events-on-your-site',
	emptyTitle: 'create-some-custom-events',
	getColumns: ({channelId, groupId}) => [
		eventListColumns.getName({channelId, groupId}),
		eventListColumns.displayName,
		eventListColumns.description
	],
	rowIdentifier: 'eventId',
	showDropdownRangeKey: false
});

interface IEventListProps {
	customEvents: boolean;
}

const EventList: React.FC<IEventListProps> = ({
	customEvents = false,
	...props
}) => {
	let items = [
		{
			description: 'mydescription',
			displayName: 'displayNamehere',
			eventId: 'myid1',
			eventType: 'DEFAULT',
			name: 'firstTest'
		},
		{
			description: 'seconddescription',
			displayName: 'seconddisplay',
			eventId: 'myid2',
			eventType: 'DEFAULT',
			name: 'testingtest'
		},
		{
			description: 'mydescription',
			displayName: 'displayNamehere',
			eventId: 'myid3',
			eventType: 'DEFAULT',
			name: 'anothernamet'
		}
	];

	if (customEvents) {
		// TODO: LRAC-7329 modify query to fetch only custom events
		items = items.map(item => ({
			...item,
			displayName: `${item.displayName}CUSTOM`,
			eventType: 'CUSTOM'
		}));
	}

	return <TableWithData {...props} items={items} />;
};

export default EventList;
