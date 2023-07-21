/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.lar.lifecycle;

import com.liferay.exportimport.kernel.lifecycle.ExportImportLifecycleEventListenerRegistryUtil;
import com.liferay.exportimport.kernel.lifecycle.ExportImportLifecycleListener;
import com.liferay.portal.kernel.lar.lifecycle.bundle.exportimportlifecycleeventlistenerregistryutil.TestAsyncExportImportLifecycleListener;
import com.liferay.portal.kernel.lar.lifecycle.bundle.exportimportlifecycleeventlistenerregistryutil.TestSyncExportImportLifecycleListener;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.SyntheticBundleRule;

import java.util.Set;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Peter Fellwock
 */
public class ExportImportLifecycleEventListenerRegistryUtilTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			new SyntheticBundleRule(
				"bundle.exportimportlifecycleeventlistenerregistryutil"));

	@Test
	public void testGetAsyncExportImportLifecycleListeners() {
		boolean exists = false;

		Set<ExportImportLifecycleListener> exportImportLifecycleListeners =
			ExportImportLifecycleEventListenerRegistryUtil.
				getAsyncExportImportLifecycleListeners();

		for (ExportImportLifecycleListener exportImportLifecycleListener :
				exportImportLifecycleListeners) {

			Class<?> clazz = exportImportLifecycleListener.getClass();

			String className = clazz.getName();

			Assert.assertNotEquals(
				TestSyncExportImportLifecycleListener.class.getName(),
				className);

			if (className.equals(
					TestAsyncExportImportLifecycleListener.class.getName())) {

				exists = true;

				break;
			}
		}

		Assert.assertTrue(exists);
	}

	@Test
	public void testGetSyncExportImportLifecycleListeners() {
		boolean exists = false;

		Set<ExportImportLifecycleListener> exportImportLifecycleListeners =
			ExportImportLifecycleEventListenerRegistryUtil.
				getSyncExportImportLifecycleListeners();

		for (ExportImportLifecycleListener exportImportLifecycleListener :
				exportImportLifecycleListeners) {

			Class<?> clazz = exportImportLifecycleListener.getClass();

			String className = clazz.getName();

			Assert.assertNotEquals(
				TestAsyncExportImportLifecycleListener.class.getName(),
				className);

			if (className.equals(
					TestSyncExportImportLifecycleListener.class.getName())) {

				exists = true;

				break;
			}
		}

		Assert.assertTrue(exists);
	}

}