import Button from 'shared/components/Button';
import Card from 'shared/components/Card';
import CrossPageSelect from 'shared/hoc/CrossPageSelect';
import EventAnalysisListQuery, {
	EventAnalysisListData,
	EventAnalysisListVariables
} from '../queries/EventAnalysisListQuery';
import Nav from 'shared/components/Nav';
import React from 'react';
import RowActions from 'shared/components/RowActions';
import URLConstants from 'shared/util/url-constants';
import {compose} from 'shared/hoc';
import {CreatedByCell} from 'shared/components/table/cell-components';
import {
	createOrderIOMap,
	getGraphQLVariablesFromPagination,
	NAME,
	USER_NAME
} from 'shared/util/pagination';
import {getSafeRangeSelectors} from 'shared/util/util';
import {mapListResultsToProps} from 'shared/util/mappers';
import {NameCell} from 'shared/components/table/cell-components';
import {noop} from 'lodash';
import {Routes, toRoute} from 'shared/util/router';
import {sub} from 'shared/util/lang';

import {useParams} from 'react-router-dom';
import {useQuery} from '@apollo/react-hooks';
import {useQueryPagination, useQueryRangeSelectors} from 'shared/hooks';
import {
	useSelectionContext,
	withSelectionProvider
} from 'shared/context/selection';

const EventAnalysisListCard: React.FC = () => {
	const {selectedItems} = useSelectionContext();

	const {delta, orderIOMap, page, query} = useQueryPagination({
		initialOrderIOMap: createOrderIOMap(NAME)
	});

	const {channelId, groupId} = useParams();
	const rangeSelectors = useQueryRangeSelectors();

	const response = useQuery<
		EventAnalysisListData,
		EventAnalysisListVariables
	>(EventAnalysisListQuery, {
		variables: {
			channelId,
			...getGraphQLVariablesFromPagination({
				delta,
				orderIOMap,
				page,
				query
			}),
			...getSafeRangeSelectors(rangeSelectors)
		}
	});

	const renderNav = () => {
		if (!selectedItems.isEmpty()) {
			// TODO: LRAC-9837 Add GraphQL mutation to delete event analysis
			return (
				<Nav>
					<Button
						borderless
						display='secondary'
						icon='trash'
						iconAlignment='left'
						onClick={noop}
						outline
					/>
				</Nav>
			);
		}
	};

	const renderRowActions = () => (
		<RowActions
			quickActions={[
				{
					iconSymbol: 'trash',
					label: Liferay.Language.get('delete'),
					// TODO: LRAC-9837 Add GraphQL mutation to delete event analysis
					onClick: noop
				}
			]}
		/>
	);

	return (
		<Card className='event-analysis-list-root' pageDisplay>
			<CrossPageSelect
				{...mapListResultsToProps(response, result => ({
					items: result.eventAnalysisList.eventAnalysis,
					total: result.eventAnalysisList.total
				}))}
				columns={[
					{
						accessor: 'name',
						cellRenderer: NameCell,
						cellRendererProps: {
							routeFn: ({data: {id}}) =>
								toRoute(Routes.EVENT_ANALYSIS_EDIT, {
									channelId,
									groupId,
									id
								})
						},
						className: 'table-cell-expand',
						label: Liferay.Language.get('name')
					},
					{
						accessor: 'userName',
						cellRenderer: CreatedByCell,
						label: Liferay.Language.get('created-by')
					}
				]}
				delta={delta}
				entityLabel={Liferay.Language.get('event-analysis')}
				legacyDropdownRangeKey={false}
				noResultsProps={{
					description: sub(
						Liferay.Language.get('empty-message-event-analysis'),
						[
							<a
								href={
									URLConstants.EventAnalysisDocumentationLink
								}
								key='DOCUMENTATION'
								target='_blank'
							>
								{Liferay.Language.get(
									'documentation'
								).toLowerCase()}
							</a>
						],
						false
					),
					icon: {
						border: false,
						size: 'xxxl',
						symbol: 'ac-satellite'
					},
					title: Liferay.Language.get('no-analysis-found')
				}}
				orderByOptions={[
					{
						label: Liferay.Language.get('name'),
						value: NAME
					},
					{
						label: Liferay.Language.get('created-by'),
						value: USER_NAME
					}
				]}
				orderIOMap={orderIOMap}
				page={page}
				query={query}
				rangeSelectors={rangeSelectors}
				renderNav={renderNav}
				renderRowActions={renderRowActions}
				rowIdentifier='id'
				showCheckbox
				showFilterAndOrder
			/>
		</Card>
	);
};

export default compose(withSelectionProvider)(EventAnalysisListCard);
