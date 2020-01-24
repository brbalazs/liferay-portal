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

export function createOdataFilterString(key, operator, value) {
	let formattedValue = value;

	if (value instanceof String) {
		formattedValue = `'${value}'`;
	}
	if (value instanceof Object) {
		formattedValue = JSON.stringify(value);
	}

	return `${key} ${operator} ${formattedValue}`;
}

export function createOdataFilterStrings(filters) {
	const oDataFilterStrings = filters
		.map(filter => {
			if (filter.value instanceof Array) {
				return filter.value
					.map(
						value =>
							`(${createOdataFilterString(
								filter.id,
								filter.operator,
								value
							)})`
					)
					.join(' or ');
			}

			if (filter.main) {
				return `(startwith(${filter.id}, '${filter.value}') eq true)`;
			}

			return createOdataFilterString(
				filter.id,
				filter.operator,
				filter.value
			);
		})
		.map(filterString => `(${filterString})`)
		.join(' and ');

	const oDataFilters = oDataFilterStrings.length
		? `$filter=${oDataFilterStrings}`
		: '';
	return oDataFilters;
}
