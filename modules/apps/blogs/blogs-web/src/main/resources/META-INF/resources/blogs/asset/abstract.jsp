<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
BlogsPortletInstanceConfiguration blogsPortletInstanceConfiguration = ConfigurationProviderUtil.getConfiguration(BlogsPortletInstanceConfiguration.class, new PortletInstanceSettingsLocator(themeDisplay.getLayout(), BlogsPortletKeys.BLOGS));

BlogsEntry entry = (BlogsEntry)request.getAttribute(WebKeys.BLOGS_ENTRY);
%>

<div class="widget-mode-simple">
	<div class="widget-mode-simple-entry">
		<div class="autofit-row widget-metadata">
			<div class="autofit-col inline-item-before">

				<%
				User entryUser = UserLocalServiceUtil.fetchUser(entry.getUserId());

				String entryUserURL = StringPool.BLANK;

				if ((entryUser != null) && !entryUser.isDefaultUser()) {
					entryUserURL = entryUser.getDisplayURL(themeDisplay);
				}
				%>

				<liferay-ui:user-portrait
					cssClass="user-icon-lg"
					user="<%= entryUser %>"
				/>
			</div>

			<div class="autofit-col autofit-col-expand">
				<div class="autofit-row">
					<div class="autofit-col autofit-col-expand">
						<div class="text-truncate-inline">
							<a class="text-truncate username" href="<%= entryUserURL %>"><%= HtmlUtil.escape(entry.getUserName()) %></a>
						</div>

						<div>
							<span class="hide-accessible"><liferay-ui:message key="published-date" /></span><liferay-ui:message arguments="<%= LanguageUtil.getTimeDescription(request, System.currentTimeMillis() - entry.getStatusDate().getTime(), true) %>" key="x-ago" translateArguments="<%= false %>" />

							<c:if test="<%= blogsPortletInstanceConfiguration.enableReadingTime() %>">
								- <liferay-reading-time:reading-time displayStyle="descriptive" model="<%= entry %>" />
							</c:if>

							<c:if test="<%= blogsPortletInstanceConfiguration.enableViewCount() %>">

								<%
								AssetEntry assetEntry = AssetEntryLocalServiceUtil.getEntry(BlogsEntry.class.getName(), entry.getEntryId());
								%>

								- <liferay-ui:message arguments="<%= assetEntry.getViewCount() %>" key='<%= (assetEntry.getViewCount() == 1) ? "x-view" : "x-views" %>' />
							</c:if>
						</div>
					</div>
				</div>
			</div>
		</div>

		<div class="widget-content" id="<portlet:namespace /><%= entry.getEntryId() %>">

			<%
			String coverImageURL = entry.getCoverImageURL(themeDisplay);
			%>

			<c:if test="<%= Validator.isNotNull(coverImageURL) %>">
				<div class="aspect-ratio aspect-ratio-8-to-3 aspect-ratio-bg-cover cover-image" style="background-image: url(<%= coverImageURL %>);"></div>
			</c:if>

			<p>

				<%
				AssetRenderer<?> assetRenderer = (AssetRenderer<?>)request.getAttribute(WebKeys.ASSET_RENDERER);
				%>

				<%= HtmlUtil.stripHtml(assetRenderer.getSummary(renderRequest, renderResponse)) %>
			</p>
		</div>
	</div>
</div>