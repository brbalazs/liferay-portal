<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/html/taglib/ui/social_bookmark/init.jsp" %>

<%
String plusOneDisplayStyle = "medium";

if (displayStyle.equals("vertical")) {
	plusOneDisplayStyle = "tall";
}
%>

<liferay-util:html-bottom
	outputKey="taglib_ui_social_bookmark_plusone"
>
	<script type="text/javascript">
		window.___gcfg = {
			lang: '<%= locale.getLanguage() %>-<%= locale.getCountry() %>'
		};

		(function() {
			var script = document.createElement('script');

			script.async = true;
			script.type = 'text/javascript';

			script.src = 'https://apis.google.com/js/plusone.js';

			var firstScript = document.getElementsByTagName('script')[0];

			firstScript.parentNode.insertBefore(script, firstScript);
		})();
	</script>
</liferay-util:html-bottom>

<g:plusone
	count='<%= !displayStyle.equals("simple") %>'
	href="<%= url %>"
	size="<%= plusOneDisplayStyle %>"
>
</g:plusone>