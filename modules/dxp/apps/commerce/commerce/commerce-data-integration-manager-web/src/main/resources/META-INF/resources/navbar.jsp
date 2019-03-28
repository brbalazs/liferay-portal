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
DataIntegrationAdminModuleRegistry dataIntegrationAdminModuleRegistry =
	(DataIntegrationAdminModuleRegistry)request.getAttribute(DataIntegrationWebPortletKeys.DATA_INTEGRATION_ADMIN_MODULE_REGISTRY);

NavigableMap<String, DataIntegrationAdminModule> dataIntegrationAdminModules = dataIntegrationAdminModuleRegistry.getDataIntegrationAdminModules(scopeGroupId);

String selectedDataIntegrationAdminModuleKey = ParamUtil.getString(request, "dataIntegrationAdminModuleKey", dataIntegrationAdminModules.firstKey());

List<NavigationItem> navigationItems = new ArrayList<>();

for (Map.Entry<String, DataIntegrationAdminModule> entry : dataIntegrationAdminModules.entrySet()) {
	String dataIntegrationAdminModuleKey = entry.getKey();
	DataIntegrationAdminModule dataIntegrationAdminModule = entry.getValue();

	PortletURL dataIntegrationAdminModuleURL = renderResponse.createRenderURL();

	dataIntegrationAdminModuleURL.setParameter("dataIntegrationAdminModuleKey", dataIntegrationAdminModuleKey);

	NavigationItem navigationItem = new NavigationItem();

	navigationItem.setActive(dataIntegrationAdminModuleKey.equals(selectedDataIntegrationAdminModuleKey));
	navigationItem.setHref(dataIntegrationAdminModuleURL.toString());
	navigationItem.setLabel(dataIntegrationAdminModule.getLabel(locale));

	navigationItems.add(navigationItem);
}
%>

<clay:navigation-bar
	inverted="<%= true %>"
	navigationItems="<%= navigationItems %>"
/>