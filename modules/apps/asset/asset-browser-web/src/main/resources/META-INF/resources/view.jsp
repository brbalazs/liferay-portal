<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<clay:navigation-bar
	navigationItems="<%= assetBrowserDisplayContext.getNavigationItems() %>"
/>

<clay:management-toolbar
	clearResultsURL="<%= assetBrowserDisplayContext.getClearResultsURL() %>"
	componentId="assetBrowserManagementToolbar"
	creationMenu="<%= Validator.isNotNull(assetBrowserDisplayContext.getAddButtonURL()) ? assetBrowserDisplayContext.getCreationMenu() : null %>"
	disabled="<%= assetBrowserDisplayContext.isDisabledManagementBar() %>"
	filterDropdownItems="<%= assetBrowserDisplayContext.getFilterItemsDropdownItems() %>"
	itemsTotal="<%= assetBrowserDisplayContext.getTotalItems() %>"
	searchActionURL="<%= assetBrowserDisplayContext.getSearchActionURL() %>"
	searchFormName="searchFm"
	selectable="<%= false %>"
	sortingOrder="<%= assetBrowserDisplayContext.getOrderByType() %>"
	sortingURL="<%= assetBrowserDisplayContext.getSortingURL() %>"
	viewTypeItems="<%= assetBrowserDisplayContext.getViewTypeItems() %>"
/>

