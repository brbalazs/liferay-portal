/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portlet.asset.validator;

import com.liferay.asset.kernel.validator.AssetEntryValidator;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.registry.collections.ServiceTrackerCollections;
import com.liferay.registry.collections.ServiceTrackerMap;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Julio Camarero
 */
public class AssetEntryValidatorRegistry {

	public void afterPropertiesSet() {
		_serviceTrackerMap = ServiceTrackerCollections.openMultiValueMap(
			AssetEntryValidator.class, "model.class.name");
	}

	public void destroy() {
		_serviceTrackerMap.close();
	}

	public List<AssetEntryValidator> getAssetEntryValidators(String className) {
		List<AssetEntryValidator> assetEntryValidators = new ArrayList<>();

		List<AssetEntryValidator> generalAssetEntryValidators =
			_serviceTrackerMap.getService("*");

		if (ListUtil.isNotEmpty(generalAssetEntryValidators)) {
			assetEntryValidators.addAll(generalAssetEntryValidators);
		}

		if (Validator.isNotNull(className)) {
			List<AssetEntryValidator> classNameAssetEntryValidators =
				_serviceTrackerMap.getService(className);

			if (ListUtil.isNotEmpty(classNameAssetEntryValidators)) {
				assetEntryValidators.addAll(classNameAssetEntryValidators);
			}
		}

		return assetEntryValidators;
	}

	private ServiceTrackerMap<String, List<AssetEntryValidator>>
		_serviceTrackerMap;

}