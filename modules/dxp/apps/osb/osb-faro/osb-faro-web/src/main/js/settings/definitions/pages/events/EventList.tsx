import React from 'react';
import {eventListColumns} from 'shared/util/table-columns';
import {withBaseResults} from 'shared/hoc';

// TODO: Use the graphql query instead of mocked data
const withData = () => WrapperComponent => props => {
	const items = [
		{
			description: 'mydescription',
			displayName: 'displayNamehere',
			eventId: 'myid1',
			eventType: 'TYPE',
			name: 'firstTest'
		},
		{
			description: 'seconddescription',
			displayName: 'seconddisplay',
			eventId: 'myid2',
			eventType: 'TYPE2',
			name: 'testingtest'
		},
		{
			description: 'mydescription',
			displayName: 'displayNamehere',
			eventId: 'myid3',
			eventType: 'TYPE3',
			name: 'anothernamet'
		}
	];

	return <WrapperComponent {...props} items={items}></WrapperComponent>;
};

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

const EventList = ({customEvents, ...props}) => {
	if (customEvents) {
		// TODO: modify query to fetch only custom events
	}

	return <TableWithData {...props} />;
};

export default EventList;
