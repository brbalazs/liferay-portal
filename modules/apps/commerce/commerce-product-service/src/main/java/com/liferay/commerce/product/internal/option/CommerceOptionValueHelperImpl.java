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

package com.liferay.commerce.product.internal.option;

import com.liferay.commerce.product.model.CPDefinitionOptionRel;
import com.liferay.commerce.product.model.CPDefinitionOptionValueRel;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.option.CommerceOptionValue;
import com.liferay.commerce.product.option.CommerceOptionValueHelper;
import com.liferay.commerce.product.service.CPInstanceOptionValueRelLocalService;
import com.liferay.commerce.product.service.CPInstanceService;
import com.liferay.commerce.product.util.CPInstanceHelper;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Igor Beslic
 */
@Component(immediate = true, service = CommerceOptionValueHelper.class)
public class CommerceOptionValueHelperImpl
	implements CommerceOptionValueHelper {

	@Override
	public List<CommerceOptionValue> getCPInstanceCommerceOptionValues(
			long cpInstanceId)
		throws PortalException {

		CPInstance cpInstance = _cpInstanceService.getCPInstance(cpInstanceId);

		if (!_cpInstanceOptionValueRelLocalService.hasCPInstanceOptionValueRel(
				cpInstance.getCPInstanceId())) {

			return Collections.emptyList();
		}

		List<CommerceOptionValue> commerceOptionValues = new ArrayList<>();

		Map<CPDefinitionOptionRel, List<CPDefinitionOptionValueRel>>
			cpInstanceCPDefinitionOptionRelsMap =
				_cpInstanceHelper.getCPInstanceCPDefinitionOptionRelsMap(
					cpInstanceId);

		cpInstanceCPDefinitionOptionRelsMap.forEach(
			(key, value) -> {
				if (Validator.isNull(key.getPriceType()) || (value == null) ||
					(value.size() != 1)) {

					return;
				}

				CommerceOptionValueImpl.Builder commerceOptionValueBuilder =
					new CommerceOptionValueImpl.Builder();

				commerceOptionValueBuilder.optionKey(key.getKey());
				commerceOptionValueBuilder.priceType(key.getPriceType());

				CPDefinitionOptionValueRel cpDefinitionOptionValueRel =
					value.get(0);

				commerceOptionValueBuilder.price(
					cpDefinitionOptionValueRel.getPrice());
				commerceOptionValueBuilder.quantity(
					cpDefinitionOptionValueRel.getQuantity());

				CPInstance cpDefinitionOptionValueRelCPInstance =
					cpDefinitionOptionValueRel.fetchCPInstance();

				if (cpDefinitionOptionValueRelCPInstance != null) {
					commerceOptionValueBuilder.cpInstanceId(
						cpDefinitionOptionValueRelCPInstance.getCPInstanceId());
					commerceOptionValueBuilder.price(
						cpDefinitionOptionValueRelCPInstance.getPrice());
				}

				commerceOptionValues.add(commerceOptionValueBuilder.build());
			});

		return commerceOptionValues;
	}

	@Reference
	private CPInstanceHelper _cpInstanceHelper;

	@Reference
	private CPInstanceOptionValueRelLocalService
		_cpInstanceOptionValueRelLocalService;

	@Reference
	private CPInstanceService _cpInstanceService;

}