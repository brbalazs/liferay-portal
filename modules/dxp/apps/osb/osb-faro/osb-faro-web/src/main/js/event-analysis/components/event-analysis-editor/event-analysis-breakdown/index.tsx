import BarComparisonCell from './BarComparisonCell';
import EventAnalysisQuery, {
	EventAnalysisData,
	EventAnalysisVariables
} from 'event-analysis/queries/EventAnalysisQuery';
import getCN from 'classnames';
import PercentOfCell from './PercentOfCell';
import React from 'react';
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
import {get, isNil, omit} from 'lodash';
import {
	getMaxEventValue,
	parserBreakdownData
} from 'event-analysis/utils/utils';
import {getSafeRangeSelectors} from 'shared/util/util';
import {SafeResults} from 'shared/hoc/util';
import {sub} from 'shared/util/lang';
import {useQuery} from '@apollo/react-hooks';
import {withAttributesConsumer} from '../context/attributes';
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
	event: Event;
	filterOrder: string[];
	filters: Filters;
	type: CalculationTypes;
}

const TableWithPagination = withPaginationBar()(Table);

const BreakdownTable: React.FC<IBreakdownTableProps> = ({
	attributes,
	breakdownOrder,
	breakdowns,
	channelId,
	compareToPrevious,
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
			value: data.value
		});

		return {
			columns,
			count: data.count,
			highestValue,
			items
		};
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
	value
}) => {
	const columns = order.map((breakdownId, i) => {
		const {attributeId, type} = breakdowns[breakdownId];

		return {
			cellRenderer: ({className, data}) => {
				const dataEvents = get(data, 'events');
				const dataValue = get(data, `breakdown${i + 1}`);
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
