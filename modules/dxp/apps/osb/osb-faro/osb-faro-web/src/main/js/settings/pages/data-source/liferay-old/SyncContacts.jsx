import * as API from 'shared/api';
import * as breadcrumbs from 'shared/util/breadcrumbs';
import autobind from 'autobind-decorator';
import BaseDataSourcePage from '../../../components/data-source/BasePage';
import ErrorDisplay from 'shared/components/ErrorDisplay';
import FormNavigation from 'settings/components/FormNavigation';
import getCN from 'classnames';
import Label from 'shared/components/Label';
import ListGroup from 'shared/components/list-group';
import omitDefinedProps from 'shared/util/omitDefinedProps';
import ProgressTimeline from 'shared/components/ProgressTimeline';
import Promise from 'metal-promise';
import React from 'react';
import Sheet from 'shared/components/Sheet';
import Spinner from 'shared/components/Spinner';
import Sticker from 'shared/components/Sticker';
import ToggleSwitch from 'shared/components/ToggleSwitch';
import {addAlert} from 'shared/actions/alerts';

import {ANALYTICS, CONTACTS, Routes, toRoute} from 'shared/util/router';

import {autoCancel, hasRequest} from 'shared/util/request-decorator';
import {close, modalTypes, open} from 'shared/actions/modals';
import {
	compose,
	redirectIf,
	withAdminPermission,
	withDataSource
} from 'shared/hoc';
import {connect} from 'react-redux';
import {DataSource} from 'shared/util/records';
import {
	dataSourceRedirectFn,
	getIdsFromConfiguration,
	getServiceAlertConfig,
	validContactsConfig
} from 'shared/util/data-sources';
import {fromJS, List, Map} from 'immutable';
import {getServiceError} from 'shared/util/request';
import {hasChanges} from 'shared/util/react';
import {isNil} from 'lodash';
import {NameCell} from 'shared/components/table/cell-components';
import {PropTypes} from 'prop-types';
import {sub} from 'shared/util/lang';
import {updateLiferayDataSource} from 'shared/actions/data-sources';

const STEP = 1;

export const TITLE_MAP = {
	[ANALYTICS]: [{title: Liferay.Language.get('analytics')}],
	[CONTACTS]: [
		{title: Liferay.Language.get('contacts')},
		{title: Liferay.Language.get('contact-data')}
	]
};

/**
 * Check if the DataSource has a prior contactsConfiguration with selections.
 * @param {DataSource} dataSource - The DataSource record.
 * @returns {boolean} - Whether the DataSource has a prior contactsConfiguration
 *                      with selections.
 */
function hasPriorConfigWithSelection(dataSource) {
	const contactsConfiguration =
		dataSource.getIn(['provider', 'contactsConfiguration']) || new Map();

	const hasPriorSelection =
		contactsConfiguration.get('enableAllContacts') ||
		contactsConfiguration.get('organizations', new List()).size ||
		contactsConfiguration.get('userGroups', new List()).size;

	return Boolean(contactsConfiguration.size && hasPriorSelection);
}

const getFormattedUsersCount = ({usersCount}) =>
	!isNil(usersCount)
		? sub(Liferay.Language.get('x-contacts'), [usersCount])
		: '';

class SyncItem extends React.Component {
	static defaultProps = {
		contactsCount: 0,
		groupsCount: 0
	};

	static propTypes = {
		contactsCount: PropTypes.number,
		groupLabel: PropTypes.string.isRequired,
		groupsCount: PropTypes.number,
		iconSymbol: PropTypes.string,
		onItemClick: PropTypes.func.isRequired,
		syncAll: PropTypes.bool
	};

	getSecondaryText() {
		const {contactsCount, groupLabel, groupsCount, syncAll} = this.props;

		const groupsSelectedText = sub(Liferay.Language.get('x-x-selected'), [
			syncAll
				? Liferay.Language.get('all')
				: groupsCount.toLocaleString(),
			groupLabel
		]);

		const contactsSelectedText = sub(
			Liferay.Language.get('x-unique-contacts'),
			[contactsCount.toLocaleString()]
		);

		return syncAll || !groupsCount
			? groupsSelectedText
			: `${groupsSelectedText} - ${contactsSelectedText}`;
	}

