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
CommerceOrganizationDisplayContext commerceOrganizationDisplayContext = (CommerceOrganizationDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

String viewMode = commerceOrganizationDisplayContext.getViewMode();

request.setAttribute("view.jsp-filterPerOrganization", false);
%>

<div class="commerce-organization-view-modes row text-right">
	<%
	for (String curViewMode : CommerceOrganizationConstants.VIEW_MODES) {
		String icon = "table2";

		if (curViewMode.equals(CommerceOrganizationConstants.CHART_VIEW_MODE)) {
			icon = "organizations";
		}

		String cssClass = "btn btn-default lfr-portal-tooltip";

		if (curViewMode.equals(viewMode)) {
			cssClass = "active " + cssClass;
		}

		Map<String, Object> data = new HashMap<>();

		data.put("title", LanguageUtil.get(request, curViewMode));

		PortletURL portletURL = commerceOrganizationDisplayContext.getPortletURL();

		portletURL.setParameter("viewMode", curViewMode);
	%>

		<aui:a cssClass="<%= cssClass %>" data="<%= data %>" href="<%= portletURL.toString() %>" id="<%= renderResponse.getNamespace() + curViewMode %>">
			<c:if test="<%= Validator.isNotNull(icon) %>">
				<aui:icon cssClass="icon-monospaced" image="<%= icon %>" markupView="lexicon" />
			</c:if>

			<span class="sr-only"><liferay-ui:message key="<%= curViewMode %>" /></span>
		</aui:a>

	<%
	}
	%>
</div>

<c:choose>
	<c:when test="<%= viewMode.equals(CommerceOrganizationConstants.LIST_VIEW_MODE) %>">
		<div class="commerce-organization-container container container-fluid" id="<portlet:namespace />entriesContainer">
			<commerce-ui:table
				dataProviderKey="<%= CommerceOrganizationClayTable.NAME %>"
				filter="<%= commerceOrganizationDisplayContext.getOrganizationFilter() %>"
				itemPerPage="<%= 5 %>"
				namespace="<%= renderResponse.getNamespace() %>"
				pageNumber="1"
				portletURL="<%= commerceOrganizationDisplayContext.getPortletURL() %>"
				tableName="<%= CommerceOrganizationClayTable.NAME %>"
			/>
		</div>
	</c:when>
	<c:when test="<%= viewMode.equals(CommerceOrganizationConstants.CHART_VIEW_MODE) %>">

		<%
		String segmentEditRootElementId = renderResponse.getNamespace() + "-org-chart-root";
		%>

		<div class="orgchart-module" id="<%= segmentEditRootElementId %>">
			<div class="inline-item my-5 p-5 w-100">
				<span aria-hidden="true" class="loading-animation"></span>
			</div>
		</div>

		<aui:script require="commerce-organization-web@1.0.0/js/index.es as OrgChart">

			OrgChart.default(
			'<%= segmentEditRootElementId %>',
			'<%= segmentEditRootElementId %>',
			{
			assetsPath: '<%= PortalUtil.getPathContext(request) + "/assets" %>',
			namespace: '<portlet:namespace/>',
			spritemap: '<%= themeDisplay.getPathThemeImages() + "/commerce-icons.svg" %>',
			imagesPath: '<%= themeDisplay.getPathThemeImages() %>',
			apiURL : '<%= PortalUtil.getPortalURL(request) + "/o/commerce-organization" %>'
			});
		</aui:script>
	</c:when>
</c:choose>

<c:if test="<%= commerceOrganizationDisplayContext.hasAddOrganizationPermissions() %>">
	<div class="commerce-cta is-visible">
		<aui:button cssClass="commerce-button commerce-button--big" name="addOrganizationButton" value="add-organization" />
	</div>

	<portlet:actionURL name="editCommerceOrganization" var="editCommerceOrganizationActionURL">
		<portlet:param name="<%= Constants.CMD %>" value="<%= Constants.ADD %>" />
		<portlet:param name="redirect" value="<%= currentURL %>" />
		<portlet:param name="organizationId" value="<%= String.valueOf(commerceOrganizationDisplayContext.getOrganizationId()) %>" />
	</portlet:actionURL>

	<aui:form action="<%= editCommerceOrganizationActionURL %>" method="post" name="organizationFm">
		<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.DELETE %>" />
		<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />
		<aui:input name="organizationId" type="hidden" />
	</aui:form>

	<aui:script require="metal-dom/src/all/dom as dom,frontend-js-web/liferay/modal/commands/OpenSimpleInputModal.es as modalCommands">
		function handleAddOrganizationButtonClick(event) {
			event.preventDefault();

			modalCommands.openSimpleInputModal(
				{
					dialogTitle: '<liferay-ui:message key="add-organization" />',
					formSubmitURL: '<%= editCommerceOrganizationActionURL %>',
					mainFieldLabel: '<liferay-ui:message key="name" />',
					mainFieldName: 'name',
					mainFieldPlaceholder: '<liferay-ui:message key="name" />',
					namespace: '<portlet:namespace />',
					spritemap: '<%= themeDisplay.getPathThemeImages() %>/lexicon/icons.svg'
				}
			);
		}

		function handleDestroyPortlet () {
			addOrganizationButton.removeEventListener('click', handleAddOrganizationButtonClick);

			Liferay.detach('destroyPortlet', handleDestroyPortlet);
		}

		var addOrganizationButton = document.getElementById('<portlet:namespace />addOrganizationButton');

		addOrganizationButton.addEventListener('click', handleAddOrganizationButtonClick);

		Liferay.on('destroyPortlet', handleDestroyPortlet);
	</aui:script>

	<aui:script>
		Liferay.provide(
			window,
			'deleteCommerceOrganization',
			function(id) {
				document.querySelector('#<portlet:namespace />organizationId').value = id;

				submitForm(document.<portlet:namespace />organizationFm);
			}
		);
	</aui:script>
</c:if>
