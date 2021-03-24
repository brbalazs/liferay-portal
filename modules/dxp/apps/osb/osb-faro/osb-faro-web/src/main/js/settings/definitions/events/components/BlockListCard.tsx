import Card from 'shared/components/Card';
import Constants from 'shared/util/constants';
import React from 'react';
import {eventListColumns} from 'shared/util/table-columns';
import {get} from 'lodash';
import {mockBlockedCustomEventDefinition} from 'test/data';
import {NAME} from 'shared/util/pagination';
import {range} from 'lodash';
import {withBaseResults} from 'shared/hoc';

const {
	pagination: {cur: defaultPage, delta: defaultDelta, orderDefault}
} = Constants;

const withData = () => WrapperComponent => ({
	delta = defaultDelta,
	// orderBy,
	// orderByField,
	page = defaultPage,
	query,
	...otherProps
}) => {
	// LRAC-7628 Connect BlockListCard to backend request
	const {data, error, loading} = {
		data: {
			blockedCustomEventDefinitions: {
				blockedCustomEventDefinitions: range(5).map(i =>
					mockBlockedCustomEventDefinition(i)
				),
				total: 5
			}
		},
		error: false,
		loading: false
	};

	return (
		<Card pageDisplay>
			<WrapperComponent
				{...otherProps}
				delta={delta}
				error={error}
				items={get(
					data,
					[
						'blockedCustomEventDefinitions',
						'blockedCustomEventDefinitions'
					],
					[]
				)}
				loading={loading}
				noResultsProps={{
					icon: {border: false, size: 'xxxl', symbol: 'ac-satellite'}
				}}
				page={page}
				query={query}
				total={get(data, ['blockedCustomEventDefinitions', 'total'], 0)}
			/>
		</Card>
	);
};

const BlockListCard = withBaseResults(withData, {
	defaultOrderBy: orderDefault,
	defaultOrderByField: NAME,
	emptyTitle: Liferay.Language.get('no-blocked-events-to-report'),
	getColumns: ({timeZoneId}) => [
		eventListColumns.name,
		eventListColumns.lastSeenURL,
		eventListColumns.getLastSeenDate(timeZoneId)
	],
	rowIdentifier: 'id',
	showDropdownRangeKey: false
});

export default BlockListCard;
