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

import PropTypes from 'prop-types';
import React from 'react';

import CheckboxesFilter from '../management_bar/components/filters/Checkboxes.es';
import DateFilter from '../management_bar/components/filters/Date.es';
import DateRangeFilter from '../management_bar/components/filters/DateRange.es';
import NumberFilter from '../management_bar/components/filters/Number.es';
import RadioFilter from '../management_bar/components/filters/Radio.es';
import SelectFilter from '../management_bar/components/filters/Select.es';
import TextFilter from '../management_bar/components/filters/Text.es';

export const filterIdToComponentMap = {
	checkbox: CheckboxesFilter,
	date: DateFilter,
	dateRange: DateRangeFilter,
	number: NumberFilter,
	radio: RadioFilter,
	select: SelectFilter,
	text: TextFilter
};

export const renderFilter = (item, panelType) => {
	const Filter = filterIdToComponentMap[item.type];

	if (!Filter) {
		throw new Error(`Filter type '${item.type}' not found.`);
	}

	return <Filter {...item} panelType={panelType} />;
};

export function formatFilters(filters) {
	const mainFilter = filters.find(filter => filter.main);

	const formattedFilters = mainFilter
		? filters
		: filters.concat({
				id: 'keyword',
				main: true,
				placeholder: Liferay.Language.get('search-for'),
				value: ''
		  });

	return formattedFilters;
}

export const baseFilterProps = {
	id: PropTypes.string.isRequired,
	invisible: PropTypes.bool,
	label: PropTypes.string.isRequired,
	operator: PropTypes.oneOf([
		'eq',
		'ne',
		'gt',
		'ge',
		'lt',
		'le',
		'and',
		'or',
		'not',
		'startswith'
	]).isRequired
};
