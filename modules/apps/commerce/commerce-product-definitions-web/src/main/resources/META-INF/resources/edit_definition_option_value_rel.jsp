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

<%@ include file="/init.jsp" %>

<%
CPDefinitionOptionValueRelDisplayContext cpDefinitionOptionValueRelDisplayContext = (CPDefinitionOptionValueRelDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

long cpDefinitionId = cpDefinitionOptionValueRelDisplayContext.getCPDefinitionId();
long cpDefinitionOptionRelId = cpDefinitionOptionValueRelDisplayContext.getCPDefinitionOptionRelId();
CPDefinitionOptionValueRel cpDefinitionOptionValueRel = cpDefinitionOptionValueRelDisplayContext.getCPDefinitionOptionValueRel();
long cpDefinitionOptionValueRelId = cpDefinitionOptionValueRelDisplayContext.getCPDefinitionOptionValueRelId();
%>

<portlet:actionURL name="editProductDefinitionOptionValueRel" var="editProductDefinitionOptionValueRelActionURL" />

<commerce-ui:side-panel-content
	title='<%= (cpDefinitionOptionValueRel == null) ? LanguageUtil.get(request, "add-value") : LanguageUtil.format(request, "edit-x", cpDefinitionOptionValueRel.getName(languageId), false) %>'
>
	<aui:form action="<%= editProductDefinitionOptionValueRelActionURL %>" method="post" name="cpDefinitionOptionValueRelfm">
		<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= (cpDefinitionOptionValueRel == null) ? Constants.ADD : Constants.UPDATE %>" />
		<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />
		<aui:input name="cpDefinitionId" type="hidden" value="<%= String.valueOf(cpDefinitionId) %>" />
		<aui:input name="cpDefinitionOptionRelId" type="hidden" value="<%= String.valueOf(cpDefinitionOptionRelId) %>" />
		<aui:input name="cpDefinitionOptionValueRelId" type="hidden" value="<%= String.valueOf(cpDefinitionOptionValueRelId) %>" />

		<aui:model-context bean="<%= cpDefinitionOptionValueRel %>" model="<%= CPDefinitionOptionValueRel.class %>" />

		<liferay-ui:error exception="<%= CPDefinitionOptionValueRelKeyException.class %>" message="that-key-is-already-being-used" />

		<commerce-ui:panel
			title='<%= LanguageUtil.get(request, "details") %>'
		>
			<aui:input id="optionValueName" name="name" />

			<aui:input name="priority" />

			<aui:input helpMessage="key-help" name="key" />
		</commerce-ui:panel>

		<c:if test="<%= cpDefinitionOptionValueRelDisplayContext.hasCustomAttributesAvailable() %>">
			<commerce-ui:panel
				title='<%= LanguageUtil.get(request, "custom-attribute") %>'
			>
				<liferay-expando:custom-attribute-list
					className="<%= CPDefinitionOptionValueRel.class.getName() %>"
					classPK="<%= (cpDefinitionOptionValueRel != null) ? cpDefinitionOptionValueRel.getCPDefinitionOptionValueRelId() : 0 %>"
					editable="<%= true %>"
					label="<%= true %>"
				/>
			</commerce-ui:panel>
		</c:if>

		<aui:button-row>
			<aui:button cssClass="btn-lg" type="submit" value="save" />

			<aui:button cssClass="btn-lg" type="cancel" />
		</aui:button-row>

		<c:if test="<%= cpDefinitionOptionValueRel == null %>">
			<aui:script require="commerce-frontend-js/utilities/index.es as utilities">
				function slugify(string) {
					return string.toLowerCase().replace(/[^a-z1-9]+/g, '-');
				}

				const form = document.getElementById(
					'<portlet:namespace />cpDefinitionOptionValueRelfm'
				);

				const keyInput = form.querySelector('#<portlet:namespace />key');
				const nameInput = form.querySelector('#<portlet:namespace />optionValueName');

				const debounce = utilities.debounce;

				var handleOnNameInput = function() {
					keyInput.value = slugify(nameInput.value);
				};

				nameInput.addEventListener('input', debounce(handleOnNameInput, 200));
			</aui:script>
		</c:if>
	</aui:form>
</commerce-ui:side-panel-content>