/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.elasticsearch6.internal.index;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.search.elasticsearch6.internal.connection.IndexName;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

/**
 * @author Rodrigo Paulino
 * @author André de Oliveira
 */
public class LiferayTypeMappingsDDMKeywordEmptyStringTest {

	@Before
	public void setUp() throws Exception {
		Class<?> clazz = getClass();

		_liferayIndexFixture = new LiferayIndexFixture(
			clazz.getSimpleName(), new IndexName(testName.getMethodName()));

		_liferayIndexFixture.setUp();
	}

	@Test
	public void testDDMKeywordEmptyStringInSecondDocument() throws Exception {
		final String field1 = randomDDMKeywordField();
		final String ddmField2 = randomDDMKeywordField();
		final String ddmField3 = randomDDMKeywordField();
		final String ddmField4 = randomDDMKeywordField();
		final String ddmField5 = randomDDMKeywordField();
		final String ddmField6 = randomDDMKeywordField();
		final String ddmField7 = randomDDMKeywordField();
		final String ddmField8 = randomDDMKeywordField();
		final String ddmField9 = randomDDMKeywordField();

		index(
			new HashMap<String, Object>() {
				{
					put(ddmField2, StringPool.BLANK);
					put(ddmField3, new Date());
					put(ddmField4, "2011-07-01T01:32:33");
					put(ddmField5, 321231312321L);
					put(ddmField6, "321231312321");
					put(ddmField7, true);
					put(ddmField8, "true");
					put(ddmField9, "NULL");
					put(field1, RandomTestUtil.randomString());
				}
			});

		index(
			new HashMap<String, Object>() {
				{
					put(ddmField2, StringPool.BLANK);
					put(ddmField3, StringPool.BLANK);
					put(ddmField4, StringPool.BLANK);
					put(ddmField5, StringPool.BLANK);
					put(ddmField6, StringPool.BLANK);
					put(ddmField7, StringPool.BLANK);
					put(ddmField8, StringPool.BLANK);
					put(ddmField9, StringPool.BLANK);
					put(field1, StringPool.BLANK);
				}
			});

		index(
			new HashMap<String, Object>() {
				{
					put(ddmField2, RandomTestUtil.randomString());
					put(ddmField3, RandomTestUtil.randomString());
					put(ddmField4, RandomTestUtil.randomString());
					put(ddmField5, String.valueOf(RandomTestUtil.randomLong()));
					put(ddmField6, RandomTestUtil.randomString());
					put(ddmField7, StringPool.FALSE);
					put(ddmField8, RandomTestUtil.randomString());
					put(ddmField9, RandomTestUtil.randomString());
					put(field1, RandomTestUtil.randomString());
				}
			});

		assertType(field1, "keyword");
		assertType(ddmField2, "keyword");
		assertType(ddmField3, "keyword");
		assertType(ddmField4, "keyword");
		assertType(ddmField5, "long");
		assertType(ddmField6, "keyword");
		assertType(ddmField7, "boolean");
		assertType(ddmField8, "keyword");
		assertType(ddmField9, "keyword");
	}

	@Rule
	public TestName testName = new TestName();

	protected static String randomDDMKeywordField() {
		return "ddm__keyword__" + RandomTestUtil.randomString();
	}

	protected void assertType(String field, String type) throws Exception {
		_liferayIndexFixture.assertType(field, type);
	}

	protected void index(Map<String, Object> map) {
		_liferayIndexFixture.index(map);
	}

	private LiferayIndexFixture _liferayIndexFixture;

}