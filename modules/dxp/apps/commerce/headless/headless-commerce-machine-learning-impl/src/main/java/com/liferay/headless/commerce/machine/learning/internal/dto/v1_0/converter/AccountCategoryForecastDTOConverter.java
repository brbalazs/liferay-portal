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

package com.liferay.headless.commerce.machine.learning.internal.dto.v1_0.converter;

import com.liferay.commerce.machine.learning.forecast.model.AssetCategoryCommerceMLForecast;
import com.liferay.commerce.machine.learning.forecast.service.AssetCategoryCommerceMLForecastService;
import com.liferay.headless.commerce.core.dto.v1_0.converter.DTOConverter;
import com.liferay.headless.commerce.core.dto.v1_0.converter.DTOConverterContext;
import com.liferay.headless.commerce.machine.learning.dto.v1_0.AccountCategoryForecast;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Riccardo Ferrari
 */
@Component(
	property = "model.class.name=com.liferay.commerce.machine.learning.forecast.model.AssetCategoryCommerceMLForecast",
	service = {AccountCategoryForecastDTOConverter.class, DTOConverter.class}
)
public class AccountCategoryForecastDTOConverter implements DTOConverter {

	@Override
	public String getContentType() {
		return AssetCategoryCommerceMLForecast.class.getSimpleName();
	}

	@Override
	public Object toDTO(DTOConverterContext dtoConverterContext)
		throws Exception {

		CommerceMLForecastCompositeResourcePrimaryKey compositeResourcePrimKey =
			(CommerceMLForecastCompositeResourcePrimaryKey)
				dtoConverterContext.getCompositeResourcePrimKey();

		AssetCategoryCommerceMLForecast assetCategoryCommerceMLForecast =
			_assetCategoryCommerceMLForecastService.
				getAssetCategoryCommerceMLForecast(
					compositeResourcePrimKey.getCompanyId(),
					compositeResourcePrimKey.getForecastId());

		return new AccountCategoryForecast() {
			{
				account =
					assetCategoryCommerceMLForecast.getCommerceAccountId();
				actual = assetCategoryCommerceMLForecast.getActual();
				category = assetCategoryCommerceMLForecast.getAssetCategoryId();
				forecast = assetCategoryCommerceMLForecast.getForecast();
				forecastLowerBound =
					assetCategoryCommerceMLForecast.getForecastLowerBound();
				forecastUpperBound =
					assetCategoryCommerceMLForecast.getForecastUpperBound();
				timestamp = assetCategoryCommerceMLForecast.getTimestamp();
				unit = assetCategoryCommerceMLForecast.getTarget();
			}
		};
	}

	@Reference
	private AssetCategoryCommerceMLForecastService
		_assetCategoryCommerceMLForecastService;

}