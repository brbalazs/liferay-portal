<%@ page import="com.liferay.commerce.product.content.util.CPContentHelper" %>
<%@ page import="com.liferay.commerce.product.catalog.CPCatalogEntry" %>
<%@ page import="com.liferay.commerce.product.catalog.CPSku" %>
<%@ page
		import="com.liferay.commerce.product.content.constants.CPContentWebKeys" %>
<%@ page import="com.liferay.portal.kernel.language.LanguageUtil" %>
<%@ page import="com.liferay.petra.string.StringPool" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.liferay.portal.kernel.util.ArrayUtil" %>
<%@ page
		import="com.liferay.commerce.product.model.CPDefinitionSpecificationOptionValue" %>
<%@ page import="com.liferay.commerce.product.model.CPOptionCategory" %>
<%@ page import="com.liferay.commerce.product.model.CPAttachmentFileEntry" %>
<%@ page import="com.liferay.portal.kernel.util.Validator" %>
<%@ page import="com.liferay.commerce.product.model.CPSpecificationOption" %>
<%@ page import="com.liferay.portal.kernel.repository.model.FileEntry" %><%--
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
CPContentHelper cpContentHelper = (CPContentHelper)request.getAttribute(CPContentWebKeys.CP_CONTENT_HELPER);

CPCatalogEntry cpCatalogEntry = cpContentHelper.getCPCatalogEntry(request);
CPSku cpSku = cpContentHelper.getDefaultCPSku(cpCatalogEntry);

long cpDefinitionId = cpCatalogEntry.getCPDefinitionId();
%>

<div class="container-fluid product-detail" id="<portlet:namespace /><%= cpDefinitionId %>ProductContent">
	<div class="row">
		<div id="minium-product-gallery" class="col-6">
			<%
				Map<String, Object> context = new HashMap<>();
				context.put("selected", 0);
				context.put("images", cpContentHelper.getImages(cpDefinitionId, themeDisplay));
			%>

			<soy:template-renderer
				context="<%= context %>"
				module="commerce-theme-minium-impl@1.0.0/product_gallery/MiniumProductGallery.es"
				templateNamespace="MiniumProductGallery.render"
			/>
		</div>
		<div class="col-6">
			<header class="minium-product-header">
				<div class="minium-dot minium-dot--good">In Stock</div>
				<h3 class="minium-product-header__tagline <%= (cpSku == null) ? "hide" : StringPool.BLANK %>" data-text-cp-instance-sku-show>
					<span data-text-cp-instance-sku><%= (cpSku == null) ? StringPool.BLANK : cpSku.getSku() %></span>
				</h3>
				<h2 class="minium-product-header__title"><%= cpCatalogEntry.getName() %></h2>
				<h4 class="minium-product-header__subtitle <%= (cpSku == null) ? "hide" : StringPool.BLANK %>" data-text-cp-instance-manufacturer-part-number-show>
					<span data-text-cp-instance-manufacturer-part-number><%= (cpSku == null) ? StringPool.BLANK : cpSku.getManufacturerPartNumber() %></span>
				</h4>
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
					<liferay-commerce:price
						CPDefinitionId="<%= cpDefinitionId %>"
						CPInstanceId="<%= cpSku.getCPInstanceId() %>"
						discountLabel="<%= LanguageUtil.get(request, "you-save") %>"
						promoPriceLabel="<%= LanguageUtil.get(request, "was") %>"
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

				<div class="autofit-col commerce-quantity-input">
					<liferay-commerce:quantity-input
							CPDefinitionId="<%= cpDefinitionId %>"
							useSelect="<%= false %>"
					/>
				</div>

				<div class="autofit-col">
					<liferay-commerce-cart:add-to-cart
							CPDefinitionId="<%= cpDefinitionId %>"
							CPInstanceId="<%= (cpSku == null) ? 0 : cpSku.getCPInstanceId() %>"
							elementClasses="btn-primary text-truncate"
							productContentId='<%= renderResponse.getNamespace() + cpDefinitionId + "ProductContent" %>'
							taglibQuantityInputId='<%= renderResponse.getNamespace() + cpDefinitionId + "Quantity" %>'
					/>
				</div>

			</div>
			<div class="autofit-float autofit-row">
				<liferay-commerce:compare-product CPDefinitionId="<%= cpDefinitionId %>" />
			</div>
		</div>
	</div>
