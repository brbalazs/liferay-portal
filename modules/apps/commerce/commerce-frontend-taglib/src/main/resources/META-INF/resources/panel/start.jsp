<%--
/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
--%>

<%@ include file="/panel/init.jsp" %>

<%
String cardCssClasses = "card d-flex" + (Validator.isNotNull(elementClasses) ? StringPool.SPACE + elementClasses : StringPool.BLANK);
String bodyCssClasses = "card-body" + (Validator.isNotNull(bodyClasses) ? StringPool.SPACE + bodyClasses : StringPool.BLANK);
%>

<div class="<%= cardCssClasses %>">
	<c:if test="<%= Validator.isNotNull(actionLabel) || Validator.isNotNull(actionIcon) || Validator.isNotNull(title) %>">
		<h4 class="align-items-center card-header d-flex justify-content-between py-3">
			<%= title %>

			<c:if test="<%= Validator.isNotNull(actionTargetId) %>">
				<aui:script require="commerce-frontend-js/utilities/eventsDefinitions.es as eventsDefinitions">
					const link = document.getElementById('<%= linkId %>');

					if (link) {
						link.addEventListener('click', function(e) {
							e.preventDefault();
							Liferay.fire(eventsDefinitions.OPEN, {
								id: '<%= actionTargetId %>'
							});
						});
					}
				</aui:script>
			</c:if>

			<c:choose>
				<c:when test="<%= Validator.isNotNull(actionLabel) %>">
					<clay:link
						href='<%= Validator.isNotNull(actionUrl) ? actionUrl : "#" %>'
						id="<%= linkId %>"
						label="<%= actionLabel %>"
					/>
				</c:when>
				<c:when test="<%= Validator.isNotNull(actionIcon) %>">
					<clay:link
						elementClasses="btn btn-monospaced btn-primary btn-sm text-white"
						href='<%= Validator.isNotNull(actionUrl) ? actionUrl : "#" %>'
						icon="<%= actionIcon %>"
						id="<%= linkId %>"
					/>
				</c:when>
				<c:when test="<%= collapsible || Validator.isNotNull(collapseLabel) || Validator.isNotNull(collapseSwitchName) %>">
					<span class="d-flex mr-n2">
						<c:if test="<%= Validator.isNotNull(collapseLabel) %>">
							<h5 class="mb-0 mr-3"><%= collapseLabel %></h5>
						</c:if>

						<label class="my-lg-n2 toggle-switch">
							<input
								aria-expanded="<%= !collapsed %>"
								<%= collapsed ? StringPool.BLANK : "checked" %>
								data-target="#<%= randomNamespace %>collapse"
								data-toggle="collapse"
								class="toggle-switch-check"
								<c:if test="<%= Validator.isNotNull(collapseSwitchName) %>">
									name="<%= collapseSwitchName %>"
								</c:if>
								type="checkbox"
							/>

							<span aria-hidden="true" class="toggle-switch-bar">
								<span class="toggle-switch-handle"></span>
							</span>
						</label>
					</span>
				</c:when>
			</c:choose>
		</h4>
	</c:if>

	<div class="collapse<%= collapsed ? StringPool.BLANK : StringPool.SPACE + "show" %>" id="<%= randomNamespace %>collapse">
		<div class="<%= bodyCssClasses %>">