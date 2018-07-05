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

package com.liferay.commerce.initializer.beryl.internal;

import com.liferay.commerce.forecast.model.CommerceForecastEntry;
import com.liferay.commerce.forecast.model.CommerceForecastEntryConstants;
import com.liferay.commerce.forecast.service.CommerceForecastEntryLocalService;
import com.liferay.commerce.forecast.service.CommerceForecastValueLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.util.Validator;

import java.math.BigDecimal;

import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Andrea Di Giorgi
 */
@Component(service = BerylSampleForecastsInitializer.class)
public class BerylSampleForecastsInitializer
	extends BaseBerylSampleInitializer {

	@Override
	protected Log getLog() {
		return _log;
	}

	@Override
	protected String getName() {
		return "forecasts";
	}

	@Override
	protected void importSample(
			long now, JSONObject jsonObject, Group group,
			long[] accountOrganizationIds, Map<String, Long> cpInstanceSKUsMap)
		throws PortalException {

		long time = now + jsonObject.getLong("time");
		int period = CommerceForecastEntryConstants.getLabelPeriod(
			jsonObject.getString("period"));
		int target = CommerceForecastEntryConstants.getLabelTarget(
			jsonObject.getString("target"));
		BigDecimal assertivity = BigDecimal.valueOf(Math.random());

		long customerId = 0;

		int jsonCustomerId = jsonObject.getInt("customerId");

		if (jsonCustomerId > 0) {
			if (jsonCustomerId <= accountOrganizationIds.length) {
				customerId = accountOrganizationIds[jsonCustomerId - 1];
			}
			else {
				if (_log.isWarnEnabled()) {
					_log.warn(
						"Ignoring imported forecast for customer " +
							jsonCustomerId);
				}

				return;
			}
		}

		long cpInstanceId = 0;

		String sku = jsonObject.getString("sku");

		if (Validator.isNotNull(sku)) {
			if (cpInstanceSKUsMap.containsKey(sku)) {
				cpInstanceId = cpInstanceSKUsMap.get(sku);
			}
			else {
				if (_log.isWarnEnabled()) {
					_log.warn("Ignoring imported forecast for SKU " + sku);
				}

				return;
			}
		}

		CommerceForecastEntry commerceForecastEntry =
			_commerceForecastEntryLocalService.addCommerceForecastEntry(
				group.getCompanyId(), group.getCreatorUserId(), time, period,
				target, customerId, cpInstanceId, assertivity);

		JSONArray valuesJSONArray = jsonObject.getJSONArray("values");

		for (int i = 0; i < valuesJSONArray.length(); i++) {
			JSONObject valueJSONObject = valuesJSONArray.getJSONObject(i);

			long valueTime = now + valueJSONObject.getLong("time");
			BigDecimal lowerValue = getBigDecimal(
				valueJSONObject, "lowerValue");
			BigDecimal value = getBigDecimal(valueJSONObject, "value");
			BigDecimal upperValue = getBigDecimal(
				valueJSONObject, "upperValue");

			_commerceForecastValueLocalService.addCommerceForecastValue(
				group.getCreatorUserId(),
				commerceForecastEntry.getCommerceForecastEntryId(), valueTime,
				lowerValue, value, upperValue);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		BerylSampleForecastsInitializer.class);

	@Reference
	private CommerceForecastEntryLocalService
		_commerceForecastEntryLocalService;

	@Reference
	private CommerceForecastValueLocalService
		_commerceForecastValueLocalService;

}