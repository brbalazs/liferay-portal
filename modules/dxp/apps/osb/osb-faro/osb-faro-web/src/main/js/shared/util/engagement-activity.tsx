import CardTabMetric from 'contacts/individual/profile/components/CardTabMetric';
import ChartTooltip from 'shared/components/ChartTooltip';
import moment from 'moment';
import React from 'react';
import ReactDOMServer from 'react-dom/server';

import TooltipChart from 'cerebro-shared/components/TooltipChart';
import {ACTIVITIES, ENGAGEMENT, Routes, toRoute} from 'shared/util/router';
import {
	DataTooltip,
	getAxisFormatter,
	getDateTitle,
	MetricValueType
} from './charts';
import {DEFAULT_ACTIVITY_MAX} from 'shared/api/activities';
import {DEFAULT_ENGAGEMENT_MAX} from 'shared/api/engagement';
import {formatUTCDateFromUnix} from 'shared/util/date';
import {get, isFinite, isNull} from 'lodash';
import {getDate} from 'shared/util/date';
import {Interval, RangeSelectors} from 'shared/types';
import {Map} from 'immutable';
import {sub} from 'shared/util/lang';

export const CHART_ACTIVITY_ID = 'activities';
export const CHART_ENGAGEMENT_ID = 'engagements';
export const CHART_ID = 'individualActivity';

type TooltipOptions = {
	dateKeysIMap: Map<Date, [Date, Date?]>;
	history: Array<ActivitiesEngagementHistory<Date>>;
	interval: Interval;
	name: string;
	rangeSelectors: RangeSelectors;
	title: string;
	type: MetricValueType;
};

interface ActivitiesEngagementHistory<initDate = number> {
	intervalInitDate: initDate;
	scoreAvg?: number;
	totalElements: number;
}

export interface IProfileCardChartProps
	extends React.HTMLAttributes<HTMLElement> {
	forwardedRef: React.Ref<any>;
	history: Array<ActivitiesEngagementHistory>;
	interval: Interval;
	onPointSelect: ({index: number}) => void;
	rangeSelectors: RangeSelectors;
}

/**
 * Object containing aggregated engagement and activity information.
 * @typedef {Object} HistoryItem
 * @property {number} intervalInitDate - The date of this data snapshot.
 * @property {number} totalElements - The total number of activities in the snapshot.
 * @property {number} scoreAvg - The average engagement score for the snapshot.
 */

/**
 * Prepare history data for use in chart.
 * Map data to two different y-axes representing engagement and activity,
 * as well as mapping date as the x-axis.
 * @param {Array.<HistoryItem>} dataPoints - History data points.
 * @return {Array.<Object>} History data mapped for use in chart.
 */
