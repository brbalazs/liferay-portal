/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.web.internal.custom.facet.portlet.shared.search;

import com.liferay.dynamic.data.mapping.util.DDMIndexer;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.search.facet.Facet;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.search.facet.custom.CustomFacetFactory;
import com.liferay.portal.search.facet.nested.NestedFacetSearchContributor;
import com.liferay.portal.search.legacy.searcher.SearchRequestBuilderFactory;
import com.liferay.portal.search.searcher.SearchRequestBuilder;
import com.liferay.portal.search.web.internal.custom.facet.builder.CustomFacetBuilder;
import com.liferay.portal.search.web.internal.custom.facet.constants.CustomFacetPortletKeys;
import com.liferay.portal.search.web.internal.custom.facet.portlet.CustomFacetPortletPreferences;
import com.liferay.portal.search.web.internal.custom.facet.portlet.CustomFacetPortletPreferencesImpl;
import com.liferay.portal.search.web.portlet.shared.search.PortletSharedSearchContributor;
import com.liferay.portal.search.web.portlet.shared.search.PortletSharedSearchSettings;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Wade Cao
 */
@Component(
	immediate = true,
	property = "javax.portlet.name=" + CustomFacetPortletKeys.CUSTOM_FACET,
	service = PortletSharedSearchContributor.class
)
public class CustomFacetPortletSharedSearchContributor
	implements PortletSharedSearchContributor {

	@Override
	public void contribute(
		PortletSharedSearchSettings portletSharedSearchSettings) {

		CustomFacetPortletPreferences customFacetPortletPreferences =
			new CustomFacetPortletPreferencesImpl(
				portletSharedSearchSettings.getPortletPreferences());

		Optional<String> fieldToAggregateOptional =
			customFacetPortletPreferences.getAggregationFieldOptional();

		if (!fieldToAggregateOptional.isPresent()) {
			return;
		}

		SearchRequestBuilder searchRequestBuilder =
			searchRequestBuilderFactory.builder(
				portletSharedSearchSettings.getSearchContext());

		String fieldToAggregate = fieldToAggregateOptional.get();

		if (!ddmIndexer.isLegacyDDMIndexFieldsEnabled() &&
			fieldToAggregate.startsWith(DDMIndexer.DDM_FIELD_PREFIX)) {

			contributeWithNestedFacet(
				fieldToAggregate, searchRequestBuilder,
				portletSharedSearchSettings, customFacetPortletPreferences);
		}
		else {
			contributeWithCustomFacet(
				fieldToAggregate, searchRequestBuilder,
				portletSharedSearchSettings, customFacetPortletPreferences);
		}
	}

	protected Facet buildFacet(
		String fieldToAggregate,
		CustomFacetPortletPreferences customFacetPortletPreferences,
		PortletSharedSearchSettings portletSharedSearchSettings) {

		CustomFacetBuilder customFacetBuilder = new CustomFacetBuilder(
			customFacetFactory);

		customFacetBuilder.setAggregationName(
			getAggregationName(
				customFacetPortletPreferences,
				portletSharedSearchSettings.getPortletId()));
		customFacetBuilder.setFieldToAggregate(fieldToAggregate);
		customFacetBuilder.setSearchContext(
			portletSharedSearchSettings.getSearchContext());

		copy(
			() -> portletSharedSearchSettings.getParameterValues(
				getParameterName(customFacetPortletPreferences)),
			customFacetBuilder::setSelectedValues);

		return customFacetBuilder.build();
	}

	protected void contributeWithCustomFacet(
		String fieldToAggregate, SearchRequestBuilder searchRequestBuilder,
		PortletSharedSearchSettings portletSharedSearchSettings,
		CustomFacetPortletPreferences customFacetPortletPreferences) {

		Facet facet = buildFacet(
			fieldToAggregate, customFacetPortletPreferences,
			portletSharedSearchSettings);

		portletSharedSearchSettings.addFacet(facet);
	}

	protected void contributeWithNestedFacet(
		String fieldToAggregate, SearchRequestBuilder searchRequestBuilder,
		PortletSharedSearchSettings portletSharedSearchSettings,
		CustomFacetPortletPreferences customFacetPortletPreferences) {

		String[] ddmStructureParts = StringUtil.split(
			fieldToAggregate, DDMIndexer.DDM_FIELD_SEPARATOR);

		String nestedFieldToAggregate = ddmIndexer.getValueFieldName(
			ddmStructureParts[1], _getSuffixLocale(ddmStructureParts[3]));

		Optional<String[]> parameterValuesOptional =
			portletSharedSearchSettings.getParameterValues(
				getParameterName(customFacetPortletPreferences));

		nestedFacetSearchContributor.contribute(
			searchRequestBuilder,
			nestedFacetBuilder -> nestedFacetBuilder.aggregationName(
				getAggregationName(
					customFacetPortletPreferences,
					portletSharedSearchSettings.getPortletId())
			).fieldToAggregate(
				StringBundler.concat(
					DDMIndexer.DDM_FIELD_ARRAY, StringPool.PERIOD,
					nestedFieldToAggregate)
			).filterField(
				StringBundler.concat(
					DDMIndexer.DDM_FIELD_ARRAY, StringPool.PERIOD,
					DDMIndexer.DDM_FIELD_NAME)
			).filterValue(
				fieldToAggregate
			).frequencyThreshold(
				customFacetPortletPreferences.getFrequencyThreshold()
			).maxTerms(
				customFacetPortletPreferences.getMaxTerms()
			).path(
				DDMIndexer.DDM_FIELD_ARRAY
			).selectedValues(
				parameterValuesOptional.orElse(new String[0])
			));
	}

	protected <T> void copy(Supplier<Optional<T>> from, Consumer<T> to) {
		Optional<T> optional = from.get();

		optional.ifPresent(to);
	}

	protected String getAggregationName(
		CustomFacetPortletPreferences customFacetPortletPreferences,
		String portletId) {

		return customFacetPortletPreferences.getAggregationFieldString() +
			StringPool.PERIOD + portletId;
	}

	protected String getParameterName(
		CustomFacetPortletPreferences customFacetPortletPreferences) {

		Optional<String> optional = Stream.of(
			customFacetPortletPreferences.getParameterNameOptional(),
			customFacetPortletPreferences.getAggregationFieldOptional()
		).filter(
			Optional::isPresent
		).map(
			Optional::get
		).findFirst();

		return optional.orElse("customfield");
	}

	@Reference
	protected CustomFacetFactory customFacetFactory;

	@Reference
	protected DDMIndexer ddmIndexer;

	@Reference
	protected NestedFacetSearchContributor nestedFacetSearchContributor;

	@Reference
	protected SearchRequestBuilderFactory searchRequestBuilderFactory;

	private Locale _getSuffixLocale(String string) {
		for (Locale availableLocale : LanguageUtil.getAvailableLocales()) {
			String availableLanguageId = LanguageUtil.getLanguageId(
				availableLocale);

			if (string.endsWith(availableLanguageId)) {
				return availableLocale;
			}
		}

		return null;
	}

}