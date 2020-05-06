import * as API from 'shared/api';
import * as breadcrumbs from 'shared/util/breadcrumbs';
import BasePage from 'settings/components/BasePage';
import Card from 'shared/components/Card';
import Constants from 'shared/util/constants';
import EmptyStateDashboard from 'shared/components/EmptyStateDashboard';
import Form, {
	validateMaxLength,
	validateMinLength,
	validateRequired
} from 'shared/components/form';
import HelpBlock from 'shared/components/form/HelpBlock';
import RadioGroup from 'shared/components/RadioGroup';
import React, {useState} from 'react';
import SitesSyncedStripe from '../components/SitesSyncedStripe';
import TitleEditor from 'shared/components/TitleEditor';
import UserList from '../components/UserList';
import {addAlert} from 'shared/actions/alerts';
import {Alert, HasModal, IPaginationUnsorted} from 'shared/types';
import {close, modalTypes, open} from 'shared/actions/modals';
import {compose, withCurrentUser} from 'shared/hoc';
import {connect} from 'react-redux';
import {PageActions} from 'shared/components/base-page/Header';
import {Routes, toRoute} from 'shared/util/router';
import {SafeResults} from 'shared/hoc/util';
import {sequence} from 'shared/util/promise';
import {sub} from 'shared/util/lang';
import {UNAUTHORIZED_ACCESS} from 'shared/util/request';
import {User} from 'shared/util/records';
import {useRequest} from 'shared/hooks';

const {channelPermissionTypes} = Constants;

type Channel = {
	createTime: number;
	groupIdCount: number;
	id: string;
	name: string;
	permissionType: number;
};

export const ViewContainer: React.FC<Omit<IViewProps, 'channel'>> = ({
	groupId,
	id,
	...otherProps
}) => {
	const {data, error, loading, refetch} = useRequest(API.channels.fetch, {
		channelId: id,
		groupId
	});

	return (
		<SafeResults
			{...{data, error, loading}}
			onReload={refetch}
			pageDisplay={false}
			spacer
		>
			{(channel: Channel) => (
				<View
					channel={channel}
					groupId={groupId}
					id={id}
					{...otherProps}
				/>
			)}
		</SafeResults>
	);
};

interface IViewProps
	extends React.HTMLAttributes<HTMLElement>,
		HasModal,
		IPaginationUnsorted {
	addAlert: Alert.AddAlert;
	channel?: Channel;
	currentUser: User;
	groupId: string;
	history: {
		push: (string) => void;
	};
	id: string;
}

