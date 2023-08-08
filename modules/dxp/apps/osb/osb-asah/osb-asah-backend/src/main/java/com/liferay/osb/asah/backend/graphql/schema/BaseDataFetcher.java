/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.backend.dog.helper.SearchQueryContext;
import com.liferay.osb.asah.backend.model.AssetType;
import com.liferay.osb.asah.common.model.TimeRange;

import graphql.execution.ExecutionTypeInfo;

import graphql.language.Field;
import graphql.language.FragmentDefinition;
import graphql.language.FragmentSpread;
import graphql.language.InlineFragment;
import graphql.language.Selection;
import graphql.language.SelectionSet;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.DataFetchingFieldSelectionSet;
import graphql.schema.GraphQLFieldDefinition;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Marcellus Tavares
 */
public abstract class BaseDataFetcher<T> implements DataFetcher<T> {

	@Override
	public T get(DataFetchingEnvironment dataFetchingEnvironment) {
		Map<String, Object> context = dataFetchingEnvironment.getContext();

		Set<String> selectedMetrics = (Set<String>)context.get(
			"selectedMetrics");

		if ((selectedMetrics == null) || selectedMetrics.isEmpty()) {
			context.put(
				"selectedMetrics",
				_getSelectedMetrics(
					dataFetchingEnvironment.getFields(), new HashSet<>()));
		}

		SearchQueryContext searchQueryContext = (SearchQueryContext)context.get(
			"searchQueryContext");

		if (searchQueryContext == null) {
			searchQueryContext = createSearchQueryContext(
				dataFetchingEnvironment);

			context.put("searchQueryContext", searchQueryContext);
		}

		return get(dataFetchingEnvironment, searchQueryContext);
	}

	public abstract T get(
		DataFetchingEnvironment dataFetchingEnvironment,
		SearchQueryContext searchQueryContext);

	protected SearchQueryContext createSearchQueryContext(
		DataFetchingEnvironment dataFetchingEnvironment) {

		SearchQueryContext searchQueryContext = new SearchQueryContext();

		searchQueryContext.setActive(
			dataFetchingEnvironment.getArgument("active"));
		searchQueryContext.setAssetId(
			dataFetchingEnvironment.getArgument("assetId"));

		AssetType assetType = getAssetType(dataFetchingEnvironment);

		if (assetType != null) {
			searchQueryContext.setAssetType(assetType);
		}

		searchQueryContext.setCanonicalUrl(
			dataFetchingEnvironment.getArgument("canonicalUrl"));
		searchQueryContext.setChannelId(
			(String)dataFetchingEnvironment.getArgument("channelId"));
		searchQueryContext.setCountry(
			dataFetchingEnvironment.getArgument("country"));
		searchQueryContext.setDataSourceId(
			dataFetchingEnvironment.getArgument("dataSourceId"));
		searchQueryContext.setDeviceType(
			dataFetchingEnvironment.getArgument("deviceType"));
		searchQueryContext.setExperimentId(
			dataFetchingEnvironment.getArgument("experimentId"));

		if (dataFetchingEnvironment.getArgument("includePrevious") != null) {
			searchQueryContext.setIncludePrevious(
				dataFetchingEnvironment.getArgument("includePrevious"));
		}
		else {
			Set<String> fieldNames = _getFieldNames(dataFetchingEnvironment);

			if (fieldNames.contains("previousValue") ||
				fieldNames.contains("trend")) {

				searchQueryContext.setIncludePrevious(true);
			}
			else {
				searchQueryContext.setIncludePrevious(false);
			}
		}

		searchQueryContext.setEntityId(
			dataFetchingEnvironment.getArgument("entityId"));
		searchQueryContext.setEntityType(
			dataFetchingEnvironment.getArgument("entityType"));
		searchQueryContext.setInterval(
			dataFetchingEnvironment.getArgument("interval"));
		searchQueryContext.setKeywords(
			dataFetchingEnvironment.getArgument("keywords"));

		if ((dataFetchingEnvironment.getArgument("rangeEnd") != null) &&
			(dataFetchingEnvironment.getArgument("rangeStart") != null)) {

			String rangeEnd = dataFetchingEnvironment.getArgument("rangeEnd");
			String rangeStart = dataFetchingEnvironment.getArgument(
				"rangeStart");

			if (rangeEnd.contains("T") && rangeStart.contains("T")) {
				searchQueryContext.setTimeRange(
					TimeRange.of(
						LocalDateTime.parse(rangeEnd),
						LocalDateTime.parse(rangeStart)));
			}
			else {
				searchQueryContext.setTimeRange(
					TimeRange.of(
						LocalDate.parse(rangeEnd),
						LocalDate.parse(rangeStart)));
			}
		}
		else if (dataFetchingEnvironment.getArgument("rangeKey") != null) {
			TimeRange timeRange = TimeRange.of(
				(int)dataFetchingEnvironment.getArgument("rangeKey"));

			if ((timeRange == TimeRange.LAST_24_HOURS) ||
				(timeRange == TimeRange.YESTERDAY)) {

				searchQueryContext.setInterval("H");
			}

			searchQueryContext.setTimeRange(timeRange);
		}

		searchQueryContext.setTerms(
			dataFetchingEnvironment.getArgument("terms"));
		searchQueryContext.setTitle(
			dataFetchingEnvironment.getArgument("title"));
		searchQueryContext.setURL(dataFetchingEnvironment.getArgument("url"));
		searchQueryContext.setVariantId(
			dataFetchingEnvironment.getArgument("variantId"));

		return searchQueryContext;
	}

