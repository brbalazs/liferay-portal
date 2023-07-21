/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.workflow.web.internal.util.filter;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.PredicateFilter;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowDefinition;

/**
 * @author Adam Brandizzi
 */
public class WorkflowDefinitionDescriptionPredicateFilter
	implements PredicateFilter<WorkflowDefinition> {

	public WorkflowDefinitionDescriptionPredicateFilter(String keywords) {
		_keywords = keywords;
	}

	@Override
	public boolean filter(WorkflowDefinition workflowDefinition) {
		if (Validator.isNull(_keywords)) {
			return true;
		}

		String delimiter = StringPool.SPACE;

		if (!StringUtil.contains(_keywords, StringPool.SPACE)) {
			delimiter = StringPool.BLANK;
		}

		return StringUtil.containsIgnoreCase(
			workflowDefinition.getDescription(), _keywords, delimiter);
	}

	private final String _keywords;

}