<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>

<%@ page contentType="text/html; charset=UTF-8" %>

<%@ page import="com.liferay.portal.kernel.util.GetterUtil" %>

</ul>
	<c:if test='<%= GetterUtil.getBoolean(request.getAttribute("liferay-ui:icon-menu:scroll")) %>'>
		</div>
	</c:if>
</div>

<aui:script use="liferay-menu">
	Liferay.Menu.handleFocus('#<%= GetterUtil.getString((String)request.getAttribute("liferay-ui:icon-menu:id")) %>menu');
</aui:script>