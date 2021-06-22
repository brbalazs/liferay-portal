import Constants from 'shared/util/constants';
import EVENT_DEFINITIONS_QUERY, {
	EventDefinitionsData,
	EventDefinitionsVariables
} from 'event-analysis/queries/EventDefinitionsQuery';
import React from 'react';
import {eventListColumns} from 'shared/util/table-columns';
import {EventTypes} from 'event-analysis/utils/types';
import {get} from 'lodash';
import {NAME} from 'shared/util/pagination';
import {useQuery} from '@apollo/react-hooks';
import {withBaseResults} from 'shared/hoc';

const {
	pagination: {cur: defaultPage, delta: defaultDelta, orderDefault}
} = Constants;

const withData = () => WrapperComponent => ({
	delta = defaultDelta,
	orderBy,
	orderByField,
	page = defaultPage,
	query,
	...otherProps
}) => {
	const {data, error, loading, refetch} = useQuery<
		EventDefinitionsData,
		EventDefinitionsVariables
	>(EVENT_DEFINITIONS_QUERY, {
		fetchPolicy: 'no-cache',
		variables: {
			eventType: EventTypes.Default,
			keyword: query,
			page: Number(page) - 1,
			size: delta,
			sort: {
				column: orderByField,
				type: orderBy.toUpperCase()
			}
		}
	});

	return (
		<WrapperComponent
			{...otherProps}
			delta={delta}
			error={error}
			items={get(data, ['eventDefinitions', 'eventDefinitions'], [])}
			loading={loading}
			noResultsProps={{
				icon: {border: false, size: 'xxxl', symbol: 'ac-satellite'}
			}}
			page={page}
			query={query}
			refetch={refetch}
			total={get(data, ['eventDefinitions', 'total'], 0)}
		/>
	);
};

const EventList = withBaseResults(withData, {
	defaultOrderBy: orderDefault,
	defaultOrderByField: NAME,
	emptyDescription: Liferay.Language.get(
		'visit-our-documentation-to-learn-how-to-add-custom-events-on-your-site'
	),
	emptyTitle: Liferay.Language.get('create-some-custom-events'),
	getColumns: ({groupId}) => [
		eventListColumns.getName({groupId}),
		eventListColumns.displayName,
		eventListColumns.description,
		eventListColumns.hidden
	],
	rowIdentifier: 'id',
	showDropdownRangeKey: false
});
export default EventList;
