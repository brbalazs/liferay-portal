/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository;

import com.liferay.osb.asah.common.entity.DXPEntity;
import com.liferay.osb.asah.common.entity.Segment;
import com.liferay.osb.asah.common.model.Transformation;
import com.liferay.osb.asah.common.repository.helper.FilterHelper;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

/**
 * @author Ivica Cardic
 */
public interface CustomSegmentRepository {

	@Cacheable
	public long countByChannelId(Long channelId);

	@Cacheable
	public long countPreviewDisabledSegments(
		List<Long> dataSourceFieldMappingFieldNames, Long dataSourceId,
		FilterHelper filterHelper);

	@Cacheable
	public long countSegments(
		FilterHelper filterHelper,
		List<Map<String, Long>> segmentIdIdentityCounts);

	@Cacheable
	public long countSegments(List<Long> channelIds, FilterHelper filterHelper);

	@Cacheable
	public List<Segment> findByChannelId(Long channelId, Pageable pageable);

	@Cacheable
	public List<Transformation> getSegmentTransformations(
		String apply, FilterHelper filterHelper, Pageable pageable,
		@Nullable List<Long> segmentIds);

	@Cacheable
	public List<Segment> searchDynamicSegments(
		FilterHelper filterHelper, @Nullable Boolean includeAnonymousUsers,
		Pageable pageable, Set<Long> segmentIds);

	@Cacheable
	public List<Segment> searchDynamicSegments(
		FilterHelper filterHelper, Pageable pageable);

	@Cacheable
	public List<Segment> searchPreviewDisabledSegments(
		List<Long> dataSourceFieldMappingFieldNames, Long dataSourceId,
		FilterHelper filterHelper, Pageable pageable);

	@Cacheable
	public List<Segment> searchSegments(
		FilterHelper filterHelper,
		List<Map<String, Long>> segmentIdIdentityCounts, Pageable pageable);

	@Cacheable
	public List<Segment> searchSegments(
		List<Long> channelIds, FilterHelper filterHelper, Pageable pageable);

	@Cacheable
	public List<Segment> searchSegments(
		Long dxpEntityId, DXPEntity.Type dxpEntityType, String state,
		Segment.Type type);

	@Cacheable
	public List<Segment> searchSegments(
		String filterString, String state, String status, Pageable pageable);

}