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

import createOdataFilter from './odata';

export function getSchemaString(object, path) {
	if (!Array.isArray(path)) {
		return object[path];
	} else {
		return path.reduce((acc, path) => acc[path], object);
	}
}

export function liferayNavigate(url) {
	if (Liferay.SPA) {
		Liferay.SPA.app.navigate(url);
	} else {
		window.location.href = url;
	}
}

export function getValueFromItem(item, fieldName) {
	if (Array.isArray(fieldName)) {
		return fieldName.reduce((acc, key) => acc[key], item);
	}
	return item[fieldName];
}

export function executeAsyncAction(url, method = 'GET') {
	return fetch(url, {
		...fetchParams,
		method
	});
}

export function formatActionUrl(url, item) {
	const regex = new RegExp('{(.*?)}', 'mg');

	return url.replace(regex, matched =>
		getValueFromItem(
			item,
			matched.substring(1, matched.length - 1).split('|')
		)
	);
}

export function getRandomId() {
	return Math.random()
		.toString(36)
		.substr(2, 9);
}

export const fetchHeaders = new Headers({
	'x-csrf-token': Liferay.authToken
});

export const fetchParams = {
	credentials: 'include',
	headers: Liferay.staticEnvHeaders || fetchHeaders
};

export function createSortingString(values) {
	if (!values.length) return null;

	return values
		.map(value => {
			return `${
				Array.isArray(value.fieldName)
					? value.fieldName[0]
					: value.fieldName
			}:${value.direction}`;
		})
		.join(',');
}

export function loadData(
	apiUrl,
	currentUrl,
	filters = [],
	searchParam,
	delta,
	page = 1,
	sorting = []
) {
	const params = new URLSearchParams();

	params.set('p_auth', window.Liferay.authToken);
	params.set('pageSize', delta);
	params.set('page', page);

	if (currentUrl) {
		params.set('currentUrl', encodeURIComponent(currentUrl));
	}

	if (searchParam) {
		params.set('search', encodeURIComponent(searchParam));
	}

	if (sorting && sorting.length) {
		params.set('sort', createSortingString(sorting));
	}

	if (filters && filters.length) {
		params.set('sort', createOdataFilter(filters));
	}

	const url = `${apiUrl}${
		apiUrl.indexOf('?') > -1 ? '&' : '?'
	}${params.toString()}`;

	return executeAsyncAction(url, 'GET').then(response => response.json());
}
