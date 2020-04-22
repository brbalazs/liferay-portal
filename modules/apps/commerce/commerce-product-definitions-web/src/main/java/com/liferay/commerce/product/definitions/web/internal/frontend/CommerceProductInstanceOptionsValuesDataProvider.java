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

package com.liferay.commerce.product.definitions.web.internal.frontend;

import com.liferay.commerce.context.CommerceContext;
import com.liferay.commerce.context.CommerceContextFactory;
import com.liferay.commerce.currency.model.CommerceMoney;
import com.liferay.commerce.currency.model.CommerceMoneyFactory;
import com.liferay.commerce.inventory.CPDefinitionInventoryEngine;
import com.liferay.commerce.price.CommerceProductPrice;
import com.liferay.commerce.price.CommerceProductPriceCalculation;
import com.liferay.commerce.product.model.CPDefinitionOptionRel;
import com.liferay.commerce.product.model.CPDefinitionOptionValueRel;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.permission.CommerceProductViewPermission;
import com.liferay.commerce.product.service.CPDefinitionOptionRelLocalService;
import com.liferay.commerce.product.service.CPDefinitionOptionValueRelLocalService;
import com.liferay.commerce.product.util.CPInstanceHelper;
import com.liferay.commerce.product.util.JsonHelper;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProvider;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderContext;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderException;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderRequest;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderResponse;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderResponseOutput;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.KeyValuePair;
import com.liferay.portal.kernel.util.ReleaseInfo;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Rafael Praxedes
 * @author Marco Leo
 */
