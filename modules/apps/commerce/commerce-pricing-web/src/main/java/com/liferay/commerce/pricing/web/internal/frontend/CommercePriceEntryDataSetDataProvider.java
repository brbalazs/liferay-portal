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

import com.liferay.commerce.currency.model.CommerceMoney;
import com.liferay.commerce.currency.util.CommercePriceFormatter;
import com.liferay.commerce.frontend.CommerceDataSetDataProvider;
import com.liferay.commerce.frontend.Filter;
import com.liferay.commerce.frontend.Pagination;
import com.liferay.commerce.frontend.model.ImageField;
import com.liferay.commerce.price.list.model.CommercePriceEntry;
import com.liferay.commerce.price.list.model.CommercePriceList;
import com.liferay.commerce.price.list.service.CommercePriceEntryService;
import com.liferay.commerce.price.list.service.CommercePriceListService;
import com.liferay.commerce.pricing.configuration.CommercePricingConfiguration;
import com.liferay.commerce.pricing.web.internal.frontend.constants.CommercePricingDataSetConstants;
import com.liferay.commerce.pricing.web.internal.model.PriceEntry;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.search.BaseModelSearchResult;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alessio Antonio Rendina
 */
@Component(
	immediate = true,
	property = "commerce.data.provider.key=" + CommercePricingDataSetConstants.COMMERCE_DATA_SET_KEY_PRICE_ENTRIES,
	service = CommerceDataSetDataProvider.class
)
public class CommercePriceEntryDataSetDataProvider
	implements CommerceDataSetDataProvider<PriceEntry> {

	@Override
	public int countItems(HttpServletRequest httpServletRequest, Filter filter)
		throws PortalException {

		long commercePriceListId = ParamUtil.getLong(
			httpServletRequest, "commercePriceListId");

		return _commercePriceEntryService.searchCommercePriceEntriesCount(
			_portal.getCompanyId(httpServletRequest), commercePriceListId,
			filter.getKeywords());
	}

	@Override
	public List<PriceEntry> getItems(
			HttpServletRequest httpServletRequest, Filter filter,
			Pagination pagination, Sort sort)
		throws PortalException {

		List<PriceEntry> priceEntries = new ArrayList<>();

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		Locale locale = themeDisplay.getLocale();

		long commercePriceListId = ParamUtil.getLong(
			httpServletRequest, "commercePriceListId");

		BaseModelSearchResult<CommercePriceEntry>
			commercePriceEntryBaseModelSearchResult =
				_commercePriceEntryService.searchCommercePriceEntries(
					_portal.getCompanyId(httpServletRequest),
					commercePriceListId, filter.getKeywords(),
					pagination.getStartPosition(), pagination.getEndPosition(),
					sort);

		for (CommercePriceEntry commercePriceEntry :
				commercePriceEntryBaseModelSearchResult.getBaseModels()) {

			CPInstance cpInstance = commercePriceEntry.getCPInstance();

			CPDefinition cpDefinition = cpInstance.getCPDefinition();

			String name = cpDefinition.getName();

			CommercePriceList commercePriceList =
				commercePriceEntry.getCommercePriceList();

			CommerceMoney priceMoney = commercePriceEntry.getPriceMoney(
				commercePriceList.getCommerceCurrencyId());

			// TODO unit discount

			priceEntries.add(
				new PriceEntry(
					_getBasePrice(cpInstance, locale),
					new ImageField(
						name, "rounded", "lg", _getImage(cpDefinition)),
					name, commercePriceEntry.getCommercePriceEntryId(),
					cpInstance.getSku(),
					_getTieredPrice(commercePriceEntry, httpServletRequest),
					null, HtmlUtil.escape(priceMoney.format(locale))));
		}

		return priceEntries;
	}

	private String _getBasePrice(CPInstance cpInstance, Locale locale)
		throws PortalException {

		CommercePricingConfiguration commercePricingConfiguration =
			_configurationProvider.getSystemConfiguration(
				CommercePricingConfiguration.class);

		String commercePricingCalculationKey =
			commercePricingConfiguration.commercePricingCalculationKey();

		if (commercePricingCalculationKey.equals("v1.0")) {
			return _commercePriceFormatter.format(
				cpInstance.getPrice(), locale);
		}

		// TODO use method from product price calc

		return "";
	}

	private String _getImage(CPDefinition cpDefinition) {
		try {
			return cpDefinition.getDefaultImageThumbnailSrc();
		}
		catch (Exception e) {
			_log.error(e, e);
		}

		return StringPool.BLANK;
	}

	private String _getTieredPrice(
		CommercePriceEntry commercePriceEntry,
		HttpServletRequest httpServletRequest) {

		if (commercePriceEntry.isHasTierPrice()) {
			return LanguageUtil.get(httpServletRequest, "yes");
		}

		return LanguageUtil.get(httpServletRequest, "no");
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CommercePriceEntryDataSetDataProvider.class);

	@Reference
	private CommercePriceEntryService _commercePriceEntryService;

	@Reference
	private CommercePriceFormatter _commercePriceFormatter;

	@Reference
	private CommercePriceListService _commercePriceListService;

	@Reference
	private ConfigurationProvider _configurationProvider;

	@Reference
	private Portal _portal;

}