import * as API from 'shared/api';
import BaseListPage from 'contacts/components/BaseListPage';
import BasePage from 'shared/components/base-page';
import ClayButton from '@clayui/button';
import Promise from 'metal-promise';
import React, {useContext, useEffect, useRef, useState} from 'react';
import RowActions from 'shared/components/RowActions';
import {
	ActionType,
	UnassignedSegmentsContext
} from 'shared/context/unassignedSegments';
import {
	ACTIVITIES_COUNT,
	INDIVIDUAL_COUNT,
	NAME,
	paginationDefaults,
	USER_NAME
} from 'shared/util/pagination';
import {addAlert} from 'shared/actions/alerts';
import {Alert, Modal} from 'shared/types';
import {ALERT_CONFIG_MAP, AlertTypes} from 'shared/components/Alert';
import {close, modalTypes, open} from 'shared/actions/modals';
import {compose, withCurrentUser, withFilters} from 'shared/hoc';
import {connect} from 'react-redux';
import {Link} from 'react-router-dom';
import {
	Routes,
	SEGMENT_STATE,
	SEGMENTS,
	setUriQueryValue,
	toRoute
} from 'shared/util/router';
import {segmentsListColumns} from 'shared/util/table-columns';
import {SegmentStates, SegmentTypes} from 'shared/util/constants';
import {Set} from 'immutable';
import {setUriQueryValues} from 'shared/util/router';
import {sub} from 'shared/util/lang';
import {User} from 'shared/util/records';

interface FetchSegmentsParams {
	channelId: string;
	delta?: string | number;
	filterBy: Map<string, Set<string>>;
	groupId: string;
	orderBy: string;
	orderByField: string;
	page: string | number;
	query: string;
}

function fetchSegments(params: FetchSegmentsParams): any {
	const {
		channelId,
		delta,
		filterBy,
		groupId,
		orderBy,
		orderByField,
		page,
		query
	} = params;

	const stateFilterISet = filterBy.get('state') || Set();

	return API.individualSegment.search({
		channelId,
		delta: delta as number,
		groupId,
		orderByFields: [
			{
				fieldName: orderByField,
				orderBy,
				system: true
			}
		],
		page: page as number,
		query,
		state: stateFilterISet.first()
	});
}

function fetchDisabledSegments(channelId: string, groupId: string): any {
	return API.individualSegment.search({
		channelId,
		delta: 1,
		groupId,
		state: SegmentStates.Disabled
	});
}

interface IListProps {
	addAlert: Alert.AddAlert;
	channelId: string;
	close: Modal.close;
	currentUser: User;
	delta?: string | number;
	filterBy?: Map<string, Set<string>>;
	groupId: string;
	history: any;
	open: Modal.open;
	orderBy?: string;
	orderByField?: string;
	page?: string | number;
	query?: string;
	timeZoneId: string;
}

