<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
Contact selContact = (Contact)request.getAttribute("user.selContact");
%>

<c:choose>
	<c:when test="<%= selContact != null %>">
		<aui:model-context bean="<%= selContact %>" model="<%= Contact.class %>" />

		<aui:fieldset>
			<div class="instant-messenger">
				<aui:input label="jabber" name="jabberSn" />
			</div>

			<div class="instant-messenger">
				<aui:input label="skype" name="skypeSn" />

				<c:if test="<%= Validator.isNotNull(selContact.getSkypeSn()) %>">
					<a href="callto://<%= HtmlUtil.escapeAttribute(selContact.getSkypeSn()) %>"><img alt="<liferay-ui:message escapeAttribute="<%= true %>" key="call-this-user" />" src="http://mystatus.skype.com/smallicon/<%= HtmlUtil.escapeAttribute(selContact.getSkypeSn()) %>" /></a>
				</c:if>
			</div>
		</aui:fieldset>
	</c:when>
	<c:otherwise>
		<div class="alert alert-info"><liferay-ui:message key="this-section-will-be-editable-after-creating-the-user" /></div>
	</c:otherwise>
</c:choose>