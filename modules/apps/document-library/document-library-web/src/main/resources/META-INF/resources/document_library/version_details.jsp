<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/document_library/init.jsp" %>

<%
boolean checkedOut = GetterUtil.getBoolean(request.getAttribute("edit_file_entry.jsp-checkedOut"));
%>

<div id="<portlet:namespace />versionDetails" style="display: none;">
	<aui:fieldset>
		<h5 class="control-label"><liferay-ui:message key="select-whether-this-is-a-major-or-minor-version" /></h5>

		<aui:input checked="<%= checkedOut %>" label="major-version" name="versionDetailsMajorVersion" type="radio" value="<%= true %>" />

		<aui:input checked="<%= !checkedOut %>" label="minor-version" name="versionDetailsMajorVersion" type="radio" value="<%= false %>" />

		<aui:input label="version-notes" maxLength="75" name="versionDetailsChangeLog" />
	</aui:fieldset>
</div>