	protected AssetType getAssetType(
		DataFetchingEnvironment dataFetchingEnvironment) {

		String assetTypeString = dataFetchingEnvironment.getArgument(
			"assetType");

		if (assetTypeString != null) {
			return AssetType.valueOf(assetTypeString);
		}

		ExecutionTypeInfo executionTypeInfo =
			dataFetchingEnvironment.getFieldTypeInfo();

		GraphQLFieldDefinition graphQLFieldDefinition =
			executionTypeInfo.getFieldDefinition();

		String name = graphQLFieldDefinition.getName();

		for (AssetType assetType : AssetType.values()) {
			if (name.equals(assetType.getValue())) {
				return assetType;
			}
		}

		return null;
	}

	private Set<String> _getFieldNames(
		DataFetchingEnvironment dataFetchingEnvironment) {

		DataFetchingFieldSelectionSet dataFetchingFieldSelectionSet =
			dataFetchingEnvironment.getSelectionSet();

		Map<String, List<Field>> selectionSetFields =
			dataFetchingFieldSelectionSet.get();

		Set<String> fieldNames = new HashSet<>();

		for (Map.Entry<String, List<Field>> entry :
				selectionSetFields.entrySet()) {

			for (Field field : entry.getValue()) {
				SelectionSet selectionSet = field.getSelectionSet();

				if (selectionSet != null) {
					fieldNames.addAll(
						_getSelectionSetFieldNames(
							dataFetchingEnvironment, selectionSet));
				}
				else {
					fieldNames.add(field.getName());
				}
			}
		}

		return fieldNames;
	}

	private Set<String> _getFragmentFieldNames(
		DataFetchingEnvironment dataFetchingEnvironment, String fragmentName) {

		Map<String, FragmentDefinition> fragments =
			dataFetchingEnvironment.getFragmentsByName();

		FragmentDefinition fragment = fragments.get(fragmentName);

		return _getSelectionSetFieldNames(
			dataFetchingEnvironment, fragment.getSelectionSet());
	}

	private Set<String> _getSelectedMetrics(
		List<Field> fields, Set<String> selectedMetrics) {

		if (fields == null) {
			return selectedMetrics;
		}

		for (Field field : fields) {
			for (Selection selection :
					_getSelections(field.getSelectionSet())) {

				if (selection instanceof Field) {
					Field selectionField = (Field)selection;

					String fieldName = selectionField.getName();

					if (fieldName.endsWith("Metric")) {
						selectedMetrics.add(selectionField.getName());
					}

					selectedMetrics.addAll(
						_getSelectedMetrics(
							Collections.singletonList(selectionField),
							selectedMetrics));
				}
				else if (selection instanceof InlineFragment) {
					InlineFragment inlineFragment = (InlineFragment)selection;

					for (Selection fragmentSelection :
							_getSelections(inlineFragment.getSelectionSet())) {

						if (fragmentSelection instanceof Field) {
							Field fragmentSelectionField =
								(Field)fragmentSelection;

							String fieldName = fragmentSelectionField.getName();

							if (fieldName.endsWith("Metric")) {
								selectedMetrics.add(
									fragmentSelectionField.getName());
							}
						}
					}
				}
			}
		}

		return selectedMetrics;
	}

	private List<Selection> _getSelections(SelectionSet selectionSet) {
		if (selectionSet == null) {
			return Collections.emptyList();
		}

		return selectionSet.getSelections();
	}

	private Set<String> _getSelectionSetFieldNames(
		DataFetchingEnvironment dataFetchingEnvironment,
		SelectionSet selectionSet) {

		Set<String> fieldNames = new HashSet<>();

		List<Selection> selections = selectionSet.getSelections();

		for (Selection selection : selections) {
			if (selection instanceof FragmentSpread) {
				FragmentSpread fragmentSpread = (FragmentSpread)selection;

				fieldNames.addAll(
					_getFragmentFieldNames(
						dataFetchingEnvironment, fragmentSpread.getName()));
			}
			else if (selection instanceof Field) {
				Field field = (Field)selection;

				SelectionSet fieldSelectionSet = field.getSelectionSet();

				if (fieldSelectionSet != null) {
					fieldNames.addAll(
						_getSelectionSetFieldNames(
							dataFetchingEnvironment, fieldSelectionSet));

					continue;
				}

				fieldNames.add(field.getName());
			}
			else if (selection instanceof InlineFragment) {
				InlineFragment inlineFragment = (InlineFragment)selection;

				fieldNames.addAll(
					_getSelectionSetFieldNames(
						dataFetchingEnvironment,
						inlineFragment.getSelectionSet()));
			}
		}

		return fieldNames;
	}

}