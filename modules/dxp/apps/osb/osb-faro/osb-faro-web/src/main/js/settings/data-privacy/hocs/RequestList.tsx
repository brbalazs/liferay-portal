import Button from 'shared/components/Button';
import Card from 'shared/components/Card';
import Constants from 'shared/util/constants';
import DataControlRequest from '../queries/DataControlRequestMutation';
import Label from 'shared/components/Label';
import moment from 'moment';
import Nav from 'shared/components/Nav';
import React from 'react';
import RequestListQuery from '../queries/RequestListQuery';
import {addAlert} from 'shared/actions/alerts';
import {Alert} from 'shared/types';
import {close, modalTypes, open} from 'shared/actions/modals';
import {compose} from 'redux';
import {connect} from 'react-redux';
import {CREATE_DATE} from 'shared/util/pagination';
import {FilterByType, RouterType} from 'shared/types';
import {formatDateToTimeZone} from 'shared/util/date';
import {
	GDPR_REQUEST_STATUSES,
	GDPR_REQUEST_TYPES,
	LAST_30_DAYS,
	LAST_7_DAYS,
	LAST_90_DAYS
} from 'shared/util/constants';
import {getFormattedTitle} from 'shared/components/NoResultsDisplay';
import {getMapResultToProps} from 'shared/hoc/mappers/metrics';
import {getSafeDisplayValue} from 'shared/util/util';
import {graphql} from '@apollo/react-hoc';
import {mapPropsToOptions} from './mappers/request-list-query';
import {OrderedMap, Set} from 'immutable';
import {
	PERIOD,
	Routes,
	setUriQueryValues,
	STATUSES,
	toRoute,
	TYPES
} from 'shared/util/router';
import {useMutation} from '@apollo/react-hooks';
import {User} from 'shared/util/records';
import {
	useSelectionContext,
	withSelectionProvider
} from 'shared/context/selection';
import {withCrossPageSelect, withFilters, withHistory} from 'shared/hoc';

const {
	pagination: {cur: defaultPage, orderDescending}
} = Constants;

const DATE_FORMAT = 'MMM DD, YYYY';

export const REQUEST_TYPE_LABEL_MAP = {
	[GDPR_REQUEST_TYPES.ACCESS]: Liferay.Language.get('access'),
	[GDPR_REQUEST_TYPES.DELETE]: Liferay.Language.get('delete'),
	[GDPR_REQUEST_TYPES.SUPPRESS]: Liferay.Language.get('suppress'),
	[GDPR_REQUEST_TYPES.UNSUPPRESS]: Liferay.Language.get('unsuppress')
};

export const REQUEST_STATUS_LABEL_MAP = {
	[GDPR_REQUEST_STATUSES.COMPLETED]: Liferay.Language.get('done'),
	[GDPR_REQUEST_STATUSES.ERROR]: Liferay.Language.get('error'),
	[GDPR_REQUEST_STATUSES.EXPIRED]: Liferay.Language.get('done'),
	[GDPR_REQUEST_STATUSES.PENDING]: Liferay.Language.get('pending'),
	[GDPR_REQUEST_STATUSES.RUNNING]: Liferay.Language.get('running')
};

export const REQUEST_STATUS_DISPLAY_MAP = {
	[GDPR_REQUEST_STATUSES.COMPLETED]: 'success',
	[GDPR_REQUEST_STATUSES.ERROR]: 'danger',
	[GDPR_REQUEST_STATUSES.EXPIRED]: 'success',
	[GDPR_REQUEST_STATUSES.PENDING]: 'secondary',
	[GDPR_REQUEST_STATUSES.RUNNING]: 'info'
};

