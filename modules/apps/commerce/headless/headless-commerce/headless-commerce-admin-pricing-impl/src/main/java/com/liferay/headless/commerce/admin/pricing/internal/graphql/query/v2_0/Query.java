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

package com.liferay.headless.commerce.admin.pricing.internal.graphql.query.v2_0;

import com.liferay.headless.commerce.admin.pricing.dto.v2_0.Discount;
import com.liferay.headless.commerce.admin.pricing.dto.v2_0.DiscountAccount;
import com.liferay.headless.commerce.admin.pricing.dto.v2_0.DiscountAccountGroup;
import com.liferay.headless.commerce.admin.pricing.dto.v2_0.DiscountCategory;
import com.liferay.headless.commerce.admin.pricing.dto.v2_0.DiscountChannel;
import com.liferay.headless.commerce.admin.pricing.dto.v2_0.DiscountProduct;
import com.liferay.headless.commerce.admin.pricing.dto.v2_0.DiscountProductGroup;
import com.liferay.headless.commerce.admin.pricing.dto.v2_0.DiscountRule;
import com.liferay.headless.commerce.admin.pricing.dto.v2_0.PriceEntry;
import com.liferay.headless.commerce.admin.pricing.dto.v2_0.PriceList;
import com.liferay.headless.commerce.admin.pricing.dto.v2_0.PriceListAccount;
import com.liferay.headless.commerce.admin.pricing.dto.v2_0.PriceListAccountGroup;
import com.liferay.headless.commerce.admin.pricing.dto.v2_0.PriceListChannel;
import com.liferay.headless.commerce.admin.pricing.dto.v2_0.PriceListDiscount;
import com.liferay.headless.commerce.admin.pricing.dto.v2_0.TierPrice;
import com.liferay.headless.commerce.admin.pricing.resource.v2_0.DiscountAccountGroupResource;
import com.liferay.headless.commerce.admin.pricing.resource.v2_0.DiscountAccountResource;
import com.liferay.headless.commerce.admin.pricing.resource.v2_0.DiscountCategoryResource;
import com.liferay.headless.commerce.admin.pricing.resource.v2_0.DiscountChannelResource;
import com.liferay.headless.commerce.admin.pricing.resource.v2_0.DiscountProductGroupResource;
import com.liferay.headless.commerce.admin.pricing.resource.v2_0.DiscountProductResource;
import com.liferay.headless.commerce.admin.pricing.resource.v2_0.DiscountResource;
import com.liferay.headless.commerce.admin.pricing.resource.v2_0.DiscountRuleResource;
import com.liferay.headless.commerce.admin.pricing.resource.v2_0.PriceEntryResource;
import com.liferay.headless.commerce.admin.pricing.resource.v2_0.PriceListAccountGroupResource;
import com.liferay.headless.commerce.admin.pricing.resource.v2_0.PriceListAccountResource;
import com.liferay.headless.commerce.admin.pricing.resource.v2_0.PriceListChannelResource;
import com.liferay.headless.commerce.admin.pricing.resource.v2_0.PriceListDiscountResource;
import com.liferay.headless.commerce.admin.pricing.resource.v2_0.PriceListResource;
import com.liferay.headless.commerce.admin.pricing.resource.v2_0.TierPriceResource;
import com.liferay.petra.function.UnsafeConsumer;
import com.liferay.petra.function.UnsafeFunction;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.vulcan.accept.language.AcceptLanguage;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLField;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLName;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLTypeExtension;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;

import java.util.Map;
import java.util.function.BiFunction;

import javax.annotation.Generated;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.ws.rs.core.UriInfo;

import org.osgi.service.component.ComponentServiceObjects;

/**
 * @author Zoltán Takács
 * @generated
 */
@Generated("")
public class Query {

	public static void setDiscountResourceComponentServiceObjects(
		ComponentServiceObjects<DiscountResource>
			discountResourceComponentServiceObjects) {

		_discountResourceComponentServiceObjects =
			discountResourceComponentServiceObjects;
	}

	public static void setDiscountAccountResourceComponentServiceObjects(
		ComponentServiceObjects<DiscountAccountResource>
			discountAccountResourceComponentServiceObjects) {

		_discountAccountResourceComponentServiceObjects =
			discountAccountResourceComponentServiceObjects;
	}

	public static void setDiscountAccountGroupResourceComponentServiceObjects(
		ComponentServiceObjects<DiscountAccountGroupResource>
			discountAccountGroupResourceComponentServiceObjects) {

		_discountAccountGroupResourceComponentServiceObjects =
			discountAccountGroupResourceComponentServiceObjects;
	}

	public static void setDiscountCategoryResourceComponentServiceObjects(
		ComponentServiceObjects<DiscountCategoryResource>
			discountCategoryResourceComponentServiceObjects) {

		_discountCategoryResourceComponentServiceObjects =
			discountCategoryResourceComponentServiceObjects;
	}

	public static void setDiscountChannelResourceComponentServiceObjects(
		ComponentServiceObjects<DiscountChannelResource>
			discountChannelResourceComponentServiceObjects) {

		_discountChannelResourceComponentServiceObjects =
			discountChannelResourceComponentServiceObjects;
	}

	public static void setDiscountProductResourceComponentServiceObjects(
		ComponentServiceObjects<DiscountProductResource>
			discountProductResourceComponentServiceObjects) {

		_discountProductResourceComponentServiceObjects =
			discountProductResourceComponentServiceObjects;
	}

	public static void setDiscountProductGroupResourceComponentServiceObjects(
		ComponentServiceObjects<DiscountProductGroupResource>
			discountProductGroupResourceComponentServiceObjects) {

		_discountProductGroupResourceComponentServiceObjects =
			discountProductGroupResourceComponentServiceObjects;
	}

	public static void setDiscountRuleResourceComponentServiceObjects(
		ComponentServiceObjects<DiscountRuleResource>
			discountRuleResourceComponentServiceObjects) {

		_discountRuleResourceComponentServiceObjects =
			discountRuleResourceComponentServiceObjects;
	}

	public static void setPriceEntryResourceComponentServiceObjects(
		ComponentServiceObjects<PriceEntryResource>
			priceEntryResourceComponentServiceObjects) {

		_priceEntryResourceComponentServiceObjects =
			priceEntryResourceComponentServiceObjects;
	}

	public static void setPriceListResourceComponentServiceObjects(
		ComponentServiceObjects<PriceListResource>
			priceListResourceComponentServiceObjects) {

		_priceListResourceComponentServiceObjects =
			priceListResourceComponentServiceObjects;
	}

	public static void setPriceListAccountResourceComponentServiceObjects(
		ComponentServiceObjects<PriceListAccountResource>
			priceListAccountResourceComponentServiceObjects) {

		_priceListAccountResourceComponentServiceObjects =
			priceListAccountResourceComponentServiceObjects;
	}

	public static void setPriceListAccountGroupResourceComponentServiceObjects(
		ComponentServiceObjects<PriceListAccountGroupResource>
			priceListAccountGroupResourceComponentServiceObjects) {

		_priceListAccountGroupResourceComponentServiceObjects =
			priceListAccountGroupResourceComponentServiceObjects;
	}

	public static void setPriceListChannelResourceComponentServiceObjects(
		ComponentServiceObjects<PriceListChannelResource>
			priceListChannelResourceComponentServiceObjects) {

		_priceListChannelResourceComponentServiceObjects =
			priceListChannelResourceComponentServiceObjects;
	}

	public static void setPriceListDiscountResourceComponentServiceObjects(
		ComponentServiceObjects<PriceListDiscountResource>
			priceListDiscountResourceComponentServiceObjects) {

		_priceListDiscountResourceComponentServiceObjects =
			priceListDiscountResourceComponentServiceObjects;
	}

