/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.commerce.product.internal.data.source;

import com.liferay.commerce.product.catalog.CPCatalogEntry;
import com.liferay.commerce.product.catalog.CPQuery;
import com.liferay.commerce.product.constants.CPWebKeys;
import com.liferay.commerce.product.data.source.CPDataSource;
import com.liferay.commerce.product.data.source.CPDataSourceResult;
import com.liferay.commerce.product.util.CPDefinitionHelper;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Ethan Bustad
 */
public abstract class BaseCPDataSourceAssetEntryImpl implements CPDataSource {

	@Override
	public CPDataSourceResult getResult(
			HttpServletRequest httpServletRequest, int start, int end)
		throws Exception {

		CPCatalogEntry cpCatalogEntry =
			(CPCatalogEntry)httpServletRequest.getAttribute(
				CPWebKeys.CP_CATALOG_ENTRY);

		if (cpCatalogEntry == null) {
			return new CPDataSourceResult(new ArrayList<>(), 0);
		}

		long groupId = portal.getScopeGroupId(httpServletRequest);

		SearchContext searchContext = new SearchContext();

		Map<String, Serializable> attributes = new HashMap<>();

		attributes.put(Field.STATUS, WorkflowConstants.STATUS_APPROVED);
		attributes.put(
			"excludedCPDefinitionId", cpCatalogEntry.getCPDefinitionId());

		LinkedHashMap<String, Object> params = new LinkedHashMap<>();

		params.put("keywords", StringPool.STAR);

		attributes.put("params", params);

		searchContext.setAttributes(attributes);

		searchContext.setCompanyId(portal.getCompanyId(httpServletRequest));

		searchContext.setKeywords(StringPool.STAR);

		CPQuery cpQuery = getCPQuery(cpCatalogEntry.getCPDefinitionId());

		return cpDefinitionHelper.search(
			groupId, searchContext, cpQuery, start, end);
	}

	protected abstract CPQuery getCPQuery(long cpDefinitionId)
		throws PortalException;

	protected ResourceBundle getResourceBundle(Locale locale) {
		return ResourceBundleUtil.getBundle(
			"content.Language", locale, getClass());
	}

	protected CPDefinitionHelper cpDefinitionHelper;
	protected Portal portal;

}