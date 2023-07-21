<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
String redirect = request.getParameter("redirect");

redirect = PortalUtil.escapeRedirect(redirect);

Portlet selPortlet = PortletLocalServiceUtil.getPortletById(company.getCompanyId(), portletDisplay.getId());

String className = ParamUtil.getString(request, "className");
long classPK = ParamUtil.getLong(request, "classPK");

if (Validator.isNotNull(className) && (classPK > 0)) {
	assetPublisherWebUtil.addAndStoreSelection(liferayPortletRequest, className, classPK, -1);
}
%>

<aui:script>
	Liferay.fire(
		'closeWindow',
		{
			id: '<portlet:namespace />editAsset',
			portletAjaxable: <%= selPortlet.isAjaxable() %>,

			<c:choose>
				<c:when test="<%= redirect != null %>">
					redirect: '<%= HtmlUtil.escapeJS(redirect) %>'
				</c:when>
				<c:otherwise>
					refresh: '<%= portletDisplay.getId() %>'
				</c:otherwise>
			</c:choose>
		}
	);
</aui:script>