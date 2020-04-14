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

					<div class="sheet-section">
						<h3 class="sheet-subtitle"><%= LanguageUtil.get(request, "product-options") %></h3>

						<div class="row">
							<div class="col">
								<label class="control-label" for="skuId"><%= LanguageUtil.get(request, "sku") %></label>

								<div id="autocomplete-root"></div>
							</div>

							<div class="col-4">
								<aui:input name="quantity" wrapperCssClass="mb-0" />
							</div>

							<div class="align-items-end col-auto d-flex">
								<button class="btn btn-monospaced btn-secondary" id="remove-sku-button">
									<clay:icon
										symbol="trash"
									/>
								</div>
							</div>
						</div>
					</div>

					<%
					String cpInstanceId = "";
					String cpInstanceLabel = "";

					CPInstance cpInstance = cpDefinitionOptionValueRel.fetchCPInstance();

					if (cpInstance != null) {
						cpInstanceId = String.valueOf(cpInstance.getCPInstanceId());
						cpInstanceLabel = cpInstance.getSku();
					}
					%>

					<aui:script require="commerce-frontend-js/components/autocomplete/entry.es as autocomplete, commerce-frontend-js/utilities/eventsDefinitions.es as events">
						autocomplete.default('autocomplete', 'autocomplete-root', {
							apiUrl: '/o/headless-commerce-admin-catalog/v1.0/skus',
							initialLabel: '<%= cpInstanceLabel %>',
							initialValue: '<%= cpInstanceId %>',
							inputId: 'skuId',
							inputName: '<%= renderResponse.getNamespace() %>cpInstanceId',
							itemsKey: 'id',
							itemsLabel: 'sku'
						});

						Liferay.on(events.AUTOCOMPLETE_VALUE_UPDATED, function(e) {
							var quantityInput = document.getElementById('<%= renderResponse.getNamespace() %>quantity');
							var deleteButton = document.getElementById('remove-sku-button');

							if(e.value) {
								quantityInput.disabled = false;
								deleteButton.disabled = false;
							} else {
								quantityInput.disabled = true;
								deleteButton.disabled = true;
							}
						})
					</aui:script>
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

				<aui:button cssClass="btn-lg ml-3" type="submit" value="save" />
			</aui:form>
		</commerce-ui:side-panel-content>
	</c:otherwise>
</c:choose>