export const List: React.FC<IListProps> = ({
	addAlert,
	channelId,
	close,
	currentUser,
	delta = paginationDefaults.delta,
	filterBy = paginationDefaults.filterBy,
	groupId,
	history,
	open,
	orderBy = paginationDefaults.orderBy,
	orderByField = NAME,
	page = paginationDefaults.page,
	query = paginationDefaults.query,
	timeZoneId
}) => {
	const [alerts, setAlerts] = useState([]);
	const _tableRef = useRef<any>();
	const _disableSegmentsRequestRef = useRef<Promise>();
	const {
		showUnassignedAlert,
		unassignedSegments,
		unassignedSegmentsDispatch
	} = useContext(UnassignedSegmentsContext);

	useEffect(() => {
		_disableSegmentsRequestRef.current = getDisabledSegmentsAlert();

		return () => _disableSegmentsRequestRef.current.cancel();
	}, []);

	const getDisabledSegmentsAlert = () =>
		fetchDisabledSegments(channelId, groupId).then(({total}) => {
			if (total) {
				setAlerts(() => handleDisabledSegmentsAlert());
			}
		});

	const getAlerts = () =>
		[
			...alerts,
			showUnassignedAlert &&
				unassignedSegments.length &&
				handleUnassignedSegmentsAlert()
		].filter(Boolean);

	const handleDisabledSegmentsAlert = () => [
		{
			message: sub(
				Liferay.Language.get(
					'some-of-your-segments-are-disabled-because-a-data-source-has-been-removed-x'
				),
				[
					<Link
						key='DISABLED_SEGMENTS'
						to={setUriQueryValue(
							window.location.href,
							SEGMENT_STATE,
							SegmentStates.Disabled
						)}
					>
						{Liferay.Language.get('view-disabled-segments')}
					</Link>
				],
				false
			),
			onClose: () => setAlerts(() => []),
			...ALERT_CONFIG_MAP[AlertTypes.Warning]
		}
	];

	const handleUnassignedSegmentsAlert = () => {
		const openModal = () => {
			open(
				modalTypes.UNASSIGNED_SEGMENTS_MODAL,
				{
					groupId,
					onClose: close
				},
				{closeOnBlur: false}
			);
		};

		return {
			message: sub(
				Liferay.Language.get(
					'there-are-existing-segments-that-have-not-been-assigned-to-a-property-x'
				),
				[
					<ClayButton
						className='p-0'
						displayType='link'
						key='UNASSIGNED_SEGMENTS'
						onClick={openModal}
						small
					>
						{Liferay.Language.get('view-unassigned-segments')}
					</ClayButton>
				],
				false
			),
			onClose: () =>
				unassignedSegmentsDispatch({type: ActionType.updateShowAlert}),
			...ALERT_CONFIG_MAP[AlertTypes.Warning]
		};
	};

	const handleDeleteSegment = ({id, items, name}) => {
		open(modalTypes.CONFIRMATION_MODAL, {
			message: (
				<div>
					<h4 className='text-secondary'>
						{Liferay.Language.get(
							'are-you-sure-you-want-to-delete-this-segment'
						)}
					</h4>

					<p>
						{Liferay.Language.get(
							'you-will-lose-all-data-related-to-this-segment.-you-will-not-be-able-to-undo-this-operation'
						)}
					</p>
				</div>
			),
			modalVariant: 'modal-warning',
			onClose: close,
			onSubmit: () =>
				API.individualSegment
					.delete({
						groupId,
						id
					})
					.then(() => {
						addAlert({
							alertType: Alert.Types.Success,
							message: Liferay.Language.get(
								'the-segment-has-been-deleted'
							)
						});

						if (items.length === 1 && page !== 1) {
							history.push(
								setUriQueryValue(
									window.location.href,
									'page',
									Number(page) - 1
								)
							);
						} else {
							_tableRef.current.reload();
						}
					})
					.catch(() => {
						addAlert({
							alertType: Alert.Types.Error,
							message: Liferay.Language.get('error'),
							timeout: false
						});
					}),
			submitButtonDisplay: 'warning',
			submitMessage: Liferay.Language.get('delete'),
			title: sub(Liferay.Language.get('deleting-x'), [name]),
			titleIcon: 'warning-full'
		});
	};

	const renderRowActions = ({data: {id, name}, items}) => {
		const commonActions = [
			{
				href: toRoute(Routes.CONTACTS_SEGMENT_EDIT, {
					channelId,
					groupId,
					id,
					type: SEGMENTS
				}),
				iconSymbol: 'pencil',
				label: Liferay.Language.get('edit')
			},
			{
				iconSymbol: 'trash',
				label: Liferay.Language.get('delete'),
				onClick: () => handleDeleteSegment({id, items, name})
			}
		];

		const actions = commonActions.map(({href, label, onClick}) => ({
			href,
			label,
			onClick
		}));

		return <RowActions actions={actions} quickActions={commonActions} />;
	};

	const pageActions = [
		{
			href: setUriQueryValues(
				{type: SegmentTypes.Dynamic},
				toRoute(Routes.CONTACTS_SEGMENT_CREATE, {
					channelId,
					groupId
				})
			),
			label: Liferay.Language.get('dynamic-segment'),
			onClick: () =>
				analytics.track('Dynamic Segment Creation - Clicked Create')
		},
		{
			href: setUriQueryValues(
				{type: SegmentTypes.Static},
				toRoute(Routes.CONTACTS_SEGMENT_CREATE, {
					channelId,
					groupId
				})
			),
			label: Liferay.Language.get('static-segment'),
			onClick: () =>
				analytics.track('Static Segment Creation - Clicked Create')
		}
	];

	const pageActionsLabel = Liferay.Language.get('create-segment');

	return (
		<BaseListPage
			alerts={getAlerts()}
			channelId={channelId}
			className='segment-list-root'
			columns={[
				segmentsListColumns.getName({channelId, groupId}),
				segmentsListColumns.individualCount,
				segmentsListColumns.activitiesCount,
				segmentsListColumns.getOwnerName(timeZoneId)
			]}
			currentUser={currentUser}
			dataSourceFn={fetchSegments}
			delta={Number(delta)}
			entityLabel={Liferay.Language.get('segments')}
			filterBy={filterBy}
			filterByOptions={[
				{
					key: SEGMENT_STATE,
					values: [
						{
							label: Liferay.Language.get('disabled-segments'),
							value: SegmentStates.Disabled
						}
					]
				}
			]}
			groupId={groupId}
			hideNav
			noResultsConfig={{
				content: (
					<BasePage.Header.PageActions
						actions={pageActions}
						label={pageActionsLabel}
					/>
				),
				description: Liferay.Language.get(
					'create-your-first-segment-by-grouping-people-together-by-similar-characteristics-and-interests'
				),
				title: Liferay.Language.get('no-segments-created')
			}}
			orderBy={orderBy}
			orderByField={orderByField}
			orderByOptions={[
				{
					label: Liferay.Language.get('name'),
					value: NAME
				},
				{
					label: Liferay.Language.get('membership'),
					value: INDIVIDUAL_COUNT
				},
				{
					label: Liferay.Language.get('total-activities'),
					value: ACTIVITIES_COUNT
				},
				{
					label: Liferay.Language.get('created-by'),
					value: USER_NAME
				}
			]}
			page={Number(page)}
			pageActions={pageActions}
			pageActionsLabel={pageActionsLabel}
			query={query}
			ref={_tableRef}
			renderRowActions={renderRowActions}
		/>
	);
};

export default compose(
	connect(
		(store, {groupId}) => ({
			timeZoneId: store.getIn([
				'projects',
				groupId,
				'data',
				'timeZone',
				'timeZoneId'
			])
		}),
		{addAlert, close, open}
	),
	withCurrentUser,
	withFilters({filterFields: [SEGMENT_STATE]})
)(List);
