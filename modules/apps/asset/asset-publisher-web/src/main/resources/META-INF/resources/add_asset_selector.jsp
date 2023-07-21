<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<div class="container-fluid-1280">
	<aui:fieldset-group markupView="lexicon">
		<aui:fieldset>

			<%
			long[] groupIds = assetPublisherDisplayContext.getGroupIds();

			Map<Long, List<AssetPublisherAddItemHolder>> scopeAssetPublisherAddItemHolders = assetPublisherDisplayContext.getScopeAssetPublisherAddItemHolders(groupIds.length);
			%>

			<aui:select label="scope" name="selectScope">

				<%
				for (Long groupId : scopeAssetPublisherAddItemHolders.keySet()) {
				%>

					<aui:option label="<%= HtmlUtil.escape((GroupLocalServiceUtil.getGroup(groupId)).getDescriptiveName(locale)) %>" selected="<%= groupId == scopeGroupId %>" value="<%= groupId %>" />

				<%
				}
				%>

			</aui:select>

			<%
			for (Map.Entry<Long, List<AssetPublisherAddItemHolder>> entry : scopeAssetPublisherAddItemHolders.entrySet()) {
				Long groupId = entry.getKey();
				List<AssetPublisherAddItemHolder> assetPublisherAddItemHolders = entry.getValue();
			%>

				<div class="asset-entry-type <%= (groupId == scopeGroupId) ? StringPool.BLANK : "hide" %>" id="<%= liferayPortletResponse.getNamespace() + groupId %>">
					<aui:select cssClass="asset-entry-type-select" label="asset-entry-type" name="selectAssetEntryType">

						<%
						for (AssetPublisherAddItemHolder assetPublisherAddItemHolder : assetPublisherAddItemHolders) {
							String message = assetPublisherAddItemHolder.getModelResource();

							long curGroupId = groupId;

							Group group = GroupLocalServiceUtil.fetchGroup(groupId);

							if (!group.isStagedPortlet(assetPublisherAddItemHolder.getPortletId()) && !group.isStagedRemotely()) {
								curGroupId = group.getLiveGroupId();
							}

							Map<String, Object> data = new HashMap<String, Object>();

							data.put("title", LanguageUtil.format((HttpServletRequest)pageContext.getRequest(), "new-x", HtmlUtil.escape(message), false));
							data.put("url", assetHelper.getAddURLPopUp(curGroupId, plid, assetPublisherAddItemHolder.getPortletURL(), false, null));
						%>

							<aui:option data="<%= data %>" label="<%= HtmlUtil.escape(message) %>" />

						<%
						}
						%>

					</aui:select>
				</div>

				<aui:script>
					Liferay.Util.toggleSelectBox('<portlet:namespace />selectScope', '<%= groupId %>', '<portlet:namespace /><%= groupId %>');
				</aui:script>

			<%
			}
			%>

		</aui:fieldset>
	</aui:fieldset-group>

	<aui:button-row>
		<aui:button onClick='<%= renderResponse.getNamespace() + "addAssetEntry();" %>' primary="<%= true %>" value="add" />
	</aui:button-row>
</div>

<aui:script>
	function <portlet:namespace />addAssetEntry() {
		var A = AUI();

		var visibleItem = A.one('.asset-entry-type:not(.hide)');

		var assetEntryTypeSelector = visibleItem.one('.asset-entry-type-select');

		var index = assetEntryTypeSelector.get('selectedIndex');

		var selectedOption = assetEntryTypeSelector.get('options').item(index);

		var title = selectedOption.attr('data-title');
		var url = selectedOption.attr('data-url');

		var dialog = Liferay.Util.getWindow();

		dialog.iframe.set('uri', url);
		dialog.titleNode.html(title);
	}
</aui:script>