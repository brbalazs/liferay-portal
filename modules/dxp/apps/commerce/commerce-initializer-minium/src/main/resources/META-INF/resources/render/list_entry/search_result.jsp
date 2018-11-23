<%--
/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */
--%>

<%@ include file="/render/init.jsp" %>

<%
CPContentHelper cpContentHelper = (CPContentHelper)request.getAttribute(CPContentWebKeys.CP_CONTENT_HELPER);

CPCatalogEntry cpCatalogEntry = cpContentHelper.getCPCatalogEntry(request);
CPSku cpSku = cpContentHelper.getDefaultCPSku(cpCatalogEntry);

long cpDefinitionId = cpCatalogEntry.getCPDefinitionId();
List<CPSku> cpSkus = cpCatalogEntry.getCPSkus();

String quantityInputId = renderResponse.getNamespace() + cpDefinitionId + "Quantity";
%>

<div class="catalog-card-page col-lg-6 col-md-6 col-sm-6 col-xl-3 col-xs-12">
	<div class="card">
		<div class="product-expand">
			<c:if test="<%= cpSku != null %>">
				<p class="card-subtitle product-sku">
					<liferay-ui:message arguments="<%= cpSku.getSku() %>" key="sku-x" translateArguments="<%= false %>" />
				</p>
			</c:if>

			<div class="autofit-row product-description">
				<div class="autofit-col autofit-col-expand">
					<div class="card-title">
						<a href="<%= cpContentHelper.getFriendlyURL(cpCatalogEntry, themeDisplay) %>"> <%= cpCatalogEntry.getName() %> </a>
					</div>
				</div>

				<div class="autofit-col">
					<span class="sticker sticker-xl">
						<span class="sticker-overlay">
							<img class="product-image sticker-img" src="<%= cpCatalogEntry.getDefaultImageFileUrl() %>">
						</span>
					</span>
				</div>
			</div>
		</div>

		<div class="product-expand">
			<div class="product-price">
				<span class="commerce-price">
					<liferay-commerce:price CPDefinitionId="<%= cpDefinitionId %>" discountLabel="<%= LanguageUtil.get(request, "you-save") %>" />
				</span>
			</div>
		</div>

		<div class="product-footer">
			<div class="product-actions">
				<c:if test="<%= cpCatalogEntry.isIgnoreSKUCombinations() %>">
					<div class="autofit-row">
						<div class="autofit-col">
							<liferay-commerce:quantity-input
								CPDefinitionId="<%= cpDefinitionId %>"
								useSelect="<%= false %>"
							/>
						</div>

						<div class="autofit-col autofit-col-expand">
							<liferay-commerce-cart:add-to-cart
								CPDefinitionId="<%= cpDefinitionId %>"
								CPInstanceId="<%= (cpSku == null) ? 0 : cpSku.getCPInstanceId() %>"
								elementClasses="btn-block btn-primary text-truncate"
								taglibQuantityInputId="<%= quantityInputId %>"
							/>
						</div>
					</div>
				</c:if>

				<c:if test="<%= !cpCatalogEntry.isIgnoreSKUCombinations() %>">
					<a class="btn btn-block btn-outline-primary text-truncate" href="<%= cpContentHelper.getFriendlyURL(cpCatalogEntry, themeDisplay) %>"><liferay-ui:message key="view-all-variants" /> </a>
				</c:if>
			</div>

			<div class="product-subactions">
				<c:choose>
					<c:when test="<%= cpCatalogEntry.isIgnoreSKUCombinations() %>">
						<div class="autofit-row">
							<div class="autofit-col autofit-col-expand">
								<div class="product-list-info-compare">
									<liferay-commerce:compare-product CPDefinitionId="<%= cpDefinitionId %>" />
								</div>
							</div>
						</div>
					</c:when>
					<c:otherwise>
						<div class="autofit-row">
							<div class="autofit-col autofit-col-expand">
								<div class="autofit-section">
									<span class="available-variants"><%= cpSkus.size() %> <liferay-ui:message key="variants-available" /></span>
								</div>
							</div>
						</div>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</div>
</div>