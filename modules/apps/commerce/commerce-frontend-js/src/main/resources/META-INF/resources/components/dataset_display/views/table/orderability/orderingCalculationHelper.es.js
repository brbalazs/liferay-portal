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

const BETWEEN = 'BETWEEN',
	GREATER = 'GREATER',
	LOWER = 'LOWER';

const CALCULATION_METHODS = {
	[BETWEEN]: (index, itemsList, orderableField) => {
		const lowerPosition = itemsList[index + 1][orderableField],
			higherPosition = itemsList[index - 1][orderableField];

		return higherPosition - ((higherPosition - lowerPosition) / 2)
	},
	[GREATER]: (index, itemsList, orderableField) =>
		itemsList[index + 1][orderableField] + 1,
	[LOWER]: (index, itemsList, orderableField) =>
		itemsList[index - 1][orderableField] - 1
};

function getCalculationMethod(index, itemsList) {
	switch (true) {
		/**
		 * Current item is moved at the beginning of the items' list.
		 * Selects the method that will calculate
		 * a priority value that is GREATER
		 * than the top priority value in the list.
		 */
		case index === 0:
			return GREATER;

		/**
		 * Current item is moved at the end of the items' list.
		 * Selects the method that will calculate
		 * a priority value that is LOWER
		 * than the lowest priority value in the list.
		 */
		case index === itemsList.length - 1:
			return LOWER;

		/**
		 * Current item is moved in the middle of the item's list.
		 * Selects the method that will calculate
		 * a priority value that is BETWEEN
		 * the respective priorities of the previous and the next items.
		 */
		default:
			return BETWEEN;
	}
}

export const DEFAULT_ORDERABLE_FIELD = 'priority';

export function calculateOrderingPosition(index, itemsList, orderableField) {
	const method = getCalculationMethod(index, itemsList);

	return CALCULATION_METHODS[method](index, itemsList, orderableField);
}