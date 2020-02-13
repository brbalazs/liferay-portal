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

import {OPEN_MODAL} from './eventsDefinitions.es';

export const iframeHandlerModalId = 'iframe-handler-modal';

export function initializeIframeListeners() {
	Liferay.on(OPEN_MODAL, payload => {
		if (!window.parent) {
			return;
		}

		window.parent.Liferay.fire(OPEN_MODAL, {
			id: iframeHandlerModalId,
			onClose() {
				window.location.reload();
			},
			...payload
		});
	});
}

export function isPageInIframe() {
	return window.location !== window.parent.location
}
