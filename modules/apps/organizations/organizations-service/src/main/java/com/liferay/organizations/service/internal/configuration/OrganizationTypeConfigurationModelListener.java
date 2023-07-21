/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.organizations.service.internal.configuration;

import com.liferay.portal.configuration.persistence.listener.ConfigurationModelListener;
import com.liferay.portal.configuration.persistence.listener.ConfigurationModelListenerException;
import com.liferay.portal.kernel.util.LocaleThreadLocal;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.Dictionary;
import java.util.ResourceBundle;

import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Drew Brokke
 */
@Component(
	immediate = true,
	property = "model.class.name=com.liferay.organizations.service.internal.configuration.OrganizationTypeConfiguration",
	service = ConfigurationModelListener.class
)
public class OrganizationTypeConfigurationModelListener
	implements ConfigurationModelListener {

	@Override
	public void onBeforeSave(String pid, Dictionary<String, Object> properties)
		throws ConfigurationModelListenerException {

		try {
			String name = (String)properties.get("name");

			_validateNameExists(name);

			_validateUniqueConfiguration(pid, name);
		}
		catch (Exception e) {
			throw new ConfigurationModelListenerException(
				e.getMessage(), OrganizationTypeConfiguration.class, getClass(),
				properties);
		}
	}

	private ResourceBundle _getResourceBundle() {
		return ResourceBundleUtil.getBundle(
			"content.Language", LocaleThreadLocal.getThemeDisplayLocale(),
			getClass());
	}

	private void _validateNameExists(String name) throws Exception {
		if (Validator.isNotNull(name)) {
			return;
		}

		ResourceBundle resourceBundle = _getResourceBundle();

		String message = ResourceBundleUtil.getString(
			resourceBundle, "an-organization-type-must-have-a-valid-name");

		throw new Exception(message);
	}

	private void _validateUniqueConfiguration(String pid, String name)
		throws Exception {

		String filterString = String.format(
			"(&(service.factoryPid=%s)(name=%s))",
			OrganizationTypeConfiguration.class.getName(), name);

		Configuration[] configurations = _configurationAdmin.listConfigurations(
			filterString);

		if (configurations == null) {
			return;
		}

		Configuration configuration = configurations[0];

		if (pid.equals(configuration.getPid())) {
			return;
		}

		ResourceBundle resourceBundle = _getResourceBundle();

		String message = ResourceBundleUtil.getString(
			resourceBundle,
			"there-is-already-an-organization-type-with-the-name-x", name);

		throw new Exception(message);
	}

	@Reference
	private ConfigurationAdmin _configurationAdmin;

}