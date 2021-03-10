import Constants from 'shared/util/constants';
import EVENT_ATTRIBUTE_DEFINITIONS_QUERY, {
	EventAttributeDefinitionsData,
	EventAttributeDefinitionsVariables
} from 'event-analysis/queries/EventAttributeDefinitionsQuery';
import React from 'react';
import {attributeListColumns} from 'shared/util/table-columns';
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
	const {data, error, loading} = useQuery<
		EventAttributeDefinitionsData,
		EventAttributeDefinitionsVariables
	>(EVENT_ATTRIBUTE_DEFINITIONS_QUERY, {
		variables: {
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
			entityLabel={Liferay.Language.get('attributes').toLowerCase()}
			error={error}
			items={get(
				data,
				['eventAttributeDefinitions', 'eventAttributeDefinitions'],
				[]
			)}
			loading={loading}
			page={page}
			query={query}
			total={get(data, ['eventAttributeDefinitions', 'total'], 0)}
		/>
	);
};

const AttributeList = withBaseResults(withData, {
	defaultOrderBy: orderDefault,
	defaultOrderByField: NAME,
	emptyTitle: Liferay.Language.get('empty-title-pages'),
	getColumns: ({channelId, groupId}) => [
		attributeListColumns.getName({channelId, groupId}),
		attributeListColumns.displayName,
		attributeListColumns.description,
		attributeListColumns.sampleValue,
		attributeListColumns.dataType
	],
	rowIdentifier: 'id',
	showDropdownRangeKey: false
});

export default AttributeList;
