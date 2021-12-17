import Button from 'shared/components/Button';
import Card from 'shared/components/Card';
import CrossPageSelect from 'shared/hoc/CrossPageSelect';
import DeleteEventAnalysisMutation, {
	DeleteEventAnalysisData,
	DeleteEventAnalysisVariables
} from '../queries/DeleteEventAnalysisMutation';
import EventAnalysisListQuery, {
	EventAnalysisListData,
	EventAnalysisListVariables
} from '../queries/EventAnalysisListQuery';
import Nav from 'shared/components/Nav';
import React from 'react';
import RowActions from 'shared/components/RowActions';
import URLConstants from 'shared/util/url-constants';
import {addAlert} from 'shared/actions/alerts';
import {Alert} from 'shared/types';
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
import {mapListResultsToProps} from 'shared/util/mappers';
import {NameCell} from 'shared/components/table/cell-components';
import {Routes, toRoute} from 'shared/util/router';
import {useMutation, useQuery} from '@apollo/react-hooks';
import {useParams} from 'react-router-dom';
import {useQueryPagination, useQueryRangeSelectors} from 'shared/hooks';
import {
	useSelectionContext,
	withSelectionProvider
} from 'shared/context/selection';

const connector = connect(null, {addAlert, close, open});

type PropsFromRedux = ConnectedProps<typeof connector>;

const EventAnalysisListCard: React.FC<PropsFromRedux> = ({
	addAlert,
	close,
	open
}) => {
	const {selectedItems, selectionDispatch} = useSelectionContext();

	const {delta, orderIOMap, page, query} = useQueryPagination({
		initialOrderIOMap: createOrderIOMap(NAME)
	});

	const {channelId, groupId} = useParams();
	const rangeSelectors = useQueryRangeSelectors();

	const {keywords, size, sort} = getGraphQLVariablesFromPagination({
		delta,
		orderIOMap,
		page,
		query
	});

	const response = useQuery<
		EventAnalysisListData,
		EventAnalysisListVariables
	>(EventAnalysisListQuery, {
		variables: {
			channelId,
			keyword: keywords,
			page: page - 1,
			size,
			sort
		}
	});

	const [deleteEventAnalysis] = useMutation<
		DeleteEventAnalysisData,
		DeleteEventAnalysisVariables
	>(DeleteEventAnalysisMutation);

	const handleDeleteEventAnalysis = (eventAnalysisIds: Array<string>) => {
		const {refetch} = response;

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
							eventAnalysisIds.length
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
							eventAnalysisIds.length
						)}
					</p>
				</div>
			),
			modalVariant: 'modal-warning',
			onClose: close,
			onSubmit: () => {
				deleteEventAnalysis({
					variables: {
						eventAnalysisIds
					}
				})
					.then(() => {
						addAlert({
							alertType: Alert.Types.Success,
							message: Liferay.Language.get(
								'the-segment-has-been-deleted'
							)
						});

						selectionDispatch({
							type: 'clear-all'
						});

						refetch();
					})
					.catch(() => {
						addAlert({
							alertType: Alert.Types.Error,
							message: Liferay.Language.get('error'),
							timeout: false
						});
					});
			},
			submitButtonDisplay: 'warning',
			submitMessage: Liferay.Language.get('delete'),
			title: Liferay.Language.get('deleting-analysis'),
			titleIcon: 'warning-full'
		});
	};

	const renderNav = () => {
		if (!selectedItems.isEmpty()) {
			const eventAnalysisIds = selectedItems.keySeq().toArray();

			return (
				<Nav>
					<Button
						borderless
						display='secondary'
						icon='trash'
						iconAlignment='left'
						onClick={() =>
							handleDeleteEventAnalysis(eventAnalysisIds)
						}
						outline
					/>
				</Nav>
			);
		}
	};

	const renderRowActions = ({data: {id}}) => {
		if (selectedItems.isEmpty()) {
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
					items: result.eventAnalyses.eventAnalyses,
					total: result.eventAnalyses.total
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
