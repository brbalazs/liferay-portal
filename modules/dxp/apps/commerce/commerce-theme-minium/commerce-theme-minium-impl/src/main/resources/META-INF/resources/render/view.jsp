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

<%@ include file="/init.jsp" %>

<%
CPContentHelper cpContentHelper = (CPContentHelper)request.getAttribute(CPContentWebKeys.CP_CONTENT_HELPER);

CPCatalogEntry cpCatalogEntry = cpContentHelper.getCPCatalogEntry(request);
CPSku cpSku = cpContentHelper.getDefaultCPSku(cpCatalogEntry);

long cpDefinitionId = cpCatalogEntry.getCPDefinitionId();
%>

<div class="container-fluid product-detail" id="<portlet:namespace /><%= cpDefinitionId %>ProductContent">
	<div class="row">
		<div class="col-6" id="minium-product-gallery">

			<%
			Map<String, Object> context = new HashMap<>();
			context.put("images", cpContentHelper.getImages(cpDefinitionId, themeDisplay));
			context.put("selected", 0);
			%>

			<soy:template-renderer
				context="<%= context %>"
				module="commerce-theme-minium-impl@1.0.7/product_gallery/MiniumProductGallery.es"
				templateNamespace="MiniumProductGallery.render"
			/>
		</div>

		<div class="col-6">
			<header class="minium-product-header">
				<commerce-ui:compare-checkbox
					componentId="compareCheckbox"
				/>

				<h3 class="minium-product-header__tagline <%= (cpSku == null) ? "hide" : StringPool.BLANK %>" data-text-cp-instance-sku-show>
					<span data-text-cp-instance-sku><%= (cpSku == null) ? StringPool.BLANK : cpSku.getSku() %></span>
				</h3>

				<h2 class="minium-product-header__title"><%= cpCatalogEntry.getName() %></h2>

				<h4 class="minium-product-header__subtitle <%= (cpSku == null) ? "hide" : StringPool.BLANK %>" data-text-cp-instance-manufacturer-part-number-show>
					<span data-text-cp-instance-manufacturer-part-number><%= (cpSku == null) ? StringPool.BLANK : cpSku.getManufacturerPartNumber() %></span>
				</h4>

				<c:choose>
					<c:when test="<%= cpSku != null %>">
						<div class="availability"><%= cpContentHelper.getAvailabilityLabel(request) %></div>

						<div class="availabilityEstimate"><%= cpContentHelper.getAvailabilityEstimateLabel(request) %></div>

						<div class="stockQuantity"><%= cpContentHelper.getStockQuantityLabel(request) %></div>
					</c:when>
					<c:otherwise>
						<div class="availability" data-text-cp-instance-availability=""></div>

						<div class="availabilityEstimate" data-text-cp-instance-availability-estimate=""></div>

						<div class="stockQuantity" data-text-cp-instance-stock-quantity=""></div>
					</c:otherwise>
				</c:choose>
			</header>

			<p><%= cpCatalogEntry.getDescription() %></p>

			<h4 class="commerce-subscription-info w-100" data-text-cp-instance-subscription-info data-text-cp-instance-subscription-info-show>
				<c:if test="<%= cpSku != null %>">
					<liferay-commerce:subscription-info
						CPInstanceId="<%= cpSku.getCPInstanceId() %>"
					/>
				</c:if>
			</h4>

			<div class="product-detail-options">
				<%= cpContentHelper.renderOptions(renderRequest, renderResponse) %>
			</div>

			<h2 class="commerce-price" data-text-cp-instance-price>
				<c:if test="<%= cpSku != null %>">
					<commerce-ui:price
						CPInstanceId="<%= cpSku.getCPInstanceId() %>"
					/>
				</c:if>
			</h2>

			<div>
				<c:if test="<%= cpSku != null %>">
					<liferay-commerce:tier-price
						CPInstanceId="<%= cpSku.getCPInstanceId() %>"
						taglibQuantityInputId='<%= renderResponse.getNamespace() + cpDefinitionId + "Quantity" %>'
					/>
				</c:if>
			</div>

			<div class="product-detail__actions">
				<div class="commerce-quantity-input">
					<liferay-commerce:quantity-input
						CPDefinitionId="<%= cpDefinitionId %>"
						useSelect="<%= false %>"
					/>
				</div>

				<div class="autofit-col">
					<liferay-commerce-cart:add-to-cart
						CPDefinitionId="<%= cpDefinitionId %>"
						CPInstanceId="<%= (cpSku == null) ? 0 : cpSku.getCPInstanceId() %>"
						elementClasses="minium-button minium-button--big"
						productContentId='<%= renderResponse.getNamespace() + cpDefinitionId + "ProductContent" %>'
						taglibQuantityInputId='<%= renderResponse.getNamespace() + cpDefinitionId + "Quantity" %>'
					/>
				</div>
			</div>
		</div>
	</div>
