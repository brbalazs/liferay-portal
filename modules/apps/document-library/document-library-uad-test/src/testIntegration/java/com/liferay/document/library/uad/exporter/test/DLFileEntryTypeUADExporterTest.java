/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.document.library.uad.exporter.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.document.library.kernel.model.DLFileEntryType;
import com.liferay.document.library.kernel.service.DLFileEntryTypeLocalService;
import com.liferay.document.library.uad.test.DLFileEntryTypeUADTestUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.user.associated.data.exporter.UADExporter;
import com.liferay.user.associated.data.test.util.BaseUADExporterTestCase;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.runner.RunWith;

/**
 * @author Brian Wing Shun Chan
 */
@RunWith(Arquillian.class)
public class DLFileEntryTypeUADExporterTest
	extends BaseUADExporterTestCase<DLFileEntryType> {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@After
	public void tearDown() throws Exception {
		DLFileEntryTypeUADTestUtil.cleanUpDependencies(
			_dlFileEntryTypeLocalService, _portal, _dlFileEntryTypes);
	}

	@Override
	protected DLFileEntryType addBaseModel(long userId) throws Exception {
		DLFileEntryType dlFileEntryType =
			DLFileEntryTypeUADTestUtil.addDLFileEntryType(
				_dlFileEntryTypeLocalService, _portal, userId);

		_dlFileEntryTypes.add(dlFileEntryType);

		return dlFileEntryType;
	}

	@Override
	protected String getPrimaryKeyName() {
		return "fileEntryTypeId";
	}

	@Override
	protected UADExporter getUADExporter() {
		return _uadExporter;
	}

	@Inject
	private DLFileEntryTypeLocalService _dlFileEntryTypeLocalService;

	@DeleteAfterTestRun
	private final List<DLFileEntryType> _dlFileEntryTypes = new ArrayList<>();

	@Inject
	private Portal _portal;

	@Inject(filter = "component.name=*.DLFileEntryTypeUADExporter")
	private UADExporter _uadExporter;

}