import autobind from 'autobind-decorator';
import FaroConstants from 'shared/util/constants';
import getCN from 'classnames';
import HeaderRow from './HeaderRow';
import React from 'react';
import Row from './Row';
import Spinner from 'shared/components/Spinner';
import {buildOrderByFields} from 'shared/util/pagination';
import {get, isArray, noop, orderBy} from 'lodash';
import {getDefaultSortOrder, invertOrder} from 'shared/util/pagination';
import {hasChanges} from 'shared/util/react';
import {OrderedMap} from 'immutable';
import {OrderParams} from 'shared/util/records';
import {PropTypes} from 'prop-types';

const {orderDefault} = FaroConstants.pagination;

const COLUMN_SHAPE = PropTypes.shape({
	accessor: PropTypes.string,
	cellRenderer: PropTypes.func,
	cellRendererProps: PropTypes.object,
	className: PropTypes.string,
	dataFormatter: PropTypes.func,
	label: PropTypes.oneOfType([PropTypes.string, PropTypes.object]),
	sortable: PropTypes.bool,
	title: PropTypes.bool
});

export const getRowIdentifierValue = (item, rowIdentifier) => {
	if (isArray(rowIdentifier)) {
		return rowIdentifier.reduce((acc, rowIdentifierKey) => {
			acc = acc.concat(get(item, rowIdentifierKey, rowIdentifierKey));

			return acc;
		}, '');
	}

	return get(item, rowIdentifier);
};

class Table extends React.Component {
	static defaultProps = {
		checkDisabled: noop,
		headingNowrap: true,
		internalSort: false,
		items: [],
		list: false,
		loading: false,
		nowrap: true,
		onRowDelete: noop,
		onRowSave: noop,
		rowBordered: true,
		rowIdentifier: 'id',
		selectedItemsIOMap: new OrderedMap(),
		showCheckbox: false
	};

	static propTypes = {
		bordered: PropTypes.bool,
		checkDisabled: PropTypes.func,
		columns: PropTypes.arrayOf(COLUMN_SHAPE).isRequired,
		defaultSort: PropTypes.shape({
			// TODO: Replace defaultSort with orderIOMap filled with OrderParam records. table will not handle state anymore
			field: PropTypes.string,
			sortOrder: PropTypes.string
		}),
		entityType: PropTypes.string,
		headingNowrap: PropTypes.bool,
		internalSort: PropTypes.bool,
		items: PropTypes.array,
		list: PropTypes.bool,
		loading: PropTypes.bool,
		nowrap: PropTypes.bool,
		onRowClick: PropTypes.func,
		onRowDelete: PropTypes.func,
		onRowSave: PropTypes.func,
		onSelectItemsChange: PropTypes.func,
		onSortChange: PropTypes.func,
		renderInlineRowActions: PropTypes.func,
		renderRowActions: PropTypes.func,
		rowBordered: PropTypes.bool,
		rowIdentifier: PropTypes.oneOfType([PropTypes.string, PropTypes.array]),
		selectedItemsIOMap: PropTypes.instanceOf(OrderedMap),
		showCheckbox: PropTypes.bool
	};

	state = {
		orderParams: new OrderParams()
	};

	constructor(props) {
		super(props);

		const {defaultSort} = this.props;

		if (defaultSort) {
			this.state = {
				...this.state,
				orderParams: new OrderParams({
					sortOrder: orderDefault,
					...defaultSort
				})
			};
		}
	}

	componentDidUpdate(prevProps) {
		const {defaultSort} = this.props;

		if (
			hasChanges(prevProps, this.props, 'columns', 'defaultSort') &&
			defaultSort
		) {
			this.setState(
				{
					orderParams: new OrderParams({
						sortOrder: orderDefault,
						...defaultSort
					})
				},
				this.handleEmitOnSortChange
			);
		}
	}

	@autobind
	handleItemClick(item) {
		const {onRowClick, onSelectItemsChange, showCheckbox} = this.props;

		if (showCheckbox && onSelectItemsChange) {
			onSelectItemsChange(item);
		}

		if (onRowClick) {
			onRowClick(item);
		}
	}

	handleEmitOnSortChange() {
		const {
			props: {entityType, onSortChange},
			state: {orderParams}
		} = this;

		if (onSortChange) {
			onSortChange({
				orderByFields: buildOrderByFields(orderParams, entityType),
				orderParams
			});
		}
	}

	@autobind
	handleSort(field) {
		const {orderParams} = this.state;

		const updatedOrderParams =
			orderParams.field === field
				? orderParams.update('sortOrder', order => invertOrder(order))
				: new OrderParams({
						field,
						sortOrder: getDefaultSortOrder(field)
				  });

		this.setState(
			{
				orderParams: updatedOrderParams
			},
			this.handleEmitOnSortChange
		);
	}

	sortItems(items) {
		const {orderParams} = this.state;

		return orderBy(
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
	}

	render() {
		const {
			props: {
				bordered,
				checkDisabled,
				className,
				columns,
				headingNowrap,
				internalSort,
				items,
				list,
				loading,
				nowrap,
				onRowClick,
				onRowDelete,
				onRowSave,
				onSelectItemsChange,
				onSortChange,
				renderInlineRowActions,
				renderRowActions,
				rowBordered,
				rowIdentifier,
				selectedItemsIOMap,
				showCheckbox
			},
			state: {orderParams}
		} = this;

		const classes = getCN('table', 'table-autofit', 'table-hover', {
			'show-quick-actions-on-hover': renderRowActions,
			'table-bordered': bordered,
			'table-heading-nowrap': headingNowrap,
			'table-list': list,
			'table-nowrap': nowrap,
			'table-row-no-bordered': !rowBordered
		});

		const itemsSorted = internalSort ? this.sortItems(items) : items;

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
						onSort={this.handleSort}
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
											(showCheckbox &&
												onSelectItemsChange)
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
										onClick={
											disabled
												? noop
												: this.handleItemClick
										}
										onRowDelete={onRowDelete}
										onRowSave={onRowSave}
										renderInlineRowActions={
											renderInlineRowActions
										}
										renderRowActions={renderRowActions}
										rowIndex={rowIndex}
										selected={
											onSelectItemsChange
												? selectedItemsIOMap.has(
														item.id
												  )
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
	}
}

export default Table;
