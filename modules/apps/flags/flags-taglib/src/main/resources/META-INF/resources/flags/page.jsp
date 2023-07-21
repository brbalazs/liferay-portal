<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/flags/init.jsp" %>

<%
String elementClasses = (String)request.getAttribute("liferay-flags:flags:elementClasses");
%>

<div class="taglib-flags <%= Validator.isNotNull(elementClasses) ? elementClasses : "" %>" id="<%= StringUtil.randomId() %>_id">
	<c:choose>
		<c:when test='<%= GetterUtil.getBoolean(request.getAttribute("liferay-flags:flags:onlyIcon")) %>'>
			<clay:button
				disabled="<%= true %>"
				elementClasses="btn-outline-borderless btn-outline-secondary lfr-portal-tooltip"
				icon="flag-empty"
				monospaced="<%= true %>"
				size="sm"
				style="secondary"
			/>
		</c:when>
		<c:otherwise>
			<clay:button
				disabled="<%= true %>"
				elementClasses="btn-outline-borderless btn-outline-secondary"
				icon="flag-empty"
				label='<%= (String)request.getAttribute("liferay-flags:flags:message") %>'
				size="sm"
				style="secondary"
			/>
		</c:otherwise>
	</c:choose>

	<react:component
		data='<%= (Map<String, Object>)request.getAttribute("liferay-flags:flags:data") %>'
		module="flags/js/index.es"
	/>
</div>