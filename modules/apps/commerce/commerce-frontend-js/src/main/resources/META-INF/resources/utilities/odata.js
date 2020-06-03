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

export function convertObjectDateToIsoString(objDate, direction) {
	const time = direction === 'from' ? [0, 0, 0, 0] : [23, 59, 59, 999];
	const date = new Date(
		objDate.year,
		objDate.month - 1,
		objDate.day,
		...time
	);
	return date.toISOString();
}

function createOdataFilterString(
	key,
	operator = 'eq',
	type,
	value,
	selectionType
) {
	switch (type) {
		case 'autocomplete':
			if (selectionType !== 'multiple') {
				const firstItemVal = value[0].value;
				return `${key} eq ${firstItemVal}`;
			}
			break;
		case 'date':
			return `${key} ${operator} ${convertObjectDateToIsoString(value)}`;
		case 'dateRange':
			if (value.from && value.to) {
				return `${key} ge ${convertObjectDateToIsoString(
					value.from,
					'from'
				)}) and (${key} le ${convertObjectDateToIsoString(
					value.to,
					'to'
				)}`;
			}
			if (value.from) {
				return `${key} ge ${convertObjectDateToIsoString(
					value.from,
					'from'
				)}`;
			}
			if (value.to) {
				return `${key} le ${convertObjectDateToIsoString(
					value.to,
					'to'
				)}`;
			}
			break;
		default:
			if (Array.isArray(value)) {
				return value
					.map(
						el =>
							`(${createOdataFilterString(
								key,
								operator,
								type,
								el
							)})`
					)
					.join(' or ');
			}
			if (value instanceof String) {
				return `${key} ${operator} '${value}'`;
			}
	}
	return `${key} ${operator} ${value}`;
}

export default function createOdataFilter(filters) {
	if (!filters.length) return null;

	return filters
		.map(filter => {
			return createOdataFilterString(
				filter.id,
				filter.operator,
				filter.type,
				filter.value,
				filter.selectionType
			);
		})
		.map(filterString => `(${filterString})`)
		.join(' and ');
}
