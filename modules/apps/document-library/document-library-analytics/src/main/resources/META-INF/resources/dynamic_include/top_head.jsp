<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/dynamic_include/init.jsp" %>

<script data-senna-track="temporary" type="text/javascript">
	if (window.Analytics) {
		window.<%= DocumentLibraryAnalyticsConstants.JS_PREFIX %>isViewFileEntry = false;
	}
</script>

<aui:script sandbox="<%= true %>">
	var pathnameRegexp = /\/documents\/(\d+)\/(\d+)\/(.+?)\/([^&]+)/;

	function handleDownloadClick(event) {
		if (event.target.nodeName.toLowerCase() === 'a') {
			if (window.Analytics) {
				var anchor = event.target;
				var match = pathnameRegexp.exec(anchor.pathname);

				if (match) {
					var getParameterValue = function(parameterName) {
						var result = null;
						var tmp = [];

						anchor
							.search
							.substr(1)
							.split("&")
							.forEach(
								function(item) {
									tmp = item.split("=");
									if (tmp[0] === parameterName) result = decodeURIComponent(tmp[1]);
								}
							);
						return result;
					}

					var groupId = match[1];
					var fileEntryUUID = match[4];

					event.preventDefault();

					fetch(
						'<%= PortalUtil.getPortalURL(request) %><%= Portal.PATH_MODULE %><%= DocumentLibraryAnalyticsConstants.PATH_RESOLVE_FILE_ENTRY %>?groupId=' + encodeURIComponent(groupId) + '&uuid=' + encodeURIComponent(fileEntryUUID),
						{
							credentials: 'include',
							method: 'GET'
						}
					).then(function(response) {
						return response.json();
					}).then(function(response) {
						location.replace(event.target.href);

						Analytics.send(
							'documentDownloaded',
							'Document',
							{
								groupId: groupId,
								fileEntryId: response.fileEntryId,
								preview: !!window.<%= DocumentLibraryAnalyticsConstants.JS_PREFIX %>isViewFileEntry,
								title: decodeURIComponent(match[3].replace(/\.[^.\\:*?"<>|\r\n]+$/, '')),
								version: getParameterValue('version')
							}
						);
					}).catch(function() {
						return;
					});
				}
			}
		}
	}

	document.body.addEventListener('click', handleDownloadClick);

	var onDestroyPortlet = function() {
		document.body.removeEventListener('click', handleDownloadClick);
		Liferay.detach('destroyPortlet', onDestroyPortlet);
	}

	Liferay.on('destroyPortlet', onDestroyPortlet);
</aui:script>