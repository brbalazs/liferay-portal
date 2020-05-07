import * as API from 'shared/api';
import autobind from 'autobind-decorator';
import Button from 'shared/components/Button';
import FaroConstants from 'shared/util/constants';
import Icon from 'shared/components/Icon';
import Nav from 'shared/components/Nav';
import React from 'react';
import RoleRenderer from '../../components/user-list/RoleRenderer';
import SearchableTableWithStaged from 'shared/components/searchable-table-with-staged';
import StatusRenderer from '../../components/user-list/StatusRenderer';
import UserActionsRenderer from '../../components/user-list/UserActionsRenderer';
import {
	ACTION_TYPES,
	SelectionContext,
	withSelectionProvider
} from 'shared/context/selection';
import {addAlert, alertTypes} from 'shared/actions/alerts';
import {close, modalTypes, open} from 'shared/actions/modals';
import {compose} from 'shared/hoc';
import {connect} from 'react-redux';
import {
	EMAIL_ADDRESS,
	FIRST_NAME,
	LAST_NAME,
	NAME,
	paginationConfig,
	paginationDefaults,
	ROLE_NAME,
	STATUS
} from 'shared/util/pagination';
import {getDisplayRole, getPluralMessage, sub} from 'shared/util/lang';
import {PropTypes} from 'prop-types';
import {UNAUTHORIZED_ACCESS} from 'shared/util/request';
import {User} from 'shared/util/records';

const {
	userRoleNames: {administrator, member}
} = FaroConstants;

const userRoleOptions = [member, administrator].map(role => ({
	label: getDisplayRole(role),
	value: role
}));

