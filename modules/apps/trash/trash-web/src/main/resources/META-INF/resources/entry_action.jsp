<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
ResultRow row = (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);

TrashEntry trashEntry = null;

if (row != null) {
	trashEntry = (TrashEntry)row.getObject();
}
else {
	trashEntry = (TrashEntry)request.getAttribute(TrashWebKeys.TRASH_ENTRY);
}
%>

<liferay-ui:icon-menu
	direction="left-side"
	icon="<%= StringPool.BLANK %>"
	markupView="lexicon"
	message="<%= StringPool.BLANK %>"
	showWhenSingleIcon="<%= true %>"
>

	<%
	TrashHandler trashHandler = TrashHandlerRegistryUtil.getTrashHandler(trashEntry.getClassName());
	%>

	<c:choose>
		<c:when test="<%= trashHandler.isRestorable(trashEntry.getClassPK()) && !trashHandler.isInTrashContainer(trashEntry.getClassPK()) %>">
			<portlet:actionURL name="restoreEntries" var="restoreEntryURL">
				<portlet:param name="redirect" value="<%= currentURL %>" />
				<portlet:param name="trashEntryId" value="<%= String.valueOf(trashEntry.getEntryId()) %>" />
			</portlet:actionURL>

			<liferay-ui:icon
				message="restore"
				url="<%= restoreEntryURL.toString() %>"
			/>
		</c:when>
		<c:when test="<%= !trashHandler.isRestorable(trashEntry.getClassPK()) && trashHandler.isMovable(trashEntry.getClassPK()) %>">
			<portlet:renderURL var="moveURL" windowState="<%= LiferayWindowState.POP_UP.toString() %>">
				<portlet:param name="mvcPath" value="/view_container_model.jsp" />
				<portlet:param name="classNameId" value="<%= String.valueOf(trashEntry.getClassNameId()) %>" />
				<portlet:param name="classPK" value="<%= String.valueOf(trashEntry.getClassPK()) %>" />
				<portlet:param name="containerModelClassNameId" value="<%= String.valueOf(PortalUtil.getClassNameId(trashHandler.getContainerModelClassName(trashEntry.getClassPK()))) %>" />
			</portlet:renderURL>

			<%
			String taglibOnClick = liferayPortletResponse.getNamespace() + "restoreDialog('" + moveURL + "')";
			%>

			<liferay-ui:icon
				message="restore"
				onClick="<%= taglibOnClick %>"
				url="javascript:;"
			/>
		</c:when>
	</c:choose>

	<c:if test="<%= trashHandler.isDeletable(trashEntry.getClassPK()) %>">
		<portlet:actionURL name="deleteEntries" var="deleteEntryURL">
			<portlet:param name="redirect" value="<%= currentURL %>" />
			<portlet:param name="trashEntryId" value="<%= String.valueOf(trashEntry.getEntryId()) %>" />
		</portlet:actionURL>

		<liferay-ui:icon-delete
			url="<%= deleteEntryURL %>"
		/>
	</c:if>
</liferay-ui:icon-menu>