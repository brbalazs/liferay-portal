/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.model;

import com.liferay.portal.kernel.model.Contact;
import com.liferay.portal.kernel.model.ModelListener;
import com.liferay.portal.kernel.model.ModelListenerRegistrationUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.model.bundle.modellistenerregistrationutil.TestModelListener;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.SyntheticBundleRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Peter Fellwock
 */
public class ModelListenerRegistrationUtilTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			new SyntheticBundleRule("bundle.modellistenerregistrationutil"));

	@Test
	public void testGetModelListeners() {
		List<ModelListener<Contact>> modelListeners = new ArrayList<>(
			Arrays.asList(
				ModelListenerRegistrationUtil.getModelListeners(
					Contact.class)));

		String testClassName = TestModelListener.class.getName();

		Assert.assertTrue(
			testClassName + " not found in " + modelListeners,
			modelListeners.removeIf(
				modelListener -> {
					Class<?> clazz = modelListener.getClass();

					return testClassName.equals(clazz.getName());
				}));
	}

}