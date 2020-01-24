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
