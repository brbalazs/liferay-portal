/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.document.library.uad.exporter.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.document.library.kernel.model.DLFolder;
import com.liferay.document.library.kernel.service.DLFolderLocalService;
import com.liferay.document.library.uad.test.DLFolderUADTestUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.user.associated.data.exporter.UADExporter;
import com.liferay.user.associated.data.test.util.BaseUADExporterTestCase;
import com.liferay.user.associated.data.test.util.WhenHasStatusByUserIdField;

import java.util.ArrayList;
import java.util.List;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.runner.RunWith;

/**
 * @author Brian Wing Shun Chan
 */
@RunWith(Arquillian.class)
public class DLFolderUADExporterTest
	extends BaseUADExporterTestCase<DLFolder>
	implements WhenHasStatusByUserIdField<DLFolder> {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Override
	public DLFolder addBaseModelWithStatusByUserId(
			long userId, long statusByUserId)
		throws Exception {

		DLFolder dlFolder = DLFolderUADTestUtil.addDLFolderWithStatusByUserId(
			_dlFolderLocalService, userId, statusByUserId);

		_dlFolders.add(dlFolder);

		return dlFolder;
	}

	@Override
	protected DLFolder addBaseModel(long userId) throws Exception {
		DLFolder dlFolder = DLFolderUADTestUtil.addDLFolder(
			_dlFolderLocalService, userId);

		_dlFolders.add(dlFolder);

		return dlFolder;
	}

	@Override
	protected String getPrimaryKeyName() {
		return "folderId";
	}

	@Override
	protected UADExporter getUADExporter() {
		return _uadExporter;
	}

	@Inject
	private DLFolderLocalService _dlFolderLocalService;

	@DeleteAfterTestRun
	private final List<DLFolder> _dlFolders = new ArrayList<>();

	@Inject(filter = "component.name=*.DLFolderUADExporter")
	private UADExporter _uadExporter;

}