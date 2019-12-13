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

package com.liferay.headless.commerce.machine.learning.resource.v1_0;

import com.liferay.headless.commerce.machine.learning.dto.v1_0.AccountForecast;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;

import java.util.Date;

import javax.annotation.Generated;

/**
 * To access this resource, run:
 *
 *     curl -u your@email.com:yourpassword -D - http://localhost:8080/o/headless-commerce-machine-learning/v1.0
 *
 * @author Riccardo Ferrari
 * @generated
 */
@Generated("")
public interface AccountForecastResource {

	public Page<AccountForecast> getAccountForecastsByMonthlyRevenuePage(
			Long[] accountIds, Date forecastStartDate, Integer historyLength,
			Integer forecastLength, Pagination pagination)
		throws Exception;

	public void setContextCompany(Company contextCompany);

}