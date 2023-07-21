<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
ResultRow row = (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);

User user2 = (User)row.getObject();

boolean organizationUser = GetterUtil.getBoolean(row.getParameter("organizationUser"));
boolean userGroupUser = GetterUtil.getBoolean(row.getParameter("userGroupUser"));
%>

<liferay-ui:icon-menu
	direction="left-side"
	icon="<%= StringPool.BLANK %>"
	markupView="lexicon"
	message="<%= StringPool.BLANK %>"
	showWhenSingleIcon="<%= true %>"
>
	<c:if test="<%= GroupPermissionUtil.contains(permissionChecker, siteMembershipsDisplayContext.getGroupId(), ActionKeys.ASSIGN_USER_ROLES) %>">
		<portlet:renderURL var="assignURL" windowState="<%= LiferayWindowState.POP_UP.toString() %>">
			<portlet:param name="mvcPath" value="/users_roles.jsp" />
			<portlet:param name="p_u_i_d" value="<%= String.valueOf(user2.getUserId()) %>" />
			<portlet:param name="groupId" value="<%= String.valueOf(siteMembershipsDisplayContext.getGroupId()) %>" />
		</portlet:renderURL>

		<%
		Map<String, Object> data = new HashMap<String, Object>();

		data.put("href", assignURL.toString());
		data.put("userid", user2.getUserId());
		%>

		<liferay-ui:icon
			cssClass="assign-site-roles"
			data="<%= data %>"
			id='<%= row.getRowId() + "assignSiteRoles" %>'
			message="assign-site-roles"
			url="javascript:;"
		/>
	</c:if>

	<c:if test="<%= GroupPermissionUtil.contains(permissionChecker, siteMembershipsDisplayContext.getGroupId(), ActionKeys.ASSIGN_MEMBERS) && !(organizationUser || userGroupUser) && !SiteMembershipPolicyUtil.isMembershipProtected(permissionChecker, user2.getUserId(), siteMembershipsDisplayContext.getGroupId()) && !SiteMembershipPolicyUtil.isMembershipRequired(user2.getUserId(), siteMembershipsDisplayContext.getGroupId()) %>">
		<portlet:actionURL name="deleteGroupUsers" var="deleteGroupUsersURL">
			<portlet:param name="redirect" value="<%= currentURL %>" />
			<portlet:param name="groupId" value="<%= String.valueOf(siteMembershipsDisplayContext.getGroupId()) %>" />
			<portlet:param name="removeUserId" value="<%= String.valueOf(user2.getUserId()) %>" />
		</portlet:actionURL>

		<liferay-ui:icon-delete
			message="remove-membership"
			url="<%= deleteGroupUsersURL %>"
		/>
	</c:if>
</liferay-ui:icon-menu>

<aui:script use="liferay-item-selector-dialog">
	$('#<portlet:namespace /><%= row.getRowId() %>assignSiteRoles').on(
		'click',
		function(event) {
			event.preventDefault();

			var currentTarget = $(event.currentTarget);

			var editUserGroupRoleFm = $(document.<portlet:namespace />editUserGroupRoleFm);

			editUserGroupRoleFm.fm('p_u_i_d').val(currentTarget.data('userid'));

			var itemSelectorDialog = new A.LiferayItemSelectorDialog(
				{
					eventName: '<portlet:namespace />selectUsersRoles',
					on: {
						selectedItemChange: function(event) {
							var selectedItem = event.newVal;

							if (selectedItem) {
								editUserGroupRoleFm.append(selectedItem);

								submitForm(editUserGroupRoleFm);
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
</aui:script>