const View: React.FC<IViewProps> = ({
	addAlert,
	channel,
	close,
	currentUser,
	groupId,
	history,
	id,
	open,
	...otherProps
}) => {
	const [name, setName] = useState(channel.name);
	const [permissionType, setPermissionType] = useState(
		channel.permissionType
	);

	const updatePermissions = permissionType =>
		API.channels
			.update({
				groupId,
				id,
				permissionType
			})
			.then(response => setPermissionType(response.permissionType))
			.catch(() =>
				addAlert({
					alertType: Alert.Types.ERROR,
					message: Liferay.Language.get('error'),
					timeout: false
				})
			);

	const authorized = currentUser.isAdmin();

	return (
		<BasePage
			breadcrumbItems={[
				breadcrumbs.getChannels({groupId}),
				breadcrumbs.getChannelName({
					active: true,
					label: name
				})
			]}
			documentTitle={`${name} - ${Liferay.Language.get('properties')}`}
			groupId={groupId}
		>
			<div className='content-header has-page-actions'>
				<div className='header-text w-100'>
					<Form
						initialValues={{
							name: channel.name
						}}
						onSubmit={({name}) =>
							API.channels
								.update({groupId, id, name})
								.then(({name}) => setName(name))
								.catch(() =>
									addAlert({
										alertType: Alert.Types.ERROR,
										message: Liferay.Language.get('error'),
										timeout: false
									})
								)
						}
					>
						{({errors, handleSubmit, values: {name}}) => (
							<>
								<TitleEditor
									editable={authorized}
									name='name'
									onBlur={() => {
										handleSubmit();

										if (!errors.name) {
											setName(name);
										}
									}}
									validate={sequence([
										validateMaxLength(75),
										validateMinLength(3),
										validateRequired
									])}
								/>
								<HelpBlock
									className='text-danger'
									name='name'
								/>
							</>
						)}
					</Form>

					<div className='description'>
						{sub(Liferay.Language.get('property-id-x'), [
							channel.id
						])}
					</div>
				</div>

				{authorized && (
					<PageActions
						actions={[
							{
								label: Liferay.Language.get('delete'),
								onClick: () =>
									open(modalTypes.DELETE_CHANNEL_MODAL, {
										channelIds: [id],
										channelName: name,
										groupId,
										onClose: close,
										onSubmit: () => {
											API.channels
												.delete({
													groupId,
													ids: [id]
												})
												.then(() => {
													const deletedMessage = Liferay.Language.get(
														'x-has-been-deleted'
													);

													close();

													history.push(
														toRoute(
															Routes.SETTINGS_CHANNELS,
															{
																groupId,
																id
															}
														)
													);

													addAlert({
														alertType:
															Alert.Types.SUCCESS,
														message: sub(
															deletedMessage,
															[name]
														) as string
													});
												})
												.catch(err =>
													addAlert({
														alertType:
															Alert.Types.ERROR,
														message:
															err.message ===
															UNAUTHORIZED_ACCESS
																? Liferay.Language.get(
																		'unauthorized-access'
																  )
																: Liferay.Language.get(
																		'error'
																  ),
														timeout: false
													})
												);
										},
										title: sub(
											Liferay.Language.get('delete-x?'),
											[name]
										)
									})
							}
						]}
					/>
				)}
			</div>

			<Card pageDisplay>
				<SitesSyncedStripe sitesSyncedCount={channel.groupIdCount} />

				<Card.Body className='flex-grow-0'>
					<RadioGroup
						checked={permissionType}
						disabled={!authorized}
						inline
						name='permissionType'
						onChange={val => {
							if (val === channelPermissionTypes.selectUsers) {
								open(modalTypes.CONFIRMATION_MODAL, {
									message: (
										<div className='text-secondary'>
											{Liferay.Language.get(
												'property-permissions-will-be-changed-if-you-proceed-to-select-users.-add-users-from-your-workspace-to-give-access-to-this-property'
											)}
										</div>
									),
									modalVariant: 'modal-warning',
									onClose: close,
									onSubmit: () => {
										updatePermissions(val);

										close();
									},
									submitButtonDisplay: 'warning',
									submitMessage: Liferay.Language.get('okay'),
									title: Liferay.Language.get(
										'permissions-change'
									),
									titleIcon: 'warning-full'
								});
							} else {
								updatePermissions(val);
							}
						}}
					>
						<RadioGroup.Option
							label={<b>{Liferay.Language.get('all-users')}</b>}
							value={channelPermissionTypes.allUsers}
						/>
						<RadioGroup.Option
							label={
								<b>{Liferay.Language.get('select-users')}</b>
							}
							value={channelPermissionTypes.selectUsers}
						/>
					</RadioGroup>
				</Card.Body>

				<Card.Body noPadding>
					{permissionType === channelPermissionTypes.allUsers ? (
						<EmptyStateDashboard
							description={Liferay.Language.get(
								'all-users-from-this-workspace-have-access-to-this-property'
							)}
							symbol='ac-no-sites'
							title={Liferay.Language.get('all-aboard')}
						/>
					) : (
						<UserList
							authorized={currentUser.isAdmin()}
							channelId={id}
							groupId={groupId}
							id={channel.id}
							propertyName={channel.name}
							{...otherProps}
						/>
					)}
				</Card.Body>
			</Card>
		</BasePage>
	);
};

export default compose<any>(
	withCurrentUser,
	connect(
		null,
		{addAlert, close, open}
	)
)(ViewContainer);
