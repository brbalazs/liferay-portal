import BarComparisonTable, {
	BarComparisonTableItems
} from './BarComparisonTable';
import React from 'react';
import {BreakdownDataItem, Event} from 'event-analysis/utils/types';
import {get} from 'lodash';

enum BAR_COMPARISON_COLORS {
	Blue = 'blue',
	Green = 'green'
}

const MAP_COLORS: Record<
	BAR_COMPARISON_COLORS,
	Array<{
		current: string;
		previous: string;
	}>
> = {
	blue: [
		{
			current: '#187FFF',
			previous: '#97C5FF'
		},
		{
			current: '#4B9BFF',
			previous: '#97C5FF'
		}
	],
	green: [
		{
			current: '#31BE88',
			previous: '#8DE2C1'
		},
		{
			current: '#3CCD95',
			previous: '#8DE2C1'
		}
	]
};

interface IBarComparisonCellProps extends React.HTMLAttributes<HTMLElement> {
	compareToPrevious: boolean;
	event: Event;
	events: BreakdownDataItem[];
	topValue: number;
}

const BarComparisonCell: React.FC<IBarComparisonCellProps> = ({
	compareToPrevious = false,
	event,
	events = [],
	topValue
}) => {
	const isComparingEvent = events.length > 1;
	const isComparingSegment = get(events[0], 'breakdownItems', []).length > 1;

	const items = isComparingSegment
		? getItems(events[0].breakdownItems, compareToPrevious, topValue)
		: getItems(events, compareToPrevious, topValue);

	return (
		<div className='table-responsive table-root bar-comparison-root'>
			<BarComparisonTable
				event={event}
				isComparingSegment={isComparingSegment}
				items={items}
			/>

			{isComparingSegment && isComparingEvent && (
				<BarComparisonTable
					event={event}
					isComparingSegment={isComparingSegment}
					items={getItems(
						events[1].breakdownItems,
						compareToPrevious,
						topValue,
						BAR_COMPARISON_COLORS.Green
					)}
				/>
			)}
		</div>
	);
};

const getItems = (
	events: BreakdownDataItem[],
	compareToPrevious: boolean,
	topValue: number,
	color: BAR_COMPARISON_COLORS = BAR_COMPARISON_COLORS.Blue
): BarComparisonTableItems => {
	const data = [];

	events.forEach(({name, previousValue, value}, i) => {
		data.push({
			isPreviousValue: false,
			name,
			percent: value / topValue,
			style: {
				'background-color': MAP_COLORS[color][i].current
			},
			value
		});

		if (compareToPrevious) {
			data.push({
				isPreviousValue: true,
				name: Liferay.Language.get('previous-value'),
				percent: previousValue / topValue,
				style: {
					'background-color': MAP_COLORS[color][i].previous
				},
				value: previousValue
			});
		}
	});

	return data;
};

export default BarComparisonCell;
