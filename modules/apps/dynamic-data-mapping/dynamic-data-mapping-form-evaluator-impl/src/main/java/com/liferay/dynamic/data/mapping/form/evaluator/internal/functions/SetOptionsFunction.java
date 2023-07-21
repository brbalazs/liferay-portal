/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.form.evaluator.internal.functions;

import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormFieldEvaluationResult;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.KeyValuePair;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Leonardo Barros
 */
public class SetOptionsFunction extends BaseDDMFormRuleFunction {

	public SetOptionsFunction(
		Map<String, List<DDMFormFieldEvaluationResult>>
			ddmFormFieldEvaluationResults,
		Locale locale, JSONFactory jsonFactory) {

		super(ddmFormFieldEvaluationResults);

		_jsonFactory = jsonFactory;

		_languageId = LanguageUtil.getLanguageId(locale);
	}

	@Override
	public Object evaluate(Object... parameters) {
		if (parameters.length != 2) {
			throw new IllegalArgumentException("Two parameters are expected");
		}

		List<KeyValuePair> keyValuePairs = createKeyValuePairList(
			String.valueOf(parameters[1]));

		List<DDMFormFieldEvaluationResult> ddmFormFieldEvaluationResults =
			getDDMFormFieldEvaluationResults(String.valueOf(parameters[0]));

		for (DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult :
				ddmFormFieldEvaluationResults) {

			ddmFormFieldEvaluationResult.setProperty("options", keyValuePairs);
		}

		return true;
	}

	protected List<KeyValuePair> createKeyValuePairList(String value) {
		List<KeyValuePair> keyValuePairs = new ArrayList<>();

		try {
			JSONObject jsonObject = _jsonFactory.createJSONObject(
				String.valueOf(value));

			Iterator<String> keys = jsonObject.keys();

			while (keys.hasNext()) {
				String languageId = keys.next();

				if (languageId.equals(_languageId)) {
					JSONArray jsonArray = jsonObject.getJSONArray(languageId);

					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject optionJSONObject = jsonArray.getJSONObject(
							i);

						KeyValuePair keyValuePair = new KeyValuePair(
							optionJSONObject.getString("value"),
							optionJSONObject.getString("label"));

						keyValuePairs.add(keyValuePair);
					}

					break;
				}
			}
		}
		catch (JSONException jsone) {
			_log.error(jsone, jsone);
		}

		return keyValuePairs;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SetOptionsFunction.class);

	private final JSONFactory _jsonFactory;
	private final String _languageId;

}