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

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.portlet.PortletIdCodec;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PortletKeys;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.Iterator;

import javax.portlet.PortletPreferences;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Andrea Di Giorgi
 */
@Component(service = BerylThemePortletSettingsInitializer.class)
public class BerylThemePortletSettingsInitializer {

	public void initialize(ServiceContext serviceContext) throws Exception {
		ClassLoader classLoader =
			BerylThemePortletSettingsInitializer.class.getClassLoader();

		String json = StringUtil.read(
			classLoader,
			BerylSiteInitializer.DEPENDENCY_PATH +
				"theme-portlet-settings.json",
			true);

		JSONArray jsonArray = _jsonFactory.createJSONArray(json);

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);

			String portletName = jsonObject.getString("portletName");

			_setPortletSettings(jsonObject, portletName, serviceContext);
		}
	}

	private void _setPlidPortletPreferences(
			long plid, String portletId, ServiceContext serviceContext)
		throws Exception {

		PortletPreferences portletSetup =
			PortletPreferencesFactoryUtil.getLayoutPortletSetup(
				serviceContext.getCompanyId(),
				PortletKeys.PREFS_OWNER_ID_DEFAULT,
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, plid, portletId,
				StringPool.BLANK);

		portletSetup.store();
	}

	private void _setPortletSettings(
			JSONObject jsonObject, String portletName,
			ServiceContext serviceContext)
		throws Exception {

		String instanceId = jsonObject.getString("instanceId");
		String layoutFriendlyURL = jsonObject.getString("layoutFriendlyURL");

		JSONObject portletPreferencesJSONObject = jsonObject.getJSONObject(
			"portletPreferences");

		String portletId = PortletIdCodec.encode(portletName, instanceId);

		PortletPreferences portletSetup =
			PortletPreferencesFactoryUtil.getLayoutPortletSetup(
				serviceContext.getCompanyId(), serviceContext.getScopeGroupId(),
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT,
				LayoutConstants.DEFAULT_PLID, portletId, StringPool.BLANK);

		Iterator<String> iterator = portletPreferencesJSONObject.keys();

		while (iterator.hasNext()) {
			String key = iterator.next();

			String value = portletPreferencesJSONObject.getString(key);

			if (key.equals("displayStyleGroupId")) {
				value = String.valueOf(serviceContext.getScopeGroupId());
			}

			portletSetup.setValue(key, value);
		}

		portletSetup.store();

		long plid = LayoutConstants.DEFAULT_PLID;

		if (Validator.isNotNull(layoutFriendlyURL)) {
			Layout layout = _layoutLocalService.fetchLayoutByFriendlyURL(
				serviceContext.getScopeGroupId(), true, layoutFriendlyURL);

			if (layout != null) {
				plid = layout.getPlid();
			}
		}

		if (plid > LayoutConstants.DEFAULT_PLID) {
			_setPlidPortletPreferences(plid, portletId, serviceContext);
		}
	}

	@Reference
	private JSONFactory _jsonFactory;

	@Reference
	private LayoutLocalService _layoutLocalService;

	@Reference
	private Portal _portal;

}