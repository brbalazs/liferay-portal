/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.upload;

import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.DependenciesTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.io.File;

import java.nio.file.Files;

import org.apache.commons.fileupload.FileItem;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * @author Manuel de la Peña
 */
public class LiferayFileItemTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() {
		_liferayFileItemFactory = new LiferayFileItemFactory(
			temporaryFolder.getRoot());
	}

	@Test
	public void testCreateItem() {
		String fieldName = RandomTestUtil.randomString();
		String contentType = RandomTestUtil.randomString();
		String fileName = RandomTestUtil.randomString();

		FileItem fileItem = _liferayFileItemFactory.createItem(
			fieldName, contentType, false, fileName);

		LiferayFileItem liferayFileItem = (LiferayFileItem)fileItem;

		Assert.assertEquals(fieldName, liferayFileItem.getFieldName());
		Assert.assertEquals(fileName, liferayFileItem.getFullFileName());
		Assert.assertFalse(liferayFileItem.isFormField());
	}

	@Test(expected = NullPointerException.class)
	public void testGetContentTypeFromInvalidFile() {
		String fieldName = RandomTestUtil.randomString();
		String contentType = RandomTestUtil.randomString();
		String fileName = RandomTestUtil.randomString();

		FileItem fileItem = _liferayFileItemFactory.createItem(
			fieldName, contentType, false, fileName);

		Assert.assertNotNull(fileItem);

		fileItem.getContentType();
	}

	@Test(expected = NullPointerException.class)
	public void testGetContentTypeFromRealFile() throws Exception {
		File file = DependenciesTestUtil.getDependencyAsFile(
			getClass(), "LiferayFileItem.txt");

		String fieldName = RandomTestUtil.randomString();
		String contentType = Files.probeContentType(file.toPath());
		String fileName = file.getName();

		FileItem fileItem = _liferayFileItemFactory.createItem(
			fieldName, contentType, false, fileName);

		Assert.assertNotNull(fileItem);

		fileItem.getContentType();
	}

	@Test
	public void testGetEncodedStringAfterCreateItem() {
		String contentType = RandomTestUtil.randomString();
		String fieldName = RandomTestUtil.randomString();
		String fileName = RandomTestUtil.randomString();

		FileItem fileItem = _liferayFileItemFactory.createItem(
			fieldName, contentType, false, fileName);

		LiferayFileItem liferayFileItem = (LiferayFileItem)fileItem;

		Assert.assertNotNull(liferayFileItem);
		Assert.assertNull(liferayFileItem.getEncodedString());
	}

	@Test
	public void testGetFileNameExtension() {
		String fieldName = RandomTestUtil.randomString();
		String contentType = RandomTestUtil.randomString();
		String fileName = RandomTestUtil.randomString() + ".txt";

		FileItem fileItem = _liferayFileItemFactory.createItem(
			fieldName, contentType, false, fileName);

		LiferayFileItem liferayFileItem = (LiferayFileItem)fileItem;

		Assert.assertEquals("txt", liferayFileItem.getFileNameExtension());
	}

	@Test
	public void testGetFileNameExtensionWithNullValue() {
		String fieldName = RandomTestUtil.randomString();
		String contentType = RandomTestUtil.randomString();
		String fileName = "theFile";

		FileItem fileItem = _liferayFileItemFactory.createItem(
			fieldName, contentType, false, fileName);

		LiferayFileItem liferayFileItem = (LiferayFileItem)fileItem;

		Assert.assertEquals("", liferayFileItem.getFileNameExtension());
	}

	@Test
	public void testSetStringRequiresCharacterEncoding() throws Exception {
		String contentType = RandomTestUtil.randomString();
		String fieldName = RandomTestUtil.randomString();
		String fileName = RandomTestUtil.randomString() + ".txt";

		FileItem fileItem = _liferayFileItemFactory.createItem(
			fieldName, contentType, false, fileName);

		LiferayFileItem liferayFileItem = (LiferayFileItem)fileItem;

		liferayFileItem.getOutputStream();

		liferayFileItem.setString("UTF-8");

		Assert.assertEquals("", liferayFileItem.getEncodedString());
	}

	@Test(expected = NullPointerException.class)
	public void testSetStringWithoutOutputStream() throws Exception {
		String fieldName = RandomTestUtil.randomString();
		String contentType = RandomTestUtil.randomString();
		String fileName = RandomTestUtil.randomString() + ".txt";

		FileItem fileItem = _liferayFileItemFactory.createItem(
			fieldName, contentType, false, fileName);

		LiferayFileItem liferayFileItem = (LiferayFileItem)fileItem;

		Assert.assertNotNull(liferayFileItem);

		liferayFileItem.setString(RandomTestUtil.randomString());
	}

	@Test
	public void testWriteRequiresCallingGetOutputStream() throws Exception {
		String fieldName = RandomTestUtil.randomString();
		String contentType = RandomTestUtil.randomString();
		String fileName = RandomTestUtil.randomString() + ".txt";

		FileItem fileItem = _liferayFileItemFactory.createItem(
			fieldName, contentType, false, fileName);

		LiferayFileItem liferayFileItem = (LiferayFileItem)fileItem;

		liferayFileItem.getOutputStream();

		liferayFileItem.write(temporaryFolder.newFile());

		liferayFileItem.setString("UTF-8");

		Assert.assertEquals("", liferayFileItem.getEncodedString());
	}

	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	private LiferayFileItemFactory _liferayFileItemFactory;

}