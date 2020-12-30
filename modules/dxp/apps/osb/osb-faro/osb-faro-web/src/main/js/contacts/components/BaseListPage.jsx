import * as API from 'shared/api';
import * as breadcrumbs from 'shared/util/breadcrumbs';
import autobind from 'autobind-decorator';
import BasePage from 'shared/components/base-page';
import Button from 'shared/components/Button';
import Card from 'shared/components/Card';
import EmbeddedAlertList from 'shared/components/EmbeddedAlertList';
import FaroConstants from 'shared/util/constants';
import Nav from 'shared/components/Nav';
import NoResultsDisplay, {
	getFormattedTitle
} from 'shared/components/NoResultsDisplay';
import omitDefinedProps from 'shared/util/omitDefinedProps';
import React from 'react';
import SearchableEntityTable from 'shared/components/SearchableEntityTable';
import Spinner from 'shared/components/Spinner';
import {autoCancel, hasRequest} from 'shared/util/request-decorator';
import {ChannelContext} from 'shared/context/channel';
import {get, isNil, noop} from 'lodash';
import {NAME} from 'shared/util/pagination';
import {PropTypes} from 'prop-types';
import {Routes, toRoute} from 'shared/util/router';
import {User} from 'shared/util/records';
const {pagination} = FaroConstants;

@hasRequest
export default class BaseListPage extends React.Component {
	static contextType = ChannelContext;

	static defaultProps = {
		alerts: [],
		hideNav: false,
		orderByOptions: [
			{
				label: Liferay.Language.get('name'),
				value: NAME
			}
		],
		rowIdentifier: 'id',
		showCheckbox: false
	};

	static propTypes = {
		alerts: PropTypes.array,
		channelId: PropTypes.string,
		className: PropTypes.string,
		columns: PropTypes.array.isRequired,
		currentUser: PropTypes.instanceOf(User).isRequired,
		dataSourceFn: PropTypes.func.isRequired,
		entityLabel: PropTypes.string,
		groupId: PropTypes.string.isRequired,
		hideNav: PropTypes.bool,
		icon: PropTypes.string,
		noResultsConfig: PropTypes.shape({
			content: PropTypes.any,
			description: PropTypes.string,
			title: PropTypes.string
		}),
		orderByOptions: PropTypes.array,
		pageActions: PropTypes.array,
		pageActionsLabel: PropTypes.string,
		renderSelectedAction: PropTypes.func,
		rowIdentifier: PropTypes.string,
		showCheckbox: PropTypes.bool
	};

	state = {
		dataSourceLoading: false,
		dataSourceTotal: null
	};

	constructor(props) {
		super(props);

		this._tableRef = React.createRef();
	}

	componentDidMount() {
		this.fetchDataSources();
	}

	@autoCancel
	fetchDataSources() {
		const {groupId} = this.props;

		this.setState({
			dataSourceLoading: true
		});

		return API.dataSource
			.search({
				cur: pagination.cur,
				delta: 1,
				groupId
			})
			.then(({total}) => {
				this.setState({
					dataSourceLoading: false,
					dataSourceTotal: total
				});
			})
			.catch(noop);
	}

	isDataSourceConnected() {
		return this.state.dataSourceTotal > 0;
	}

	/**
	 * Public method for refreshing data
	 */
	reload() {
		this._tableRef.current.reload();
	}

	@autobind
	renderNav(checkedItemsISet) {
		const {hideNav, renderSelectedAction} = this.props;

		if (
			this.isDataSourceConnected() &&
			!hideNav &&
			renderSelectedAction &&
			!checkedItemsISet.isEmpty()
		) {
			return (
				<Nav>
					<Nav.Item key='PRIMARY_ACTION'>
						{renderSelectedAction(checkedItemsISet)}
					</Nav.Item>
				</Nav>
			);
		}
	}

	@autobind
	renderNoResults(query, activeFilters) {
		const {
			props: {
				channelId,
				currentUser,
				entityLabel,
				groupId,
				icon,
				noResultsConfig
			},
			state: {dataSourceLoading, dataSourceTotal}
		} = this;

		const authorized = currentUser.isAdmin();

		const createDataSourceButton = (
			<Button
				display='primary'
				href={toRoute(Routes.SETTINGS_ADD_DATA_SOURCE, {
					channelId,
					groupId
				})}
			>
				{Liferay.Language.get('connect-data-source')}
			</Button>
		);

		if (dataSourceLoading || isNil(dataSourceTotal)) {
			return (
				<NoResultsDisplay>
					<Spinner key='DATA_SOURCE_SPINNER' overlay />
				</NoResultsDisplay>
			);
		} else if (query || activeFilters) {
			return (
				<NoResultsDisplay
					icon={icon && {symbol: icon}}
					title={getFormattedTitle(entityLabel)}
				/>
			);
		} else if (!this.isDataSourceConnected()) {
			return (
				<NoResultsDisplay
					description={
						authorized
							? Liferay.Language.get(
									'please-connect-people-data-sources-to-start-using-analytics-cloud'
							  )
							: Liferay.Language.get(
									'please-contact-your-site-administrator-to-add-people-data-sources'
							  )
					}
					primary
					title={Liferay.Language.get('no-data-sources-connected')}
				>
					{authorized && createDataSourceButton}
				</NoResultsDisplay>
			);
		} else {
			return (
				<NoResultsDisplay
					description={get(noResultsConfig, 'description')}
					primary
					title={get(noResultsConfig, 'title')}
				>
					{get(noResultsConfig, 'content') ||
						(authorized && createDataSourceButton)}
				</NoResultsDisplay>
			);
		}
	}

	render() {
		const {
			alerts,
			channelId,
			className,
			columns,
			dataSourceFn,
			entityLabel,
			groupId,
			orderByOptions,
			pageActions,
			pageActionsLabel,
			rowIdentifier,
			showCheckbox,
			...otherProps
		} = this.props;

		const {selectedChannel} = this.context;

		return (
			<BasePage className={className} documentTitle={entityLabel}>
				<BasePage.Header
					breadcrumbs={[
						breadcrumbs.getHome({
							channelId,
							groupId,
							label: selectedChannel && selectedChannel.name
						})
					]}
					groupId={groupId}
				>
					<BasePage.Row>
						<BasePage.Header.TitleSection title={entityLabel} />

						<BasePage.Header.Section>
							<BasePage.Header.PageActions
								actions={pageActions}
								label={pageActionsLabel}
							/>
						</BasePage.Header.Section>
					</BasePage.Row>
				</BasePage.Header>

				<BasePage.Body>
					<EmbeddedAlertList alerts={alerts} />

					<Card pageDisplay>
						<Card.Body noPadding>
							<SearchableEntityTable
								{...omitDefinedProps(
									otherProps,
									BaseListPage.propTypes
								)}
								columns={columns}
								dataSourceFn={dataSourceFn}
								dataSourceParams={{channelId, groupId}}
								entityLabel={entityLabel}
								navRenderer={this.renderNav}
								noResultsRenderer={this.renderNoResults}
								orderByOptions={orderByOptions}
								ref={this._tableRef}
								rowIdentifier={rowIdentifier}
								showCheckbox={showCheckbox}
							/>
						</Card.Body>
					</Card>
				</BasePage.Body>
			</BasePage>
		);
	}
}
