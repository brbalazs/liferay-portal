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

package com.liferay.commerce.pricing.web.internal.frontend;

import com.liferay.commerce.frontend.CommerceDataSetDataProvider;
import com.liferay.commerce.frontend.Filter;
import com.liferay.commerce.frontend.Pagination;
import com.liferay.commerce.frontend.model.LabelField;
import com.liferay.commerce.price.list.model.CommercePriceList;
import com.liferay.commerce.price.list.service.CommercePriceListService;
import com.liferay.commerce.pricing.web.internal.frontend.constants.CommercePricingDataSetConstants;
import com.liferay.commerce.pricing.web.internal.model.PriceList;
import com.liferay.commerce.product.model.CommerceCatalog;
import com.liferay.commerce.product.service.CommerceCatalogService;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.search.BaseModelSearchResult;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alessio Antonio Rendina
 */
@Component(
	immediate = true,
	property = "commerce.data.provider.key=" + CommercePricingDataSetConstants.COMMERCE_DATA_SET_KEY_PRICE_LISTS,
	service = CommerceDataSetDataProvider.class
)
public class CommercePriceListDataSetDataProvider
	implements CommerceDataSetDataProvider<PriceList> {

	@Override
	public int countItems(HttpServletRequest httpServletRequest, Filter filter)
		throws PortalException {

		return _commercePriceListService.searchCommercePriceListsCount(
			_portal.getCompanyId(httpServletRequest), filter.getKeywords(),
			WorkflowConstants.STATUS_ANY);
	}

	@Override
	public List<PriceList> getItems(
			HttpServletRequest httpServletRequest, Filter filter,
			Pagination pagination, Sort sort)
		throws PortalException {

		List<PriceList> priceLists = new ArrayList<>();

		BaseModelSearchResult<CommercePriceList>
			commercePriceListBaseModelSearchResult =
				_commercePriceListService.searchCommercePriceLists(
					_portal.getCompanyId(httpServletRequest),
					filter.getKeywords(), WorkflowConstants.STATUS_ANY,
					pagination.getStartPosition(), pagination.getEndPosition(),
					sort);

		for (CommercePriceList commercePriceList :
				commercePriceListBaseModelSearchResult.getBaseModels()) {

			Date createDate = commercePriceList.getCreateDate();

			String createDateDescription = LanguageUtil.getTimeDescription(
				httpServletRequest,
				System.currentTimeMillis() - createDate.getTime(), true);

			priceLists.add(
				new PriceList(
					_getActive(commercePriceList, httpServletRequest),
					commercePriceList.getUserName(),
					_getCatalog(commercePriceList),
					LanguageUtil.format(
						httpServletRequest, "x-ago", createDateDescription,
						false),
					commercePriceList.getName(),
					commercePriceList.getCommercePriceListId(),
					commercePriceList.getPriority(),
					_getLabelField(commercePriceList, httpServletRequest)));
		}

		return priceLists;
	}

	private String _getActive(
		CommercePriceList commercePriceList,
		HttpServletRequest httpServletRequest) {

		if (commercePriceList.isInactive()) {
			return LanguageUtil.get(httpServletRequest, "no");
		}

		return LanguageUtil.get(httpServletRequest, "yes");
	}

	private String _getCatalog(CommercePriceList commercePriceList)
		throws PortalException {

		CommerceCatalog commerceCatalog =
			_commerceCatalogService.fetchCommerceCatalogByGroupId(
				commercePriceList.getGroupId());

		if (commerceCatalog == null) {
			return StringPool.BLANK;
		}

		return commerceCatalog.getName();
	}

	private LabelField _getLabelField(
		CommercePriceList commercePriceList,
		HttpServletRequest httpServletRequest) {

		String statusLabel = WorkflowConstants.getStatusLabel(
			commercePriceList.getStatus());

		if (commercePriceList.isInactive()) {
			return new LabelField(
				"danger", LanguageUtil.get(httpServletRequest, statusLabel));
		}

		return new LabelField(
			"success", LanguageUtil.get(httpServletRequest, statusLabel));
	}

	@Reference
	private CommerceCatalogService _commerceCatalogService;

	@Reference
	private CommercePriceListService _commercePriceListService;

	@Reference
	private Portal _portal;

}