import * as API from 'shared/api';
import autobind from 'autobind-decorator';
import BasePage from 'settings/components/BasePage';
import Button from 'shared/components/Button';
import Card from 'shared/components/Card';
import EmptyState from 'shared/components/EmptyStateDashboard';
import Nav from 'shared/components/Nav';
import React from 'react';
import SearchableTableWithStaged from 'shared/components/searchable-table-with-staged';
import TextTruncate from 'shared/components/TextTruncate';
import {
	ACTION_TYPES,
	SelectionContext,
	withSelectionProvider
} from 'shared/context/selection';
import {addAlert} from 'shared/actions/alerts';
import {Alert, Modal} from 'shared/types';
import {autoCancel} from 'shared/util/request-decorator';
import {close, modalTypes, open} from 'shared/actions/modals';
import {compose, withCurrentUser} from 'shared/hoc';
import {connect} from 'react-redux';
import {formatDateToTimeZone} from 'shared/util/date';
import {FormikActions} from 'formik';
import {getPluralMessage, sub} from 'shared/util/lang';
import {IPagination} from 'shared/types';
import {Link} from 'react-router-dom';
import {paginationDefaults} from 'shared/util/pagination';
import {Routes, toRoute} from 'shared/util/router';
import {UNAUTHORIZED_ACCESS} from 'shared/util/request';
import {User} from 'shared/util/records';

interface IChannelListProps extends IPagination {
	addAlert: Alert.AddAlert;
	close: Modal.close;
	currentUser: User;
	groupId: string;
	history: {
		push: (string) => void;
	};
	open: Modal.open;
	timeZoneId: string;
}

type ChannelNameFn = (attrs: {
	data: {id: string; name: string};
	hrefFormatter: (data: object) => string;
}) => React.ReactNode;

type SelectedItems = {
	keySeq: () => {
		toArray: () => Array<string>;
	};
	first: () => {
		name: string;
	};
	isEmpty: () => boolean;
};

type FormValues = {
	name: string;
};

const ChannelName: ChannelNameFn = ({data, hrefFormatter}) => (
	<td className='table-cell-expand' key={data.id}>
		<div className='table-title text-truncate'>
			<Link to={hrefFormatter(data)}>
				<TextTruncate title={data.name} />
			</Link>
		</div>
	</td>
);

const renderEmptyState: () => React.ReactNode = () => (
	<EmptyState
		className='no-results-root mt-0'
		description={Liferay.Language.get(
			'create-a-new-property-to-get-started'
		)}
		symbol='ac-satellite'
		title={Liferay.Language.get('no-properties-found')}
	/>
);

export class ChannelList extends React.Component<IChannelListProps> {
	static contextType = SelectionContext;

	static defaultProps = {
		...paginationDefaults,
		orderByField: 'createTime'
	};

	_tableRef: React.RefObject<any>;

	constructor(props) {
		super(props);

		this._tableRef = React.createRef();
	}

	@autobind
	getSiteURL({id}: {id: string}) {
		const {groupId} = this.props;

		return toRoute(Routes.SETTINGS_CHANNELS_VIEW, {
			groupId,
			id
		});
	}

	@autoCancel
	fetchChannels({delta, groupId, orderBy, orderByField, page, query}) {
		const {addAlert} = this.props;

		return API.channels
			.search({
				cur: page,
				delta,
				groupId,
				orderByFields: [
					{
						fieldName: orderByField,
						orderBy
					}
				],
				query
			})
			.catch(() =>
				addAlert({
					alertType: Alert.Types.ERROR,
					message: Liferay.Language.get('error')
				})
			);
	}

	@autobind
	handleAddChannel() {
		const {close, open} = this.props;

		open(modalTypes.ADD_CHANNEL_MODAL, {
			onCloseFn: close,
			onSubmitFn: this.handleSubmit
		});
	}

	@autobind
	handleClearData(selectedItems: SelectedItems) {
		const {
			context: {selectionDispatch},
			props: {addAlert, close, groupId, open}
		} = this;

		const ids = selectedItems.keySeq().toArray();

		const message: string = getPluralMessage(
			selectedItems.first().name,
			Liferay.Language.get('x-properties'),
			ids.length
		) as string;

		open(modalTypes.DELETE_CONFIRMATION_MODAL, {
			children: (
				<>
					<p>
						<strong>
							{sub(
								Liferay.Language.get(
									'to-clear-data-from-x,-copy-the-sentence-below-to-confirm-your-intention-to-clear-data-from-this-property'
								),
								[message]
							)}
						</strong>
					</p>

					<p>
						{Liferay.Language.get(
							'this-will-result-in-the-complete-removal-of-this-property-and-its-historical-events.-you-will-not-be-able-to-undo-this-operation'
						)}
					</p>
				</>
			),
			deleteButtonLabel: Liferay.Language.get('clear-data'),
			deleteConfirmationText: sub(Liferay.Language.get('clear-x'), [
				message
			]),
			onClose: close,
			onSubmit: () =>
				API.channels
					.clear({
						groupId,
						ids
					})
					.then(() => {
						const clearedMessage: string = getPluralMessage(
							Liferay.Language.get('x-property-has-been-cleared'),
							Liferay.Language.get(
								'x-properties-have-been-cleared'
							),
							ids.length
						) as string;

						addAlert({
							alertType: Alert.Types.SUCCESS,
							message: sub(
								clearedMessage,
								[<b key='clearedCount'>{ids.length}</b>],
								false
							) as string
						});

						selectionDispatch({type: ACTION_TYPES.clearAll});

						this._tableRef.current.reload();

						close();
					})
					.catch(err =>
						addAlert({
							alertType: Alert.Types.ERROR,
							message:
								err.message === UNAUTHORIZED_ACCESS
									? Liferay.Language.get(
											'unauthorized-access'
									  )
									: Liferay.Language.get('error'),
							timeout: false
						})
					),
			title: sub(Liferay.Language.get('clear-x-data?'), [message])
		});
	}

