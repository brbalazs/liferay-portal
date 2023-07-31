/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.osb.asah.common.repository;

import com.liferay.osb.asah.common.entity.BQIndividual;
import com.liferay.osb.asah.common.model.Distribution;
import com.liferay.osb.asah.common.model.Individual;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

/**
 * @author Robson Pastor
 * @author Ivica Cardic
 */
public interface CustomBQIndividualRepository {

	public long countBQIndividuals(
		@Nullable Long accountId, @Nullable Long channelId,
		@Nullable Long dataSourceId, @Nullable String interestName,
		@Nullable Long notSegmentId, @Nullable String query,
		@Nullable Long segmentId);

	public long countBQIndividuals(
		@Nullable Long channelId, String filterString,
		@Nullable Boolean includeAnonymousUsers, @Nullable String query,
		@Nullable Long segmentId);

	public long countBQIndividualsCreatedSince(Date startDate);

	public long countBQIndividualsModifiedLast30Days(Long channelId);

	public long countIndividualFieldValuesCustom(
		@Nullable Long channelId, String fieldName,
		@Nullable String filterString);

	public long countIndividualFieldValuesDemographics(
		@Nullable Long channelId, String fieldName,
		@Nullable String filterString);

	public void deleteAll();

	public Optional<Individual> findByChannelIdAndId(
		@Nullable Long channelId, String id);

	public Optional<BQIndividual> findByEmailAddress(String emailAddresses);

	public List<Distribution> getIndividualDistributions(
		@Nullable Long channelId, String fieldName, String fieldType,
		@Nullable Long individualSegmentId, Pageable pageable);

	public BQIndividual insert(BQIndividual bqIndividual);

	public List<Individual> searchBQIndividuals(
		@Nullable Long accountId, @Nullable Long channelId,
		@Nullable Long dataSourceId, @Nullable String interestName,
		@Nullable Long notSegmentId, Pageable pageable, @Nullable String query,
		@Nullable Long segmentId);

	public List<Individual> searchBQIndividuals(
		@Nullable Long channelId, String filterString, Pageable pageable,
		@Nullable String query, @Nullable Long segmentId);

	public List<Long> searchIndividualDataSourceIds(String id);

	public List<String> searchIndividualFieldValuesCustom(
		@Nullable Long channelId, String fieldName,
		@Nullable String filterString, Pageable pageable);

	public List<String> searchIndividualFieldValuesDemographics(
		@Nullable Long channelId, String fieldName,
		@Nullable String filterString, Pageable pageable);

	public void updateSuppressed(String id, Boolean suppressed);

}