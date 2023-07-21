/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.configuration.admin.web.internal.util;

import com.liferay.configuration.admin.web.internal.model.ConfigurationModel;

import java.io.IOException;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.osgi.framework.Bundle;
import org.osgi.service.cm.Configuration;

/**
 * @author Michael C. Han
 */
public interface ConfigurationModelRetriever {

	public Map<String, Set<ConfigurationModel>> categorizeConfigurationModels(
		Map<String, ConfigurationModel> configurationModels);

	public Configuration getConfiguration(String pid);

	public Map<String, ConfigurationModel> getConfigurationModels();

	public Map<String, ConfigurationModel> getConfigurationModels(
		Bundle bundle);

	public Map<String, ConfigurationModel> getConfigurationModels(
		String locale);

	public Set<ConfigurationModel> getConfigurationModels(
		String configurationCategory, String languageId);

	public List<ConfigurationModel> getFactoryInstances(
			ConfigurationModel factoryConfigurationModel)
		throws IOException;

}