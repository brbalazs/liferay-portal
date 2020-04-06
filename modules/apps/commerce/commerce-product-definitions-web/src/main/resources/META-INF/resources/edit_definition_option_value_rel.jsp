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

CPDefinitionOptionValueRel cpDefinitionOptionValueRel = cpDefinitionOptionValueRelDisplayContext.getCPDefinitionOptionValueRel();

CPInstance cpInstance = cpDefinitionOptionValueRelDisplayContext.fetchCPInstance();

long cpInstanceId = 0;

if (cpInstance != null) {
	cpInstanceId = cpInstance.getCPInstanceId();
}
%>

<portlet:actionURL name="editProductDefinitionOptionValueRel" var="editProductDefinitionOptionValueRelActionURL" />

<c:choose>
	<c:when test="<%= cpDefinitionOptionValueRel == null %>">
		<commerce-ui:modal-content
			title='<%= LanguageUtil.get(request, "add-value") %>'
		>
			<aui:form action="<%= editProductDefinitionOptionValueRelActionURL %>" method="post" name="cpDefinitionOptionValueRelfm">
				<%@ include file="/edit_definition_option_value_rel.jspf" %>

				<c:if test="<%= cpDefinitionOptionValueRelDisplayContext.hasCustomAttributesAvailable() %>">
					<liferay-expando:custom-attribute-list
						className="<%= CPDefinitionOptionValueRel.class.getName() %>"
						classPK="<%= (cpDefinitionOptionValueRel != null) ? cpDefinitionOptionValueRel.getCPDefinitionOptionValueRelId() : 0 %>"
						editable="<%= true %>"
						label="<%= true %>"
					/>
				</c:if>
			</aui:form>
		</commerce-ui:modal-content>
	</c:when>
	<c:otherwise>
		<commerce-ui:side-panel-content
			title='<%= LanguageUtil.format(request, "edit-x", cpDefinitionOptionValueRel.getName(languageId), false) %>'
		>
			<aui:form action="<%= editProductDefinitionOptionValueRelActionURL %>" method="post" name="cpDefinitionOptionValueRelfm">
				<commerce-ui:panel
					title='<%= LanguageUtil.get(request, "details") %>'
				>
					<%@ include file="/edit_definition_option_value_rel.jspf" %>
				</commerce-ui:panel>

				<commerce-ui:panel
					title='<%= LanguageUtil.get(request, "linked-product") %>'
				>
					<aui:input id="cpInstanceId" label="product" name="cpInstanceId" type="number" value="<%= cpInstanceId %>" />

					<aui:input name="quantity" />

					<%
					CommerceCurrency commerceCurrency = cpDefinitionOptionValueRelDisplayContext.getCommerceCurrency();

					BigDecimal price = cpDefinitionOptionValueRel.getPrice();

					if (price == null) {
						price = BigDecimal.ZERO;
					}
					%>

					<aui:input name="price" suffix="<%= HtmlUtil.escape(commerceCurrency.getCode()) %>" type="text" value="<%= commerceCurrency.round(price) %>">
						<aui:validator name="min">0</aui:validator>
						<aui:validator name="number" />
					</aui:input>
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
				</aui:button-row>
			</aui:form>
		</commerce-ui:side-panel-content>
	</c:otherwise>
</c:choose>