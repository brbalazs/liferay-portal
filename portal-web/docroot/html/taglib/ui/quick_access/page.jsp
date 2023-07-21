<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/html/taglib/ui/quick_access/init.jsp" %>

<%
String randomNamespace = StringUtil.randomId() + StringPool.UNDERLINE;
%>

<c:if test="<%= ((quickAccessEntries != null) && !quickAccessEntries.isEmpty()) || Validator.isNotNull(contentId) %>">
	<nav class="quick-access-nav" id="<%= randomNamespace %>quickAccessNav">
		<h1 class="hide-accessible"><liferay-ui:message key="navigation" /></h1>

		<ul>
			<c:if test="<%= Validator.isNotNull(contentId) %>">
				<li><a href="<%= contentId %>"><liferay-ui:message key="skip-to-content" /></a></li>
			</c:if>

			<c:if test="<%= (quickAccessEntries != null) && !quickAccessEntries.isEmpty() %>">

				<%
				for (QuickAccessEntry quickAccessEntry : quickAccessEntries) {
				%>

					<li>
						<a href="<%= quickAccessEntry.getURL() %>" id="<%= randomNamespace + quickAccessEntry.getId() %>"><%= quickAccessEntry.getContent() %></a>
					</li>

				<%
				}
				%>

			</c:if>
		</ul>
	</nav>

	<c:if test="<%= (quickAccessEntries != null) && !quickAccessEntries.isEmpty() %>">
		<aui:script sandbox="<%= true %>">
			var callbacks = {};

			<%
			for (QuickAccessEntry quickAccessEntry : quickAccessEntries) {
				String onClick = quickAccessEntry.getOnClick();

				if (Validator.isNotNull(onClick)) {
			%>

					callbacks['<%= randomNamespace + quickAccessEntry.getId() %>'] = function() {
						<%= onClick %>
					};

			<%
				}
			}
			%>

			$('#<%= randomNamespace %>quickAccessNav').on(
				'click',
				'li a',
				function(event) {
					var callbackFn = callbacks[$(event.currentTarget).attr('id')];

					if (callbackFn instanceof Function) {
						callbackFn();
					}
				}
			);
		</aui:script>
	</c:if>
</c:if>