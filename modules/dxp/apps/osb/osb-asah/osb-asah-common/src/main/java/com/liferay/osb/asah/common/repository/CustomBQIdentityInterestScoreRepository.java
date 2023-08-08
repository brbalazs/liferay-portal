/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository;

import com.liferay.osb.asah.common.entity.BQIdentityInterestScore;
import com.liferay.osb.asah.common.model.CompositionResultBag;
import com.liferay.osb.asah.common.model.IdentityInterestScore;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;

/**
 * @author Ivica Cardic
 */
public interface CustomBQIdentityInterestScoreRepository {

	public long count();

	public long countByChannelIdAndIndividualId(
		@Nullable Long channelId, String individualId);

	public long countByChannelIdAndIndividualIdAndKeywords(
		@Nullable Long channelId, @Nullable String individualId,
		@Nullable String keywords);

	public long countByIndividualId(String individualId);

	public long countKeywords(@Nullable String keywords);

	@Modifying
	public void deleteByKeywordAndRecordedDateGreaterThanEqual(
		@Param("keyword") String keyword,
		@Param("recordedDate") Date recordedDate);

	@Modifying
	public void deleteByRecordedDate(Date recordedDate);

	@Modifying
	public void deleteByRecordedDateLessThanEqual(
		@Param("recordedDate") Date recordedDate);

	public List<BQIdentityInterestScore> findByChannelIdAndIndividualId(
		@Nullable Long channelId, String individualId, Pageable pageable);

	public List<IdentityInterestScore>
		findByChannelIdAndIndividualIdAndKeywords(
			@Nullable Long channelId, @Nullable String individualId,
			@Nullable String keywords, Pageable pageable);

	public List<BQIdentityInterestScore> findByIndividualId(
		String individualId, Pageable pageable);

	public List<BQIdentityInterestScore>
		findByIndividualIdAndKeywordAndRecordedDateBetween(
			String individualId, String keyword, Date recordedDate1,
			Date recordedDate2);

	public List<BQIdentityInterestScore> findByRecordedDate(
		@Nullable Date recordedDate, int size);

	public List<String> findIndividualIdsByFilterStringAndIndividualId(
		@Nullable String filterString, String individualId);

	public BQIdentityInterestScore getByIndividualIdAndKeywordAndRecordedDate(
		String individualId, String keyword, Date recordedDate);

	public CompositionResultBag getInterestCompositionResultBag(
		boolean active, @Nullable Long channelId, @Nullable String keywords,
		@Nullable Long segmentId, Pageable pageable);

	public List<String> getKeywords(
		@Nullable String keywords, Pageable pageable);

	public List<String> getTopKeywordsByIndividualId(
		String individualId, int size);

	public List<Map<String, Object>> getTransformations(
		Date fromDate, @Nullable String filterString, String period,
		Date toDate);

	public BQIdentityInterestScore insert(
		BQIdentityInterestScore bqIdentityInterestScore);

	public void insertAll(
		List<BQIdentityInterestScore> bqIdentityInterestScores);

}