import Button from 'shared/components/Button';
import Card from 'shared/components/Card';
import Constants, {
	jobRunDataPeriods,
	jobRunFrequencies,
	jobStatuses,
	jobTypes
} from 'shared/util/constants';
import Label from 'shared/components/Label';
import Nav from 'shared/components/Nav';
import React from 'react';
import RecommendationListQuery from '../queries/RecommendationListQuery';
import {
	ACTION_TYPES,
	useSelectionContext,
	withSelectionProvider
} from 'shared/context/selection';
import {addAlert} from 'shared/actions/alerts';
import {Alert, Modal, RouterType} from 'shared/types';
import {close, modalTypes, open} from 'shared/actions/modals';
import {compose} from 'redux';
import {connect} from 'react-redux';
import {formatUTCDate} from 'shared/util/date';
import {getFormattedTitle} from 'shared/components/NoResultsDisplay';
import {
	getMapPropsToOptions,
	getMapResultToProps
} from 'shared/hoc/mappers/metrics';
import {graphql} from '@apollo/react-hoc';
import {
	JOB_RUN_DATA_PERIODS_LABEL_MAP,
	JOB_RUN_FREQUENCIES_LABEL_MAP,
	JOB_STATUSES_DISPLAY_MAP,
	JOB_STATUSES_LABEL_MAP,
	JOB_TYPES_LABEL_MAP
} from '../utils/utils';
import {NAME} from 'shared/util/pagination';
import {NameCell} from 'shared/components/table/cell-components';
import {RECOMMENDATION_DELETE_MUTATION} from '../queries/RecommendationMutation';
import {Routes, setUriQueryValues, toRoute} from 'shared/util/router';
import {sub} from 'shared/util/lang';
import {useMutation} from '@apollo/react-hooks';
import {User} from 'shared/util/records';
import {withCrossPageSelect, withCurrentUser} from 'shared/hoc';

const {
	pagination: {cur: defaultPage, orderDescending}
} = Constants;

interface IRecommendationListProps {
	addAlert: Alert.AddAlert;
	close: Modal.close;
	currentUser: User;
	groupId: string;
	history: {
		push: (string) => void;
	};
	open: Modal.open;
	router: RouterType;
}

const withData = () =>
	graphql(RecommendationListQuery, {
		options: (props: any) => ({
			...getMapPropsToOptions(RecommendationListQuery)(props),
			fetchPolicy: 'no-cache'
		}),
		props: getMapResultToProps(({jobs: {jobs, total}}) => ({
			items: jobs,
			total
		}))
	});

const withQueryOptions = Component => ({
	addAlert,
	close,
	currentUser,
	history,
	open,
	refetch,
	...otherProps
}: IRecommendationListProps & {
	delta: string;
	groupId: string;
	refetch: (options: {variables: {[key: string]: any}}) => Promise<any>;
}) => {
	const {selectedItems, selectionDispatch} = useSelectionContext();

	const [deleteRecommendationJobs] = useMutation(
		RECOMMENDATION_DELETE_MUTATION
	);

	const {delta, groupId} = otherProps;

	const singleSelectedItem =
		selectedItems.size === 1 ? selectedItems.first() : null;

	const selectedItemsCount = selectedItems.size;

	const confirmationMessage = singleSelectedItem
		? sub(
				Liferay.Language.get(
					'delete-x-and-its-historical-training-output-data'
				),
				[singleSelectedItem.name]
		  )
		: sub(
				Liferay.Language.get(
					'delete-x-models-and-their-historical-training-output-data'
				),
				[selectedItemsCount]
		  );

	const handleSubmit = () => {
		deleteRecommendationJobs({
			variables: {
				jobIds: selectedItems.map(({id}) => id).toArray()
			}
		})
			.then(() => {
				const successMessage = singleSelectedItem
					? sub(Liferay.Language.get('x-has-been-deleted'), [
							singleSelectedItem.name
					  ])
					: sub(Liferay.Language.get('x-models-have-been-deleted'), [
							selectedItemsCount
					  ]);

				addAlert({
					alertType: Alert.Types.SUCCESS,
					message: successMessage as string
				});

				selectionDispatch({type: ACTION_TYPES.clearAll});

				refetch({
					variables: {
						keywords: '',
						size: delta,
						sort: {
							column: NAME,
							type: orderDescending.toUpperCase()
						},
						start: 0
					}
				});

				history.push(
					setUriQueryValues(
						{
							keywords: '',
							orderBy: orderDescending,
							orderByField: NAME,
							page: defaultPage
						},
						toRoute(Routes.SETTINGS_RECOMMENDATIONS, {
							groupId
						})
					)
				);
			})
			.catch(() => {
				addAlert({
					alertType: Alert.Types.ERROR,
					message: Liferay.Language.get(
						'there-was-an-error-processing-your-request.-please-try-again'
					),
					timeout: false
				});
			});
	};

	return (
		<Component
			{...otherProps}
			renderNav={() => {
				if (!currentUser.isAdmin()) {
					return null;
				}

				if (selectedItemsCount) {
					return (
						<Nav>
							<Nav.Item>
								{
									<Button
										borderless
										display='secondary'
										onClick={() => {
											open(
												modalTypes.CONFIRMATION_MODAL,
												{
													message: (
														<div>
															<h4 className='text-secondary'>
																{
																	confirmationMessage
																}
															</h4>

															<p>
																{singleSelectedItem
																	? Liferay.Language.get(
																			'components-using-this-model-will-need-to-be-reconfigured'
																	  )
																	: Liferay.Language.get(
																			'components-using-these-models-will-need-to-be-reconfigured'
																	  )}
															</p>
														</div>
													),
													modalVariant:
														'modal-warning',
													onClose: close,
													onSubmit: handleSubmit,
													submitButtonDisplay:
														'warning',
													submitMessage: Liferay.Language.get(
														'delete'
													),
													title: sub(
														Liferay.Language.get(
															'deleting-x'
														),
														[
															singleSelectedItem
																? singleSelectedItem.name
																: sub(
																		Liferay.Language.get(
																			'x-models'
																		),
																		[
																			selectedItemsCount
																		]
																  )
														]
													),
													titleIcon: 'warning-full'
												}
											);
										}}
										outline
									>
										{Liferay.Language.get('delete')}
									</Button>
								}
							</Nav.Item>
						</Nav>
					);
				}

				return (
					<Nav>
						<Nav.Item>
							{
								<Button
									className='nav-btn'
									display='primary'
									href={toRoute(
										Routes.SETTINGS_RECOMMENDATIONS_CREATE_ITEM_SIMILARITY_MODEL,
										{groupId}
									)}
								>
									{Liferay.Language.get('new-model')}
								</Button>
							}
						</Nav.Item>
					</Nav>
				);
			}}
		/>
	);
};

