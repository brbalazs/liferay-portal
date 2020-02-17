/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

import ClayTable from '@clayui/table';
import classNames from 'classnames';
import PropTypes from 'prop-types';
import React, {useState, useEffect, useContext} from 'react';

import DatasetDisplayContext from '../../DatasetDisplayContext.es';
import ActionsDropdown from '../../data_renderer/ActionsDropdown.es';
import Checkbox from '../../data_renderer/Checkbox.es';
import Comment from '../../data_renderer/Comment.es';
import Radio from '../../data_renderer/Radio.es';
import {
	getDataRendererById,
	getDataRendererByUrl
} from '../../data_renderer/index.es';
import TableHeadRow from './TableHeadRow.es';

function CustomTableCell(props) {
	const {view} = props;
	const [currentView, updateCurrentView] = useState({
		...view,
		Component: view.contentRendererModuleUrl
			? null
			: getDataRendererById(view.contentRenderer)
	});
	const [loading, setLoading] = useState(false);

	useEffect(() => {
		if (loading) {
			return;
		}
		if (currentView.contentRendererModuleUrl) {
			setLoading(true);
			getDataRendererByUrl(currentView.contentRendererModuleUrl).then(
				Component => {
					updateCurrentView({
						...currentView,
						Component
					});
					setLoading(false);
				}
			);
		}
		// eslint-disable-next-line react-hooks/exhaustive-deps
	}, [currentView]);

	return (
		<ClayTable.Cell>
			{currentView.Component && !loading ? (
				<currentView.Component
					actions={props.actions}
					itemId={props.itemId}
					options={props.options}
					value={props.value}
				/>
			) : (
				<span
					aria-hidden="true"
					className="loading-animation loading-animation-sm"
				/>
			)}
			{props.comment && <Comment>{props.comment}</Comment>}
		</ClayTable.Cell>
	);
}

function Table(props) {
	const {
		highlightedItemsValue,
		selectItems,
		selectable,
		selectedItemsKey,
		selectedItemsValue,
		selectionType,
		sorting,
		updateSorting
	} = useContext(DatasetDisplayContext);

	const showActionItems = !!props.items.find(el => el.actionItems);

	const SelectionComponent = selectionType === 'multiple' ? Checkbox : Radio;

	return (
		<ClayTable borderless responsive={false}>
			<TableHeadRow
				items={props.items}
				schema={props.schema}
				selectItems={selectItems}
				selectable={selectable}
				selectedItemsKey={selectedItemsKey}
				selectedItemsValue={selectedItemsValue}
				selectionType={selectionType}
				showActionItems={showActionItems}
				sorting={sorting}
				updateSorting={updateSorting}
			/>
			<ClayTable.Body>
				{props.items.map((item, i) => {
					const itemId = item[selectedItemsKey] || i;

					return (
						<ClayTable.Row 
							className={classNames(
								highlightedItemsValue.includes(itemId) &&
									'active'
							)}
							key={itemId}
						>
							{selectable && (
								<ClayTable.Cell>
									<SelectionComponent
										checked={
											!!selectedItemsValue.find(
												el =>
													String(el) ===
													String(itemId)
											)
										}
										onChange={() =>
											selectItems(itemId)
										}
										value={itemId}
									/>
								</ClayTable.Cell>
							)}
							{props.schema.fields.map((field, i) => {
								const fieldName = field.fieldName;
								const {
									actionItems,
									[fieldName]: value,
									...otherProps
								} = item;
								const comment = otherProps.comments
									? otherProps.comments[field.fieldName]
									: null;
								return (
									<CustomTableCell
										actions={actionItems}
										comment={comment}
										itemId={itemId}
										key={fieldName || i}
										options={field}
										value={value}
										view={{
											contentRenderer: field.contentRenderer,
											contentRendererModuleUrl:
												field.contentRendererModuleUrl
										}}
									/>
								);
							})}
							{showActionItems ? (
								item.actionItems ? (
									<ClayTable.Cell>
										<ActionsDropdown
											actions={item.actionItems}
											itemId={itemId}
										/>
									</ClayTable.Cell>
								) : (
									<ClayTable.Cell />
								)
							) : null}
						</ClayTable.Row>
					)
				})}
			</ClayTable.Body>
		</ClayTable>
	);
}

Table.propTypes = {
	items: PropTypes.arrayOf(
		PropTypes.shape({
			id: PropTypes.oneOfType([PropTypes.string, PropTypes.number])
				.isRequired
		})
	),
	schema: PropTypes.shape({
		fields: PropTypes.array.isRequired
	}).isRequired
};

Table.defaultProps = {
	items: []
};

export default Table;
