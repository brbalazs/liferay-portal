import CardTabMetric from 'contacts/individual/profile/components/CardTabMetric';
import Constants from 'shared/util/constants';
import React from 'react';
import {ACTIVITIES, toRoute} from 'shared/util/router';
import {DEFAULT_ACTIVITY_MAX} from 'shared/api/activities';
import {formatUTCDateFromUnix} from 'shared/util/date';
import {Interval, RangeSelectors} from 'shared/types';
import {Map} from 'immutable';
import {MetricValueType} from './charts';
import {sub} from 'shared/util/lang';

export const CHART_ACTIVITY_ID = 'activities';
export const CHART_ID = 'individualActivity';

const {timeIntervals} = Constants;

export type TooltipRowType = {
	label: string;
	value: any;
};

export type TooltipOptionsType = {
	dateKeysIMap: Map<number, [number, number?]>;
	history: Array<IActivitiesHistory>;
	interval: Interval;
	name: string;
	rangeSelectors: RangeSelectors;
	tooltipRenderRows?: (data: {[key: string]: any}) => Array<TooltipRowType>;
	title: string;
	type: MetricValueType;
};

export interface IActivitiesHistory<initDateType = number> {
	intervalInitDate: initDateType;
	totalElements: number;
}

export interface IChartProps<T> extends React.HTMLAttributes<HTMLElement> {
	alwaysShowSelectedTooltip: boolean;
	hasSelectedPoint?: boolean;
	height: number;
	history: Array<T>;
	interval?: Interval;
	onAfterInit?: () => void;
	onPointSelect: ({index: number}) => void;
	rangeSelectors?: RangeSelectors;
	selectedPoint: number;
	tooltipRenderRows?: (data: T) => Array<TooltipRowType>;
}

/**
 * Format actvitiy metrics for use in ChangeLegend
 * @param {Object} changeMetrics - History data points.
 * @param {number} changeMetrics.activityChange - The activity count change from
 *                                                previous period.
 * @param {number} changeMetrics.activityCount - The activity count.
 * @return {Array} Engagement and activity metrics
 *                 formatted for use in ChangeLegend.
 */
export function buildLegendItems({activityChange, activityCount}) {
	return [
		{
			change: activityChange,
			id: CHART_ACTIVITY_ID,
			secondaryInfo: sub(Liferay.Language.get('x-day-change'), [
				DEFAULT_ACTIVITY_MAX
			]),
			title: sub(Liferay.Language.get('total-activity-count-x'), [
				activityCount.toLocaleString()
			])
		}
	];
}

export function buildTabItems({
	activityChange,
	activityCount,
	channelId,
	groupId,
	id,
	route
}) {
	return [
		{
			secondaryInfo: (
				<CardTabMetric
					change={activityChange}
					type={MetricValueType.Number}
					value={activityCount}
				/>
			),
			tabId: ACTIVITIES,
			tabUrl: toRoute(route, {
				channelId,
				groupId,
				id,
				tabId: ACTIVITIES
			}),
			title: Liferay.Language.get('activities')
		}
	];
}

/**
 * Format the x-axis chart tick value.
 * @param {number|string} date
 * @return {string} formatted date
 */
export function formatTickVal(date) {
	return formatUTCDateFromUnix(date, 'M/D');
}

export const getSafeRangeKey = (
	rangeKey: RangeSelectors['rangeKey']
): RangeSelectors['rangeKey'] | null => {
	if (rangeKey === 'CUSTOM') {
		return null;
	}

	return rangeKey;
};

export const INTERVAL_MAP = {
	D: timeIntervals.day,
	M: timeIntervals.month,
	W: timeIntervals.week
};