</div>

<%
List<CPDefinitionSpecificationOptionValue> cpDefinitionSpecificationOptionValues = cpContentHelper.getCPDefinitionSpecificationOptionValues(cpDefinitionId);
List<CPOptionCategory> cpOptionCategories = cpContentHelper.getCPOptionCategories(scopeGroupId);
List<CPMedia> cpAttachmentFileEntries = cpContentHelper.getCPAttachmentFileEntries(cpDefinitionId, themeDisplay);
%>

<c:if test="<%= cpContentHelper.hasCPDefinitionSpecificationOptionValues(cpDefinitionId) %>">
	<div class="row">
		<div class="col">
			<div class="minium-card">
				<div class="minium-card__title"><%= LanguageUtil.get(resourceBundle, "specifications") %></div>
				<div class="minium-card__content">
					<dl class="specification-list">

						<%
						for (CPDefinitionSpecificationOptionValue cpDefinitionSpecificationOptionValue : cpDefinitionSpecificationOptionValues) {
							CPSpecificationOption cpSpecificationOption = cpDefinitionSpecificationOptionValue.getCPSpecificationOption();
						%>

							<dt class="specification-term">
								<%= cpSpecificationOption.getTitle(languageId) %>
							</dt>
							<dd class="specification-desc">
								<%= cpDefinitionSpecificationOptionValue.getValue(languageId) %>
							</dd>

						<%
						}
						%>

					</dl>

					<%
					for (CPOptionCategory cpOptionCategory : cpOptionCategories) {
						List<CPDefinitionSpecificationOptionValue> categorizedCPDefinitionSpecificationOptionValues = cpContentHelper.getCategorizedCPDefinitionSpecificationOptionValues(cpDefinitionId, cpOptionCategory.getCPOptionCategoryId());
					%>

						<c:if test="<%= !categorizedCPDefinitionSpecificationOptionValues.isEmpty() %>">
							<dl class="autofit-float autofit-row autofit-row-center specification-list">

								<%
								for (CPDefinitionSpecificationOptionValue cpDefinitionSpecificationOptionValue : categorizedCPDefinitionSpecificationOptionValues) {
									CPSpecificationOption cpSpecificationOption = cpDefinitionSpecificationOptionValue.getCPSpecificationOption();
								%>

									<dt class="specification-term">
										<%= cpSpecificationOption.getTitle(languageId) %>
									</dt>
									<dd class="specification-desc">
										<%= cpDefinitionSpecificationOptionValue.getValue(languageId) %>
									</dd>

								<%
								}
								%>

							</dl>
						</c:if>

					<%
					}
					%>

				</div>
			</div>
		</div>
	</div>
</c:if>

<c:if test="<%= !cpAttachmentFileEntries.isEmpty() %>">
	<div class="row">
		<div class="col">
			<div class="minium-card">
				<div class="minium-card__title"><%= LanguageUtil.get(resourceBundle, "attachments") %></div>
				<div class="minium-card__content">
					<dl class="specification-list">

						<%
						int attachmentsCount = 0;

						for (CPMedia curCPAttachmentFileEntry : cpAttachmentFileEntries) {
						%>

							<dt class="specification-term">
								<%= curCPAttachmentFileEntry.getTitle() %>
							</dt>
							<dd class="specification-desc">
								<aui:icon cssClass="icon-monospaced" image="download" markupView="lexicon" target="_blank" url="<%= curCPAttachmentFileEntry.getDownloadUrl() %>" />
							</dd>

							<%
							attachmentsCount = attachmentsCount + 1;

							if (attachmentsCount >= 2) {
							%>

								<dt class="specification-empty specification-term"></dt>
								<dd class="specification-desc specification-empty"></dd>

						<%
								attachmentsCount = 0;
							}
						}
						%>

					</dl>
				</div>
			</div>
		</div>
	</div>
</c:if>