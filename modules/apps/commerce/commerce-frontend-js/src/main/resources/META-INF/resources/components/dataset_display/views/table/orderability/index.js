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

import {DEFAULT_ORDERABLE_FIELD} from './orderingCalculationHelper';

export function secureOrderability(itemsList, orderableField) {
	/**
	 * To avoid overhead, will trust the first item of the list.
	 */

	const invalid = `Unable to find orderableField: "${orderableField}"`,
		foundDefault =
			'but found default orderableField:\n' +
			`"${DEFAULT_ORDERABLE_FIELD}". Falling back...`;

	if (orderableField in itemsList[0]) {
		return orderableField;
	} else if (DEFAULT_ORDERABLE_FIELD in itemsList[0]) {
		// eslint-disable-next-line no-console
		console.warn(invalid, foundDefault);

		return DEFAULT_ORDERABLE_FIELD;
	}

	// eslint-disable-next-line no-console
	console.warn(invalid);

	return null;
}

export function hasEnoughItems(itemsList) {
	return itemsList.length > 1;
}
