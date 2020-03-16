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

import ClayLoadingIndicator from '@clayui/loading-indicator';
import ClayModal, {useModal} from '@clayui/modal';
import PropTypes from 'prop-types';
import React, {useState, useRef, useEffect} from 'react';
import { liferayNavigate } from '../../utilities/index.es';

import {
	CLOSE_MODAL,
	IS_LOADING_MODAL,
	OPEN_MODAL
} from '../../utilities/eventsDefinitions.es';

import {isPageInIframe} from '../../utilities/iframes.es';

function Modal(props) {
	const [visible, setVisible] = useState(false);
	const [loading, setLoading] = useState(false);
	const [onClose, setOnClose] = useState(null);
	const [iframeLoadingCounter, setIframeLoadingCounter] = useState(0);
	const [title, setTitle] = useState(props.title);
	const [url, setUrl] = useState(props.url);
	const iframeRef = useRef(null);

	function cleanUpModal() {
		setIframeLoadingCounter(() => 0);
		setLoading(false);
		setVisible(false);
	}

	function doClose(successNotification) {
		if (onClose) {
			onClose(successNotification);
		} else if (props.onClose) {
			props.onClose(successNotification);
		}

		cleanUpModal();
	}

	const {observer, onClose: closeOnIframeRefresh} = useModal({
		onClose: () => {
			if (iframeLoadingCounter > 1) {
				doClose();
			} else {
				cleanUpModal();
			}
		}
	});

	useEffect(() => {
		function handleOpenEvent(data) {
			if (props.id !== data.id || visible || isPageInIframe()) {
				return;
			}

			setLoading(true);
			setVisible(true);

			if (data.url) {
				setUrl(data.url);
			}

			if (data.onClose) {
				setOnClose(() => data.onClose);
			}

			if (data.title) {
				setTitle(data.title);
			}
		}

		function handleCloseModal({
			redirectURL = '',
			successNotification = {},
			willIframeRefresh = true
		}) {

			if (redirectURL) {
				liferayNavigate(redirectURL);
			}

			if (willIframeRefresh) {
				closeOnIframeRefresh(successNotification);
			} else {
				doClose(successNotification);
			}
		}

		function handleSetLoading(data) {
			const { isLoading } = data;

			setLoading(isLoading || false);
		}

		function cleanUpListeners(e) {
			if (e.portletId === props.portletId) {
				Liferay.detach(OPEN_MODAL, handleOpenEvent);
				Liferay.detach(CLOSE_MODAL, handleCloseModal);
				Liferay.detach(IS_LOADING_MODAL, handleSetLoading);
				Liferay.detach('destroyPortlet', cleanUpListeners);
			}
		}

		if (Liferay.on) {
			Liferay.on(OPEN_MODAL, handleOpenEvent);
			Liferay.on(CLOSE_MODAL, handleCloseModal);
			Liferay.on(IS_LOADING_MODAL, handleSetLoading);
			Liferay.on('destroyPortlet', cleanUpListeners);
		}

		return () => cleanUpListeners({portletId: props.portletId});
	}, [props.id, props.portletId, closeOnIframeRefresh, visible]);

	useEffect(() => {
		setOnClose(() => props.onClose);
	}, [props.onClose]);

	function handleIframeLoad() {
		setLoading(false);
		setIframeLoadingCounter(c => c + 1);

		const iframeDocument = iframeRef.current.contentDocument;
		const iframeWindow = iframeRef.current.contentWindow;

		if (iframeDocument && iframeWindow) {
			if (iframeWindow.Liferay && iframeWindow.Liferay.on) {
				iframeWindow.Liferay.on('endNavigate', e => {
					e.preventDefault();
					setIframeLoadingCounter(c => c + 1);
				});
			}
		}
	}

	return visible ? (
		<ClayModal
			observer={observer}
			size="lg"
			spritemap={props.spritemap}
			status={props.status}
		>
			{title && <ClayModal.Header>{title}</ClayModal.Header>}
			<div
				className="modal-body modal-body-iframe"
				style={{height: '450px', maxHeight: '100%'}}
			>
				<iframe
					onLoad={handleIframeLoad}
					ref={iframeRef}
					src={url}
					title={title}
				/>
				{loading && (
					<div className="loader-container">
						<ClayLoadingIndicator />
					</div>
				)}
			</div>
		</ClayModal>
	) : null;
}

Modal.propTypes = {
	closeOnSubmit: PropTypes.bool,
	id: PropTypes.string.isRequired,
	onClose: PropTypes.func,
	portletId: PropTypes.string,
	size: PropTypes.string,
	spritemap: PropTypes.string,
	status: PropTypes.string,
	title: PropTypes.string,
	url: PropTypes.string
};

export default Modal;