	public static void setTierPriceResourceComponentServiceObjects(
		ComponentServiceObjects<TierPriceResource>
			tierPriceResourceComponentServiceObjects) {

		_tierPriceResourceComponentServiceObjects =
			tierPriceResourceComponentServiceObjects;
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {discounts(page: ___, pageSize: ___){items {__}, page, pageSize, totalCount}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField
	public DiscountPage discounts(
			@GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page)
		throws Exception {

		return _applyComponentServiceObjects(
			_discountResourceComponentServiceObjects,
			this::_populateResourceContext,
			discountResource -> new DiscountPage(
				discountResource.getDiscountsPage(
					Pagination.of(page, pageSize))));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {discountByExternalReferenceCode(externalReferenceCode: ___){accountGroups, accounts, active, categories, channels, couponCode, customFields, displayDate, expirationDate, externalReferenceCode, id, level, limitationTimes, limitationType, maximumDiscountAmount, neverExpire, numberOfUse, percentageLevel1, percentageLevel2, percentageLevel3, percentageLevel4, productGroups, products, rules, rulesConjuntion, target, title, useCouponCode, usePercentage}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField
	public Discount discountByExternalReferenceCode(
			@GraphQLName("externalReferenceCode") String externalReferenceCode)
		throws Exception {

		return _applyComponentServiceObjects(
			_discountResourceComponentServiceObjects,
			this::_populateResourceContext,
			discountResource ->
				discountResource.getDiscountByExternalReferenceCode(
					externalReferenceCode));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {discount(id: ___){accountGroups, accounts, active, categories, channels, couponCode, customFields, displayDate, expirationDate, externalReferenceCode, id, level, limitationTimes, limitationType, maximumDiscountAmount, neverExpire, numberOfUse, percentageLevel1, percentageLevel2, percentageLevel3, percentageLevel4, productGroups, products, rules, rulesConjuntion, target, title, useCouponCode, usePercentage}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField
	public Discount discount(@GraphQLName("id") Long id) throws Exception {
		return _applyComponentServiceObjects(
			_discountResourceComponentServiceObjects,
			this::_populateResourceContext,
			discountResource -> discountResource.getDiscount(id));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {discountByExternalReferenceCodeDiscountAccounts(externalReferenceCode: ___, page: ___, pageSize: ___){items {__}, page, pageSize, totalCount}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField
	public DiscountAccountPage discountByExternalReferenceCodeDiscountAccounts(
			@GraphQLName("externalReferenceCode") String externalReferenceCode,
			@GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page)
		throws Exception {

		return _applyComponentServiceObjects(
			_discountAccountResourceComponentServiceObjects,
			this::_populateResourceContext,
			discountAccountResource -> new DiscountAccountPage(
				discountAccountResource.
					getDiscountByExternalReferenceCodeDiscountAccountsPage(
						externalReferenceCode, Pagination.of(page, pageSize))));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {discountIdDiscountAccounts(id: ___, page: ___, pageSize: ___){items {__}, page, pageSize, totalCount}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField
	public DiscountAccountPage discountIdDiscountAccounts(
			@GraphQLName("id") Long id, @GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page)
		throws Exception {

		return _applyComponentServiceObjects(
			_discountAccountResourceComponentServiceObjects,
			this::_populateResourceContext,
			discountAccountResource -> new DiscountAccountPage(
				discountAccountResource.getDiscountIdDiscountAccountsPage(
					id, Pagination.of(page, pageSize))));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {discountByExternalReferenceCodeDiscountAccountGroups(externalReferenceCode: ___, page: ___, pageSize: ___){items {__}, page, pageSize, totalCount}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField
	public DiscountAccountGroupPage
			discountByExternalReferenceCodeDiscountAccountGroups(
				@GraphQLName("externalReferenceCode") String
					externalReferenceCode,
				@GraphQLName("pageSize") int pageSize,
				@GraphQLName("page") int page)
		throws Exception {

		return _applyComponentServiceObjects(
			_discountAccountGroupResourceComponentServiceObjects,
			this::_populateResourceContext,
			discountAccountGroupResource -> new DiscountAccountGroupPage(
				discountAccountGroupResource.
					getDiscountByExternalReferenceCodeDiscountAccountGroupsPage(
						externalReferenceCode, Pagination.of(page, pageSize))));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {discountIdDiscountAccountGroups(id: ___, page: ___, pageSize: ___){items {__}, page, pageSize, totalCount}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField
	public DiscountAccountGroupPage discountIdDiscountAccountGroups(
			@GraphQLName("id") Long id, @GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page)
		throws Exception {

		return _applyComponentServiceObjects(
			_discountAccountGroupResourceComponentServiceObjects,
			this::_populateResourceContext,
			discountAccountGroupResource -> new DiscountAccountGroupPage(
				discountAccountGroupResource.
					getDiscountIdDiscountAccountGroupsPage(
						id, Pagination.of(page, pageSize))));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {discountByExternalReferenceCodeDiscountCategories(externalReferenceCode: ___, page: ___, pageSize: ___){items {__}, page, pageSize, totalCount}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField
	public DiscountCategoryPage
			discountByExternalReferenceCodeDiscountCategories(
				@GraphQLName("externalReferenceCode") String
					externalReferenceCode,
				@GraphQLName("pageSize") int pageSize,
				@GraphQLName("page") int page)
		throws Exception {

		return _applyComponentServiceObjects(
			_discountCategoryResourceComponentServiceObjects,
			this::_populateResourceContext,
			discountCategoryResource -> new DiscountCategoryPage(
				discountCategoryResource.
					getDiscountByExternalReferenceCodeDiscountCategoriesPage(
						externalReferenceCode, Pagination.of(page, pageSize))));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {discountIdDiscountCategories(id: ___, page: ___, pageSize: ___){items {__}, page, pageSize, totalCount}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField
	public DiscountCategoryPage discountIdDiscountCategories(
			@GraphQLName("id") Long id, @GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page)
		throws Exception {

		return _applyComponentServiceObjects(
			_discountCategoryResourceComponentServiceObjects,
			this::_populateResourceContext,
			discountCategoryResource -> new DiscountCategoryPage(
				discountCategoryResource.getDiscountIdDiscountCategoriesPage(
					id, Pagination.of(page, pageSize))));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {discountByExternalReferenceCodeDiscountChannels(externalReferenceCode: ___, page: ___, pageSize: ___){items {__}, page, pageSize, totalCount}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField
	public DiscountChannelPage discountByExternalReferenceCodeDiscountChannels(
			@GraphQLName("externalReferenceCode") String externalReferenceCode,
			@GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page)
		throws Exception {

		return _applyComponentServiceObjects(
			_discountChannelResourceComponentServiceObjects,
			this::_populateResourceContext,
			discountChannelResource -> new DiscountChannelPage(
				discountChannelResource.
					getDiscountByExternalReferenceCodeDiscountChannelsPage(
						externalReferenceCode, Pagination.of(page, pageSize))));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {discountIdDiscountChannels(id: ___, page: ___, pageSize: ___){items {__}, page, pageSize, totalCount}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField
	public DiscountChannelPage discountIdDiscountChannels(
			@GraphQLName("id") Long id, @GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page)
		throws Exception {

		return _applyComponentServiceObjects(
			_discountChannelResourceComponentServiceObjects,
			this::_populateResourceContext,
			discountChannelResource -> new DiscountChannelPage(
				discountChannelResource.getDiscountIdDiscountChannelsPage(
					id, Pagination.of(page, pageSize))));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {discountByExternalReferenceCodeDiscountProducts(externalReferenceCode: ___, page: ___, pageSize: ___){items {__}, page, pageSize, totalCount}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField
	public DiscountProductPage discountByExternalReferenceCodeDiscountProducts(
			@GraphQLName("externalReferenceCode") String externalReferenceCode,
			@GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page)
		throws Exception {

		return _applyComponentServiceObjects(
			_discountProductResourceComponentServiceObjects,
			this::_populateResourceContext,
			discountProductResource -> new DiscountProductPage(
				discountProductResource.
					getDiscountByExternalReferenceCodeDiscountProductsPage(
						externalReferenceCode, Pagination.of(page, pageSize))));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {discountIdDiscountProducts(id: ___, page: ___, pageSize: ___){items {__}, page, pageSize, totalCount}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField
	public DiscountProductPage discountIdDiscountProducts(
			@GraphQLName("id") Long id, @GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page)
		throws Exception {

		return _applyComponentServiceObjects(
			_discountProductResourceComponentServiceObjects,
			this::_populateResourceContext,
			discountProductResource -> new DiscountProductPage(
				discountProductResource.getDiscountIdDiscountProductsPage(
					id, Pagination.of(page, pageSize))));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {discountByExternalReferenceCodeDiscountProductGroups(externalReferenceCode: ___, page: ___, pageSize: ___){items {__}, page, pageSize, totalCount}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField
	public DiscountProductGroupPage
			discountByExternalReferenceCodeDiscountProductGroups(
				@GraphQLName("externalReferenceCode") String
					externalReferenceCode,
				@GraphQLName("pageSize") int pageSize,
				@GraphQLName("page") int page)
		throws Exception {

		return _applyComponentServiceObjects(
			_discountProductGroupResourceComponentServiceObjects,
			this::_populateResourceContext,
			discountProductGroupResource -> new DiscountProductGroupPage(
				discountProductGroupResource.
					getDiscountByExternalReferenceCodeDiscountProductGroupsPage(
						externalReferenceCode, Pagination.of(page, pageSize))));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {discountIdDiscountProductGroups(id: ___, page: ___, pageSize: ___){items {__}, page, pageSize, totalCount}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField
	public DiscountProductGroupPage discountIdDiscountProductGroups(
			@GraphQLName("id") Long id, @GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page)
		throws Exception {

		return _applyComponentServiceObjects(
			_discountProductGroupResourceComponentServiceObjects,
			this::_populateResourceContext,
			discountProductGroupResource -> new DiscountProductGroupPage(
				discountProductGroupResource.
					getDiscountIdDiscountProductGroupsPage(
						id, Pagination.of(page, pageSize))));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {discountRule(id: ___){discountId, id, name, type, typeSettings}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField
	public DiscountRule discountRule(@GraphQLName("id") Long id)
		throws Exception {

		return _applyComponentServiceObjects(
			_discountRuleResourceComponentServiceObjects,
			this::_populateResourceContext,
			discountRuleResource -> discountRuleResource.getDiscountRule(id));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {discountByExternalReferenceCodeDiscountRules(externalReferenceCode: ___, page: ___, pageSize: ___){items {__}, page, pageSize, totalCount}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField
	public DiscountRulePage discountByExternalReferenceCodeDiscountRules(
			@GraphQLName("externalReferenceCode") String externalReferenceCode,
			@GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page)
		throws Exception {

		return _applyComponentServiceObjects(
			_discountRuleResourceComponentServiceObjects,
			this::_populateResourceContext,
			discountRuleResource -> new DiscountRulePage(
				discountRuleResource.
					getDiscountByExternalReferenceCodeDiscountRulesPage(
						externalReferenceCode, Pagination.of(page, pageSize))));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {discountIdDiscountRules(id: ___, page: ___, pageSize: ___){items {__}, page, pageSize, totalCount}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField
	public DiscountRulePage discountIdDiscountRules(
			@GraphQLName("id") Long id, @GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page)
		throws Exception {

		return _applyComponentServiceObjects(
			_discountRuleResourceComponentServiceObjects,
			this::_populateResourceContext,
			discountRuleResource -> new DiscountRulePage(
				discountRuleResource.getDiscountIdDiscountRulesPage(
					id, Pagination.of(page, pageSize))));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {priceEntryByExternalReferenceCode(externalReferenceCode: ___){active, bulkPricing, customFields, discountDiscovery, discountLevel1, discountLevel2, discountLevel3, discountLevel4, displayDate, expirationDate, externalReferenceCode, hasTierPrice, id, price, priceListExternalReferenceCode, priceListId, promoPrice, sku, skuExternalReferenceCode, skuId, tierPrices}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField
	public PriceEntry priceEntryByExternalReferenceCode(
			@GraphQLName("externalReferenceCode") String externalReferenceCode)
		throws Exception {

		return _applyComponentServiceObjects(
			_priceEntryResourceComponentServiceObjects,
			this::_populateResourceContext,
			priceEntryResource ->
				priceEntryResource.getPriceEntryByExternalReferenceCode(
					externalReferenceCode));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {priceEntry(id: ___){active, bulkPricing, customFields, discountDiscovery, discountLevel1, discountLevel2, discountLevel3, discountLevel4, displayDate, expirationDate, externalReferenceCode, hasTierPrice, id, price, priceListExternalReferenceCode, priceListId, promoPrice, sku, skuExternalReferenceCode, skuId, tierPrices}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField
	public PriceEntry priceEntry(@GraphQLName("id") Long id) throws Exception {
		return _applyComponentServiceObjects(
			_priceEntryResourceComponentServiceObjects,
			this::_populateResourceContext,
			priceEntryResource -> priceEntryResource.getPriceEntry(id));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {priceListByExternalReferenceCodePriceEntries(externalReferenceCode: ___, page: ___, pageSize: ___){items {__}, page, pageSize, totalCount}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField
	public PriceEntryPage priceListByExternalReferenceCodePriceEntries(
			@GraphQLName("externalReferenceCode") String externalReferenceCode,
			@GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page)
		throws Exception {

		return _applyComponentServiceObjects(
			_priceEntryResourceComponentServiceObjects,
			this::_populateResourceContext,
			priceEntryResource -> new PriceEntryPage(
				priceEntryResource.
					getPriceListByExternalReferenceCodePriceEntriesPage(
						externalReferenceCode, Pagination.of(page, pageSize))));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {priceListIdPriceEntries(id: ___, page: ___, pageSize: ___){items {__}, page, pageSize, totalCount}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField
	public PriceEntryPage priceListIdPriceEntries(
			@GraphQLName("id") Long id, @GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page)
		throws Exception {

		return _applyComponentServiceObjects(
			_priceEntryResourceComponentServiceObjects,
			this::_populateResourceContext,
			priceEntryResource -> new PriceEntryPage(
				priceEntryResource.getPriceListIdPriceEntriesPage(
					id, Pagination.of(page, pageSize))));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {priceLists(filter: ___, page: ___, pageSize: ___, search: ___, sorts: ___){items {__}, page, pageSize, totalCount}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField
	public PriceListPage priceLists(
			@GraphQLName("search") String search,
			@GraphQLName("filter") String filterString,
			@GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page,
			@GraphQLName("sort") String sortsString)
		throws Exception {

		return _applyComponentServiceObjects(
			_priceListResourceComponentServiceObjects,
			this::_populateResourceContext,
			priceListResource -> new PriceListPage(
				priceListResource.getPriceListsPage(
					search,
					_filterBiFunction.apply(priceListResource, filterString),
					Pagination.of(page, pageSize),
					_sortsBiFunction.apply(priceListResource, sortsString))));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {priceListByExternalReferenceCode(externalReferenceCode: ___){accountGroups, accounts, active, catalogBasePriceList, catalogId, channels, currencyCode, customFields, discounts, displayDate, expirationDate, externalReferenceCode, id, name, netPrice, neverExpire, priceEntries, priority, type}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField
	public PriceList priceListByExternalReferenceCode(
			@GraphQLName("externalReferenceCode") String externalReferenceCode)
		throws Exception {

		return _applyComponentServiceObjects(
			_priceListResourceComponentServiceObjects,
			this::_populateResourceContext,
			priceListResource ->
				priceListResource.getPriceListByExternalReferenceCode(
					externalReferenceCode));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {priceList(id: ___){accountGroups, accounts, active, catalogBasePriceList, catalogId, channels, currencyCode, customFields, discounts, displayDate, expirationDate, externalReferenceCode, id, name, netPrice, neverExpire, priceEntries, priority, type}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField
	public PriceList priceList(@GraphQLName("id") Long id) throws Exception {
		return _applyComponentServiceObjects(
			_priceListResourceComponentServiceObjects,
			this::_populateResourceContext,
			priceListResource -> priceListResource.getPriceList(id));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {priceListByExternalReferenceCodePriceListAccount(externalReferenceCode: ___, page: ___, pageSize: ___){items {__}, page, pageSize, totalCount}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField
	public PriceListAccountPage
			priceListByExternalReferenceCodePriceListAccount(
				@GraphQLName("externalReferenceCode") String
					externalReferenceCode,
				@GraphQLName("pageSize") int pageSize,
				@GraphQLName("page") int page)
		throws Exception {

		return _applyComponentServiceObjects(
			_priceListAccountResourceComponentServiceObjects,
			this::_populateResourceContext,
			priceListAccountResource -> new PriceListAccountPage(
				priceListAccountResource.
					getPriceListByExternalReferenceCodePriceListAccountPage(
						externalReferenceCode, Pagination.of(page, pageSize))));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {priceListIdPriceListAccounts(id: ___, page: ___, pageSize: ___){items {__}, page, pageSize, totalCount}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField
	public PriceListAccountPage priceListIdPriceListAccounts(
			@GraphQLName("id") Long id, @GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page)
		throws Exception {

		return _applyComponentServiceObjects(
			_priceListAccountResourceComponentServiceObjects,
			this::_populateResourceContext,
			priceListAccountResource -> new PriceListAccountPage(
				priceListAccountResource.getPriceListIdPriceListAccountsPage(
					id, Pagination.of(page, pageSize))));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {priceListByExternalReferenceCodePriceListAccountGroup(externalReferenceCode: ___, page: ___, pageSize: ___){items {__}, page, pageSize, totalCount}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField
	public PriceListAccountGroupPage
			priceListByExternalReferenceCodePriceListAccountGroup(
				@GraphQLName("externalReferenceCode") String
					externalReferenceCode,
				@GraphQLName("pageSize") int pageSize,
				@GraphQLName("page") int page)
		throws Exception {

		return _applyComponentServiceObjects(
			_priceListAccountGroupResourceComponentServiceObjects,
			this::_populateResourceContext,
			priceListAccountGroupResource -> new PriceListAccountGroupPage(
				priceListAccountGroupResource.
					getPriceListByExternalReferenceCodePriceListAccountGroupPage(
						externalReferenceCode, Pagination.of(page, pageSize))));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {priceListIdPriceListAccountGroups(id: ___, page: ___, pageSize: ___){items {__}, page, pageSize, totalCount}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField
	public PriceListAccountGroupPage priceListIdPriceListAccountGroups(
			@GraphQLName("id") Long id, @GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page)
		throws Exception {

		return _applyComponentServiceObjects(
			_priceListAccountGroupResourceComponentServiceObjects,
			this::_populateResourceContext,
			priceListAccountGroupResource -> new PriceListAccountGroupPage(
				priceListAccountGroupResource.
					getPriceListIdPriceListAccountGroupsPage(
						id, Pagination.of(page, pageSize))));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {priceListByExternalReferenceCodePriceListChannel(externalReferenceCode: ___, page: ___, pageSize: ___){items {__}, page, pageSize, totalCount}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField
	public PriceListChannelPage
			priceListByExternalReferenceCodePriceListChannel(
				@GraphQLName("externalReferenceCode") String
					externalReferenceCode,
				@GraphQLName("pageSize") int pageSize,
				@GraphQLName("page") int page)
		throws Exception {

		return _applyComponentServiceObjects(
			_priceListChannelResourceComponentServiceObjects,
			this::_populateResourceContext,
			priceListChannelResource -> new PriceListChannelPage(
				priceListChannelResource.
					getPriceListByExternalReferenceCodePriceListChannelPage(
						externalReferenceCode, Pagination.of(page, pageSize))));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {priceListIdPriceListChannels(id: ___, page: ___, pageSize: ___){items {__}, page, pageSize, totalCount}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField
	public PriceListChannelPage priceListIdPriceListChannels(
			@GraphQLName("id") Long id, @GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page)
		throws Exception {

		return _applyComponentServiceObjects(
			_priceListChannelResourceComponentServiceObjects,
			this::_populateResourceContext,
			priceListChannelResource -> new PriceListChannelPage(
				priceListChannelResource.getPriceListIdPriceListChannelsPage(
					id, Pagination.of(page, pageSize))));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {priceListByExternalReferenceCodePriceListDiscount(externalReferenceCode: ___, page: ___, pageSize: ___){items {__}, page, pageSize, totalCount}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField
	public PriceListDiscountPage
			priceListByExternalReferenceCodePriceListDiscount(
				@GraphQLName("externalReferenceCode") String
					externalReferenceCode,
				@GraphQLName("pageSize") int pageSize,
				@GraphQLName("page") int page)
		throws Exception {

		return _applyComponentServiceObjects(
			_priceListDiscountResourceComponentServiceObjects,
			this::_populateResourceContext,
			priceListDiscountResource -> new PriceListDiscountPage(
				priceListDiscountResource.
					getPriceListByExternalReferenceCodePriceListDiscountPage(
						externalReferenceCode, Pagination.of(page, pageSize))));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {priceListIdPriceListDiscounts(id: ___, page: ___, pageSize: ___){items {__}, page, pageSize, totalCount}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField
	public PriceListDiscountPage priceListIdPriceListDiscounts(
			@GraphQLName("id") Long id, @GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page)
		throws Exception {

		return _applyComponentServiceObjects(
			_priceListDiscountResourceComponentServiceObjects,
			this::_populateResourceContext,
			priceListDiscountResource -> new PriceListDiscountPage(
				priceListDiscountResource.getPriceListIdPriceListDiscountsPage(
					id, Pagination.of(page, pageSize))));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {priceEntryByExternalReferenceCodeTierPrices(externalReferenceCode: ___, page: ___, pageSize: ___){items {__}, page, pageSize, totalCount}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField
	public TierPricePage priceEntryByExternalReferenceCodeTierPrices(
			@GraphQLName("externalReferenceCode") String externalReferenceCode,
			@GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page)
		throws Exception {

		return _applyComponentServiceObjects(
			_tierPriceResourceComponentServiceObjects,
			this::_populateResourceContext,
			tierPriceResource -> new TierPricePage(
				tierPriceResource.
					getPriceEntryByExternalReferenceCodeTierPricesPage(
						externalReferenceCode, Pagination.of(page, pageSize))));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {priceEntryIdTierPrices(id: ___, page: ___, pageSize: ___){items {__}, page, pageSize, totalCount}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField
	public TierPricePage priceEntryIdTierPrices(
			@GraphQLName("id") Long id, @GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page)
		throws Exception {

		return _applyComponentServiceObjects(
			_tierPriceResourceComponentServiceObjects,
			this::_populateResourceContext,
			tierPriceResource -> new TierPricePage(
				tierPriceResource.getPriceEntryIdTierPricesPage(
					id, Pagination.of(page, pageSize))));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {tierPriceByExternalReferenceCode(externalReferenceCode: ___){active, customFields, discountDiscovery, discountLevel1, discountLevel2, discountLevel3, discountLevel4, displayDate, expirationDate, externalReferenceCode, id, minimumQuantity, price, priceEntryExternalReferenceCode, priceEntryId, promoPrice}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField
	public TierPrice tierPriceByExternalReferenceCode(
			@GraphQLName("externalReferenceCode") String externalReferenceCode)
		throws Exception {

		return _applyComponentServiceObjects(
			_tierPriceResourceComponentServiceObjects,
			this::_populateResourceContext,
			tierPriceResource ->
				tierPriceResource.getTierPriceByExternalReferenceCode(
					externalReferenceCode));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {tierPrice(id: ___){active, customFields, discountDiscovery, discountLevel1, discountLevel2, discountLevel3, discountLevel4, displayDate, expirationDate, externalReferenceCode, id, minimumQuantity, price, priceEntryExternalReferenceCode, priceEntryId, promoPrice}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField
	public TierPrice tierPrice(@GraphQLName("id") Long id) throws Exception {
		return _applyComponentServiceObjects(
			_tierPriceResourceComponentServiceObjects,
			this::_populateResourceContext,
			tierPriceResource -> tierPriceResource.getTierPrice(id));
	}

	@GraphQLTypeExtension(Discount.class)
	public class
		GetDiscountByExternalReferenceCodeDiscountAccountGroupsPageTypeExtension {

		public GetDiscountByExternalReferenceCodeDiscountAccountGroupsPageTypeExtension(
			Discount discount) {

			_discount = discount;
		}

		@GraphQLField
		public DiscountAccountGroupPage
				byExternalReferenceCodeDiscountAccountGroups(
					@GraphQLName("pageSize") int pageSize,
					@GraphQLName("page") int page)
			throws Exception {

			return _applyComponentServiceObjects(
				_discountAccountGroupResourceComponentServiceObjects,
				Query.this::_populateResourceContext,
				discountAccountGroupResource -> new DiscountAccountGroupPage(
					discountAccountGroupResource.
						getDiscountByExternalReferenceCodeDiscountAccountGroupsPage(
							_discount.getExternalReferenceCode(),
							Pagination.of(page, pageSize))));
		}

		private Discount _discount;

	}

	@GraphQLTypeExtension(Discount.class)
	public class
		GetDiscountByExternalReferenceCodeDiscountCategoriesPageTypeExtension {

		public GetDiscountByExternalReferenceCodeDiscountCategoriesPageTypeExtension(
			Discount discount) {

			_discount = discount;
		}

		@GraphQLField
		public DiscountCategoryPage byExternalReferenceCodeDiscountCategories(
				@GraphQLName("pageSize") int pageSize,
				@GraphQLName("page") int page)
			throws Exception {

			return _applyComponentServiceObjects(
				_discountCategoryResourceComponentServiceObjects,
				Query.this::_populateResourceContext,
				discountCategoryResource -> new DiscountCategoryPage(
					discountCategoryResource.
						getDiscountByExternalReferenceCodeDiscountCategoriesPage(
							_discount.getExternalReferenceCode(),
							Pagination.of(page, pageSize))));
		}

		private Discount _discount;

	}

	@GraphQLTypeExtension(Discount.class)
	public class
		GetDiscountByExternalReferenceCodeDiscountProductGroupsPageTypeExtension {

		public GetDiscountByExternalReferenceCodeDiscountProductGroupsPageTypeExtension(
			Discount discount) {

			_discount = discount;
		}

		@GraphQLField
		public DiscountProductGroupPage
				byExternalReferenceCodeDiscountProductGroups(
					@GraphQLName("pageSize") int pageSize,
					@GraphQLName("page") int page)
			throws Exception {

			return _applyComponentServiceObjects(
				_discountProductGroupResourceComponentServiceObjects,
				Query.this::_populateResourceContext,
				discountProductGroupResource -> new DiscountProductGroupPage(
					discountProductGroupResource.
						getDiscountByExternalReferenceCodeDiscountProductGroupsPage(
							_discount.getExternalReferenceCode(),
							Pagination.of(page, pageSize))));
		}

		private Discount _discount;

	}

	@GraphQLTypeExtension(Discount.class)
	public class
		GetPriceEntryByExternalReferenceCodeTierPricesPageTypeExtension {

		public GetPriceEntryByExternalReferenceCodeTierPricesPageTypeExtension(
			Discount discount) {

			_discount = discount;
		}

		@GraphQLField
		public TierPricePage priceEntryByExternalReferenceCodeTierPrices(
				@GraphQLName("pageSize") int pageSize,
				@GraphQLName("page") int page)
			throws Exception {

			return _applyComponentServiceObjects(
				_tierPriceResourceComponentServiceObjects,
				Query.this::_populateResourceContext,
				tierPriceResource -> new TierPricePage(
					tierPriceResource.
						getPriceEntryByExternalReferenceCodeTierPricesPage(
							_discount.getExternalReferenceCode(),
							Pagination.of(page, pageSize))));
		}

		private Discount _discount;

	}

	@GraphQLTypeExtension(Discount.class)
	public class
		GetDiscountByExternalReferenceCodeDiscountProductsPageTypeExtension {

		public GetDiscountByExternalReferenceCodeDiscountProductsPageTypeExtension(
			Discount discount) {

			_discount = discount;
		}

		@GraphQLField
		public DiscountProductPage byExternalReferenceCodeDiscountProducts(
				@GraphQLName("pageSize") int pageSize,
				@GraphQLName("page") int page)
			throws Exception {

			return _applyComponentServiceObjects(
				_discountProductResourceComponentServiceObjects,
				Query.this::_populateResourceContext,
				discountProductResource -> new DiscountProductPage(
					discountProductResource.
						getDiscountByExternalReferenceCodeDiscountProductsPage(
							_discount.getExternalReferenceCode(),
							Pagination.of(page, pageSize))));
		}

		private Discount _discount;

	}

	@GraphQLTypeExtension(Discount.class)
	public class
		GetPriceListByExternalReferenceCodePriceListAccountGroupPageTypeExtension {

		public GetPriceListByExternalReferenceCodePriceListAccountGroupPageTypeExtension(
			Discount discount) {

			_discount = discount;
		}

		@GraphQLField
		public PriceListAccountGroupPage
				priceListByExternalReferenceCodePriceListAccountGroup(
					@GraphQLName("pageSize") int pageSize,
					@GraphQLName("page") int page)
			throws Exception {

			return _applyComponentServiceObjects(
				_priceListAccountGroupResourceComponentServiceObjects,
				Query.this::_populateResourceContext,
				priceListAccountGroupResource -> new PriceListAccountGroupPage(
					priceListAccountGroupResource.
						getPriceListByExternalReferenceCodePriceListAccountGroupPage(
							_discount.getExternalReferenceCode(),
							Pagination.of(page, pageSize))));
		}

		private Discount _discount;

	}

	@GraphQLTypeExtension(Discount.class)
	public class
		GetPriceListByExternalReferenceCodePriceListChannelPageTypeExtension {

		public GetPriceListByExternalReferenceCodePriceListChannelPageTypeExtension(
			Discount discount) {

			_discount = discount;
		}

		@GraphQLField
		public PriceListChannelPage
				priceListByExternalReferenceCodePriceListChannel(
					@GraphQLName("pageSize") int pageSize,
					@GraphQLName("page") int page)
			throws Exception {

			return _applyComponentServiceObjects(
				_priceListChannelResourceComponentServiceObjects,
				Query.this::_populateResourceContext,
				priceListChannelResource -> new PriceListChannelPage(
					priceListChannelResource.
						getPriceListByExternalReferenceCodePriceListChannelPage(
							_discount.getExternalReferenceCode(),
							Pagination.of(page, pageSize))));
		}

		private Discount _discount;

	}

	@GraphQLTypeExtension(PriceEntry.class)
	public class GetDiscountByExternalReferenceCodeTypeExtension {

		public GetDiscountByExternalReferenceCodeTypeExtension(
			PriceEntry priceEntry) {

			_priceEntry = priceEntry;
		}

		@GraphQLField
		public Discount discountByExternalReferenceCode() throws Exception {
			return _applyComponentServiceObjects(
				_discountResourceComponentServiceObjects,
				Query.this::_populateResourceContext,
				discountResource ->
					discountResource.getDiscountByExternalReferenceCode(
						_priceEntry.getExternalReferenceCode()));
		}

		private PriceEntry _priceEntry;

	}

	@GraphQLTypeExtension(Discount.class)
	public class GetPriceListByExternalReferenceCodeTypeExtension {

		public GetPriceListByExternalReferenceCodeTypeExtension(
			Discount discount) {

			_discount = discount;
		}

		@GraphQLField
		public PriceList priceListByExternalReferenceCode() throws Exception {
			return _applyComponentServiceObjects(
				_priceListResourceComponentServiceObjects,
				Query.this::_populateResourceContext,
				priceListResource ->
					priceListResource.getPriceListByExternalReferenceCode(
						_discount.getExternalReferenceCode()));
		}

		private Discount _discount;

	}

	@GraphQLTypeExtension(Discount.class)
	public class
		GetPriceListByExternalReferenceCodePriceListDiscountPageTypeExtension {

		public GetPriceListByExternalReferenceCodePriceListDiscountPageTypeExtension(
			Discount discount) {

			_discount = discount;
		}

		@GraphQLField
		public PriceListDiscountPage
				priceListByExternalReferenceCodePriceListDiscount(
					@GraphQLName("pageSize") int pageSize,
					@GraphQLName("page") int page)
			throws Exception {

			return _applyComponentServiceObjects(
				_priceListDiscountResourceComponentServiceObjects,
				Query.this::_populateResourceContext,
				priceListDiscountResource -> new PriceListDiscountPage(
					priceListDiscountResource.
						getPriceListByExternalReferenceCodePriceListDiscountPage(
							_discount.getExternalReferenceCode(),
							Pagination.of(page, pageSize))));
		}

		private Discount _discount;

	}

	@GraphQLTypeExtension(Discount.class)
	public class GetTierPriceByExternalReferenceCodeTypeExtension {

		public GetTierPriceByExternalReferenceCodeTypeExtension(
			Discount discount) {

			_discount = discount;
		}

		@GraphQLField
		public TierPrice tierPriceByExternalReferenceCode() throws Exception {
			return _applyComponentServiceObjects(
				_tierPriceResourceComponentServiceObjects,
				Query.this::_populateResourceContext,
				tierPriceResource ->
					tierPriceResource.getTierPriceByExternalReferenceCode(
						_discount.getExternalReferenceCode()));
		}

		private Discount _discount;

	}

	@GraphQLTypeExtension(Discount.class)
	public class
		GetDiscountByExternalReferenceCodeDiscountChannelsPageTypeExtension {

		public GetDiscountByExternalReferenceCodeDiscountChannelsPageTypeExtension(
			Discount discount) {

			_discount = discount;
		}

		@GraphQLField
		public DiscountChannelPage byExternalReferenceCodeDiscountChannels(
				@GraphQLName("pageSize") int pageSize,
				@GraphQLName("page") int page)
			throws Exception {

			return _applyComponentServiceObjects(
				_discountChannelResourceComponentServiceObjects,
				Query.this::_populateResourceContext,
				discountChannelResource -> new DiscountChannelPage(
					discountChannelResource.
						getDiscountByExternalReferenceCodeDiscountChannelsPage(
							_discount.getExternalReferenceCode(),
							Pagination.of(page, pageSize))));
		}

		private Discount _discount;

	}

	@GraphQLTypeExtension(Discount.class)
	public class
		GetPriceListByExternalReferenceCodePriceListAccountPageTypeExtension {

		public GetPriceListByExternalReferenceCodePriceListAccountPageTypeExtension(
			Discount discount) {

			_discount = discount;
		}

		@GraphQLField
		public PriceListAccountPage
				priceListByExternalReferenceCodePriceListAccount(
					@GraphQLName("pageSize") int pageSize,
					@GraphQLName("page") int page)
			throws Exception {

			return _applyComponentServiceObjects(
				_priceListAccountResourceComponentServiceObjects,
				Query.this::_populateResourceContext,
				priceListAccountResource -> new PriceListAccountPage(
					priceListAccountResource.
						getPriceListByExternalReferenceCodePriceListAccountPage(
							_discount.getExternalReferenceCode(),
							Pagination.of(page, pageSize))));
		}

		private Discount _discount;

	}

	@GraphQLTypeExtension(Discount.class)
	public class GetPriceEntryByExternalReferenceCodeTypeExtension {

		public GetPriceEntryByExternalReferenceCodeTypeExtension(
			Discount discount) {

			_discount = discount;
		}

		@GraphQLField
		public PriceEntry priceEntryByExternalReferenceCode() throws Exception {
			return _applyComponentServiceObjects(
				_priceEntryResourceComponentServiceObjects,
				Query.this::_populateResourceContext,
				priceEntryResource ->
					priceEntryResource.getPriceEntryByExternalReferenceCode(
						_discount.getExternalReferenceCode()));
		}

		private Discount _discount;

	}

	@GraphQLTypeExtension(Discount.class)
	public class
		GetPriceListByExternalReferenceCodePriceEntriesPageTypeExtension {

		public GetPriceListByExternalReferenceCodePriceEntriesPageTypeExtension(
			Discount discount) {

			_discount = discount;
		}

		@GraphQLField
		public PriceEntryPage priceListByExternalReferenceCodePriceEntries(
				@GraphQLName("pageSize") int pageSize,
				@GraphQLName("page") int page)
			throws Exception {

			return _applyComponentServiceObjects(
				_priceEntryResourceComponentServiceObjects,
				Query.this::_populateResourceContext,
				priceEntryResource -> new PriceEntryPage(
					priceEntryResource.
						getPriceListByExternalReferenceCodePriceEntriesPage(
							_discount.getExternalReferenceCode(),
							Pagination.of(page, pageSize))));
		}

		private Discount _discount;

	}

	@GraphQLTypeExtension(Discount.class)
	public class
		GetDiscountByExternalReferenceCodeDiscountRulesPageTypeExtension {

		public GetDiscountByExternalReferenceCodeDiscountRulesPageTypeExtension(
			Discount discount) {

			_discount = discount;
		}

		@GraphQLField
		public DiscountRulePage byExternalReferenceCodeDiscountRules(
				@GraphQLName("pageSize") int pageSize,
				@GraphQLName("page") int page)
			throws Exception {

			return _applyComponentServiceObjects(
				_discountRuleResourceComponentServiceObjects,
				Query.this::_populateResourceContext,
				discountRuleResource -> new DiscountRulePage(
					discountRuleResource.
						getDiscountByExternalReferenceCodeDiscountRulesPage(
							_discount.getExternalReferenceCode(),
							Pagination.of(page, pageSize))));
		}

		private Discount _discount;

	}

	@GraphQLTypeExtension(Discount.class)
	public class
		GetDiscountByExternalReferenceCodeDiscountAccountsPageTypeExtension {

		public GetDiscountByExternalReferenceCodeDiscountAccountsPageTypeExtension(
			Discount discount) {

			_discount = discount;
		}

		@GraphQLField
		public DiscountAccountPage byExternalReferenceCodeDiscountAccounts(
				@GraphQLName("pageSize") int pageSize,
				@GraphQLName("page") int page)
			throws Exception {

			return _applyComponentServiceObjects(
				_discountAccountResourceComponentServiceObjects,
				Query.this::_populateResourceContext,
				discountAccountResource -> new DiscountAccountPage(
					discountAccountResource.
						getDiscountByExternalReferenceCodeDiscountAccountsPage(
							_discount.getExternalReferenceCode(),
							Pagination.of(page, pageSize))));
		}

		private Discount _discount;

	}

	@GraphQLName("DiscountPage")
	public class DiscountPage {

		public DiscountPage(Page discountPage) {
			actions = discountPage.getActions();
			items = discountPage.getItems();
			lastPage = discountPage.getLastPage();
			page = discountPage.getPage();
			pageSize = discountPage.getPageSize();
			totalCount = discountPage.getTotalCount();
		}

		@GraphQLField
		protected Map<String, Map> actions;

		@GraphQLField
		protected java.util.Collection<Discount> items;

		@GraphQLField
		protected long lastPage;

		@GraphQLField
		protected long page;

		@GraphQLField
		protected long pageSize;

		@GraphQLField
		protected long totalCount;

	}

	@GraphQLName("DiscountAccountPage")
	public class DiscountAccountPage {

		public DiscountAccountPage(Page discountAccountPage) {
			actions = discountAccountPage.getActions();
			items = discountAccountPage.getItems();
			lastPage = discountAccountPage.getLastPage();
			page = discountAccountPage.getPage();
			pageSize = discountAccountPage.getPageSize();
			totalCount = discountAccountPage.getTotalCount();
		}

		@GraphQLField
		protected Map<String, Map> actions;

		@GraphQLField
		protected java.util.Collection<DiscountAccount> items;

		@GraphQLField
		protected long lastPage;

		@GraphQLField
		protected long page;

		@GraphQLField
		protected long pageSize;

		@GraphQLField
		protected long totalCount;

	}

	@GraphQLName("DiscountAccountGroupPage")
	public class DiscountAccountGroupPage {

		public DiscountAccountGroupPage(Page discountAccountGroupPage) {
			actions = discountAccountGroupPage.getActions();
			items = discountAccountGroupPage.getItems();
			lastPage = discountAccountGroupPage.getLastPage();
			page = discountAccountGroupPage.getPage();
			pageSize = discountAccountGroupPage.getPageSize();
			totalCount = discountAccountGroupPage.getTotalCount();
		}

		@GraphQLField
		protected Map<String, Map> actions;

		@GraphQLField
		protected java.util.Collection<DiscountAccountGroup> items;

		@GraphQLField
		protected long lastPage;

		@GraphQLField
		protected long page;

		@GraphQLField
		protected long pageSize;

		@GraphQLField
		protected long totalCount;

	}

	@GraphQLName("DiscountCategoryPage")
	public class DiscountCategoryPage {

		public DiscountCategoryPage(Page discountCategoryPage) {
			actions = discountCategoryPage.getActions();
			items = discountCategoryPage.getItems();
			lastPage = discountCategoryPage.getLastPage();
			page = discountCategoryPage.getPage();
			pageSize = discountCategoryPage.getPageSize();
			totalCount = discountCategoryPage.getTotalCount();
		}

		@GraphQLField
		protected Map<String, Map> actions;

		@GraphQLField
		protected java.util.Collection<DiscountCategory> items;

		@GraphQLField
		protected long lastPage;

		@GraphQLField
		protected long page;

		@GraphQLField
		protected long pageSize;

		@GraphQLField
		protected long totalCount;

	}

	@GraphQLName("DiscountChannelPage")
	public class DiscountChannelPage {

		public DiscountChannelPage(Page discountChannelPage) {
			actions = discountChannelPage.getActions();
			items = discountChannelPage.getItems();
			lastPage = discountChannelPage.getLastPage();
			page = discountChannelPage.getPage();
			pageSize = discountChannelPage.getPageSize();
			totalCount = discountChannelPage.getTotalCount();
		}

		@GraphQLField
		protected Map<String, Map> actions;

		@GraphQLField
		protected java.util.Collection<DiscountChannel> items;

		@GraphQLField
		protected long lastPage;

		@GraphQLField
		protected long page;

		@GraphQLField
		protected long pageSize;

		@GraphQLField
		protected long totalCount;

	}

	@GraphQLName("DiscountProductPage")
	public class DiscountProductPage {

		public DiscountProductPage(Page discountProductPage) {
			actions = discountProductPage.getActions();
			items = discountProductPage.getItems();
			lastPage = discountProductPage.getLastPage();
			page = discountProductPage.getPage();
			pageSize = discountProductPage.getPageSize();
			totalCount = discountProductPage.getTotalCount();
		}

		@GraphQLField
		protected Map<String, Map> actions;

		@GraphQLField
		protected java.util.Collection<DiscountProduct> items;

		@GraphQLField
		protected long lastPage;

		@GraphQLField
		protected long page;

		@GraphQLField
		protected long pageSize;

		@GraphQLField
		protected long totalCount;

	}

	@GraphQLName("DiscountProductGroupPage")
	public class DiscountProductGroupPage {

		public DiscountProductGroupPage(Page discountProductGroupPage) {
			actions = discountProductGroupPage.getActions();
			items = discountProductGroupPage.getItems();
			lastPage = discountProductGroupPage.getLastPage();
			page = discountProductGroupPage.getPage();
			pageSize = discountProductGroupPage.getPageSize();
			totalCount = discountProductGroupPage.getTotalCount();
		}

		@GraphQLField
		protected Map<String, Map> actions;

		@GraphQLField
		protected java.util.Collection<DiscountProductGroup> items;

		@GraphQLField
		protected long lastPage;

		@GraphQLField
		protected long page;

		@GraphQLField
		protected long pageSize;

		@GraphQLField
		protected long totalCount;

	}

	@GraphQLName("DiscountRulePage")
	public class DiscountRulePage {

		public DiscountRulePage(Page discountRulePage) {
			actions = discountRulePage.getActions();
			items = discountRulePage.getItems();
			lastPage = discountRulePage.getLastPage();
			page = discountRulePage.getPage();
			pageSize = discountRulePage.getPageSize();
			totalCount = discountRulePage.getTotalCount();
		}

		@GraphQLField
		protected Map<String, Map> actions;

		@GraphQLField
		protected java.util.Collection<DiscountRule> items;

		@GraphQLField
		protected long lastPage;

		@GraphQLField
		protected long page;

		@GraphQLField
		protected long pageSize;

		@GraphQLField
		protected long totalCount;

	}

	@GraphQLName("PriceEntryPage")
	public class PriceEntryPage {

		public PriceEntryPage(Page priceEntryPage) {
			actions = priceEntryPage.getActions();
			items = priceEntryPage.getItems();
			lastPage = priceEntryPage.getLastPage();
			page = priceEntryPage.getPage();
			pageSize = priceEntryPage.getPageSize();
			totalCount = priceEntryPage.getTotalCount();
		}

		@GraphQLField
		protected Map<String, Map> actions;

		@GraphQLField
		protected java.util.Collection<PriceEntry> items;

		@GraphQLField
		protected long lastPage;

		@GraphQLField
		protected long page;

		@GraphQLField
		protected long pageSize;

		@GraphQLField
		protected long totalCount;

	}

	@GraphQLName("PriceListPage")
	public class PriceListPage {

		public PriceListPage(Page priceListPage) {
			actions = priceListPage.getActions();
			items = priceListPage.getItems();
			lastPage = priceListPage.getLastPage();
			page = priceListPage.getPage();
			pageSize = priceListPage.getPageSize();
			totalCount = priceListPage.getTotalCount();
		}

		@GraphQLField
		protected Map<String, Map> actions;

		@GraphQLField
		protected java.util.Collection<PriceList> items;

		@GraphQLField
		protected long lastPage;

		@GraphQLField
		protected long page;

		@GraphQLField
		protected long pageSize;

		@GraphQLField
		protected long totalCount;

	}

	@GraphQLName("PriceListAccountPage")
	public class PriceListAccountPage {

		public PriceListAccountPage(Page priceListAccountPage) {
			actions = priceListAccountPage.getActions();
			items = priceListAccountPage.getItems();
			lastPage = priceListAccountPage.getLastPage();
			page = priceListAccountPage.getPage();
			pageSize = priceListAccountPage.getPageSize();
			totalCount = priceListAccountPage.getTotalCount();
		}

		@GraphQLField
		protected Map<String, Map> actions;

		@GraphQLField
		protected java.util.Collection<PriceListAccount> items;

		@GraphQLField
		protected long lastPage;

		@GraphQLField
		protected long page;

		@GraphQLField
		protected long pageSize;

		@GraphQLField
		protected long totalCount;

	}

	@GraphQLName("PriceListAccountGroupPage")
	public class PriceListAccountGroupPage {

		public PriceListAccountGroupPage(Page priceListAccountGroupPage) {
			actions = priceListAccountGroupPage.getActions();
			items = priceListAccountGroupPage.getItems();
			lastPage = priceListAccountGroupPage.getLastPage();
			page = priceListAccountGroupPage.getPage();
			pageSize = priceListAccountGroupPage.getPageSize();
			totalCount = priceListAccountGroupPage.getTotalCount();
		}

		@GraphQLField
		protected Map<String, Map> actions;

		@GraphQLField
		protected java.util.Collection<PriceListAccountGroup> items;

		@GraphQLField
		protected long lastPage;

		@GraphQLField
		protected long page;

		@GraphQLField
		protected long pageSize;

		@GraphQLField
		protected long totalCount;

	}

	@GraphQLName("PriceListChannelPage")
	public class PriceListChannelPage {

		public PriceListChannelPage(Page priceListChannelPage) {
			actions = priceListChannelPage.getActions();
			items = priceListChannelPage.getItems();
			lastPage = priceListChannelPage.getLastPage();
			page = priceListChannelPage.getPage();
			pageSize = priceListChannelPage.getPageSize();
			totalCount = priceListChannelPage.getTotalCount();
		}

		@GraphQLField
		protected Map<String, Map> actions;

		@GraphQLField
		protected java.util.Collection<PriceListChannel> items;

		@GraphQLField
		protected long lastPage;

		@GraphQLField
		protected long page;

		@GraphQLField
		protected long pageSize;

		@GraphQLField
		protected long totalCount;

	}

	@GraphQLName("PriceListDiscountPage")
	public class PriceListDiscountPage {

		public PriceListDiscountPage(Page priceListDiscountPage) {
			actions = priceListDiscountPage.getActions();
			items = priceListDiscountPage.getItems();
			lastPage = priceListDiscountPage.getLastPage();
			page = priceListDiscountPage.getPage();
			pageSize = priceListDiscountPage.getPageSize();
			totalCount = priceListDiscountPage.getTotalCount();
		}

		@GraphQLField
		protected Map<String, Map> actions;

		@GraphQLField
		protected java.util.Collection<PriceListDiscount> items;

		@GraphQLField
		protected long lastPage;

		@GraphQLField
		protected long page;

		@GraphQLField
		protected long pageSize;

		@GraphQLField
		protected long totalCount;

	}

	@GraphQLName("TierPricePage")
	public class TierPricePage {

		public TierPricePage(Page tierPricePage) {
			actions = tierPricePage.getActions();
			items = tierPricePage.getItems();
			lastPage = tierPricePage.getLastPage();
			page = tierPricePage.getPage();
			pageSize = tierPricePage.getPageSize();
			totalCount = tierPricePage.getTotalCount();
		}

		@GraphQLField
		protected Map<String, Map> actions;

		@GraphQLField
		protected java.util.Collection<TierPrice> items;

		@GraphQLField
		protected long lastPage;

		@GraphQLField
		protected long page;

		@GraphQLField
		protected long pageSize;

		@GraphQLField
		protected long totalCount;

	}

	private <T, R, E1 extends Throwable, E2 extends Throwable> R
			_applyComponentServiceObjects(
				ComponentServiceObjects<T> componentServiceObjects,
				UnsafeConsumer<T, E1> unsafeConsumer,
				UnsafeFunction<T, R, E2> unsafeFunction)
		throws E1, E2 {

		T resource = componentServiceObjects.getService();

		try {
			unsafeConsumer.accept(resource);

			return unsafeFunction.apply(resource);
		}
		finally {
			componentServiceObjects.ungetService(resource);
		}
	}

	private void _populateResourceContext(DiscountResource discountResource)
		throws Exception {

		discountResource.setContextAcceptLanguage(_acceptLanguage);
		discountResource.setContextCompany(_company);
		discountResource.setContextHttpServletRequest(_httpServletRequest);
		discountResource.setContextHttpServletResponse(_httpServletResponse);
		discountResource.setContextUriInfo(_uriInfo);
		discountResource.setContextUser(_user);
	}

	private void _populateResourceContext(
			DiscountAccountResource discountAccountResource)
		throws Exception {

		discountAccountResource.setContextAcceptLanguage(_acceptLanguage);
		discountAccountResource.setContextCompany(_company);
		discountAccountResource.setContextHttpServletRequest(
			_httpServletRequest);
		discountAccountResource.setContextHttpServletResponse(
			_httpServletResponse);
		discountAccountResource.setContextUriInfo(_uriInfo);
		discountAccountResource.setContextUser(_user);
	}

	private void _populateResourceContext(
			DiscountAccountGroupResource discountAccountGroupResource)
		throws Exception {

		discountAccountGroupResource.setContextAcceptLanguage(_acceptLanguage);
		discountAccountGroupResource.setContextCompany(_company);
		discountAccountGroupResource.setContextHttpServletRequest(
			_httpServletRequest);
		discountAccountGroupResource.setContextHttpServletResponse(
			_httpServletResponse);
		discountAccountGroupResource.setContextUriInfo(_uriInfo);
		discountAccountGroupResource.setContextUser(_user);
	}

	private void _populateResourceContext(
			DiscountCategoryResource discountCategoryResource)
		throws Exception {

		discountCategoryResource.setContextAcceptLanguage(_acceptLanguage);
		discountCategoryResource.setContextCompany(_company);
		discountCategoryResource.setContextHttpServletRequest(
			_httpServletRequest);
		discountCategoryResource.setContextHttpServletResponse(
			_httpServletResponse);
		discountCategoryResource.setContextUriInfo(_uriInfo);
		discountCategoryResource.setContextUser(_user);
	}

	private void _populateResourceContext(
			DiscountChannelResource discountChannelResource)
		throws Exception {

		discountChannelResource.setContextAcceptLanguage(_acceptLanguage);
		discountChannelResource.setContextCompany(_company);
		discountChannelResource.setContextHttpServletRequest(
			_httpServletRequest);
		discountChannelResource.setContextHttpServletResponse(
			_httpServletResponse);
		discountChannelResource.setContextUriInfo(_uriInfo);
		discountChannelResource.setContextUser(_user);
	}

	private void _populateResourceContext(
			DiscountProductResource discountProductResource)
		throws Exception {

		discountProductResource.setContextAcceptLanguage(_acceptLanguage);
		discountProductResource.setContextCompany(_company);
		discountProductResource.setContextHttpServletRequest(
			_httpServletRequest);
		discountProductResource.setContextHttpServletResponse(
			_httpServletResponse);
		discountProductResource.setContextUriInfo(_uriInfo);
		discountProductResource.setContextUser(_user);
	}

	private void _populateResourceContext(
			DiscountProductGroupResource discountProductGroupResource)
		throws Exception {

		discountProductGroupResource.setContextAcceptLanguage(_acceptLanguage);
		discountProductGroupResource.setContextCompany(_company);
		discountProductGroupResource.setContextHttpServletRequest(
			_httpServletRequest);
		discountProductGroupResource.setContextHttpServletResponse(
			_httpServletResponse);
		discountProductGroupResource.setContextUriInfo(_uriInfo);
		discountProductGroupResource.setContextUser(_user);
	}

	private void _populateResourceContext(
			DiscountRuleResource discountRuleResource)
		throws Exception {

		discountRuleResource.setContextAcceptLanguage(_acceptLanguage);
		discountRuleResource.setContextCompany(_company);
		discountRuleResource.setContextHttpServletRequest(_httpServletRequest);
		discountRuleResource.setContextHttpServletResponse(
			_httpServletResponse);
		discountRuleResource.setContextUriInfo(_uriInfo);
		discountRuleResource.setContextUser(_user);
	}

	private void _populateResourceContext(PriceEntryResource priceEntryResource)
		throws Exception {

		priceEntryResource.setContextAcceptLanguage(_acceptLanguage);
		priceEntryResource.setContextCompany(_company);
		priceEntryResource.setContextHttpServletRequest(_httpServletRequest);
		priceEntryResource.setContextHttpServletResponse(_httpServletResponse);
		priceEntryResource.setContextUriInfo(_uriInfo);
		priceEntryResource.setContextUser(_user);
	}

	private void _populateResourceContext(PriceListResource priceListResource)
		throws Exception {

		priceListResource.setContextAcceptLanguage(_acceptLanguage);
		priceListResource.setContextCompany(_company);
		priceListResource.setContextHttpServletRequest(_httpServletRequest);
		priceListResource.setContextHttpServletResponse(_httpServletResponse);
		priceListResource.setContextUriInfo(_uriInfo);
		priceListResource.setContextUser(_user);
	}

	private void _populateResourceContext(
			PriceListAccountResource priceListAccountResource)
		throws Exception {

		priceListAccountResource.setContextAcceptLanguage(_acceptLanguage);
		priceListAccountResource.setContextCompany(_company);
		priceListAccountResource.setContextHttpServletRequest(
			_httpServletRequest);
		priceListAccountResource.setContextHttpServletResponse(
			_httpServletResponse);
		priceListAccountResource.setContextUriInfo(_uriInfo);
		priceListAccountResource.setContextUser(_user);
	}

	private void _populateResourceContext(
			PriceListAccountGroupResource priceListAccountGroupResource)
		throws Exception {

		priceListAccountGroupResource.setContextAcceptLanguage(_acceptLanguage);
		priceListAccountGroupResource.setContextCompany(_company);
		priceListAccountGroupResource.setContextHttpServletRequest(
			_httpServletRequest);
		priceListAccountGroupResource.setContextHttpServletResponse(
			_httpServletResponse);
		priceListAccountGroupResource.setContextUriInfo(_uriInfo);
		priceListAccountGroupResource.setContextUser(_user);
	}

	private void _populateResourceContext(
			PriceListChannelResource priceListChannelResource)
		throws Exception {

		priceListChannelResource.setContextAcceptLanguage(_acceptLanguage);
		priceListChannelResource.setContextCompany(_company);
		priceListChannelResource.setContextHttpServletRequest(
			_httpServletRequest);
		priceListChannelResource.setContextHttpServletResponse(
			_httpServletResponse);
		priceListChannelResource.setContextUriInfo(_uriInfo);
		priceListChannelResource.setContextUser(_user);
	}

	private void _populateResourceContext(
			PriceListDiscountResource priceListDiscountResource)
		throws Exception {

		priceListDiscountResource.setContextAcceptLanguage(_acceptLanguage);
		priceListDiscountResource.setContextCompany(_company);
		priceListDiscountResource.setContextHttpServletRequest(
			_httpServletRequest);
		priceListDiscountResource.setContextHttpServletResponse(
			_httpServletResponse);
		priceListDiscountResource.setContextUriInfo(_uriInfo);
		priceListDiscountResource.setContextUser(_user);
	}

	private void _populateResourceContext(TierPriceResource tierPriceResource)
		throws Exception {

		tierPriceResource.setContextAcceptLanguage(_acceptLanguage);
		tierPriceResource.setContextCompany(_company);
		tierPriceResource.setContextHttpServletRequest(_httpServletRequest);
		tierPriceResource.setContextHttpServletResponse(_httpServletResponse);
		tierPriceResource.setContextUriInfo(_uriInfo);
		tierPriceResource.setContextUser(_user);
	}

	private static ComponentServiceObjects<DiscountResource>
		_discountResourceComponentServiceObjects;
	private static ComponentServiceObjects<DiscountAccountResource>
		_discountAccountResourceComponentServiceObjects;
	private static ComponentServiceObjects<DiscountAccountGroupResource>
		_discountAccountGroupResourceComponentServiceObjects;
	private static ComponentServiceObjects<DiscountCategoryResource>
		_discountCategoryResourceComponentServiceObjects;
	private static ComponentServiceObjects<DiscountChannelResource>
		_discountChannelResourceComponentServiceObjects;
	private static ComponentServiceObjects<DiscountProductResource>
		_discountProductResourceComponentServiceObjects;
	private static ComponentServiceObjects<DiscountProductGroupResource>
		_discountProductGroupResourceComponentServiceObjects;
	private static ComponentServiceObjects<DiscountRuleResource>
		_discountRuleResourceComponentServiceObjects;
	private static ComponentServiceObjects<PriceEntryResource>
		_priceEntryResourceComponentServiceObjects;
	private static ComponentServiceObjects<PriceListResource>
		_priceListResourceComponentServiceObjects;
	private static ComponentServiceObjects<PriceListAccountResource>
		_priceListAccountResourceComponentServiceObjects;
	private static ComponentServiceObjects<PriceListAccountGroupResource>
		_priceListAccountGroupResourceComponentServiceObjects;
	private static ComponentServiceObjects<PriceListChannelResource>
		_priceListChannelResourceComponentServiceObjects;
	private static ComponentServiceObjects<PriceListDiscountResource>
		_priceListDiscountResourceComponentServiceObjects;
	private static ComponentServiceObjects<TierPriceResource>
		_tierPriceResourceComponentServiceObjects;

	private AcceptLanguage _acceptLanguage;
	private BiFunction<Object, String, Filter> _filterBiFunction;
	private BiFunction<Object, String, Sort[]> _sortsBiFunction;
	private com.liferay.portal.kernel.model.Company _company;
	private com.liferay.portal.kernel.model.User _user;
	private HttpServletRequest _httpServletRequest;
	private HttpServletResponse _httpServletResponse;
	private UriInfo _uriInfo;

}