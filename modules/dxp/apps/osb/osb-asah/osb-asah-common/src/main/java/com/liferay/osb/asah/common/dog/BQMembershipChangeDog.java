/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog;

import com.liferay.osb.asah.common.date.dog.TimeZoneDog;
import com.liferay.osb.asah.common.dog.util.SortUtil;
import com.liferay.osb.asah.common.entity.BQMembershipChange;
import com.liferay.osb.asah.common.entity.Segment;
import com.liferay.osb.asah.common.model.MembershipCountSnapshot;
import com.liferay.osb.asah.common.model.Transformation;
import com.liferay.osb.asah.common.repository.BQMembershipChangeRepository;
import com.liferay.osb.asah.common.repository.helper.FilterHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;

/**
 * @author Inácio Nery
 */
@Component
public class BQMembershipChangeDog {

	public void addBQMembershipChange(BQMembershipChange bqMembershipChange) {
		_bqMembershipChangeRepository.insert(bqMembershipChange);
	}

	public void addBQMembershipChange(
		MembershipCountSnapshot membershipCountSnapshot) {

		_bqMembershipChangeRepository.addBQMembershipChange(
			membershipCountSnapshot);
	}

	public void deleteBQMembershipChanges(List<Long> segmentIds) {
		_bqMembershipChangeRepository.deleteBySegmentIdIn(segmentIds);
	}

	public List<Long> findSegmentIdByFilterString(String filterString) {
		return _bqMembershipChangeRepository.findSegmentIdByFilterString(
			filterString);
	}

	public BQMembershipChange getLastBQMembershipChangeBySegmentId(
		Long segmentId) {

		List<BQMembershipChange> bqMembershipChanges =
			getLastBQMembershipChangeBySegmentIds(
				Collections.singletonList(segmentId));

		if (bqMembershipChanges.isEmpty()) {
			return null;
		}

		return bqMembershipChanges.get(0);
	}

	public List<BQMembershipChange> getLastBQMembershipChangeBySegmentIds(
		List<Long> segmentIds) {

		return _bqMembershipChangeRepository.
			findLastBQMembershipChangeBySegmentIds(segmentIds);
	}

	public Map<Long, BQMembershipChange> getLastBQMembershipChanges(
		List<Segment> segments) {

		Map<Long, BQMembershipChange> bqMembershipChanges = new HashMap<>();

		List<Long> segmentIds = new ArrayList<>();

		for (Segment segment : segments) {
			segmentIds.add(segment.getId());
		}

		for (BQMembershipChange bqMembershipChange :
				_bqMembershipChangeRepository.
					findLastBQMembershipChangeBySegmentIds(segmentIds)) {

			bqMembershipChanges.put(
				bqMembershipChange.getSegmentId(), bqMembershipChange);
		}

		return bqMembershipChanges;
	}

	public List<Transformation> getMembershipChangeTransformations(
		boolean includeToday, Long segmentId, int page, int size) {

		return _bqMembershipChangeRepository.getMembershipChangeTransformations(
			includeToday, segmentId, PageRequest.of(page, size));
	}

	public void initializeBQMembershipChanges(Long channelId, Long segmentId) {
		_bqMembershipChangeRepository.initializeBQMembershipChanges(
			channelId, segmentId, _timeZoneDog.getZoneId());
	}

	public Page<BQMembershipChange> searchBQMembershipChangePage(
		String filterString, Long segmentId, int page, int size,
		String[] sorts) {

		FilterHelper filterHelper = new FilterHelper(filterString);

		PageRequest pageRequest = PageRequest.of(
			page, size, SortUtil.getSort(sorts));

		return PageableExecutionUtils.getPage(
			_bqMembershipChangeRepository.searchBQMembershipChanges(
				filterHelper, segmentId, pageRequest),
			pageRequest,
			() -> _bqMembershipChangeRepository.countBQMembershipChanges(
				filterHelper, segmentId));
	}

	@Autowired
	private BQMembershipChangeRepository _bqMembershipChangeRepository;

	@Autowired
	private TimeZoneDog _timeZoneDog;

}