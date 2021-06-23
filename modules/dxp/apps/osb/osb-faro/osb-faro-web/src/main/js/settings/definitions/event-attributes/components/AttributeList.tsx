import Constants from 'shared/util/constants';
import EventAttributeDefinitionsQuery, {
	EventAttributeDefinitionsData,
	EventAttributeDefinitionsVariables
} from 'event-analysis/queries/EventAttributeDefinitionsQuery';
import React from 'react';
import {attributeListColumns} from 'shared/util/table-columns';
import {AttributeTypes} from 'event-analysis/utils/types';
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
		EventAttributeDefinitionsData,
		EventAttributeDefinitionsVariables
	>(EventAttributeDefinitionsQuery, {
		variables: {
			keyword: query,
			page: Number(page) - 1,
			size: delta,
			sort: {
				column: orderByField,
				type: orderBy.toUpperCase()
			},
			type: AttributeTypes.Local
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
			refetch={refetch}
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
