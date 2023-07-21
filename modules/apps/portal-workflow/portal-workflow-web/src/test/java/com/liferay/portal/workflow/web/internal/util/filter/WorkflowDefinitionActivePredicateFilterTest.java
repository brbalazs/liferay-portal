/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.workflow.web.internal.util.filter;

import com.liferay.portal.kernel.workflow.WorkflowDefinition;
import com.liferay.portal.workflow.web.internal.constants.WorkflowDefinitionConstants;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Adam Brandizzi
 */
public class WorkflowDefinitionActivePredicateFilterTest {

	@Test
	public void testFilterAllIncludeActive() {
		WorkflowDefinitionActivePredicateFilter filter =
			new WorkflowDefinitionActivePredicateFilter(
				WorkflowDefinitionConstants.STATUS_ALL);

		WorkflowDefinition workflowDefinition = new WorkflowDefinitionImpl(
			true);

		boolean result = filter.filter(workflowDefinition);

		Assert.assertTrue(result);
	}

	@Test
	public void testFilterAllIncludeInactive() {
		WorkflowDefinitionActivePredicateFilter filter =
			new WorkflowDefinitionActivePredicateFilter(
				WorkflowDefinitionConstants.STATUS_ALL);

		WorkflowDefinition workflowDefinition = new WorkflowDefinitionImpl(
			false);

		boolean result = filter.filter(workflowDefinition);

		Assert.assertTrue(result);
	}

	@Test
	public void testFilterNotPublishedExcludeActive() {
		WorkflowDefinitionActivePredicateFilter filter =
			new WorkflowDefinitionActivePredicateFilter(
				WorkflowDefinitionConstants.STATUS_NOT_PUBLISHED);

		WorkflowDefinition workflowDefinition = new WorkflowDefinitionImpl(
			true);

		boolean result = filter.filter(workflowDefinition);

		Assert.assertFalse(result);
	}

	@Test
	public void testFilterNotPublishedIncludeInactive() {
		WorkflowDefinitionActivePredicateFilter filter =
			new WorkflowDefinitionActivePredicateFilter(
				WorkflowDefinitionConstants.STATUS_NOT_PUBLISHED);

		WorkflowDefinition workflowDefinition = new WorkflowDefinitionImpl(
			false);

		boolean result = filter.filter(workflowDefinition);

		Assert.assertTrue(result);
	}

	@Test
	public void testFilterPublishedExcludeInactive() {
		WorkflowDefinitionActivePredicateFilter filter =
			new WorkflowDefinitionActivePredicateFilter(
				WorkflowDefinitionConstants.STATUS_PUBLISHED);

		WorkflowDefinition workflowDefinition = new WorkflowDefinitionImpl(
			false);

		boolean result = filter.filter(workflowDefinition);

		Assert.assertFalse(result);
	}

	@Test
	public void testFilterPublishedIncludeActive() {
		WorkflowDefinitionActivePredicateFilter filter =
			new WorkflowDefinitionActivePredicateFilter(
				WorkflowDefinitionConstants.STATUS_PUBLISHED);

		WorkflowDefinition workflowDefinition = new WorkflowDefinitionImpl(
			true);

		boolean result = filter.filter(workflowDefinition);

		Assert.assertTrue(result);
	}

}