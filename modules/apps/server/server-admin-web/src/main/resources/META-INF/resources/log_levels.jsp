<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
String[] tab2Names = {"update-categories", "add-category"};

if (!ArrayUtil.contains(tab2Names, tabs2)) {
	tabs2 = tab2Names[0];
}

String keywords = ParamUtil.getString(request, "keywords");

PortletURL serverURL = renderResponse.createRenderURL();

serverURL.setParameter("mvcRenderCommandName", "/server_admin/view");
serverURL.setParameter("tabs1", tabs1);
serverURL.setParameter("tabs2", tabs2);
%>

<div class="server-admin-tabs">
	<aui:nav-bar cssClass="collapse-basic-search" markupView="lexicon">
		<aui:nav cssClass="navbar-nav">

			<%
			for (String tab2Name : tab2Names) {
				serverURL.setParameter("tabs2", tab2Name);
			%>

				<aui:nav-item href="<%= serverURL.toString() %>" label="<%= tab2Name %>" selected="<%= tabs2.equals(tab2Name) %>" />

			<%
			}

			serverURL.setParameter("tabs2", tabs2);
			%>

		</aui:nav>

		<c:if test='<%= tabs2.equals("update-categories") %>'>
			<aui:nav-bar-search>
				<liferay-ui:input-search
					autoFocus="<%= windowState.equals(WindowState.MAXIMIZED) %>"
					markupView="lexicon"
					placeholder='<%= LanguageUtil.get(request, "keywords") %>'
					title='<%= LanguageUtil.get(request, "search-categories") %>'
				/>
			</aui:nav-bar-search>
		</c:if>
	</aui:nav-bar>

	<c:choose>
		<c:when test='<%= tabs2.equals("add-category") %>'>
			<aui:fieldset>
				<aui:input cssClass="lfr-input-text-container" label="logger-name" name="loggerName" type="text" />

				<aui:select label="log-level" name="priority">

					<%
					for (int i = 0; i < Levels.ALL_LEVELS.length; i++) {
					%>

						<aui:option label="<%= Levels.ALL_LEVELS[i] %>" selected="<%= Level.INFO.equals(Levels.ALL_LEVELS[i]) %>" />

					<%
					}
					%>

				</aui:select>
			</aui:fieldset>

			<aui:button-row>
				<aui:button cssClass="save-server-button" data-cmd="addLogLevel" value="save" />
			</aui:button-row>
		</c:when>
		<c:otherwise>

			<%
			Map currentLoggerNames = new TreeMap();

			Enumeration enu = LogManager.getCurrentLoggers();

			while (enu.hasMoreElements()) {
				Logger logger = (Logger)enu.nextElement();

				String name = logger.getName();

				if (Validator.isNull(keywords) || name.contains(keywords)) {
					currentLoggerNames.put(name, logger);
				}
			}

			List currentLoggerNamesList = ListUtil.fromCollection(currentLoggerNames.entrySet());

			Iterator itr = currentLoggerNamesList.iterator();

			while (itr.hasNext()) {
				Map.Entry entry = (Map.Entry)itr.next();

				Logger logger = (Logger)entry.getValue();

				Level level = logger.getLevel();

				if (level == null) {
					itr.remove();
				}
			}
			%>

			<liferay-ui:search-container
				iteratorURL="<%= serverURL %>"
				total="<%= currentLoggerNamesList.size() %>"
			>
				<liferay-ui:search-container-results
					results="<%= ListUtil.subList(currentLoggerNamesList, searchContainer.getStart(), searchContainer.getEnd()) %>"
				/>

				<liferay-ui:search-container-row
					className="java.util.Map.Entry"
					modelVar="entry"
				>

					<%
					String name = (String)entry.getKey();
					%>

					<liferay-ui:search-container-column-text
						name="category"
						value="<%= HtmlUtil.escape(name) %>"
					/>

					<liferay-ui:search-container-column-text
						name="level"
					>

						<%
						Logger logger = (Logger)entry.getValue();

						Level level = logger.getLevel();
						%>

						<select name="<%= renderResponse.getNamespace() %>logLevel<%= HtmlUtil.escapeAttribute(name) %>">

							<%
							for (int j = 0; j < Levels.ALL_LEVELS.length; j++) {
							%>

								<option <%= level.equals(Levels.ALL_LEVELS[j]) ? "selected" : StringPool.BLANK %> value="<%= Levels.ALL_LEVELS[j] %>"><%= Levels.ALL_LEVELS[j] %></option>

							<%
							}
							%>

						</select>
					</liferay-ui:search-container-column-text>
				</liferay-ui:search-container-row>

				<liferay-ui:search-iterator
					markupView="lexicon"
				/>
			</liferay-ui:search-container>

			<aui:button-row>
				<aui:button cssClass="save-server-button" data-cmd="updateLogLevels" value="save" />
			</aui:button-row>
		</c:otherwise>
	</c:choose>
</div>