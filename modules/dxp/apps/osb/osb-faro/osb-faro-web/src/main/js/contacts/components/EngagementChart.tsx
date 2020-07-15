import Chart, {SPLINE_CHART} from 'shared/components/Chart';
import Constants from 'shared/util/constants';
import React from 'react';
import {
	CHART_ENGAGEMENT_ID,
	CHART_ID,
	convertHistoryInitDateToDate,
	createDateKeysIMap,
	IChartProps,
	IEngagementHistory,
	renderTooltip,
	TooltipRowType
} from 'shared/util/engagement-activity';
import {
	DEFAULT_ENGAGEMENT_INTERVAL,
	DEFAULT_ENGAGEMENT_MAX
} from 'shared/api/engagement';
import {
	formatXAxisDate,
	getIntervals,
	MetricValueType
} from 'shared/util/charts';
import {Interval} from 'shared/types';

const {timeIntervals} = Constants;

const INTERVAL_MAP = {
	[timeIntervals.day]: 'D',
	[timeIntervals.month]: 'M',
	[timeIntervals.week]: 'W'
};

const EngagementChart: React.FC<IChartProps<IEngagementHistory<number>>> = ({
	forwardedRef,
	history,
	onAfterInit,
	onPointSelect
}) => {
	const rangeKey = String(DEFAULT_ENGAGEMENT_MAX);
	const interval = INTERVAL_MAP[DEFAULT_ENGAGEMENT_INTERVAL] as Interval;

	const parsedHistory = convertHistoryInitDateToDate<
		IEngagementHistory<number>
	>(history);
	const dateKeysIMap = createDateKeysIMap(interval, parsedHistory);

	const historyDate = [
		{
			data: parsedHistory.map(({scoreAvg}) => scoreAvg),
			id: 'CHART_ID',
			name: Liferay.Language.get('engagement')
		},
		{
			data: parsedHistory.map(({intervalInitDate}) => intervalInitDate),
			id: 'x'
		}
	];

	const intervals = getIntervals(
		rangeKey,
		parsedHistory.map(({intervalInitDate}) => intervalInitDate),
		interval,
		dateKeysIMap
	);

	const getTooltipContents = renderTooltip({
		dateKeysIMap,
		history: parsedHistory,
		interval,
		name: Liferay.Language.get('avg-engagement'),
		rangeSelectors: {
			rangeEnd: null,
			rangeKey,
			rangeStart: null
		},
		title: Liferay.Language.get('engagement'),
		type: MetricValueType.Engagement
	});

	return (
		<Chart
			alwaysShowSelectedTooltip
			axisX={{
				tick: {
					centered: false,
					format: date =>
						formatXAxisDate(date, rangeKey, interval, dateKeysIMap),
					values: intervals
				},
				type: 'timeseries'
			}}
			axisY={{min: 0, padding: {bottom: 0}}}
			chartType={SPLINE_CHART}
			className='engagement-chart-root'
			data={historyDate}
			dataId={CHART_ENGAGEMENT_ID}
			id={CHART_ID}
			onafterinit={onAfterInit}
			onPointSelect={onPointSelect}
			ref={forwardedRef}
			tooltip={{
				contents: getTooltipContents
			}}
			x='x'
			yLabel={Liferay.Language.get('engagement')}
		/>
	);
};

export default EngagementChart;
