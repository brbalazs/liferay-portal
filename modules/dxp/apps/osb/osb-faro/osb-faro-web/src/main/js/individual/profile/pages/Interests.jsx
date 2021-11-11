import * as API from 'shared/api';
import Card from 'shared/components/Card';
import getCN from 'classnames';
import React from 'react';
import SearchableEntityTable from 'shared/components/SearchableEntityTable';
import TextTruncate from 'shared/components/TextTruncate';
import {Individual} from 'shared/util/records';
import {INDIVIDUALS} from 'shared/util/router';
import {interestListColumns} from 'shared/util/table-columns';
import {
	NAME,
	paginationConfig,
	paginationDefaults
} from 'shared/util/pagination';
import {PropTypes} from 'prop-types';
import {Routes, toRoute} from 'shared/util/router';
import {sub} from 'shared/util/lang';

export const TOTAL_DAYS = 90;

function fetchInterests({orderBy, orderByField, ...otherData}) {
	const orderByFields = [{fieldName: orderByField, orderBy}];

	return API.interests.search({
		...otherData,
		orderByFields
	});
}

export class ContributionsCell extends React.Component {
	static propTypes = {
		data: PropTypes.shape({
			relatedPagesCount: PropTypes.number
		}).isRequired
	};

	render() {
		const {
			className,
			data: {relatedPagesCount}
		} = this.props;

		return (
			<td className={getCN('table-cell-expand', className)}>
				<TextTruncate
					title={sub(Liferay.Language.get('x-contributing-pages'), [
						relatedPagesCount
					])}
				/>
			</td>
		);
	}
}

export default class Interests extends React.Component {
	static defaultProps = {
		...paginationDefaults,
		orderByField: NAME
	};

	static propTypes = {
		...paginationConfig,
		channelId: PropTypes.string,
		groupId: PropTypes.string.isRequired,
		id: PropTypes.string.isRequired,
		individual: PropTypes.instanceOf(Individual).isRequired,
		orderBy: PropTypes.string,
		orderByField: PropTypes.string
	};

	render() {
		const {
			channelId,
			delta,
			filterBy,
			groupId,
			id,
			orderBy,
			orderByField,
			page,
			query
		} = this.props;

		return (
			<Card pageDisplay>
				<SearchableEntityTable
					className='interest-history-table'
					columns={[
						interestListColumns.getName({
							groupId,
							id,
							routeFn: ({data: {name}}) =>
								name &&
								toRoute(
									Routes.CONTACTS_INDIVIDUAL_INTEREST_DETAILS,
									{
										channelId,
										groupId,
										id,
										interestId: name
									}
								),
							type: INDIVIDUALS
						}),
						{
							accessor: 'relatedPagesCount',
							cellRenderer: ContributionsCell,
							label: Liferay.Language.get('contributing-pages'),
							sortable: false
						}
					]}
					dataSourceFn={fetchInterests}
					dataSourceParams={{
						contactsEntityId: id,
						groupId,
						interestMax: TOTAL_DAYS
					}}
					delta={Number(delta)}
					entityLabel={Liferay.Language.get('interests')}
					filterBy={filterBy}
					orderBy={orderBy}
					orderByField={orderByField}
					orderByOptions={[
						{
							label: Liferay.Language.get('interest'),
							value: NAME
						}
					]}
					page={Number(page)}
					query={query}
					rowIdentifier='name'
				/>
			</Card>
		);
	}
}
