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

export function convertObjectDateToIsoString(objDate) {
	const date = new Date(objDate.year, objDate.month - 1, objDate.day);
	return date.toISOString();
}

export function createOdataFilterString(key, defaultOperator, type, value) {
	const operator = defaultOperator || 'eq';

	switch (type) {
		case 'date':
			return `${key} ${operator} ${convertObjectDateToIsoString(value)}`;
		case 'dateRange':
			if (value.from && value.to) {
				return `${key} gt ${convertObjectDateToIsoString(
					value.from
				)}) and (${key} lt ${convertObjectDateToIsoString(value.to)}`;
			}
			if (value.from) {
				return `${key} gt ${convertObjectDateToIsoString(value.from)}`;
			}
			if (value.to) {
				return `${key} lt ${convertObjectDateToIsoString(value.to)}`;
			}
			break;
		default:
			if (value instanceof String) {
				return `${key} ${operator} '${value}'`;
			}
			if (value instanceof Object) {
				return `${key} ${operator} ${JSON.stringify(value)}`;
			}
	}
	return `${key} ${operator} ${value}`;
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
								filter.type,
								value
							)})`
					)
					.join(' or ');
			}

			return createOdataFilterString(
				filter.id,
				filter.operator,
				filter.type,
				filter.value
			);
		})
		.map(filterString => `(${filterString})`)
		.join(' and ');

	const oDataFilters = oDataFilterStrings.length
		? `filter=${oDataFilterStrings}`
		: '';
	return oDataFilters;
}
