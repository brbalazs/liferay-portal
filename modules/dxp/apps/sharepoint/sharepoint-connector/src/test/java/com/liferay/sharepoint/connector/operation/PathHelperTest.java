/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.sharepoint.connector.operation;

import com.liferay.portal.kernel.util.StringPool;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Iván Zaera
 */
public class PathHelperTest {

	@Test
	public void testBuildPathWithNonrootFolder() {
		Assert.assertEquals(
			"/folderPath/name", _pathHelper.buildPath("/folderPath", "name"));
	}

	@Test
	public void testBuildPathWithRootFolder() {
		Assert.assertEquals(
			"/name", _pathHelper.buildPath(StringPool.SLASH, "name"));
	}

	@Test
	public void testGetExtensionWhithMissingExtension() {
		Assert.assertEquals(
			StringPool.BLANK, _pathHelper.getExtension("/name."));
		Assert.assertEquals(
			StringPool.BLANK, _pathHelper.getExtension("/name"));
	}

	@Test
	public void testGetExtensionWithExtension() {
		Assert.assertEquals("ext", _pathHelper.getExtension("/name.ext"));
	}

	@Test
	public void testGetName() {
		Assert.assertEquals("name.ext", _pathHelper.getName("/name.ext"));
	}

	@Test
	public void testGetNameWithoutExtensionWithExtension() {
		Assert.assertEquals(
			"name", _pathHelper.getNameWithoutExtension("/name.ext"));
	}

	@Test
	public void testGetNameWithoutExtensionWithMissingExtension() {
		Assert.assertEquals(
			"name", _pathHelper.getNameWithoutExtension("/name."));
		Assert.assertEquals(
			"name", _pathHelper.getNameWithoutExtension("/name"));
	}

	@Test
	public void testGetParentFolderPathWithNonrootFolder() {
		Assert.assertEquals(
			"/folder", _pathHelper.getParentFolderPath("/folder/name"));
	}

	@Test
	public void testGetParentFolderPathWithRootFolder() {
		Assert.assertEquals("/", _pathHelper.getParentFolderPath("/name"));
	}

	private final PathHelper _pathHelper = new PathHelper();

}