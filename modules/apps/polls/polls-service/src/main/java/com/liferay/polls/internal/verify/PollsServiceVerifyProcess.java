/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.polls.internal.verify;

import com.liferay.polls.internal.verify.model.PollsChoiceVerifiableModel;
import com.liferay.polls.internal.verify.model.PollsVoteVerifiableModel;
import com.liferay.polls.service.PollsChoiceLocalService;
import com.liferay.portal.kernel.util.LoggingTimer;
import com.liferay.portal.verify.VerifyAuditedModel;
import com.liferay.portal.verify.VerifyGroupedModel;
import com.liferay.portal.verify.VerifyProcess;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Miguel Pastor
 */
@Component(
	immediate = true,
	property = "verify.process.name=com.liferay.polls.service",
	service = VerifyProcess.class
)
public class PollsServiceVerifyProcess extends VerifyProcess {

	@Override
	protected void doVerify() throws Exception {
		verifyAuditedModels();
		verifyGroupedModels();
	}

	@Reference(unbind = "-")
	protected void setPollsChoiceLocalService(
		PollsChoiceLocalService pollsChoiceLocalService) {
	}

	protected void verifyAuditedModels() throws Exception {
		try (LoggingTimer loggingTimer = new LoggingTimer()) {
			_verifyAuditedModel.verify(new PollsChoiceVerifiableModel());
			_verifyAuditedModel.verify(new PollsVoteVerifiableModel());
		}
	}

	protected void verifyGroupedModels() throws Exception {
		try (LoggingTimer loggingTimer = new LoggingTimer()) {
			_verifyGroupedModel.verify(new PollsChoiceVerifiableModel());
			_verifyGroupedModel.verify(new PollsVoteVerifiableModel());
		}
	}

	private final VerifyAuditedModel _verifyAuditedModel =
		new VerifyAuditedModel();
	private final VerifyGroupedModel _verifyGroupedModel =
		new VerifyGroupedModel();

}