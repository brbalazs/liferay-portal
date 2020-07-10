import Chart, {SPLINE_CHART} from 'shared/components/Chart';
import ChartTooltip from 'shared/components/ChartTooltip';
import React from 'react';
import ReactDOMServer from 'react-dom/server';
import {
	CHART_ENGAGEMENT_ID,
	CHART_ID,
	formatTickVal
} from 'shared/util/engagement-activity';
import {formatUTCDateFromUnix} from 'shared/util/date';
import {get} from 'lodash';

interface IEngagementChartProps extends React.HTMLAttributes<HTMLElement> {
	forwardedRef: React.Ref<any>;
	history: any;
	onPointSelect: ({index: number}) => void;
}

const EngagementChart: React.FC<IEngagementChartProps> = ({
	forwardedRef,
	history,
	onPointSelect
}) => {
	const buildHistoryData = (dataPoints = []) => [
		{
			data: dataPoints.map(item => item.intervalInitDate),
			id: 'date'
		},
		{
			data: dataPoints.map(item => item.scoreAvg),
			id: CHART_ID,
			name: Liferay.Language.get('engagement')
		}
	];

	const getTooltipContents = data => {
		const {intervalInitDate, scoreAvg} = history[get(data, [0, 'index'])];

		return ReactDOMServer.renderToString(
			<ChartTooltip
				items={[
					{
						label: Liferay.Language.get('engagement-score'),
						value: scoreAvg.toFixed(2)
					}
				]}
				title={formatUTCDateFromUnix(intervalInitDate)}
			/>
		);
	};

	return (
		<Chart
			alwaysShowSelectedTooltip
			axisX={{
				categories: history.map(item =>
					item.intervalInitDate.toString()
				),
				tick: {
					centered: false,
					format: formatTickVal,
					multiline: true,
					outer: false
				},
				type: 'timeseries'
			}}
			axisY={{min: 0, padding: {bottom: 0}}}
			chartType={SPLINE_CHART}
			className='engagement-chart-root'
			data={buildHistoryData(history)}
			dataId={CHART_ENGAGEMENT_ID}
			id={CHART_ID}
			onPointSelect={onPointSelect}
			ref={forwardedRef}
			tooltip={{
				contents: getTooltipContents
			}}
			x='date'
			yLabel={Liferay.Language.get('engagement')}
		/>
	);
};

export default EngagementChart;
