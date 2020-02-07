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

package com.liferay.commerce.machine.learning.internal.recommendation.data.source;

import com.liferay.commerce.machine.learning.recommendation.model.ProductContentCommerceMLRecommendation;
import com.liferay.commerce.machine.learning.recommendation.service.ProductContentCommerceMLRecommendationService;
import com.liferay.commerce.product.catalog.CPCatalogEntry;
import com.liferay.commerce.product.catalog.CPQuery;
import com.liferay.commerce.product.constants.CPWebKeys;
import com.liferay.commerce.product.data.source.CPDataSource;
import com.liferay.commerce.product.data.source.CPDataSourceResult;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.util.CPDefinitionHelper;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.BooleanClause;
import com.liferay.portal.kernel.search.BooleanClauseFactoryUtil;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Query;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Riccardo Ferrari
 */
@Component(
	immediate = true,
	property = "commerce.product.data.source.name=" + ProductContentCommerceMLRecommendationCPDataSourceImpl.NAME,
	service = CPDataSource.class
)
public class ProductContentCommerceMLRecommendationCPDataSourceImpl
	extends BaseCommerceMLRecommendationCPDataSource {

	public static final String NAME =
		"productContentCommerceMLRecommendationDataSource";

	@Override
	public String getLabel(Locale locale) {
		return LanguageUtil.get(
			getResourceBundle(locale), "product-content-based-recommendations");
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public CPDataSourceResult getResult(
			HttpServletRequest httpServletRequest, int start, int end)
		throws Exception {

		CPCatalogEntry cpCatalogEntry =
			(CPCatalogEntry)httpServletRequest.getAttribute(
				CPWebKeys.CP_CATALOG_ENTRY);

		if (cpCatalogEntry == null) {
			return new CPDataSourceResult(Collections.emptyList(), 0);
		}

		long companyId = _portal.getCompanyId(httpServletRequest);

		List<ProductContentCommerceMLRecommendation>
			productContentCommerceMLRecommendations =
				_productContentCommerceMLRecommendationService.
					getProductContentCommerceMLRecommendations(
						companyId, cpCatalogEntry.getCPDefinitionId());

		if (productContentCommerceMLRecommendations.isEmpty()) {
			return new CPDataSourceResult(Collections.emptyList(), 0);
		}

		long groupId = _portal.getScopeGroupId(httpServletRequest);

		SearchContext searchContext = new SearchContext();

		Map<String, Serializable> attributes = new HashMap<>();

		attributes.put(Field.STATUS, WorkflowConstants.STATUS_APPROVED);

		searchContext.setAttributes(attributes);

		searchContext.setCompanyId(companyId);

		searchContext.setEntryClassNames(
			new String[] {CPDefinition.class.getName()});

		List<BooleanClause> booleanClauseList = new ArrayList<>();

		for (ProductContentCommerceMLRecommendation
				prodcutContentCommerceMLRecommendation :
					productContentCommerceMLRecommendations) {

			long recommendedEntryClassPK =
				prodcutContentCommerceMLRecommendation.
					getRecommendedEntryClassPK();

			float score = prodcutContentCommerceMLRecommendation.getScore();

			int rank = prodcutContentCommerceMLRecommendation.getRank();

			if (_log.isTraceEnabled()) {
				StringBuilder sb = new StringBuilder();

				sb.append("Recommended item: ");
				sb.append(recommendedEntryClassPK);
				sb.append(" rank: ");
				sb.append(rank);
				sb.append(" score: ");
				sb.append(score);

				_log.trace(sb.toString());
			}

			BooleanClause<Query> entryClassPKBooleanClause =
				BooleanClauseFactoryUtil.create(
					Field.ENTRY_CLASS_PK,
					String.valueOf(recommendedEntryClassPK),
					BooleanClauseOccur.SHOULD.getName());

			booleanClauseList.add(entryClassPKBooleanClause);
		}

		searchContext.setBooleanClauses(
			booleanClauseList.toArray(new BooleanClause[0]));

		return _cpDefinitionHelper.search(
			groupId, searchContext, new CPQuery(), start, end);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ProductContentCommerceMLRecommendationCPDataSourceImpl.class);

	@Reference(unbind = "-")
	private CPDefinitionHelper _cpDefinitionHelper;

	@Reference(unbind = "-")
	private Portal _portal;

	@Reference(unbind = "-")
	private ProductContentCommerceMLRecommendationService
		_productContentCommerceMLRecommendationService;

}