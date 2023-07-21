<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ page contentType="text/javascript; charset=UTF-8" %>

<%@ page import="com.liferay.portal.kernel.language.LanguageUtil" %><%@
page import="com.liferay.portal.kernel.util.LocaleUtil" %>

<%@ page import="java.util.Locale" %>

<%
Locale locale = LocaleUtil.fromLanguageId(LanguageUtil.getLanguageId(request));
%>

AUI.add(
	'portal-available-languages',
	function(A) {
		var available = {};

		var direction = {};

		<%
		for (Locale curLocale : LanguageUtil.getAvailableLocales()) {
			String selLanguageId = LocaleUtil.toLanguageId(curLocale);
		%>

			available['<%= selLanguageId %>'] = '<%= curLocale.getDisplayName(locale) %>';
			direction['<%= selLanguageId %>'] = '<%= LanguageUtil.get(curLocale, "lang.dir") %>';

		<%
		}
		%>

		Liferay.Language.available = available;
		Liferay.Language.direction = direction;
	},
	'',
	{
		requires: ['liferay-language']
	}
);