<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
String redirect = ParamUtil.getString(request, "redirect");

if (Validator.isNull(redirect)) {
	redirect = trashDisplayContext.getContainerModelRedirectURL();
}

ResultRow row = (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);

String className = null;
long classPK = 0;

if ((row != null) && (row.getObject() instanceof TrashedModel)) {
	TrashedModel trashedModel = (TrashedModel)row.getObject();

	className = ((ClassedModel)trashedModel).getModelClassName();
	classPK = trashedModel.getTrashEntryClassPK();
}
else {
	className = (String)request.getAttribute("view.jsp-className");
	classPK = GetterUtil.getLong(request.getAttribute("view.jsp-classPK"));
}

TrashHandler trashHandler = TrashHandlerRegistryUtil.getTrashHandler(className);
%>

<liferay-ui:icon-menu
	direction="left-side"
	icon="<%= StringPool.BLANK %>"
	markupView="lexicon"
	message="<%= StringPool.BLANK %>"
	showWhenSingleIcon="<%= true %>"
>
	<c:if test="<%= trashHandler.isMovable(classPK) %>">
		<portlet:renderURL var="moveURL" windowState="<%= LiferayWindowState.POP_UP.toString() %>">
			<portlet:param name="mvcPath" value="/view_container_model.jsp" />
			<portlet:param name="redirect" value="<%= redirect %>" />
			<portlet:param name="classNameId" value="<%= String.valueOf(PortalUtil.getClassNameId(className)) %>" />
			<portlet:param name="classPK" value="<%= String.valueOf(classPK) %>" />
			<portlet:param name="containerModelClassNameId" value="<%= String.valueOf(PortalUtil.getClassNameId(trashHandler.getContainerModelClassName(classPK))) %>" />
		</portlet:renderURL>

		<%
		String taglibOnClick = renderResponse.getNamespace() + "restoreDialog('" + moveURL + "')";
		%>

		<liferay-ui:icon
			message="restore"
			onClick="<%= taglibOnClick %>"
			url="javascript:;"
		/>
	</c:if>

	<c:if test="<%= trashHandler.isDeletable(classPK) %>">
		<portlet:actionURL name="deleteEntries" var="deleteEntryURL">
			<portlet:param name="redirect" value="<%= currentURL %>" />
			<portlet:param name="className" value="<%= className %>" />
			<portlet:param name="classPK" value="<%= String.valueOf(classPK) %>" />
		</portlet:actionURL>

		<liferay-ui:icon-delete
			url="<%= deleteEntryURL %>"
		/>
	</c:if>
</liferay-ui:icon-menu>