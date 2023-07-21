/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.data.provider.internal;

import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderContext;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderRequest;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.dynamic.data.mapping.test.util.DDMFormTestUtil;
import com.liferay.dynamic.data.mapping.test.util.DDMFormValuesTestUtil;

import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.strategy.properties.HystrixProperty;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Pedro Queiroz
 */
public class DDMDataProviderInvokerImplTest {

	@Test
	public void testDataProviderTimeoutWithTimeoutOnRange() {
		DDMForm ddmForm = DDMFormTestUtil.createDDMForm("timeout");

		DDMFormValues ddmFormValues = DDMFormValuesTestUtil.createDDMFormValues(
			ddmForm);

		DDMFormFieldValue ddmFormFieldValue =
			DDMFormValuesTestUtil.createLocalizedDDMFormFieldValue(
				"timeout", "10000");

		ddmFormValues.addDDMFormFieldValue(ddmFormFieldValue);

		DDMDataProviderInvokeCommand ddmDataProviderInvokeCommand =
			getDDMDataProviderInvokeCommand("dataProvider1", ddmFormValues);

		Assert.assertEquals(
			10000,
			getExecutionTimeoutInMilliseconds(ddmDataProviderInvokeCommand));
	}

	@Test
	public void testDataProviderWithTimeoutAboveRange() {
		DDMForm ddmForm = DDMFormTestUtil.createDDMForm("timeout");

		DDMFormValues ddmFormValues = DDMFormValuesTestUtil.createDDMFormValues(
			ddmForm);

		DDMFormFieldValue ddmFormFieldValue =
			DDMFormValuesTestUtil.createLocalizedDDMFormFieldValue(
				"timeout", "0");

		ddmFormValues.addDDMFormFieldValue(ddmFormFieldValue);

		DDMDataProviderInvokeCommand ddmDataProviderInvokeCommand =
			getDDMDataProviderInvokeCommand("dataProvider3", ddmFormValues);

		Assert.assertEquals(
			1000,
			getExecutionTimeoutInMilliseconds(ddmDataProviderInvokeCommand));
	}

	@Test
	public void testDataProviderWithTimeoutBelowRange() {
		DDMForm ddmForm = DDMFormTestUtil.createDDMForm("timeout");

		DDMFormValues ddmFormValues = DDMFormValuesTestUtil.createDDMFormValues(
			ddmForm);

		DDMFormFieldValue ddmFormFieldValue =
			DDMFormValuesTestUtil.createLocalizedDDMFormFieldValue(
				"timeout", "30000000");

		ddmFormValues.addDDMFormFieldValue(ddmFormFieldValue);

		DDMDataProviderInvokeCommand ddmDataProviderInvokeCommand =
			getDDMDataProviderInvokeCommand("dataProvider2", ddmFormValues);

		Assert.assertEquals(
			1000,
			getExecutionTimeoutInMilliseconds(ddmDataProviderInvokeCommand));
	}

	protected DDMDataProviderInvokeCommand getDDMDataProviderInvokeCommand(
		String ddmDataProviderInstanceName, DDMFormValues ddmFormValues) {

		DDMDataProviderContext ddmDataProviderContext =
			new DDMDataProviderContext(ddmFormValues);

		DDMDataProviderRequest ddmDataProviderRequest =
			new DDMDataProviderRequest(null, null);

		ddmDataProviderRequest.setDDMDataProviderContext(
			ddmDataProviderContext);

		return new DDMDataProviderInvokeCommand(
			ddmDataProviderInstanceName, null, ddmDataProviderRequest);
	}

	protected int getExecutionTimeoutInMilliseconds(
		DDMDataProviderInvokeCommand ddmDataProviderInvokeCommand) {

		HystrixCommandProperties properties =
			ddmDataProviderInvokeCommand.getProperties();

		HystrixProperty<Integer> executionTimeoutInMillisecondsProperty =
			properties.executionTimeoutInMilliseconds();

		return executionTimeoutInMillisecondsProperty.get();
	}

}