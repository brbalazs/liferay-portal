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

package com.liferay.journal.web.internal.asset.display.contributor;

import com.liferay.asset.display.contributor.AssetDisplayContributor;
import com.liferay.asset.display.contributor.BaseAssetDisplayContributor;
import com.liferay.document.library.kernel.service.DLAppService;
import com.liferay.document.library.kernel.util.DLUtil;
import com.liferay.dynamic.data.mapping.kernel.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.kernel.DDMFormValues;
import com.liferay.dynamic.data.mapping.kernel.Value;
import com.liferay.dynamic.data.mapping.util.FieldsToDDMFormValuesConverter;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.util.JournalConverter;
import com.liferay.journal.web.asset.JournalArticleDDMFormValuesReader;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.sanitizer.Sanitizer;
import com.liferay.portal.kernel.sanitizer.SanitizerException;
import com.liferay.portal.kernel.sanitizer.SanitizerUtil;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.Validator;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Jürgen Kappler
 */
@Component(immediate = true, service = AssetDisplayContributor.class)
public class JournalArticleAssetDisplayContributor
	extends BaseAssetDisplayContributor<JournalArticle> {

	@Override
	public String getClassName() {
		return JournalArticle.class.getName();
	}

	@Override
	protected Map<String, Object> getClassTypeValues(
		JournalArticle article, Locale locale) {

		Map<String, Object> classTypeValues = new HashMap<>();

		JournalArticleDDMFormValuesReader journalArticleDDMFormValuesReader =
			new JournalArticleDDMFormValuesReader(article);

		journalArticleDDMFormValuesReader.setFieldsToDDMFormValuesConverter(
			_fieldsToDDMFormValuesConverter);
		journalArticleDDMFormValuesReader.setJournalConverter(
			_journalConverter);

		try {
			DDMFormValues ddmFormValues =
				journalArticleDDMFormValuesReader.getDDMFormValues();

			Map<String, List<DDMFormFieldValue>> ddmFormFieldsValuesMap =
				ddmFormValues.getDDMFormFieldValuesMap();

			for (Map.Entry<String, List<DDMFormFieldValue>> entry :
					ddmFormFieldsValuesMap.entrySet()) {

				List<DDMFormFieldValue> ddmFormFieldValues = entry.getValue();

				_addDDMFormFieldValues(
					article, entry.getKey(), ddmFormFieldValues,
					classTypeValues, locale);
			}
		}
		catch (PortalException pe) {
			if (_log.isDebugEnabled()) {
				_log.debug(pe, pe);
			}
		}

		return classTypeValues;
	}

	private void _addDDMFormFieldValues(
			JournalArticle article, String key,
			List<DDMFormFieldValue> ddmFormFieldValues,
			Map<String, Object> classTypeValues, Locale locale)
		throws PortalException {

		Object fieldValue = null;

		if (ddmFormFieldValues.size() == 1) {
			DDMFormFieldValue ddmFormFieldValue = ddmFormFieldValues.get(0);

			_addNestedFields(
				article, ddmFormFieldValue, classTypeValues, locale);

			Value value = ddmFormFieldValue.getValue();

			fieldValue = _sanitizeFieldValue(
				article, ddmFormFieldValue.getType(), value.getString(locale));
		}
		else {
			Stream<DDMFormFieldValue> stream = ddmFormFieldValues.stream();

			fieldValue = stream.map(
				ddmFormFieldValue -> {
					try {
						_addNestedFields(
							article, ddmFormFieldValue, classTypeValues,
							locale);

						return _sanitizeFieldValue(
							article, ddmFormFieldValue.getType(),
							ddmFormFieldValue.getValue(
							).getString(
								locale
							));
					}
					catch (PortalException pe) {
						_log.error(
							"Unable to sanitize field " +
								ddmFormFieldValue.getName(),
							pe);

						return null;
					}
				}
			).filter(
				value -> value != null
			).collect(
				Collectors.toList()
			);
		}

		classTypeValues.put(key, fieldValue);
	}

	private void _addNestedFields(
			JournalArticle article, DDMFormFieldValue ddmFormFieldValue,
			Map<String, Object> classTypeValues, Locale locale)
		throws PortalException {

		Map<String, List<DDMFormFieldValue>> nestedDDMFormFieldsValuesMap =
			ddmFormFieldValue.getNestedDDMFormFieldValuesMap();

		for (Map.Entry<String, List<DDMFormFieldValue>> entry :
				nestedDDMFormFieldsValuesMap.entrySet()) {

			List<DDMFormFieldValue> ddmFormFieldValues = entry.getValue();

			_addDDMFormFieldValues(
				article, entry.getKey(), ddmFormFieldValues, classTypeValues,
				locale);
		}
	}

	private Object _sanitizeFieldValue(
			JournalArticle article, String type, String value)
		throws SanitizerException {

		if (Objects.equals(type, "ddm-image")) {
			return _transformFileEntryURL(value);
		}

		return SanitizerUtil.sanitize(
			article.getCompanyId(), article.getGroupId(), article.getUserId(),
			JournalArticle.class.getName(), article.getResourcePrimKey(),
			ContentTypes.TEXT_HTML, Sanitizer.MODE_ALL, value, null);
	}

	private String _transformFileEntryURL(String data) {
		try {
			JSONObject jsonObject = JSONFactoryUtil.createJSONObject(data);

			String uuid = jsonObject.getString("uuid");
			long groupId = jsonObject.getLong("groupId");

			if (Validator.isNull(uuid) && (groupId == 0)) {
				return StringPool.BLANK;
			}

			FileEntry fileEntry = _dlAppService.getFileEntryByUuidAndGroupId(
				uuid, groupId);

			return DLUtil.getImagePreviewURL(fileEntry, null);
		}
		catch (Exception e) {
			if (_log.isDebugEnabled()) {
				_log.debug(e, e);
			}
		}

		return StringPool.BLANK;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		JournalArticleAssetDisplayContributor.class);

	@Reference
	private DLAppService _dlAppService;

	@Reference
	private FieldsToDDMFormValuesConverter _fieldsToDDMFormValuesConverter;

	@Reference
	private JournalConverter _journalConverter;

}