/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.bundle.blacklist;

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.StreamUtil;
import com.liferay.portal.lpkg.deployer.test.util.LPKGTestUtil;

import java.io.FileOutputStream;
import java.io.OutputStream;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Matthew Tambara
 */
public class BundleBlacklistSetUpBatchTest {

	@Test
	public void testCreateAndBlacklistTestBundles() throws Exception {
		String liferayHome = System.getProperty("liferay.home");

		Assert.assertNotNull(
			"Missing system property \"liferay.home\"", liferayHome);

		String blacklistCfgName = System.getProperty("blacklist.cfg.name");

		Assert.assertNotNull(
			"Missing system property \"cfg.name\"", blacklistCfgName);

		try (OutputStream outputStream = new FileOutputStream(
				liferayHome + "/osgi/portal/" + _JAR_BUNDLE_SYMBOLIC_NAME +
					".jar")) {

			StreamUtil.transfer(
				LPKGTestUtil.createJAR(_JAR_BUNDLE_SYMBOLIC_NAME),
				outputStream);
		}

		try (OutputStream outputStream = new FileOutputStream(
				liferayHome + "/osgi/war/" + _WAR_BUNDLE_SYMBOLIC_NAME +
					".war")) {

			StreamUtil.transfer(
				LPKGTestUtil.createWAR(_WAR_BUNDLE_SYMBOLIC_NAME),
				outputStream);
		}

		StringBundler sb = new StringBundler(4);

		sb.append("blacklistBundleSymbolicNames=");
		sb.append(_JAR_BUNDLE_SYMBOLIC_NAME);
		sb.append(StringPool.COMMA);
		sb.append(_WAR_BUNDLE_SYMBOLIC_NAME);

		String cfgBody = sb.toString();

		try (OutputStream outputStream = new FileOutputStream(
				liferayHome + "/osgi/configs/" + blacklistCfgName)) {

			outputStream.write(cfgBody.getBytes());
		}
	}

	private static final String _JAR_BUNDLE_SYMBOLIC_NAME =
		"com.liferay.portal.bundle.blacklist.test.bundle";

	private static final String _WAR_BUNDLE_SYMBOLIC_NAME =
		_JAR_BUNDLE_SYMBOLIC_NAME.concat(".war");

}