@Component(
	immediate = true,
	property = "ddm.data.provider.instance.id=getCPInstanceOptionsValues",
	service = DDMDataProvider.class
)
public class CommerceProductInstanceOptionsValuesDataProvider
	implements DDMDataProvider {

	@Override
	public List<KeyValuePair> getData(
			DDMDataProviderContext ddmDataProviderContext)
		throws DDMDataProviderException {

		return Collections.emptyList();
	}

	@Override
	public DDMDataProviderResponse getData(
			DDMDataProviderRequest ddmDataProviderRequest)
		throws DDMDataProviderException {

		DDMDataProviderResponse.Builder ddmDataProviderResponseBuilder =
			DDMDataProviderResponse.Builder.newBuilder();

		long cpDefinitionId = _getParameter(
			ddmDataProviderRequest, "cpDefinitionId");

		long commerceAccountId = _getParameter(
			ddmDataProviderRequest, "commerceAccountId");

		long groupId = _getParameter(ddmDataProviderRequest, "groupId");

		try {
			if (!_commerceProductViewPermission.contains(
					PermissionThreadLocal.getPermissionChecker(),
					commerceAccountId, groupId, cpDefinitionId)) {

				return ddmDataProviderResponseBuilder.build();
			}
		}
		catch (PortalException pe) {
			_log.error(pe, pe);

			return ddmDataProviderResponseBuilder.build();
		}

		if (cpDefinitionId == 0) {
			return ddmDataProviderResponseBuilder.build();
		}

		Locale locale = ddmDataProviderRequest.getLocale();

		try {

			/**
			 * Extract the filters and the outputs based on fields that were
			 * filled or not in the data provider request
			 * Ex:
			 * If we have size and color as product options,
			 * In this case, we will have four possible states as follow:
			 * 1 - Size and Color both empty -> It won't do any query and it
			 * 	will return an empty result.
			 * 2 - Size filled and Color empty -> It'll assume that size will
			 * 	compose the query filter and color will be the output. So,
			 * 	it will return all colors for the specified size
			 * 3 - Size empty and Color filled - the same approach that item 2
			 * 4 - Size and Color both filled - the same approach that item 1
			 */
			Map<String, String> parameters =
				ddmDataProviderRequest.getParameters();

			List<CPDefinitionOptionRel> cpDefinitionOptionRels =
				_cpDefinitionOptionRelLocalService.getCPDefinitionOptionRels(
					cpDefinitionId, true);

			List<CPDefinitionOptionRel> requestedCPDefinitionOptionRels =
				new ArrayList<>();

			List<Long> skuCombinationCPDefinitionOptionValueRelIds =
				new ArrayList<>();

			for (CPDefinitionOptionRel cpDefinitionOptionRel :
					cpDefinitionOptionRels) {

				String parameterValue = parameters.get(
					cpDefinitionOptionRel.getKey());

				// Collect filters and outputs

				if (_jsonHelper.isEmpty(parameterValue)) {
					requestedCPDefinitionOptionRels.add(cpDefinitionOptionRel);
				}
				else {
					String optionValueKey = parameterValue;

					if (_jsonHelper.isArray(parameterValue)) {
						optionValueKey = _jsonHelper.getFirstElementStringValue(
							parameterValue);
					}

					CPDefinitionOptionValueRel cpDefinitionOptionValueRel =
						_cpDefinitionOptionValueRelLocalService.
							fetchCPDefinitionOptionValueRel(
								cpDefinitionOptionRel.
									getCPDefinitionOptionRelId(),
								optionValueKey);

					if (cpDefinitionOptionValueRel != null) {
						skuCombinationCPDefinitionOptionValueRelIds.add(
							cpDefinitionOptionValueRel.
								getCPDefinitionOptionValueRelId());
					}
				}
			}

			// Do search and populate the outputs if the outputs are not empty

			if (requestedCPDefinitionOptionRels.isEmpty()) {
				return ddmDataProviderResponseBuilder.build();
			}

			List<Output> outputs = new ArrayList<>();

			for (CPDefinitionOptionRel cpDefinitionOptionRel :
					requestedCPDefinitionOptionRels) {

				List<CPDefinitionOptionValueRel> renameMe =
					Collections.emptyList();

				if (skuCombinationCPDefinitionOptionValueRelIds.isEmpty()) {
					renameMe =
						_cpInstanceHelper.
							getCPInstanceCPDefinitionOptionValueRels(
								cpDefinitionId,
								cpDefinitionOptionRel.
									getCPDefinitionOptionRelId());
				}
				else {
					renameMe =
						_cpInstanceHelper.filterCPDefinitionOptionValueRels(
							cpDefinitionOptionRel.getCPDefinitionOptionRelId(),
							skuCombinationCPDefinitionOptionValueRelIds);
				}

				if (Validator.isNotNull(cpDefinitionOptionRel.getPriceType())) {
					renameMe = _filterByInvenntoryAvailable(renameMe);
				}

				outputs.add(
					new Output(
						cpDefinitionOptionRel.getKey(), "list",
						_toCPDefinitionOptionValueRelKeyValuePairs(
							renameMe, locale,
							_getCommerceContext(
								ddmDataProviderRequest.
									getHttpServletRequest()))));
			}

			return Output.toDDMDataProviderResponse(outputs);
		}
		catch (Exception e) {
			_log.error(e, e);
		}

		return ddmDataProviderResponseBuilder.build();
	}

	@Override
	public Class<?> getSettings() {
		throw new UnsupportedOperationException();
	}

	protected static class Output {

		public static DDMDataProviderResponse toDDMDataProviderResponse(
			List<Output> outputs) {

			if (ReleaseInfo.getBuildNumber() >= _RELEASE_7_2_0_BUILD_NUMBER) {
				DDMDataProviderResponse.Builder ddmDataProviderResponseBuilder =
					DDMDataProviderResponse.Builder.newBuilder();

				for (Output output : outputs) {
					ddmDataProviderResponseBuilder.withOutput(
						output._name, output._value);
				}

				return ddmDataProviderResponseBuilder.build();
			}

			DDMDataProviderResponseOutput[] ddmDataProviderResponseOutputs =
				new DDMDataProviderResponseOutput[outputs.size()];

			for (int i = 0; i < outputs.size(); i++) {
				Output output = outputs.get(i);

				ddmDataProviderResponseOutputs[i] =
					DDMDataProviderResponseOutput.of(
						output._name, output._type, output._value);
			}

			return DDMDataProviderResponse.of(ddmDataProviderResponseOutputs);
		}

		public Output(String name, String type, Object value) {
			_name = name;
			_type = type;
			_value = value;
		}

		private final String _name;
		private final String _type;
		private final Object _value;

	}

	private List<CPDefinitionOptionValueRel> _filterByInvenntoryAvailable(
		List<CPDefinitionOptionValueRel> cpDefinitionOptionValueRels) {

		List<CPDefinitionOptionValueRel> filtered = new ArrayList<>();

		for (CPDefinitionOptionValueRel cpDefinitionOptionValueRel :
				cpDefinitionOptionValueRels) {

			if (Validator.isNull(
					cpDefinitionOptionValueRel.getCPInstanceUuid())) {

				filtered.add(cpDefinitionOptionValueRel);

				continue;
			}

			CPInstance cpInstance =
				cpDefinitionOptionValueRel.fetchCPInstance();

			if (cpInstance == null) {
				continue;
			}

			try {
				if ((cpDefinitionOptionValueRel.getQuantity() >
						_cpDefinitionInventoryEngine.getMaxOrderQuantity(
							cpInstance)) ||
					(cpDefinitionOptionValueRel.getQuantity() <
						_cpDefinitionInventoryEngine.getMinOrderQuantity(
							cpInstance))) {

					continue;
				}
			}
			catch (PortalException pe) {
				_log.error(
					"Unable to determinate allowed order quantity for CP instance ID " +
						cpInstance.getCPInstanceId());

				continue;
			}

			filtered.add(cpDefinitionOptionValueRel);
		}

		return filtered;
	}

	private CommerceContext _getCommerceContext(
		HttpServletRequest httpServletRequest) {

		return _commerceContextFactory.create(httpServletRequest);
	}

	private CommerceMoney _getCommerceMoney(
			CPDefinitionOptionValueRel cpDefinitionOptionValueRel,
			CommerceContext commerceContext)
		throws PortalException {

		CPInstance cpInstance = cpDefinitionOptionValueRel.fetchCPInstance();

		if (cpInstance != null) {
			return _getCommerceProductFinalPrice(
				cpInstance.getCPInstanceId(),
				cpDefinitionOptionValueRel.getQuantity(), commerceContext);
		}

		return _commerceMoneyFactory.create(
			commerceContext.getCommerceCurrency(),
			cpDefinitionOptionValueRel.getPrice());
	}

	private CommerceMoney _getCommerceProductFinalPrice(
			long cpInstanceId, int quantity, CommerceContext commerceContext)
		throws PortalException {

		CommerceProductPrice commerceProductPrice =
			_commerceProductPriceCalculation.getCommerceProductPrice(
				cpInstanceId, quantity, true, commerceContext);

		return commerceProductPrice.getFinalPrice();
	}

	private long _getParameter(
		DDMDataProviderRequest ddmDataProviderRequest, String param) {

		Map<String, String> parameters = ddmDataProviderRequest.getParameters();

		return GetterUtil.getLong(parameters.get(param));
	}

	private List<KeyValuePair> _toCPDefinitionOptionValueRelKeyValuePairs(
			List<CPDefinitionOptionValueRel> cpDefinitionOptionValueRels,
			Locale locale, CommerceContext commerceContext)
		throws PortalException {

		if (cpDefinitionOptionValueRels.isEmpty()) {
			return Collections.emptyList();
		}

		List<KeyValuePair> keyValuePairs = new ArrayList<>();

		for (CPDefinitionOptionValueRel cpDefinitionOptionValueRel :
				cpDefinitionOptionValueRels) {

			CommerceMoney commerceMoney = _getCommerceMoney(
				cpDefinitionOptionValueRel, commerceContext);

			keyValuePairs.add(
				new KeyValuePair(
					cpDefinitionOptionValueRel.getKey(),
					String.format(
						"%s %s", cpDefinitionOptionValueRel.getName(locale),
						commerceMoney.format(locale))));
		}

		return keyValuePairs;
	}

	private static final int _RELEASE_7_2_0_BUILD_NUMBER =
		ReleaseInfo.RELEASE_7_1_0_BUILD_NUMBER + 100;

	private static final Log _log = LogFactoryUtil.getLog(
		CommerceProductInstanceOptionsValuesDataProvider.class);

	@Reference
	private CommerceContextFactory _commerceContextFactory;

	@Reference
	private CommerceMoneyFactory _commerceMoneyFactory;

	@Reference
	private CommerceProductPriceCalculation _commerceProductPriceCalculation;

	@Reference
	private CommerceProductViewPermission _commerceProductViewPermission;

	@Reference
	private CPDefinitionInventoryEngine _cpDefinitionInventoryEngine;

	@Reference
	private CPDefinitionOptionRelLocalService
		_cpDefinitionOptionRelLocalService;

	@Reference
	private CPDefinitionOptionValueRelLocalService
		_cpDefinitionOptionValueRelLocalService;

	@Reference
	private CPInstanceHelper _cpInstanceHelper;

	@Reference
	private JsonHelper _jsonHelper;

}