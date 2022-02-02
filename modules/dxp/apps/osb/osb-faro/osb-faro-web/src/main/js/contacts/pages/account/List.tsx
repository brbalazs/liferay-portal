import * as API from 'shared/api';
import BaseListPage from 'contacts/components/BaseListPage';
import React, {FC} from 'react';
import {
	ACCOUNT_TYPE,
	ACTIVITIES_COUNT,
	createOrderIOMap,
	getDefaultSortOrder,
	INDIVIDUAL_COUNT,
	NAME
} from 'shared/util/pagination';
import {accountsListColumns} from 'shared/util/table-columns';
import {FetchSegmentsParams} from 'segment/pages/List';
import {useQueryPagination} from 'shared/hooks';
import {User} from 'shared/util/records';
import {withCurrentUser} from 'shared/hoc';

const getAccountsDataSource: FC<FetchSegmentsParams> = ({
	channelId,
	delta,
	groupId,
	orderIOMap,
	page,
	query
}) =>
	API.accounts.search({
		channelId,
		delta,
		groupId,
		orderIOMap,
		page,
		query
	});

interface IListProps {
	channelId: string;
	currentUser: User;
	groupId: string;
}

const List: React.FC<IListProps> = ({
	channelId,
	currentUser,
	groupId,
	...otherProps
}) => {
	const columns = [
		accountsListColumns.getName({channelId, groupId}),
		accountsListColumns.type,
		accountsListColumns.individualCount,
		accountsListColumns.activitiesCount
	];

	const {delta, orderIOMap, page, query} = useQueryPagination({
		initialOrderIOMap: createOrderIOMap(NAME, getDefaultSortOrder(NAME))
	});

	return (
		<BaseListPage
			{...otherProps}
			columns={columns}
			currentUser={currentUser}
			dataSourceFn={getAccountsDataSource}
			delta={delta}
			emptyStateTitle={Liferay.Language.get(
				'no-accounts-synced-from-data-sources'
			)}
			entityLabel={Liferay.Language.get('accounts')}
			hideNav
			icon='suitcase'
			noResultsConfig={{
				description: Liferay.Language.get(
					'there-is-no-account-data-from-existing-data-sources'
				),
				title: Liferay.Language.get('no-account-data-available')
			}}
			orderByOptions={[
				{
					label: Liferay.Language.get('account-name'),
					value: NAME
				},
				{
					label: Liferay.Language.get('account-type'),
					value: ACCOUNT_TYPE
				},
				{
					label: Liferay.Language.get('individuals'),
					value: INDIVIDUAL_COUNT
				},
				{
					label: Liferay.Language.get('total-activities'),
					value: ACTIVITIES_COUNT
				}
			]}
			orderIOMap={orderIOMap}
			page={page}
			query={query}
			rowIdentifier='id'
		/>
	);
};

export default withCurrentUser(List);