	render() {
		const {
			groupLabel,
			groupsCount,
			iconSymbol,
			onItemClick,
			syncAll
		} = this.props;

		return (
			<ListGroup.Item
				action
				className={
					this.props.className ? ` ${this.props.className}` : ''
				}
				className='sync-item-root'
				disabled={syncAll}
				flex
				onClick={onItemClick}
			>
				<ListGroup.ItemField>
					<Sticker
						className={getCN({
							'has-selection': syncAll || groupsCount
						})}
						display='light'
						symbol={iconSymbol}
					/>
				</ListGroup.ItemField>

				<ListGroup.ItemField>
					<ListGroup.ItemTitle>
						{sub(Liferay.Language.get('sync-by-x'), [groupLabel])}
					</ListGroup.ItemTitle>

					<ListGroup.ItemText subtext>
						{this.getSecondaryText()}
					</ListGroup.ItemText>
				</ListGroup.ItemField>
			</ListGroup.Item>
		);
	}
}

export class SyncContacts extends React.Component {
	static propTypes = {
		addAlert: PropTypes.func.isRequired,
		close: PropTypes.func.isRequired,
		dataSource: PropTypes.instanceOf(DataSource).isRequired,
		groupId: PropTypes.string.isRequired,
		history: PropTypes.object.isRequired,
		id: PropTypes.string.isRequired,
		open: PropTypes.func.isRequired,
		updateLiferayDataSource: PropTypes.func.isRequired
	};

	state = {
		error: false,
		loading: false,
		selectedOrganizations: [],
		selectedUserGroups: [],
		submitting: false,
		syncAll: false,
		syncCounts: {
			allUsersCount: 0,
			currentUsersCount: 0,
			organizationsUsersCount: 0,
			totalUsersCount: 0,
			userGroupsUsersCount: 0
		}
	};

	componentDidMount() {
		const {
			dataSource: {provider}
		} = this.props;

		this.fetchSyncCounts();

		const contactsConfiguration = provider.get('contactsConfiguration');

		if (contactsConfiguration) {
			this.setState({
				syncAll: contactsConfiguration.get('enableAllContacts')
			});

			this.handleFetchContactsFromConfiguration();
		}
	}

	componentDidUpdate(prevProps, prevState) {
		if (
			hasChanges(
				prevState,
				this.state,
				'selectedOrganizations',
				'selectedUserGroups',
				'syncAll'
			)
		) {
			this.fetchSyncCounts();
		}
	}

	buildSyncItems() {
		const {
			selectedOrganizations,
			selectedUserGroups,
			syncCounts: {organizationsUsersCount, userGroupsUsersCount}
		} = this.state;

		return [
			{
				contactsCount: userGroupsUsersCount,
				groupLabel: Liferay.Language.get('user-groups'),
				groupsCount: selectedUserGroups.length,
				iconSymbol: 'user',
				onItemClick: this.handleSyncUserGroupsModal
			},
			{
				contactsCount: organizationsUsersCount,
				groupLabel: Liferay.Language.get('organizations'),
				groupsCount: selectedOrganizations.length,
				iconSymbol: 'organizations',
				onItemClick: this.handleSyncOrganizationsModal
			}
		];
	}

	@autoCancel
	fetchSyncCounts() {
		const {groupId, id} = this.props;

		return API.dataSource
			.fetchLiferaySyncCounts({
				contactsConfiguration: this.getContactsConfiguration(),
				groupId,
				id
			})
			.then(response => {
				this.setState({
					syncCounts: response
				});
			})
			.catch(err => {
				if (!err.IS_CANCELLATION_ERROR) {
					const serviceError = getServiceError(err);

					if (serviceError) {
						this.handleServicePermissionError(serviceError);
					}
				}
			});
	}

