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
import com.liferay.portal.kernel.search.generic.TermQueryImpl;
import com.liferay.portal.kernel.util.GetterUtil;

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

		CommerceMLForecastScope commerceMLForecastScope =
			CommerceMLForecastScope.COMMERCE_ACCOUNT;

		CommerceMLForecastTarget commerceMLForecastTarget =
			CommerceMLForecastTarget.REVENUE;

		Date now = new Date();

		Date startDate = getStartDate(now, commerceMLForecastPeriod);

		Date endDate = getEndDate(now, commerceMLForecastPeriod);

		BooleanQuery booleanQuery = getBaseQuery(
			commerceMLForecastScope.getLabel(),
			commerceMLForecastPeriod.getLabel(),
			commerceMLForecastTarget.getLabel(), startDate, endDate);

		TermQueryImpl commerceAccountIdTermQuery = new TermQueryImpl(
			CommerceMLForecastField.COMMERCE_ACCOUNT_ID,
			String.valueOf(commerceAccountId));

		booleanQuery.add(commerceAccountIdTermQuery, BooleanClauseOccur.MUST);

		return getSearchResults(companyId, booleanQuery, startDate, endDate);
	}

	@Override
	protected CommerceAccountCommerceMLForecast toForecastModel(
		Document document) {

		CommerceAccountCommerceMLForecast commerceAccountCommerceMLForecast =
			getBaseForecastModel(
				new CommerceAccountCommerceMLForecastImpl(), document);

		commerceAccountCommerceMLForecast.setCommerceAccountId(
			GetterUtil.getLong(
				document.get(CommerceMLForecastField.COMMERCE_ACCOUNT_ID)));

		return commerceAccountCommerceMLForecast;
	}

}