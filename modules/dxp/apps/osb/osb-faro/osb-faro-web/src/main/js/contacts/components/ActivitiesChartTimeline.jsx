import * as API from 'shared/api';
import autobind from 'autobind-decorator';
import Button from 'shared/components/Button';
import Card from 'shared/components/Card';
import Chart, {BAR_CHART} from 'shared/components/Chart';
import ChartTooltip from 'shared/components/ChartTooltip';
import FaroConstants from 'shared/util/constants';
import getCN from 'classnames';
import moment from 'moment';
import React from 'react';
import ReactDOMServer from 'react-dom/server';
import SearchableVerticalTimeline from 'shared/components/SearchableVerticalTimeline';
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
import {get, omit} from 'lodash';
import {PropTypes} from 'prop-types';
import {START_TIME} from 'shared/util/pagination';
import {sub} from 'shared/util/lang';
import {withSelectedPoint, withStatefulPagination} from 'shared/hoc';

const SearchableVerticalTimelineHOC = withStatefulPagination(
	SearchableVerticalTimeline,
	null,
	props => omit(props, 'onSearchValueChange')
);

const {
	pagination: {orderDescending}
} = FaroConstants;

const CHART_ID = 'activity';
const CHART_ACTIVITY_ID = 'activities';

function formatTickVal(date) {
	return moment.utc(date).format('M/D');
}

function getActivities(params) {
	const {
		channelId,
		contactsEntityId,
		contactsEntityType,
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
			contactsEntityType,
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

export class ActivitiesChartTimeline extends React.Component {
	static propTypes = {
		activitiesLabel: PropTypes.string.isRequired,
		entityType: PropTypes.number.isRequired,
		groupId: PropTypes.string.isRequired,
		history: PropTypes.arrayOf(
			PropTypes.shape({
				intervalInitDate: PropTypes.number,
				totalElements: PropTypes.number
			})
		).isRequired,
		id: PropTypes.string.isRequired,
		onPointSelect: PropTypes.func.isRequired,
		selectedPoint: PropTypes.number
	};

	constructor(props) {
		super(props);

		this._chartRef = React.createRef();
		this._searchableVerticalTimelineRef = React.createRef();
	}

	buildHistoryData(dataPoints = []) {
		return [
			{
				axis: 'y',
				data: dataPoints.map(({totalElements}) =>
					Number(totalElements)
				),
				id: CHART_ACTIVITY_ID,
				name: Liferay.Language.get('activity-count'),
				type: 'bar'
			},
			{
				data: dataPoints.map(({intervalInitDate}) =>
					Number(intervalInitDate)
				),
				id: 'date',
				name: Liferay.Language.get('date')
			}
		];
	}

	getDateRange() {
		const {hasSelectedPoint, history, selectedPoint} = this.props;

		if (!hasSelectedPoint) {
			return {
				endDate: getLastDate(history, 'intervalInitDate'),
				startDate: getFirstDate(history, 'intervalInitDate')
			};
		}

		const {intervalInitDate} = history[selectedPoint];

		return {endDate: intervalInitDate, startDate: intervalInitDate};
	}

	@autobind
	getTooltipContents(data) {
		const {history} = this.props;

		const {intervalInitDate, totalElements} = history[
			get(data, [0, 'index'])
		];

		return ReactDOMServer.renderToString(
			<ChartTooltip
				items={[
					{
						label:
							totalElements === 1
								? Liferay.Language.get('activities')
								: Liferay.Language.get('activity'),
						value: totalElements
					}
				]}
				title={formatUTCDateFromUnix(intervalInitDate)}
			/>
		);
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

	render() {
		const {
			activitiesLabel,
			channelId,
			className,
			entityType,
			groupId,
			hasSelectedPoint,
			history,
			id,
			selectedPoint
		} = this.props;

		const {intervalInitDate, totalElements} = history[selectedPoint] || {};

		return (
			<Card.Body
				className={getCN('activities-chart-timeline-root', className)}
				noPadding
			>
				<Chart
					alwaysShowSelectedTooltip
					axisX={{
						tick: {
							centered: false,
							format: formatTickVal,
							multiline: true,
							outer: false
						}
					}}
					axisY={{
						max: getMaxActivitiesValue(history),
						min: 0,
						padding: {bottom: 0}
					}}
					bar={{width: {ratio: 0.9}}}
					chartType={BAR_CHART}
					className='activities-timeline-chart'
					data={this.buildHistoryData(history)}
					dataId={CHART_ACTIVITY_ID}
					id={CHART_ID}
					onPointSelect={this.handleChartSelect}
					ref={this._chartRef}
					tooltip={{
						contents: this.getTooltipContents
					}}
					x='date'
					yLabel={Liferay.Language.get('activities')}
				/>

				{!!history.length && (
					<div className='selected-info'>
						{hasSelectedPoint ? (
							<>
								<div className='d-flex align-items-baseline'>
									<h4>
										{sub(activitiesLabel, [
											formatUTCDateFromUnix(
												intervalInitDate
											)
										])}
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
							<h4>
								{sub(activitiesLabel, [
									getDateRangeLabel(
										history,
										'intervalInitDate'
									)
								])}
							</h4>
						)}
					</div>
				)}

				{!!history.length && (
					<SearchableVerticalTimelineHOC
						dataSourceFn={getActivities}
						dataSourceParams={{
							...this.getDateRange(),
							channelId,
							contactsEntityId: id,
							contactsEntityType: entityType,
							groupId
						}}
						entityLabel={Liferay.Language.get('activities')}
						groupId={groupId}
						headerLabels={{
							count: Liferay.Language.get('activity-count'),
							label: Liferay.Language.get('time'),
							title: Liferay.Language.get('session')
						}}
						initialExpanded={false}
						ref={this._searchableVerticalTimelineRef}
					/>
				)}
			</Card.Body>
		);
	}
}

export default withSelectedPoint(ActivitiesChartTimeline);
