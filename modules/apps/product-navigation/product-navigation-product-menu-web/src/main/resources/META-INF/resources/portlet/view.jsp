<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/portlet/init.jsp" %>

<%
String productMenuState = SessionClicks.get(request, ProductNavigationProductMenuWebKeys.PRODUCT_NAVIGATION_PRODUCT_MENU_STATE, "closed");
%>

<div class="lfr-product-menu-sidebar" id="productMenuSidebar">
	<div class="sidebar-header">
		<div class="autofit-row">
			<div class="autofit-col autofit-col-expand">
				<a href="<%= PortalUtil.addPreservedParameters(themeDisplay, themeDisplay.getURLPortal(), false, true) %>">
					<span class="company-details truncate-text">
						<img alt="" class="company-logo" src="<%= themeDisplay.getRealCompanyLogo() %>" />

						<span class="company-name"><%= HtmlUtil.escape(company.getName()) %></span>
					</span>
				</a>
			</div>

			<div class="autofit-col">
				<aui:icon cssClass="d-inline-block d-md-none icon-monospaced sidenav-close" image="times" markupView="lexicon" url="javascript:;" />
			</div>
		</div>
	</div>

	<div class="sidebar-body">
		<c:if test='<%= Objects.equals(productMenuState, "open") %>'>
			<liferay-util:include page="/portlet/product_menu.jsp" servletContext="<%= application %>" />
		</c:if>
	</div>
</div>

<aui:script use="io-request,liferay-store,parse-content">
	var sidenavToggle = $('#<portlet:namespace />sidenavToggleId');

	sidenavToggle.sideNavigation();

	Liferay.once(
		'screenLoad',
		function() {
			var sideNavigation = sidenavToggle.data('lexicon.sidenav');

			if (sideNavigation) {
				sideNavigation.destroy();
			}
		}
	);

	var sidenavSlider = $('#<portlet:namespace />sidenavSliderId');

	sidenavSlider.on(
		'closed.lexicon.sidenav',
		function(event) {
			Liferay.Store('<%= ProductNavigationProductMenuWebKeys.PRODUCT_NAVIGATION_PRODUCT_MENU_STATE %>', 'closed');
		}
	);

	sidenavSlider.on(
		'open.lexicon.sidenav',
		function(event) {
			Liferay.Store('<%= ProductNavigationProductMenuWebKeys.PRODUCT_NAVIGATION_PRODUCT_MENU_STATE %>', 'open');
		}
	);

	if (Liferay.Util.isPhone() && ($('body').hasClass('open'))) {
		sidenavToggle.sideNavigation('hide');
	}

	<c:if test="<%= productMenuDisplayContext.hasUserPanelCategory() %>">
		var openUserMenuEventHandle = Liferay.on(
			'ProductMenu:openUserMenu',
			function(event) {
				var userCollapseSelector = '#<portlet:namespace /><%= AUIUtil.normalizeId(PanelCategoryKeys.USER) %>Collapse';

				var showUserCollapse = function() {
					var userCollapse = $(userCollapseSelector);

					userCollapse.collapse(
						{
							parent: '#<portlet:namespace />Accordion',
							show: true
						}
					);

					userCollapse.collapse('show');
				};

				if ($('body').hasClass('open')) {
					if ($(userCollapseSelector).hasClass('show')) {
						sidenavToggle.sideNavigation('hide');
					}
					else {
						showUserCollapse();
					}
				}
				else {
					sidenavToggle.sideNavigation('show');

					if (!sidenavToggle.attr('data-url')) {
						showUserCollapse();
					}
					else {
						var urlLoadedState = sidenavSlider.data('url-loaded') ? sidenavSlider.data('url-loaded').state() : '';

						if (urlLoadedState === 'resolved') {
							showUserCollapse();
						}
						else {
							sidenavSlider.on(
								'urlLoaded.lexicon.sidenav',
								function(event) {
									showUserCollapse();
								}
							);
						}
					}
				}
			}
		);

		var clearProductNavigationProductMenuHandles = Liferay.on(
			'destroyPortlet',
			function(event) {
				if (event.portletId === '<%= portletDisplay.getId() %>') {
					openUserMenuEventHandle.detach();

					clearProductNavigationProductMenuHandles.detach();
				}
			}
		);

	</c:if>
</aui:script>