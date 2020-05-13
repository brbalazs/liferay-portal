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

window.themeDisplay = {
	getDefaultLanguageId: () => 'en_US',
	getLanguageId: () => 'it_IT'
};

window.Liferay = {
	Language: {
		get(v) {
			const charZero = v.charAt(0).toUpperCase(),
				rest = v
					.substring(1, v.length)
					.split('-')
					.join(' ');

			return `${charZero}${rest}`;
		}
	},
	ThemeDisplay: {
		getCanonicalURL: () => '/',
		getLanguageId: () => 'en_US',
		getPathThemeImages: () => '/assets'
	},
	ThemeDisplay: window.themeDisplay,
	component: () => {},
	detach: (name, fn) => {
		window.removeEventListener(name, fn);
	},
	fire: (name, payload) => {
		var e = document.createEvent('CustomEvent');
		e.initCustomEvent(name);
		if (payload) {
			Object.keys(payload).forEach(key => {
				e[key] = payload[key];
			});
		}
		window.dispatchEvent(e);
	},
	on: (name, fn) => {
		window.addEventListener(name, fn);
	},
	staticEnvHeaders: new Headers({
		Accept: 'application/json',
		Authorization: `Basic ${window.btoa('test@liferay.com:test')}`,
		'Content-Type': 'application/json'
	})
};
