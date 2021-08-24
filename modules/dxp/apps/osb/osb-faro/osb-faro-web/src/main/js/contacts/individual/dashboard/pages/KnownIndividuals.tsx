import * as API from 'shared/api';
import autobind from 'autobind-decorator';
import Button from 'shared/components/Button';
import Card from 'shared/components/Card';
import Nav from 'shared/components/Nav';
import NoResultsDisplay, {
	getFormattedTitle
} from 'shared/components/NoResultsDisplay';
import PropTypes from 'prop-types';
import React from 'react';
import SearchableTableWithStaged from 'shared/components/searchable-table-with-staged';
import Spinner from 'shared/components/Spinner';
import {
	ACTION_TYPES,
	SelectionContext,
	withSelectionProvider
} from 'shared/context/selection';
import {
	ACTIVITIES_COUNT,
	buildOrderByFields,
	JOB_TITLE,
	LAST_ACTIVITY_DATE,
	NAME,
	paginationConfig,
	paginationDefaults
} from 'shared/util/pagination';
import {addAlert} from 'shared/actions/alerts';
import {Alert} from 'shared/types';
import {autoCancel, hasRequest} from 'shared/util/request-decorator';
import {close, modalTypes, open} from 'shared/actions/modals';
import {compose, withCurrentUser} from 'shared/hoc';
import {connect, ConnectedProps} from 'react-redux';
import {EntityTypes, SegmentTypes} from 'shared/util/constants';
import {INDIVIDUALS} from 'shared/util/router';
import {individualsListColumns} from 'shared/util/table-columns';
import {isNil, noop} from 'lodash';
import {OrderParams, User} from 'shared/util/records';
import {RootState} from 'shared/store';
import {Routes, toRoute} from 'shared/util/router';
import {sub} from 'shared/util/lang';

const getIndividualsDataSource = ({
	channelId,
	delta,
	groupId,
	orderBy,
	orderByField,
	page,
	query
}) =>
	API.individuals.search({
		channelId,
		delta,
		groupId,
		orderByFields: buildOrderByFields(
			new OrderParams({field: orderByField, sortOrder: orderBy}),
			INDIVIDUALS
		),
		page,
		query
	});

const connector = connect(
	(store: RootState, {groupId}: {groupId: string}) => ({
		timeZoneId: store.getIn([
			'projects',
			groupId,
			'data',
			'timeZone',
			'timeZoneId'
		])
	}),
	{addAlert, close, open}
);

type PropsFromRedux = ConnectedProps<typeof connector>;

interface IKnownIndividualsProps
	extends React.HTMLAttributes<HTMLDivElement>,
		PropsFromRedux {
	channelId: string;
	currentUser: {
		isAdmin: () => boolean;
		isMember: () => boolean;
		isOwner: () => boolean;
	};
	delta: string;
	groupId: string;
	orderBy?: string;
	orderByField?: string;
	page?: string;
	query?: string;
}

interface IKnownIndividualsState {
	dataSourceLoading: boolean;
	dataSourceTotal: number;
}

@hasRequest
export class KnownIndividuals extends React.Component<
	IKnownIndividualsProps,
	IKnownIndividualsState
