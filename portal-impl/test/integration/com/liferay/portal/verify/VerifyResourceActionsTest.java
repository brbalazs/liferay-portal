/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.verify;

import com.liferay.counter.kernel.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.model.ResourceAction;
import com.liferay.portal.kernel.service.ResourceActionLocalServiceUtil;
import com.liferay.portal.kernel.service.persistence.ResourceActionUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.TransactionalTestRule;
import com.liferay.portal.verify.test.BaseVerifyProcessTestCase;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Michael Bowerman
 */
public class VerifyResourceActionsTest extends BaseVerifyProcessTestCase {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(), TransactionalTestRule.INSTANCE);

	@Before
	@Override
	@Transactional
	public void setUp() throws Exception {
		super.setUp();

		_createResourceAction(_NAME_1, _ACTION_ID_1, 2);
		_createResourceAction(_NAME_1, _ACTION_ID_2, 2);
		_createResourceAction(_NAME_1, _ACTION_ID_3, 2);
		_createResourceAction(_NAME_2, _ACTION_ID_1, 2);
		_createResourceAction(_NAME_2, _ACTION_ID_2, 4);
	}

	@Test
	public void testDeleteDuplicateBitwiseValuesOnResource() throws Throwable {
		ResourceActionLocalServiceUtil.checkResourceActions();

		_assertResourceAction(_NAME_1, _ACTION_ID_1, false);
		_assertResourceAction(_NAME_1, _ACTION_ID_2, false);
		_assertResourceAction(_NAME_1, _ACTION_ID_3, false);
		_assertResourceAction(_NAME_2, _ACTION_ID_1, false);
		_assertResourceAction(_NAME_2, _ACTION_ID_2, false);

		doVerify();

		_assertResourceAction(_NAME_1, _ACTION_ID_1, false);
		_assertResourceAction(_NAME_1, _ACTION_ID_2, true);
		_assertResourceAction(_NAME_1, _ACTION_ID_3, true);
		_assertResourceAction(_NAME_2, _ACTION_ID_1, false);
		_assertResourceAction(_NAME_2, _ACTION_ID_2, false);
	}

	@Override
	protected VerifyProcess getVerifyProcess() {
		return new VerifyResourceActions();
	}

	private void _assertResourceAction(
		String name, String actionId, boolean expectsNull) {

		ResourceAction resourceAction =
			ResourceActionLocalServiceUtil.fetchResourceAction(name, actionId);

		if (expectsNull) {
			Assert.assertNull(resourceAction);
		}
		else {
			Assert.assertNotNull(resourceAction);
		}
	}

	private void _createResourceAction(
		final String name, final String actionId, final long bitwiseValue) {

		long resourceActionId = CounterLocalServiceUtil.increment(
			ResourceAction.class.getName());

		ResourceAction resourceAction = ResourceActionUtil.create(
			resourceActionId);

		resourceAction.setName(name);
		resourceAction.setActionId(actionId);
		resourceAction.setBitwiseValue(bitwiseValue);

		_resourceActions.add(ResourceActionUtil.update(resourceAction));
	}

	private static final String _ACTION_ID_1 = "action1";

	private static final String _ACTION_ID_2 = "action2";

	private static final String _ACTION_ID_3 = "action3";

	private static final String _NAME_1 = "portlet1";

	private static final String _NAME_2 = "portlet2";

	@DeleteAfterTestRun
	private final List<ResourceAction> _resourceActions = new ArrayList<>();

}