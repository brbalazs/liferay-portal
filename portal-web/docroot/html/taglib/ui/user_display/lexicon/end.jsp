<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page import="com.liferay.portal.kernel.util.GetterUtil" %>

<%
boolean showUserDetails = GetterUtil.getBoolean((String)request.getAttribute("liferay-ui:user-display:showUserDetails"));
boolean showUserName = GetterUtil.getBoolean((String)request.getAttribute("liferay-ui:user-display:showUserName"));
%>

<c:if test="<%= showUserDetails || showUserName %>">
			<c:if test="<%= showUserDetails %>">
				</div>
			</c:if>
		</div>
	</div>
</c:if>