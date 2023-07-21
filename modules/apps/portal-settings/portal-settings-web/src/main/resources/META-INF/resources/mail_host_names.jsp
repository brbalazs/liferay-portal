<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<h3><liferay-ui:message key="mail-host-names" /></h3>

<aui:fieldset>
	<aui:input label='<%= LanguageUtil.format(request, "enter-one-mail-host-name-per-line-for-all-additional-mail-host-names-besides-x", company.getMx(), false) %>' name='<%= "settings--" + PropsKeys.ADMIN_MAIL_HOST_NAMES + "--" %>' type="textarea" value="<%= PrefsPropsUtil.getString(company.getCompanyId(), PropsKeys.ADMIN_MAIL_HOST_NAMES) %>" />
</aui:fieldset>