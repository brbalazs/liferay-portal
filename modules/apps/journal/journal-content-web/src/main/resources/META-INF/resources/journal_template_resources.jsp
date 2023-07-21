<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
DDMTemplate ddmTemplate = journalContentDisplayContext.getDDMTemplate();

String ddmTemplateImageURL = ddmTemplate.getTemplateImageURL(themeDisplay);
%>

<div class="row">
	<div class="col-md-4">
		<liferay-frontend:horizontal-card
			text="<%= ddmTemplate.getName(locale) %>"
		>
			<liferay-frontend:horizontal-card-col>
				<c:choose>
					<c:when test="<%= Validator.isNotNull(ddmTemplateImageURL) %>">
						<img alt="" class="<%= Validator.isNotNull(ddmTemplateImageURL) ? "icon-monospaced" : StringPool.BLANK %>" src="<%= ddmTemplateImageURL %>" />
					</c:when>
					<c:otherwise>
						<liferay-frontend:horizontal-card-icon
							icon="edit-layout"
						/>
					</c:otherwise>
				</c:choose>
			</liferay-frontend:horizontal-card-col>
		</liferay-frontend:horizontal-card>
	</div>
</div>