import * as API from 'shared/api';
import BaseListPage from 'contacts/components/BaseListPage';
import omitDefinedProps from 'shared/util/omitDefinedProps';
import React from 'react';
import {
	ACCOUNT_TYPE,
	ACTIVITIES_COUNT,
	INDIVIDUAL_COUNT,
	NAME,
	paginationConfig,
	paginationDefaults
} from 'shared/util/pagination';
import {ACCOUNTS} from 'shared/util/router';
import {accountsListColumns} from 'shared/util/table-columns';
import {buildOrderByFields} from 'shared/util/pagination';
import {OrderParams, User} from 'shared/util/records';
import {PropTypes} from 'prop-types';
import {withCurrentUser} from 'shared/hoc';

function getAccountsDataSource({
	channelId,
	delta,
	groupId,
	orderBy,
	orderByField,
	page,
	query
}) {
	return API.accounts.search({
		channelId,
		cur: page,
		delta,
		groupId,
		orderByFields: buildOrderByFields(
			new OrderParams({field: orderByField, sortOrder: orderBy}),
			ACCOUNTS
		),
		query
	});
}

export class List extends React.Component {
	static defaultProps = {
		...paginationDefaults,
		orderByField: NAME
	};

	static propTypes = {
		...paginationConfig,
		channelId: PropTypes.string,
		currentUser: PropTypes.instanceOf(User).isRequired,
		groupId: PropTypes.string.isRequired
	};

	getColumns() {
		const {channelId, groupId} = this.props;

		return [
			accountsListColumns.getName({channelId, groupId}),
			accountsListColumns.type,
			accountsListColumns.individualCount,
			accountsListColumns.activitiesCount
		];
	}

	render() {
		const {
			channelId,
			currentUser,
			delta,
			filterBy,
			groupId,
			orderBy,
			orderByField,
			page,
			query,
			...otherProps
		} = this.props;

		return (
			<BaseListPage
				{...omitDefinedProps(otherProps, List.propTypes)}
				channelId={channelId}
				columns={this.getColumns()}
				currentUser={currentUser}
				dataSourceFn={getAccountsDataSource}
				delta={Number(delta)}
				entityLabel={Liferay.Language.get('accounts')}
				filterBy={filterBy}
				groupId={groupId}
				hideNav
				icon='suitcase'
				noResultsConfig={{
					description: Liferay.Language.get(
						'there-is-no-account-data-from-existing-data-sources'
					),
					title: Liferay.Language.get('no-account-data-available')
				}}
				orderBy={orderBy}
				orderByField={orderByField}
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
				page={Number(page)}
				query={query}
				rowIdentifier='id'
			/>
		);
	}
}

export default withCurrentUser(List);
