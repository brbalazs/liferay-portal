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

export const prettifyCheckboxValue = (value, items) => {
	const prettifiedValue = value
		? value
				.map(v => {
					return items.reduce(
						(found, item) =>
							found || (item.value === v ? item.label : null),
						null
					);
				})
				.join(', ')
		: '';

	return prettifiedValue;
};

export const prettifySelectValue = (value, items) => {
	const prettifiedValue = value
		? items.reduce(
				(found, item) =>
					found || (item.value === value ? item.label : null),
				null
		  )
		: '';

	return prettifiedValue;
};

export const prettifyDateValue = value => {
	if (!value) {
		return '';
	}

	const date =
		value instanceof Date
			? value
			: new Date(value.year, value.month, value.day);

	return date.toLocaleDateString();
};

export const prettifyDateTimeValue = value => {
	if (!value) {
		return '';
	}

	const date =
		value instanceof Date
			? value
			: new Date(
					value.year,
					value.month,
					value.day,
					value.hours,
					value.minutes,
					value.seconds
			  );

	return date.toLocaleDateString() + ' ' + date.toLocaleTimeString();
};

export const prettifyFilterValue = props => {
	switch (props.type) {
		case 'checkbox':
			return prettifyCheckboxValue(props.value, props.items);
		case 'radio':
		case 'select':
			return prettifySelectValue(props.value, props.items);
		case 'date':
			return prettifyDateValue(props.value);
		case 'date-time':
			return prettifyDateTimeValue(props.value);

		default:
			return props.value;
	}
};
