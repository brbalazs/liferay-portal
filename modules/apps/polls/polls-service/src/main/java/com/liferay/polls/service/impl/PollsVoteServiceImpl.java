/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.polls.service.impl;

import com.liferay.polls.model.PollsQuestion;
import com.liferay.polls.model.PollsVote;
import com.liferay.polls.service.base.PollsVoteServiceBaseImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermissionFactory;
import com.liferay.portal.kernel.service.ServiceContext;

/**
 * @author Brian Wing Shun Chan
 */
public class PollsVoteServiceImpl extends PollsVoteServiceBaseImpl {

	@Override
	public PollsVote addVote(
			long questionId, long choiceId, ServiceContext serviceContext)
		throws PortalException {

		_pollsQuestionModelResourcePermission.check(
			getPermissionChecker(), questionId, ActionKeys.ADD_VOTE);

		return pollsVoteLocalService.addVote(
			getUserId(), questionId, choiceId, serviceContext);
	}

	private static volatile ModelResourcePermission<PollsQuestion>
		_pollsQuestionModelResourcePermission =
			ModelResourcePermissionFactory.getInstance(
				PollsVoteServiceImpl.class,
				"_pollsQuestionModelResourcePermission", PollsQuestion.class);

}