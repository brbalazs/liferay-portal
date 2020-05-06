import * as API from 'shared/api';
import * as breadcrumbs from 'shared/util/breadcrumbs';
import autobind from 'autobind-decorator';
import BaseDataSourcePage from '../../../components/data-source/BasePage';
import ErrorDisplay from 'shared/components/ErrorDisplay';
import FormNavigation from 'settings/components/FormNavigation';
import getCN from 'classnames';
import omitDefinedProps from 'shared/util/omitDefinedProps';
import ProgressTimeline from 'shared/components/ProgressTimeline';
import React from 'react';
import SearchableTableWithStaged from 'shared/components/searchable-table-with-staged';
import Sheet from 'shared/components/Sheet';
import {
	ACTION_TYPES,
	SelectionContext,
	withSelectionProvider
} from 'shared/context/selection';
import {addAlert} from 'shared/actions/alerts';
import {ANALYTICS, Routes, toRoute} from 'shared/util/router';
import {autoCancel, hasRequest} from 'shared/util/request-decorator';
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
	LIFERAY_SITE_TYPE,
	validAnalyticsConfig
} from 'shared/util/data-sources';
import {get} from 'lodash';
import {getServiceError} from 'shared/util/request';
import {List} from 'immutable';
import {NameCell} from 'shared/components/table/cell-components';
import {PropTypes} from 'prop-types';
import {TITLE_MAP} from './SyncContacts';
import {updateLiferayDataSource} from 'shared/actions/data-sources';

const STEP = 1;

export class SyncSites extends React.Component {
	static contextType = SelectionContext;

	static propTypes = {
		addAlert: PropTypes.func.isRequired,
		dataSource: PropTypes.instanceOf(DataSource).isRequired,
		groupId: PropTypes.string.isRequired,
		history: PropTypes.object.isRequired,
		id: PropTypes.string.isRequired,
		updateLiferayDataSource: PropTypes.func.isRequired
	};

	state = {
		error: false,
		loading: false,
		submitting: false
	};

	componentDidMount() {
		const {dataSource} = this.props;

		if (validAnalyticsConfig(dataSource)) {
			this.handleFetchSitesFromConfiguration();
		}
	}

	@autobind
	getSitesDataSource({delta, page, query}) {
		const {groupId, id} = this.props;

		return API.dataSource
			.fetchSites({
				cur: page,
				delta,
				groupId,
				id,
				name: query
			})
			.then(({disableSearch, items, total}) => ({
				disableSearch,
				items: items.map(item => ({
					type: LIFERAY_SITE_TYPE,
					...item
				})),
				total
			}))
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
	handleFetchSitesFromConfiguration() {
		const {
			context: {selectionDispatch},
			props: {
				dataSource: {provider},
				groupId,
				id
			}
		} = this;

		const analyticsConfigurationIMap = provider.get(
			'analyticsConfiguration'
		);

		this.setState({
			error: false,
			loading: true
		});

		return API.dataSource
			.fetchSitesById({
				groupId,
				id,
				siteIds: getIdsFromConfiguration(
					analyticsConfigurationIMap,
					'sites'
				)
			})
			.then(sites => {
				selectionDispatch({
					payload: {items: sites},
					type: ACTION_TYPES.add
				});

				this.setState({
					loading: false
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
	handleUpdateLiferay() {
		const {
			context: {selectedItems: selectedSitesIOMap},
			props: {
				dataSource: {name},
				groupId,
				history,
				id,
				updateLiferayDataSource
			}
		} = this;

		this.setState({
			submitting: true
		});

		updateLiferayDataSource({
			analyticsConfiguration: {
				enableAllSites: false,
				sites: selectedSitesIOMap.toArray()
			},
			groupId,
			id,
			name
		})
			.then(({payload: {id}}) => {
				this.setState({
					submitting: false
				});

				history.push(
					toRoute(Routes.SETTINGS_LIFERAY_CONFIGURATION_STATUS, {
						groupId,
						id
					})
				);
			})
			.catch(() => {
				this.setState({
					submitting: false
				});
			});
	}

	render() {
		const {
			context: {selectedItems: selectedSitesIOMap},
			props: {className, dataSource, groupId, id, ...otherProps},
			state: {error, loading, submitting}
		} = this;

		const sitesIList = dataSource.getIn(
			['provider', 'analyticsConfiguration', 'sites'],
			new List()
		);

		return (
			<BaseDataSourcePage
				{...omitDefinedProps(otherProps, SyncSites.propTypes)}
				breadcrumbItems={[
					breadcrumbs.getDataSources({groupId}),
					breadcrumbs.getDataSourceName({
						groupId,
						id,
						label: dataSource.name
					}),
					{
						active: true,
						label: Liferay.Language.get('configure-sites')
					}
				]}
				className={getCN('sync-sites-root', className)}
				dataSource={dataSource}
				documentTitle={dataSource.name}
				groupId={groupId}
				key='SyncSites'
				pageTitle={Liferay.Language.get('configuration-options')}
				showDelete={false}
			>
				<Sheet>
					<Sheet.Header divider>
						<ProgressTimeline
							activeIndex={STEP - 1}
							items={TITLE_MAP[ANALYTICS]}
						/>
					</Sheet.Header>

					<Sheet.Body>
						<h3>
							{Liferay.Language.get(
								'register-sites-for-analytics'
							)}
						</h3>

						<p>
							{Liferay.Language.get(
								'select-sites-to-register-for-use-in-analytics.-analytics-will-sync-all-pages-and-assets-within-the-sites-selected-for-tracking-in-analytics-cloud'
							)}
						</p>
					</Sheet.Body>

					{error ? (
						<ErrorDisplay
							key='ERROR'
							onReload={this.handleFetchSitesFromConfiguration}
						/>
					) : (
						<SearchableTableWithStaged
							{...omitDefinedProps(
								otherProps,
								SyncSites.propTypes
							)}
							columns={[
								{
									accessor: 'name',
									cellRenderer: NameCell,
									cellRendererProps: {
										renderSecondaryInfo: data =>
											get(data, 'friendlyURL')
									},
									className: 'table-cell-expand',
									label: Liferay.Language.get('name'),
									sortable: false
								}
							]}
							dataSourceFn={this.getSitesDataSource}
							entityLabel={Liferay.Language.get('sites')}
							groupId={groupId}
							loadingOverride={loading}
							noResultIcon='sites'
							rowIdentifier='id'
							showCheckbox
						/>
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
								(!sitesIList.isEmpty() ||
									!selectedSitesIOMap.isEmpty())
							}
							onNextStep={this.handleUpdateLiferay}
							submitMessage={Liferay.Language.get('configure')}
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
			updateLiferayDataSource
		}
	),
	withAdminPermission,
	withDataSource,
	redirectIf(dataSourceRedirectFn),
	hasRequest,
	withSelectionProvider
)(SyncSites);
