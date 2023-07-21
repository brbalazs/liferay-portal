<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/control_menu/init.jsp" %>

<%
ProductNavigationControlMenuCategoryRegistry productNavigationControlMenuCategoryRegistry = ServletContextUtil.getProductNavigationControlMenuCategoryRegistry();

List<ProductNavigationControlMenuCategory> productNavigationControlMenuCategories = productNavigationControlMenuCategoryRegistry.getProductNavigationControlMenuCategories(ProductNavigationControlMenuCategoryKeys.ROOT);

ProductNavigationControlMenuEntryRegistry productNavigationControlMenuEntryRegistry = ServletContextUtil.getProductNavigationControlMenuEntryRegistry();
%>

<c:if test="<%= !productNavigationControlMenuCategories.isEmpty() %>">
	<div class="control-menu control-menu-level-1 d-print-none" data-qa-id="controlMenu" id="<portlet:namespace />ControlMenu">
		<div class="container-fluid container-fluid-max-xl">
			<ul class="control-menu-level-1-nav control-menu-nav" data-namespace="<portlet:namespace />" data-qa-id="header" id="<portlet:namespace />controlMenu">

				<%
				Map<ProductNavigationControlMenuCategory, List<ProductNavigationControlMenuEntry>> productNavigationControlMenuEntriesMap = new LinkedHashMap<>();

				for (ProductNavigationControlMenuCategory productNavigationControlMenuCategory : productNavigationControlMenuCategories) {
				%>

					<li class="control-menu-nav-category <%= productNavigationControlMenuCategory.getKey() %>-control-group">
						<ul class="control-menu-nav">

							<%
							List<ProductNavigationControlMenuEntry> productNavigationControlMenuEntries = productNavigationControlMenuEntryRegistry.getProductNavigationControlMenuEntries(productNavigationControlMenuCategory, request);

							productNavigationControlMenuEntriesMap.put(productNavigationControlMenuCategory, productNavigationControlMenuEntries);

							for (ProductNavigationControlMenuEntry productNavigationControlMenuEntry : productNavigationControlMenuEntries) {
								if (productNavigationControlMenuEntry.includeIcon(request, PipingServletResponse.createPipingServletResponse(pageContext))) {
									continue;
								}
							%>

								<li class="control-menu-nav-item">
									<liferay-ui:icon
										data="<%= productNavigationControlMenuEntry.getData(request) %>"
										icon="<%= productNavigationControlMenuEntry.getIcon(request) %>"
										iconCssClass="<%= productNavigationControlMenuEntry.getIconCssClass(request) %>"
										label="<%= false %>"
										linkCssClass='<%= "control-menu-icon " + productNavigationControlMenuEntry.getLinkCssClass(request) %>'
										markupView="<%= productNavigationControlMenuEntry.getMarkupView(request) %>"
										message="<%= productNavigationControlMenuEntry.getLabel(locale) %>"
										method="get"
										url="<%= productNavigationControlMenuEntry.getURL(request) %>"
									/>
								</li>

							<%
							}
							%>

						</ul>
					</li>

				<%
				}
				%>

			</ul>
		</div>

		<div class="control-menu-body">

			<%
			for (ProductNavigationControlMenuCategory productNavigationControlMenuCategory : productNavigationControlMenuCategories) {
				List<ProductNavigationControlMenuEntry> productNavigationControlMenuEntries = productNavigationControlMenuEntriesMap.get(productNavigationControlMenuCategory);

				for (ProductNavigationControlMenuEntry productNavigationControlMenuEntry : productNavigationControlMenuEntries) {
					productNavigationControlMenuEntry.includeBody(request, PipingServletResponse.createPipingServletResponse(pageContext));
				}
			}
			%>

		</div>

		<div id="controlMenuAlertsContainer"></div>
	</div>

	<aui:script use="liferay-product-navigation-control-menu">
		Liferay.ControlMenu.init('#<portlet:namespace />controlMenu');

		var panelEntryBodies = $('#<portlet:namespace />ControlMenu [data-toggle="sidenav"]').toArray().map(
			function(item) {
				return $(item.getAttribute('data-target').split(',')[0]);
			}
		);

		panelEntryBodies.forEach(
			function(item) {
				item.on(
					'openStart.lexicon.sidenav',
					function(event) {
						var itemId = event.target.getAttribute('id');

						panelEntryBodies.forEach(
							function(item) {
								var panelId = item.attr('id');

								if (panelId !== itemId) {
									$('#<portlet:namespace />ControlMenu [data-toggle="sidenav"][data-target*="' + panelId + '"]').sideNavigation('hide');
								}
							}
						);
					}
				);
			}
		);
	</aui:script>
</c:if>