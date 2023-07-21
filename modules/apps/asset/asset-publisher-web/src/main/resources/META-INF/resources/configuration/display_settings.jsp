<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
PortletURL configurationRenderURL = (PortletURL)request.getAttribute("configuration.jsp-configurationRenderURL");
%>

<div class="display-template">
	<liferay-ddm:template-selector
		className="<%= AssetEntry.class.getName() %>"
		defaultDisplayStyle="<%= assetPublisherDisplayContext.getDefaultDisplayStyle() %>"
		displayStyle="<%= assetPublisherDisplayContext.getDisplayStyle() %>"
		displayStyleGroupId="<%= assetPublisherDisplayContext.getDisplayStyleGroupId() %>"
		displayStyles="<%= Arrays.asList(assetPublisherDisplayContext.getDisplayStyles()) %>"
		label="display-template"
		refreshURL="<%= configurationRenderURL.toString() %>"
	/>
</div>

<aui:select cssClass="abstract-length hidden-field" helpMessage="abstract-length-key-help" name="preferences--abstractLength--" value="<%= assetPublisherDisplayContext.getAbstractLength() %>">
	<aui:option label="100" />
	<aui:option label="200" />
	<aui:option label="300" />
	<aui:option label="400" />
	<aui:option label="500" />
</aui:select>

<aui:input cssClass="hidden-field show-asset-title" name="preferences--showAssetTitle--" type="toggle-switch" value="<%= assetPublisherDisplayContext.isShowAssetTitle() %>" />

<aui:input cssClass="hidden-field show-context-link" name="preferences--showContextLink--" type="toggle-switch" value="<%= assetPublisherDisplayContext.isShowContextLink() %>" />

<aui:input cssClass="hidden-field show-extra-info" name="preferences--showExtraInfo--" type="toggle-switch" value="<%= assetPublisherDisplayContext.isShowExtraInfo() %>" />

<aui:select cssClass="asset-link-behavior" name="preferences--assetLinkBehavior--">
	<aui:option label="show-full-content" selected="<%= assetPublisherDisplayContext.isAssetLinkBehaviorShowFullContent() %>" value="showFullContent" />
	<aui:option label="view-in-context" selected="<%= assetPublisherDisplayContext.isAssetLinkBehaviorViewInPortlet() %>" value="viewInPortlet" />
</aui:select>

<aui:input helpMessage="number-of-items-to-display-help" label="number-of-items-to-display" name="preferences--delta--" type="text" value="<%= assetPublisherDisplayContext.getDelta() %>">
	<aui:validator name="digits" />
</aui:input>

<aui:select name="preferences--paginationType--">

	<%
	for (String paginationType : assetPublisherDisplayContext.PAGINATION_TYPES) {
	%>

		<aui:option label="<%= paginationType %>" selected="<%= assetPublisherDisplayContext.isPaginationTypeSelected(paginationType) %>" />

	<%
	}
	%>

</aui:select>

<c:if test="<%= !assetPublisherDisplayContext.isSearchWithIndex() %>">
	<c:if test="<%= assetPublisherDisplayContext.isSelectionStyleDynamic() %>">
		<aui:input label="exclude-assets-with-0-views" name="preferences--excludeZeroViewCount--" type="toggle-switch" value="<%= assetPublisherDisplayContext.isExcludeZeroViewCount() %>" />
	</c:if>
</c:if>

<aui:script sandbox="<%= true %>">
	var selectDisplayStyle = $('#<portlet:namespace />displayStyle');

	function showHiddenFields() {
		var hiddenFields = $('.hidden-field');

		hiddenFields.parentsUntil('.general-display-settings', '.checkbox, .form-group').addClass('hide');

		var displayStyle = selectDisplayStyle.val();

		if (displayStyle == 'full-content') {
			showParent('.show-asset-title');
			showParent('.show-context-link');
			showParent('.show-extra-info');
		}
		else if (displayStyle == 'abstracts') {
			showParent('.abstract-length');
		}
	}

	function showParent(child, parent) {
		$(child).parentsUntil('.general-display-settings', '.form-group').removeClass('hide');
	}

	showHiddenFields();

	selectDisplayStyle.on('change', showHiddenFields);
</aui:script>