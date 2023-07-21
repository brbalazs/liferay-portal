<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
PortletURL configurationRenderURL = (PortletURL)request.getAttribute("configuration.jsp-configurationRenderURL");
String eventName = "_" + HtmlUtil.escapeJS(assetPublisherDisplayContext.getPortletResource()) + "_selectAsset";

List<AssetEntry> assetEntries = assetPublisherHelper.getAssetEntries(renderRequest, portletPreferences, permissionChecker, assetPublisherDisplayContext.getGroupIds(), true, assetPublisherDisplayContext.isEnablePermissions(), true, AssetRendererFactory.TYPE_LATEST);
%>

<liferay-ui:search-container
	compactEmptyResultsMessage="<%= true %>"
	emptyResultsMessage="none"
	iteratorURL="<%= configurationRenderURL %>"
	total="<%= assetEntries.size() %>"
>
	<liferay-ui:search-container-results
		results="<%= assetEntries.subList(searchContainer.getStart(), searchContainer.getResultEnd()) %>"
	/>

	<liferay-ui:search-container-row
		className="com.liferay.asset.kernel.model.AssetEntry"
		escapedModel="<%= true %>"
		keyProperty="entryId"
		modelVar="assetEntry"
	>

		<%
		AssetRendererFactory<?> assetRendererFactory = AssetRendererFactoryRegistryUtil.getAssetRendererFactoryByClassName(assetEntry.getClassName());

		AssetRenderer<?> assetRenderer = assetRendererFactory.getAssetRenderer(assetEntry.getClassPK(), AssetRendererFactory.TYPE_LATEST);
		%>

		<liferay-ui:search-container-column-text
			name="title"
			truncate="<%= true %>"
		>
			<%= HtmlUtil.escape(assetRenderer.getTitle(locale)) %>

			<c:if test="<%= !assetEntry.isVisible() %>">
				(<aui:workflow-status
					markupView="lexicon"
					showIcon="<%= false %>"
					showLabel="<%= false %>"
					status="<%= assetRenderer.getStatus() %>"
					statusMessage='<%= (assetRenderer.getStatus() == 0) ? "not-visible" : WorkflowConstants.getStatusLabel(assetRenderer.getStatus()) %>'
				/>)
			</c:if>
		</liferay-ui:search-container-column-text>

		<liferay-ui:search-container-column-text
			name="type"
			value="<%= assetRendererFactory.getTypeName(locale) %>"
		/>

		<liferay-ui:search-container-column-date
			name="modified-date"
			value="<%= assetEntry.getModifiedDate() %>"
		/>

		<liferay-ui:search-container-column-jsp
			path="/configuration/asset_selection_action.jsp"
		/>

		<liferay-ui:search-container-column-jsp
			cssClass="entry-action-column"
			path="/configuration/asset_selection_order_action.jsp"
		/>
	</liferay-ui:search-container-row>

	<liferay-ui:search-iterator
		markupView="lexicon"
		paginate="<%= total > SearchContainer.DEFAULT_DELTA %>"
	/>
</liferay-ui:search-container>

<c:if test='<%= SessionMessages.contains(renderRequest, "deletedMissingAssetEntries") %>'>
	<div class="alert alert-info">
		<liferay-ui:message key="the-selected-assets-have-been-removed-from-the-list-because-they-do-not-belong-in-the-scope-of-this-widget" />
	</div>
</c:if>

<%
long[] groupIds = assetPublisherDisplayContext.getGroupIds();

