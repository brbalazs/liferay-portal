/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.json;

import org.json.JSONArray;
import org.json.JSONObject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Vishal Reddy
 */
public class JSONArrayPaginatorTest {

	@Test
	public void testPaginate() throws Exception {
		final TestService testService = new TestService(2000);

		new JSONArrayPaginator() {

			@Override
			protected JSONArray paginate(int start, int end) {
				JSONArray jsonArray = testService.getJSONArray(start, end);

				processedCount += jsonArray.length();

				Assertions.assertEquals(
					start + jsonArray.length(), processedCount);

				return jsonArray;
			}

		};

		Assertions.assertEquals(0, testService.getUnread());
	}

	private static class TestService {

		public TestService(int size) {
			JSONArray jsonArray = new JSONArray();

			for (int i = 0; i < size; i++) {
				JSONObject jsonObject = new JSONObject();

				jsonObject.put("foo", "bar");

				jsonArray.put(jsonObject);
			}

			_jsonArray = jsonArray;
			_unread = size;
		}

		public JSONArray getJSONArray(int start, int end) {
			if (start >= _jsonArray.length()) {
				return new JSONArray();
			}

			JSONArray jsonArray = new JSONArray();

			if (start < 0) {
				start = 0;
			}

			if (end >= _jsonArray.length()) {
				end = _jsonArray.length();
			}

			for (int i = start; i < end; i++) {
				jsonArray.put(_jsonArray.get(i));

				_unread -= 1;
			}

			return jsonArray;
		}

		public int getUnread() {
			return _unread;
		}

		private final JSONArray _jsonArray;
		private int _unread;

	}

}