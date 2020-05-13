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

export default function debounce(func, wait, immediate) {
	let timeout;

	return () => {
		const context = this;
		const args = arguments;
		function later() {
			timeout = null;
			if (!immediate) func.apply(context, args);
		}
		const callNow = immediate && !timeout;

		clearTimeout(timeout);
		timeout = setTimeout(later, wait);
		if (callNow) func.apply(context, args);
	};
}

export function throttle(fn, limit) {
	let lastFunction, lastRan;

	return function() {
		const context = this,
			args = arguments;

		if (!lastRan) {
			fn.apply(context, args);

			lastRan = Date.now();
		} else {
			clearTimeout(lastFunction);

			lastFunction = setTimeout(() => {
				if (Date.now() - lastRan >= limit) {
					fn.apply(context, args);

					lastRan = Date.now();
				}
			}, limit - (Date.now() - lastRan));
		}
	};
}