	@autobind
	fetchOrganizations({delta, page, query}) {
		const {groupId, id} = this.props;

		return API.dataSource
			.fetchOrganizations({
				cur: page,
				delta,
				groupId,
				id,
				name: query
			})
			.catch(err => {
				if (!err.IS_CANCELLATION_ERROR) {
					const serviceError = getServiceError(err);

					if (serviceError) {
						this.handleServicePermissionError(serviceError);
					} else {
						throw err;
					}
				}
			});
	}

	@autoCancel
	@autobind
	fetchOrganizationsById(organizationIds) {
		const {groupId, id} = this.props;

		return organizationIds.length
			? API.dataSource.fetchOrganizationsById({
					groupId,
					id,
					organizationIds
			  })
			: Promise.resolve([]);
	}

	@autobind
	fetchUserGroups({delta, page, query}) {
		const {groupId, id} = this.props;

		return API.dataSource
			.fetchUserGroups({
				cur: page,
				delta,
				groupId,
				id,
				name: query
			})
			.catch(err => {
				if (!err.IS_CANCELLATION_ERROR) {
					const serviceError = getServiceError(err);

					if (serviceError) {
						this.handleServicePermissionError(serviceError);
					} else {
						throw err;
					}
				}
			});
	}

	@autoCancel
	@autobind
	fetchUserGroupsById(userGroupIds) {
		const {groupId, id} = this.props;

		return userGroupIds.length
			? API.dataSource.fetchUserGroupsById({
					groupId,
					id,
					userGroupIds
			  })
			: Promise.resolve([]);
	}

	/**
	 * Gets the selected settings for syncing contacts and formats the data.
	 * @return {Object} Contacts configuration object
	 */
	getContactsConfiguration() {
		const {selectedOrganizations, selectedUserGroups, syncAll} = this.state;

		return {
			enableAllContacts: syncAll,
			organizations: selectedOrganizations,
			userGroups: selectedUserGroups
		};
	}

	handleFetchContactsFromConfiguration() {
		const {
			dataSource: {provider}
		} = this.props;

		const contactsConfigurationIMap = provider.get('contactsConfiguration');

		this.setState({
			error: false,
			loading: true
		});

		const userGroupIds = getIdsFromConfiguration(
			contactsConfigurationIMap,
			'userGroups'
		);

		const organizationIds = getIdsFromConfiguration(
			contactsConfigurationIMap,
			'organizations'
		);

		return Promise.all([
			this.fetchUserGroupsById(userGroupIds),
			this.fetchOrganizationsById(organizationIds)
		])
			.then(([userGroups, organizations]) => {
				this.setState({
					loading: false,
					selectedOrganizations: organizations,
					selectedUserGroups: userGroups
				});
			})
			.catch(err => {
				if (!err.IS_CANCELLATION_ERROR) {
					const serviceError = getServiceError(err);

					if (serviceError) {
						this.handleServicePermissionError(serviceError);
					} else {
						this.setState({
							error: true,
							loading: false
						});
					}
				}
			});
	}

	@autobind
	handleServicePermissionError(serviceError) {
		const {addAlert, groupId, history, id} = this.props;

		addAlert(getServiceAlertConfig(serviceError.status));

		history.push(
			toRoute(Routes.SETTINGS_DATA_SOURCE, {
				groupId,
				id
			})
		);
	}

	@autobind
	handleSyncAll() {
		this.setState({
			syncAll: !this.state.syncAll
		});
	}

	@autobind
	handleSyncUserGroupsModal() {
		const {
			props: {close, groupId, open},
			state: {selectedUserGroups}
		} = this;

		open(modalTypes.SEARCHABLE_TABLE_MODAL, {
			columns: [
				{
					accessor: 'name',
					cellRenderer: NameCell,
					cellRendererProps: {
						renderSecondaryInfo: getFormattedUsersCount
					},
					className: 'table-cell-expand',
					label: Liferay.Language.get('user-group-name'),
					sortable: false
				}
			],
			dataSourceFn: this.fetchUserGroups,
			delta: 10,
			entityLabel: Liferay.Language.get('user-groups'),
			groupId,
			instruction: Liferay.Language.get(
				'select-all-user-groups-that-contain-the-contacts-youd-like-to-sync'
			),
			onClose: close,
			onSubmit: selectedItemsList => {
				this.setState({
					selectedUserGroups: selectedItemsList.toArray()
				});

				close();
			},
			requireSelection: !selectedUserGroups.length,
			selectedItems: selectedUserGroups,
			submitMessage: Liferay.Language.get('add'),
			title: Liferay.Language.get('sync-by-user-groups')
		});
	}

