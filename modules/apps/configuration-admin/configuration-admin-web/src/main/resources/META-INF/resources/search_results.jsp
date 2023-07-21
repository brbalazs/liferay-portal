<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
String redirect = PortalUtil.escapeRedirect(renderRequest.getParameter("redirect"));

ConfigurationEntryRetriever configurationEntryRetriever = (ConfigurationEntryRetriever)request.getAttribute(ConfigurationAdminWebKeys.CONFIGURATION_ENTRY_RETRIEVER);
ConfigurationModelIterator configurationModelIterator = (ConfigurationModelIterator)request.getAttribute(ConfigurationAdminWebKeys.CONFIGURATION_MODEL_ITERATOR);
ResourceBundleLoaderProvider resourceBundleLoaderProvider = (ResourceBundleLoaderProvider)request.getAttribute(ConfigurationAdminWebKeys.RESOURCE_BUNDLE_LOADER_PROVIDER);

if (Validator.isNull(redirect)) {
	redirect = String.valueOf(renderResponse.createRenderURL());
}

PortletURL searchURL = renderResponse.createRenderURL();

searchURL.setParameter("mvcRenderCommandName", "/search");
searchURL.setParameter("redirect", redirect);

portletDisplay.setShowBackIcon(true);
portletDisplay.setURLBack(redirect);

renderResponse.setTitle(LanguageUtil.get(request, "search-results"));
%>

<clay:management-toolbar
	clearResultsURL="<%= redirect %>"
	itemsTotal="<%= configurationModelIterator.getTotal() %>"
	searchActionURL="<%= searchURL.toString() %>"
	selectable="<%= false %>"
	showSearch="<%= true %>"
/>