const RecommendationListWithData = withCrossPageSelect(withData, {
	defaultOrderByField: NAME,
	emptyTitle: getFormattedTitle(
		Liferay.Language.get('recommendations').toLowerCase()
	),
	getColumns: ({groupId}) => [
		{
			accessor: 'name',
			cellRenderer: NameCell,
			cellRendererProps: {
				routeFn: ({data: {id}}) =>
					toRoute(Routes.SETTINGS_RECOMMENDATION_MODEL_VIEW, {
						groupId,
						jobId: id
					})
			},
			className: 'table-cell-expand',
			label: Liferay.Language.get('name')
		},
		{
			accessor: 'type',
			dataFormatter: (type: jobTypes) => JOB_TYPES_LABEL_MAP[type],
			label: Liferay.Language.get('training-model')
		},
		{
			accessor: 'runDataPeriod',
			dataFormatter: (type: jobRunDataPeriods) =>
				JOB_RUN_DATA_PERIODS_LABEL_MAP[type],
			label: Liferay.Language.get('training-period')
		},
		{
			accessor: 'runFrequency',
			dataFormatter: (type: jobRunFrequencies) =>
				JOB_RUN_FREQUENCIES_LABEL_MAP[type],
			label: Liferay.Language.get('training-frequency')
		},
		{
			accessor: 'runDate',
			dataFormatter: (date: string) => formatUTCDate(date, 'MM DD, YYYY'),
			label: Liferay.Language.get('last-trained')
		},
		{
			accessor: 'status',
			cellRenderer: ({
				className,
				data: {status}
			}: {
				className: string;
				data: {status: jobStatuses};
			}) => (
				<td className={className}>
					<Label
						className='status'
						display={JOB_STATUSES_DISPLAY_MAP[status]}
						size='lg'
						uppercase
					>
						{JOB_STATUSES_LABEL_MAP[status]}
					</Label>
				</td>
			),
			label: Liferay.Language.get('status')
		}
	],
	page: false,
	primary: true,
	rowIdentifier: 'id',
	showDropdownRangeKey: false,
	withQueryOptions
});

const RecommendationList: React.FC<IRecommendationListProps> = ({
	groupId,
	router,
	...otherProps
}) => (
	<Card className='recommendations-list-root' pageDisplay>
		<RecommendationListWithData
			{...otherProps}
			defaultOrderBy={orderDescending}
			defaultOrderByField={NAME}
			entityLabel={Liferay.Language.get('recommendations')}
			groupId={groupId}
			router={router}
		/>
	</Card>
);

export default compose<any>(
	withCurrentUser,
	withSelectionProvider,
	connect(
		null,
		{addAlert, close, open}
	)
)(RecommendationList);
