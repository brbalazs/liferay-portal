import * as API from 'shared/api';
import ActivitiesChart from './ActivitiesChart';
import autobind from 'autobind-decorator';
import Button from 'shared/components/Button';
import Card from 'shared/components/Card';
import FaroConstants from 'shared/util/constants';
import getCN from 'classnames';
import React from 'react';
import SearchableVerticalTimeline from 'shared/components/SearchableVerticalTimeline';
import {formatSessions, getActivityLabel} from 'shared/util/activities';
import {
	getDateRangeLabel,
	getDateRangeLabelFromDate,
	getEndDate,
	getFirstDate,
	getLastDate
} from 'shared/util/date';
import {omit} from 'lodash';
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

	getDateRange() {
		const {hasSelectedPoint, history, interval, selectedPoint} = this.props;

		if (!hasSelectedPoint) {
			return {
				endDate: getLastDate(history, interval, 'intervalInitDate'),
				startDate: getFirstDate(history, 'intervalInitDate')
			};
		}

		const {intervalInitDate} = history[selectedPoint];

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
			interval,
			rangeSelectors,
			selectedPoint
		} = this.props;

		const {intervalInitDate, totalElements} = history[selectedPoint] || {};

		return (
			<Card.Body
				className={getCN('activities-chart-timeline-root', className)}
				noPadding
			>
				<ActivitiesChart
					forwardedRef={this._chartRef}
					history={history}
					interval={interval}
					onPointSelect={this.handleChartSelect}
					rangeSelectors={rangeSelectors}
				/>

				{!!history.length && (
					<div className='selected-info'>
						{hasSelectedPoint ? (
							<>
								<div className='d-flex align-items-baseline'>
									<h4>
										{sub(activitiesLabel, [
											getDateRangeLabelFromDate(
												intervalInitDate,
												interval
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
										interval,
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
