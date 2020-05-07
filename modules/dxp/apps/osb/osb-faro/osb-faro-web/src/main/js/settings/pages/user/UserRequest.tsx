import * as API from 'shared/api';
import React, {useRef} from 'react';
import RequestActionsRenderer from 'settings/components/user-list/RequestActionsRenderer';
import SearchableTableWithStaged from 'shared/components/searchable-table-with-staged';
import {addAlert, alertTypes} from 'shared/actions/alerts';
import {close, modalTypes, open} from 'shared/actions/modals';
import {compose, withAdminPermission} from 'shared/hoc';
import {connect} from 'react-redux';
import {
	EMAIL_ADDRESS,
	FIRST_NAME,
	LAST_NAME,
	NAME,
	paginationDefaults
} from 'shared/util/pagination';
import {sub} from 'shared/util/lang';
import {userStatuses} from 'shared/util/constants';

interface IUserRequestProps extends React.HTMLAttributes<HTMLElement> {}

type UserRequest = {
	close: () => void;
	delta?: string;
	groupId: string;
	onSetUserRequest: (userRequest: number) => void;
	open: (modalType: string, options: object) => void;
	orderBy?: string;
	orderByField?: string;
	page?: string;
	query?: string;
};

const getUsers = ({
	delta,
	groupId,
	orderBy,
	orderByField,
	page,
	query
}: UserRequest) => {
	const getOrderByFields = () =>
		orderByField && orderByField !== 'name'
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

	return API.user.fetchMany({
		cur: page,
		delta,
		groupId,
		orderByFields: getOrderByFields(),
		query,
		statuses: [userStatuses.requested]
	});
};

export const UserRequest: React.FC<UserRequest & IUserRequestProps> = ({
	close,
	delta = paginationDefaults.delta,
	groupId,
	open,
	onSetUserRequest,
	orderBy = paginationDefaults.orderBy,
	orderByField = paginationDefaults.orderByField,
	page = paginationDefaults.page,
	query = paginationDefaults.query
}) => {
	const tableRef = useRef<any>();

	const handleGetUsers = params =>
		getUsers(params).then(data => {
			onSetUserRequest(data.total);

			return data;
		});

	const onAccept = response => {
		const {emailAddress, id} = response;

		open(modalTypes.CONFIRMATION_MODAL, {
			message: sub(
				Liferay.Language.get('are-you-sure-you-want-to-accept-x'),
				[<b key='acceptCount'>{emailAddress}</b>],
				false
			),
			modalVariant: 'modal-info',
			onClose: close,
			onSubmit: () =>
				API.user
					.accept({groupId, id})
					.then(() => {
						addAlert({
							alertType: alertTypes.SUCCESS,
							message: Liferay.Language.get('user-added')
						});

						tableRef.current.reload();
					})
					.catch(() => {
						addAlert({
							alertType: alertTypes.ERROR,
							message: Liferay.Language.get('error')
						});
					}),
			title: Liferay.Language.get('accept')
		});
	};

	const onDecline = ({emailAddress, id}) => {
		open(modalTypes.CONFIRMATION_MODAL, {
			message: sub(
				Liferay.Language.get('are-you-sure-you-want-to-decline-x'),
				[<b key='declineCount'>{emailAddress}</b>],
				false
			),
			modalVariant: 'modal-info',
			onClose: close,
			onSubmit: () =>
				API.user
					.delete({groupId, ids: [id]})
					.then(() => {
						addAlert({
							alertType: alertTypes.DEFAULT,
							message: Liferay.Language.get(
								'user-request-to-join-denied'
							)
						});

						tableRef.current.reload();
					})
					.catch(() => {
						addAlert({
							alertType: alertTypes.ERROR,
							message: Liferay.Language.get('error')
						});
					}),
			title: Liferay.Language.get('decline')
		});
	};

	return (
		<SearchableTableWithStaged
			columns={[
				{
					accessor: 'name',
					label: Liferay.Language.get('name'),
					title: true
				},
				{
					accessor: 'emailAddress',
					label: Liferay.Language.get('email')
				},
				{
					accessor: 'status',
					cellRenderer: RequestActionsRenderer,
					cellRendererProps: {
						onAccept,
						onDecline
					},
					className: 'text-right'
				}
			]}
			dataSourceFn={handleGetUsers}
			dataSourceParams={{groupId}}
			delta={parseInt(delta)}
			entityLabel={Liferay.Language.get('users')}
			navRenderer={null}
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
				}
			]}
			page={parseInt(page)}
			query={query}
			ref={tableRef}
			showCheckbox={false}
		/>
	);
};

export default compose<any>(
	withAdminPermission,
	connect(
		null,
		{addAlert, close, open}
	)
)(UserRequest);
