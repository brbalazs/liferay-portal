<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ taglib uri="http://liferay.com/tld/asset" prefix="liferay-asset" %>

<%@ page import="com.liferay.journal.model.JournalArticle" %><%@
page import="com.liferay.journal.model.JournalArticleDisplay" %><%@
page import="com.liferay.portal.kernel.util.WebKeys" %>

<%
JournalArticleDisplay articleDisplay = (JournalArticleDisplay)request.getAttribute(WebKeys.JOURNAL_ARTICLE_DISPLAY);
%>

<div class="content-metadata-asset-addon-entry content-metadata-asset-addon-entry-links">
	<liferay-asset:asset-links
		className="<%= JournalArticle.class.getName() %>"
		classPK="<%= articleDisplay.getResourcePrimKey() %>"
	/>
</div>