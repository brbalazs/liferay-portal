<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<liferay-util:dynamic-include key="com.liferay.asset.publisher.web#/view_content.jsp#pre" />

<%
String returnToFullPageURL = ParamUtil.getString(request, "returnToFullPageURL");

if (Validator.isNotNull(returnToFullPageURL)) {
	portletDisplay.setURLBack(returnToFullPageURL);
}

long assetEntryId = ParamUtil.getLong(request, "assetEntryId");
String type = ParamUtil.getString(request, "type");
long groupId = ParamUtil.getLong(request, "groupId", scopeGroupId);
String urlTitle = ParamUtil.getString(request, "urlTitle");

boolean print = Objects.equals(ParamUtil.getString(request, "viewMode"), Constants.PRINT);

AssetEntry assetEntry = null;

AssetRendererFactory<?> assetRendererFactory = AssetRendererFactoryRegistryUtil.getAssetRendererFactoryByType(type);
AssetRenderer<?> assetRenderer = null;

if (assetEntryId > 0) {
	assetEntry = assetRendererFactory.getAssetEntry(assetEntryId);

	assetRenderer = assetRendererFactory.getAssetRenderer(assetEntry.getClassPK());
}
else if (Validator.isNotNull(urlTitle)) {
	assetRenderer = assetRendererFactory.getAssetRenderer(groupId, urlTitle);

	assetEntry = assetRendererFactory.getAssetEntry(assetRendererFactory.getClassName(), assetRenderer.getClassPK());
}
%>

<c:choose>
	<c:when test="<%= (assetEntry.isVisible() && !assetPublisherDisplayContext.isEnablePermissions()) || (assetRenderer.hasViewPermission(permissionChecker) && assetRenderer.isDisplayable()) %>">

		<%
		String title = assetRenderer.getTitle(locale);

		request.setAttribute("view.jsp-showBackURL", Boolean.valueOf(!print));

		request.setAttribute("view.jsp-results", new ArrayList());

		request.setAttribute("view.jsp-assetEntryIndex", Integer.valueOf(0));

		request.setAttribute("view.jsp-assetEntry", assetEntry);
		request.setAttribute("view.jsp-assetRenderer", assetRenderer);
		request.setAttribute("view.jsp-assetRendererFactory", assetRendererFactory);

		request.setAttribute("view.jsp-title", title);

		request.setAttribute("view.jsp-print", Boolean.valueOf(print));
		request.setAttribute("view.jsp-viewInContext", assetPublisherDisplayContext.isAssetLinkBehaviorViewInPortlet());

		PortalUtil.addPortletBreadcrumbEntry(request, title, currentURL);
		%>

		<div>
			<liferay-util:include page="/display/full_content.jsp" servletContext="<%= application %>">
				<liferay-util:param name="languageId" value="<%= LanguageUtil.getLanguageId(request) %>" />
			</liferay-util:include>
		</div>

		<liferay-util:include page="/asset_html_metadata.jsp" servletContext="<%= application %>" />
	</c:when>
	<c:otherwise>

		<%
		if (!assetEntry.isVisible()) {
			SessionErrors.add(renderRequest, NoSuchModelException.class.getName());
		}
		else {
			SessionErrors.add(renderRequest, PrincipalException.MustHavePermission.class.getName());
		}
		%>

		<liferay-util:include page="/error.jsp" servletContext="<%= application %>" />
	</c:otherwise>
</c:choose>

<aui:script>
	Liferay.once(
		'allPortletsReady',
		function() {
			if (!Liferay.Browser.isIe()) {
				document.getElementById('p_p_id_<%= portletDisplay.getId() %>_').scrollIntoView();
			}
		}
	);
</aui:script>

<liferay-util:dynamic-include key="com.liferay.asset.publisher.web#/view_content.jsp#post" />