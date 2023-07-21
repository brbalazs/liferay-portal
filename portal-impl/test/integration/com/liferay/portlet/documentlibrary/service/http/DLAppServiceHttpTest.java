/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portlet.documentlibrary.service.http;

import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.service.http.HttpPrincipalTestUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Alexander Chow
 */
public class DLAppServiceHttpTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();

		String name = "Test Folder";
		String description = "This is a test folder.";

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		try {
			DLAppServiceHttp.deleteFolder(
				HttpPrincipalTestUtil.getHttpPrincipal(), _group.getGroupId(),
				DLFolderConstants.DEFAULT_PARENT_FOLDER_ID, name);
		}
		catch (Exception e) {
		}

		_folder = DLAppServiceHttp.addFolder(
			HttpPrincipalTestUtil.getHttpPrincipal(), _group.getGroupId(),
			DLFolderConstants.DEFAULT_PARENT_FOLDER_ID, name, description,
			serviceContext);
	}

	@After
	public void tearDown() throws Exception {
		try {
			if (_folder != null) {
				DLAppServiceHttp.deleteFolder(
					HttpPrincipalTestUtil.getHttpPrincipal(),
					_folder.getFolderId());
			}
		}
		catch (Exception e) {
		}
	}

	@Test
	public void testAddFileEntry() throws Exception {
		addFileEntry("Test Add.txt");
	}

	@Test
	public void testDeleteFileEntry() throws Exception {
		FileEntry fileEntry = addFileEntry("Test Delete.txt");

		DLAppServiceHttp.deleteFileEntry(
			HttpPrincipalTestUtil.getHttpPrincipal(),
			fileEntry.getFileEntryId());
	}

	@Test
	public void testGetFileEntry() throws Exception {
		FileEntry fileEntry = addFileEntry("Test Get.txt");

		DLAppServiceHttp.getFileEntryByUuidAndGroupId(
			HttpPrincipalTestUtil.getHttpPrincipal(), fileEntry.getUuid(),
			fileEntry.getGroupId());
	}

	protected FileEntry addFileEntry(String title) throws Exception {
		long folderId = _folder.getFolderId();
		String description = StringPool.BLANK;
		String changeLog = StringPool.BLANK;
		byte[] bytes = _CONTENT.getBytes();

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		return DLAppServiceHttp.addFileEntry(
			HttpPrincipalTestUtil.getHttpPrincipal(), _group.getGroupId(),
			folderId, title, ContentTypes.TEXT_PLAIN, title, description,
			changeLog, bytes, serviceContext);
	}

	private static final String _CONTENT =
		"Content: Enterprise. Open Source. For Life.";

	private Folder _folder;
	private Group _group;

}