/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.polls.internal.verify.model;

import com.liferay.portal.kernel.verify.model.VerifiableAuditedModel;
import com.liferay.portal.kernel.verify.model.VerifiableGroupedModel;

/**
 * @author Miguel Pastor
 */
public class PollsChoiceVerifiableModel
	implements VerifiableAuditedModel, VerifiableGroupedModel {

	@Override
	public String getJoinByTableName() {
		return "questionId";
	}

	@Override
	public String getPrimaryKeyColumnName() {
		return "choiceId";
	}

	@Override
	public String getRelatedModelName() {
		return "PollsQuestion";
	}

	@Override
	public String getRelatedPKColumnName() {
		return "questionId";
	}

	@Override
	public String getRelatedPrimaryKeyColumnName() {
		return "questionId";
	}

	@Override
	public String getRelatedTableName() {
		return "PollsQuestion";
	}

	@Override
	public String getTableName() {
		return "PollsChoice";
	}

	@Override
	public boolean isAnonymousUserAllowed() {
		return false;
	}

	@Override
	public boolean isUpdateDates() {
		return false;
	}

}