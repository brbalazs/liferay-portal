/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

const FIELD_BLACKLIST = ['formDate', 'p_auth'];

export function isFormElement(form) {
	return form instanceof HTMLFormElement;
}

function isBlacklistedField(field) {
	return FIELD_BLACKLIST.includes(field);
}

export function toJSON(formData) {
	const json = {};

	// eslint-disable-next-line no-for-of-loops/no-for-of-loops, no-unused-vars
	for (const [key, value] of formData.entries()) {
		if (!isBlacklistedField(key)) {
			if (!(key in json)) {
				json[key] = value;
				continue;
			}

			if (!Array.isArray(json[key])) {
				json[key] = [json[key]];
			}

			json[key].push(value);
		}
	}

	return json;
}
