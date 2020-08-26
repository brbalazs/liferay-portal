import * as API from 'shared/api';
import ActivitiesChart from '../../../components/ActivitiesChart';
import autobind from 'autobind-decorator';
import Button from 'shared/components/Button';
import Card from 'shared/components/Card';
import CardTabs from 'shared/components/CardTabs';
import Constants from 'shared/util/constants';
import EngagementChart from 'contacts/components/EngagementChart';
import Promise from 'metal-promise';
import React from 'react';
import SearchableVerticalTimeline from 'shared/components/SearchableVerticalTimeline';
import {ACTIVITIES, Routes} from 'shared/util/router';
import {autoCancel, hasRequest} from 'shared/util/request-decorator';
import {
	buildTabItems,
	getSafeRangeKey,
	INTERVAL_MAP
} from 'shared/util/engagement-activity';
import {formatEngagementAggregation} from 'shared/util/engagement';
import {formatSessions, getActivityLabel} from 'shared/util/activities';
import {
	formatUTCDateFromUnix,
	getDateRangeLabel,
	getFirstDate,
	getLastDate
} from 'shared/util/date';
import {getSafeChange} from 'shared/util/change';
import {Individual} from 'shared/util/records';
import {omit} from 'lodash';
import {PropTypes} from 'prop-types';
import {START_TIME} from 'shared/util/pagination';
import {sub} from 'shared/util/lang';
import {withSelectedPoint, withStatefulPagination} from 'shared/hoc';
import {WrapSafeResults} from 'shared/hoc/util';

const {
	entityTypes: {individual},
	pagination: {orderDescending}
} = Constants;

const SearchableVerticalTimelineHOC = withStatefulPagination(
	SearchableVerticalTimeline,
	null,
	props => omit(props, 'onSearchValueChange')
);

function getActivities(params) {
	const {
		channelId,
		contactsEntityId,
		delta,
		endDate,
		groupId,
		page,
		query,
		startDate
	} = params;

	return API.activities
		.fetchGroup({
			channelId,
			contactsEntityId,
			contactsEntityType: individual,
			cur: page,
			delta,
			endDate,
			groupId,
			orderByFields: [{fieldName: START_TIME, orderBy: orderDescending}],
			query,
			startDate
		})
		.then(({items, total}) => ({
			items: formatSessions(items, groupId, channelId),
			total
		}));
}

@hasRequest
export class IndividualProfileCard extends React.Component {
	static defaultProps = {
		tabId: ACTIVITIES
	};

	static propTypes = {
		entity: PropTypes.instanceOf(Individual).isRequired,
		groupId: PropTypes.string.isRequired,
		hasSelectedPoint: PropTypes.bool,
		onPointSelect: PropTypes.func.isRequired,
		selectedPoint: PropTypes.number,
		tabId: PropTypes.string
	};

	state = {
		activityChange: 0,
		activityCount: 0,
		activityHistory: [],
		engagementChange: 0,
		engagementHistory: [],
		error: false,
		history: [],
		loading: true
	};

	constructor(props) {
		super(props);

		this._chartRef = React.createRef();
		this._searchableVerticalTimelineRef = React.createRef();
	}

	componentDidMount() {
		this.handleFetchHistory();
	}

	componentDidUpdate(prevProps) {
		const {interval, rangeSelectors} = this.props;

		if (
			prevProps.rangeSelectors !== rangeSelectors ||
			prevProps.interval !== interval
		) {
			this.handleFetchHistory();
		}
	}

	@autoCancel
	@autobind
	getActivityHistory() {
		const {
			channelId,
			entity: {id},
			groupId,
			interval,
			rangeSelectors
		} = this.props;

		return API.activities.fetchHistory({
			channelId,
			contactsEntityId: id,
			contactsEntityType: individual,
			groupId,
			interval: INTERVAL_MAP[interval],
			max: getSafeRangeKey(rangeSelectors.rangeKey),
			...rangeSelectors
		});
	}

	getDateRange() {
		const {
			props: {hasSelectedPoint, selectedPoint, tabId},
			state: {activityHistory, engagementHistory}
		} = this;

		const history =
			tabId === ACTIVITIES ? activityHistory : engagementHistory;

		if (!hasSelectedPoint) {
			return {
				endDate: getLastDate(history, 'intervalInitDate'),
				startDate: getFirstDate(history, 'intervalInitDate')
			};
		}

		const {intervalInitDate} = history[selectedPoint] || {};

		return {endDate: intervalInitDate, startDate: intervalInitDate};
	}

	@autoCancel
	@autobind
	getEngagementHistory() {
		const {
			entity: {id},
			groupId
		} = this.props;

		return API.engagement.fetchHistory({
			contactsEntityId: id,
			contactsEntityType: individual,
			groupId
		});
	}

