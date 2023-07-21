/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.configuration.metatype.util;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Jorge Ferrer
 */
public class ParameterMapUtilWhenSettingAParameterMapWithPrefixesTest {

	@Before
	public void setUp() throws ConfigurationException {
		ParameterMapUtilTestUtil.TestBean testBean =
			ParameterMapUtilTestUtil.getTestBean();

		Map<String, String[]> parameterMap = new HashMap<>();

		parameterMap.put("prefix--testBoolean1--", new String[] {"false"});
		parameterMap.put(
			"prefix--testString1--",
			new String[] {ParameterMapUtilTestUtil.PARAMETER_MAP_STRING});
		parameterMap.put(
			"prefix--testStringArray1--",
			ParameterMapUtilTestUtil.PARAMETER_MAP_STRING_ARRAY);

		_testBean = ParameterMapUtil.setParameterMap(
			ParameterMapUtilTestUtil.TestBean.class, testBean, parameterMap,
			"prefix--", StringPool.DOUBLE_DASH);
	}

	@Test
	public void testValuesInTheParameterMapAreReadFirst() {
		Assert.assertFalse(_testBean.testBoolean1());
		Assert.assertEquals(
			ParameterMapUtilTestUtil.PARAMETER_MAP_STRING,
			_testBean.testString1());
		Assert.assertArrayEquals(
			ParameterMapUtilTestUtil.PARAMETER_MAP_STRING_ARRAY,
			_testBean.testStringArray1());
	}

	@Test
	public void testValuesNotInTheParameterMapAreReadFromBean() {
		Assert.assertTrue(_testBean.testBoolean2());
		Assert.assertEquals(
			ParameterMapUtilTestUtil.TEST_BEAN_STRING, _testBean.testString2());
		Assert.assertArrayEquals(
			ParameterMapUtilTestUtil.TEST_BEAN_STRING_ARRAY,
			_testBean.testStringArray2());
	}

	private ParameterMapUtilTestUtil.TestBean _testBean;

}