import * as API from 'shared/api';
import Card from 'shared/components/Card';
import getCN from 'classnames';
import React from 'react';
import SearchableEntityTable from 'shared/components/SearchableEntityTable';
import TextTruncate from 'shared/components/TextTruncate';
import {
	createOrderIOMap,
	getDefaultSortOrder,
	NAME
} from 'shared/util/pagination';
import {INDIVIDUALS} from 'shared/util/router';
import {interestListColumns} from 'shared/util/table-columns';
import {Routes, toRoute} from 'shared/util/router';
import {sub} from 'shared/util/lang';
import {useQueryPagination} from 'shared/hooks';

export const TOTAL_DAYS = 90;

interface IContributionsCellProps {
	className: string;
	data: {relatedPagesCount: number};
}

export const ContributionsCell: React.FC<IContributionsCellProps> = ({
	className,
	data: {relatedPagesCount}
}) => (
	<td className={getCN('table-cell-expand', className)}>
		<TextTruncate
			title={sub(Liferay.Language.get('x-contributing-pages'), [
				relatedPagesCount
			])}
		/>
	</td>
);

interface IInterestsProps {
	channelId: string;
	groupId: string;
	id: string;
}

const Interests: React.FC<IInterestsProps> = ({channelId, groupId, id}) => {
	const {delta, orderIOMap, page, query} = useQueryPagination({
		initialOrderIOMap: createOrderIOMap(NAME, getDefaultSortOrder(NAME))
	});

	return (
		<Card pageDisplay>
			<SearchableEntityTable
				className='interest-history-table'
				columns={[
					interestListColumns.getName({
						channelId,
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
				dataSourceFn={API.interests.search}
				dataSourceParams={{
					contactsEntityId: id,
					groupId,
					interestMax: TOTAL_DAYS
				}}
				delta={delta}
				entityLabel={Liferay.Language.get('interests')}
				orderByOptions={[
					{
						label: Liferay.Language.get('interest'),
						value: NAME
					}
				]}
				orderIOMap={orderIOMap}
				page={page}
				query={query}
				rowIdentifier='name'
			/>
		</Card>
	);
};

export default Interests;
