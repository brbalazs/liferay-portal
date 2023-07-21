/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.data.provider.instance;

import com.liferay.dynamic.data.mapping.data.provider.DDMDataProvider;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderContext;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderException;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderOutputParametersSettings;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderParameterSettings;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderRequest;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderResponse;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderResponseOutput;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderTracker;
import com.liferay.dynamic.data.mapping.io.DDMFormValuesJSONDeserializer;
import com.liferay.dynamic.data.mapping.model.DDMDataProviderInstance;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.service.DDMDataProviderInstanceService;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.dynamic.data.mapping.util.DDMFormFactory;
import com.liferay.dynamic.data.mapping.util.DDMFormInstanceFactory;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ClassUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.KeyValuePair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Rafael Praxedes
 */
@Component(
	immediate = true,
	property = "ddm.data.provider.instance.id=getDataProviderInstanceOutputParameters",
	service = DDMDataProvider.class
)
public class DDMDataProviderInstanceOutputParametersDataProvider
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

		long dataProviderInstanceId = GetterUtil.getLong(
			ddmDataProviderRequest.getParameter("dataProviderInstanceId"));

		if (dataProviderInstanceId == 0) {
			return DDMDataProviderResponse.of();
		}

		List<KeyValuePair> data = new ArrayList<>();

		try {
			DDMDataProviderOutputParametersSettings[]
				ddmDataProviderOutputParametersSettings =
					getDDMDataProviderOutputParametersSettings(
						dataProviderInstanceId);

			for (DDMDataProviderOutputParametersSettings
					ddmDataProviderOutputParametersSetting :
						ddmDataProviderOutputParametersSettings) {

				data.add(
					new KeyValuePair(
						ddmDataProviderOutputParametersSetting.
							outputParameterId(),
						ddmDataProviderOutputParametersSetting.
							outputParameterName()));
			}
		}
		catch (Exception e) {
			_log.error(
				String.format(
					"Unable to get the output parameters for data provider " +
						"instance with id '%d'",
					dataProviderInstanceId),
				e);
		}

		return DDMDataProviderResponse.of(
			DDMDataProviderResponseOutput.of(
				"outputParameterNames", "list", data));
	}

	@Override
	public Class<?> getSettings() {
		throw new UnsupportedOperationException();
	}

	protected DDMFormValues getDataProviderInstanceFormValues(
			DDMDataProvider ddmDataProvider,
			DDMDataProviderInstance ddmDataProviderInstance)
		throws PortalException {

		DDMForm ddmForm = DDMFormFactory.create(ddmDataProvider.getSettings());

		return _ddmFormValuesJSONDeserializer.deserialize(
			ddmForm, ddmDataProviderInstance.getDefinition());
	}

	protected DDMDataProviderOutputParametersSettings[]
			getDDMDataProviderOutputParametersSettings(
				long dataProviderInstanceId)
		throws Exception {

		DDMDataProviderInstance ddmDataProviderInstance =
			_ddmDataProviderInstanceService.getDataProviderInstance(
				dataProviderInstanceId);

		DDMDataProvider ddmDataProvider =
			_ddmDataProviderTracker.getDDMDataProvider(
				ddmDataProviderInstance.getType());

		if (!ClassUtil.isSubclass(
				ddmDataProvider.getSettings(),
				DDMDataProviderParameterSettings.class)) {

			return new DDMDataProviderOutputParametersSettings[0];
		}

		DDMFormValues dataProviderFormValues =
			getDataProviderInstanceFormValues(
				ddmDataProvider, ddmDataProviderInstance);

		DDMDataProviderParameterSettings ddmDataProviderParameterSetting =
			DDMFormInstanceFactory.create(
				DDMDataProviderParameterSettings.class, dataProviderFormValues);

		return ddmDataProviderParameterSetting.outputParameters();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DDMDataProviderInstanceOutputParametersDataProvider.class);

	@Reference
	private DDMDataProviderInstanceService _ddmDataProviderInstanceService;

	@Reference
	private DDMDataProviderTracker _ddmDataProviderTracker;

	@Reference
	private DDMFormValuesJSONDeserializer _ddmFormValuesJSONDeserializer;

}