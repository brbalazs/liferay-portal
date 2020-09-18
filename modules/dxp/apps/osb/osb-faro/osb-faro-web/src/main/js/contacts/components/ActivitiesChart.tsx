import React, {useRef, useState} from 'react';
import {
	ANIMATION_DURATION,
	AXIS,
	getAxisTickText,
	getChartTooltip,
	getYAxisLabel,
	getYAxisWidth
} from 'shared/util/recharts';
import {
	Bar,
	CartesianGrid,
	Cell,
	ComposedChart,
	ReferenceLine,
	ResponsiveContainer,
	Tooltip,
	XAxis,
	YAxis
} from 'recharts';
import {CHART_COLOR_NAMES} from 'shared/components/Chart';
import {createDateKeysIMap} from 'shared/util/intervals';
import {
	formatXAxisDate,
	getBarColor,
	getDateTitle,
	getIntervals
} from 'shared/util/charts';
import {get} from 'lodash';
import {IActivitiesHistory, IChartProps} from 'shared/util/engagement-activity';

const {stark: CHART_BLUE} = CHART_COLOR_NAMES;

const ActivitiesChart: React.FC<IChartProps<IActivitiesHistory<number>>> = ({
	alwaysShowSelectedTooltip = false,
	hasSelectedPoint,
	height = 340,
	history,
	interval,
	onPointSelect,
	rangeSelectors,
	selectedPoint
}) => {
	const _tooltipRef = useRef<any>();

	const [hoverIndex, setHoverIndex] = useState(-1);
	const [mouseOutside, setMouseOutside] = useState(false);
	const [selectedTooltipX, setSelectedTooltipX] = useState(null);

	const dateKeysIMap = createDateKeysIMap(
		interval,
		history,
		'intervalInitDate'
	);

	const renderTooltip = ({active, payload}) => {
		if ((active && !!payload.length) || hasSelectedPoint) {
			const {intervalInitDate, totalElements} = get(
				payload,
				[0, 'payload'],
				history[selectedPoint]
			);

			return getChartTooltip({
				dateTitle: getDateTitle(
					dateKeysIMap.get(intervalInitDate),
					rangeSelectors.rangeKey,
					interval
				),
				rows: [
					{
						label: Liferay.Language.get('activities'),
						value: totalElements.toLocaleString()
					}
				],
				title: Liferay.Language.get('activities')
			});
		}
	};

	const intervals = getIntervals(
		rangeSelectors.rangeKey,
		history.map(({intervalInitDate}) => intervalInitDate),
		interval,
		dateKeysIMap
	);

	const showFixedTooltip = hasSelectedPoint && mouseOutside;

	const yAxisWidth = getYAxisWidth(history, 'totalElements');

	return (
		<ResponsiveContainer height={height}>
			<ComposedChart
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
							rangeSelectors.rangeKey,
							interval,
							dateKeysIMap
						)
					)}
					tickLine={false}
					tickMargin={12}
					ticks={intervals}
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
					label={getYAxisLabel(
						Liferay.Language.get('activities'),
						'left',
						yAxisWidth
					)}
					name={Liferay.Language.get('activities')}
					stroke={AXIS.gridStroke}
					tick={getAxisTickText('y')}
					tickCount={6}
					tickLine={false}
					type='number'
					width={yAxisWidth}
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

				<Bar
					animationDuration={ANIMATION_DURATION.bar}
					dataKey='totalElements'
					fill={CHART_BLUE}
					onMouseEnter={(e, index) => setHoverIndex(index)}
					onMouseLeave={() => setHoverIndex(-1)}
				>
					{history.map((entry, index) => (
						<Cell
							fill={getBarColor(index, hoverIndex, selectedPoint)}
							key={`cell-${index}`}
						/>
					))}
				</Bar>
			</ComposedChart>
		</ResponsiveContainer>
	);
};

export default ActivitiesChart;
