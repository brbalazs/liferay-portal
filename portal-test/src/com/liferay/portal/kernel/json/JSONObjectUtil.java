/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.json;

import com.liferay.petra.reflect.ReflectionUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;

/**
 * @author Shuyang Zhou
 */
public class JSONObjectUtil {

	public static String toOrderedJSONString(JSONObject jsonObject) {
		return toOrderedJSONString(jsonObject.toString());
	}

	public static String toOrderedJSONString(String jsonString) {
		try {
			org.json.JSONObject jsonObject = new org.json.JSONObject(
				jsonString) {

				@Override
				@SuppressWarnings("rawtypes")
				public Iterator keys() {
					Iterator<?> iterator = super.keys();

					List<Object> list = new ArrayList<>(length());

					while (iterator.hasNext()) {
						list.add(iterator.next());
					}

					Collections.sort(
						list,
						new Comparator<Object>() {

							@Override
							public int compare(Object object1, Object object2) {
								String string1 = object1.toString();

								return string1.compareTo(object2.toString());
							}

						});

					return list.iterator();
				}

			};

			return jsonObject.toString();
		}
		catch (JSONException jsone) {
			return ReflectionUtil.throwException(jsone);
		}
	}

}