> {
	static contextType = SelectionContext;

	static defaultProps = {
		...paginationDefaults,
		orderByField: NAME
	};

	static propTypes = {
		...paginationConfig,
		addAlert: PropTypes.func.isRequired,
		channelId: PropTypes.string,
		close: PropTypes.func.isRequired,
		currentUser: PropTypes.instanceOf(User).isRequired,
		groupId: PropTypes.string.isRequired,
		open: PropTypes.func.isRequired,
		timeZoneId: PropTypes.string
	};

	state = {
		dataSourceLoading: false,
		dataSourceTotal: null
	};

	componentDidMount() {
		this.fetchDataSources();
	}

	@autobind
	addToSegment(selectedSegmentsList, idsArray) {
		const {
			context: {selectionDispatch},
			props: {addAlert, groupId}
		} = this;

		const selectedSegmentId = selectedSegmentsList[0].id;

		return API.individualSegment
			.addIndividuals({
				groupId,
				individualIds: idsArray,
				selectedSegmentId
			})
			.then(() => {
				addAlert({
					alertType: Alert.Types.Success,
					message: sub(
						Liferay.Language.get(
							'x-individuals-have-been-added-to-this-static-segment'
						),
						[idsArray.length]
					)
				});

				selectionDispatch({type: ACTION_TYPES.clearAll});
			})
			.catch(error => {
				addAlert({
					alertType: Alert.Types.Error,
					message: Liferay.Language.get(
						'an-unexpected-error-occurred'
					)
				});

				return error;
			});
	}

	@autoCancel
	fetchDataSources() {
		const {groupId} = this.props;

		this.setState({
			dataSourceLoading: true
		});

		return API.dataSource
			.search({
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

	@autobind
	getStaticIndividualSegments({cur, delta, orderBy, query}) {
		const {channelId, groupId} = this.props;

		return API.individualSegment.search({
			channelId,
			delta,
			groupId,
			orderByFields: [
				{
					fieldName: NAME,
					orderBy,
					system: true
				}
			],
			page: cur,
			query,
			segmentType: SegmentTypes.Static
		});
	}

	@autobind
	handleAddIndividualsToSegmentModal(idsArray) {
		const {close, groupId, open} = this.props;

		return () =>
			open(modalTypes.SELECT_ITEMS_MODAL, {
				countLabel: Liferay.Language.get('x-segments'),
				dataSourceFn: this.getStaticIndividualSegments,
				entityType: EntityTypes.IndividualsSegment,
				groupId,
				noResultsIcon: 'ac-segment',
				noResultsName: Liferay.Language.get('static-segments'),
				onClose: close,
				onSubmit: selectedSegmentsList =>
					this.addToSegment(selectedSegmentsList, idsArray),
				selectMultiple: false,
				submitMessage: Liferay.Language.get('add'),
				title: Liferay.Language.get('add-to-static-segment')
			});
	}

	isDataSourceConnected() {
		return this.state.dataSourceTotal > 0;
	}

	@autobind
	renderNav(selectedItemsIOMap) {
		if (this.isDataSourceConnected() && !selectedItemsIOMap.isEmpty()) {
			return (
				<Nav>
					<Nav.Item key='PRIMARY_ACTION'>
						<Button
							className='nav-btn'
							display='primary'
							onClick={this.handleAddIndividualsToSegmentModal(
								selectedItemsIOMap.keySeq().toArray()
							)}
						>
							{Liferay.Language.get('add-to-static-segment')}
						</Button>
					</Nav.Item>
				</Nav>
			);
		}
	}

	@autobind
	renderNoResults(query, activeFilters) {
		const {
			props: {currentUser, groupId},
			state: {dataSourceLoading, dataSourceTotal}
		} = this;

		const authorized = currentUser.isAdmin();

		const createDataSourceButton = (
			<Button
				display='primary'
				href={toRoute(Routes.SETTINGS_ADD_DATA_SOURCE, {
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
					title={getFormattedTitle(
						Liferay.Language.get('individuals')
					)}
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
					description={
						authorized
							? Liferay.Language.get(
									'please-connect-a-data-source-with-people-data.-if-this-problem-persists-visit-the-documentation-for-connecting-data-sources'
							  )
							: Liferay.Language.get(
									'please-contact-your-site-administrator-to-add-a-data-source-with-people-data'
							  )
					}
					primary
					title={Liferay.Language.get(
						'no-individuals-sycned-from-data-sources'
					)}
				>
					{authorized && createDataSourceButton}
				</NoResultsDisplay>
			);
		}
	}

	render() {
		const {
			channelId,
			currentUser,
			delta,
			groupId,
			orderBy,
			orderByField,
			page,
			query,
			timeZoneId
		} = this.props;

		return (
			<div className='individuals-dashboard-known-individuals-root'>
				<div className='row'>
					<div className='col-xl-12'>
						<Card pageDisplay>
							<SearchableTableWithStaged
								columns={[
									individualsListColumns.getNameEmail({
										channelId,
										groupId
									}),
									individualsListColumns.jobTitle,
									individualsListColumns.activitiesCount,
									individualsListColumns.getLastActivityDate(
										timeZoneId
									)
								]}
								currentUser={currentUser}
								dataSourceFn={getIndividualsDataSource}
								dataSourceParams={{channelId, groupId}}
								delta={Number(delta)}
								entityLabel={Liferay.Language.get(
									'individuals'
								)}
								navRenderer={this.renderNav}
								noResultsRenderer={this.renderNoResults}
								orderBy={orderBy}
								orderByField={orderByField}
								orderByOptions={[
									{
										label: Liferay.Language.get('name'),
										value: NAME
									},
									{
										label: Liferay.Language.get(
											'job-title'
										),
										value: JOB_TITLE
									},
									{
										label: Liferay.Language.get(
											'total-activities'
										),
										value: ACTIVITIES_COUNT
									},
									{
										label: Liferay.Language.get(
											'last-activity'
										),
										value: LAST_ACTIVITY_DATE
									}
								]}
								page={Number(page)}
								query={query}
								showCheckbox
							/>
						</Card>
					</div>{' '}
				</div>
			</div>
		);
	}
}

export default compose<any>(
	withCurrentUser,
	connector,
	withSelectionProvider
)(KnownIndividuals);