for (long groupId : groupIds) {
	Group group = GroupLocalServiceUtil.getGroup(groupId);
%>

	<div class="select-asset-selector">
		<div class="edit-controls lfr-meta-actions">
			<liferay-ui:icon-menu
				cssClass="select-existing-selector"
				direction="right"
				message='<%= LanguageUtil.format(request, (groupIds.length == 1) ? "select" : "select-in-x", HtmlUtil.escape(group.getDescriptiveName(locale)), false) %>'
				showArrow="<%= false %>"
				showWhenSingleIcon="<%= true %>"
			>

				<%
				List<AssetRendererFactory<?>> assetRendererFactories = ListUtil.sort(AssetRendererFactoryRegistryUtil.getAssetRendererFactories(company.getCompanyId()), new AssetRendererFactoryTypeNameComparator(locale));

				for (AssetRendererFactory<?> curRendererFactory : assetRendererFactories) {
					long curGroupId = groupId;

					if (!curRendererFactory.isSelectable()) {
						continue;
					}

					PortletURL assetBrowserURL = PortletProviderUtil.getPortletURL(request, curRendererFactory.getClassName(), PortletProvider.Action.BROWSE);

					if (assetBrowserURL == null) {
						continue;
					}

					if (group.isStagingGroup() && !group.isStagedPortlet(curRendererFactory.getPortletId())) {
						curGroupId = group.getLiveGroupId();
					}

					assetBrowserURL.setParameter("groupId", String.valueOf(curGroupId));
					assetBrowserURL.setParameter("selectedGroupIds", String.valueOf(curGroupId));
					assetBrowserURL.setParameter("typeSelection", curRendererFactory.getClassName());
					assetBrowserURL.setParameter("showNonindexable", String.valueOf(Boolean.TRUE));
					assetBrowserURL.setParameter("showScheduled", String.valueOf(Boolean.TRUE));
					assetBrowserURL.setParameter("eventName", eventName);
					assetBrowserURL.setPortletMode(PortletMode.VIEW);
					assetBrowserURL.setWindowState(LiferayWindowState.POP_UP);

					Map<String, Object> data = new HashMap<String, Object>();

					data.put("groupid", String.valueOf(curGroupId));
				%>

					<c:choose>
						<c:when test="<%= !curRendererFactory.isSupportsClassTypes() %>">

							<%
							data.put("href", assetBrowserURL.toString());

							String type = curRendererFactory.getTypeName(locale);

							data.put("destroyOnHide", true);
							data.put("title", LanguageUtil.format(request, "select-x", type, false));
							data.put("type", type);
							%>

							<liferay-ui:icon
								cssClass="asset-selector"
								data="<%= data %>"
								id="<%= curGroupId + FriendlyURLNormalizerUtil.normalize(type) %>"
								message="<%= HtmlUtil.escape(type) %>"
								url="javascript:;"
							/>
						</c:when>
						<c:otherwise>

							<%
							ClassTypeReader classTypeReader = curRendererFactory.getClassTypeReader();

							List<ClassType> assetAvailableClassTypes = classTypeReader.getAvailableClassTypes(PortalUtil.getCurrentAndAncestorSiteGroupIds(curGroupId), locale);

							for (ClassType assetAvailableClassType : assetAvailableClassTypes) {
								assetBrowserURL.setParameter("subtypeSelectionId", String.valueOf(assetAvailableClassType.getClassTypeId()));
								assetBrowserURL.setParameter("showNonindexable", String.valueOf(Boolean.TRUE));
								assetBrowserURL.setParameter("showScheduled", String.valueOf(Boolean.TRUE));

								data.put("href", assetBrowserURL.toString());

								String type = assetAvailableClassType.getName();

								data.put("destroyOnHide", true);
								data.put("title", LanguageUtil.format(request, "select-x", type, false));
								data.put("type", type);
							%>

								<liferay-ui:icon
									cssClass="asset-selector"
									data="<%= data %>"
									id="<%= curGroupId + FriendlyURLNormalizerUtil.normalize(type) %>"
									message="<%= HtmlUtil.escape(type) %>"
									url="javascript:;"
								/>

							<%
							}
							%>

						</c:otherwise>
					</c:choose>

				<%
				}
				%>

			</liferay-ui:icon-menu>
		</div>
	</div>

<%
}
%>

<aui:script>
	function <portlet:namespace />moveSelectionDown(assetEntryOrder) {
		var form = AUI.$(document.<portlet:namespace />fm);

		form.fm('<%= Constants.CMD %>').val('move-selection-down');
		form.fm('redirect').val('<%= HtmlUtil.escapeJS(currentURL) %>');
		form.fm('assetEntryOrder').val(assetEntryOrder);

		submitForm(form);
	}

	function <portlet:namespace />moveSelectionUp(assetEntryOrder) {
		var form = AUI.$(document.<portlet:namespace />fm);

		form.fm('<%= Constants.CMD %>').val('move-selection-up');
		form.fm('redirect').val('<%= HtmlUtil.escapeJS(currentURL) %>');
		form.fm('assetEntryOrder').val(assetEntryOrder);

		submitForm(form);
	}

	function selectAsset(assetEntryId, assetClassName, assetType, assetEntryTitle, groupName) {
		document.<portlet:namespace />fm.<portlet:namespace /><%= Constants.CMD %>.value = 'add-selection';
		document.<portlet:namespace />fm.<portlet:namespace />redirect.value = '<%= HtmlUtil.escapeJS(currentURL) %>';
		document.<portlet:namespace />fm.<portlet:namespace />assetEntryId.value = assetEntryId;
		document.<portlet:namespace />fm.<portlet:namespace />assetEntryType.value = assetClassName;

		submitForm(document.<portlet:namespace />fm);
	}
</aui:script>

<aui:script sandbox="<%= true %>">
	$('body').on(
		'click',
		'.asset-selector a',
		function(event) {
			event.preventDefault();

			var currentTarget = $(event.currentTarget);

			Liferay.Util.selectEntity(
				{
					dialog: {
						constrain: true,
						destroyOnHide: true,
						modal: true
					},
					eventName: '<%= eventName %>',
					id: '<%= eventName %>' + currentTarget.attr('id'),
					title: currentTarget.data('title'),
					uri: currentTarget.data('href')
				},
				function(event) {
					selectAsset(event.entityid, event.assetclassname, event.assettype, event.assettitle, event.groupdescriptivename);
				}
			);
		}
	);
</aui:script>