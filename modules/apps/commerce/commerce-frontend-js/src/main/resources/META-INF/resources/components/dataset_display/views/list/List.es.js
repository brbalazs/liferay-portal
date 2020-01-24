import {ClayRadio, ClayCheckbox} from '@clayui/form';
import ClayList from '@clayui/list';
import classNames from 'classnames';
import PropTypes from 'prop-types';
import React, {useContext} from 'react';

function List(props) {
	const {
		selectItems,
		selectedItemsKey,
		selectedItemsValue,
		selectionType
	} = useContext(props.datasetDisplayContext);

	return (
		<ClayList>
			{props.items.map((item, i) => (
				<ClayList.Item
					className={classNames(
						i
							? 'border-left-0 border-bottom-0 border-right-0'
							: 'border-0'
					)}
					flex
					key={item.id}
				>
					<ClayList.ItemField>
						{selectionType === 'single' ? (
							<ClayRadio
								checked={selectedItemsValue.includes(
									item[selectedItemsKey]
								)}
								onChange={() =>
									selectItems(item[selectedItemsKey])
								}
							/>
						) : (
							<ClayCheckbox
								checked={selectedItemsValue.includes(
									item[selectedItemsKey]
								)}
								onChange={() =>
									selectItems(item[selectedItemsKey])
								}
							/>
						)}
					</ClayList.ItemField>
					<ClayList.ItemField expand>
						{props.schema.title && (
							<ClayList.ItemTitle>
								{item[props.schema.title]}
							</ClayList.ItemTitle>
						)}
						{props.schema.description && (
							<ClayList.ItemText>
								{item[props.schema.description]}
							</ClayList.ItemText>
						)}
					</ClayList.ItemField>
				</ClayList.Item>
			))}
		</ClayList>
	);
}

List.propTypes = {
	context: PropTypes.any,
	items: PropTypes.arrayOf(
		PropTypes.shape({
			id: PropTypes.oneOfType([PropTypes.string, PropTypes.number])
				.isRequired
		})
	),
	schema: PropTypes.shape({
		description: PropTypes.string,
		selectedItemValue: PropTypes.string,
		title: PropTypes.string
	})
};

List.defaultTypes = {
	activeItemValue: ''
};

export default List;
