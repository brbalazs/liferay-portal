<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
ResultRow row = (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);

UserGroup userGroup = (UserGroup)row.getObject();

UserGroupsWebConfiguration userGroupsWebConfiguration = ConfigurationProviderUtil.getSystemConfiguration(UserGroupsWebConfiguration.class);
%>

<liferay-ui:icon-menu
	direction="left-side"
	icon="<%= StringPool.BLANK %>"
	markupView="lexicon"
	message="<%= StringPool.BLANK %>"
	showWhenSingleIcon="<%= true %>"
>
	<c:if test="<%= GroupPermissionUtil.contains(permissionChecker, siteMembershipsDisplayContext.getGroup(), ActionKeys.ASSIGN_USER_ROLES) %>">
		<portlet:renderURL var="assignURL" windowState="<%= LiferayWindowState.POP_UP.toString() %>">
			<portlet:param name="mvcPath" value="/user_groups_roles.jsp" />
			<portlet:param name="userGroupId" value="<%= String.valueOf(userGroup.getUserGroupId()) %>" />
			<portlet:param name="groupId" value="<%= String.valueOf(siteMembershipsDisplayContext.getGroupId()) %>" />
		</portlet:renderURL>

		<%
		Map<String, Object> assignData = new HashMap<>();

		assignData.put("enableassignunassign", userGroupsWebConfiguration.enableAssignUnassignRoleActions());
		assignData.put("href", assignURL.toString());
		assignData.put("usergroupid", userGroup.getUserGroupId());
		%>

		<liferay-ui:icon
			cssClass="assign-site-roles"
			data="<%= assignData %>"
			id='<%= row.getRowId() + "assignSiteRoles" %>'
			message="assign-site-roles"
			url="javascript:;"
		/>

		<c:if test="<%= userGroupsWebConfiguration.enableAssignUnassignRoleActions() %>">
			<portlet:renderURL var="unassignURL" windowState="<%= LiferayWindowState.POP_UP.toString() %>">
				<portlet:param name="mvcPath" value="/user_groups_roles.jsp" />
				<portlet:param name="userGroupId" value="<%= String.valueOf(userGroup.getUserGroupId()) %>" />
				<portlet:param name="groupId" value="<%= String.valueOf(siteMembershipsDisplayContext.getGroupId()) %>" />
				<portlet:param name="assignRoles" value="<%= Boolean.FALSE.toString() %>" />
			</portlet:renderURL>

			<%
			Map<String, Object> unassignData = new HashMap<>();

			unassignData.put("href", unassignURL.toString());
			unassignData.put("usergroupid", userGroup.getUserGroupId());
			%>

			<liferay-ui:icon
				cssClass="unassign-site-roles"
				data="<%= unassignData %>"
				id='<%= row.getRowId() + "unassignSiteRoles" %>'
				message="unassign-site-roles"
				url="javascript:;"
			/>
		</c:if>
	</c:if>

	<c:if test="<%= GroupPermissionUtil.contains(permissionChecker, siteMembershipsDisplayContext.getGroup(), ActionKeys.ASSIGN_MEMBERS) %>">
		<portlet:actionURL name="deleteGroupUserGroups" var="deleteGroupUserGroupsURL">
			<portlet:param name="redirect" value="<%= currentURL %>" />
			<portlet:param name="groupId" value="<%= String.valueOf(siteMembershipsDisplayContext.getGroupId()) %>" />
			<portlet:param name="removeUserGroupId" value="<%= String.valueOf(userGroup.getUserGroupId()) %>" />
		</portlet:actionURL>

		<liferay-ui:icon-delete
			message="remove-membership"
			url="<%= deleteGroupUserGroupsURL %>"
		/>
	</c:if>
</liferay-ui:icon-menu>

<aui:script use="liferay-item-selector-dialog">
	$('#<portlet:namespace /><%= row.getRowId() %>assignSiteRoles').on(
		'click',
		function(event) {
			event.preventDefault();

			var currentTarget = $(event.currentTarget);

			var enableAssignUnassign = currentTarget.data('enableassignunassign');

			var actionUserGroupGroupRoleFm;

			if (enableAssignUnassign) {
				actionUserGroupGroupRoleFm = $(document.<portlet:namespace />addUserGroupGroupRoleFm);
			}
			else {
				actionUserGroupGroupRoleFm = $(document.<portlet:namespace />editUserGroupGroupRoleFm);
			}

			actionUserGroupGroupRoleFm.fm('userGroupId').val(currentTarget.data('usergroupid'));

			var itemSelectorDialog = new A.LiferayItemSelectorDialog(
				{
					eventName: '<portlet:namespace />selectUserGroupsRoles',
					on: {
						selectedItemChange: function(event) {
							var selectedItem = event.newVal;

							if (selectedItem) {
								actionUserGroupGroupRoleFm.append(selectedItem);

								submitForm(actionUserGroupGroupRoleFm);
							}
						}
					},
					'strings.add': '<liferay-ui:message key="done" />',
					title: '<liferay-ui:message key="assign-site-roles" />',
					url: currentTarget.data('href')
				}
			);

			itemSelectorDialog.open();
		}
	);

	<c:if test="<%= userGroupsWebConfiguration.enableAssignUnassignRoleActions() %>">
		$('#<portlet:namespace /><%= row.getRowId() %>unassignSiteRoles').on(
			'click',
			function(event) {
				event.preventDefault();

				var currentTarget = $(event.currentTarget);

				var unassignUserGroupGroupRoleFm = $(document.<portlet:namespace />unassignUserGroupGroupRoleFm);

				unassignUserGroupGroupRoleFm.fm('userGroupId').val(currentTarget.data('usergroupid'));

				var itemSelectorDialog = new A.LiferayItemSelectorDialog(
					{
						eventName: '<portlet:namespace />selectUserGroupsRoles',
						on: {
							selectedItemChange: function(event) {
								var selectedItem = event.newVal;

								if (selectedItem) {
									unassignUserGroupGroupRoleFm.append(selectedItem);

									submitForm(unassignUserGroupGroupRoleFm);
								}
							}
						},
						'strings.add': '<liferay-ui:message key="done" />',
						title: '<liferay-ui:message key="unassign-site-roles" />',
						url: currentTarget.data('href')
					}
				);

				itemSelectorDialog.open();
			}
		);
	</c:if>
</aui:script>