export class UserList extends React.Component {
	static contextType = SelectionContext;

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
		open: PropTypes.func.isRequired,
		orderByField: PropTypes.string
	};

	constructor(props) {
		super(props);

		this._tableRef = React.createRef();
	}

	@autobind
	getUsers({delta, groupId, page, query}) {
		return API.user.fetchMany({
			cur: page,
			delta,
			groupId,
			orderByFields: this.getOrderByFields(),
			query
		});
	}

	getOrderByFields() {
		const {orderBy, orderByField} = this.props;

		return orderByField && orderByField !== 'name'
			? [
					{
						fieldName: orderByField,
						orderBy
					}
			  ]
			: [
					{
						fieldName: FIRST_NAME,
						orderBy
					},
					{
						fieldName: LAST_NAME,
						orderBy
					}
			  ];
	}

	@autobind
	handleInviteModal() {
		const {close, open} = this.props;

		open(modalTypes.INVITE_USERS_MODAL, {
			onClose: close,
			onSubmit: this.handleUserInvite
		});
	}

	@autobind
	handleActions(selectedItemsIOMap) {
		const {close, open} = this.props;

		open(modalTypes.BATCH_ACTION_MODAL, {
			actionOptions: {
				actionCountString: Liferay.Language.get(
					'changing-permissions-for-x-users'
				),
				options: userRoleOptions,
				optionsLabel: Liferay.Language.get('select-permission')
			},
			columns: [
				{
					accessor: 'name',
					className: 'table-cell-expand',
					label: Liferay.Language.get('name'),
					sortable: false,
					title: true
				},
				{
					accessor: 'emailAddress',
					label: Liferay.Language.get('email'),
					sortable: false
				},
				{
					accessor: 'status',
					cellRenderer: StatusRenderer,
					label: Liferay.Language.get('status'),
					sortable: false
				},
				{
					accessor: 'roleName',
					cellRenderer: RoleRenderer,
					label: Liferay.Language.get('permission'),
					sortable: false
				}
			],
			editableAttr: 'roleName',
			fitContent: true,
			items: selectedItemsIOMap.toArray(),
			onClose: close,
			onSave: this.handleUserSave,
			title: Liferay.Language.get('edit-permissions-for-selected-users')
		});
	}

	@autobind
	handleUserDelete(ids) {
		const {
			context: {selectionDispatch},
			props: {addAlert, close, groupId, open}
		} = this;

		open(modalTypes.CONFIRMATION_MODAL, {
			message: getPluralMessage(
				Liferay.Language.get('are-you-sure-you-want-to-delete-x-user'),
				Liferay.Language.get('are-you-sure-you-want-to-delete-x-users'),
				ids.length,
				false,
				[<b key='confirmDeleteCount'>{ids.length}</b>]
			),
			modalVariant: 'modal-warning',
			onClose: close,
			onSubmit: () =>
				API.user
					.delete({
						groupId,
						ids
					})
					.then(data => {
						addAlert({
							alertType: alertTypes.SUCCESS,
							message: getPluralMessage(
								Liferay.Language.get('x-user-has-been-deleted'),
								Liferay.Language.get(
									'x-users-have-been-deleted'
								),
								data.length,
								false,
								[<b key='deleteCount'>{data.length}</b>]
							)
						});

						selectionDispatch({type: ACTION_TYPES.clearAll});

						this._tableRef.current.reload();
					})
					.catch(err =>
						addAlert({
							alertType: alertTypes.ERROR,
							message:
								err.message === UNAUTHORIZED_ACCESS
									? Liferay.Language.get(
											'unauthorized-access'
									  )
									: Liferay.Language.get('error'),
							timeout: false
						})
					),
			title: Liferay.Language.get('delete-user'),
			titleIcon: 'warning-full'
		});
	}

	@autobind
	handleUserInvite(emailAddresses) {
		const {addAlert, close, groupId} = this.props;

		API.user
			.inviteMany({emailAddresses, groupId, roleName: 'Site Member'})
			.then(() => {
				addAlert({
					alertType: alertTypes.SUCCESS,
					message: Liferay.Language.get('invitations-have-been-sent')
				});

				this._tableRef.current.reload();
			})
			.catch(() => {
				addAlert({
					alertType: alertTypes.ERROR,
					message: Liferay.Language.get('error')
				});
			});

		close();
	}

	@autobind
	handleUserSave(attrObj) {
		const {
			context: {selectionDispatch},
			props: {addAlert, groupId}
		} = this;

		const {edits, ids} = attrObj;

		API.user
			.updateMany({...edits, groupId, ids})
			.then(data => {
				addAlert({
					alertType: alertTypes.SUCCESS,
					message: sub(
						Liferay.Language.get(
							'permissions-have-been-changed-for-x-users'
						),
						[<b key='changedCount'>{data.length}</b>],
						false
					)
				});

				selectionDispatch({type: ACTION_TYPES.clearAll});

				this._tableRef.current.reload();
			})
			.catch(err =>
				addAlert({
					alertType: alertTypes.ERROR,
					message:
						err.message === UNAUTHORIZED_ACCESS
							? Liferay.Language.get('unauthorized-access')
							: Liferay.Language.get('error')
				})
			);
	}

	@autobind
	isUserDisabled(user) {
		const {currentUser} = this.props;

		const userRow = new User(user);

		return (
			!currentUser.isAdmin() ||
			userRow.isOwner() ||
			user.id === currentUser.id
		);
	}

	@autobind
	renderNav(selectedItemsIOMap) {
		let retVal;

		if (selectedItemsIOMap.isEmpty()) {
			retVal = (
				<Nav>
					<Nav.Item>
						<Button
							className='nav-btn'
							display='primary'
							onClick={this.handleInviteModal}
						>
							{Liferay.Language.get('invite-users')}
						</Button>
					</Nav.Item>
				</Nav>
			);
		} else {
			retVal = (
				<Nav>
					<Button
						borderless
						display='secondary'
						onClick={() => this.handleActions(selectedItemsIOMap)}
						outline
					>
						{Liferay.Language.get('change-permissions')}
					</Button>

					<Button
						borderless
						display='secondary'
						onClick={() =>
							this.handleUserDelete(
								selectedItemsIOMap.keySeq().toArray()
							)
						}
						outline
					>
						<Icon symbol='trash' />
					</Button>
				</Nav>
			);
		}

		return retVal;
	}

	@autobind
	renderInlineRowActions({data, editing, edits, itemsSelected, rowEvents}) {
		const {currentUser} = this.props;

		return (
			/* eslint-disable react/jsx-handler-names */
			<UserActionsRenderer
				currentUserId={currentUser.id}
				data={new User(data)}
				editing={editing}
				edits={edits}
				itemsSelected={itemsSelected}
				{...rowEvents}
				onUserDelete={this.handleUserDelete}
				onUserSave={this.handleUserSave}
			/>
			/* eslint-enable react/jsx-handler-names */
		);
	}

	render() {
		const {
			currentUser,
			delta,
			filterBy,
			groupId,
			orderBy,
			orderByField,
			page,
			query
		} = this.props;

		const authorized = currentUser.isAdmin();

		return (
			<SearchableTableWithStaged
				checkDisabled={this.isUserDisabled}
				columns={[
					{
						accessor: 'name',
						className: 'table-cell-expand',
						label: Liferay.Language.get('name'),
						title: true
					},
					{
						accessor: 'emailAddress',
						label: Liferay.Language.get('email')
					},
					{
						accessor: 'status',
						cellRenderer: StatusRenderer,
						label: Liferay.Language.get('status')
					},
					{
						accessor: 'roleName',
						cellRenderer: RoleRenderer,
						cellRendererProps: {
							options: userRoleOptions
						},
						editable: true,
						label: Liferay.Language.get('permission')
					}
				]}
				dataSourceFn={this.getUsers}
				dataSourceParams={{groupId}}
				delta={Number(delta)}
				entityLabel={Liferay.Language.get('users')}
				filterBy={filterBy}
				navRenderer={authorized ? this.renderNav : null}
				orderBy={orderBy}
				orderByField={orderByField}
				orderByOptions={[
					{
						label: Liferay.Language.get('name'),
						value: NAME
					},
					{
						label: Liferay.Language.get('email'),
						value: EMAIL_ADDRESS
					},
					{
						label: Liferay.Language.get('permission'),
						value: ROLE_NAME
					},
					{
						label: Liferay.Language.get('status'),
						value: STATUS
					}
				]}
				page={Number(page)}
				query={query}
				ref={this._tableRef}
				renderInlineRowActions={
					authorized ? this.renderInlineRowActions : null
				}
				showCheckbox={authorized}
			/>
		);
	}
}

export default compose(
	connect(
		null,
		{addAlert, close, open}
	),
	withSelectionProvider
)(UserList);
