/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller;

import com.liferay.osb.asah.backend.dto.BlockedKeywordDTO;
import com.liferay.osb.asah.backend.dto.PageDTO;
import com.liferay.osb.asah.common.dog.BlockedKeywordDog;
import com.liferay.osb.asah.common.entity.BlockedKeyword;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.spring.http.exception.OSBAsahException;

import java.util.List;
import java.util.Set;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Marcellus Tavares
 */
@RequestMapping("/blocked-keywords")
@RestController
public class BlockedKeywordsRestController extends BaseRestController {

	@DeleteMapping
	public void deleteBlockedKeywords(@RequestBody List<Long> ids)
		throws OSBAsahException {

		if (ids.isEmpty()) {
			throw new OSBAsahException(
				HttpStatus.BAD_REQUEST, "Empty blocked keyword IDs");
		}

		_blockedKeywordDog.deleteBlockedKeywords(ids);
	}

	@GetMapping("/{id}")
	public BlockedKeywordDTO getBlockedKeywordDTO(@PathVariable Long id) {
		return new BlockedKeywordDTO(_blockedKeywordDog.getBlockedKeyword(id));
	}

	@GetMapping
	public PageDTO<BlockedKeywordDTO> getBlockedKeywordDTOPageDTO(
		@RequestParam(required = false) String keyword,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "20") int size,
		@RequestParam(name = "sort", required = false) String[] sorts) {

		return _toPageDTO(
			_blockedKeywordDog.getBlockedKeywordPage(
				keyword, page, size, sorts));
	}

	@PostMapping
	public BlockedKeywordDTO postBlockedKeywords(@RequestBody String json) {
		JSONObject jsonObject = new JSONObject(json);

		Set<String> keywords = JSONUtil.toStringSet(
			jsonObject.getJSONArray("keywords"));

		List<BlockedKeyword> duplicatedBlockedKeywords =
			_blockedKeywordDog.getBlockedKeywords(keywords);

		List<BlockedKeyword> addedBlockedKeywords =
			_blockedKeywordDog.addMissingBlockedKeywords(keywords);

		return new BlockedKeywordDTO(
			duplicatedBlockedKeywords, addedBlockedKeywords);
	}

	private PageDTO<BlockedKeywordDTO> _toPageDTO(
		Page<BlockedKeyword> blockedKeywordsPage) {

		return new PageDTO<>(
			"_embedded",
			new BlockedKeywordDTO(blockedKeywordsPage.getContent()),
			blockedKeywordsPage.getNumber(), blockedKeywordsPage.getSize(),
			blockedKeywordsPage.getTotalElements(),
			blockedKeywordsPage.getTotalPages());
	}

	@Autowired
	private BlockedKeywordDog _blockedKeywordDog;

}