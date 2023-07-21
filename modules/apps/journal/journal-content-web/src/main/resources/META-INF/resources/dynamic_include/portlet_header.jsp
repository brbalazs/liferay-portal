<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/dynamic_include/init.jsp" %>

<%
JournalArticle article = journalContentDisplayContext.getArticle();
%>

<div class="visible-interaction">
	<liferay-ui:icon-menu
		direction="left-side"
		icon="<%= StringPool.BLANK %>"
		markupView="lexicon"
		message="web-content-options"
		showWhenSingleIcon="<%= true %>"
	>
		<c:if test="<%= journalContentDisplayContext.isShowEditArticleIcon() %>">

			<%
			JournalArticle latestArticle = journalContentDisplayContext.getLatestArticle();

			Map<String, Object> data = new HashMap<String, Object>();

			data.put("destroyOnHide", true);
			data.put("id", HtmlUtil.escape(portletDisplay.getNamespace()) + "editAsset");
			data.put("title", HtmlUtil.escape(latestArticle.getTitle(locale)));
			%>

			<liferay-ui:icon
				data="<%= data %>"
				id="editWebContentIcon"
				message="edit-web-content"
				url="<%= journalContentDisplayContext.getURLEdit() %>"
				useDialog="<%= true %>"
			/>
		</c:if>

		<c:if test="<%= journalContentDisplayContext.isShowEditTemplateIcon() %>">

			<%
			DDMTemplate ddmTemplate = journalContentDisplayContext.getDDMTemplate();

			Map<String, Object> data = new HashMap<String, Object>();

			data.put("destroyOnHide", true);
			data.put("id", HtmlUtil.escape(portletDisplay.getNamespace()) + "editAsset");
			data.put("title", HtmlUtil.escape(ddmTemplate.getName(locale)));
			%>

			<liferay-ui:icon
				data="<%= data %>"
				id="editTemplateIcon"
				message="edit-template"
				url="<%= journalContentDisplayContext.getURLEditTemplate() %>"
				useDialog="<%= true %>"
			/>
		</c:if>

		<c:if test="<%= JournalArticlePermission.contains(permissionChecker, article, ActionKeys.PERMISSIONS) %>">
			<liferay-security:permissionsURL
				modelResource="<%= JournalArticle.class.getName() %>"
				modelResourceDescription="<%= HtmlUtil.escape(article.getTitle(locale)) %>"
				resourcePrimKey="<%= String.valueOf(article.getResourcePrimKey()) %>"
				var="permissionsURL"
				windowState="<%= LiferayWindowState.POP_UP.toString() %>"
			/>

			<liferay-ui:icon
				message="permissions"
				method="get"
				url="<%= permissionsURL %>"
				useDialog="<%= true %>"
			/>
		</c:if>

		<c:if test="<%= JournalArticlePermission.contains(permissionChecker, article, ActionKeys.UPDATE) %>">

			<%
			JournalArticle latestArticle = journalContentDisplayContext.getLatestArticle();

			Map<String, Object> data = new HashMap<String, Object>();

			data.put("destroyOnHide", true);
			data.put("id", HtmlUtil.escape(portletDisplay.getNamespace()) + "editAsset");
			data.put("title", HtmlUtil.escape(latestArticle.getTitle(locale)));
			%>

			<liferay-ui:icon
				data="<%= data %>"
				id="basicViewHistoryIcon"
				message="view-history"
				url="<%= journalContentDisplayContext.getURLViewHistory() %>"
				useDialog="<%= true %>"
			/>
		</c:if>
	</liferay-ui:icon-menu>
</div>