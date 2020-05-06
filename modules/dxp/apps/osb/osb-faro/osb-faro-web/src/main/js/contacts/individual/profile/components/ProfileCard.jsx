import * as API from 'shared/api';
import autobind from 'autobind-decorator';
import Button from 'shared/components/Button';
import Card from 'shared/components/Card';
import ChangeLegend from 'contacts/components/ChangeLegend';
import Chart, {COMBINED_CHART} from 'shared/components/Chart';
import ErrorDisplay from 'shared/components/ErrorDisplay';
import FaroConstants from 'shared/util/constants';
import getCN from 'classnames';
import Promise from 'metal-promise';
import React from 'react';
import SearchableVerticalTimeline from 'shared/components/SearchableVerticalTimeline';
import Spinner from 'shared/components/Spinner';
import {autoCancel, hasRequest} from 'shared/util/request-decorator';
import {
	buildEngagementActivityAxes,
	buildLegendItems,
	CHART_ACTIVITY_ID,
	CHART_ID,
	formatTickVal,
	renderTooltipToString
} from 'shared/util/engagement-activity';
import {
	formatEngagementAggregation,
	formatEngagementScore,
	mergeHistoryByDate
} from 'shared/util/engagement';
import {
	formatSessions,
	getActivityLabel,
	getMaxActivitiesValue
} from 'shared/util/activities';
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

const {
	entityTypes: {individual},
	pagination: {orderDescending}
} = FaroConstants;

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
	static propTypes = {
		entity: PropTypes.instanceOf(Individual).isRequired,
		groupId: PropTypes.string.isRequired,
		hasSelectedPoint: PropTypes.bool,
		onPointSelect: PropTypes.func.isRequired,
		selectedPoint: PropTypes.number
	};

	state = {
		activityChange: 0,
		engagementChange: 0,
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

	@autoCancel
	@autobind
	getActivityHistory() {
		const {
			channelId,
			entity: {id},
			groupId
		} = this.props;

		return API.activities.fetchHistory({
			channelId,
			contactsEntityId: id,
			contactsEntityType: individual,
			groupId
		});
	}

	getDateRange() {
		const {
			props: {hasSelectedPoint, selectedPoint},
			state: {history}
		} = this;

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
	getTooltipContents(data) {
		const {history} = this.state;

		return renderTooltipToString(data, history);
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
					change: activityChange
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
					engagementChange: getSafeChange(engagementChange),
					history: mergeHistoryByDate(
						engagementHistory,
						activityHistory
					),
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
			props: {
				entity: {activitiesCount, engagementScore},
				hasSelectedPoint,
				selectedPoint
			},
			state: {activityChange, engagementChange, error, history, loading}
		} = this;

		const {intervalInitDate, totalElements} = history[selectedPoint] || {};

		if (loading) {
			return <Spinner className='flex-grow-1' key='LOADING' spacer />;
		} else if (error) {
			return (
				<ErrorDisplay
					className='flex-grow-1'
					key='ERROR_DISPLAY'
					onReload={this.handleFetchHistory}
					spacer
				/>
			);
		} else {
			return (
				<div className='individuals-activities-chart'>
					<ChangeLegend
						items={buildLegendItems({
							activityChange,
							activityCount: activitiesCount,
							engagementChange,
							engagementScore: formatEngagementScore(
								engagementScore
							)
						})}
					/>

					<Chart
						alwaysShowSelectedTooltip
						axisX={{tick: {format: formatTickVal}}}
						axisY={{
							max: getMaxActivitiesValue(history),
							min: 0,
							padding: {bottom: 0}
						}}
						axisY2={{
							min: 0,
							padding: {bottom: 0},
							show: true
						}}
						bar={{width: {ratio: 0.9}}}
						chartType={COMBINED_CHART}
						data={buildEngagementActivityAxes(history)}
						dataId={CHART_ACTIVITY_ID}
						id={CHART_ID}
						onPointSelect={this.handleChartSelect}
						ref={this._chartRef}
						splineInterpolationType='monotone-x'
						tooltip={{
							contents: this.getTooltipContents
						}}
						x='date'
						y2Label={Liferay.Language.get('engagement')}
						yLabel={Liferay.Language.get('activities')}
					/>

					<div className='selected-info'>
						{hasSelectedPoint ? (
							<>
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
	}

	renderTimeline() {
		const {
			props: {
				channelId,
				entity: {id},
				groupId
			},
			state: {history}
		} = this;

		return (
			<SearchableVerticalTimelineHOC
				dataSourceFn={
					history.length
						? getActivities
						: () => Promise.resolve({items: [], total: 0})
				}
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
		const {className} = this.props;

		return (
			<Card
				className={getCN('individual-profile-card-root', className)}
				pageDisplay
			>
				<Card.Header>
					<Card.Title>
						{Liferay.Language.get('individual-activities')}
					</Card.Title>
				</Card.Header>

				<Card.Body noPadding>
					{this.renderChart()}

					{this.renderTimeline()}
				</Card.Body>
			</Card>
		);
	}
}

export default withSelectedPoint(IndividualProfileCard);
