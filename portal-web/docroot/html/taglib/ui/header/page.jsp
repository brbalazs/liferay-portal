<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/html/taglib/ui/header/init.jsp" %>

<%
if (Validator.isNull(backLabel)) {
	backLabel = LanguageUtil.get(resourceBundle, "back");
}

String headerTitle = localizeTitle ? LanguageUtil.get(resourceBundle, title) : title;
String message = escapeXml ? HtmlUtil.escape(backLabel) : backLabel;
%>

<div class="taglib-header <%= cssClass %>">
	<c:if test="<%= showBackURL && Validator.isNotNull(backURL) %>">
		<span class="header-back-to lfr-header-tooltip" title="<%= HtmlUtil.escapeAttribute(LanguageUtil.get(resourceBundle, HtmlUtil.stripHtml(message))) %>">
			<aui:a cssClass="lfr-icon-item taglib-icon" href="<%= backURL %>" id="<portlet:namespace />TabsBack" target="_self">
				<aui:icon image="angle-left" markupView="lexicon" />

				<span class="sr-only taglib-text"><%= LanguageUtil.get(resourceBundle, message) %></span>
			</aui:a>
		</span>
	</c:if>

	<aui:script use="aui-tooltip">
		new A.TooltipDelegate(
			{
				constrain: true,
				opacity: 1,
				trigger: '.lfr-header-tooltip',
				triggerHideEvent: ['click', 'mouseleave', 'MSPointerUp', 'touchend'],
				position: 'bottom',
				visible: false,
				zIndex: Liferay.zIndex.TOOLTIP
			}
		)
	</aui:script>

	<h3 class="header-title">
		<span>
			<c:choose>
				<c:when test="<%= escapeXml %>">
					<%= HtmlUtil.escape(headerTitle) %>
				</c:when>
				<c:otherwise>
					<%= headerTitle %>
				</c:otherwise>
			</c:choose>
		</span>
	</h3>
</div>