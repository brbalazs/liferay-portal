/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.verify;

import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.verify.test.BaseVerifyProcessTestCase;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Manuel de la Peña
 * @author Preston Crary
 */
public class VerifyUserTest extends BaseVerifyProcessTestCase {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Test
	public void testVerifyInactive() throws Exception {
		_user = UserTestUtil.addUser();

		UserLocalServiceUtil.updateStatus(
			_user.getUserId(), WorkflowConstants.STATUS_INACTIVE,
			new ServiceContext());

		Group group = _user.getGroup();

		group.setActive(true);

		GroupLocalServiceUtil.updateGroup(group);

		doVerify();

		group = _user.getGroup();

		Assert.assertFalse(GroupLocalServiceUtil.isLiveGroupActive(group));
	}

	@Override
	protected VerifyProcess getVerifyProcess() {
		return new VerifyUser();
	}

	@DeleteAfterTestRun
	private User _user;

}