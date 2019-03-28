<%--
/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */
--%>

<%@ include file="/init.jsp" %>

<%
CommerceDashboardDisplayContext commerceDashboardDisplayContext = (CommerceDashboardDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);
%>

<portlet:actionURL name="editCommerceDashboardPeriod" var="editCommerceDashboardPeriodURL" />

<aui:form action="<%= editCommerceDashboardPeriodURL %>" method="post" name="fm">
	<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />

	<div class="btn-group">

		<%
		for (int period : CommerceForecastEntryConstants.PERIODS) {
		%>

			<clay:button
				elementClasses='<%= (period == commerceDashboardDisplayContext.getPeriod()) ? "active" : StringPool.BLANK %>'
				label="<%= LanguageUtil.get(request, CommerceForecastEntryConstants.getPeriodLabel(period)) %>"
				name='<%= renderResponse.getNamespace() + "period" %>'
				style="secondary"
				type="submit"
				value="<%= String.valueOf(period) %>"
			/>

		<%
		}
		%>

	</div>
</aui:form>

<aui:script require="metal-dom/src/all/dom as dom">
	var fm = document.querySelector('#<portlet:namespace/>fm');

	var editCommerceDashboardPeriodHandler = dom.delegate(
		fm,
		'click',
		'button',
		function(event) {
			event.preventDefault();

			submitForm(document.hrefFm, fm.action + '&<portlet:namespace />period=' + event.delegateTarget.value);
		}
	);

	function removeListener() {
		editCommerceDashboardPeriodHandler.removeListener();

		Liferay.detach('destroyPortlet', removeListener);
	}

	Liferay.on('destroyPortlet', removeListener);
</aui:script>