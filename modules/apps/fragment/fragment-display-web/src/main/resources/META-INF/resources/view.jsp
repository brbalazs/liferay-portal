<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
FragmentEntryLink fragmentEntryLink = fragmentEntryDisplayContext.getFragmentEntryLink();

if (fragmentEntryLink == null) {
	renderRequest.setAttribute(WebKeys.PORTLET_CONFIGURATOR_VISIBILITY, Boolean.TRUE);
}
%>

<c:choose>
	<c:when test="<%= fragmentEntryLink == null %>">
		<div class="alert alert-info text-center">
			<div>
				<liferay-ui:message key="this-application-is-not-visible-to-users-yet" />
			</div>

			<c:if test="<%= fragmentEntryDisplayContext.isShowConfigurationLink() %>">
				<div>
					<aui:a href="javascript:;" onClick="<%= portletDisplay.getURLConfigurationJS() %>"><liferay-ui:message key="select-fragment-entry-to-make-it-visible" /></aui:a>
				</div>
			</c:if>
		</div>
	</c:when>
	<c:otherwise>
		<c:choose>
			<c:when test="<%= fragmentEntryDisplayContext.hasEditPermission() %>">
				<liferay-editor:resources
					editorName="alloyeditor"
				/>

				<soy:component-renderer
					context="<%= fragmentEntryDisplayContext.getSoyContext() %>"
					module="js/FragmentEntryDisplay.es"
					templateNamespace="com.liferay.fragment.display.web.FragmentEntryDisplay.render"
				/>
			</c:when>
			<c:otherwise>
				<%= FragmentEntryRenderUtil.renderFragmentEntryLink(fragmentEntryLink, request, response) %>
			</c:otherwise>
		</c:choose>
	</c:otherwise>
</c:choose>