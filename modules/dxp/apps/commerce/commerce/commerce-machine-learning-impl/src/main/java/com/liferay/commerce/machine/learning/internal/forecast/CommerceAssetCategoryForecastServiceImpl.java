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

package com.liferay.commerce.machine.learning.internal.forecast;

import com.liferay.commerce.machine.learning.internal.forecast.api.CommerceAssetCategoryForecastService;
import com.liferay.commerce.machine.learning.internal.forecast.api.ForecastLevel;
import com.liferay.commerce.machine.learning.internal.forecast.api.ForecastPeriod;
import com.liferay.commerce.machine.learning.internal.forecast.api.ForecastTarget;
import com.liferay.commerce.machine.learning.internal.forecast.model.AssetCategoryForecast;
import com.liferay.commerce.machine.learning.internal.forecast.model.AssetCategoryForecastImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.generic.TermQueryImpl;
import com.liferay.portal.kernel.util.GetterUtil;

import java.util.Date;
import java.util.List;

import org.osgi.service.component.annotations.Component;

/**
 * @author Riccardo Ferrari
 */
@Component(
	immediate = true, service = CommerceAssetCategoryForecastService.class
)
public class CommerceAssetCategoryForecastServiceImpl
	extends BaseCommerceForecastServiceImpl<AssetCategoryForecast>
	implements CommerceAssetCategoryForecastService {

	@Override
	public List<AssetCategoryForecast> getMonthlyForecastByRevenue(
			long companyId, long categoryId)
		throws PortalException {

		Date now = new Date();

		ForecastPeriod forecastPeriod = ForecastPeriod.MONTH;

		ForecastTarget forecastTarget = ForecastTarget.REVENUE;

		Date startDate = getStartDate(now, forecastPeriod);

		Date endDate = getEndDate(forecastPeriod);

		BooleanQuery booleanQuery = getBaseQuery(
			ForecastLevel.CATEGORY.getLabel(), forecastPeriod.getLabel(),
			forecastTarget.getLabel(), startDate, endDate);

		TermQueryImpl commerceAccountIdTermQuery = new TermQueryImpl(
			Field.ASSET_CATEGORY_ID, String.valueOf(categoryId));

		booleanQuery.add(commerceAccountIdTermQuery, BooleanClauseOccur.MUST);

		return getSearchResults(companyId, booleanQuery, startDate, endDate);
	}

	@Override
	protected AssetCategoryForecast toForecastModel(Document document) {
		AssetCategoryForecast assetCategoryForecast = getBaseForecastModel(
			new AssetCategoryForecastImpl(), document);

		assetCategoryForecast.setAssetCategoryId(
			GetterUtil.getLong(document.get(Field.ASSET_CATEGORY_ID)));

		return assetCategoryForecast;
	}

}