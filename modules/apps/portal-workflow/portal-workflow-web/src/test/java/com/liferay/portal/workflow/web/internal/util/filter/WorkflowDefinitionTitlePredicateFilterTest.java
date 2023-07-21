/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.workflow.web.internal.util.filter;

import com.liferay.portal.kernel.workflow.WorkflowDefinition;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Leonardo Barros
 */
public class WorkflowDefinitionTitlePredicateFilterTest {

	@Test
	public void testFilterWithoutSpace1() {
		WorkflowDefinitionTitlePredicateFilter filter =
			new WorkflowDefinitionTitlePredicateFilter("Single");

		WorkflowDefinition workflowDefinition = new WorkflowDefinitionImpl(
			null, "Single Approver");

		boolean result = filter.filter(workflowDefinition);

		Assert.assertTrue(result);
	}

	@Test
	public void testFilterWithoutSpace2() {
		WorkflowDefinitionTitlePredicateFilter filter =
			new WorkflowDefinitionTitlePredicateFilter("Appr");

		WorkflowDefinition workflowDefinition = new WorkflowDefinitionImpl(
			null, "Single Approver");

		boolean result = filter.filter(workflowDefinition);

		Assert.assertTrue(result);
	}

	@Test
	public void testFilterWithoutSpace3() {
		WorkflowDefinitionTitlePredicateFilter filter =
			new WorkflowDefinitionTitlePredicateFilter("Approver");

		WorkflowDefinition workflowDefinition = new WorkflowDefinitionImpl(
			null, "A Different Definition");

		boolean result = filter.filter(workflowDefinition);

		Assert.assertFalse(result);
	}

	@Test
	public void testFilterWithSpace1() {
		WorkflowDefinitionTitlePredicateFilter filter =
			new WorkflowDefinitionTitlePredicateFilter("Single Approver");

		WorkflowDefinition workflowDefinition = new WorkflowDefinitionImpl(
			null, "Single Approver Definition");

		boolean result = filter.filter(workflowDefinition);

		Assert.assertTrue(result);
	}

	@Test
	public void testFilterWithSpace2() {
		WorkflowDefinitionTitlePredicateFilter filter =
			new WorkflowDefinitionTitlePredicateFilter("Single Approver");

		WorkflowDefinition workflowDefinition = new WorkflowDefinitionImpl(
			null, "A Different Definition");

		boolean result = filter.filter(workflowDefinition);

		Assert.assertFalse(result);
	}

}