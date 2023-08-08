/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.dog.util.SortUtil;
import com.liferay.osb.asah.common.entity.BQIdentityInterestScore;
import com.liferay.osb.asah.common.model.IdentityInterestScore;
import com.liferay.osb.asah.common.repository.BQIdentityInterestScoreRepository;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 */
@Component
public class BQIdentityInterestScoreDog {

	public Page<BQIdentityInterestScore> getBQIdentityInterestScorePage(
		@Nullable Long channelId, String individualId, int size, int start) {

		return PageableExecutionUtils.getPage(
			_bqIdentityInterestScoreRepository.findByChannelIdAndIndividualId(
				channelId, individualId, PageRequest.of(start / size, size)),
			PageRequest.of(start / size, size),
			() ->
				_bqIdentityInterestScoreRepository.
					countByChannelIdAndIndividualId(channelId, individualId));
	}

	public List<BQIdentityInterestScore> getBQIdentityInterestScores(
		String individualId, String keyword, Date fromRecordedDate,
		Date toRecordedDate) {

		return _bqIdentityInterestScoreRepository.
			findByIndividualIdAndKeywordAndRecordedDateBetween(
				individualId, keyword, fromRecordedDate, toRecordedDate);
	}

	public Page<IdentityInterestScore> getIdentityInterestScorePage(
		@Nullable Long channelId, @Nullable String individualId,
		@Nullable String keywords, int page, int size, String[] sorts) {

		PageRequest pageRequest = PageRequest.of(
			page, size, SortUtil.getSort(sorts));

		return PageableExecutionUtils.getPage(
			_bqIdentityInterestScoreRepository.
				findByChannelIdAndIndividualIdAndKeywords(
					channelId, individualId, keywords, pageRequest),
			pageRequest,
			() ->
				_bqIdentityInterestScoreRepository.
					countByChannelIdAndIndividualIdAndKeywords(
						channelId, individualId, keywords));
	}

	public Page<String> getKeywordsPage(String keywords, int page, int size) {
		PageRequest pageRequest = PageRequest.of(page, size);

		return PageableExecutionUtils.getPage(
			_bqIdentityInterestScoreRepository.getKeywords(
				keywords, pageRequest),
			pageRequest,
			() -> _bqIdentityInterestScoreRepository.countKeywords(keywords));
	}

	public List<String> getTopKeywords(String individualId, int size) {
		return _bqIdentityInterestScoreRepository.getTopKeywordsByIndividualId(
			individualId, size);
	}

	public JSONArray getTransformations(
		String apply, String filterString, int page, int size) {

		String period = "day";

		if (apply != null) {
			Matcher matcher = _periodPattern.matcher(apply);

			if (!matcher.find()) {
				throw new IllegalArgumentException("Invalid apply: " + apply);
			}

			String fieldName = matcher.group("fieldName");

			if (!fieldName.equals("dateRecorded")) {
				throw new IllegalArgumentException(
					"Compute function not supported for " + fieldName);
			}

			period = matcher.group("period");
		}

		Date toDate = DateUtil.addDays(DateUtil.newDayDate(), -(page * size));

		Date fromDate = DateUtil.addDays(toDate, 1 - size);

		return new JSONArray(
			_bqIdentityInterestScoreRepository.getTransformations(
				fromDate, filterString, period, toDate));
	}

	private static final Pattern _periodPattern = Pattern.compile(
		"compute\\((?<period>\\w+)\\((?<fieldName>\\w+)\\)\\)");

	@Autowired
	private BQIdentityInterestScoreRepository
		_bqIdentityInterestScoreRepository;

}