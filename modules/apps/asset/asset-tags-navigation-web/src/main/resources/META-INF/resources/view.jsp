<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
List<AssetTag> assetTags = null;

if (showAssetCount && (classNameId > 0)) {
	assetTags = AssetTagServiceUtil.getTags(scopeGroupId, classNameId, null, 0, maxAssetTags, new AssetTagCountComparator());
}
else {
	assetTags = AssetTagServiceUtil.getGroupTags(scopeGroupId, 0, maxAssetTags, new AssetTagCountComparator());
}

assetTags = ListUtil.sort(assetTags);

Map<String, Object> contextObjects = new HashMap<String, Object>();

contextObjects.put("scopeGroupId", Long.valueOf(scopeGroupId));
%>

<liferay-ddm:template-renderer
	className="<%= AssetTag.class.getName() %>"
	contextObjects="<%= contextObjects %>"
	displayStyle="<%= displayStyle %>"
	displayStyleGroupId="<%= displayStyleGroupId %>"
	entries="<%= assetTags %>"
>
	<liferay-asset:asset-tags-navigation
		classNameId="<%= classNameId %>"
		displayStyle="<%= displayStyle %>"
		hidePortletWhenEmpty="<%= true %>"
		maxAssetTags="<%= maxAssetTags %>"
		showAssetCount="<%= showAssetCount %>"
		showZeroAssetCount="<%= showZeroAssetCount %>"
	/>
</liferay-ddm:template-renderer>