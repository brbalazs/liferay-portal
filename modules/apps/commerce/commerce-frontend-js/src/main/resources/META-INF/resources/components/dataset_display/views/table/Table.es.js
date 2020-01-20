import ClayTable from '@clayui/table';
import PropTypes from 'prop-types';
import React, { useState, useEffect, useContext } from 'react';

import ActionsDropdown from '../../data_renderer/ActionsDropdown.es';
import Checkbox from '../../data_renderer/Checkbox.es';
import Comment from '../../data_renderer/Comment.es';
import { getDataRendererById, getDataRendererByUrl } from '../../data_renderer/index.es';
import TableHeadRow from './TableHeadRow.es';

function CustomTableCell(props) {
	const { view, ...otherProps } = props;
	const [ currentView, updateCurrentView ] = useState({
		...view,
		Component: view.contentRendererModuleUrl ? null : getDataRendererById(view.contentRenderer)
	});
	const [ loading, setLoading ] = useState(false);

	useEffect(() => {
		if(loading) {
			return;
		}
		if(currentView.contentRendererModuleUrl) {
			setLoading(true);
			getDataRendererByUrl(currentView.contentRendererModuleUrl).then(
				(Component) => {
					updateCurrentView({
						...currentView,
						Component
					})
					setLoading(false)
				})
		}
	}, [])
	
	return props.value ? (
		<ClayTable.Cell>
			{(currentView.Component && !loading) ? (
				<currentView.Component {...otherProps} value={props.value} />
			) : (
				<span aria-hidden="true" className="loading-animation loading-animation-sm" />
			)}
			{props.comment && <Comment>{props.comment}</Comment>}
		</ClayTable.Cell>
	) : <ClayTable.Cell />
}

function areAllElementsSelected(selectedItemsId, allItems) {
	const selectedItemsString = selectedItemsId.sort().join(',');
	const allItemsString = allItems
		.map(el => el.id)
		.sort()
		.join(',');

	return selectedItemsString === allItemsString;
}

function Table(props) {
	const {
		formRef,
		selectItems,
		selectable,
		selectedItemsId,
		sorting
	} = useContext(props.datasetDisplayContext);

	const showActionItems = !!props.items.find(el => el.actionItems);

	const allElementsSelected = areAllElementsSelected(
		selectedItemsId,
		props.items
	);

	return (
		<form ref={formRef}>
			<ClayTable borderless responsive={false}>
				<TableHeadRow
					allElementsSelected={allElementsSelected}
					itemsQuantity={props.items.length}
					schema={props.schema}
					selectItems={selectItems}
					selectable={selectable}
					selectedItemsId={selectedItemsId}
					showActionItems={showActionItems}
					sorting={sorting}
				/>
				<ClayTable.Body>
					{props.items.map(item => (
						<ClayTable.Row key={item.id}>
							{selectable && (
								<ClayTable.Cell>
									<Checkbox
										checked={
											!!selectedItemsId.find(
												el => el === item.id
											)
										}
										name="selectedIds"
										onSelect={selectItems}
										value={item.id}
									/>
								</ClayTable.Cell>
							)}
							{props.schema.fields.map(field => {
								const fieldName = field.fieldName;
								const {
									[fieldName]: value,
									...otherProps
								} = item;
								const comment = otherProps.comments
									? otherProps.comments[field.fieldName]
									: null;
								return (
									<CustomTableCell
										comment={comment}
										data={otherProps}
										fieldName={field.fieldName}
										key={field.fieldName}
										value={value}
										view={{
											contentRenderer: field.contentRenderer,
											contentRendererModuleUrl: field.contentRendererModuleUrl
										}}
									/>
								);
							})}
							{showActionItems ? (
								item.actionItems ? (
									<ClayTable.Cell>
										<ActionsDropdown
											items={item.actionItems}
										/>
									</ClayTable.Cell>
								) : (
									<ClayTable.Cell />
								)
							) : null}
						</ClayTable.Row>
					))}
				</ClayTable.Body>
			</ClayTable>
		</form>
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
	}).isRequired,
	selectedItemsId: PropTypes.array
};

Table.defaultProps = {
	items: []
};

export default Table;
