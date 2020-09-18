import React from 'react';
import TooltipChart from 'cerebro-shared/components/TooltipChart';
import {Text} from 'recharts';

const AXIS_LABEL_OFFSET = 20;
const TEXT_PADDING = 4;
const Y_AXIS_WIDTH = 30;

export const ANIMATION_DURATION = {
	bar: 800,
	line: 1000
};

export const AXIS = {
	borderStroke: '#E7E7ED',
	font:
		'14px "Source Sans Pro", "Source Sans, -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol"',
	gridStroke: '#E7E7ED',
	textColor: '#6B6C7E'
};

export const BAR_COLORS = {
	blue: {
		default: '#4B9BFF',
		hover: '#318DFF',
		notSelected: '#97C5FF',
		selected: '#0071FD'
	},
	orange: {
		default: '#FFB46E',
		hover: '#FFA754',
		notSelected: '#FFCEA1',
		selected: '#FF8C21'
	}
};

export const getTextWidth = (text, font = '14px Source Sans Pro') => {
	const canvas =
		getTextWidth.canvas ||
		(getTextWidth.canvas = document.createElement('canvas'));
	const context = canvas.getContext('2d');
	context.font = font;
	const metrics = context.measureText(text);

	return Math.ceil(metrics.width) + TEXT_PADDING;
};

export const getAxisTickText = (axis = 'x', formatter = val => val) => ({
	payload: {offset, value},
	textAnchor,
	x,
	y
}) => (
	<Text
		style={{
			fill: AXIS.textColor,
			font: AXIS.font,
			fontSize: '0.75rem'
		}}
		textAnchor={textAnchor}
		x={x}
		y={axis === 'y' ? y + offset : y}
	>
		{formatter(value)}
	</Text>
);

export const getChartTooltip = ({dateTitle, rows, title}) => (
	<div className='bb-tooltip-container' style={{position: 'static'}}>
		<TooltipChart
			header={[
				{
					label: title,
					weight: 'semibold',
					width: 150
				},
				{
					align: 'right',
					label: dateTitle,
					weight: 'semibold',
					width: 55
				}
			]}
			rows={rows.map(({label, value}) => ({
				columns: [
					{
						label,
						weight: 'normal'
					},
					{
						align: 'right',
						label: value,
						weight: 'semibold'
					}
				]
			}))}
		/>
	</div>
);

export const getYAxisLabel = (
	label,
	position = 'left',
	yAxisWidth = Y_AXIS_WIDTH
) => ({viewBox: {height, width, x, y}}) => {
	const verticalSign = height >= 0 ? 1 : -1;

	const verticalEnd = verticalSign > 0 ? 'end' : 'start';

	return (
		<Text
			fill={AXIS.textColor}
			textAnchor={position === 'right' ? 'end' : 'start'}
			verticalAnchor={verticalEnd}
			x={position === 'right' ? x + yAxisWidth : x + width - yAxisWidth}
			y={y - verticalSign * AXIS_LABEL_OFFSET}
		>
			{label}
		</Text>
	);
};

export const getYAxisWidth = (data, dataKey, minWidth = Y_AXIS_WIDTH) =>
	data.reduce((acc, tick) => {
		const tickLabel = tick[dataKey];

		const textWidth = getTextWidth(tickLabel);

		return textWidth > acc ? textWidth : acc;
	}, minWidth);