<div class="container-fluid container-fluid-max-xl container-view">
	<liferay-ui:search-container
		emptyResultsMessage="no-configurations-were-found"
		iteratorURL="<%= searchURL %>"
		total="<%= configurationModelIterator.getTotal() %>"
	>
		<liferay-ui:search-container-results
			results="<%= configurationModelIterator.getResults(searchContainer.getStart(), searchContainer.getEnd()) %>"
		/>

		<liferay-ui:search-container-row
			className="com.liferay.configuration.admin.web.internal.model.ConfigurationModel"
			keyProperty="ID"
			modelVar="configurationModel"
		>
			<portlet:renderURL var="editURL">
				<portlet:param name="mvcRenderCommandName" value="/edit_configuration" />
				<portlet:param name="redirect" value="<%= currentURL %>" />
				<portlet:param name="factoryPid" value="<%= configurationModel.getFactoryPid() %>" />
				<portlet:param name="pid" value="<%= configurationModel.getID() %>" />
			</portlet:renderURL>

			<portlet:renderURL var="viewFactoryInstancesURL">
				<portlet:param name="mvcRenderCommandName" value="/view_factory_instances" />
				<portlet:param name="redirect" value="<%= currentURL %>" />
				<portlet:param name="factoryPid" value="<%= configurationModel.getFactoryPid() %>" />
			</portlet:renderURL>

			<liferay-ui:search-container-column-text
				cssClass="table-cell-content"
				name="name"
			>

				<%
				ResourceBundleLoader resourceBundleLoader = resourceBundleLoaderProvider.getResourceBundleLoader(configurationModel.getBundleSymbolicName());

				ResourceBundle componentResourceBundle = resourceBundleLoader.loadResourceBundle(PortalUtil.getLocale(request));

				String configurationModelName = (componentResourceBundle != null) ? LanguageUtil.get(componentResourceBundle, configurationModel.getName()) : configurationModel.getName();
				%>

				<c:choose>
					<c:when test="<%= configurationModel.isFactory() && !configurationModel.isCompanyFactory() %>">
						<aui:a href="<%= viewFactoryInstancesURL %>"><strong><%= configurationModelName %></strong></aui:a>
					</c:when>
					<c:otherwise>
						<aui:a href="<%= editURL %>"><strong><%= configurationModelName %></strong></aui:a>
					</c:otherwise>
				</c:choose>
			</liferay-ui:search-container-column-text>

			<liferay-ui:search-container-column-text
				cssClass="table-cell-content"
				name="category"
			>

				<%
				ConfigurationCategory configurationCategory = configurationEntryRetriever.getConfigurationCategory(configurationModel.getCategory());

				String categorySection = null;
				String category = null;

				if (configurationCategory != null) {
					ConfigurationCategoryMenuDisplay configurationCategoryMenuDisplay = configurationEntryRetriever.getConfigurationCategoryMenuDisplay(configurationCategory.getCategoryKey(), themeDisplay.getLanguageId());

					ConfigurationCategoryDisplay configurationCategoryDisplay = configurationCategoryMenuDisplay.getConfigurationCategoryDisplay();

					category = HtmlUtil.escape(configurationCategoryDisplay.getCategoryLabel(locale));

					categorySection = configurationCategoryDisplay.getSectionLabel(locale);
				}
				else {
					categorySection = LanguageUtil.get(request, "other");
					category = configurationModel.getCategory();
				}
				%>

				<liferay-ui:message key="<%= categorySection %>" /> &gt; <liferay-ui:message key="<%= category %>" />
			</liferay-ui:search-container-column-text>

			<liferay-ui:search-container-column-text
				cssClass="table-cell-content"
				name="scope"
			>
				<c:choose>
					<c:when test="<%= ExtendedObjectClassDefinition.Scope.COMPANY.equals(configurationModel.getScope()) %>">
						<liferay-ui:message key="default-settings-for-all-instances" />
					</c:when>
					<c:when test="<%= ExtendedObjectClassDefinition.Scope.GROUP.equals(configurationModel.getScope()) %>">
						<liferay-ui:message key="default-configuration-for-all-sites" />
					</c:when>
					<c:when test="<%= ExtendedObjectClassDefinition.Scope.PORTLET_INSTANCE.equals(configurationModel.getScope()) %>">
						<liferay-ui:message key="default-configuration-for-widget" />
					</c:when>
					<c:when test="<%= ExtendedObjectClassDefinition.Scope.SYSTEM.equals(configurationModel.getScope()) %>">
						<liferay-ui:message key="system" />
					</c:when>
					<c:otherwise>
						-
					</c:otherwise>
				</c:choose>
			</liferay-ui:search-container-column-text>

			<liferay-ui:search-container-column-text
				name=""
			>
				<liferay-ui:icon-menu
					direction="right"
					markupView="lexicon"
					showWhenSingleIcon="<%= true %>"
				>
					<c:choose>
						<c:when test="<%= configurationModel.isFactory() && !configurationModel.isCompanyFactory() %>">
							<liferay-ui:icon
								message="edit"
								method="post"
								url="<%= viewFactoryInstancesURL %>"
							/>
						</c:when>
						<c:otherwise>
							<liferay-ui:icon
								message="edit"
								method="post"
								url="<%= editURL %>"
							/>

							<c:if test="<%= configurationModel.hasConfiguration() %>">
								<portlet:actionURL name="deleteConfiguration" var="deleteConfigActionURL">
									<portlet:param name="redirect" value="<%= currentURL %>" />
									<portlet:param name="factoryPid" value="<%= configurationModel.getFactoryPid() %>" />
									<portlet:param name="pid" value="<%= configurationModel.getID() %>" />
								</portlet:actionURL>

								<liferay-ui:icon
									message="reset-default-values"
									method="post"
									url="<%= deleteConfigActionURL %>"
								/>

								<portlet:resourceURL id="export" var="exportURL">
									<portlet:param name="factoryPid" value="<%= configurationModel.getFactoryPid() %>" />
									<portlet:param name="pid" value="<%= configurationModel.getID() %>" />
								</portlet:resourceURL>

								<liferay-ui:icon
									message="export"
									method="get"
									url="<%= exportURL %>"
								/>
							</c:if>
						</c:otherwise>
					</c:choose>
				</liferay-ui:icon-menu>
			</liferay-ui:search-container-column-text>
		</liferay-ui:search-container-row>

		<liferay-ui:search-iterator
			markupView="lexicon"
		/>
	</liferay-ui:search-container>
</div>