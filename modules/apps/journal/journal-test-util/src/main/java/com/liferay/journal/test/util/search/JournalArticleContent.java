/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.journal.test.util.search;

import com.liferay.dynamic.data.mapping.test.util.DDMStructureTestUtil;
import com.liferay.portal.kernel.settings.LocalizedValuesMap;
import com.liferay.portal.kernel.util.LocaleUtil;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author André de Oliveira
 */
public class JournalArticleContent extends LocalizedValuesMap {

	public String getContentString() {
		List<Map<Locale, String>> list = getContents();

		return DDMStructureTestUtil.getSampleStructuredContent(
			name, list, LocaleUtil.toLanguageId(defaultLocale));
	}

	protected List<Map<Locale, String>> getContents() {
		Map<Locale, String> values = getValues();

		if (values.isEmpty()) {
			return Collections.emptyList();
		}

		return Collections.singletonList(values);
	}

	protected Locale defaultLocale;
	protected String name;

}