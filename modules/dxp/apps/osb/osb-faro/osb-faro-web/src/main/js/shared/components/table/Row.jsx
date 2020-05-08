import autobind from 'autobind-decorator';
import Button from 'shared/components/Button';
import Cell from './Cell';
import Checkbox from 'shared/components/Checkbox';
import getCN from 'classnames';
import HeaderCell from './HeaderCell';
import Icon from 'shared/components/Icon';
import omitDefinedProps from 'shared/util/omitDefinedProps';
import React, {Fragment} from 'react';
import Table from './index';
import {get, isNil, noop} from 'lodash';
import {PropTypes} from 'prop-types';
import {StopClickPropagation} from './cell-components';

class Row extends React.Component {
	static defaultProps = {
		columns: [],
		data: {},
		disabled: false,
		header: false,
		items: [],
		itemsSelected: false,
		onClick: noop,
		onRowDelete: noop,
		onRowSave: noop,
		selected: false,
		showCheckbox: false
	};

	static propTypes = {
		columns: PropTypes.array,
		data: PropTypes.object,
		disabled: PropTypes.bool,
		expandable: PropTypes.bool,
		header: PropTypes.bool,
		items: PropTypes.array,
		itemsSelected: PropTypes.bool,
		nestedLevel: PropTypes.number,
		nestedTables: PropTypes.array,
		onClick: PropTypes.func,
		onRowDelete: PropTypes.func,
		onRowSave: PropTypes.func,
		renderInlineRowActions: PropTypes.func,
		renderRowActions: PropTypes.func,
		rowIndex: PropTypes.number,
		selected: PropTypes.bool,
		showCheckbox: PropTypes.bool
	};

	state = {
		editing: false,
		edits: {},
		expanded: false
	};

	@autobind
	handleCheckboxChange() {
		const {data, onClick} = this.props;

		onClick(data);
	}

	@autobind
	handleExpand(event) {
		this.handleEventPropagation(event);

		const {data, nestedLevel, nestedTables, onClick} = this.props;

		if (nestedTables && nestedTables[nestedLevel]) {
			this.setState({
				expanded: !this.state.expanded
			});
		}

		onClick(data);
	}

	handleEventPropagation(event) {
		event.stopPropagation();
	}

	@autobind
	handleEdit() {
		this.setState({
			editing: true
		});
	}

	@autobind
	handleResetEdits() {
		this.setState({
			editing: false,
			edits: {}
		});
	}

	@autobind
	handleUpdateEdits(attr, value) {
		const {edits} = this.state;

		this.setState({
			edits: {...edits, [attr]: value}
		});
	}

	renderActionColumn() {
		const {
			props: {
				data,
				header,
				items,
				itemsSelected,
				renderInlineRowActions,
				renderRowActions
			},
			state: {editing, edits}
		} = this;

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
								onRowCancel: this.handleResetEdits,
								onRowEdit: this.handleEdit,
								onRowSave: this.handleResetEdits
							}
						})}
					</StopClickPropagation>
				</Cell>
			);
		}
	}

	render() {
		const {
			props: {
				className,
				clickable,
				columns,
				data,
				disabled,
				expandable,
				header,
				nestedLevel,
				nestedTables,
				renderInlineRowActions,
				renderRowActions,
				rowIndex,
				selected,
				showCheckbox,
				...otherProps
			},
			state: {editing, expanded}
		} = this;

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
				<tr className={classes} onClick={this.handleExpand}>
					{!header && showCheckbox && (
						<Cell>
							<Checkbox
								checked={selected}
								disabled={disabled}
								onChange={this.handleCheckboxChange}
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
										{...omitDefinedProps(
											otherProps,
											Row.propTypes
										)}
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
									{...omitDefinedProps(
										otherProps,
										Row.propTypes
									)}
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
									onClick={this.handleEventPropagation}
									onUpdateEdits={this.handleUpdateEdits}
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
									onClick={this.handleEventPropagation}
									rowIndex={rowIndex}
								/>
							);
						} else {
							const dataValue = get(data, accessor);

							return (
								<Cell
									className={className}
									key={i}
									title={title}
								>
									{!isNil(dataValue)
										? dataFormatter(dataValue, data)
										: '-'}
								</Cell>
							);
						}
					})}

					{(renderRowActions || renderInlineRowActions) &&
						this.renderActionColumn()}

					{expandable &&
						(header ? (
							<th />
						) : (
							<Cell key='EXPAND_CARET'>
								<Button
									className='table-action-link'
									display='unstyled'
									onClick={this.handleExpand}
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
	}
}

export default Row;
