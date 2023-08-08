/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.model;

import com.liferay.osb.asah.backend.test.util.BaseEnumTestCase;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Inácio Nery
 */
public class AssetTypeTest extends BaseEnumTestCase<AssetType> {

	@Test
	public void testOfAsset() {
		Assertions.assertThrows(
			IllegalArgumentException.class, () -> AssetType.of("asset"));
	}

	@Test
	public void testOfBlog() {
		Assertions.assertEquals(AssetType.BLOG, AssetType.of("blog"));
	}

	@Test
	public void testOfDocument() {
		Assertions.assertEquals(AssetType.DOCUMENT, AssetType.of("document"));
	}

	@Test
	public void testOfForm() {
		Assertions.assertEquals(AssetType.FORM, AssetType.of("form"));
	}

	@Test
	public void testOfJournal() {
		Assertions.assertEquals(AssetType.JOURNAL, AssetType.of("journal"));
	}

	@Test
	public void testOfURL() {
		Assertions.assertEquals(AssetType.PAGE, AssetType.of("page"));
	}

	@Override
	protected Class<? extends Enum<?>> getClazz() {
		return AssetType.class;
	}

}