	@autobind
	handleSyncOrganizationsModal() {
		const {
			props: {close, groupId, open},
			state: {selectedOrganizations}
		} = this;

		open(modalTypes.SEARCHABLE_TABLE_MODAL, {
			columns: [
				{
					accessor: 'name',
					cellRenderer: NameCell,
					cellRendererProps: {
						renderSecondaryInfo: getFormattedUsersCount
					},
					className: 'table-cell-expand',
					label: Liferay.Language.get('organization-name'),
					sortable: false
				}
			],
			dataSourceFn: this.fetchOrganizations,
			delta: 10,
			entityLabel: Liferay.Language.get('organizations'),
			groupId,
			instruction: Liferay.Language.get(
				'select-all-organizations-that-contain-the-contacts-youd-like-to-sync.-sub-organzations-and-its-contacts-are-not-automatically-selected-by-selecting-the-parent-organzation'
			),
			onClose: close,
			onSubmit: selectedOrganizations => {
				this.setState({
					selectedOrganizations: selectedOrganizations.toArray()
				});

				close();
			},
			requireSelection: !selectedOrganizations.length,
			selectedItems: selectedOrganizations,
			submitMessage: Liferay.Language.get('add'),
			title: Liferay.Language.get('select-contacts-by-organization')
		});
	}

	@autobind
	handleUpdateLiferay() {
		const {
			dataSource: {name},
			groupId,
			history,
			id,
			updateLiferayDataSource
		} = this.props;

		this.setState({
			submitting: true
		});

		updateLiferayDataSource({
			contactsConfiguration: this.getContactsConfiguration(),
			groupId,
			id,
			name
		})
			.then(({payload: dataSource}) => {
				this.setState({
					submitting: false
				});

				const updatedDataSource = new DataSource(fromJS(dataSource));

				if (validContactsConfig(updatedDataSource)) {
					history.push(
						toRoute(Routes.SETTINGS_LIFERAY_CONFIGURE_CONTACTS, {
							groupId,
							id
						})
					);
				} else {
					history.push(
						toRoute(Routes.SETTINGS_LIFERAY_CONFIGURATION_STATUS, {
							groupId,
							id
						})
					);
				}
			})
			.catch(() => {
				this.setState({
					submitting: false
				});
			});
	}

