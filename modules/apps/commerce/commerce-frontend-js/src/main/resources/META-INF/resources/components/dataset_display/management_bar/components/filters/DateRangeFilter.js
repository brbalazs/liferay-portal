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

import ClayButton from '@clayui/button';
import ClayForm from '@clayui/form';
import PropTypes from 'prop-types';
import React, {useState} from 'react';

import {
	formatDateObject,
	getDateFromDateString
} from '../../../utilities/dates';

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

function getOdataString(value, key) {
	if (value.from && value.to) {
		return `${key} ge ${convertObjectDateToIsoString(
			value.from,
			'from'
		)}) and (${key} le ${convertObjectDateToIsoString(value.to, 'to')}`;
	}
	if (value.from) {
		return `${key} ge ${convertObjectDateToIsoString(value.from, 'from')}`;
	}
	if (value.to) {
		return `${key} le ${convertObjectDateToIsoString(value.to, 'to')}`;
	}
}
function DateRangeFilter(props) {
	const [fromValue, setFromValue] = useState(
		props.value && props.value.from && formatDateObject(props.value.from)
	);
	const [toValue, setToValue] = useState(
		props.value && props.value.to && formatDateObject(props.value.to)
	);

	return (
		<div className="form-group">
			<ClayForm.Group className="form-group-sm">
				<label htmlFor={`from-${props.id}`}>
					{Liferay.Language.get('from')}
				</label>
				<input
					className="form-control"
					id={`from-${props.id}`}
					max={toValue || (props.max && formatDateObject(props.max))}
					min={props.min && formatDateObject(props.min)}
					onChange={e => setFromValue(e.target.value)}
					pattern="\d{4}-\d{2}-\d{2}"
					placeholder={props.placeholder || 'yyyy-mm-dd'}
					type="date"
					value={fromValue || ''}
				/>
			</ClayForm.Group>
			<ClayForm.Group className="form-group-sm mt-2">
				<label htmlFor={`to-${props.id}`}>
					{Liferay.Language.get('to')}
				</label>
				<input
					className="form-control"
					id={`to-${props.id}`}
					max={props.max && formatDateObject(props.max)}
					min={
						fromValue || (props.min && formatDateObject(props.min))
					}
					onChange={e => setToValue(e.target.value)}
					pattern="\d{4}-\d{2}-\d{2}"
					placeholder={props.placeholder || 'yyyy-mm-dd'}
					type="date"
					value={toValue || ''}
				/>
			</ClayForm.Group>

			<div className="mt-3">
				<ClayButton
					className="btn-sm"
					disabled={
						fromValue ===
							(props.value && props.value.from
								? formatDateObject(props.value.from)
								: '') &&
						toValue ===
							(props.value && props.value.to
								? formatDateObject(props.value.to)
								: '')
					}
					onClick={() => {
						if (!fromValue && !toValue) {
							props.actions.updateFilterValue(
								props.id,
								null,
								null
							);
						} else {
							const newValue = {
								from: fromValue
									? getDateFromDateString(fromValue)
									: null,
								to: toValue
									? getDateFromDateString(toValue)
									: null
							};
							props.actions.updateFilterValue(
								props.id,
								newValue,
								getOdataString(newValue, props.id)
							);
						}
					}}
				>
					{props.value
						? Liferay.Language.get('edit-filter')
						: Liferay.Language.get('add-filter')}
				</ClayButton>
			</div>
		</div>
	);
}

const dateShape = PropTypes.shape({
	day: PropTypes.number,
	month: PropTypes.number,
	year: PropTypes.number
});

DateRangeFilter.propTypes = {
	id: PropTypes.string.isRequired,
	invisible: PropTypes.bool,
	label: PropTypes.string.isRequired,
	max: dateShape,
	min: dateShape,
	placeholder: PropTypes.string,
	type: PropTypes.oneOf(['dateRange']).isRequired,
	value: PropTypes.shape({
		from: dateShape,
		to: dateShape
	})
};

export default DateRangeFilter;
