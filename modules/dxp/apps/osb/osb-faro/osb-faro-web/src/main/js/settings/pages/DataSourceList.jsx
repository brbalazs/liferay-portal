import * as API from 'shared/api';
import autobind from 'autobind-decorator';
import BasePage from 'settings/components/BasePage';
import Button from 'shared/components/Button';
import Card from 'shared/components/Card';
import EmbeddedAlertList from 'shared/components/EmbeddedAlertList';
import FaroConstants from 'shared/util/constants';
import Icon from 'shared/components/Icon';
import Label from 'shared/components/Label';
import moment from 'moment';
import Nav from 'shared/components/Nav';
import NoResultsDisplay, {
	getFormattedTitle
} from 'shared/components/NoResultsDisplay';
import React from 'react';
import SearchableEntityTable from 'shared/components/SearchableEntityTable';
import {autoCancel, hasRequest} from 'shared/util/request-decorator';
import {compose, withCurrentUser} from 'shared/hoc';
import {DataSource, User} from 'shared/util/records';
import {
	DATE_CREATED,
	NAME,
	paginationConfig,
	paginationDefaults,
	PROVIDER_TYPE
} from 'shared/util/pagination';
import {fromJS} from 'immutable';

import {get, isNil} from 'lodash';

import {
	getDataSourceDisplayObject,
	validAnalyticsConfig,
	validContactsConfig
} from 'shared/util/data-sources';
import {Link} from 'react-router-dom';

import {PropTypes} from 'prop-types';

import {Routes, toRoute} from 'shared/util/router';
import {sub} from 'shared/util/lang';

const {
	dataSourceStates: {credentialsInvalid, inProgressDeleting, urlInvalid},
	dataSourceStatuses,
	dataSourceTypes: {csv, liferay, salesforce},
	pagination
} = FaroConstants;

function getAlertMessage(dataSource, currentUser, count, groupId) {
	const admin = currentUser.isAdmin();

	const {credentials, id, name} = dataSource;

	const email = get(credentials, ['oAuthOwner', 'emailAddress']);

	if (admin && count === 1) {
		return sub(
			Liferay.Language.get(
				'your-authorization-token-for-x-has-expired.-please-x-your-account-credentials'
			),
			[
				<b key='NAME'>{name}</b>,
				<Link
					key='REAUTHORIZE'
					to={toRoute(Routes.SETTINGS_DATA_SOURCE, {
						groupId,
						id
					})}
				>
					{Liferay.Language.get('reauthorize').toLowerCase()}
				</Link>
			],
			false
		);
	} else if (admin && count > 1) {
		return Liferay.Language.get(
			'some-of-your-authorization-tokens-have-expired.-please-reauthorize-the-account-credentials-on-these-data-sources-to-prevent-syncing-interruptions'
		);
	} else if (count === 1) {
		return sub(
			Liferay.Language.get(
				'your-authorization-token-for-x-has-expired.-please-contact-your-oauth-administrator,-x,-to-reauthorize'
			),
			[<b key='NAME'>{name}</b>, email],
			false
		);
	} else if (count > 1) {
		return Liferay.Language.get(
			'some-of-your-authorization-tokens-have-expired.-please-contact-your-oauth-administrator-to-reauthorize'
		);
	}
}

export const DataSourceName = ({data, hrefFormatter}) => (
	<td className='table-cell-expand'>
		<div className='table-title'>
			{disableRow(data) ? (
				<span className='text-truncate'>{data.name}</span>
			) : (
				<Link className='text-truncate' to={hrefFormatter(data)}>
					{data.name}
				</Link>
			)}
		</div>
	</td>
);

const AnalyticsCell = ({data}) => (
	<td>
		{validAnalyticsConfig(new DataSource(fromJS(data))) && (
			<Icon symbol='check' />
		)}
	</td>
);

const ContactsCell = ({data}) => (
	<td>
		{validContactsConfig(new DataSource(fromJS(data))) &&
			data.status === dataSourceStatuses.active && (
				<Icon symbol='check' />
			)}
	</td>
);

const dateFormatter = date => moment(date).format('ll');
export const disableRow = ({state}) => state === inProgressDeleting;

export const SyncTimeRenderer = ({data}) => {
	const {lastSyncDate} = data;

	return <td>{!isNil(lastSyncDate) ? dateFormatter(lastSyncDate) : '-'}</td>;
};

export const StatusRenderer = ({data}) => {
	const {display, label} = getDataSourceDisplayObject(
		new DataSource(fromJS(data)),
		true
	);

	return (
		<td>
			<Label display={display} uppercase>
				{label}
			</Label>
		</td>
	);
};

const typeFormatter = type => {
	switch (type) {
		case csv:
			return Liferay.Language.get('.csv');
		case liferay:
			return Liferay.Language.get('liferay-portal');
		case salesforce:
			return Liferay.Language.get('salesforce');
		default:
			return '';
	}
};

export class DataSourceList extends React.Component {
	static defaultProps = {
		...paginationDefaults,
		orderByField: NAME
	};

	static propTypes = {
		...paginationConfig,
		currentUser: PropTypes.instanceOf(User).isRequired,
		groupId: PropTypes.string.isRequired,
		history: PropTypes.object.isRequired,
		orderByField: PropTypes.string
	};

	state = {
		alerts: []
	};

	componentDidMount() {
		this.getAlerts();
	}

