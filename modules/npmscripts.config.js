/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

const COMMERCE_GLOBS = [
	'/apps/commerce/*/*.js',
	'/apps/commerce/*/{src,test}/**/*.{js,scss}',
	'/apps/commerce/*/{src}/**/*.{jsp,jspf}'
];

module.exports = {
	check: COMMERCE_GLOBS,
	fix: COMMERCE_GLOBS,
};
