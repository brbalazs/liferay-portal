/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

AUI.add(
	'liferay-calendar-a11y',
	function(A) {
		let template = A.CalendarBase.HEADER_TEMPLATE;

		template = template.replace('aria-role="heading"', '');
		template = template.replace('<div class="yui3-u', '<h1 class="yui3-u');
		template = template.replace(/div></, 'h1><');

		A.CalendarBase.HEADER_TEMPLATE = template;
	},
	'',
	{
		requires: ['calendar'],
	}
);