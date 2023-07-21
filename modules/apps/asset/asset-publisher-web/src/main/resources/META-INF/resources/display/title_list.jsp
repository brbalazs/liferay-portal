<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
List results = (List)request.getAttribute("view.jsp-results");

int assetEntryIndex = ((Integer)request.getAttribute("view.jsp-assetEntryIndex")).intValue();

AssetEntry assetEntry = (AssetEntry)request.getAttribute("view.jsp-assetEntry");
AssetRendererFactory<?> assetRendererFactory = (AssetRendererFactory<?>)request.getAttribute("view.jsp-assetRendererFactory");
AssetRenderer<?> assetRenderer = (AssetRenderer<?>)request.getAttribute("view.jsp-assetRenderer");

String title = (String)request.getAttribute("view.jsp-title");

if (Validator.isNull(title)) {
	title = assetRenderer.getTitle(locale);
}

request.setAttribute("view.jsp-showIconLabel", false);

boolean viewInContext = ((Boolean)request.getAttribute("view.jsp-viewInContext")).booleanValue();
%>

<c:if test="<%= assetEntryIndex == 0 %>">
	<ul class="list-unstyled">
</c:if>

<li class="h3 <%= assetRendererFactory.getType() %>">
	<span class="asset-anchor lfr-asset-anchor" id="<%= assetEntry.getEntryId() %>"></span>

	<aui:a href='<%= HttpUtil.addParameter(assetPublisherHelper.getAssetViewURL(liferayPortletRequest, liferayPortletResponse, assetRenderer, assetEntry, viewInContext), liferayPortletResponse.getNamespace() + "viewSingleAsset", true) %>'>
		<%= HtmlUtil.escape(title) %>
	</aui:a>

	<liferay-util:include page="/asset_actions.jsp" servletContext="<%= application %>" />

	<liferay-asset:asset-metadata
		className="<%= assetEntry.getClassName() %>"
		classPK="<%= assetEntry.getClassPK() %>"
		filterByMetadata="<%= true %>"
		metadataFields="<%= assetPublisherDisplayContext.getMetadataFields() %>"
	/>
</li>

<c:if test="<%= (assetEntryIndex + 1) == results.size() %>">
	</ul>
</c:if>