import React, {useState} from 'react';
import Spinner from 'shared/components/Spinner';
import TooltipChart from 'cerebro-shared/components/TooltipChart';
import {ANIMATION_DURATION, AXIS, getAxisTickText} from 'shared/util/recharts';
import {
	Bar,
	CartesianGrid,
	Cell,
	ComposedChart,
	Legend,
	ResponsiveContainer,
	Tooltip,
	XAxis,
	YAxis
} from 'recharts';
import {CHART_COLOR_NAMES} from 'shared/components/Chart';
import {
	formatXAxisDate,
	getBarColor,
	getDateTitle,
	getIntervals
} from 'shared/util/charts';
import {get} from 'lodash';
import {Interval} from 'shared/types';
import {Map} from 'immutable';
import {RangeSelectors} from 'shared/types';
import {toThousands} from 'shared/util/numbers';

const {mormont: CHART_ORANGE, stark: CHART_BLUE} = CHART_COLOR_NAMES;

interface IActiveIndividualsChartProps {
	data: {
		anonymousVisitors: number;
		intervalInitDate: number;
		knownVisitors: number;
		visitors: number;
	}[];
	dateKeysIMap: Map<number, [number, number?]>;
	height: number;
	interval: Interval;
	loading: Boolean;
	rangeSelectors: RangeSelectors;
}

const ActiveIndividualsChart: React.FC<IActiveIndividualsChartProps> = ({
	data = [],
	dateKeysIMap,
	height = 360,
	interval,
	loading,
	rangeSelectors
}) => {
	const [hoverIndex, setHoverIndex] = useState(-1);
	const [legendHoverItem, setLegendHoverItem] = useState(null);

	const renderTooltip = ({active, payload}) => {
		if (active) {
			const {
				anonymousVisitors,
				intervalInitDate,
				knownVisitors,
				visitors
			} = get(payload, [0, 'payload'], {});

			return (
				<div
					className='bb-tooltip-container'
					style={{position: 'static'}}
				>
					<TooltipChart
						header={[
							{
								label: Liferay.Language.get(
									'active-individuals'
								),
								weight: 'semibold',
								width: 150
							},
							{
								align: 'right',
								label: getDateTitle(
									dateKeysIMap.get(intervalInitDate),
									rangeSelectors.rangeKey,
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
										label: Liferay.Language.get(
											'anonymous'
										),
										weight: 'semibold'
									},
									{
										align: 'right',
										label: toThousands(anonymousVisitors),
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
										label: toThousands(knownVisitors),
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
										label: toThousands(visitors),
										weight: 'semibold'
									}
								]
							}
						]}
					/>
				</div>
			);
		}
	};

	return loading ? (
		<Spinner alignCenter key='LOADING' />
	) : (
		<ResponsiveContainer height={height}>
			<ComposedChart data={data}>
				<CartesianGrid
					stroke={AXIS.gridStroke}
					strokeDasharray='3 3'
					vertical={false}
				/>

				<XAxis
					axisLine={{stroke: AXIS.borderStroke}}
					dataKey='intervalInitDate'
					domain={['dataMin', 'dataMax']}
					padding={{left: 20, right: 20}}
					tick={getAxisTickText('x', value =>
						formatXAxisDate(
							value,
							rangeSelectors.rangeKey,
							interval,
							dateKeysIMap
						)
					)}
					tickLine={false}
					tickMargin={12}
					ticks={getIntervals(
						rangeSelectors.rangeKey,
						data.map(({intervalInitDate}) => intervalInitDate),
						interval,
						dateKeysIMap
					)}
				/>

				<XAxis
					axisLine={{stroke: AXIS.borderStroke}}
					dataKey='intervalInitDate'
					orientation='top'
					stroke={AXIS.gridStroke}
					tick={false}
					tickLine={false}
					xAxisId='top'
				/>

				<YAxis
					allowDecimals={false}
					axisLine={{stroke: AXIS.borderStroke}}
					label={{
						fill: AXIS.textColor,
						offset: 20,
						position: 'top',
						value: Liferay.Language.get('individuals')
					}}
					name={Liferay.Language.get('individuals')}
					stroke={AXIS.gridStroke}
					tick={getAxisTickText('y')}
					tickCount={6}
					tickLine={false}
					type='number'
				/>

				<YAxis
					axisLine={{stroke: AXIS.borderStroke}}
					orientation='right'
					stroke={AXIS.gridStroke}
					tick={false}
					tickLine={false}
					type='number'
					width={1}
					yAxisId='right'
				/>

				<Legend
					align='right'
					iconSize={8}
					onMouseEnter={({dataKey}) => setLegendHoverItem(dataKey)}
					onMouseLeave={() => setLegendHoverItem(null)}
					verticalAlign='bottom'
					wrapperStyle={{
						bottom: 0,
						color: AXIS.textColor,
						fontSize: '14px',
						lineHeight: '21px',
						right: 0
					}}
				/>

				<Tooltip
					content={renderTooltip}
					cursor={{stroke: CHART_BLUE}}
				/>

				<Bar
					animationDuration={ANIMATION_DURATION.bar}
					dataKey='knownVisitors'
					fill={CHART_BLUE}
					fillOpacity={
						legendHoverItem === 'anonymousVisitors' ? 0.2 : 1
					}
					legendType='circle'
					name={Liferay.Language.get('known-visitors')}
					onMouseEnter={(e, index) => setHoverIndex(index)}
					onMouseLeave={() => setHoverIndex(-1)}
					stackId='count'
				>
					{data.map((entry, index) => (
						<Cell
							fill={getBarColor(index, hoverIndex, null, 'blue')}
							key={`cell-${index}`}
						/>
					))}
				</Bar>

				<Bar
					animationDuration={ANIMATION_DURATION.bar}
					dataKey='anonymousVisitors'
					fill={CHART_ORANGE}
					fillOpacity={legendHoverItem === 'knownVisitors' ? 0.2 : 1}
					legendType='circle'
					name={Liferay.Language.get('anonymous-visitors')}
					onMouseEnter={(e, index) => setHoverIndex(index)}
					onMouseLeave={() => setHoverIndex(-1)}
					stackId='count'
				>
					{data.map((entry, index) => (
						<Cell
							fill={getBarColor(
								index,
								hoverIndex,
								null,
								'orange'
							)}
							key={`cell-${index}`}
						/>
					))}
				</Bar>
			</ComposedChart>
		</ResponsiveContainer>
	);
};

export default ActiveIndividualsChart;
