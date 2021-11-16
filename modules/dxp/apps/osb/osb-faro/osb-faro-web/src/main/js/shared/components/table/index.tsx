import getCN from 'classnames';
import HeaderRow from './HeaderRow';
import React from 'react';
import Row, {Column} from './Row';
import Spinner from 'shared/components/Spinner';
import {get, isArray, noop} from 'lodash';
import {OrderedMap} from 'immutable';
import {OrderParams} from 'shared/util/records';

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
	checkDisabled?: (item: object) => boolean;
	className?: string;
	columns: Column[];
	// defaultSort?: {
	// 	field: string;
	// 	sortOrder: string;
	// }; // TODO: Convert this over to orderIOMap;
	enableMultiSort?: boolean;
	headingNowrap?: boolean;
	internalSort?: boolean;
	items: {[key: string]: any}[];
	list?: boolean;
	loading?: boolean;
	nowrap?: boolean;
	orderIOMap?: OrderedMap<string, OrderParams>; // TODO: Maybe optional? we'll see
	onOrderIOMapChange?: (orderIOMap: OrderedMap<string, OrderParams>) => void;
	onRowClick?: (item: object) => void; // TODO: Maybe do something about this.
	onRowDelete?: (item: object) => void;
	onRowSave?: (item: object) => void;
	onSelectItemsChange?: (item: any) => void;
	renderInlineRowActions?: (params: {
		data: {[key: string]: any};
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
	selectedItemsIOMap?: OrderedMap<string, object>;
	showCheckbox?: boolean;
}

const Table: React.FC<ITableProps> = ({
	bordered,
	checkDisabled = () => false,
	className,
	columns,
	// defaultSort, // TODO: No more default because it's all handled by its parent
	enableMultiSort = false,
	headingNowrap = true,
	internalSort = false, // TODO: maybe have internal sort in here still but base it off of the provided sort
	items = [],
	list = false,
	loading = false,
	nowrap = true,
	onOrderIOMapChange,
	onRowClick,
	onRowDelete = noop,
	onRowSave = noop,
	onSelectItemsChange,
	orderIOMap = OrderedMap(),
	renderInlineRowActions,
	renderRowActions,
	rowBordered = true,
	rowIdentifier = 'id',
	selectedItemsIOMap = OrderedMap(),
	showCheckbox = false
}) => {
	const handleSortOrderChange = (orderParams: OrderParams) => {
		if (onOrderIOMapChange) {
			if (enableMultiSort) {
				onOrderIOMapChange(
					orderIOMap.set(orderParams.field, orderParams)
				);
			} else {
				onOrderIOMapChange(
					OrderedMap({[orderParams.field]: orderParams})
				);
			}
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

	// const handleSort = field => {
	// 	const updatedOrderParams =
	// 		orderParams.field === field
	// 			? orderParams.update('sortOrder', order => invertOrder(order))
	// 			: new OrderParams({
	// 					field,
	// 					sortOrder: getDefaultSortOrder(field)
	// 			  });

	// 	setOrderParams(updatedOrderParams);
	// };

	// const sortItems = items =>
	// 	orderBy(
	// 		items,
	// 		item => {
	// 			const fieldValue = item[orderParams.field];

	// 			if (typeof fieldValue === 'string') {
	// 				return fieldValue.toLowerCase();
	// 			}

	// 			return fieldValue;
	// 		},
	// 		orderParams.sortOrder
	// 	);

	const classes = getCN('table', 'table-autofit', 'table-hover', {
		'show-quick-actions-on-hover': renderRowActions,
		'table-bordered': bordered,
		'table-heading-nowrap': headingNowrap,
		'table-list': list,
		'table-nowrap': nowrap,
		'table-row-no-bordered': !rowBordered
	});

	const itemsSorted = items; // internalSort ? sortItems(items) : items;

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
					headerLink={!internalSort && !onOrderIOMapChange}
					onSortOrderChange={handleSortOrderChange}
					orderIOMap={orderIOMap}
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
										!!onRowClick ||
										(showCheckbox && !!onSelectItemsChange)
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

export {Column};
export default Table;