	@autoCancel
	@autobind
	fetchDataSources(data) {
		const {delta, orderBy, orderByField, page, query} = data;

		return API.dataSource.search({
			cur: page,
			delta,
			groupId: this.props.groupId,
			orderByFields: [
				{
					fieldName: orderByField,
					orderBy
				}
			],
			query
		});
	}

	fetchInvalidDataSources() {
		const {groupId} = this.props;

		return API.dataSource.search({
			cur: pagination.cur,
			delta: 1,
			groupId,
			states: [credentialsInvalid, urlInvalid]
		});
	}

	getAlerts() {
		const {currentUser, groupId} = this.props;

		this.fetchInvalidDataSources().then(({items, total}) => {
			const alerts = [];

			if (total) {
				alerts.push({
					iconSymbol: 'warning-full',
					message: getAlertMessage(
						items[0],
						currentUser,
						total,
						groupId
					),
					title: Liferay.Language.get('warning'),
					type: 'warning'
				});
			}

			this.setState({
				alerts
			});
		});
	}

	@autobind
	getDataSourceURL(dataSource) {
		return toRoute(Routes.SETTINGS_DATA_SOURCE, {
			groupId: this.props.groupId,
			id: dataSource.id
		});
	}

	@autobind
	handleRowClick(dataSource) {
		const {history} = this.props;

		history.push(this.getDataSourceURL(dataSource));
	}

	@autobind
	renderNav(checkedItemsISet) {
		const {groupId} = this.props;

		if (checkedItemsISet.isEmpty()) {
			return (
				<Nav>
					<Nav.Item>
						<Button
							className='nav-btn'
							display='primary'
							href={toRoute(Routes.SETTINGS_ADD_DATA_SOURCE, {
								groupId
							})}
						>
							{Liferay.Language.get('add-data-source')}
						</Button>
					</Nav.Item>
				</Nav>
			);
		}
	}

	@autobind
	renderNoResults(query) {
		const {currentUser, groupId} = this.props;

		const authorized = currentUser.isAdmin();

		const connectMessage = authorized
			? Liferay.Language.get(
					'please-connect-people-data-sources-to-start-using-analytics-cloud'
			  )
			: Liferay.Language.get(
					'please-contact-your-workspace-administrator-to-add-data-sources'
			  );

		if (query) {
			return (
				<NoResultsDisplay
					icon={{symbol: 'sheets'}}
					title={getFormattedTitle(
						Liferay.Language.get('data-sources')
					)}
				/>
			);
		} else {
			return (
				<NoResultsDisplay
					description={connectMessage}
					primary
					title={Liferay.Language.get('no-data-sources-connected')}
				>
					{authorized && (
						<Button
							display='primary'
							href={toRoute(Routes.SETTINGS_ADD_DATA_SOURCE, {
								groupId
							})}
						>
							{Liferay.Language.get('connect-data-source')}
						</Button>
					)}
				</NoResultsDisplay>
			);
		}
	}

	render() {
		const {
			props: {
				currentUser,
				delta,
				filterBy,
				groupId,
				orderBy,
				orderByField,
				page,
				query
			},
			state: {alerts}
		} = this;

		return (
			<BasePage
				className={
					this.props.className ? ` ${this.props.className}` : ''
				}
				groupId={groupId}
				key='dataSourceListpage'
				pageDescription={Liferay.Language.get(
					'manage-data-sources-that-are-synced-with-analytics-cloud'
				)}
				pageTitle={Liferay.Language.get('data-sources')}
			>
				<EmbeddedAlertList alerts={alerts} />

				<Card pageDisplay>
					<SearchableEntityTable
						checkDisabled={disableRow}
						columns={[
							{
								accessor: 'name',
								cellRenderer: DataSourceName,
								cellRendererProps: {
									hrefFormatter: this.getDataSourceURL
								},
								label: Liferay.Language.get('name')
							},
							{
								accessor: 'provider.type',
								dataFormatter: typeFormatter,
								label: Liferay.Language.get('source')
							},
							{
								cellRenderer: ContactsCell,
								label: Liferay.Language.get('contacts'),
								sortable: false
							},
							{
								cellRenderer: AnalyticsCell,
								label: Liferay.Language.get('analytics'),
								sortable: false
							},
							{
								accessor: 'dateCreated',
								dataFormatter: dateFormatter,
								label: Liferay.Language.get('date-added')
							},
							{
								cellRenderer: SyncTimeRenderer,
								label: Liferay.Language.get('last-synced'),
								sortable: false
							},
							{
								cellRenderer: StatusRenderer,
								label: Liferay.Language.get('status'),
								sortable: false
							}
						]}
						dataSourceFn={this.fetchDataSources}
						delta={Number(delta)}
						entityLabel={Liferay.Language.get('data-sources')}
						filterBy={filterBy}
						navRenderer={
							currentUser.isAdmin() ? this.renderNav : null
						}
						noResultsName={Liferay.Language.get('data-sources')}
						noResultsRenderer={this.renderNoResults}
						orderBy={orderBy}
						orderByField={orderByField}
						orderByOptions={[
							{
								label: Liferay.Language.get('name'),
								value: NAME
							},
							{
								label: Liferay.Language.get('source'),
								value: PROVIDER_TYPE
							},
							{
								label: Liferay.Language.get('date-added'),
								value: DATE_CREATED
							}
						]}
						page={Number(page)}
						query={query}
						rowIdentifier='id'
						showCheckbox={false}
					/>
				</Card>
			</BasePage>
		);
	}
}

export default compose(
	withCurrentUser,
	hasRequest
)(DataSourceList);
