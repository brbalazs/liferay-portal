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

import getCN from 'classnames';
import {useDrag, useDrop} from 'react-dnd';
import React, {useContext} from 'react';

import {ItemTypes} from '../../../utilities/drag_drop/constants.es';
import DatasetDisplayContext from '../DatasetDisplayContext.es';

function getClassNames(isDragging, isOver) {
	return getCN(
		'draggable-cell',
		isDragging ? 'is-dragging' : '',
		isOver ? 'is-over' : ''
	);
}

function DraggableDroppable({item: rowItem, value}) {
	const {openModal} = useContext(DatasetDisplayContext);

	const [{isOver}, drop] = useDrop({
		accept: ItemTypes.DATASET_CELL,

		collect: monitor => ({isOver: monitor.isOver()}),

		drop(dropResult) {
			const idTo = rowItem.id,
				idFrom = dropResult.id;

			if (idFrom !== idTo) {
				openModal({idFrom, idTo});
			}
		}
	});

	const [{isDragging}, drag] = useDrag({
		collect: monitor => ({isDragging: monitor.isDragging()}),

		item: {
			id: rowItem.id,
			type: ItemTypes.DATASET_CELL
		}
	});

	return drop(
		drag(<span className={getClassNames(isDragging, isOver)}>{value}</span>)
	);
}

export default DraggableDroppable;
