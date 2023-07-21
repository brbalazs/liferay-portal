/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.data.provider.instance;

import com.liferay.dynamic.data.mapping.data.provider.DDMDataProvider;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderContext;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderException;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderRequest;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderResponse;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderResponseOutput;
import com.liferay.dynamic.data.mapping.model.DDMDataProviderInstance;
import com.liferay.dynamic.data.mapping.service.DDMDataProviderInstanceLocalService;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.KeyValuePair;
import com.liferay.portal.kernel.util.LocaleThreadLocal;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Rafael Praxedes
 */
@Component(
	immediate = true,
	property = "ddm.data.provider.instance.id=getDataProviderInstances",
	service = DDMDataProvider.class
)
public class DDMDataProviderInstancesDataProvider implements DDMDataProvider {

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

		List<KeyValuePair> data = new ArrayList<>();

		try {
			HttpServletRequest request =
				ddmDataProviderRequest.getHttpServletRequest();

			long scopeGroupId = ParamUtil.getLong(request, "scopeGroupId");

			if (scopeGroupId == 0) {
				ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
					WebKeys.THEME_DISPLAY);

				scopeGroupId = themeDisplay.getScopeGroupId();
			}

			long[] groupIds = _portal.getCurrentAndAncestorSiteGroupIds(
				scopeGroupId);

			List<DDMDataProviderInstance> ddmDataProviderInstances =
				_ddmDataProviderInstanceLocalService.getDataProviderInstances(
					groupIds);

			for (DDMDataProviderInstance ddmDataProviderInstance :
					ddmDataProviderInstances) {

				long value =
					ddmDataProviderInstance.getDataProviderInstanceId();
				String label = ddmDataProviderInstance.getName(
					LocaleThreadLocal.getThemeDisplayLocale());

				data.add(new KeyValuePair(String.valueOf(value), label));
			}
		}
		catch (Exception e) {
			if (_log.isDebugEnabled()) {
				_log.debug(e, e);
			}
		}

		return DDMDataProviderResponse.of(
			DDMDataProviderResponseOutput.of("Default-Output", "list", data));
	}

	@Override
	public Class<?> getSettings() {
		throw new UnsupportedOperationException();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DDMDataProviderInstancesDataProvider.class);

	@Reference
	private DDMDataProviderInstanceLocalService
		_ddmDataProviderInstanceLocalService;

	@Reference
	private Portal _portal;

}