	@autobind
	handleDeleteChannel(selectedItems: SelectedItems) {
		const {
			context: {selectionDispatch},
			props: {addAlert, close, groupId, open}
		} = this;

		const ids = selectedItems.keySeq().toArray();

		const message: string = getPluralMessage(
			selectedItems.first().name,
			Liferay.Language.get('x-properties'),
			ids.length
		) as string;

		open(modalTypes.DELETE_CHANNEL_MODAL, {
			channelIds: ids,
			channelName: message,
			groupId,
			onClose: close,
			onSubmit: () =>
				API.channels
					.delete({
						groupId,
						ids
					})
					.then(() => {
						const deletedMessage: string = getPluralMessage(
							Liferay.Language.get('x-property-has-been-deleted'),
							Liferay.Language.get(
								'x-properties-have-been-deleted'
							),
							ids.length
						) as string;

						addAlert({
							alertType: Alert.Types.SUCCESS,
							message: sub(
								deletedMessage,
								[<b key='deleteCount'>{ids.length}</b>],
								false
							) as string
						});

						selectionDispatch({type: ACTION_TYPES.clearAll});

						this._tableRef.current.reload();

						close();
					})
					.catch(err =>
						addAlert({
							alertType: Alert.Types.ERROR,
							message:
								err.message === UNAUTHORIZED_ACCESS
									? Liferay.Language.get(
											'unauthorized-access'
									  )
									: Liferay.Language.get('error'),
							timeout: false
						})
					)
		});
	}

	@autobind
	handleSubmit(
		{name}: FormValues,
		{setFieldError, setSubmitting}: FormikActions<FormValues>
	) {
		const {addAlert, close, groupId, history} = this.props;

		API.channels
			.create({groupId, name: name.trim()})
			.then(({id, name}) => {
				addAlert({
					alertType: Alert.Types.SUCCESS,
					message: sub(Liferay.Language.get('x-has-been-created'), [
						name
					]) as string
				});

				close();

				history.push(
					toRoute(Routes.SETTINGS_CHANNELS_VIEW, {
						groupId,
						id
					})
				);
			})
			.catch(({field, message}) => {
				setSubmitting(false);

				if (field) {
					setFieldError(field, message);
				}
			});
	}

	@autobind
	renderNav(checkedItemsISet: SelectedItems) {
		if (checkedItemsISet.isEmpty()) {
			return (
				<Nav>
					<Nav.Item>
						<Button
							className='nav-btn'
							data-testid='addproperty-button'
							display='primary'
							onClick={this.handleAddChannel}
						>
							{Liferay.Language.get('new-property')}
						</Button>
					</Nav.Item>
				</Nav>
			);
		} else {
			return (
				<Nav>
					<Button
						borderless
						display='secondary'
						onClick={() => this.handleClearData(checkedItemsISet)}
						outline
					>
						{Liferay.Language.get('clear-data')}
					</Button>

					<Button
						borderless
						display='secondary'
						onClick={() =>
							this.handleDeleteChannel(checkedItemsISet)
						}
						outline
					>
						{Liferay.Language.get('delete')}
					</Button>
				</Nav>
			);
		}
	}

	render() {
		const {
			currentUser,
			delta = paginationDefaults.delta,
			groupId,
			orderBy,
			orderByField,
			page = paginationDefaults.page,
			query,
			timeZoneId
		} = this.props;

		const authorized: boolean = currentUser.isAdmin();

		return (
			<BasePage
				groupId={groupId}
				key='sitesListPage'
				pageDescription={
					<>
						<div>
							{Liferay.Language.get(
								'analytics-cloud-allows-for-customized-user-access-settings-per-property-managed'
							)}
						</div>
						<div>
							{Liferay.Language.get(
								'by-default-property-access-settings-will-be-set-to-all-users'
							)}
						</div>
					</>
				}
				pageTitle={Liferay.Language.get('properties')}
			>
				<Card pageDisplay>
					<SearchableTableWithStaged
						columns={[
							{
								accessor: 'name',
								cellRenderer: ChannelName,
								cellRendererProps: {
									hrefFormatter: this.getSiteURL
								},
								className: 'table-cell-expand',
								label: Liferay.Language.get('property-name')
							},
							{
								accessor: 'id',
								label: Liferay.Language.get('property-id'),
								sortable: false
							},
							{
								accessor: 'permissionType',
								dataFormatter: value =>
									value === 0
										? Liferay.Language.get('all-users')
										: Liferay.Language.get('select-users'),
								label: Liferay.Language.get('access-setting'),
								sortable: false
							},
							{
								accessor: 'createTime',
								dataFormatter: date =>
									formatDateToTimeZone(
										date,
										'll',
										timeZoneId
									),
								label: Liferay.Language.get('date-added')
							}
						]}
						currentUser={currentUser}
						dataSourceFn={this.fetchChannels}
						dataSourceParams={{groupId}}
						delta={Number(delta)}
						entityLabel={Liferay.Language.get('properties')}
						navRenderer={authorized ? this.renderNav : null}
						noResultsRenderer={renderEmptyState}
						orderBy={orderBy}
						orderByField={orderByField}
						page={Number(page)}
						query={query}
						ref={this._tableRef}
						rowIdentifier='id'
						showCheckbox={authorized}
					/>
				</Card>
			</BasePage>
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
	withSelectionProvider
)(ChannelList);
