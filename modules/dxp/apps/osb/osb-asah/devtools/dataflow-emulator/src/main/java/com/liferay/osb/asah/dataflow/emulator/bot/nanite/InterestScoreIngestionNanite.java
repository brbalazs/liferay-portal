/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.emulator.bot.nanite;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.entity.BQIdentityInterestPage;
import com.liferay.osb.asah.common.entity.BQIdentityInterestScore;
import com.liferay.osb.asah.common.entity.BQSessionInterestScore;
import com.liferay.osb.asah.common.repository.BQEventRepository;
import com.liferay.osb.asah.common.repository.BQIdentityInterestPageRepository;
import com.liferay.osb.asah.common.repository.BQIdentityInterestScoreRepository;
import com.liferay.osb.asah.common.repository.BQSessionInterestScoreRepository;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.RandomUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Robson Pastor
 */
@Component
public class InterestScoreIngestionNanite {

	public void run() {
		Date date = DateUtil.newDayDate();

		List<BQIdentityInterestScore> bqIdentityInterestScores =
			new ArrayList<>();
		List<BQSessionInterestScore> bqSessionInterestScores = new ArrayList();
		Set<String> keywords = new HashSet<>();
		Set<String> userIds = new HashSet<>();

		for (Map<String, String> keywordsMap :
				_bqEventRepository.
					getKeywordsGroupedByChannelIdAndSessionIdAndUserId(date)) {

			String userId = keywordsMap.get("userId");

			if (!userIds.contains(userId)) {
				bqIdentityInterestScores.addAll(
					_getBQIdentityInterestScores(keywordsMap, date));

				userIds.add(userId);
			}

			bqSessionInterestScores.addAll(
				_getBQSessionInterestScores(keywordsMap, date));

			keywords.addAll(_getKeywords(keywordsMap.get("keywords")));
		}

		_bqIdentityInterestScoreRepository.deleteByRecordedDate(date);

		if (!bqIdentityInterestScores.isEmpty()) {
			_bqIdentityInterestScoreRepository.insertAll(
				bqIdentityInterestScores);
		}

		_bqSessionInterestScoreRepository.deleteByRecordedDate(date);

		if (!bqSessionInterestScores.isEmpty()) {
			_bqSessionInterestScoreRepository.insertAll(
				bqSessionInterestScores);
		}

		_bqIdentityInterestPageRepository.deleteAll();

		List<BQIdentityInterestPage> bqIdentityInterestPages =
			_getBQIdentityInterestPages(keywords);

		if (!bqIdentityInterestPages.isEmpty()) {
			_bqIdentityInterestPageRepository.insertAll(
				bqIdentityInterestPages);
		}
	}

	private List<BQIdentityInterestPage> _getBQIdentityInterestPages(
		Set<String> keywords) {

		List<BQIdentityInterestPage> bqIdentityInterestPages = new ArrayList();

		for (String keyword : keywords) {
			bqIdentityInterestPages.addAll(
				_bqIdentityInterestPageRepository.getBQIdentityInterestPages(
					keyword));
		}

		return bqIdentityInterestPages;
	}

	private List<BQIdentityInterestScore> _getBQIdentityInterestScores(
		Map<String, String> keywords, Date recordedDate) {

		List<BQIdentityInterestScore> bqIdentityInterestScores =
			new ArrayList();

		for (String keyword : _getKeywords(keywords.get("keywords"))) {
			double interestScore = RandomUtils.nextDouble(0, 1);
			boolean interested = false;

			if (interestScore > _INTEREST_THRESHOLD) {
				interested = true;
			}

			BQIdentityInterestScore bqIdentityInterestScore =
				new BQIdentityInterestScore();

			BigDecimal channelId = new BigDecimal(keywords.get("channelId"));

			bqIdentityInterestScore.setChannelId(channelId.longValue());

			bqIdentityInterestScore.setIdentityId(keywords.get("userId"));
			bqIdentityInterestScore.setInterestScore(interestScore);
			bqIdentityInterestScore.setInterested(interested);
			bqIdentityInterestScore.setKeyword(keyword);
			bqIdentityInterestScore.setRecordedDate(recordedDate);

			bqIdentityInterestScores.add(bqIdentityInterestScore);
		}

		return bqIdentityInterestScores;
	}

	private List<BQSessionInterestScore> _getBQSessionInterestScores(
		Map<String, String> keywords, Date recordedDate) {

		List<BQSessionInterestScore> bqSessionInterestScores = new ArrayList();

		for (String keyword : _getKeywords(keywords.get("keywords"))) {
			double interestScore = RandomUtils.nextDouble(0, 1);
			boolean interested = false;

			if (interestScore > _INTEREST_THRESHOLD) {
				interested = true;
			}

			BQSessionInterestScore bqSessionInterestScore =
				new BQSessionInterestScore();

			BigDecimal channelId = new BigDecimal(keywords.get("channelId"));

			bqSessionInterestScore.setChannelId(channelId.longValue());

			bqSessionInterestScore.setIdentityId(keywords.get("userId"));
			bqSessionInterestScore.setInterestScore(interestScore);
			bqSessionInterestScore.setInterested(interested);
			bqSessionInterestScore.setKeyword(keyword);
			bqSessionInterestScore.setRecordedDate(recordedDate);
			bqSessionInterestScore.setSessionId(keywords.get("sessionId"));

			bqSessionInterestScores.add(bqSessionInterestScore);
		}

		return bqSessionInterestScores;
	}

	private Set<String> _getKeywords(String keywords) {
		return new HashSet<>(Arrays.asList(keywords.split(" ")));
	}

	private static final double _INTEREST_THRESHOLD = 0.2;

	@Autowired
	private BQEventRepository _bqEventRepository;

	@Autowired
	private BQIdentityInterestPageRepository _bqIdentityInterestPageRepository;

	@Autowired
	private BQIdentityInterestScoreRepository
		_bqIdentityInterestScoreRepository;

	@Autowired
	private BQSessionInterestScoreRepository _bqSessionInterestScoreRepository;

}