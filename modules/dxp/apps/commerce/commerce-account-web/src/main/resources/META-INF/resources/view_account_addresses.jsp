<%--
/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */
--%>

<%@ include file="/init.jsp" %>

<%
CommerceAccountAddressesDisplayContext commerceAccountAddressesDisplayContext = (CommerceAccountAddressesDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);
%>

<portlet:actionURL name="editCommerceAddress" var="editCommerceAddressActionURL" />

<commerce-ui:table
	dataProviderKey="<%= CommerceAccountAddressClayTable.NAME %>"
	itemPerPage="<%= 5 %>"
	namespace="<%= renderResponse.getNamespace() %>"
	pageNumber="1"
	portletURL="<%= commerceAccountAddressesDisplayContext.getPortletURL() %>"
	tableName="<%= CommerceAccountAddressClayTable.NAME %>"
/>

<div class="minium-frame__cta is-visible">
	<aui:button cssClass="js-invite-user minium-button minium-button--big" onClick="<%= commerceAccountAddressesDisplayContext.getAddCommerceAddressHref() %>" value="add-address" />
</div>

<aui:script>
	function editCommerceAddress(title, uri) {
		Liferay.Util.openWindow(
			{
				dialog: {
					centered: true,
					destroyOnClose: true,
					height: 800,
					modal: true,
					width: 900
				},
				dialogIframe: {
					bodyCssClass: 'dialog-with-footer'
				},
				id: 'editCommerceAddressDialog',
				title: title,
				uri: uri
			}
		);
	}

	Liferay.provide(
		window,
		'<portlet:namespace />closePopup',
		function(dialogId) {
			var dialog = Liferay.Util.Window.getById(dialogId);

			dialog.destroy();
		},
		['liferay-util-window']
	);
</aui:script>