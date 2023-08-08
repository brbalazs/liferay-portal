/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller.test;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.osb.asah.backend.OSBAsahBackendSpringTestContext;
import com.liferay.osb.asah.backend.dto.BlockedKeywordDTO;
import com.liferay.osb.asah.backend.rest.controller.BlockedKeywordsRestController;
import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.dog.BlockedKeywordDog;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.repository.BlockedKeywordRepository;
import com.liferay.osb.asah.common.spring.http.exception.OSBAsahException;
import com.liferay.osb.asah.common.spring.resource.ResourceUtil;
import com.liferay.osb.asah.test.util.annotation.RepositoryResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.BooleanUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.skyscreamer.jsonassert.JSONAssert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

/**
 * @author Marcellus Tavares
 */
public class BlockedKeywordsRestControllerTest
	implements OSBAsahBackendSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@RepositoryResource(
		repositoryClass = BlockedKeywordRepository.class,
		resourcePath = "osbasahfaroinfo/blocked_keywords.json"
	)
	@Test
	public void testDeleteBlockedKeywords() {
		_blockedKeywordsRestController.deleteBlockedKeywords(
			Arrays.asList(351238757269547424L, 351238757269547425L));

		Assertions.assertThrows(
			OSBAsahException.class,
			() -> _blockedKeywordDog.getBlockedKeyword(351238757269547424L));
		Assertions.assertThrows(
			OSBAsahException.class,
			() -> _blockedKeywordDog.getBlockedKeyword(351238757269547425L));
	}

	@RepositoryResource(
		repositoryClass = BlockedKeywordRepository.class,
		resourcePath = "osbasahfaroinfo/blocked_keywords.json"
	)
	@Test
	public void testDeleteBlockedKeywordsWithEmptyIDs() {
		try {
			_blockedKeywordsRestController.deleteBlockedKeywords(
				Collections.emptyList());
		}
		catch (OSBAsahException osbAsahException) {
			Assertions.assertEquals(
				"Empty blocked keyword IDs", osbAsahException.getMessage());
		}
	}

	@RepositoryResource(
		repositoryClass = BlockedKeywordRepository.class,
		resourcePath = "osbasahfaroinfo/blocked_keywords.json"
	)
	@Test
	public void testGetBlockedKeyword() {
		BlockedKeywordDTO blockedKeywordDTO =
			_blockedKeywordsRestController.getBlockedKeywordDTO(
				351238757269547424L);

		Assertions.assertEquals(
			"2019-01-02T17:09:07.666Z",
			DateUtil.toUTCString(blockedKeywordDTO.getCreateDate()));
		Assertions.assertEquals(
			"351238757269547424", blockedKeywordDTO.getId());
		Assertions.assertEquals("liferay", blockedKeywordDTO.getKeyword());
	}

	@RepositoryResource(
		repositoryClass = BlockedKeywordRepository.class,
		resourcePath = "osbasahfaroinfo/blocked_keywords.json"
	)
	@Test
	public void testGetBlockedKeywordsFilter() throws Exception {
		JSONAssert.assertEquals(
			ResourceUtil.readResourceToString(
				"dependencies/expected_blocked_keywords_filter.json", this),
			_objectMapper.convertValue(
				_blockedKeywordsRestController.getBlockedKeywordDTOPageDTO(
					"web", 0, 2, null),
				JSONObject.class),
			false);
		JSONAssert.assertEquals(
			ResourceUtil.readResourceToString(
				"dependencies/expected_blocked_keywords_filter_number.json",
				this),
			_objectMapper.convertValue(
				_blockedKeywordsRestController.getBlockedKeywordDTOPageDTO(
					"1500", 0, 1, null),
				JSONObject.class),
			false);
		JSONAssert.assertEquals(
			ResourceUtil.readResourceToString(
				"dependencies/expected_blocked_keywords_filter_number.json",
				this),
			_objectMapper.convertValue(
				_blockedKeywordsRestController.getBlockedKeywordDTOPageDTO(
					"1500", 0, 1, null),
				JSONObject.class),
			false);
	}

	@RepositoryResource(
		repositoryClass = BlockedKeywordRepository.class,
		resourcePath = "osbasahfaroinfo/blocked_keywords.json"
	)
	@Test
	public void testGetBlockedKeywordsPagination() throws Exception {
		JSONAssert.assertEquals(
			ResourceUtil.readResourceToString(
				"dependencies/expected_blocked_keywords_page_0.json", this),
			_objectMapper.convertValue(
				_blockedKeywordsRestController.getBlockedKeywordDTOPageDTO(
					null, 0, 2, null),
				JSONObject.class),
			false);
		JSONAssert.assertEquals(
			ResourceUtil.readResourceToString(
				"dependencies/expected_blocked_keywords_page_1.json", this),
			_objectMapper.convertValue(
				_blockedKeywordsRestController.getBlockedKeywordDTOPageDTO(
					null, 1, 2, null),
				JSONObject.class),
			false);
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = BlockedKeywordRepository.class,
		resourcePath = "osbasahfaroinfo/blocked_keywords.json"
	)
	@Test
	public void testGetBlockedKeywordsSort() throws Exception {
		JSONAssert.assertEquals(
			ResourceUtil.readResourceToString(
				"dependencies/expected_blocked_keywords_sort_date.json", this),
			_objectMapper.convertValue(
				_blockedKeywordsRestController.getBlockedKeywordDTOPageDTO(
					null, 0, 2, new String[] {"createDate,desc"}),
				JSONObject.class),
			false);

		JSONAssert.assertEquals(
			ResourceUtil.readResourceToString(
				"dependencies/expected_blocked_keywords_sort_keyword.json",
				this),
			_objectMapper.convertValue(
				_blockedKeywordsRestController.getBlockedKeywordDTOPageDTO(
					null, 0, 8, new String[] {"keyword,asc"}),
				JSONObject.class),
			false);
	}

	@Test
	public void testPostBlockedKeywords() {
		BlockedKeywordDTO blockedKeywordDTO =
			_blockedKeywordsRestController.postBlockedKeywords(
				JSONUtil.put(
					"keywords", JSONUtil.putAll("Liferay", "DXP", "Portal", " ")
				).toString());

		Assertions.assertTrue(blockedKeywordDTO.getSucceeded());

		List<BlockedKeywordDTO> blockedKeywordDTOs =
			blockedKeywordDTO.getBlockedKeywordDTOs();

		Assertions.assertEquals(
			3, blockedKeywordDTOs.size(), blockedKeywordDTOs.toString());

		_assertBlockedKeyword(blockedKeywordDTOs, "liferay");
		_assertBlockedKeyword(blockedKeywordDTOs, "dxp");
		_assertBlockedKeyword(blockedKeywordDTOs, "portal");
	}

	@Test
	public void testPostEmptyBlockedKeywords() {
		try {
			_blockedKeywordsRestController.postBlockedKeywords(
				JSONUtil.put(
					"keywords", new JSONArray()
				).toString());
		}
		catch (OSBAsahException osbAsahException) {
			Assertions.assertEquals(
				HttpStatus.BAD_REQUEST, osbAsahException.getHttpStatus());
			Assertions.assertEquals(
				"Empty keywords", osbAsahException.getMessage());
		}
	}

	@RepositoryResource(
		repositoryClass = BlockedKeywordRepository.class,
		resourcePath = "osbasahfaroinfo/blocked_keywords.json"
	)
	@Test
	public void testPostExistingBlockedKeywords() {
		BlockedKeywordDTO blockedKeywordDTO =
			_blockedKeywordsRestController.postBlockedKeywords(
				JSONUtil.put(
					"keywords", JSONUtil.putAll("Liferay", "AC", "Portal")
				).toString());

		List<BlockedKeywordDTO> blockedKeywordDTOs =
			blockedKeywordDTO.getBlockedKeywordDTOs();

		Assertions.assertEquals(
			3, blockedKeywordDTOs.size(), blockedKeywordDTOs.toString());

		Assertions.assertFalse(_isDuplicate(blockedKeywordDTOs, "ac"));
		Assertions.assertTrue(_isDuplicate(blockedKeywordDTOs, "liferay"));
		Assertions.assertTrue(_isDuplicate(blockedKeywordDTOs, "portal"));
	}

	private void _assertBlockedKeyword(
		List<BlockedKeywordDTO> blockedKeywordDTOs, String expectedKeyword) {

		BlockedKeywordDTO actualBlockedKeywordDTO = _getBlockedKeyword(
			blockedKeywordDTOs, expectedKeyword);

		Assertions.assertNotNull(
			actualBlockedKeywordDTO, "Blocked keyword not found");
		Assertions.assertEquals(
			expectedKeyword, actualBlockedKeywordDTO.getKeyword());
		Assertions.assertTrue(actualBlockedKeywordDTO.getCreateDate() != null);
		Assertions.assertTrue(actualBlockedKeywordDTO.getId() != null);
	}

	private BlockedKeywordDTO _getBlockedKeyword(
		List<BlockedKeywordDTO> blockedKeywordDTOs, String keyword) {

		for (BlockedKeywordDTO blockedKeywordDTO : blockedKeywordDTOs) {
			if (Objects.equals(blockedKeywordDTO.getKeyword(), keyword)) {
				return blockedKeywordDTO;
			}
		}

		return null;
	}

	private boolean _isDuplicate(
		List<BlockedKeywordDTO> blockedKeywordDTOs, String keyword) {

		BlockedKeywordDTO blockedKeywordDTO = _getBlockedKeyword(
			blockedKeywordDTOs, keyword);

		Assertions.assertNotNull(
			blockedKeywordDTO, "Blocked keyword not found");

		return BooleanUtils.toBoolean(blockedKeywordDTO.getDuplicate());
	}

	@Autowired
	private BlockedKeywordDog _blockedKeywordDog;

	@Autowired
	private BlockedKeywordsRestController _blockedKeywordsRestController;

	@Autowired
	private ObjectMapper _objectMapper;

}