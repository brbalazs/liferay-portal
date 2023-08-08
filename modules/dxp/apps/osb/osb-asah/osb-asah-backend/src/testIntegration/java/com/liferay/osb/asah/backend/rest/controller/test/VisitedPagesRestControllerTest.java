/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller.test;

import com.liferay.osb.asah.backend.OSBAsahBackendSpringTestContext;
import com.liferay.osb.asah.backend.rest.controller.VisitedPagesRestController;
import com.liferay.osb.asah.common.spring.resource.ResourceUtil;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;
import com.liferay.osb.asah.test.util.annotation.RepositoryResource;
import com.liferay.osb.asah.test.util.repository.CrudBQVisitedPageRepository;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import org.json.JSONObject;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.skyscreamer.jsonassert.JSONAssert;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Leslie Wong
 */
public class VisitedPagesRestControllerTest
	implements OSBAsahBackendSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@Disabled
	@RepositoryResource(
		repositoryClass = CrudBQVisitedPageRepository.class,
		resourcePath = "osbasahfaroinfo/visited_pages.json"
	)
	@Test
	public void testGetVisitedPage() throws Exception {
		JSONAssert.assertEquals(
			new JSONObject(),
			new JSONObject(
				_visitedPagesRestController.getVisitedPages(
					"356972058733468926")),
			false);
	}

	@BQSQLResource(resourcePath = "test_visited_pages_rest_controller.sql")
	@Test
	public void testGetVisitedPages() throws Exception {

		// Not visited pages

		JSONAssert.assertEquals(
			ResourceUtil.readResourceToJSONObject(
				"dependencies/expected_visited_pages_not_visited.json", this),
			new JSONObject(
				_visitedPagesRestController.getVisitedPages(
					1L, null, "356970616429554152", "individual", 0, 20,
					new String[] {"title", "asc"}, false)),
			false);

		// Not visited pages with interest

		JSONAssert.assertEquals(
			ResourceUtil.readResourceToJSONObject(
				"dependencies/expected_visited_pages_not_visited_interest.json",
				this),
			new JSONObject(
				_visitedPagesRestController.getVisitedPages(
					1L, "interestName eq 'compelling action-items'",
					"356970540447478387", "individual", 0, 20,
					new String[] {"title", "asc"}, false)),
			false);

		// Visited pages

		JSONAssert.assertEquals(
			ResourceUtil.readResourceToJSONObject(
				"dependencies/expected_visited_pages_visited.json", this),
			new JSONObject(
				_visitedPagesRestController.getVisitedPages(
					1L, null, "356970527927171432", "individual", 0, 20,
					new String[] {"title", "asc"}, true)),
			false);

		// Visited pages with interest

		JSONAssert.assertEquals(
			ResourceUtil.readResourceToJSONObject(
				"dependencies/expected_visited_pages_visited_interest.json",
				this),
			new JSONObject(
				_visitedPagesRestController.getVisitedPages(
					1L, "interestName eq 'revolutionary ROI'",
					"356970527927171432", "individual", 0, 20,
					new String[] {"title", "asc"}, true)),
			false);
	}

	@Autowired
	private VisitedPagesRestController _visitedPagesRestController;

}