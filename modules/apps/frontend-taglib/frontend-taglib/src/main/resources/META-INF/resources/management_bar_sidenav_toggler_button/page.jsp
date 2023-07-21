<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/management_bar_sidenav_toggler_button/init.jsp" %>

<liferay-frontend:management-bar-button
	active="<%= false %>"
	cssClass="<%= cssClass %>"
	data="<%= data %>"
	disabled="<%= disabled %>"
	href="<%= href %>"
	icon="<%= icon %>"
	iconCssClass="<%= iconCssClass %>"
	id="<%= id %>"
	label="<%= label %>"
/>

<aui:script use="liferay-store">
	var sidenavToggle = $('[href="#<%= sidenavId %>"]');

	if (!sidenavToggle.sideNavigation('instance')) {
		sidenavToggle.sideNavigation(
			{
				position: '<%= position %>',
				type: '<%= type %>',
				typeMobile: '<%= typeMobile %>',
				width: '<%= width %>'
			}
		);

		var sidenavSlider = $('#<%= sidenavId %>');

		sidenavSlider.on(
			'closed.lexicon.sidenav',
			function(event) {
				Liferay.Store('com.liferay.info.panel_<%= sidenavId %>', 'closed');
			}
		);

		sidenavSlider.on(
			'open.lexicon.sidenav',
			function(event) {
				Liferay.Store('com.liferay.info.panel_<%= sidenavId %>', 'open');
			}
		);
	}
</aui:script>