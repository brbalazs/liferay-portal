<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
UADDisplay uadDisplay = (UADDisplay)request.getAttribute(UADWebKeys.INFO_PANEL_UAD_DISPLAY);
List<UADEntity> uadEntities = (List<UADEntity>)request.getAttribute(UADWebKeys.INFO_PANEL_UAD_ENTITIES);
%>

<div class="sidebar sidebar-light">
	<c:choose>
		<c:when test="<%= ListUtil.isEmpty(uadEntities) %>">
			<div class="sidebar-header">
				<h3 class="sidebar-title"><%= uadDisplay.getTypeName(locale) %></h3>

				<h5 class="sidebar-subtitle"><%= UADLanguageUtil.getApplicationName(uadDisplay, locale) %></h5>
			</div>
		</c:when>
		<c:when test="<%= ListUtil.isNotEmpty(uadEntities) && (uadEntities.size() == 1) %>">

			<%
			UADEntity uadEntity = uadEntities.get(0);

			Serializable primaryKey = uadEntity.getPrimaryKey();

			Map<String, Object> displayValues = uadDisplay.getFieldValues(uadEntity.getEntity(), uadDisplay.getDisplayFieldNames());

			String identifierFieldName = uadDisplay.getDisplayFieldNames()[0];
			%>

			<div class="sidebar-header">
				<ul class="sidebar-header-actions">
					<li>
						<%@ include file="/single_entity_action_menu.jspf" %>
					</li>
				</ul>

				<h3 class="sidebar-title"><%= SafeDisplayValueUtil.get(displayValues.get(identifierFieldName)) %></h3>

				<h5 class="sidebar-subtitle"><%= uadDisplay.getTypeName(locale) %></h5>
			</div>

			<div class="sidebar-body">
				<dl class="sidebar-block sidebar-dl sidebar-section">
					<dt class="sidebar-dt"><%= LanguageUtil.get(request, "primary-key") %></dt>
					<dd class="sidebar-dd"><%= primaryKey %></dd>

					<%
					displayValues = new TreeMap<>(displayValues);

					for (Map.Entry<String, Object> entry : displayValues.entrySet()) {
						if (identifierFieldName.equals(entry.getKey())) {
							continue;
						}
					%>

						<dt class="sidebar-dt"><%= entry.getKey() %></dt>
						<dd class="sidebar-dd"><%= SafeDisplayValueUtil.get(entry.getValue()) %></dd>

					<%
					}
					%>

				</dl>
			</div>
		</c:when>
		<c:when test="<%= ListUtil.isNotEmpty(uadEntities) && (uadEntities.size() > 1) %>">
			<div class="sidebar-header">
				<h3 class="sidebar-title"><%= uadDisplay.getTypeName(locale) %></h3>

				<h5 class="sidebar-subtitle"><liferay-ui:message arguments="<%= uadEntities.size() %>" key="x-items-are-selected" /></h5>
			</div>
		</c:when>
	</c:choose>
</div>