</div>

<%
List<CPDefinitionSpecificationOptionValue> cpDefinitionSpecificationOptionValues = cpContentHelper.getCPDefinitionSpecificationOptionValues(cpDefinitionId);
List<CPOptionCategory> cpOptionCategories = cpContentHelper.getCPOptionCategories(scopeGroupId);
List<CPAttachmentFileEntry> cpAttachmentFileEntries = cpContentHelper.getCPAttachmentFileEntries(cpDefinitionId);
%>

<div class="row">
	<div class="col">

	</div>
</div>


<div class="product-detail-description">
	<div class="container-fluid container-fluid-max-xl">
		<ul class="nav nav-underline product-detail-description-tabs" role="tablist">

			<c:if test="<%= cpContentHelper.hasCPDefinitionSpecificationOptionValues(cpDefinitionId) %>">
				<li class="nav-item" role="presentation">
					<a aria-controls="<portlet:namespace />specifications" aria-expanded="false" class="nav-link" data-toggle="tab" href="#<portlet:namespace />specifications" role="tab">
						<%= LanguageUtil.get(resourceBundle, "specifications") %>
					</a>
				</li>
			</c:if>

			<c:if test="<%= !cpAttachmentFileEntries.isEmpty() %>">
				<li class="nav-item" role="presentation">
					<a aria-controls="<portlet:namespace />attachments" aria-expanded="false" class="nav-link" data-toggle="tab" href="#<portlet:namespace />attachments" role="tab">
						<%= LanguageUtil.get(resourceBundle, "attachments") %>
					</a>
				</li>
			</c:if>
		</ul>

		<div class="tab-content">


			<c:if test="<%= cpContentHelper.hasCPDefinitionSpecificationOptionValues(cpDefinitionId) %>">
				<div class="fade tab-pane" id="<portlet:namespace />specifications">
					<c:if test="<%= !cpDefinitionSpecificationOptionValues.isEmpty() %>">
						<dl class="autofit-float autofit-row autofit-row-center specification-list">

							<%
								for (CPDefinitionSpecificationOptionValue cpDefinitionSpecificationOptionValue : cpDefinitionSpecificationOptionValues) {
									CPSpecificationOption cpSpecificationOption = cpDefinitionSpecificationOptionValue.getCPSpecificationOption();
							%>

							<dt class="autofit-col specification-term">
								<%= cpSpecificationOption.getTitle(languageId) %>
							</dt>
							<dd class="autofit-col specification-desc">
								<%= cpDefinitionSpecificationOptionValue.getValue(languageId) %>
							</dd>

							<%
								}
							%>

						</dl>
					</c:if>

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

							<dt class="autofit-col specification-term">
								<%= cpSpecificationOption.getTitle(languageId) %>
							</dt>
							<dd class="autofit-col specification-desc">
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
			</c:if>

			<c:if test="<%= !cpAttachmentFileEntries.isEmpty() %>">
				<div class="fade tab-pane" id="<portlet:namespace />attachments">
					<dl class="autofit-float autofit-row autofit-row-center specification-list">

						<%
							int attachmentsCount = 0;

							for (CPAttachmentFileEntry curCPAttachmentFileEntry : cpAttachmentFileEntries) {
								FileEntry fileEntry = curCPAttachmentFileEntry.getFileEntry();
						%>

						<dt class="autofit-col specification-term">
							<%= curCPAttachmentFileEntry.getTitle(themeDisplay.getLanguageId()) %>
						</dt>
						<dd class="autofit-col specification-desc">
							<aui:icon cssClass="icon-monospaced" image="download" markupView="lexicon" url="<%= cpContentHelper.getDownloadFileEntryURL(fileEntry, themeDisplay) %>" />
						</dd>

						<%
							attachmentsCount = attachmentsCount + 1;

							if (attachmentsCount >= 2) {
						%>

						<dt class="autofit-col specification-empty specification-term"></dt>
						<dd class="autofit-col specification-desc specification-empty"></dd>

						<%
								attachmentsCount = 0;
							}
						%>

						<%
							}
						%>

					</dl>
				</div>
			</c:if>
		</div>
	</div>
</div>
