import React, {useRef, useState} from 'react';
import TooltipChart from 'cerebro-shared/components/TooltipChart';
import {AXIS} from 'shared/util/clay-recharts';
import {
	CartesianGrid,
	Line,
	LineChart,
	ReferenceDot,
	ReferenceLine,
	ResponsiveContainer,
	Text,
	Tooltip,
	XAxis,
	YAxis
} from 'recharts';
import {CHART_COLOR_NAMES} from 'shared/components/Chart';
import {formatUTCDateFromUnix} from 'shared/util/date';
import {get} from 'lodash';
import {IChartProps, IEngagementHistory} from 'shared/util/engagement-activity';

const {stark: CHART_BLUE} = CHART_COLOR_NAMES;

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

	const renderTooltip = ({active, payload}) => {
		if (active || hasSelectedPoint) {
			const {contributors, intervalInitDate, scoreAvg} = get(
				payload,
				[0, 'payload'],
				history[selectedPoint]
			);

			return (
				<div
					className='bb-tooltip-container'
					style={{position: 'static'}}
				>
					<TooltipChart
						header={[
							{
								label: Liferay.Language.get('engagement'),
								weight: 'semibold',
								width: 100
							},
							{
								align: 'right',
								label: formatUTCDateFromUnix(
									intervalInitDate,
									'YYYY MMM DD'
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
											'avg-engagement'
										),
										weight: 'normal'
									},
									{
										align: 'right',
										label: scoreAvg.toFixed(2),
										weight: 'semibold'
									}
								]
							},
							...(tooltipRenderRows &&
								tooltipRenderRows(contributors))
						].filter(Boolean)}
					/>
				</div>
			);
		}
	};

	const showFixedTooltip = hasSelectedPoint && mouseOutside;

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
					interval={6}
					padding={{left: 20, right: 20}}
					scale='time'
					tick={({payload, textAnchor, x, y}) => (
						<Text
							style={{
								fill: AXIS.textColor,
								font: AXIS.font,
								fontSize: '0.75rem'
							}}
							textAnchor={textAnchor}
							x={x}
							y={y}
						>
							{formatUTCDateFromUnix(payload.value, 'MMM DD')}
						</Text>
					)}
					tickLine={false}
					tickMargin={12}
					type='number'
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
					tick={({payload, textAnchor, x, y}) => (
						<Text
							style={{
								fill: AXIS.textColor,
								font: AXIS.font,
								fontSize: '0.75rem'
							}}
							textAnchor={textAnchor}
							x={x}
							y={y + payload.offset}
						>
							{payload.value}
						</Text>
					)}
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
