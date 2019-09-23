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

import com.liferay.commerce.machine.learning.forecast.model.CommerceAccountCommerceMLForecast;
import com.liferay.commerce.machine.learning.forecast.service.CommerceAccountCommerceMLForecastService;
import com.liferay.commerce.machine.learning.internal.forecast.constants.CommerceMLForecastField;
import com.liferay.commerce.machine.learning.internal.forecast.constants.CommerceMLForecastPeriod;
import com.liferay.commerce.machine.learning.internal.forecast.constants.CommerceMLForecastScope;
import com.liferay.commerce.machine.learning.internal.forecast.constants.CommerceMLForecastTarget;
import com.liferay.commerce.machine.learning.internal.forecast.model.CommerceAccountCommerceMLForecastImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.GroupBy;
import com.liferay.portal.kernel.search.filter.BooleanFilter;
import com.liferay.portal.kernel.search.filter.TermsFilter;
import com.liferay.portal.kernel.search.generic.TermQueryImpl;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.search.engine.adapter.search.SearchSearchRequest;

import java.util.Date;
import java.util.List;

import org.osgi.service.component.annotations.Component;

/**
 * @author Riccardo Ferrari
 */
@Component(
	immediate = true, service = CommerceAccountCommerceMLForecastService.class
)
public class CommerceAccountCommerceMLForecastServiceImpl
	extends BaseCommerceMLForecastServiceImpl<CommerceAccountCommerceMLForecast>
	implements CommerceAccountCommerceMLForecastService {

	@Override
	public List<CommerceAccountCommerceMLForecast>
			getMonthlyRevenueCommerceAccountCommerceMLForecasts(
				long companyId, long commerceAccountId)
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
	public List<CommerceAccountCommerceMLForecast>
			getMonthlyRevenueCommerceAccountCommerceMLForecasts(
				long companyId, long[] commerceAccountIds, Date actualDate,
				int historyLength, int forecastLength)
		throws PortalException {

		CommerceMLForecastPeriod commerceMLForecastPeriod =
			CommerceMLForecastPeriod.MONTH;

		CommerceMLForecastTarget commerceMLForecastTarget =
			CommerceMLForecastTarget.REVENUE;

		Date endDate = getEndDate(
			actualDate, commerceMLForecastPeriod, forecastLength);

		Date startDate = getStartDate(
			actualDate, commerceMLForecastPeriod, historyLength);

		BooleanQuery baseQuery = getBaseQuery(
			_commerceMLForecastScope.getLabel(),
			commerceMLForecastPeriod.getLabel(),
			commerceMLForecastTarget.getLabel(), startDate, endDate);

		BooleanFilter preBooleanFilter = baseQuery.getPreBooleanFilter();

		TermsFilter termsFilter = new TermsFilter(
			CommerceMLForecastField.COMMERCE_ACCOUNT_ID);

		termsFilter.addValues(ArrayUtil.toStringArray(commerceAccountIds));

		preBooleanFilter.add(termsFilter, BooleanClauseOccur.MUST);

		GroupBy groupBy = new GroupBy(
			CommerceMLForecastField.COMMERCE_ACCOUNT_ID);

		groupBy.setStart(0);

		groupBy.setSize(historyLength);

		SearchSearchRequest searchRequest = getSearchRequest(
			commerceMLIndexer.getIndexName(companyId), baseQuery, 0, 0, true);

		searchRequest.setGroupBy(groupBy);

		return getSearchResults(searchRequest);
	}

	@Override
	protected CommerceAccountCommerceMLForecast toForecastModel(
		Document document) {

		CommerceAccountCommerceMLForecast commerceAccountCommerceMLForecast =
			getBaseCommerceMLForecastModel(
				new CommerceAccountCommerceMLForecastImpl(), document);

		commerceAccountCommerceMLForecast.setCommerceAccountId(
			GetterUtil.getLong(
				document.get(CommerceMLForecastField.COMMERCE_ACCOUNT_ID)));

		return commerceAccountCommerceMLForecast;
	}

	private static final CommerceMLForecastScope _commerceMLForecastScope =
		CommerceMLForecastScope.COMMERCE_ACCOUNT;

}