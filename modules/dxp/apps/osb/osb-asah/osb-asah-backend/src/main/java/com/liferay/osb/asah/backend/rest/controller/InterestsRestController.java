/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller;

import com.liferay.osb.asah.backend.dto.InterestDTO;
import com.liferay.osb.asah.backend.dto.PageDTO;
import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.date.dog.util.TimeZoneDogUtil;
import com.liferay.osb.asah.common.dog.BQIdentityInterestScoreDog;
import com.liferay.osb.asah.common.entity.BQIdentityInterestScore;
import com.liferay.osb.asah.common.findbugs.SuppressFBWarnings;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.model.IdentityInterestScore;
import com.liferay.osb.asah.common.spring.annotation.Cacheable;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import org.jetbrains.annotations.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Vishal Reddy
 * @author David Bhasme
 */
@RequestMapping("/interests")
@RestController(
	"com.liferay.osb.asah.backend.rest.controller.InterestsRestController"
)
@SuppressFBWarnings("NM_SAME_SIMPLE_NAME_AS_SUPERCLASS")
public class InterestsRestController
	extends com.liferay.osb.asah.backend.rest.controller.api.data.source.v1.
				InterestsRestController {

	@Cacheable
	@GetMapping(params = "!apply")
	public PageDTO<InterestDTO> getInterestDTOPageDTO(
		@RequestParam(required = false) Long channelId,
		@RequestParam(required = false) String expand,
		@RequestParam(name = "ownerId", required = false) String individualId,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(required = false) String query,
		@RequestParam(defaultValue = "20") int size,
		@RequestParam(name = "sort", required = false) String[] sorts) {

		return _toPageDTO(
			expand,
			bqIdentityInterestScoreDog.getIdentityInterestScorePage(
				channelId, individualId, query, page, size, sorts));
	}

	@GetMapping("/keywords")
	public String getInterestKeywords(
		@RequestParam(required = false) String name,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "20") int size) {

		Page<String> keywordsPage = _bqIdentityInterestScoreDog.getKeywordsPage(
			name, page, size);

		return JSONUtil.put(
			"_embedded",
			JSONUtil.put("interest-keywords", keywordsPage.getContent())
		).put(
			"page",
			getPageJSONObject(page, size, keywordsPage.getTotalElements())
		).toString();
	}

	@Cacheable
	@GetMapping(params = "apply")
	public String getInterestTransformations(
		@RequestParam String apply,
		@RequestParam(name = "filter", required = false) String filterString,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "20") int size) {

		return JSONUtil.put(
			"_embedded",
			JSONUtil.put(
				"interest-transformations",
				bqIdentityInterestScoreDog.getTransformations(
					apply, filterString, page, size))
		).put(
			"page", getPageJSONObject(page, size, size)
		).toString();
	}

	@NotNull
	private InterestDTO _createInterestDTO(
		int days, IdentityInterestScore identityInterestScore) {

		InterestDTO interestDTO = new InterestDTO(identityInterestScore);

		Map<String, Object> embedded = new HashMap<>();

		if (days > 0) {
			LocalDateTime endDayLocalDateTime = DateUtil.newDayLocalDateTime(
				TimeZoneDogUtil.getZoneId());

			embedded.put(
				"interest-aggregation-last-" + days + "-days",
				_getInterestAggregations(
					endDayLocalDateTime,
					identityInterestScore.getIndividualId(),
					identityInterestScore.getKeyword(),
					endDayLocalDateTime.plusDays(1 - days)));
		}

		interestDTO.setEmbedded(embedded);

		return interestDTO;
	}

	private Set<InterestDTO> _createInterestDTOs(
		String expand, List<IdentityInterestScore> identityInterestScores) {

		int days = _getDaysRange(expand);

		Set<InterestDTO> interestDTOs = new LinkedHashSet<>();

		for (IdentityInterestScore identityInterestScore :
				identityInterestScores) {

			interestDTOs.add(_createInterestDTO(days, identityInterestScore));
		}

		return interestDTOs;
	}

	private int _getDaysRange(String expand) {
		if (StringUtils.isEmpty(expand)) {
			return 0;
		}

		Set<String> expandParts = new HashSet<>(
			Arrays.asList(expand.split(",")));

		if (expandParts.contains("interest-aggregation-last-30-days")) {
			return 30;
		}

		if (expandParts.contains("interest-aggregation-last-60-days")) {
			return 60;
		}

		if (expand.contains("interest-aggregation-last-90-days")) {
			return 90;
		}

		return 0;
	}

	private List<Map<String, Object>> _getInterestAggregations(
		LocalDateTime endDayLocalDateTime, String individualId, String keyword,
		LocalDateTime startDayLocalDateTime) {

		List<Map<String, Object>> interestAggregations = new ArrayList<>();

		List<BQIdentityInterestScore> bqIdentityInterestScores =
			bqIdentityInterestScoreDog.getBQIdentityInterestScores(
				individualId, keyword,
				DateUtil.toUTCDate(startDayLocalDateTime),
				DateUtil.toUTCDate(endDayLocalDateTime));

		Map<LocalDateTime, BQIdentityInterestScore> bqIdentityInterestScoreMap =
			new HashMap<>();

		for (BQIdentityInterestScore bqIdentityInterestScore :
				bqIdentityInterestScores) {

			bqIdentityInterestScoreMap.put(
				DateUtil.toLocalDateTime(
					bqIdentityInterestScore.getRecordedDate(),
					TimeZoneDogUtil.getZoneId()),
				bqIdentityInterestScore);
		}

		LocalDateTime currentDayLocalDateTime = startDayLocalDateTime;

		while (currentDayLocalDateTime.compareTo(endDayLocalDateTime) <= 0) {
			BQIdentityInterestScore bqIdentityInterestScore =
				bqIdentityInterestScoreMap.get(currentDayLocalDateTime);

			Map<String, Object> item = new HashMap<>();

			item.put("intervalInitDate", currentDayLocalDateTime.toString());

			if (bqIdentityInterestScore != null) {
				item.put(
					"scoreAvg", bqIdentityInterestScore.getInterestScore());
				item.put("totalElements", 1);
				item.put("viewsSum", 0);
			}
			else {
				item.put("scoreAvg", 0.0);
				item.put("totalElements", 0);
				item.put("viewsSum", 0);
			}

			interestAggregations.add(item);

			currentDayLocalDateTime = currentDayLocalDateTime.plusDays(1);
		}

		return interestAggregations;
	}

	private PageDTO<InterestDTO> _toPageDTO(
		InterestDTO interestDTO,
		Page<IdentityInterestScore> identityInterestScores) {

		return new PageDTO<>(
			"_embedded", interestDTO, identityInterestScores.getNumber(),
			identityInterestScores.getSize(),
			identityInterestScores.getTotalElements(),
			identityInterestScores.getTotalPages());
	}

	private PageDTO<InterestDTO> _toPageDTO(
		String expand, Page<IdentityInterestScore> identityInterestScores) {

		return _toPageDTO(
			new InterestDTO(
				_createInterestDTOs(
					expand, identityInterestScores.getContent())),
			identityInterestScores);
	}

	@Autowired
	private BQIdentityInterestScoreDog _bqIdentityInterestScoreDog;

}