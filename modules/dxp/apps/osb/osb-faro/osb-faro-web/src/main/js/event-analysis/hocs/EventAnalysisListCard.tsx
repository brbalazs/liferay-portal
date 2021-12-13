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
import {close, modalTypes, open} from 'shared/actions/modals';
import {compose} from 'shared/hoc';
import {connect, ConnectedProps} from 'react-redux';
import {CreatedByCell} from 'shared/components/table/cell-components';
import {
	createOrderIOMap,
	getGraphQLVariablesFromPagination,
	NAME,
	USER_NAME
} from 'shared/util/pagination';
import {getPluralMessage, sub} from 'shared/util/lang';
import {getSafeRangeSelectors} from 'shared/util/util';
import {mapListResultsToProps} from 'shared/util/mappers';
import {Modal} from 'shared/types';
import {NameCell} from 'shared/components/table/cell-components';
import {noop} from 'lodash';
import {Routes, toRoute} from 'shared/util/router';
import {useParams} from 'react-router-dom';
import {useQuery} from '@apollo/react-hooks';
import {useQueryPagination, useQueryRangeSelectors} from 'shared/hooks';
import {
	useSelectionContext,
	withSelectionProvider
} from 'shared/context/selection';

const connector = connect(null, {close, open});

type PropsFromRedux = ConnectedProps<typeof connector>;

const EventAnalysisListCard: React.FC<PropsFromRedux> = ({close, open}) => {
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

	const handleDeleteEventAnalysis = ids => {
		open(modalTypes.CONFIRMATION_MODAL, {
			message: (
				<div>
					<h4 className='text-secondary'>
						{getPluralMessage(
							Liferay.Language.get(
								'are-you-sure-you-want-to-delete-this-analysis'
							),
							Liferay.Language.get(
								'are-you-sure-you-want-to-delete-these-analyses'
							),
							ids.length
						)}
					</h4>

					<p>
						{getPluralMessage(
							Liferay.Language.get(
								'you-will-lose-all-data-related-to-this-analysis.-you-will-not-be-able-to-undo-this-operation'
							),
							Liferay.Language.get(
								'you-will-lose-all-data-related-to-these-analyses.-you-will-not-be-able-to-undo-this-operation'
							),
							ids.length
						)}
					</p>
				</div>
			),
			modalVariant: 'modal-warning',
			onClose: close,
			// TODO: LRAC-9837 Add GraphQL mutation to delete event analysis
			onSubmit: noop,
			submitButtonDisplay: 'warning',
			submitMessage: Liferay.Language.get('delete'),
			title: Liferay.Language.get('deleting-analysis'),
			titleIcon: 'warning-full'
		});
	};

	const renderNav = () => {
		if (!selectedItems.isEmpty()) {
			const ids = selectedItems.keySeq().toArray();

			return (
				<Nav>
					<Button
						borderless
						display='secondary'
						icon='trash'
						iconAlignment='left'
						onClick={() => handleDeleteEventAnalysis(ids)}
						outline
					/>
				</Nav>
			);
		}
	};

	const renderRowActions = ({data: {id}}) => {
		if (selectedItems.isEmpty()) {
			// TODO: LRAC-9837 Add GraphQL mutation to delete event analysis
			return (
				<RowActions
					quickActions={[
						{
							iconSymbol: 'trash',
							label: Liferay.Language.get('delete'),
							onClick: () => handleDeleteEventAnalysis([id])
						}
					]}
				/>
			);
		}
	};

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

export default compose(withSelectionProvider, connector)(EventAnalysisListCard);
