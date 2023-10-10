/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog;

import com.liferay.osb.asah.common.entity.BlockedKeyword;
import com.liferay.osb.asah.common.repository.BlockedKeywordRepository;
import com.liferay.osb.asah.common.spring.http.exception.OSBAsahException;
import com.liferay.osb.asah.common.util.SetUtil;
import com.liferay.osb.asah.common.util.TimeOrderedUuidGenerator;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * @author André Miranda
 */
@Component
public class BlockedKeywordDog {

	public List<BlockedKeyword> addMissingBlockedKeywords(
		Set<String> keywords) {

		keywords = _normalizeKeywords(keywords);

		if (keywords.isEmpty()) {
			throw new OSBAsahException(
				HttpStatus.BAD_REQUEST, "Empty keywords");
		}

		List<BlockedKeyword> missingBlockedKeywords =
			_createMissingBlockedKeywords(
				getBlockedKeywords(keywords), keywords);

		if (missingBlockedKeywords.isEmpty()) {
			return Collections.emptyList();
		}

		return IterableUtils.toList(
			_blockedKeywordRepository.saveAll(missingBlockedKeywords));
	}

	public void deleteBlockedKeywords(List<Long> blockedKeywordIds) {
		_blockedKeywordRepository.deleteByIdIn(
			new HashSet<>(blockedKeywordIds));
	}

	public BlockedKeyword getBlockedKeyword(Long blockedKeywordId) {
		Optional<BlockedKeyword> blockedKeywordOptional =
			_blockedKeywordRepository.findById(blockedKeywordId);

		return blockedKeywordOptional.orElseThrow(
			() -> new OSBAsahException(
				HttpStatus.BAD_REQUEST,
				"There is no blocked keyword with ID " + blockedKeywordId));
	}

	public Page<BlockedKeyword> getBlockedKeywordPage(
		String keyword, int page, int size, String[] sorts) {

		Sort sort = Sort.by(Sort.Order.asc("keyword"));

		if (keyword != null) {
			return PageableExecutionUtils.getPage(
				_blockedKeywordRepository.findByKeywordContainingIgnoreCase(
					keyword, PageRequest.of(page, size, sort)),
				PageRequest.of(page, size, sort),
				() ->
					_blockedKeywordRepository.
						countByKeywordContainingIgnoreCase(keyword));
		}

		return _blockedKeywordRepository.findAll(
			PageRequest.of(page, size, sort));
	}

	public List<BlockedKeyword> getBlockedKeywords() {
		return IterableUtils.toList(_blockedKeywordRepository.findAll());
	}

	public List<BlockedKeyword> getBlockedKeywords(Set<String> keywords) {
		return _blockedKeywordRepository.findByKeywordIn(
			_normalizeKeywords(keywords));
	}

	private List<BlockedKeyword> _createMissingBlockedKeywords(
		List<BlockedKeyword> existingBlockedKeywords, Set<String> newKeywords) {

		Date date = new Date();

		Set<String> existingKeywords = SetUtil.map(
			existingBlockedKeywords, BlockedKeyword::getKeyword);

		Stream<String> stream = newKeywords.stream();

		return stream.filter(
			keyword -> !existingKeywords.contains(keyword)
		).map(
			keyword -> {
				BlockedKeyword blockedKeyword = new BlockedKeyword(
					date, keyword);

				blockedKeyword.setId(
					_timeOrderedUuidGenerator.generateIdAsLong());
				blockedKeyword.setIsNew(Boolean.TRUE);

				return blockedKeyword;
			}
		).collect(
			Collectors.toList()
		);
	}

	private Set<String> _normalizeKeywords(Set<String> keywords) {
		Stream<String> stream = keywords.stream();

		return stream.map(
			String::trim
		).map(
			String::toLowerCase
		).filter(
			StringUtils::isNotEmpty
		).collect(
			Collectors.toSet()
		);
	}

	@Autowired
	private BlockedKeywordRepository _blockedKeywordRepository;

	private final TimeOrderedUuidGenerator _timeOrderedUuidGenerator =
		new TimeOrderedUuidGenerator();

}