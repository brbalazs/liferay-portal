import ClayTable from '@clayui/table';
import PropTypes from 'prop-types';
import React, { useContext } from 'react';

import Comment from '../../data_renderer/Comment.es';
import { getDataRenderer } from '../../data_renderer/index.es';
import TableHeadRow from './TableHeadRow.es';

function CustomTableCell(props) {
	if (!props.value) {
		return (<ClayTable.Cell />)
	}

	const { dataRenderers, template, ...otherProps } = props;
	const Template = getDataRenderer(template, dataRenderers);

	return (
		<ClayTable.Cell>
			<Template {...otherProps} />
			{props.comment && <Comment>{props.comment}</Comment>}
		</ClayTable.Cell>
	);
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
	const {dataRenderers, formRef} = useContext(props.datasetDisplayContext);
	const showActionItems = !!props.items.find(el => el.actionItems);
	const allElementsSelected = areAllElementsSelected(
		props.selectedItemsId,
		props.items
	);

	const ActionsDropdown = getDataRenderer('actionsDropdown', dataRenderers)

	return (
		<form ref={formRef}>
			<ClayTable borderless responsive={false}>
				<TableHeadRow
					allElementsSelected={allElementsSelected}
					itemsQuantity={props.items.length}
					onSelect={props.onSelect}
					schema={props.schema}
					selectable={props.selectable}
					selectedItemsId={props.selectedItemsId}
					showActionItems={showActionItems}
					sorting={props.sorting}
				/>
				<ClayTable.Body>
					{props.items.map(item => (
						<ClayTable.Row key={item.id}>
							{props.selectable && (
								<CustomTableCell
									checked={
										!!props.selectedItemsId.find(
											el => el === item.id
										)
									}
									dataRenderers={dataRenderers}
									name="selectedIds"
									onSelect={props.onSelect}
									template="checkbox"
									value={item.id}
								/>
							)}
							{props.schema.fields.map(field => {
								const fieldName = field.fieldName;
								const {
									[fieldName]: value,
									...otherProps
								} = item;
								const comment = otherProps.comments
									? otherProps.comments[
											field.fieldName
										]
									: null;
								return (
									<CustomTableCell
										comment={comment}
										data={otherProps}
										dataRenderers={dataRenderers}
										fieldName={field.fieldName}
										key={field.fieldName}
										template={field.contentRenderer}
										value={value}
									/>
								);
							})}
							{showActionItems ? (
								item.actionItems ? (
									<ClayTable.Cell>
										<ActionsDropdown
											items={item.actionItems}
										/>
									</ClayTable.Cell >
								) : (
									<ClayTable.Cell />
								)
							) : null}
						</ClayTable.Row>
					))}
				</ClayTable.Body>
			</ClayTable>
		</form>
	)
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
	selectedItemsId: PropTypes.array,
};

Table.defaultProps = {
	items: []
};

export default Table;