export const FILTER_BY_OPTIONS = [
	{
		key: STATUSES,
		label: Liferay.Language.get('status'),
		values: [
			{
				label: REQUEST_STATUS_LABEL_MAP.COMPLETED,
				value: GDPR_REQUEST_STATUSES.COMPLETED
			}
		]
	},
	{
		key: TYPES,
		label: Liferay.Language.get('type'),
		values: [
			{
				label: REQUEST_TYPE_LABEL_MAP.ACCESS,
				value: GDPR_REQUEST_TYPES.ACCESS
			},
			{
				label: REQUEST_TYPE_LABEL_MAP.DELETE,
				value: GDPR_REQUEST_TYPES.DELETE
			},
			{
				label: REQUEST_TYPE_LABEL_MAP.SUPPRESS,
				value: GDPR_REQUEST_TYPES.SUPPRESS
			}
		]
	},
	{
		key: PERIOD,
		label: Liferay.Language.get('period'),
		type: 'radio' as const,
		values: [
			{
				label: Liferay.Language.get('last-seven-days'),
				value: LAST_7_DAYS
			},
			{
				label: Liferay.Language.get('last-30-days'),
				value: LAST_30_DAYS
			},
			{
				label: Liferay.Language.get('last-90-days'),
				value: LAST_90_DAYS
			}
		]
	}
];

export const getTodaysDate = () => moment().utc();

const isDisabled = ({
	completeDate,
	status
}: {
	completeDate: string;
	status: GDPR_REQUEST_STATUSES;
}): boolean => !completeDate || status !== GDPR_REQUEST_STATUSES.COMPLETED;

/**
 * Function for searching and filtering requests.
 */
export const searchSelectedFn = ({
	filterBy,
	items,
	query
}: {
	filterBy: FilterByType;
	items: OrderedMap<any, any>;
	query: string;
}): OrderedMap<any, any> => {
	let result: OrderedMap<any, any>;

	const statuses = filterBy.get(STATUSES, Set()).toArray();
	const requestTypes = filterBy.get(TYPES, Set()).toArray();
	const period = filterBy.get(PERIOD, Set()).toArray();

	result = items.filter(item =>
		Object.values(item).some((value: any) =>
			String(getSafeDisplayValue(value, ''))
				.toLowerCase()
				.match(query.toLowerCase())
		)
	) as OrderedMap<any, any>;

	if (statuses.length) {
		result = result.filter(({status}) =>
			statuses.includes(status)
		) as OrderedMap<any, any>;
	}

	if (requestTypes.length) {
		result = result.filter(({type}) =>
			requestTypes.includes(type)
		) as OrderedMap<any, any>;
	}

	if (period.length) {
		const dateLimit = getTodaysDate().subtract(period[0], 'days');

		result = result.filter(({createDate}) =>
			moment(createDate).isAfter(dateLimit)
		) as OrderedMap<any, any>;
	}

	return result;
};

const withData = () =>
	graphql(RequestListQuery, {
		options: (props: any) => ({
			...mapPropsToOptions(props),
			fetchPolicy: 'no-cache'
		}),
		props: getMapResultToProps(
			({dataControlTasks: {dataControlTasks, total}}) => ({
				items: dataControlTasks,
				total
			})
		)
	});

