<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://liferay.com/tld/frontend" prefix="liferay-frontend" %><%@
taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>

<%@ page import="com.liferay.journal.model.JournalArticle" %><%@
page import="com.liferay.journal.model.JournalArticleDisplay" %><%@
page import="com.liferay.portal.kernel.language.LanguageUtil" %><%@
page import="com.liferay.portal.kernel.util.ArrayUtil" %><%@
page import="com.liferay.portal.kernel.util.Constants" %><%@
page import="com.liferay.portal.kernel.util.LocaleUtil" %><%@
page import="com.liferay.portal.kernel.util.ParamUtil" %><%@
page import="com.liferay.portal.kernel.util.WebKeys" %>

<liferay-frontend:defineObjects />

<%
JournalArticle article = (JournalArticle)request.getAttribute(WebKeys.JOURNAL_ARTICLE);

JournalArticleDisplay articleDisplay = (JournalArticleDisplay)request.getAttribute(WebKeys.JOURNAL_ARTICLE_DISPLAY);

String viewMode = ParamUtil.getString(request, "viewMode");
%>

<c:if test="<%= !viewMode.equals(Constants.PRINT) %>">

	<%
	String languageId = LanguageUtil.getLanguageId(request);

	String[] availableLocales = articleDisplay.getAvailableLocales();

	if (ArrayUtil.isNotEmpty(availableLocales) && !ArrayUtil.contains(availableLocales, languageId)) {
		if (article != null) {
			languageId = article.getDefaultLanguageId();
		}
		else {
			languageId = LocaleUtil.toLanguageId(LocaleUtil.getSiteDefault());

			if (!ArrayUtil.contains(availableLocales, languageId)) {
				languageId = availableLocales[0];
			}
		}
	}
	%>

	<c:if test="<%= availableLocales.length > 1 %>">
		<div class="locale-actions user-tool-asset-addon-entry">
			<liferay-ui:language
				formAction="<%= currentURL %>"
				languageId="<%= languageId %>"
				languageIds="<%= availableLocales %>"
			/>
		</div>
	</c:if>
</c:if>