<aui:form action="<%= assetBrowserDisplayContext.getPortletURL() %>" cssClass="container-fluid-1280" method="post" name="selectAssetFm">
	<aui:input name="typeSelection" type="hidden" value="<%= assetBrowserDisplayContext.getTypeSelection() %>" />

	<liferay-ui:search-container
		searchContainer="<%= assetBrowserDisplayContext.getAssetBrowserSearch() %>"
	>
		<liferay-ui:search-container-row
			className="com.liferay.asset.kernel.model.AssetEntry"
			escapedModel="<%= true %>"
			modelVar="assetEntry"
		>

			<%
			AssetRenderer assetRenderer = assetEntry.getAssetRenderer();
			%>

			<c:choose>
				<c:when test="<%= assetRenderer != null %>">

					<%
					AssetRendererFactory assetRendererFactory = assetBrowserDisplayContext.getAssetRendererFactory();

					Group group = GroupLocalServiceUtil.getGroup(assetEntry.getGroupId());

					String cssClass = StringPool.BLANK;

					String columnCssClass = StringPool.BLANK;

					Map<String, Object> data = new HashMap<String, Object>();

					if ((assetEntry.getEntryId() == assetBrowserDisplayContext.getRefererAssetEntryId()) ||
						(assetEntry.getClassPK() == assetBrowserDisplayContext.getRefererAssetEntryId())
					) {

						cssClass = "unselectable";

						columnCssClass = "unselectable";
					}
					else {
						data.put("assetclassname", assetEntry.getClassName());
						data.put("assetclasspk", assetEntry.getClassPK());
						data.put("assettitle", assetRenderer.getTitle(locale));
						data.put("assettitlemap", JSONFactoryUtil.looseSerialize(LocalizationUtil.getLocalizationMap(assetEntry.getTitle())));
						data.put("assettype", assetRendererFactory.getTypeName(locale, assetBrowserDisplayContext.getSubtypeSelectionId()));
						data.put("entityid", assetEntry.getEntryId());
						data.put("groupdescriptivename", group.getDescriptiveName(locale));

						cssClass = "selector-button";
					}
					%>

					<c:choose>
						<c:when test='<%= Objects.equals(assetBrowserDisplayContext.getDisplayStyle(), "descriptive") %>'>
							<liferay-ui:search-container-column-text
								cssClass="<%= columnCssClass %>"
							>
								<liferay-ui:user-portrait
									userId="<%= assetEntry.getUserId() %>"
								/>
							</liferay-ui:search-container-column-text>

							<liferay-ui:search-container-column-text
								colspan="<%= 2 %>"
								cssClass="<%= columnCssClass %>"
							>

								<%
								Date modifiedDate = assetEntry.getModifiedDate();
								%>

								<h6 class="text-default">
									<span><liferay-ui:message arguments="<%= LanguageUtil.getTimeDescription(request, System.currentTimeMillis() - modifiedDate.getTime(), true) %>" key="modified-x-ago" /></span>
								</h6>

								<h5>
									<c:choose>
										<c:when
											test="<%=
												(assetEntry.getEntryId() == assetBrowserDisplayContext.getRefererAssetEntryId()) ||
													(assetEntry.getClassPK() == assetBrowserDisplayContext.getRefererAssetEntryId())
											%>"
										>
											<%= HtmlUtil.escape(assetRenderer.getTitle(locale)) %>
										</c:when>
										<c:otherwise>
											<aui:a cssClass="<%= cssClass %>" data="<%= data %>" href="javascript:;">
												<%= HtmlUtil.escape(assetRenderer.getTitle(locale)) %>
											</aui:a>
										</c:otherwise>
									</c:choose>
								</h5>

								<h6 class="text-default">
									<%= HtmlUtil.escape(group.getDescriptiveName(locale)) %>
								</h6>
							</liferay-ui:search-container-column-text>
						</c:when>
						<c:when test='<%= Objects.equals(assetBrowserDisplayContext.getDisplayStyle(), "icon") %>'>

							<%
							row.setCssClass("entry-card lfr-asset-item");
							%>

							<liferay-ui:search-container-column-text
								cssClass="<%= columnCssClass %>"
							>
								<c:choose>
									<c:when test="<%= Validator.isNotNull(assetRenderer.getThumbnailPath(renderRequest)) %>">
										<liferay-frontend:vertical-card
											cssClass="<%= cssClass %>"
											data="<%= data %>"
											imageUrl="<%= assetRenderer.getThumbnailPath(renderRequest) %>"
											subtitle="<%= HtmlUtil.escape(group.getDescriptiveName(locale)) %>"
											title="<%= assetRenderer.getTitle(locale) %>"
										/>
									</c:when>
									<c:otherwise>
										<liferay-frontend:icon-vertical-card
											cssClass="<%= cssClass %>"
											data="<%= data %>"
											icon="<%= assetRendererFactory.getIconCssClass() %>"
											subtitle="<%= HtmlUtil.escape(group.getDescriptiveName(locale)) %>"
											title="<%= assetRenderer.getTitle(locale) %>"
										/>
									</c:otherwise>
								</c:choose>
							</liferay-ui:search-container-column-text>
						</c:when>
						<c:when test='<%= Objects.equals(assetBrowserDisplayContext.getDisplayStyle(), "list") %>'>
							<liferay-ui:search-container-column-text
								cssClass="<%= columnCssClass %>"
								name="title"
								truncate="<%= true %>"
							>
								<c:choose>
									<c:when
										test="<%=
											(assetEntry.getEntryId() == assetBrowserDisplayContext.getRefererAssetEntryId()) ||
												(assetEntry.getClassPK() == assetBrowserDisplayContext.getRefererAssetEntryId())
										%>"
									>
										<%= HtmlUtil.escape(assetRenderer.getTitle(locale)) %>
									</c:when>
									<c:otherwise>
										<aui:a cssClass="<%= cssClass %>" data="<%= data %>" href="javascript:;">
											<%= HtmlUtil.escape(assetRenderer.getTitle(locale)) %>
										</aui:a>
									</c:otherwise>
								</c:choose>
							</liferay-ui:search-container-column-text>

							<liferay-ui:search-container-column-text
								cssClass="<%= columnCssClass %>"
								name="description"
								truncate="<%= true %>"
								value="<%= HtmlUtil.escape(assetRenderer.getSummary(renderRequest, renderResponse)) %>"
							/>

							<liferay-ui:search-container-column-text
								cssClass="<%= columnCssClass %>"
								name="author"
								value="<%= PortalUtil.getUserName(assetEntry) %>"
							/>

							<liferay-ui:search-container-column-date
								cssClass="<%= columnCssClass %>"
								name="modified-date"
								value="<%= assetEntry.getModifiedDate() %>"
							/>

							<liferay-ui:search-container-column-text
								cssClass="<%= columnCssClass %>"
								name="site"
								value="<%= HtmlUtil.escape(group.getDescriptiveName(locale)) %>"
							/>
						</c:when>
					</c:choose>
				</c:when>
				<c:otherwise>

					<%
					if (assetRenderer == null) {
						_log.error("Unable to get asset renderer for asset entry with primary key " + assetEntry.getEntryId());
					}

					row.setSkip(true);
					%>

				</c:otherwise>
			</c:choose>
		</liferay-ui:search-container-row>

		<liferay-ui:search-iterator
			displayStyle="<%= assetBrowserDisplayContext.getDisplayStyle() %>"
			markupView="lexicon"
		/>
	</liferay-ui:search-container>
</aui:form>

<aui:script>
	Liferay.Util.selectEntityHandler('#<portlet:namespace />selectAssetFm', '<%= HtmlUtil.escapeJS(assetBrowserDisplayContext.getEventName()) %>');
</aui:script>

<%!
private static Log _log = LogFactoryUtil.getLog("com_liferay_asset_browser_web.view_jsp");
%>