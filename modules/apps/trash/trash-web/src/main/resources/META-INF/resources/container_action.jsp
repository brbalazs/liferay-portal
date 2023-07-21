<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
TrashEntry trashEntry = trashDisplayContext.getTrashEntry();
TrashHandler trashHandler = trashDisplayContext.getTrashHandler();
TrashRenderer trashRenderer = trashDisplayContext.getTrashRenderer();
%>

<liferay-ui:icon-menu
	direction="left-side"
	icon="<%= StringPool.BLANK %>"
	markupView="lexicon"
	message="<%= StringPool.BLANK %>"
	showWhenSingleIcon="<%= true %>"
>
	<c:choose>
		<c:when test="<%= trashEntry != null %>">
			<c:choose>
				<c:when test="<%= trashHandler.isRestorable(trashEntry.getClassPK()) && !trashHandler.isInTrashContainer(trashEntry.getClassPK()) %>">
					<portlet:actionURL name="restoreEntries" var="restoreEntryURL">
						<portlet:param name="redirect" value="<%= trashDisplayContext.getViewContentRedirectURL() %>" />
						<portlet:param name="trashEntryId" value="<%= String.valueOf(trashEntry.getEntryId()) %>" />
					</portlet:actionURL>

					<liferay-ui:icon
						id="restoreEntryButton"
						message="restore"
						url="<%= restoreEntryURL.toString() %>"
					/>
				</c:when>
				<c:when test="<%= !trashHandler.isRestorable(trashEntry.getClassPK()) && trashHandler.isMovable(trashEntry.getClassPK()) %>">

					<%
					String trashHandlerEntryContainerModelClassName = trashHandler.getContainerModelClassName(trashEntry.getClassPK());
					%>

					<portlet:renderURL var="moveURL" windowState="<%= LiferayWindowState.POP_UP.toString() %>">
						<portlet:param name="mvcPath" value="/view_container_model.jsp" />
						<portlet:param name="classNameId" value="<%= String.valueOf(trashEntry.getClassNameId()) %>" />
						<portlet:param name="classPK" value="<%= String.valueOf(trashEntry.getClassPK()) %>" />
						<portlet:param name="containerModelClassNameId" value="<%= String.valueOf(PortalUtil.getClassNameId(trashHandlerEntryContainerModelClassName)) %>" />
					</portlet:renderURL>

					<%
					String taglibOnClick = renderResponse.getNamespace() + "restoreDialog('" + moveURL + "')";
					%>

					<liferay-ui:icon
						id="restoreEntryButton"
						message="restore"
						onClick="<%= taglibOnClick %>"
						url="javascript:;"
					/>
				</c:when>
			</c:choose>

			<c:if test="<%= trashHandler.isDeletable(trashRenderer.getClassPK()) %>">
				<portlet:actionURL name="deleteEntries" var="deleteEntryURL">
					<portlet:param name="redirect" value="<%= trashDisplayContext.getViewContentRedirectURL() %>" />
					<portlet:param name="trashEntryId" value="<%= String.valueOf(trashEntry.getEntryId()) %>" />
				</portlet:actionURL>

				<%
				String taglibOnClick = "if (confirm('" + UnicodeLanguageUtil.get(request, "are-you-sure-you-want-to-delete-this") + "')) { submitForm(document.hrefFm, '" + deleteEntryURL.toString() + "'); }";
				%>

				<liferay-ui:icon
					id="removeEntryButton"
					message="delete"
					onClick="<%= taglibOnClick %>"
					url="javascript:;"
				/>
			</c:if>
		</c:when>
		<c:otherwise>
			<c:if test="<%= trashHandler.isMovable(trashRenderer.getClassPK()) %>">

				<%
				String containerModelClassName = trashHandler.getContainerModelClassName(trashDisplayContext.getClassPK());

				long trashRendererClassNameId = PortalUtil.getClassNameId(trashRenderer.getClassName());
				%>

				<portlet:renderURL var="moveURL" windowState="<%= LiferayWindowState.POP_UP.toString() %>">
					<portlet:param name="mvcPath" value="/view_container_model.jsp" />
					<portlet:param name="classNameId" value="<%= String.valueOf(trashRendererClassNameId) %>" />
					<portlet:param name="classPK" value="<%= String.valueOf(trashRenderer.getClassPK()) %>" />
					<portlet:param name="containerModelClassNameId" value="<%= String.valueOf(PortalUtil.getClassNameId(containerModelClassName)) %>" />
				</portlet:renderURL>

				<%
				String taglibOnClick = renderResponse.getNamespace() + "restoreDialog('" + moveURL + "')";
				%>

				<liferay-ui:icon
					id="moveEntryButton"
					message="restore"
					onClick="<%= taglibOnClick %>"
					url="javascript:;"
				/>
			</c:if>

			<c:if test="<%= trashHandler.isDeletable(trashRenderer.getClassPK()) %>">
				<portlet:actionURL name="deleteEntries" var="deleteEntryURL">
					<portlet:param name="redirect" value="<%= trashDisplayContext.getViewContentRedirectURL() %>" />
					<portlet:param name="className" value="<%= trashRenderer.getClassName() %>" />
					<portlet:param name="classPK" value="<%= String.valueOf(trashRenderer.getClassPK()) %>" />
				</portlet:actionURL>

				<%
				String taglibOnClick = "if (confirm('" + UnicodeLanguageUtil.get(request, "are-you-sure-you-want-to-delete-this") + "')) { submitForm(document.hrefFm, '" + deleteEntryURL.toString() + "'); }";
				%>

				<liferay-ui:icon
					id="removeEntryButton"
					message="delete"
					onClick="<%= taglibOnClick %>"
					url="javascript:;"
				/>
			</c:if>
		</c:otherwise>
	</c:choose>
</liferay-ui:icon-menu>