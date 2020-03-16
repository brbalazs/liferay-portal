<%@ page import="com.liferay.portal.kernel.servlet.SessionMessages" %><%--
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
--%>

<%@ include file="/modal_content/init.jsp" %>

	</div>

	<c:if test="<%= Validator.isNotNull(submitButtonLabel) || showCancelButton || showSubmitButton %>">
		<div class="modal-iframe-footer">
			<c:if test="<%= showCancelButton %>">
				<button class="btn btn-secondary ml-3 modal-closer"><%= LanguageUtil.get(request, "cancel") %></button>
			</c:if>

			<c:if test="<%= showSubmitButton || Validator.isNotNull(submitButtonLabel) %>">
				<button class="btn btn-primary form-submitter ml-3">
					<%= Validator.isNotNull(submitButtonLabel) ? submitButtonLabel : LanguageUtil.get(request, "submit") %>
				</button>
			</c:if>
		</div>
	</c:if>
</div>

<aui:script require="commerce-frontend-js/utilities/eventsDefinitions.es as events, commerce-frontend-js/utilities/index.es as utilities">
	<c:if test='<%= SessionMessages.contains(renderRequest, "requestProcessed") %>'>
		window.parent.Liferay.fire(events.CLOSE_MODAL);
	</c:if>

	document.querySelectorAll('.modal-closer').forEach(function(trigger) {
		trigger.addEventListener('click', function(e) {
			e.preventDefault();
			window.parent.Liferay.fire(events.CLOSE_MODAL);
		});
	});

	var iframeContent = window.document.querySelector('.modal-iframe-content'),
		iframeFooter = window.document.querySelector('.modal-iframe-footer'),
		iframeForm = iframeContent.querySelector('form');

	var formSubmitterButton = document.querySelector('.form-submitter');

	function handleSubmit(event) {
		event.preventDefault();

		window.parent.Liferay.fire(events.IS_LOADING_MODAL, { isLoading: true });

		submitForm(iframeForm);
	}

	if (iframeForm) {
		window.addEventListener('submit', handleSubmit);
		formSubmitterButton.addEventListener('click', handleSubmit);
	}

	if (iframeContent && iframeFooter) {
		function adjustBottomSpace() {
			iframeContent.style.marginBottom = iframeFooter.offsetHeight + 'px';
		}

		var debouncedAdjustBottomSpace = utilities.debounce(adjustBottomSpace, 300);

		adjustBottomSpace();

		window.addEventListener('resize', debouncedAdjustBottomSpace);
	}
</aui:script>