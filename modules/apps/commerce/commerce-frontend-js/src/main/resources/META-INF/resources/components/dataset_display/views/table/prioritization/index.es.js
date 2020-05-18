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


import {calculatePriority} from "./calculationHelper.es";

export {
	default as DraggableTableBodyRow
} from './DraggableTableBodyRow.es';
export {
	default as TableBodyRowConfiguration
} from './DraggableTableBodyRowConfiguration.es';

export function moveListItem(indexFrom, indexTo, itemsList) {
	const [...clonedList] = itemsList,
		[itemToMove] = clonedList.splice(indexFrom, 1);

	clonedList.splice(indexTo, 0, itemToMove);

	return clonedList;
}

export function patchItemPriority(indexTo, itemsList) {
	const currentItem = itemsList[indexTo],
		currentItemId = currentItem.id,
		currentItemPriority = calculatePriority(indexTo, itemsList);

	if (!currentItemPriority || __DEV_MODE__) {
		return;
	}

	currentItem.priority = currentItemPriority;

	// TODO have the right URL passed in and currentItem sent
	return fetch(url, {
		body: currentItem,
		credentials: 'include',
		headers: new Headers({
			'x-csrf-token': Liferay.authToken,
			'content-type': 'application/json'
		}),
		method: 'PATCH'
	})
		.then(response => response.json())
		.then(() => { /* handle success */
		})
		.catch(err => { /* handle errors */
		});
}