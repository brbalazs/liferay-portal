import Chart, {BAR_CHART} from 'shared/components/Chart';
import React from 'react';
import {
	CHART_ACTIVITY_ID,
	CHART_ID,
	convertHistoryInitDateToDate,
	createDateKeysIMap,
	IProfileCardChartProps,
	renderTooltip
} from 'shared/util/engagement-activity';
import {
	formatXAxisDate,
	getIntervals,
	MetricValueType
} from 'shared/util/charts';
import {getMaxActivitiesValue} from 'shared/util/activities';

const ActivitiesChart: React.FC<IProfileCardChartProps> = ({
	forwardedRef,
	history,
	interval,
	onPointSelect,
	rangeSelectors
}) => {
	const parsedHistory = convertHistoryInitDateToDate(history);
	const dateKeysIMap = createDateKeysIMap(interval, parsedHistory);

	const historyData = [
		{
			axis: 'y',
			data: parsedHistory.map(({totalElements}) => Number(totalElements)),
			id: CHART_ACTIVITY_ID,
			name: Liferay.Language.get('activity-count'),
			type: 'bar'
		},
		{
			data: parsedHistory.map(({intervalInitDate}) => intervalInitDate),
			id: 'x'
		}
	];

	const getTooltipContents = renderTooltip({
		dateKeysIMap,
		history: parsedHistory,
		interval,
		name: Liferay.Language.get('activities'),
		rangeSelectors,
		title: Liferay.Language.get('activities'),
		type: MetricValueType.Number
	});

	const intervals = getIntervals(
		rangeSelectors.rangeKey,
		parsedHistory.map(({intervalInitDate}) => intervalInitDate),
		interval,
		dateKeysIMap
	);

	return (
		<Chart
			alwaysShowSelectedTooltip
			axisX={{
				tick: {
					centered: false,
					format: date =>
						formatXAxisDate(
							date,
							rangeSelectors.rangeKey,
							interval,
							dateKeysIMap
						),
					values: intervals
				},
				type: 'timeseries'
			}}
			axisY={{
				max: getMaxActivitiesValue(parsedHistory),
				min: 0,
				padding: {bottom: 0}
			}}
			bar={{width: {ratio: 0.9}}}
			chartType={BAR_CHART}
			className='activities-timeline-chart'
			data={historyData}
			dataId={CHART_ACTIVITY_ID}
			id={CHART_ID}
			onPointSelect={onPointSelect}
			ref={forwardedRef}
			tooltip={{
				contents: getTooltipContents
			}}
			x='x'
			yLabel={Liferay.Language.get('activities')}
		/>
	);
};

export default ActivitiesChart;
