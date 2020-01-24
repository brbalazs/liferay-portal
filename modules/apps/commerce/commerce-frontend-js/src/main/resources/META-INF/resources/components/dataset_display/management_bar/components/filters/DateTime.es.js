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
import ClayDatePicker from '@clayui/date-picker';
import React, {useState} from 'react';

import {prettifyDateTimeValue} from '../../../utilities/dates.es';
import getAppContext from '../Context.es';

const getDateTimeObj = value => {
	const date = value instanceof Date ? value : new Date(value);

	return {
		day: date.getDate(),
		hours: date.getHours(),
		minutes: date.getMinutes(),
		month: date.getMonth(),
		seconds: date.getSeconds(),
		year: date.getFullYear()
	};
};

function DateFilter(props) {
	const {actions} = getAppContext();

	const [value, setValue] = useState(props.value);
	const [formattedValue, setFormattedValue] = useState(
		prettifyDateTimeValue(props.value)
	);

	function updateDateTime(selectedDateTime) {
		const newValue = getDateTimeObj(selectedDateTime);

		setValue(newValue);
		setFormattedValue(prettifyDateTimeValue(newValue));
	}

	return (
		<>
			<ClayDatePicker
				onValueChange={updateDateTime}
				time
				value={formattedValue}
			/>
			<div className="mt-2">
				<ClayButton
					className="btn-sm"
					onClick={() => actions.updateFilterValue(props.id, value)}
				>
					{props.panelType === 'edit'
						? Liferay.Language.get('edit-filter')
						: Liferay.Language.get('add-filter')}
				</ClayButton>
			</div>
		</>
	);
}

export default DateFilter;
