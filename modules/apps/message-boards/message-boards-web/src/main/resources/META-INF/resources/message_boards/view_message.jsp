<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/message_boards/init.jsp" %>

<%
MBMessageDisplay messageDisplay = (MBMessageDisplay)request.getAttribute(WebKeys.MESSAGE_BOARDS_MESSAGE_DISPLAY);

MBMessage message = messageDisplay.getMessage();

MBThread thread = messageDisplay.getThread();

if (layout.isTypeControlPanel()) {
	MBBreadcrumbUtil.addPortletBreadcrumbEntries(message, request, renderResponse);
}

AssetEntry layoutAssetEntry = AssetEntryLocalServiceUtil.getEntry(MBMessage.class.getName(), message.getMessageId());

request.setAttribute(WebKeys.LAYOUT_ASSET_ENTRY, layoutAssetEntry);

AssetEntryServiceUtil.incrementViewCounter(layoutAssetEntry);

boolean portletTitleBasedNavigation = GetterUtil.getBoolean(portletConfig.getInitParameter("portlet-title-based-navigation"));

MBBreadcrumbUtil.addPortletBreadcrumbEntries(message, request, renderResponse);
%>

<div <%= portletTitleBasedNavigation ? "class=\"container-fluid-1280\"" : StringPool.BLANK %>>
	<c:if test="<%= !portletTitleBasedNavigation %>">
		<liferay-util:include page="/message_boards/nav.jsp" servletContext="<%= application %>" />
	</c:if>

	<div <%= !portletTitleBasedNavigation ? "class=\"main-content-body\"" : StringPool.BLANK %>>
		<c:if test="<%= !portletTitleBasedNavigation %>">
			<liferay-ui:breadcrumb
				showCurrentGroup="<%= false %>"
				showGuestGroup="<%= false %>"
				showLayout="<%= false %>"
				showParentGroups="<%= false %>"
			/>
		</c:if>

		<liferay-util:include page="/message_boards/view_message_content.jsp" servletContext="<%= application %>" />
	</div>
</div>

<aui:script>
	function <portlet:namespace />addReplyToMessage(messageId, quote) {
		var addQuickReplyContainer = AUI.$(
			'#<portlet:namespace />addReplyToMessage' + messageId
		);

		addQuickReplyContainer.removeClass('hide');

		addQuickReplyContainer
			.find('#<portlet:namespace />parentMessageId')
			.val(messageId);

		addQuickReplyContainer.scrollTop();

		var editorName = '<portlet:namespace />replyMessageBody' + messageId;

		if (!window[editorName].instanceReady) {
			window[editorName].create();
		}

		window[editorName].setHTML(quote);
		window[editorName].focus();

		if (AUI().UA.mobile) {
			document
				.getElementById(
					'<portlet:namespace />addReplyToMessage' + messageId
				)
				.scrollIntoView(true);
		}

		Liferay.Util.toggleDisabled(
			'#<portlet:namespace />replyMessageButton' + messageId,
			true
		);
	}

	function <portlet:namespace />hideReplyMessage(messageId) {
		var addQuickReplyContainer = AUI.$('#<portlet:namespace />addReplyToMessage' + messageId);

		addQuickReplyContainer.addClass('hide');

		var editorName = '<portlet:namespace />replyMessageBody' + messageId;

		if (window[editorName]) {
			window[editorName].dispose();
		}

		var alloyEditorContainer = addQuickReplyContainer.find('.alloy-editor-container');

		alloyEditorContainer.find( '.alloy-editor-placeholder, .alloy-editor-icon, .yui3-widget, .yui3-aclist-aria' ).remove();
	}

	<c:if test="<%= thread.getRootMessageId() != message.getMessageId() %>">
		document
			.getElementById(
				'<portlet:namespace />message_' + <%= message.getMessageId() %>
			)
			.scrollIntoView(true);
	</c:if>
</aui:script>

<%
MBThreadFlagLocalServiceUtil.addThreadFlag(themeDisplay.getUserId(), thread, new ServiceContext());

PortalUtil.setPageSubtitle(message.getSubject(), request);
PortalUtil.setPageDescription(message.getSubject(), request);

List<AssetTag> assetTags = AssetTagLocalServiceUtil.getTags(MBMessage.class.getName(), message.getMessageId());

PortalUtil.setPageKeywords(ListUtil.toString(assetTags, AssetTag.NAME_ACCESSOR), request);
%>