const withQueryOptions = Component => ({
	addAlert,
	close,
	currentUser,
	history,
	open,
	refetch,
	...otherProps
}: IRequestListProps & {
	delta: string;
	groupId: string;
	refetch: (options: {variables: {[key: string]: any}}) => Promise<any>;
}) => {
	const {selectedItems} = useSelectionContext();

	const [addDataControlTask] = useMutation(DataControlRequest);

	const {delta, groupId} = otherProps;

	const handleOpenNewRequestModal = () =>
		open(modalTypes.NEW_REQUEST_MODAL, {
			groupId,
			onClose: close,
			onSubmit: ({
				emailAddresses,
				fileName,
				types
			}: {
				emailAddresses?: string[];
				fileName?: string;
				types: string[];
			}) => {
				addDataControlTask({
					variables: {
						emailAddresses,
						fileName,
						ownerId: currentUser.id,
						types
					}
				})
					.then(() => {
						addAlert({
							alertType: Alert.Types.SUCCESS,
							message: Liferay.Language.get(
								'requests-have-been-successfully-submitted'
							)
						});

						refetch({
							variables: {
								keywords: '',
								size: delta,
								sort: {
									column: CREATE_DATE,
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
									orderByField: CREATE_DATE,
									page: defaultPage
								},
								toRoute(
									Routes.SETTINGS_DATA_PRIVACY_REQUEST_LOG,
									{
										groupId
									}
								)
							)
						);

						close();
					})
					.catch(() =>
						addAlert({
							alertType: Alert.Types.ERROR,
							message: Liferay.Language.get(
								'there-was-an-error-processing-your-request.-please-try-again'
							),
							timeout: false
						})
					);
			}
		});

	return (
		<Component
			{...otherProps}
			renderNav={() => (
				<Nav>
					<Nav.Item>
						{selectedItems.size ? (
							<Button
								className='nav-btn'
								display='primary'
								download
								externalLink
								href={`/o/proxy/download/data-control-tasks?projectGroupId=${groupId}&filter=(id eq ${selectedItems
									.map(({id}) => id)
									.join(' or id eq ')})`}
							>
								{Liferay.Language.get('download-all')}
							</Button>
						) : (
							<Button
								className='nav-btn'
								display='primary'
								onClick={handleOpenNewRequestModal}
							>
								{Liferay.Language.get('create-request')}
							</Button>
						)}
					</Nav.Item>
				</Nav>
			)}
		/>
	);
};

const RequestListWithData = withCrossPageSelect(withData, {
	defaultOrderByField: CREATE_DATE,
	emptyTitle: getFormattedTitle(Liferay.Language.get('requests')),
	getColumns: ({timeZoneId}) => [
		{
			accessor: 'batchId',
			label: Liferay.Language.get('request-id'),
			title: true
		},
		{
			accessor: 'emailAddress',
			className: 'table-cell-expand',
			label: Liferay.Language.get('email')
		},
		{
			accessor: 'type',
			dataFormatter: (type: GDPR_REQUEST_TYPES) =>
				REQUEST_TYPE_LABEL_MAP[type],
			label: Liferay.Language.get('request-type')
		},
		{
			accessor: CREATE_DATE,
			dataFormatter: (date: string) => formatDateToTimeZone(date, DATE_FORMAT, timeZoneId),
			label: Liferay.Language.get('requested-date')
		},
		{
			accessor: 'status',
			cellRenderer: ({
				data: {status}
			}: {
				data: {status: GDPR_REQUEST_STATUSES};
			}) => (
				<td>
					<Label
						className='status'
						display={REQUEST_STATUS_DISPLAY_MAP[status]}
						size='lg'
						uppercase
					>
						{REQUEST_STATUS_LABEL_MAP[status]}
					</Label>
				</td>
			),
			label: Liferay.Language.get('request-status')
		}
	],
	page: false,
	primary: true,
	showDropdownRangeKey: false,
	withQueryOptions
});

interface IRequestListProps {
	addAlert: Alert.AddAlert;
	close: () => void;
	currentUser: User;
	filterBy: FilterByType;
	history: {
		push: (string) => void;
	};
	open: (modalType: string, options: object) => void;
	router: RouterType;
	timeZoneId: string;
}

const RequestList: React.FC<IRequestListProps> = ({
	filterBy,
	router,
	...otherProps
}) => {
	const {
		params: {groupId}
	} = router;

	return (
		<Card className='request-list-root' pageDisplay>
			<RequestListWithData
				{...otherProps}
				checkDisabled={isDisabled}
				defaultOrderBy={orderDescending}
				defaultOrderByField={CREATE_DATE}
				entityLabel={Liferay.Language.get('requests')}
				filterBy={filterBy}
				groupId={groupId}
				renderInlineRowActions={({
					data: {id, status},
					itemsSelected
				}: {
					data: {
						completeDate: string;
						id: string;
						status: GDPR_REQUEST_STATUSES;
					};
					itemsSelected: boolean;
				}) => {
					if (status === GDPR_REQUEST_STATUSES.EXPIRED) {
						return (
							<b>{Liferay.Language.get('download-expired')}</b>
						);
					}

					return (
						status === GDPR_REQUEST_STATUSES.COMPLETED && (
							<Button
								disabled={itemsSelected}
								display='secondary'
								download
								externalLink
								href={`/o/proxy/download/data-control-tasks/${id}?projectGroupId=${groupId}`}
								size='sm'
							>
								{Liferay.Language.get('download')}
							</Button>
						)
					);
				}}
				router={router}
				searchSelectedFn={searchSelectedFn}
				toolbarProps={{
					filterBy,
					filterByOptions: FILTER_BY_OPTIONS,
					flatFilter: true
				}}
			/>
		</Card>
	);
};

export default compose<any>(
	withSelectionProvider,
	withFilters({destructured: false, filterFields: [STATUSES, TYPES, PERIOD]}),
	connect(
		null,
		{addAlert, close, open}
	),
	withHistory
)(RequestList);
