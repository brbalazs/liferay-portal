import * as API from 'shared/api';
import autobind from 'autobind-decorator';
import BaseListPage from 'contacts/components/BaseListPage';
import BasePage from 'shared/components/base-page';
import ClayButton from '@clayui/button';
import FaroConstants from 'shared/util/constants';
import React from 'react';
import RowActions from 'shared/components/table/RowActions';
import {
	ActionType,
	UnassignedSegmentsContext
} from 'shared/context/unassignedSegments';
import {
	ACTIVITIES_COUNT,
	ENGAGEMENT_SCORE,
	INDIVIDUAL_COUNT,
	NAME,
	paginationConfig,
	paginationDefaults,
	USER_NAME
} from 'shared/util/pagination';
import {addAlert, alertTypes} from 'shared/actions/alerts';
import {ALERT_CONFIG_MAP, ALERT_TYPE_MAP} from 'shared/components/Alert';
import {autoCancel, hasRequest} from 'shared/util/request-decorator';
import {close, modalTypes, open} from 'shared/actions/modals';
import {compose, withCurrentUser, withFilters} from 'shared/hoc';
import {connect} from 'react-redux';
import {Link} from 'react-router-dom';
import {PropTypes} from 'prop-types';
import {
	Routes,
	SEGMENT_STATE,
	SEGMENTS,
	setUriQueryValue,
	toRoute
} from 'shared/util/router';
import {segmentsListColumns} from 'shared/util/table-columns';
import {Set} from 'immutable';
import {setUriQueryValues} from 'shared/util/router';
import {sub} from 'shared/util/lang';
import {User} from 'shared/util/records';

const {segmentStates, segmentTypes} = FaroConstants;

function fetchSegments({
	channelId,
	delta,
	filterBy,
	groupId,
	orderBy,
	orderByField,
	page,
	query
}) {
	const stateFilterISet = filterBy.get('state', new Set());

	return API.individualSegment.search({
		channelId,
		delta,
		groupId,
		orderByFields: [
			{
				fieldName: orderByField,
				orderBy,
				system: true
			}
		],
		page,
		query,
		state: stateFilterISet.first()
	});
}

function fetchDisabledSegments({channelId, groupId}) {
	return API.individualSegment.search({
		channelId,
		delta: 1,
		groupId,
		state: segmentStates.disabled
	});
}

@hasRequest
export class List extends React.Component {
	static contextType = UnassignedSegmentsContext;

	static defaultProps = {
		...paginationDefaults,
		orderByField: NAME
	};

	static propTypes = {
		...paginationConfig,
		addAlert: PropTypes.func.isRequired,
		close: PropTypes.func.isRequired,
		currentUser: PropTypes.instanceOf(User).isRequired,
		groupId: PropTypes.string.isRequired,
		history: PropTypes.object.isRequired,
		open: PropTypes.func.isRequired,
		timeZoneId: PropTypes.string
	};

	state = {
		alerts: []
	};

	constructor(props) {
		super(props);

		this._tableRef = React.createRef();

		this.getDisabledSegmentsAlert();
	}

	@autoCancel
	getDisabledSegmentsAlert() {
		const {channelId, groupId} = this.props;

		return fetchDisabledSegments({channelId, groupId}).then(({total}) => {
			if (total) {
				this.setState({alerts: this.handleDisabledSegmentsAlert()});
			}
		});
	}

	getAlerts() {
		const {
			context: {showUnassignedAlert, unassignedSegments},
			state: {alerts}
		} = this;

		return [
			...alerts,
			showUnassignedAlert &&
				unassignedSegments.length &&
				this.handleUnassignedSegmentsAlert()
		].filter(Boolean);
	}

	handleDisabledSegmentsAlert() {
		return [
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
								segmentStates.disabled
							)}
						>
							{Liferay.Language.get('view-disabled-segments')}
						</Link>
					],
					false
				),
				onClose: () => this.setState({alerts: []}),
				...ALERT_CONFIG_MAP[ALERT_TYPE_MAP.warning]
			}
		];
	}

	handleUnassignedSegmentsAlert() {
		const {close, groupId, open} = this.props;
		const {unassignedSegmentsDispatch} = this.context;

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
			...ALERT_CONFIG_MAP[ALERT_TYPE_MAP.warning]
		};
	}

	@autobind
	handleDeleteSegment({id, items, name}) {
		const {addAlert, close, groupId, history, open, page} = this.props;

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
							alertType: alertTypes.SUCCESS,
							message: Liferay.Language.get(
								'the-segment-has-been-deleted'
							)
						});

						if (items.length === 1 && page !== 1) {
							history.push(
								setUriQueryValue(
									window.location.href,
									'page',
									page - 1
								)
							);
						} else {
							this._tableRef.current.reload();
						}
					})
					.catch(() => {
						addAlert({
							alertType: alertTypes.ERROR,
							message: Liferay.Language.get('error'),
							timeout: false
						});
					}),
			submitButtonDisplay: 'warning',
			submitMessage: Liferay.Language.get('delete'),
			title: sub(Liferay.Language.get('deleting-x'), [name]),
			titleIcon: 'warning-full'
		});
	}

	@autobind
	renderRowActions({data: {id, name}, items}) {
		const {channelId, groupId} = this.props;

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
				onClick: () => this.handleDeleteSegment({id, items, name})
			}
		];

		const actions = commonActions.map(({href, label, onClick}) => ({
			href,
			label,
			onClick
		}));

		return <RowActions actions={actions} quickActions={commonActions} />;
	}

	render() {
		const {
			props: {
				channelId,
				currentUser,
				delta,
				filterBy,
				groupId,
				orderBy,
				orderByField,
				page,
				query,
				timeZoneId
			}
		} = this;

		const alerts = this.getAlerts();

		const pageActions = [
			{
				href: setUriQueryValues(
					{type: segmentTypes.dynamic},
					toRoute(Routes.CONTACTS_SEGMENT_CREATE, {
						channelId,
						groupId
					})
				),
				label: Liferay.Language.get('dynamic-segment')
			},
			{
				href: setUriQueryValues(
					{type: segmentTypes.static},
					toRoute(Routes.CONTACTS_SEGMENT_CREATE, {
						channelId,
						groupId
					})
				),
				label: Liferay.Language.get('static-segment')
			}
		];

		const pageActionsLabel = Liferay.Language.get('create-segment');

		return (
			<BaseListPage
				alerts={alerts}
				channelId={channelId}
				className='segment-list-root'
				columns={[
					segmentsListColumns.getName({channelId, groupId}),
					segmentsListColumns.individualCount,
					segmentsListColumns.activitiesCount,
					segmentsListColumns.engagementScore,
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
								label: Liferay.Language.get(
									'disabled-segments'
								),
								value: segmentStates.disabled
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
						label: Liferay.Language.get('30-day-engagement'),
						value: ENGAGEMENT_SCORE
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
				ref={this._tableRef}
				renderRowActions={this.renderRowActions}
			/>
		);
	}
}

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
