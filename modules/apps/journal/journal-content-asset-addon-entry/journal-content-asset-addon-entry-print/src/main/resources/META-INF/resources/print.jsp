<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %><%@
taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>

<%@ page import="com.liferay.journal.model.JournalArticleDisplay" %><%@
page import="com.liferay.portal.kernel.language.LanguageUtil" %><%@
page import="com.liferay.portal.kernel.portlet.LiferayWindowState" %><%@
page import="com.liferay.portal.kernel.util.Constants" %><%@
page import="com.liferay.portal.kernel.util.HtmlUtil" %><%@
page import="com.liferay.portal.kernel.util.ParamUtil" %><%@
page import="com.liferay.portal.kernel.util.WebKeys" %>

<portlet:defineObjects />

<%
JournalArticleDisplay articleDisplay = (JournalArticleDisplay)request.getAttribute(WebKeys.JOURNAL_ARTICLE_DISPLAY);

String viewMode = ParamUtil.getString(request, "viewMode");
%>

<c:choose>
	<c:when test="<%= viewMode.equals(Constants.PRINT) %>">
		<aui:script>
			print();
		</aui:script>
	</c:when>
	<c:otherwise>
		<portlet:renderURL var="printPageURL" windowState="<%= LiferayWindowState.POP_UP.toString() %>">
			<portlet:param name="groupId" value="<%= String.valueOf(articleDisplay.getGroupId()) %>" />
			<portlet:param name="articleId" value="<%= articleDisplay.getArticleId() %>" />
			<portlet:param name="page" value="<%= String.valueOf(articleDisplay.getCurrentPage()) %>" />
			<portlet:param name="viewMode" value="<%= Constants.PRINT %>" />
		</portlet:renderURL>

		<div class="print-action user-tool-asset-addon-entry">
			<liferay-ui:icon
				icon="print"
				label="<%= true %>"
				markupView="lexicon"
				message='<%= LanguageUtil.format(request, "print-x-x", new Object[] {"hide-accessible", HtmlUtil.escape(articleDisplay.getTitle())}, false) %>'
				url='<%= "javascript:" + renderResponse.getNamespace() + "printPage();" %>'
			/>
		</div>

		<aui:script>
			function <portlet:namespace />printPage() {
				window.open('<%= printPageURL %>', '', 'directories=0,height=480,left=80,location=1,menubar=1,resizable=1,scrollbars=yes,status=0,toolbar=0,top=180,width=640');
			}
		</aui:script>
	</c:otherwise>
</c:choose>