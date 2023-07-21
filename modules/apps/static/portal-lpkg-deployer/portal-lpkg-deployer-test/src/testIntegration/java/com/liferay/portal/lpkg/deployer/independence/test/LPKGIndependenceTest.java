/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.lpkg.deployer.independence.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.util.PropsValues;

import java.io.File;

import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * @author Matthew Tambara
 */
@RunWith(Arquillian.class)
public class LPKGIndependenceTest {

	@Test
	public void testLPKGIndependence() throws Exception {
		Bundle bundle = FrameworkUtil.getBundle(LPKGIndependenceTest.class);

		BundleContext bundleContext = bundle.getBundleContext();

		ServiceReference serviceReference = bundleContext.getServiceReference(
			"com.liferay.portal.lpkg.deployer.internal.LPKGIndexValidator");

		Object service = bundleContext.getService(serviceReference);

		Path tempPath = Paths.get(
			PropsValues.MODULE_FRAMEWORK_BASE_DIR, "temp");

		File tempFile = tempPath.toFile();

		for (File lpkgDir : tempFile.listFiles()) {
			List<File> files = Arrays.asList(lpkgDir.listFiles());

			Assert.assertTrue(
				"Unable to validate " + files,
				(Boolean)ReflectionTestUtil.invoke(
					service, "validate", new Class<?>[] {List.class}, files));
		}
	}

}