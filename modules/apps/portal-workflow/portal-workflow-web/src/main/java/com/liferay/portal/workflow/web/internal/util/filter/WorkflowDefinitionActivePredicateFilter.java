/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.workflow.web.internal.util.filter;

import com.liferay.portal.kernel.util.PredicateFilter;
import com.liferay.portal.kernel.workflow.WorkflowDefinition;
import com.liferay.portal.workflow.web.internal.constants.WorkflowDefinitionConstants;

/**
 * @author Adam Brandizzi
 */
public class WorkflowDefinitionActivePredicateFilter
	implements PredicateFilter<WorkflowDefinition> {

	public WorkflowDefinitionActivePredicateFilter(int status) {
		_status = status;
	}

	@Override
	public boolean filter(WorkflowDefinition workflowDefinition) {
		if (_status == WorkflowDefinitionConstants.STATUS_ALL) {
			return true;
		}
		else if (_status == WorkflowDefinitionConstants.STATUS_PUBLISHED) {
			return workflowDefinition.isActive();
		}

		return !workflowDefinition.isActive();
	}

	private final int _status;

}