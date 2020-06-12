import Chart, {COMBINED_CHART} from 'shared/components/Chart';
import Circle from 'shared/components/Circle';
import React from 'react';
import ReactDOMServer from 'react-dom/server';
import Spinner from 'shared/components/Spinner';
import TooltipChart from 'cerebro-shared/components/TooltipChart';
import {find, get} from 'lodash';
import {
	formatXAxisDate,
	getAxisMeasuresFromCompositeData,
	getDateTitle,
	getIntervals,
	isEmptyData
} from 'shared/util/charts';
import {Interval} from 'shared/types';
import {Map} from 'immutable';
import {RangeSelectors} from 'shared/types';
import {toThousands} from 'shared/util/numbers';

const CHART_HEIGHT = 320;
const CHART_ID = 'activeIndividuals';
export const CHART_DATA_ID_1 = 'knownIndividuals';
export const CHART_DATA_ID_2 = 'anonymousIndividuals';

export const LANG_MAP = {
	[CHART_DATA_ID_1]: Liferay.Language.get('known-visitors'),
	[CHART_DATA_ID_2]: Liferay.Language.get('anonymous-visitors')
};

export const renderTooltip = (
	data,
	rangeKey,
	interval,
	dateKeysIMap: Map<Date, [Date, Date?]>
) => {
	const known = find(data, ({id}) => id === CHART_DATA_ID_1);
	const anonymous = find(data, ({id}) => id === CHART_DATA_ID_2);

	return ReactDOMServer.renderToString(
		<TooltipChart
			header={[
				{
					label: Liferay.Language.get('active-individuals'),
					weight: 'semibold',
					width: 150
				},
				{
					align: 'right',
					label: getDateTitle(
						dateKeysIMap.get(known.x),
						rangeKey,
						interval
					),
					weight: 'semibold',
					width: 55
				}
			]}
			rows={[
				{
					columns: [
						{
							label: Liferay.Language.get('anonymous'),
							weight: 'semibold'
						},
						{
							align: 'right',
							label: toThousands(anonymous.value),
							weight: 'semibold'
						}
					]
				},
				{
					columns: [
						{
							label: Liferay.Language.get('known'),
							weight: 'semibold'
						},
						{
							align: 'right',
							label: toThousands(known.value),
							weight: 'semibold'
						}
					]
				},
				{
					columns: [
						{
							label: Liferay.Language.get('total'),
							weight: 'semibold'
						},
						{
							align: 'right',
							label: toThousands(known.value + anonymous.value),
							weight: 'semibold'
						}
					]
				}
			]}
		/>
	);
};

type xAxisData = {
	data: Date[];
	id: string;
};

type yAxisData = {
	data: number[];
	id: string;
	name: string;
	type: string;
};

interface IActiveIndividualsChartProps {
	data: (xAxisData | yAxisData)[];
	dateKeysIMap: Map<Date, [Date, Date?]>;
	interval: Interval;
	loading: Boolean;
	rangeSelectors: RangeSelectors;
}

const ActiveIndividualsChart: React.FC<IActiveIndividualsChartProps> = ({
	data,
	dateKeysIMap,
	interval,
	loading,
	rangeSelectors
}) => {
	const knownData = get(
		find(data, ({id}) => id === CHART_DATA_ID_1),
		'data',
		[]
	);
	const anonymousData = get(
		find(data, ({id}) => id === CHART_DATA_ID_2),
		'data',
		[]
	);

	const {
		intervalCount,
		intervals,
		maxValue
	} = getAxisMeasuresFromCompositeData([knownData, anonymousData]);

	return loading ? (
		<Spinner alignCenter key='LOADING' />
	) : (
		<>
			<Chart
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
						values: getIntervals(
							rangeSelectors.rangeKey,
							get(
								find(data, ({id}) => id === 'x') as xAxisData,
								'data',
								[]
							),
							interval
						)
					},
					type: 'timeseries'
				}}
				axisY={{
					max: maxValue,
					padding: {
						bottom: 0,
						top: 0
					},
					tick: isEmptyData([knownData, anonymousData])
						? {
								count: 5,
								format: val => (val === 0 ? val : '')
						  }
						: {
								count: intervalCount,
								format: toThousands,
								values: intervals
						  }
				}}
				chartType={COMBINED_CHART}
				data={data}
				dataId={`${CHART_ID}Data`}
				generateChartOnLoad
				height={CHART_HEIGHT}
				id={CHART_ID}
				legend={{
					contents: {
						bindto: '#legend-active-individuals',
						template: (id, color) =>
							ReactDOMServer.renderToString(
								<li className='chart-legend-item'>
									<Circle color={color} />

									{LANG_MAP[id]}
								</li>
							)
					},
					item: {
						onclick: () => false
					},
					show: true
				}}
				otherData={{groups: [[CHART_DATA_ID_1, CHART_DATA_ID_2]]}}
				tooltip={{
					contents: d =>
						renderTooltip(
							d,
							rangeSelectors.rangeKey,
							interval,
							dateKeysIMap
						)
				}}
				x='x'
				yLabel={Liferay.Language.get('individuals')}
			/>

			<ul className='chart-legend' id='legend-active-individuals' />
		</>
	);
};

export default ActiveIndividualsChart;
