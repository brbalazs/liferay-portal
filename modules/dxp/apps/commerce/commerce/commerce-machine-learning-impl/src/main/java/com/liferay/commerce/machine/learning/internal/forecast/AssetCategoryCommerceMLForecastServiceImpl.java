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

import com.liferay.commerce.machine.learning.forecast.model.AssetCategoryCommerceMLForecast;
import com.liferay.commerce.machine.learning.forecast.service.AssetCategoryCommerceMLForecastService;
import com.liferay.commerce.machine.learning.internal.forecast.constants.CommerceMLForecastField;
import com.liferay.commerce.machine.learning.internal.forecast.constants.CommerceMLForecastPeriod;
import com.liferay.commerce.machine.learning.internal.forecast.constants.CommerceMLForecastScope;
import com.liferay.commerce.machine.learning.internal.forecast.constants.CommerceMLForecastTarget;
import com.liferay.commerce.machine.learning.internal.forecast.model.AssetCategoryCommerceMLForecastImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.generic.TermQueryImpl;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.search.engine.adapter.search.SearchSearchRequest;

import java.util.Date;
import java.util.List;

import org.osgi.service.component.annotations.Component;

/**
 * @author Riccardo Ferrari
 */
@Component(
	immediate = true, service = AssetCategoryCommerceMLForecastService.class
)
public class AssetCategoryCommerceMLForecastServiceImpl
	extends BaseCommerceMLForecastServiceImpl<AssetCategoryCommerceMLForecast>
	implements AssetCategoryCommerceMLForecastService {

	@Override
	public List<AssetCategoryCommerceMLForecast>
			getMonthlyRevenueAssetCategoryCommerceMLForecasts(
				long companyId, long assetCategoryId, long commerceAccountId)
		throws PortalException {

		CommerceMLForecastPeriod commerceMLForecastPeriod =
			CommerceMLForecastPeriod.MONTH;

		CommerceMLForecastTarget commerceMLForecastTarget =
			CommerceMLForecastTarget.REVENUE;

		Date now = new Date();

		Date startDate = getStartDate(
			now, commerceMLForecastPeriod, DEFAULT_HISTORY_LENGTH);

		Date endDate = getEndDate(
			now, commerceMLForecastPeriod, DEFAULT_FORECAST_LENGTH);

		BooleanQuery booleanQuery = getBaseQuery(
			_commerceMLForecastScope.getLabel(),
			commerceMLForecastPeriod.getLabel(),
			commerceMLForecastTarget.getLabel(), startDate, endDate);

		TermQueryImpl assetCategoryIdTermQuery = new TermQueryImpl(
			Field.ASSET_CATEGORY_ID, String.valueOf(assetCategoryId));

		booleanQuery.add(assetCategoryIdTermQuery, BooleanClauseOccur.MUST);

		TermQueryImpl commerceAccountIdTermQuery = new TermQueryImpl(
			CommerceMLForecastField.COMMERCE_ACCOUNT_ID,
			String.valueOf(commerceAccountId));

		booleanQuery.add(commerceAccountIdTermQuery, BooleanClauseOccur.MUST);

		int size = getSearchSize(startDate, endDate, commerceMLForecastPeriod);

		SearchSearchRequest searchRequest = getSearchRequest(
			commerceMLIndexer.getIndexName(companyId), booleanQuery, 0, size,
			true);

		return getSearchResults(searchRequest);
	}

	@Override
	protected AssetCategoryCommerceMLForecast toForecastModel(
		Document document) {

		AssetCategoryCommerceMLForecast assetCategoryCommerceMLForecast =
			getBaseCommerceMLForecastModel(
				new AssetCategoryCommerceMLForecastImpl(), document);

		assetCategoryCommerceMLForecast.setAssetCategoryId(
			GetterUtil.getLong(document.get(Field.ASSET_CATEGORY_ID)));

		assetCategoryCommerceMLForecast.setCommerceAccountId(
			GetterUtil.getLong(
				document.get(CommerceMLForecastField.COMMERCE_ACCOUNT_ID)));

		return assetCategoryCommerceMLForecast;
	}

	private static final CommerceMLForecastScope _commerceMLForecastScope =
		CommerceMLForecastScope.ASSET_CATEGORY;

}