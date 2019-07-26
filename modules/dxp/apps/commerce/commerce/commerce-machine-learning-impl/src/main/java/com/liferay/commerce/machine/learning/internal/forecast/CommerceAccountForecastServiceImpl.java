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

import com.liferay.commerce.machine.learning.internal.forecast.api.CommerceAccountForecastService;
import com.liferay.commerce.machine.learning.internal.forecast.api.CommerceForecastField;
import com.liferay.commerce.machine.learning.internal.forecast.api.ForecastLevel;
import com.liferay.commerce.machine.learning.internal.forecast.api.ForecastPeriod;
import com.liferay.commerce.machine.learning.internal.forecast.api.ForecastTarget;
import com.liferay.commerce.machine.learning.internal.forecast.model.CommerceAccountForecast;
import com.liferay.commerce.machine.learning.internal.forecast.model.CommerceAccountForecastImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.generic.TermQueryImpl;
import com.liferay.portal.kernel.util.GetterUtil;

import java.util.Date;
import java.util.List;

import org.osgi.service.component.annotations.Component;

/**
 * @author Riccardo Ferrari
 */
@Component(immediate = true, service = CommerceAccountForecastService.class)
public class CommerceAccountForecastServiceImpl
	extends BaseCommerceForecastServiceImpl<CommerceAccountForecast>
	implements CommerceAccountForecastService {

	@Override
	public List<CommerceAccountForecast> getMonthlyForecastByRevenue(
			long companyId, long commerceAccountId)
		throws PortalException {

		Date now = new Date();

		ForecastPeriod forecastPeriod = ForecastPeriod.MONTH;

		Date startDate = getStartDate(now, forecastPeriod);

		Date endDate = getEndDate(forecastPeriod);

		BooleanQuery booleanQuery = getBaseQuery(
			ForecastLevel.COMMERCE_ACCOUNT.getLabel(),
			forecastPeriod.getLabel(), ForecastTarget.REVENUE.getLabel(),
			startDate, endDate);

		TermQueryImpl commerceAccountIdTermQuery = new TermQueryImpl(
			CommerceForecastField.COMMERCE_ACCOUNT_ID,
			String.valueOf(commerceAccountId));

		booleanQuery.add(commerceAccountIdTermQuery, BooleanClauseOccur.MUST);

		return getSearchResults(companyId, booleanQuery, startDate, endDate);
	}

	@Override
	protected CommerceAccountForecast toForecastModel(Document document) {
		CommerceAccountForecast commerceAccountForecast = getBaseForecastModel(
			new CommerceAccountForecastImpl(), document);

		commerceAccountForecast.setCommerceAccountId(
			GetterUtil.getLong(
				document.get(CommerceForecastField.COMMERCE_ACCOUNT_ID)));

		return commerceAccountForecast;
	}

}