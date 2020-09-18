import React, {useRef, useState} from 'react';
import {
	ANIMATION_DURATION,
	AXIS,
	getAxisTickText,
	getChartTooltip
} from 'shared/util/recharts';
import {
	CartesianGrid,
	Line,
	LineChart,
	ReferenceDot,
	ReferenceLine,
	ResponsiveContainer,
	Tooltip,
	XAxis,
	YAxis
} from 'recharts';
import {CHART_COLOR_NAMES} from 'shared/components/Chart';
import {createDateKeysIMap} from 'shared/util/intervals';
import {formatXAxisDate, getDateTitle, getIntervals} from 'shared/util/charts';
import {get, isNumber} from 'lodash';
import {IChartProps, IEngagementHistory} from 'shared/util/engagement-activity';
import {LAST_30_DAYS} from 'shared/util/constants';

const {stark: CHART_BLUE} = CHART_COLOR_NAMES;

const INTERVAL = 'D';

const EngagementChart: React.FC<IChartProps<IEngagementHistory<number>>> = ({
	alwaysShowSelectedTooltip = false,
	hasSelectedPoint,
	height = 340,
	history,
	onPointSelect,
	selectedPoint,
	tooltipRenderRows
}) => {
	const _tooltipRef = useRef<any>();

	const [mouseOutside, setMouseOutside] = useState(false);
	const [selectedTooltipX, setSelectedTooltipX] = useState(null);

	const dateKeysIMap = createDateKeysIMap(
		INTERVAL,
		history,
		'intervalInitDate'
	);

	const renderTooltip = ({active, payload}) => {
		if ((active && !!payload.length) || hasSelectedPoint) {
			const {contributors, intervalInitDate, scoreAvg} = get(
				payload,
				[0, 'payload'],
				history[selectedPoint]
			);

			return getChartTooltip({
				dateTitle: getDateTitle(
					dateKeysIMap.get(intervalInitDate),
					LAST_30_DAYS,
					INTERVAL
				),
				rows: [
					{
						label: Liferay.Language.get('avg-engagement'),
						value: isNumber(scoreAvg) ? scoreAvg.toFixed(2) : null
					},
					...(tooltipRenderRows && tooltipRenderRows(contributors))
				].filter(Boolean),
				title: Liferay.Language.get('engagement')
			});
		}
	};

	const showFixedTooltip = hasSelectedPoint && mouseOutside;

	const intervals = getIntervals(
		LAST_30_DAYS,
		history.map(({intervalInitDate}) => intervalInitDate),
		INTERVAL,
		dateKeysIMap
	);

	return (
		<ResponsiveContainer height={height}>
			<LineChart
				data={history}
				onClick={pointData => {
					if (alwaysShowSelectedTooltip && pointData) {
						if (_tooltipRef) {
							const {
								getTranslate,
								props: {viewBox},
								state: {boxWidth}
							} = _tooltipRef.current;

							setSelectedTooltipX(
								getTranslate({
									key: 'x',
									tooltipDimension: boxWidth,
									viewBoxDimension: viewBox.width
								})
							);
						}

						onPointSelect({index: pointData.activeTooltipIndex});
					}
				}}
				onMouseLeave={() => setMouseOutside(true)}
				onMouseMove={() => setMouseOutside(false)}
			>
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
							LAST_30_DAYS,
							INTERVAL,
							dateKeysIMap
						)
					)}
					tickLine={false}
					tickMargin={12}
					ticks={intervals}
				/>

				<XAxis
					axisLine={{stroke: AXIS.borderStroke}}
					dataKey='modifiedDate'
					orientation='top'
					stroke={AXIS.gridStroke}
					tick={false}
					tickLine={false}
					xAxisId='top'
				/>

				<YAxis
					allowDecimals={false}
					axisLine={{stroke: AXIS.borderStroke}}
					dataKey='scoreAvg'
					label={{
						fill: AXIS.textColor,
						offset: 20,
						position: 'top',
						value: Liferay.Language.get('engagement')
					}}
					name={Liferay.Language.get('engagement')}
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

				<Tooltip
					content={renderTooltip}
					cursor={{stroke: CHART_BLUE}}
					position={
						showFixedTooltip
							? {
									x: selectedTooltipX
							  }
							: null
					}
					ref={_tooltipRef}
					wrapperStyle={
						showFixedTooltip
							? {
									visibility: 'visible'
							  }
							: null
					}
				/>

				<ReferenceLine
					strokeWidth={1}
					x={
						showFixedTooltip
							? history[selectedPoint].intervalInitDate
							: null
					}
				/>

				<ReferenceDot
					fill={CHART_BLUE}
					isFront
					r={4}
					stroke='none'
					x={
						hasSelectedPoint
							? history[selectedPoint].intervalInitDate
							: null
					}
					y={
						hasSelectedPoint
							? history[selectedPoint].scoreAvg
							: null
					}
				/>

				<Line
					activeDot={{r: 4, stroke: CHART_BLUE}}
					animationDuration={ANIMATION_DURATION.line}
					connectNulls
					dataKey='scoreAvg'
					dot={false}
					stroke={CHART_BLUE}
					strokeWidth='2'
					type='monotone'
				/>
			</LineChart>
		</ResponsiveContainer>
	);
};

export default EngagementChart;
