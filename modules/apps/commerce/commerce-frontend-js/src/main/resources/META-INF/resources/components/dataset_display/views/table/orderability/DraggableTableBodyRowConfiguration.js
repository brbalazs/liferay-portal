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

import {ItemTypes} from '../../../../../utilities/drag_drop/constants';
import {moveListItem, editItemOrdering} from './index';

const configureDragSource = ({
	index: indexFrom,
	itemsList,
	orderableField
}) => ({
	collect: monitor => ({
		isDragging: monitor.isDragging()
	}),

	end: ({indexFrom: indexTo, orderableField}) =>
		editItemOrdering(indexTo, itemsList, orderableField),

	item: {
		indexFrom,
		orderableField,
		type: ItemTypes.DATASET_ROW
	}
});

const configureDropTarget = ({index: indexTo, itemsList, setItemsList}) => ({
	accept: ItemTypes.DATASET_ROW,

	collect: monitor => ({
		isOver: monitor.isOver()
	}),

	hover(draggedItem) {
		const {indexFrom} = draggedItem;

		if (indexFrom !== indexTo) {
			const alteredItemsList = moveListItem(
				indexFrom,
				indexTo,
				itemsList
			);

			setItemsList(alteredItemsList);
			draggedItem.indexFrom = indexTo;
		}
	}
});

export default {
	configureDragSource,
	configureDropTarget
};
