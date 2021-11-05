import Button from 'shared/components/Button';
import Cell from './Cell';
import Checkbox from 'shared/components/Checkbox';
import getCN from 'classnames';
import Icon from 'shared/components/Icon';
import React, {useState} from 'react';
import Table from './index';
import {get, isNil, noop} from 'lodash';
import {StopClickPropagation} from './cell-components';

interface IRowProps {
	className?: string;
	clickable?: boolean;
	columns?: Array<any>;
	data?: Object;
	disabled?: boolean;
	expandable?: boolean;
	items?: Array<any>;
	itemsSelected?: boolean;
	nestedLevel: number;
	nestedTables?: Array<any>;
	onClick?: (data: any) => void;
	renderInlineRowActions?: (params: any) => void; // Can we just remove this?  doesn't seem to be that useful... we can just use it in the columns
	renderRowActions?: (params: any) => void;
	rowIndex: number;
	selected?: boolean;
	showCheckbox?: boolean;
}

const Row: React.FC<IRowProps> = ({
	clickable = false,
	columns = [],
	data = {},
	disabled = false,
	expandable,
	items = [],
	itemsSelected = false,
	nestedLevel,
	nestedTables,
	onClick = noop,
	renderInlineRowActions,
	renderRowActions,
	rowIndex,
	selected = false,
	showCheckbox = false
}) => {
	const [state, setState] = useState({
		editing: false,
		edits: {},
		expanded: false
	});

	const handleCheckboxChange = () => onClick(data);

	const handleExpand = event => {
		handleEventPropagation(event);

		if (nestedTables && nestedTables[nestedLevel]) {
			setState(prevState => ({
				...prevState,
				expanded: !prevState.expanded
			}));
		}

		onClick(data);
	};

	const handleEventPropagation = event => event.stopPropagation();

	const handleEdit = () =>
		setState(prevState => ({
			...prevState,
			editing: true
		}));

	const handleResetEdits = () =>
		setState(prevState => ({
			...prevState,
			editing: false,
			edits: {}
		}));

	const handleUpdateEdits = (attr, value) =>
		setState(prevState => ({
			...prevState,
			edits: {...prevState.edits, [attr]: value}
		}));

	const renderActionColumn = () => {
		const {editing, edits} = state;

		if (renderRowActions) {
			return (
				<Cell className='row-actions' key='ROW_ACTIONS'>
					<StopClickPropagation>
						{renderRowActions({
							data,
							items
						})}
					</StopClickPropagation>
				</Cell>
			);
		} else if (renderInlineRowActions) {
			return (
				<Cell className='row-inline-actions' key='INLINE_ACTIONS'>
					<StopClickPropagation>
						{renderInlineRowActions({
							data,
							editing,
							edits,
							items,
							itemsSelected,
							rowEvents: {
								onRowCancel: handleResetEdits,
								onRowEdit: handleEdit,
								onRowSave: handleResetEdits
							}
						})}
					</StopClickPropagation>
				</Cell>
			);
		}
	};

	const {editing, expanded} = state;

	const classes = getCN({
		clickable,
		disabled,
		'table-active': selected
	});

	const directNestedTable = nestedTables
		? nestedTables[nestedLevel]
		: undefined;

	return (
		<>
			<tr className={classes} onClick={handleExpand}>
				{showCheckbox && (
					<Cell>
						<Checkbox
							checked={selected}
							disabled={disabled}
							onChange={handleCheckboxChange}
						/>
					</Cell>
				)}

				{columns.map((column, i) => {
					const {
						accessor,
						cellRenderer: CellRenderer,
						cellRendererProps,
						className,
						dataFormatter = val => val,
						editable = false,
						title
					} = column;

					if (CellRenderer && editable) {
						return (
							<CellRenderer
								{...cellRendererProps}
								className={className}
								data={data}
								disabled={disabled}
								editing={editing && !selected}
								key={i}
								onClick={handleEventPropagation}
								onUpdateEdits={handleUpdateEdits}
							/>
						);
					} else if (CellRenderer) {
						return (
							<CellRenderer
								{...cellRendererProps}
								className={className}
								data={data}
								disabled={disabled}
								key={i}
								onClick={handleEventPropagation}
								rowIndex={rowIndex}
							/>
						);
					} else {
						const dataValue = get(data, accessor);

						return (
							<Cell className={className} key={i} title={title}>
								{!isNil(dataValue)
									? dataFormatter(dataValue, data)
									: '-'}
							</Cell>
						);
					}
				})}

				{(renderRowActions || renderInlineRowActions) &&
					renderActionColumn()}

				{expandable && (
					<Cell key='EXPAND_CARET'>
						<Button
							className='table-action-link'
							display='unstyled'
							onClick={handleExpand}
						>
							<Icon
								symbol={
									expanded ? 'caret-bottom' : 'caret-right'
								}
							/>
						</Button>
					</Cell>
				)}
			</tr>

			{expandable && expanded && (
				<tr className='row-nested'>
					<td colSpan={columns.length + 1}>
						<Table
							columns={directNestedTable.columns}
							data={get(data, directNestedTable.accessor)}
							defaultSort={directNestedTable.defaultSort}
							nestedLevel={nestedLevel + 1}
							nestedTables={nestedTables}
							rowIdentifier={directNestedTable.rowIdentifier}
						/>
					</td>
				</tr>
			)}
		</>
	);
};

export default Row;
