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

package com.liferay.commerce.order.web.internal.frontend.util;

import com.liferay.commerce.constants.CommerceOrderConstants;
import com.liferay.commerce.model.CommerceAddress;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.model.CommerceRegion;
import com.liferay.commerce.product.model.CommerceChannel;
import com.liferay.commerce.product.service.CommerceChannelServiceUtil;
import com.liferay.commerce.search.facet.NegatableSimpleFacet;
import com.liferay.commerce.service.CommerceOrderLocalService;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.search.BaseModelSearchResult;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.QueryConfig;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.facet.Facet;
import com.liferay.portal.kernel.search.facet.SimpleFacet;
import com.liferay.portal.kernel.search.facet.config.FacetConfiguration;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.text.Format;

import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

/**
 * @author Alessio Antonio Rendina
 */
public class CommerceOrderDataSetDataProviderUtil {

	public static String getCommerceOrderDateTime(
		CommerceOrder commerceOrder, Format dateTimeFormat, Locale locale) {

		if (commerceOrder.getOrderDate() == null) {
			return LanguageUtil.get(locale, "unknown");
		}

		return dateTimeFormat.format(commerceOrder.getOrderDate());
	}

	public static List<CommerceOrder> getCommerceOrders(
			CommerceOrderLocalService commerceOrderLocalService, long companyId,
			String activeTab, int orderStatus, String advanceStatus,
			String keywords, int start, int end, Sort sort)
		throws PortalException {

		SearchContext searchContext = buildSearchContext(
			companyId, activeTab, orderStatus, advanceStatus, keywords, start,
			end, sort);

		BaseModelSearchResult<CommerceOrder> baseModelSearchResult =
			commerceOrderLocalService.searchCommerceOrders(searchContext);

		return baseModelSearchResult.getBaseModels();
	}

	public static int getCommerceOrdersCount(
			CommerceOrderLocalService commerceOrderLocalService, long companyId,
			String activeTab, int orderStatus, String advanceStatus,
			String keywords)
		throws PortalException {

		SearchContext searchContext = buildSearchContext(
			companyId, activeTab, orderStatus, advanceStatus, keywords,
			QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);

		BaseModelSearchResult<CommerceOrder> baseModelSearchResult =
			commerceOrderLocalService.searchCommerceOrders(searchContext);

		return baseModelSearchResult.getLength();
	}

	public static String getDescriptiveCommerceAddress(
			CommerceAddress commerceAddress)
		throws PortalException {

		if (commerceAddress == null) {
			return StringPool.BLANK;
		}

		CommerceRegion commerceRegion = commerceAddress.getCommerceRegion();

		StringBundler sb = new StringBundler((commerceRegion == null) ? 5 : 7);

		sb.append(commerceAddress.getStreet1());
		sb.append(StringPool.SPACE);
		sb.append(commerceAddress.getCity());
		sb.append(StringPool.NEW_LINE);

		if (commerceRegion != null) {
			sb.append(commerceRegion.getCode());
			sb.append(StringPool.SPACE);
		}

		sb.append(commerceAddress.getZip());

		return sb.toString();
	}

	protected static SearchContext buildSearchContext(
			long companyId, String activeTab, int orderStatus,
			String advanceStatus, String keywords, int start, int end,
			Sort sort)
		throws PortalException {

		SearchContext searchContext = new SearchContext();

		_addFacetOrderStatus(searchContext, activeTab, orderStatus);
		_addFacetStatus(searchContext);

		if (Validator.isNotNull(advanceStatus)) {
			_addFacetAdvanceStatus(searchContext, advanceStatus);
		}

		searchContext.setAttribute(Field.ENTRY_CLASS_PK, keywords);
		searchContext.setAttribute("faceted", Boolean.TRUE);
		searchContext.setAttribute("purchaseOrderNumber", keywords);
		searchContext.setAttribute(
			"useSearchResultPermissionFilter", Boolean.FALSE);

		searchContext.setCompanyId(companyId);
		searchContext.setKeywords(keywords);
		searchContext.setStart(start);
		searchContext.setEnd(end);

		long[] commerceChannelGroupIds = _getCommerceChannelGroupIds(companyId);

		if ((commerceChannelGroupIds != null) &&
			(commerceChannelGroupIds.length > 0)) {

			searchContext.setGroupIds(commerceChannelGroupIds);
		}

		QueryConfig queryConfig = searchContext.getQueryConfig();

		queryConfig.setHighlightEnabled(false);
		queryConfig.setScoreEnabled(false);

		searchContext.setSorts(sort);

		return searchContext;
	}

	private static void _addFacetAdvanceStatus(
		SearchContext searchContext, String advanceStatus) {

		Facet facet = new SimpleFacet(searchContext);

		facet.setFieldName("advanceStatus");

		searchContext.addFacet(facet);

		searchContext.setAttribute(facet.getFieldId(), advanceStatus);
	}

	private static SearchContext _addFacetOrderStatus(
		SearchContext searchContext, String activeTab, int orderStatus) {

		boolean negated = false;
		int[] orderStatuses = null;

		if (activeTab.equals("open")) {
			orderStatuses = new int[] {
				CommerceOrderConstants.ORDER_STATUS_IN_PROGRESS,
				CommerceOrderConstants.ORDER_STATUS_OPEN
			};
		}
		else if (activeTab.equals("pending")) {
			orderStatuses = new int[] {
				CommerceOrderConstants.ORDER_STATUS_TO_FULFILL
			};
		}
		else if (orderStatus == CommerceOrderConstants.ORDER_STATUS_ANY) {
			negated = true;

			orderStatuses = new int[] {
				CommerceOrderConstants.ORDER_STATUS_IN_PROGRESS,
				CommerceOrderConstants.ORDER_STATUS_OPEN,
				CommerceOrderConstants.ORDER_STATUS_TO_FULFILL
			};
		}
		else {
			orderStatuses = new int[] {orderStatus};
		}

		searchContext.setAttribute("negateOrderStatuses", negated);
		searchContext.setAttribute("orderStatuses", orderStatuses);

		return searchContext;
	}

	private static SearchContext _addFacetStatus(SearchContext searchContext) {
		NegatableSimpleFacet negatableSimpleFacet = new NegatableSimpleFacet(
			searchContext);

		negatableSimpleFacet.setFieldName(Field.STATUS);
		negatableSimpleFacet.setNegated(true);
		negatableSimpleFacet.setStatic(true);

		FacetConfiguration facetConfiguration =
			negatableSimpleFacet.getFacetConfiguration();

		JSONObject dataJSONObject = facetConfiguration.getData();

		dataJSONObject.put(
			"value", String.valueOf(WorkflowConstants.STATUS_DRAFT));

		searchContext.addFacet(negatableSimpleFacet);

		return searchContext;
	}

	private static long[] _getCommerceChannelGroupIds(long companyId)
		throws PortalException {

		List<CommerceChannel> commerceChannels =
			CommerceChannelServiceUtil.searchCommerceChannels(companyId);

		Stream<CommerceChannel> stream = commerceChannels.stream();

		return stream.mapToLong(
			CommerceChannel::getGroupId
		).toArray();
	}

}