/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.message.boards.uad.display.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.message.boards.model.MBCategory;
import com.liferay.message.boards.service.MBCategoryLocalService;
import com.liferay.message.boards.uad.test.MBCategoryUADTestUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.user.associated.data.display.UADDisplay;
import com.liferay.user.associated.data.test.util.BaseUADDisplayTestCase;

import java.util.ArrayList;
import java.util.List;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.runner.RunWith;

/**
 * @author Brian Wing Shun Chan
 */
@RunWith(Arquillian.class)
public class MBCategoryUADDisplayTest
	extends BaseUADDisplayTestCase<MBCategory> {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Override
	protected MBCategory addBaseModel(long userId) throws Exception {
		MBCategory mbCategory = MBCategoryUADTestUtil.addMBCategory(
			_mbCategoryLocalService, userId);

		_mbCategories.add(mbCategory);

		return mbCategory;
	}

	@Override
	protected UADDisplay getUADDisplay() {
		return _uadDisplay;
	}

	@DeleteAfterTestRun
	private final List<MBCategory> _mbCategories = new ArrayList<>();

	@Inject
	private MBCategoryLocalService _mbCategoryLocalService;

	@Inject(filter = "component.name=*.MBCategoryUADDisplay")
	private UADDisplay _uadDisplay;

}