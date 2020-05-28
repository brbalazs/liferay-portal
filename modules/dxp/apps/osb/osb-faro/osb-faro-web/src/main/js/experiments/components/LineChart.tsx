import * as d3 from 'd3';
import Chart from 'shared/components/Chart';
import Circle from 'shared/components/Circle';
import React, {useRef} from 'react';
import ReactDOMServer from 'react-dom/server';
import {getAxisMeasuresFromData} from 'shared/util/charts';
import {getShortIntervals} from 'experiments/util/experiments';

const CLASSNAME = 'analytics-experiments-line-chart';

const isEmptyData: IsEmptyData = data =>
	!data.filter(value => value > 0).length;

type ChartData = {
	data: Array<number> | Array<Date>;
	id: string;
	label?: string;
};

type IsEmptyData = (data: Array<number>) => boolean;

type Format = (value: Date) => Function | string;

type tickY = {
	count?: number;
	format: Format;
	values?: Array<Date>;
};

type DataPoint = {
	x: Date;
	value: number;
	id: string;
	index: number;
	name: string;
};

interface Tooltip extends React.HTMLAttributes<HTMLElement> {
	dataPoint: Array<DataPoint>;
}

interface ILineChartProps extends React.HTMLAttributes<HTMLElement> {
	Tooltip?: React.FC<Tooltip>;
	data: Array<ChartData>;
	format: Function;
	height?: number;
	intervals: Array<Date>;
	tooltipConfig?: Object;
	type?: string;
}

/**
 * Returns a {point} object formatted for the lineChart,
 * passing the date as a parameter to check if the array size
 * is 1 because if so, there is only one day to plot on the chart.
 * So we can make the point, improving UX.
 * @param data
 * @returns point
 */
const getPoint = (data: Array<Object>) => {
	let point: any = {
		select: {
			r: 20
		},
		show: true
	};

	if (data.length === 1) {
		point = {
			...point,
			r: 3
		};
	}

	return point;
};

const LineChart: React.FC<ILineChartProps> = ({
	data,
	format,
	height = 320,
	intervals,
	Tooltip,
	tooltipConfig = {},
	type = 'line'
}) => {
	const chartRef = useRef(null);

	let maxValue = 1;

	let tickY: tickY = {
		format: value => format(value)
	};

	let chartData = [];

	data.forEach(({data, id}) => {
		if (id !== 'x') {
			chartData = [...chartData, ...data];
		}
	});

	// Shorten intervals
	if (intervals.length >= 12) {
		intervals = getShortIntervals(intervals);
	}

	if (!isEmptyData(chartData)) {
		let intervalsY = [];

		({intervals: intervalsY, maxValue} = getAxisMeasuresFromData(
			chartData
		));

		tickY = {
			...tickY,
			values: intervalsY
		};
	} else {
		tickY = {
			...tickY,
			count: 5,
			format: () => format(0)
		};
	}

	return (
		<div className={CLASSNAME}>
			<Chart
				axisX={{
					tick: {
						format: (date: Date) => d3.utcFormat('%b %-d')(date),
						values: intervals
					},
					type: 'timeseries'
				}}
				axisY={{
					max: maxValue,
					min: 0,
					padding: {
						bottom: 0,
						top: 0
					},
					tick: tickY
				}}
				chartType={type}
				data={data}
				dataId='lineChartDataId'
				height={height}
				id='lineChartId'
				legend={{
					contents: {
						bindto: '#legendId',
						template: (id: string, color: string) => {
							const selected = data.find(d => d.id === id);

							if (!selected)
								return ReactDOMServer.renderToString(<div />);

							return ReactDOMServer.renderToString(
								<li className='chart-legend-item'>
									<Circle color={color} />{' '}
									{` ${selected.label}`}
								</li>
							);
						}
					},
					item: {
						onclick: () => false
					},
					show: true
				}}
				point={getPoint(data[0].data)}
				ref={chartRef}
				tooltip={
					Tooltip && {
						contents: (dataPoint: Array<DataPoint>) =>
							ReactDOMServer.renderToString(
								<Tooltip dataPoint={dataPoint} />
							),
						...tooltipConfig
					}
				}
				x='x'
			/>

			<ul className='chart-legend' id='legendId' />
		</div>
	);
};

export default LineChart;
