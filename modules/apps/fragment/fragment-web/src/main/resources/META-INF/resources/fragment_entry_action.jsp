<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
ResultRow row = (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);

FragmentEntry fragmentEntry = (FragmentEntry)row.getObject();
%>

<liferay-ui:icon-menu
	direction="left-side"
	icon="<%= StringPool.BLANK %>"
	markupView="lexicon"
	message="<%= StringPool.BLANK %>"
	showWhenSingleIcon="<%= true %>"
>
	<c:if test="<%= FragmentPermission.contains(permissionChecker, scopeGroupId, FragmentActionKeys.MANAGE_FRAGMENT_ENTRIES) %>">
		<portlet:renderURL var="editFragmentEntryURL">
			<portlet:param name="mvcRenderCommandName" value="/fragment/edit_fragment_entry" />
			<portlet:param name="redirect" value="<%= currentURL %>" />
			<portlet:param name="fragmentCollectionId" value="<%= String.valueOf(fragmentEntry.getFragmentCollectionId()) %>" />
			<portlet:param name="fragmentEntryId" value="<%= String.valueOf(fragmentEntry.getFragmentEntryId()) %>" />
		</portlet:renderURL>

		<liferay-ui:icon
			message="edit"
			url="<%= editFragmentEntryURL %>"
		/>

		<portlet:actionURL name="/fragment/update_fragment_entry" var="updateFragmentEntryURL">
			<portlet:param name="fragmentCollectionId" value="<%= String.valueOf(fragmentEntry.getFragmentCollectionId()) %>" />
			<portlet:param name="fragmentEntryId" value="<%= String.valueOf(fragmentEntry.getFragmentEntryId()) %>" />
		</portlet:actionURL>

		<%
		Map<String, Object> updateFragmentEntryData = new HashMap<String, Object>();

		updateFragmentEntryData.put("form-submit-url", updateFragmentEntryURL.toString());
		updateFragmentEntryData.put("id-field-value", fragmentEntry.getFragmentEntryId());
		updateFragmentEntryData.put("main-field-value", fragmentEntry.getName());
		%>

		<liferay-ui:icon
			cssClass='<%= renderResponse.getNamespace() + "update-fragment-action-option" %>'
			data="<%= updateFragmentEntryData %>"
			message="rename"
			url="javascript:;"
		/>
	</c:if>

	<c:if test="<%= FragmentPermission.contains(permissionChecker, scopeGroupId, FragmentActionKeys.MANAGE_FRAGMENT_ENTRIES) %>">

		<%
		Map<String, Object> data = new HashMap<>();

		data.put("fragment-entry-id", fragmentEntry.getFragmentEntryId());
		%>

		<liferay-ui:icon
			cssClass="update-fragment-preview"
			data="<%= data %>"
			message="change-thumbnail"
			url="javascript:;"
		/>
	</c:if>

	<portlet:resourceURL id="/fragment/export_fragment_entries" var="exportFragmentEntriesURL">
		<portlet:param name="fragmentEntryId" value="<%= String.valueOf(fragmentEntry.getFragmentEntryId()) %>" />
	</portlet:resourceURL>

	<liferay-ui:icon
		message="export"
		url="<%= exportFragmentEntriesURL %>"
	/>

	<c:if test="<%= fragmentEntry.getUsageCount() > 0 %>">
		<portlet:renderURL var="viewFragmentEntryUsagesURL">
			<portlet:param name="mvcRenderCommandName" value="/fragment/view_fragment_entry_usages" />
			<portlet:param name="redirect" value="<%= currentURL %>" />
			<portlet:param name="fragmentCollectionId" value="<%= String.valueOf(fragmentEntry.getFragmentCollectionId()) %>" />
			<portlet:param name="fragmentEntryId" value="<%= String.valueOf(fragmentEntry.getFragmentEntryId()) %>" />
		</portlet:renderURL>

		<liferay-ui:icon
			message="view-usages"
			url="<%= viewFragmentEntryUsagesURL %>"
		/>
	</c:if>

	<c:if test="<%= FragmentPermission.contains(permissionChecker, scopeGroupId, FragmentActionKeys.MANAGE_FRAGMENT_ENTRIES) %>">
		<portlet:actionURL name="/fragment/delete_fragment_entries" var="deleteFragmentEntryURL">
			<portlet:param name="redirect" value="<%= currentURL %>" />
			<portlet:param name="fragmentEntryId" value="<%= String.valueOf(fragmentEntry.getFragmentEntryId()) %>" />
		</portlet:actionURL>

		<liferay-ui:icon-delete
			url="<%= deleteFragmentEntryURL %>"
		/>
	</c:if>
</liferay-ui:icon-menu>