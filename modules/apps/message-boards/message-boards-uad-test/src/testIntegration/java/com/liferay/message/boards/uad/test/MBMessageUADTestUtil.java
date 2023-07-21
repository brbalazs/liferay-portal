/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.message.boards.uad.test;

import com.liferay.message.boards.model.MBCategory;
import com.liferay.message.boards.model.MBMessage;
import com.liferay.message.boards.service.MBCategoryLocalService;
import com.liferay.message.boards.service.MBMessageLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.io.Serializable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Brian Wing Shun Chan
 */
public class MBMessageUADTestUtil {

	public static MBMessage addMBMessage(
			MBCategoryLocalService mbCategoryLocalService,
			MBMessageLocalService mbMessageLocalService, long userId)
		throws Exception {

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				TestPropsValues.getGroupId());

		MBCategory mbCategory = mbCategoryLocalService.addCategory(
			userId, 0, RandomTestUtil.randomString(),
			RandomTestUtil.randomString(), serviceContext);

		return mbMessageLocalService.addMessage(
			userId, RandomTestUtil.randomString(), TestPropsValues.getGroupId(),
			mbCategory.getCategoryId(), RandomTestUtil.randomString(),
			RandomTestUtil.randomString(), serviceContext);
	}

	public static MBMessage addMBMessageWithStatusByUserId(
			MBCategoryLocalService mbCategoryLocalService,
			MBMessageLocalService mbMessageLocalService, long userId,
			long statusByUserId)
		throws Exception {

		MBMessage mbMessage = addMBMessage(
			mbCategoryLocalService, mbMessageLocalService, userId);

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				TestPropsValues.getGroupId());

		Map<String, Serializable> workflowContext = new HashMap<>();

		workflowContext.put(WorkflowConstants.CONTEXT_URL, "http://localhost");

		return mbMessageLocalService.updateStatus(
			statusByUserId, mbMessage.getMessageId(),
			WorkflowConstants.STATUS_APPROVED, serviceContext, workflowContext);
	}

	public static void cleanUpDependencies(
			MBCategoryLocalService mbCategoryLocalService,
			List<MBMessage> mbMessages)
		throws Exception {

		for (MBMessage mbMessage : mbMessages) {
			mbCategoryLocalService.deleteCategory(mbMessage.getCategoryId());
		}
	}

}