	@autobind
	handleChartSelect({index}) {
		this._searchableVerticalTimelineRef.current.resetPage();

		this.props.onPointSelect({index});
	}

	@autobind
	handleClearSelection() {
		const {_chartRef, _searchableVerticalTimelineRef} = this;

		_chartRef.current.unselect();

		_searchableVerticalTimelineRef.current.resetPage();

		this.props.onPointSelect({index: null});
	}

	@autobind
	handleFetchHistory() {
		this.setState({
			error: false,
			loading: true
		});

		Promise.all([this.getActivityHistory(), this.getEngagementHistory()])
			.then(([activity, engagement]) => {
				const {
					activityAggregations: activityHistory,
					change: activityChange,
					count: activityCount
				} = activity;

				const {
					change: engagementChange,
					engagementAggregations
				} = engagement;

				const engagementHistory = engagementAggregations.map(
					formatEngagementAggregation
				);

				this.setState({
					activityChange: getSafeChange(activityChange),
					activityCount,
					activityHistory,
					engagementChange: getSafeChange(engagementChange),
					engagementHistory,
					loading: false
				});
			})
			.catch(err => {
				if (!err.IS_CANCELLATION_ERROR) {
					this.setState({
						error: true,
						loading: false
					});
				}
			});
	}

	renderChart() {
		const {
			_chartRef,
			props: {
				hasSelectedPoint,
				interval,
				rangeSelectors,
				selectedPoint,
				tabId
			},
			state: {activityHistory, engagementHistory}
		} = this;

		const history =
			tabId === ACTIVITIES ? activityHistory : engagementHistory;
		const SelectedChart =
			tabId === ACTIVITIES ? ActivitiesChart : EngagementChart;
		const {intervalInitDate, totalElements} = history[selectedPoint] || {};

		return (
			<div className='individuals-activities-chart'>
				<SelectedChart
					forwardedRef={_chartRef}
					history={history}
					interval={interval}
					onPointSelect={this.handleChartSelect}
					rangeSelectors={rangeSelectors}
				/>

				<div className='selected-info'>
					{hasSelectedPoint ? (
						<>
							<div className='d-flex align-items-baseline'>
								<h4>
									{sub(
										Liferay.Language.get(
											'individuals-activities-x'
										),
										[
											formatUTCDateFromUnix(
												intervalInitDate
											)
										]
									)}
								</h4>

								<Button
									display='link'
									onClick={this.handleClearSelection}
									size='sm'
								>
									{Liferay.Language.get(
										'clear-date-selection'
									)}
								</Button>
							</div>

							<div className='details'>
								{getActivityLabel(totalElements)}
							</div>
						</>
					) : (
						<b>
							{history.length
								? sub(
										Liferay.Language.get(
											'individuals-activities-x'
										),
										[
											getDateRangeLabel(
												history,
												'intervalInitDate'
											)
										]
								  )
								: Liferay.Language.get(
										'individuals-activities'
								  )}
						</b>
					)}
				</div>
			</div>
		);
	}
	renderTimeline() {
		const {
			channelId,
			entity: {id},
			groupId
		} = this.props;

		return (
			<SearchableVerticalTimelineHOC
				dataSourceFn={getActivities}
				dataSourceParams={{
					...this.getDateRange(),
					channelId,
					contactsEntityId: id,
					groupId
				}}
				entityLabel={Liferay.Language.get('activities')}
				headerLabels={{
					count: Liferay.Language.get('activity-count'),
					label: Liferay.Language.get('time'),
					title: Liferay.Language.get('session')
				}}
				initialExpanded={false}
				ref={this._searchableVerticalTimelineRef}
			/>
		);
	}

	render() {
		const {
			props: {
				channelId,
				entity: {engagementScore, id},
				groupId,
				tabId
			},
			state: {
				activityChange,
				activityCount,
				engagementChange,
				error,
				loading
			}
		} = this;

		return (
			<Card.Body noPadding>
				<WrapSafeResults
					className='flex-grow-1'
					error={error}
					errorProps={{
						className: 'flex-grow-1',
						onReload: this.handleFetchHistory
					}}
					loading={loading}
					page={false}
					pageDisplay={false}
				>
					<CardTabs
						activeTabId={tabId}
						className='mdn-button-tab'
						tabs={buildTabItems({
							activityChange,
							activityCount,
							channelId,
							engagementChange,
							engagementScore,
							groupId,
							id,
							route: Routes.CONTACTS_INDIVIDUAL
						})}
					/>
					{this.renderChart()}

					{this.renderTimeline()}
				</WrapSafeResults>
			</Card.Body>
		);
	}
}

export default withSelectedPoint(IndividualProfileCard);
