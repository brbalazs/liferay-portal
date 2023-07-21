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
			<div class="social-network">
				<aui:input label="facebook" name="facebookSn" />

				<i class="icon-facebook-sign"></i>
			</div>

			<div class="social-network">
				<aui:input label="twitter" name="twitterSn" />

				<i class="icon-twitter-sign"></i>
			</div>
		</aui:fieldset>
	</c:when>
	<c:otherwise>
		<div class="alert alert-info">
			<liferay-ui:message key="this-section-will-be-editable-after-creating-the-user" />
		</div>
	</c:otherwise>
</c:choose>