import * as API from 'shared/api';
import ActivitiesChart from '../../../components/ActivitiesChart';
import autobind from 'autobind-decorator';
import Button from 'shared/components/Button';
import Card from 'shared/components/Card';
import Constants from 'shared/util/constants';
import React from 'react';
import SearchableVerticalTimeline from 'shared/components/SearchableVerticalTimeline';
import {ACTIVITIES} from 'shared/util/router';
import {autoCancel, hasRequest} from 'shared/util/request-decorator';
import {
	formatSessions,
	getActivityLabel,
	getSafeRangeKey,
	INTERVAL_MAP
} from 'shared/util/activities';
import {
	getDateRangeLabel,
	getDateRangeLabelFromDate,
	getEndDate,
	getFirstDate,
	getLastDate
} from 'shared/util/date';
import {getSafeChange} from 'shared/util/change';
import {hasChanges} from 'shared/util/react';
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
		selectedPoint: PropTypes.oneOfType([
			PropTypes.number,
			PropTypes.object
		]),
		tabId: PropTypes.string,
		timeZoneId: PropTypes.string.isRequired
	};

	state = {
		activityChange: 0,
		activityCount: 0,
		activityHistory: [],
		error: false,
		loading: true
	};

	constructor(props) {
		super(props);

		this._searchableVerticalTimelineRef = React.createRef();
	}

	componentDidMount() {
		this.handleFetchHistory();
	}

	componentDidUpdate(prevProps) {
		const {hasSelectedPoint, selectedPoint} = this.props;

		if (
			hasChanges(
				prevProps,
				this.props,
				'interval',
				'rangeSelectors',
				'tabId'
			)
		) {
			if (hasSelectedPoint || selectedPoint) {
				this.handleClearSelection();
			}

			this.handleFetchHistory();
		}
	}

	getDateRange() {
		const {
			props: {hasSelectedPoint, interval, selectedPoint},
			state: {activityHistory}
		} = this;

		if (!hasSelectedPoint) {
			return {
				endDate: getLastDate(
					activityHistory,
					interval,
					'intervalInitDate'
				),
				startDate: getFirstDate(activityHistory, 'intervalInitDate')
			};
		}

		const {intervalInitDate} = activityHistory[selectedPoint] || {};

		return {
			endDate: getEndDate(intervalInitDate, interval),
			startDate: intervalInitDate
		};
	}

	@autobind
	handleChartSelect({index}) {
		this._searchableVerticalTimelineRef.current.resetPage();

		this.props.onPointSelect({index});
	}

	@autobind
	handleClearSelection() {
		const {_searchableVerticalTimelineRef} = this;

		_searchableVerticalTimelineRef.current.resetPage();

		this.props.onPointSelect({index: null});
	}

	@autoCancel
	@autobind
	handleFetchHistory() {
		const {
			channelId,
			entity: {id},
			groupId,
			interval,
			rangeSelectors
		} = this.props;

		this.setState({
			error: false,
			loading: true
		});

		return API.activities
			.fetchHistory({
				channelId,
				contactsEntityId: id,
				contactsEntityType: individual,
				groupId,
				interval: INTERVAL_MAP[interval],
				max: getSafeRangeKey(rangeSelectors.rangeKey),
				...rangeSelectors
			})
			.then(
				({
					activityAggregations: activityHistory,
					change: activityChange,
					count: activityCount
				}) => {
					this.setState({
						activityChange: getSafeChange(activityChange),
						activityCount,
						activityHistory,
						loading: false
					});
				}
			)
			.catch(err => {
				if (!err.IS_CANCELLATION_ERROR) {
					this.setState({
						error: true,
						loading: false
					});
				}
			});
	}

	renderChartInfo() {
		const {
			props: {hasSelectedPoint, interval, selectedPoint},
			state: {activityCount, activityHistory}
		} = this;

		const {intervalInitDate, totalElements = 0} =
			activityHistory[selectedPoint] || {};

		const selected = hasSelectedPoint || selectedPoint;

		const date = selected
			? getDateRangeLabelFromDate(intervalInitDate, interval)
			: getDateRangeLabel(activityHistory, interval, 'intervalInitDate');

		return (
			<div className='selected-info'>
				<div className='activities-date d-flex align-items-baseline'>
					<h4>
						{activityHistory.length
							? sub(
									Liferay.Language.get(
										'individuals-activities-x'
									),
									[date]
							  )
							: Liferay.Language.get('individuals-activities')}
					</h4>

					{selected && (
						<Button
							display='link'
							onClick={this.handleClearSelection}
							size='sm'
						>
							{Liferay.Language.get('clear-date-selection')}
						</Button>
					)}
				</div>

				<div className='details'>
					{getActivityLabel(
						(selected
							? totalElements
							: activityCount
						).toLocaleString()
					)}
				</div>
			</div>
		);
	}

	renderTimeline() {
		const {
			channelId,
			entity: {id},
			groupId,
			timeZoneId
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
				timeZoneId={timeZoneId}
			/>
		);
	}

	render() {
		const {
			props: {hasSelectedPoint, interval, rangeSelectors, selectedPoint},
			state: {activityHistory, error, loading}
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
					<div className='individuals-activities-chart'>
						<ActivitiesChart
							alwaysShowSelectedTooltip
							hasSelectedPoint={hasSelectedPoint}
							history={activityHistory}
							interval={interval}
							onPointSelect={this.handleChartSelect}
							rangeSelectors={rangeSelectors}
							selectedPoint={selectedPoint}
						/>

						{this.renderChartInfo()}
					</div>

					{this.renderTimeline()}
				</WrapSafeResults>
			</Card.Body>
		);
	}
}

export default withSelectedPoint(IndividualProfileCard);
