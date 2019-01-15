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

package com.liferay.commerce.data.integration.manager.web.internal.admin.panel.app;

import com.liferay.application.list.BasePanelApp;
import com.liferay.application.list.PanelApp;
import com.liferay.application.list.constants.PanelCategoryKeys;
import com.liferay.commerce.data.integration.manager.web.internal.admin.api.DataIntegrationAdminModule;
import com.liferay.commerce.data.integration.manager.web.internal.portlet.constants.DataIntegrationWebPortletKeys;
import com.liferay.commerce.data.integration.manager.web.internal.util.DataIntegrationAdminModuleRegistry;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.util.ResourceBundleUtil;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author guywandji
 */
@Component(
	immediate = true,
	property = {
		"panel.app.order:Integer=1000",
		"panel.category.key=" + PanelCategoryKeys.SITE_ADMINISTRATION_CONTENT
	},
	service = PanelApp.class
)
public class DataIntegrationPanelApp extends BasePanelApp {

	@Override
	public String getKey() {
		return _KEY;
	}

	@Override
	public String getLabel(Locale locale) {
		ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
			"content.Language", locale, getClass());

		return LanguageUtil.get(resourceBundle, "data-integration");
	}

	@Override
	public String getPortletId() {
		return DataIntegrationWebPortletKeys.DATA_INTEGRATION_WEB;
	}

	@Override
	public boolean isShow(PermissionChecker permissionChecker, Group group)
		throws PortalException {

		boolean show = super.isShow(permissionChecker, group);

		if (show) {
			Map<String, DataIntegrationAdminModule>
				lrDataIntegrationAdminModules =
					_lrDataIntegrationAdminModuleRegistry.
						getLrDataIntegrationModules();

			if (lrDataIntegrationAdminModules.isEmpty()) {
				show = false;
			}
		}

		return show;
	}

	@Override
	@Reference(
		target = "(javax.portlet.name=" + DataIntegrationWebPortletKeys.DATA_INTEGRATION_WEB + ")",
		unbind = "-"
	)
	public void setPortlet(Portlet portlet) {
		super.setPortlet(portlet);
	}

	private static final String _KEY = "data-integration";

	@Reference
	private DataIntegrationAdminModuleRegistry
		_lrDataIntegrationAdminModuleRegistry;

}