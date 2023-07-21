<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
AssetEntry assetEntry = (AssetEntry)request.getAttribute("view.jsp-assetEntry");
AssetRenderer<?> assetRenderer = (AssetRenderer<?>)request.getAttribute("view.jsp-assetRenderer");

String title = (String)request.getAttribute("view.jsp-title");

if (Validator.isNull(title)) {
	title = assetRenderer.getTitle(locale);
}

if (Validator.isNull(request.getAttribute(WebKeys.PAGE_DESCRIPTION))) {
	String summary = StringUtil.shorten(assetRenderer.getSummary(liferayPortletRequest, liferayPortletResponse), assetPublisherDisplayContext.getAbstractLength());

	PortalUtil.setPageDescription(summary, request);
}

PortalUtil.setPageTitle(title, request);
PortalUtil.setPageKeywords(assetHelper.getAssetKeywords(assetEntry.getClassName(), assetEntry.getClassPK(), locale), request);
%>