/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.workflow.web.internal.util.filter;

import com.liferay.portal.kernel.workflow.WorkflowDefinition;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Adam Brandizzi
 */
public class WorkflowDefinitionDescriptionPredicateFilterTest {

	@Test
	public void testFilterWithoutSpace1() {
		WorkflowDefinitionDescriptionPredicateFilter filter =
			new WorkflowDefinitionDescriptionPredicateFilter("Default");

		WorkflowDefinition workflowDefinition = new WorkflowDefinitionImpl(
			null, "Single Approver", "Default Single Approver");

		boolean result = filter.filter(workflowDefinition);

		Assert.assertTrue(result);
	}

	@Test
	public void testFilterWithoutSpace2() {
		WorkflowDefinitionDescriptionPredicateFilter filter =
			new WorkflowDefinitionDescriptionPredicateFilter("Def");

		WorkflowDefinition workflowDefinition = new WorkflowDefinitionImpl(
			null, "Single Approver", "Default Single Approver");

		boolean result = filter.filter(workflowDefinition);

		Assert.assertTrue(result);
	}

	@Test
	public void testFilterWithoutSpace3() {
		WorkflowDefinitionDescriptionPredicateFilter filter =
			new WorkflowDefinitionDescriptionPredicateFilter("Approver");

		WorkflowDefinition workflowDefinition = new WorkflowDefinitionImpl(
			null, "A Different Definition", "Not that one");

		boolean result = filter.filter(workflowDefinition);

		Assert.assertFalse(result);
	}

	@Test
	public void testFilterWithSpace1() {
		WorkflowDefinitionDescriptionPredicateFilter filter =
			new WorkflowDefinitionDescriptionPredicateFilter("Single Approver");

		WorkflowDefinition workflowDefinition = new WorkflowDefinitionImpl(
			null, "Single Approver Definition",
			"Single Approver by Default Default ");

		boolean result = filter.filter(workflowDefinition);

		Assert.assertTrue(result);
	}

	@Test
	public void testFilterWithSpace2() {
		WorkflowDefinitionDescriptionPredicateFilter filter =
			new WorkflowDefinitionDescriptionPredicateFilter("Single Approver");

		WorkflowDefinition workflowDefinition = new WorkflowDefinitionImpl(
			null, "A Different Definition", "Not that one");

		boolean result = filter.filter(workflowDefinition);

		Assert.assertFalse(result);
	}

}