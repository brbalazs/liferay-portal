import BarComparisonCell from './BarComparisonCell';
import EventAnalysisQuery, {
	EventAnalysisData,
	EventAnalysisVariables
} from 'event-analysis/queries/EventAnalysisQuery';
import getCN from 'classnames';
import PercentOfCell from './PercentOfCell';
import React, {useState} from 'react';
import Table from 'shared/components/table';
import useStatefulPagination from 'shared/hooks/useStatefulPagination';
import WithEmptyState from './hoc/WithEmptyState';
import {
	Attributes,
	BreakdownData,
	Breakdowns,
	CalculationTypes,
	Event,
	Filters
} from 'event-analysis/utils/types';
import {compose} from 'redux';
import {EditBreakdown, withAttributesConsumer} from '../context/attributes';
import {get, isNil, omit} from 'lodash';
import {
	getMaxEventValue,
	parserBreakdownData
} from 'event-analysis/utils/utils';
import {getSafeRangeSelectors} from 'shared/util/util';
import {OrderByDirections} from 'shared/util/constants';
import {SafeResults} from 'shared/hoc/util';
import {sub} from 'shared/util/lang';
import {useQuery} from '@apollo/react-hooks';
import {withPaginationBar} from 'shared/hoc';
import {WithRangeKeyProps} from 'shared/hoc/WithRangeKey';

export interface IBreakdownTableProps
	extends WithRangeKeyProps,
		React.HTMLAttributes<HTMLElement> {
	attributes: Attributes;
	breakdownOrder: string[];
	breakdowns: Breakdowns;
	channelId: string;
	compareToPrevious: boolean;
	editBreakdown: EditBreakdown;
	event: Event;
	filterOrder: string[];
	filters: Filters;
	type: CalculationTypes;
}

const getBreakdownByAccessor = (accessor, breakdownOrder, breakdowns) => {
	const orderIndex = Number(accessor.split('breakdown').pop()) - 1;
	const breakdownId = breakdownOrder[orderIndex];

	return breakdowns[breakdownId];
};

const TableWithPagination = withPaginationBar()(Table);

const BreakdownTable: React.FC<IBreakdownTableProps> = ({
	attributes,
	breakdownOrder,
	breakdowns,
	channelId,
	compareToPrevious,
	editBreakdown,
	event,
	filterOrder,
	filters,
	rangeSelectors,
	type
}) => {
	const {delta, page, setDelta, setPage} = useStatefulPagination();

	const result = useQuery<EventAnalysisData, EventAnalysisVariables>(
		EventAnalysisQuery,
		{
			fetchPolicy: 'network-only',
			variables: {
				analysisType: type,
				channelId,
				compareToPrevious,
				eventAnalysisBreakdowns: breakdownOrder.map(breakdownId =>
					omit(breakdowns[breakdownId], 'id')
				),
				eventAnalysisFilters: filterOrder.map(filterId =>
					omit(filters[filterId], 'id')
				),
				eventDefinitionId: event.id,
				page: page - 1,
				size: delta,
				...getSafeRangeSelectors(rangeSelectors)
			}
		}
	);

	const [orderFields, setOrderFields] = useState({});

	const parseData = (data: BreakdownData) => {
		const items = parserBreakdownData(data);

		const highestValue = getMaxEventValue(items, compareToPrevious);

		const columns = getColumns({
			attributes,
			breakdowns,
			compareToPrevious,
			event,
			highestValue,
			order: breakdownOrder,
			orderFields,
			value: data.value
		});

		return {
			columns,
			count: data.count,
			highestValue,
			items
		};
	};

	const handleSort = ({orderParams: {field, sortOrder}}) => {
		const breakdown = getBreakdownByAccessor(
			field,
			breakdownOrder,
			breakdowns
		);

		const attribute = attributes[breakdown.attributeId];

		editBreakdown({
			attribute,
			breakdown: {
				...breakdown,
				sortType:
					sortOrder === 'asc'
						? OrderByDirections.Ascending
						: OrderByDirections.Descending
			},
			id: breakdown.id
		});

		setOrderFields(orderFields => ({
			...orderFields,
			[field]: sortOrder
		}));
	};

	return (
		<SafeResults {...result} page={false} pageDisplay={false}>
			{({eventAnalysis}: {eventAnalysis: EventAnalysisData}) => {
				const {columns, count, highestValue, items} = parseData(
					eventAnalysis
				);

				return (
					<div
						className={getCN('breakdown-table-root', {
							'breakdown-single-event': !breakdownOrder.length
						})}
					>
						{!breakdownOrder.length ? (
							<BarComparisonCell
								compareToPrevious={compareToPrevious}
								event={event}
								events={items[0].events}
								topValue={highestValue}
							/>
						) : (
							<TableWithPagination
								bordered
								columns={columns}
								delta={delta}
								internalSort
								items={items}
								onSortChange={handleSort}
								page={page}
								paginationProps={{
									onDeltaChange: setDelta,
									onPageChange: setPage
								}}
								rowIdentifier='index'
								total={count}
							/>
						)}
					</div>
				);
			}}
		</SafeResults>
	);
};

const getColumns = ({
	attributes,
	breakdowns,
	compareToPrevious,
	event,
	highestValue,
	order,
	orderFields,
	value
}) => {
	const columns = order.map((breakdownId, i) => {
		const {attributeId, type} = breakdowns[breakdownId];
		const accessor = `breakdown${i + 1}`;

		return {
			accessor,
			cellRenderer: ({className, data}) => {
				const dataEvents = get(data, 'events');
				const dataValue = get(data, accessor);
				const nextDataValue = get(data, `breakdown${i + 2}`);

				if (
					isNil(dataValue) &&
					isNil(dataEvents) &&
					isNil(nextDataValue)
				) {
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
				} else if (isNil(dataValue)) {
					return null;
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
			headProps: {
				order: orderFields[accessor]
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
		label: Liferay.Language.get('events'),
		sortable: false
	});

	columns.push({
		cellRenderer: ({className, data: {events}}) => (
			<td className={getCN('align-top', className)}>
				<PercentOfCell
					compareToPrevious={compareToPrevious}
					events={events}
					totalValue={value}
				/>
			</td>
		),
		label: sub(Liferay.Language.get('percent-of-x'), [
			event.displayName || event.name
		]),
		sortable: false
	});

	return columns;
};

export default compose(withAttributesConsumer, WithEmptyState)(BreakdownTable);
