import Button from 'shared/components/Button';
import Cell from './Cell';
import Checkbox from 'shared/components/Checkbox';
import getCN from 'classnames';
import HeaderCell from './HeaderCell';
import Icon from 'shared/components/Icon';
import React, {Fragment, useState} from 'react';
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
	header?: boolean;
	items?: Array<any>;
	itemsSelected?: boolean;
	nestedLevel: number;
	nestedTables?: Array<any>;
	onClick?: (data: any) => void;
	renderInlineRowActions?: (params: any) => void;
	renderRowActions?: (params: any) => void;
	rowIndex: number;
	selected?: boolean;
	showCheckbox?: boolean;
}

const Row: React.FC<IRowProps> = ({
	className,
	clickable = false,
	columns = [],
	data = {},
	disabled = false,
	expandable,
	header = false,
	items = [],
	itemsSelected = false,
	nestedLevel,
	nestedTables,
	onClick = noop,
	renderInlineRowActions,
	renderRowActions,
	rowIndex,
	selected = false,
	showCheckbox = false,
	...otherProps
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

		if (header) {
			return <th />;
		} else if (renderRowActions) {
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

	const RowGroup = header ? 'thead' : 'tbody';

	const directNestedTable = nestedTables
		? nestedTables[nestedLevel]
		: undefined;

	return (
		<RowGroup className={className}>
			<tr className={classes} onClick={handleExpand}>
				{!header && showCheckbox && (
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
						dataFormatter = val => val,
						editable = false,
						className,
						label,
						sortable,
						title
					} = column;

					if (showCheckbox && i === 0 && header) {
						return (
							<Fragment key={i}>
								<th />

								<HeaderCell
									{...otherProps}
									accessor={accessor}
									className={className}
									sortable={sortable}
								>
									{label}
								</HeaderCell>
							</Fragment>
						);
					} else if (header) {
						return (
							<HeaderCell
								{...otherProps}
								accessor={accessor}
								className={className}
								key={i}
								sortable={sortable}
							>
								{label}
							</HeaderCell>
						);
					} else if (CellRenderer && editable) {
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

				{expandable &&
					(header ? (
						<th />
					) : (
						<Cell key='EXPAND_CARET'>
							<Button
								className='table-action-link'
								display='unstyled'
								onClick={handleExpand}
							>
								<Icon
									symbol={
										expanded
											? 'caret-bottom'
											: 'caret-right'
									}
								/>
							</Button>
						</Cell>
					))}
			</tr>

			{expandable && !header && expanded && (
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
		</RowGroup>
	);
};

export default Row;
