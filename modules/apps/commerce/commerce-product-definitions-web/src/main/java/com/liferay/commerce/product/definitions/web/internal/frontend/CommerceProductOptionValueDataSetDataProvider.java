/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.commerce.product.definitions.web.internal.frontend;

import com.liferay.commerce.frontend.CommerceDataSetDataProvider;
import com.liferay.commerce.frontend.DefaultFilterImpl;
import com.liferay.commerce.frontend.Filter;
import com.liferay.commerce.frontend.Pagination;
import com.liferay.commerce.product.definitions.web.internal.model.ProductOptionValue;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.model.CPDefinitionOptionValueRel;
import com.liferay.commerce.product.service.CPDefinitionOptionValueRelService;
import com.liferay.commerce.product.service.CPDefinitionService;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.search.BaseModelSearchResult;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alessio Antonio Rendina
 */
@Component(
	immediate = true,
	property = "commerce.data.provider.key=" + CommerceProductDataSetConstants.COMMERCE_DATA_SET_KEY_PRODUCT_OPTION_VALUES,
	service = CommerceDataSetDataProvider.class
)
public class CommerceProductOptionValueDataSetDataProvider
	implements CommerceDataSetDataProvider<ProductOptionValue> {

	@Override
	public int countItems(HttpServletRequest httpServletRequest, Filter filter)
		throws PortalException {

		DefaultFilterImpl defaultFilterImpl = (DefaultFilterImpl)filter;

		String keywords = defaultFilterImpl.getKeywords();

		long cpDefinitionOptionRelId = ParamUtil.getLong(
			httpServletRequest, "cpDefinitionOptionRelId");

		if (Validator.isNotNull(keywords) || (cpDefinitionOptionRelId == 0)) {
			BaseModelSearchResult<CPDefinitionOptionValueRel>
				baseModelSearchResult = _getBaseModelSearchResult(
					cpDefinitionOptionRelId, keywords, QueryUtil.ALL_POS,
					QueryUtil.ALL_POS, null);

			return baseModelSearchResult.getLength();
		}

		return _cpDefinitionOptionValueRelService.
			getCPDefinitionOptionValueRelsCount(cpDefinitionOptionRelId);
	}

	@Override
	public List<ProductOptionValue> getItems(
			HttpServletRequest httpServletRequest, Filter filter,
			Pagination pagination, Sort sort)
		throws PortalException {

		List<ProductOptionValue> productOptionValues = new ArrayList<>();

		DefaultFilterImpl defaultFilterImpl = (DefaultFilterImpl)filter;

		long cpDefinitionOptionRelId = ParamUtil.getLong(
			httpServletRequest, "cpDefinitionOptionRelId");

		Locale locale = _portal.getLocale(httpServletRequest);

		List<CPDefinitionOptionValueRel> cpDefinitionOptionValueRels =
			_getCPDefinitionOptionValueRels(
				cpDefinitionOptionRelId, defaultFilterImpl.getKeywords(),
				pagination.getStartPosition(), pagination.getEndPosition(),
				sort);

		for (CPDefinitionOptionValueRel cpDefinitionOptionValueRel :
				cpDefinitionOptionValueRels) {

			productOptionValues.add(
				new ProductOptionValue(
					cpDefinitionOptionValueRel.
						getCPDefinitionOptionValueRelId(),
					HtmlUtil.escape(
						cpDefinitionOptionValueRel.getName(
							LanguageUtil.getLanguageId(locale))),
					cpDefinitionOptionValueRel.getPriority()));
		}

		return productOptionValues;
	}

	private BaseModelSearchResult<CPDefinitionOptionValueRel>
			_getBaseModelSearchResult(
				long cpDefinitionOptionRelId, String keywords, int start,
				int end, Sort sort)
		throws PortalException {

		CPDefinition cpDefinition = _cpDefinitionService.getCPDefinition(
			cpDefinitionOptionRelId);

		return _cpDefinitionOptionValueRelService.
			searchCPDefinitionOptionValueRels(
				cpDefinition.getCompanyId(), cpDefinition.getGroupId(),
				cpDefinitionOptionRelId, keywords, start, end, sort);
	}

	private List<CPDefinitionOptionValueRel> _getCPDefinitionOptionValueRels(
			long cpDefinitionOptionRelId, String keywords, int start, int end,
			Sort sort)
		throws PortalException {

		if (Validator.isNotNull(keywords) || (cpDefinitionOptionRelId == 0)) {
			BaseModelSearchResult<CPDefinitionOptionValueRel>
				baseModelSearchResult = _getBaseModelSearchResult(
					cpDefinitionOptionRelId, keywords, start, end, sort);

			return baseModelSearchResult.getBaseModels();
		}

		return _cpDefinitionOptionValueRelService.
			getCPDefinitionOptionValueRels(cpDefinitionOptionRelId, start, end);
	}

	@Reference
	private CPDefinitionOptionValueRelService
		_cpDefinitionOptionValueRelService;

	@Reference
	private CPDefinitionService _cpDefinitionService;

	@Reference
	private Portal _portal;

}