import FaroConstants from 'shared/util/constants';
import getCN from 'classnames';
import HeaderRow from './HeaderRow';
import React, {useEffect, useState} from 'react';
import Row, {Column} from './Row';
import Spinner from 'shared/components/Spinner';
import {buildOrderByFields} from 'shared/util/pagination';
import {get, isArray, noop, orderBy} from 'lodash';
import {getDefaultSortOrder, invertOrder} from 'shared/util/pagination';
import {OrderedMap} from 'immutable';
import {OrderParams} from 'shared/util/records';

const {orderDefault} = FaroConstants.pagination;

export const getRowIdentifierValue = (item, rowIdentifier) => {
	if (isArray(rowIdentifier)) {
		return rowIdentifier.reduce((acc, rowIdentifierKey) => {
			acc = acc.concat(get(item, rowIdentifierKey, rowIdentifierKey));

			return acc;
		}, '');
	}

	return get(item, rowIdentifier);
};

interface ITableProps {
	bordered?: boolean;
	checkDisabled?: (item: object) => void;
	className?: string;
	columns: Column[];
	defaultSort: {
		field: string;
		sortOrder: string;
	}; // TODO: Convert this over to orderIOMap;
	entityType?: string; // TODO: table should not handle this.
	headingNowrap?: boolean;
	internalSort?: boolean;
	items: object[];
	list?: boolean;
	loading?: boolean;
	nowrap?: boolean;
	onRowClick?: (item: object) => void; // TODO: Maybe do something about this.
	onRowDelete?: (item: object) => void;
	onRowSave?: (item: object) => void;
	onSelectItemsChange: (item: object) => void;
	onSortChange: (params: {
		orderByFields: string;
		orderParams: OrderParams;
	}) => void;
	renderInlineRowActions?: (params: {
		data: object;
		editing: boolean;
		edits: object;
		items: object[];
		itemsSelected: object[];
		rowEvents: {
			onRowCancel: () => void;
			onRowEdit: () => void;
			onRowSave: () => void;
		};
	}) => React.ReactNode;
	renderRowActions?: (params: {
		data: object;
		items: object[];
	}) => React.ReactNode;
	rowBordered?: boolean;
	rowIdentifier: string | string[];
	selectedItemsIOMap: OrderedMap<string, object>;
	showCheckbox?: boolean;
}

const Table: React.FC<ITableProps> = ({
	bordered,
	checkDisabled = noop,
	className,
	columns,
	defaultSort,
	entityType,
	headingNowrap = true,
	internalSort = false,
	items = [],
	list = false,
	loading = false,
	nowrap = true,
	onRowClick,
	onRowDelete = noop,
	onRowSave = noop,
	onSelectItemsChange,
	onSortChange,
	renderInlineRowActions,
	renderRowActions,
	rowBordered = true,
	rowIdentifier = 'id',
	selectedItemsIOMap = new OrderedMap(),
	showCheckbox = false
}) => {
	const [orderParams, setOrderParams] = useState(
		new OrderParams({
			sortOrder: orderDefault,
			...defaultSort
		})
	);

	useEffect(() => {
		if (defaultSort) {
			setOrderParams(
				new OrderParams({
					sortOrder: orderDefault,
					...defaultSort
				})
			);
		}
	}, ['columns', 'defaultSort']);

	useEffect(() => {
		handleEmitOnSortChange();
	}, [orderParams]);

	const handleEmitOnSortChange = () => {
		if (onSortChange) {
			onSortChange({
				orderByFields: buildOrderByFields(orderParams, entityType),
				orderParams
			});
		}
	};

	const handleItemClick = item => {
		if (showCheckbox && onSelectItemsChange) {
			onSelectItemsChange(item);
		}

		if (onRowClick) {
			onRowClick(item);
		}
	};

	const handleSort = field => {
		const updatedOrderParams =
			orderParams.field === field
				? orderParams.update('sortOrder', order => invertOrder(order))
				: new OrderParams({
						field,
						sortOrder: getDefaultSortOrder(field)
				  });

		setOrderParams(updatedOrderParams);
	};

	const sortItems = items =>
		orderBy(
			items,
			item => {
				const fieldValue = item[orderParams.field];

				if (typeof fieldValue === 'string') {
					return fieldValue.toLowerCase();
				}

				return fieldValue;
			},
			orderParams.sortOrder
		);

	const classes = getCN('table', 'table-autofit', 'table-hover', {
		'show-quick-actions-on-hover': renderRowActions,
		'table-bordered': bordered,
		'table-heading-nowrap': headingNowrap,
		'table-list': list,
		'table-nowrap': nowrap,
		'table-row-no-bordered': !rowBordered
	});

	const itemsSorted = internalSort ? sortItems(items) : items;

	return (
		<div
			className={getCN(
				'table-responsive table-root flex-grow-1',
				className
			)}
		>
			<table className={classes}>
				<HeaderRow
					columns={columns}
					headerLink={!internalSort && !onSortChange}
					onSort={handleSort}
					orderParams={orderParams}
					showCheckbox={showCheckbox}
					showInlineRowActions={
						!!renderInlineRowActions || !!renderRowActions
					}
				/>

				{!!itemsSorted.length && (
					<tbody className={className}>
						{itemsSorted.map((item, rowIndex) => {
							const disabled = checkDisabled(item);

							return (
								<Row
									className={className}
									clickable={
										onRowClick ||
										(showCheckbox && onSelectItemsChange)
									}
									columns={columns}
									data={item}
									disabled={disabled}
									items={items}
									itemsSelected={
										!selectedItemsIOMap.isEmpty()
									}
									key={getRowIdentifierValue(
										item,
										rowIdentifier
									)}
									onClick={disabled ? noop : handleItemClick}
									onRowDelete={onRowDelete}
									onRowSave={onRowSave}
									renderInlineRowActions={
										renderInlineRowActions
									}
									renderRowActions={renderRowActions}
									rowIndex={rowIndex}
									selected={
										onSelectItemsChange
											? selectedItemsIOMap.has(item.id)
											: null
									}
									showCheckbox={showCheckbox}
								/>
							);
						})}
					</tbody>
				)}
			</table>

			{loading && <Spinner overlay />}
		</div>
	);
};

export default Table;
