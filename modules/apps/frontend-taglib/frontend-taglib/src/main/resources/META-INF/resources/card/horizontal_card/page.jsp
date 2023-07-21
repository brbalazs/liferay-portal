<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/card/horizontal_card/init.jsp" %>

<c:choose>
	<c:when test="<%= (rowChecker != null) && (resultRow != null) %>">
		<liferay-util:buffer
			var="checkboxInput"
		>
			<%= rowChecker.getRowCheckBox(request, rowChecker.isChecked(resultRow.getObject()), rowChecker.isDisabled(resultRow.getObject()), resultRow.getPrimaryKey()) %>
		</liferay-util:buffer>

		<c:if test="<%= Validator.isNotNull(checkboxInput) %>">
			<div class="checkbox checkbox-card checkbox-middle-left">
				<label>
					<%= checkboxInput %>
		</c:if>
	</c:when>
	<c:when test="<%= showCheckbox %>">
		<div class="checkbox checkbox-card checkbox-middle-left">
			<label>
				<aui:input checked="<%= checkboxChecked %>" cssClass="<%= checkboxCSSClass %>" data="<%= checkboxData %>" disabled="<%= checkboxDisabled %>" id="<%= checkboxId %>" label="" name="<%= checkboxName %>" title='<%= LanguageUtil.format(request, "select-x", new Object[] {HtmlUtil.escape(text)}) %>' type="checkbox" useNamespace="<%= false %>" value="<%= checkboxValue %>" wrappedField="<%= true %>" />
	</c:when>
</c:choose>

<div class="card card-horizontal taglib-horizontal-card <%= Validator.isNotNull(cssClass) ? cssClass : StringPool.BLANK %>" <%= AUIUtil.buildData(data) %>>
	<div class="card-row card-row-padded <%= showCheckbox ? "selectable" : StringPool.BLANK %>">
		<c:if test="<%= Validator.isNotNull(colHTML) %>">
			<div class="card-col-field">
				<%= colHTML %>
			</div>
		</c:if>

		<div class="card-col-content card-col-gutters">
			<span class="lfr-card-title-text truncate-text">
				<aui:a data="<%= linkData %>" href="<%= url %>" title="<%= HtmlUtil.escapeAttribute(text) %>">
					<%= HtmlUtil.escape(text) %>
				</aui:a>
			</span>
		</div>

		<liferay-util:buffer
			var="actionJspBuffer"
		>
			<liferay-util:include page="<%= actionJsp %>" servletContext="<%= actionJspServletContext %>" />
		</liferay-util:buffer>

		<c:if test="<%= Validator.isNotNull(actionJspBuffer) %>">
			<div class="card-col-field lfr-card-actions-column">
				<%= actionJspBuffer %>
			</div>
		</c:if>
	</div>
</div>

<c:choose>
	<c:when test="<%= (rowChecker != null) && (resultRow != null) %>">
		<liferay-util:buffer
			var="checkboxInput"
		>
			<%= rowChecker.getRowCheckBox(request, rowChecker.isChecked(resultRow.getObject()), rowChecker.isDisabled(resultRow.getObject()), resultRow.getPrimaryKey()) %>
		</liferay-util:buffer>

		<c:if test="<%= Validator.isNotNull(checkboxInput) %>">
				</label>
			</div>
		</c:if>
	</c:when>
	<c:when test="<%= showCheckbox %>">
			</label>
		</div>
	</c:when>
</c:choose>