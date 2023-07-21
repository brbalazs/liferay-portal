<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<liferay-ui:error-marker
	key="<%= WebKeys.ERROR_SECTION %>"
	value="maps"
/>

<h3><liferay-ui:message key="maps" /></h3>

<liferay-map:map-provider-selector
	configurationPrefix="settings"
	mapProviderKey="<%= (String)request.getAttribute(MapProviderWebKeys.MAP_PROVIDER_KEY) %>"
	name='<%= "settings--" + MapProviderWebKeys.MAP_PROVIDER_KEY + "--" %>'
/>