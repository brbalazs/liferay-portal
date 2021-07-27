import BarComparisonCell from './BarComparisonCell';
import getCN from 'classnames';
import PercentOfCell from './PercentOfCell';
import React, {useEffect, useMemo} from 'react';
import Table from 'shared/components/table';
import WithEmptyState from './hoc/WithEmptyState';
import {
	Attributes,
	Breakdowns,
	Event,
	Filters
} from 'event-analysis/utils/types';
import {compose} from 'redux';
import {get, isNil} from 'lodash';
import {getDummyBreakdownData} from 'test/data';
import {
	getMaxEventValue,
	parserBreakdownData
} from 'event-analysis/utils/utils';
import {sub} from 'shared/util/lang';
import {withAttributesConsumer} from '../context/attributes';
import {withPaginationBar} from 'shared/hoc';
import {WithRangeKeyProps} from 'shared/hoc/WithRangeKey';

export interface IBreakdownTableProps
	extends WithRangeKeyProps,
		React.HTMLAttributes<HTMLElement> {
	attributes: Attributes;
	breakdownOrder: string[];
	breakdowns: Breakdowns;
	compareToPrevious: boolean;
	event: Event;
	filterOrder: string[];
	filters: Filters;
}

const TableWithPagination = withPaginationBar()(Table);

const BreakdownTable: React.FC<IBreakdownTableProps> = ({
	attributes,
	breakdownOrder,
	breakdowns,
	compareToPrevious,
	event,
	filterOrder,
	filters,
	rangeSelectors
}) => {
	useEffect(() => {
		// TODO: LRAC-7333 Add request and remove Dummy data
	}, [
		attributes,
		breakdownOrder,
		breakdowns,
		compareToPrevious,
		event,
		filterOrder,
		filters,
		rangeSelectors
	]);

	const [count, items, totalEvents] = useMemo(() => {
		// TODO: LRAC-7333 Add request and remove Dummy data
		const data = getDummyBreakdownData(
			event,
			attributes,
			breakdownOrder.map(
				breakdownId => breakdowns[breakdownId].attributeId
			)
		);

		if (!Object.keys(attributes).length) {
			return [data.count, data.breakdownItems, data.totalEvents];
		}

		const items = parserBreakdownData(data);

		return [data.count, items, data.totalEvents];
	}, [attributes, breakdownOrder, filterOrder]);

	const [highestValue, columns] = useMemo(() => {
		if (!Object.keys(attributes).length) {
			const highestValue = getMaxEventValue(
				[{events: items}],
				compareToPrevious
			);

			return [highestValue, []];
		}

		const highestValue = getMaxEventValue(items, compareToPrevious);

		const columns = getColumns({
			attributes,
			breakdowns,
			compareToPrevious,
			event,
			highestValue,
			order: breakdownOrder,
			totalEvents
		});

		return [highestValue, columns];
	}, [compareToPrevious, attributes, breakdownOrder]);

	if (!Object.keys(attributes).length) {
		return (
			<div className='breakdown-table-root breakdown-single-event'>
				<BarComparisonCell
					compareToPrevious={compareToPrevious}
					event={event}
					events={items}
					topValue={highestValue}
				/>
			</div>
		);
	}

	return (
		<div className='breakdown-table-root'>
			<TableWithPagination
				bordered
				columns={columns}
				internalSort
				items={items}
				rowIdentifier='index'
				total={count}
			/>
		</div>
	);
};

const getColumns = ({
	attributes,
	breakdowns,
	compareToPrevious,
	event,
	highestValue,
	order,
	totalEvents
}) => {
	const columns = order.map((breakdownId, i) => {
		const {attributeId, type} = breakdowns[breakdownId];

		return {
			cellRenderer: ({className, data}) => {
				const dataValue = get(data, `breakdown${i + 1}`);

				if (isNil(dataValue)) {
					return (
						<td
							className={getCN(
								'align-top',
								'empty-breakdown-column',
								className
							)}
						>
							{Liferay.Language.get('no-results')}
						</td>
					);
				}

				return (
					<td
						className={getCN(
							'font-weight-semibold',
							'align-top',
							className
						)}
						rowSpan={dataValue.rowSpan}
					>
						{dataValue.name}
					</td>
				);
			},
			label: (
				<div>
					<span className='breakdown-category'>{type}</span>
					{attributes[attributeId].displayName}
				</div>
			)
		};
	});

	columns.push({
		cellRenderer: ({className, data: {events}}) => {
			if (isNil(events)) {
				return (
					<td
						className={getCN(
							'align-top',
							'empty-breakdown-column',
							className
						)}
					>
						{Liferay.Language.get('no-results')}
					</td>
				);
			}

			return (
				<td className={getCN('align-top', className)}>
					<BarComparisonCell
						compareToPrevious={compareToPrevious}
						event={event}
						events={events}
						topValue={highestValue}
					/>
				</td>
			);
		},
		label: Liferay.Language.get('events')
	});

	columns.push({
		cellRenderer: ({className, data: {events}}) => (
			<td className={getCN('align-top', className)}>
				<PercentOfCell
					compareToPrevious={compareToPrevious}
					events={events}
					totalEvents={totalEvents}
				/>
			</td>
		),
		label: sub(Liferay.Language.get('percent-of-x'), [
			event.displayName || event.name
		])
	});

	return columns;
};

export default compose(withAttributesConsumer, WithEmptyState)(BreakdownTable);
