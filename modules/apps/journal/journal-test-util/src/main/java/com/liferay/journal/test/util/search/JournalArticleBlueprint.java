/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.journal.test.util.search;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.util.Locale;
import java.util.Map;

/**
 * @author André de Oliveira
 */
public class JournalArticleBlueprint {

	public long[] getAssetCategoryIds() {
		return assetCategoryIds;
	}

	public String getContentString() {
		return journalArticleContent.getContentString();
	}

	public long getGroupId() {
		return groupId;
	}

	public Map<Locale, String> getTitleMap() {
		return journalArticleTitle.getValues();
	}

	public long getUserId() {
		if (userId > 0) {
			return userId;
		}

		try {
			return TestPropsValues.getUserId();
		}
		catch (PortalException pe) {
			throw new RuntimeException(pe);
		}
	}

	public int getWorkflowAction() {
		if (draft) {
			return WorkflowConstants.ACTION_SAVE_DRAFT;
		}

		return WorkflowConstants.ACTION_PUBLISH;
	}

	public boolean isWorkflowEnabled() {
		return workflowEnabled;
	}

	protected long[] assetCategoryIds;
	protected boolean draft;
	protected long groupId;
	protected JournalArticleContent journalArticleContent;
	protected JournalArticleTitle journalArticleTitle;
	protected long userId;
	protected boolean workflowEnabled;

}