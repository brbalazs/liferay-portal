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

const DEFAULT_HEADERS = new Headers({
	Accept: 'application/json',
	'Content-Type': 'application/json',
	'x-csrf-token': Liferay.authToken
});

const BASE_OPTIONS = {
	credentials: 'include',
	headers: Liferay.staticEnvHeaders || DEFAULT_HEADERS,
	method: 'GET'
};

function doFetch(url, options = {}) {
	return fetch(url, {...BASE_OPTIONS, ...options})
		.catch(error => {
			throw new Error(error);
		})
		.then(response => {
			if (response.status === 204) {
				return Promise.resolve();
			}

			return response.json();
		});
}

const Headless = {
	DELETE(apiUrl, customOptions = {}) {
		const options = {
			method: 'DELETE',
			...customOptions
		};

		return doFetch(apiUrl, options);
	},

	GET(apiUrl, customOptions = {}) {
		return doFetch(apiUrl, customOptions);
	},

	PATCH(apiUrl, jsonProps = {}, customOptions = {}) {
		const options = {
			body: JSON.stringify(jsonProps),
			method: 'PATCH',
			...customOptions
		};

		return doFetch(apiUrl, options);
	},

	POST(apiUrl, json = {}, customOptions = {}) {
		const options = {
			body: JSON.stringify(json),
			method: 'POST',
			...customOptions
		};

		return doFetch(apiUrl, options);
	},

	PUT(apiUrl, json = {}, customOptions = {}) {
		const options = {
			body: JSON.stringify(json),
			method: 'PUT',
			...customOptions
		};

		return doFetch(apiUrl, options);
	}
};

export default Headless;
