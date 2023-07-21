/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.configuration.admin.web.internal.util;

import com.liferay.configuration.admin.category.ConfigurationCategory;
import com.liferay.configuration.admin.display.ConfigurationScreen;
import com.liferay.configuration.admin.web.internal.display.ConfigurationCategoryMenuDisplay;
import com.liferay.configuration.admin.web.internal.display.ConfigurationCategorySectionDisplay;
import com.liferay.configuration.admin.web.internal.display.ConfigurationEntry;

import java.util.List;
import java.util.Set;

/**
 * @author Michael C. Han
 */
public interface ConfigurationEntryRetriever {

	public ConfigurationCategory getConfigurationCategory(
		String configurationCategoryKey);

	public ConfigurationCategoryMenuDisplay getConfigurationCategoryMenuDisplay(
		String configurationCategory, String languageId);

	public List<ConfigurationCategorySectionDisplay>
		getConfigurationCategorySectionDisplays();

	public Set<ConfigurationEntry> getConfigurationEntries(
		String configurationCategory, String languageId);

	public ConfigurationScreen getConfigurationScreen(
		String configurationScreenKey);

}