	render() {
		const {
			props: {dataSource, groupId, id, ...otherProps},
			state: {
				error,
				loading,
				selectedOrganizations,
				selectedUserGroups,
				submitting,
				syncAll,
				syncCounts: {allUsersCount, currentUsersCount, totalUsersCount}
			}
		} = this;

		const syncAllLabel = sub(
			Liferay.Language.get('sync-all-x'),
			[
				`(${sub(Liferay.Language.get('x-contacts'), [
					allUsersCount.toLocaleString()
				])})`
			],
			false
		);

		const unsyncedUsersCount = totalUsersCount - currentUsersCount;

		return (
			<BaseDataSourcePage
				{...omitDefinedProps(otherProps, SyncContacts.propTypes)}
				breadcrumbItems={[
					breadcrumbs.getDataSources({groupId}),
					breadcrumbs.getDataSourceName({
						groupId,
						id,
						label: dataSource.name
					}),
					{
						active: true,
						label: Liferay.Language.get('sync-contacts')
					}
				]}
				className='sync-contacts-root'
				dataSource={dataSource}
				documentTitle={dataSource.name}
				groupId={groupId}
				key='SyncContacts'
				pageTitle={Liferay.Language.get('configuration-options')}
				showDelete={false}
			>
				<Sheet>
					<Sheet.Header divider>
						<ProgressTimeline
							activeIndex={STEP - 1}
							items={TITLE_MAP[CONTACTS]}
						/>
					</Sheet.Header>

					{error ? (
						<ErrorDisplay
							key='ERROR'
							onReload={this.handleFetchContactsFromConfiguration}
						/>
					) : (
						<Sheet.Body>
							<Sheet.Section>
								<div className='section-title'>
									<span className='sync-contacts-title'>
										{Liferay.Language.get(
											'sync-all-contacts'
										)}
									</span>

									<Label display='info' size='lg' uppercase>
										{Liferay.Language.get('recommended')}
									</Label>
								</div>

								<span className='secondary-text'>
									{Liferay.Language.get(
										'syncing-all-your-contacts-from-liferay-portal-is-the-best-way-to-get-started-in-analytics-cloud'
									)}
								</span>

								<ListGroup noBorder>
									<ListGroup.Item flex>
										<ListGroup.ItemField>
											<ToggleSwitch
												checked={syncAll}
												name='syncAll'
												onChange={this.handleSyncAll}
											/>
										</ListGroup.ItemField>

										<ListGroup.ItemField>
											<ListGroup.ItemTitle>
												<label htmlFor='syncAll'>
													{syncAllLabel}
												</label>
											</ListGroup.ItemTitle>
										</ListGroup.ItemField>
									</ListGroup.Item>
								</ListGroup>
							</Sheet.Section>

							<Sheet.Section>
								<h4>
									{Liferay.Language.get(
										'sync-by-user-groups-and-organizations'
									)}
								</h4>

								<span className='secondary-text'>
									{Liferay.Language.get(
										'you-can-also-choose-to-sync-by-specific-user-groups-and-organizations.-contacts-belonging-to-multiple-user-groups-and-organizations-are-only-counted-once'
									)}
								</span>

								{loading ? (
									<Spinner spacer />
								) : (
									<ListGroup noBorder>
										{this.buildSyncItems().map(
											(syncItem, i) => (
												<SyncItem
													{...syncItem}
													key={i}
													syncAll={syncAll}
												/>
											)
										)}
									</ListGroup>
								)}

								<div className='total-sync-info'>
									<div className='total-unique'>
										{sub(
											Liferay.Language.get(
												'total-unique-contacts-to-sync-x'
											),
											[totalUsersCount.toLocaleString()]
										)}
									</div>

									<div>
										{sub(
											unsyncedUsersCount < 0
												? Liferay.Language.get(
														'by-saving-this-configuration-,-you-will-be-syncing-x-fewer-contacts'
												  )
												: Liferay.Language.get(
														'by-saving-this-configuration-,-you-will-be-syncing-x-more-contacts'
												  ),
											[
												Math.abs(
													unsyncedUsersCount
												).toLocaleString()
											]
										)}
									</div>
								</div>
							</Sheet.Section>
						</Sheet.Body>
					)}

					<Sheet.Footer divider>
						<FormNavigation
							cancelHref={toRoute(
								Routes.SETTINGS_LIFERAY_CONFIGURATION_STATUS,
								{
									groupId,
									id
								}
							)}
							enableNext={
								!error &&
								(hasPriorConfigWithSelection(dataSource) ||
									!!selectedOrganizations.length ||
									!!selectedUserGroups.length ||
									syncAll)
							}
							onNextStep={this.handleUpdateLiferay}
							submitMessage={Liferay.Language.get(
								'save-and-continue'
							)}
							submitting={submitting}
						/>
					</Sheet.Footer>
				</Sheet>
			</BaseDataSourcePage>
		);
	}
}

export default compose(
	connect(
		null,
		{
			addAlert,
			close,
			open,
			updateLiferayDataSource
		}
	),
	hasRequest,
	withAdminPermission,
	withDataSource,
	redirectIf(dataSourceRedirectFn),
	hasRequest
)(SyncContacts);