export function buildEngagementActivityAxes(dataPoints = []) {
	return [
		{
			axis: 'y',
			data: dataPoints.map(({totalElements}) => Number(totalElements)),
			id: CHART_ACTIVITY_ID,
			name: Liferay.Language.get('activity-count'),
			type: 'bar'
		},
		{
			axis: 'y2',
			data: dataPoints.map(({scoreAvg}) => scoreAvg),
			id: CHART_ENGAGEMENT_ID,
			name: Liferay.Language.get('engagement-score'),
			type: 'spline'
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

/**
 * Format engagement and actvitiy metrics for use in ChangeLegend
 * @param {Object} changeMetrics - History data points.
 * @param {number} changeMetrics.activityChange - The activity count change from
 *                                                previous period.
 * @param {number} changeMetrics.activityCount - The activity count.
 * @param {number} changeMetrics.engagementChange - The engagement score change from
 *                                                  previous period.
 * @param {number} changeMetrics.engagementScore - The engagement score.
 * @return {Array} Engagement and activity metrics
 *                 formatted for use in ChangeLegend.
 */
export function buildLegendItems({
	activityChange,
	activityCount,
	engagementChange,
	engagementScore
}) {
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
		},
		{
			change: engagementChange,
			id: CHART_ENGAGEMENT_ID,
			secondaryInfo: sub(Liferay.Language.get('x-day-change'), [
				DEFAULT_ENGAGEMENT_MAX
			]),
			title: isNull(engagementScore)
				? sub(Liferay.Language.get('engagement-score-x'), ['--'])
				: sub(Liferay.Language.get('engagement-score-x-10'), [
						engagementScore.toFixed(2)
				  ])
		}
	];
}

export function buildTabItems({
	activityChange,
	activityCount,
	channelId,
	engagementChange,
	engagementScore,
	groupId,
	id
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
			tabUrl: toRoute(Routes.CONTACTS_INDIVIDUAL, {
				channelId,
				groupId,
				id,
				tabId: ACTIVITIES
			}),
			title: Liferay.Language.get('account-activities')
		},
		{
			secondaryInfo: (
				<CardTabMetric
					change={engagementChange}
					type={MetricValueType.Engagement}
					value={engagementScore}
				/>
			),
			tabId: ENGAGEMENT,
			tabUrl: toRoute(Routes.CONTACTS_INDIVIDUAL, {
				channelId,
				groupId,
				id,
				tabId: ENGAGEMENT
			}),
			title: Liferay.Language.get('engagement-score')
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

/**
 * Return the chart tooltip contents rendered to string.
 * @param {Array.<Object>} data - The array of active items.
 * @param {Array.<HistoryItem>} history - History data points.
 * @returns {string} The tooltip as a string.
 */
export function renderTooltipToString(data, history) {
	const {intervalInitDate, scoreAvg, totalElements} = history[
		get(data, [0, 'index'])
	];

	let items = [
		{
			label:
				totalElements === 1
					? Liferay.Language.get('activities')
					: Liferay.Language.get('activity'),
			value: totalElements
		}
	];

	if (isFinite(scoreAvg)) {
		items = items.concat({
			label: Liferay.Language.get('engagement-score'),
			value: scoreAvg.toFixed(2)
		});
	}

	return ReactDOMServer.renderToString(
		<ChartTooltip
			items={items}
			title={formatUTCDateFromUnix(intervalInitDate)}
		/>
	);
}

export const createDateKeysIMap = (
	interval: Interval,
	history: Array<ActivitiesEngagementHistory<Date>>
) => {
	const parserHistory = ({intervalInitDate}) => {
		const dateEnd =
			interval === 'W'
				? moment
						.utc(intervalInitDate)
						.add('6', 'days')
						.toDate()
				: null;

		return [intervalInitDate, [intervalInitDate, dateEnd]];
	};

	return Map<Date, [Date, Date?]>(history.map(parserHistory));
};

export const convertHistoryInitDateToDate = (
	history: Array<ActivitiesEngagementHistory>
): Array<ActivitiesEngagementHistory<Date>> =>
	history.map(({intervalInitDate, ...others}) => ({
		intervalInitDate: getDate(intervalInitDate),
		...others
	}));

export const renderTooltip = (options: TooltipOptions) => (
	dataPoints: [DataTooltip]
): string => {
	const {
		dateKeysIMap,
		history,
		interval,
		name,
		rangeSelectors,
		title,
		type
	} = options;

	const formatter = getAxisFormatter(type);
	const {intervalInitDate, scoreAvg, totalElements} = history[
		get(dataPoints, [0, 'index'])
	];

	const value = type === ENGAGEMENT ? scoreAvg : totalElements;

	const currentPeriodTitle = getDateTitle(
		dateKeysIMap.get(intervalInitDate),
		rangeSelectors.rangeKey,
		interval
	);

	const header = [
		{label: title, weight: 'semibold', width: 100},
		{
			align: 'right',
			label: currentPeriodTitle,
			weight: 'semibold',
			width: 55
		}
	];

	const rows = [
		{
			columns: [
				{
					label: name,
					weight: 'normal'
				},
				{
					align: 'right',
					label: formatter(value),
					weight: 'semibold'
				}
			]
		}
	];

	return ReactDOMServer.renderToString(
		<TooltipChart header={header} rows={rows} />
	);
};

export const getSafeRangeKey = (
	rangeKey: RangeSelectors['rangeKey']
): RangeSelectors['rangeKey'] | null => {
	if (rangeKey === 'CUSTOM') {
		return null;
	}

	return rangeKey;
};
