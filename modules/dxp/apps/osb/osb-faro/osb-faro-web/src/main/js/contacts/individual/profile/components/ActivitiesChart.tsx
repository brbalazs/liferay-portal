import Chart, {BAR_CHART} from 'shared/components/Chart';
import ChartTooltip from 'shared/components/ChartTooltip';
import React from 'react';
import ReactDOMServer from 'react-dom/server';
import {
	CHART_ACTIVITY_ID,
	CHART_ID,
	formatTickVal
} from 'shared/util/engagement-activity';
import {formatUTCDateFromUnix} from 'shared/util/date';
import {get} from 'lodash';
import {getMaxActivitiesValue} from 'shared/util/activities';

interface IActivitiesChartProps extends React.HTMLAttributes<HTMLElement> {
	forwardedRef: React.Ref<any>;
	history: any;
	onPointSelect: ({index: number}) => void;
}

const ActivitiesChart: React.FC<IActivitiesChartProps> = ({
	forwardedRef,
	history,
	onPointSelect
}) => {
	const buildHistoryData = (dataPoints = []) => [
		{
			axis: 'y',
			data: dataPoints.map(({totalElements}) => Number(totalElements)),
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

	const getTooltipContents = data => {
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
	};

	return (
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
			data={buildHistoryData(history)}
			dataId={CHART_ACTIVITY_ID}
			id={CHART_ID}
			onPointSelect={onPointSelect}
			ref={forwardedRef}
			tooltip={{
				contents: getTooltipContents
			}}
			x='date'
			yLabel={Liferay.Language.get('activities')}
		/>
	);
};

export default ActivitiesChart;
