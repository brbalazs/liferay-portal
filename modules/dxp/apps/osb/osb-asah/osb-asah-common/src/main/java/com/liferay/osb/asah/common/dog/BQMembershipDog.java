/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.osb.asah.common.date.dog.TimeZoneDog;
import com.liferay.osb.asah.common.entity.BQMembership;
import com.liferay.osb.asah.common.entity.Segment;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.model.MembershipCountSnapshot;
import com.liferay.osb.asah.common.repository.BQMembershipRepository;
import com.liferay.osb.asah.common.util.ListUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * @author Michael Bowerman
 * @author Rachael Koestartyo
 */
@Component
public class BQMembershipDog {

	public BQMembership addBQMembership(BQMembership bqMembership) {
		return _bqMembershipRepository.insert(bqMembership);
	}

	public List<BQMembership> addBQMemberships(
		List<BQMembership> bqMemberships) {

		if (bqMemberships.isEmpty()) {
			return bqMemberships;
		}

		_bqMembershipRepository.insertAll(bqMemberships);

		BQMembership bqMembership = bqMemberships.get(0);

		_addBQMembershipChange(
			bqMembership.getChannelId(), bqMembership.getSegmentId());

		_bqMembershipIndividualDog.updateMembershipIndividuals(
			bqMembership.getSegmentId());

		return bqMemberships;
	}

	public void deleteBQMembership(String individualId, Segment segment) {
		_bqMembershipRepository.deleteByIndividualIdAndSegmentId(
			individualId, segment.getId());

		_addBQMembershipChange(segment.getChannelId(), segment.getId());
	}

	public void deleteBQMemberships(List<Long> segmentIds) {
		_bqMembershipChangeDog.deleteBQMembershipChanges(segmentIds);

		_bqMembershipRepository.deleteBySegmentIdIn(segmentIds);
	}

	public List<BQMembership> getActiveBQMemberships(
		String individualId, List<Long> segmentIds) {

		return _bqMembershipRepository.
			findByIndividualIdAndSegmentIdInAndStatus(
				individualId, segmentIds, "ACTIVE");
	}

	public long getActiveBQMembershipsCount(
		@Nullable Boolean includeAnonymousUsers, Long segmentId) {

		return _bqMembershipRepository.countActiveMembersBySegmentId(
			includeAnonymousUsers, segmentId, _timeZoneDog.getZoneId());
	}

	public List<String> getActiveIdentityIds(Long segmentId) {
		return _bqMembershipRepository.findIdentityIdBySegmentIdAndStatus(
			segmentId, "ACTIVE");
	}

	public List<Map<String, Long>> getActiveSegmentIds(String individualId) {
		return _bqMembershipRepository.
			findSegmentIdIdentitiesCountByIndividualIdAndStatus(
				individualId, "ACTIVE");
	}

	public Page<BQMembership> getBQMembershipPage(
		List<String> identityIds, Long segmentId, String status, int page,
		int size, String[] sorts) {

		PageRequest pageRequest = PageRequest.of(page, size, _getSort(sorts));

		return PageableExecutionUtils.getPage(
			_bqMembershipRepository.findByIdentityIdInAndSegmentIdAndStatus(
				identityIds, segmentId, status, pageRequest),
			pageRequest,
			() ->
				_bqMembershipRepository.
					countByIdentityIdInAndSegmentIdAndStatus(
						identityIds, segmentId, status));
	}

	public Page<BQMembership> getBQMembershipPage(
		Long segmentId, String status, int page, int size, String[] sorts) {

		PageRequest pageRequest = PageRequest.of(page, size, _getSort(sorts));

		return PageableExecutionUtils.getPage(
			_bqMembershipRepository.findBySegmentIdAndStatus(
				segmentId, status, pageRequest),
			pageRequest,
			() -> _bqMembershipRepository.countBySegmentIdAndStatus(
				segmentId, status));
	}

	public long getBQMembershipsCount(Long segmentId) {
		return _bqMembershipRepository.countBySegmentId(segmentId);
	}

	public List<Long> getIndividualSegmentIds(String individualId) {
		return _bqMembershipRepository.findSegmentIdByIndividualId(
			individualId);
	}

	public MembershipCountSnapshot getMembershipCountSnapshot(Segment segment) {
		return _bqMembershipRepository.getMembershipCountSnapshot(
			segment.getChannelId(), segment.getId());
	}

	public Map<Long, JSONObject> getMembershipsJSONObjects(
		String individualId, List<Segment> segments) {

		List<BQMembership> bqMemberships = getActiveBQMemberships(
			individualId, ListUtil.map(segments, Segment::getId));

		Map<Long, JSONObject> segmentJSONObjects = new HashMap<>();

		for (BQMembership bqMembership : bqMemberships) {
			segmentJSONObjects.put(
				bqMembership.getSegmentId(),
				_objectMapper.convertValue(bqMembership, JSONObject.class));
		}

		Map<Long, JSONObject> membershipsJSONObjects = new HashMap<>();

		for (Segment segment : segments) {
			Long segmentId = segment.getId();

			if (!segmentJSONObjects.containsKey(segmentId)) {
				if (_log.isWarnEnabled()) {
					_log.warn(
						"Unable to get active membership for user " +
							individualId + "and individual segment " +
								segmentId);
				}

				continue;
			}

			JSONObject membershipJSONObject = segmentJSONObjects.get(segmentId);

			membershipJSONObject.remove("dateModified");
			membershipJSONObject.remove("dateRemoved");
			membershipJSONObject.remove("id");

			membershipsJSONObjects.put(
				segmentId,
				JSONUtil.put("active-membership", membershipJSONObject));
		}

		return membershipsJSONObjects;
	}

	public List<Long> getSegmentIds(String individualId) {
		return _bqMembershipRepository.findTop20SegmentIdByIndividualId(
			individualId);
	}

	public boolean isMember(String identityId, Long segmentId) {
		return _bqMembershipRepository.existsByIdentityIdAndSegmentIdAndStatus(
			identityId, segmentId, "ACTIVE");
	}

	public void updateBQMemberships(
		String filterString, Boolean includeAnonymousUsers, Segment segment) {

		_bqMembershipRepository.updateBQMemberships(
			segment.getChannelId(), filterString, includeAnonymousUsers,
			segment.getId());
	}

	private void _addBQMembershipChange(Long channelId, Long segmentId) {
		_bqMembershipChangeDog.addBQMembershipChange(
			_bqMembershipRepository.getMembershipCountSnapshot(
				channelId, segmentId));
	}

	private Sort _getSort(String[] sorts) {
		if (ArrayUtils.isEmpty(sorts)) {
			return Sort.by(Sort.Order.desc("id"));
		}

		List<Sort.Order> orders = new ArrayList<>();

		for (int i = 0; i < (sorts.length - 1); i = i + 2) {
			if (Objects.equals(sorts[i + 1], "asc")) {
				orders.add(Sort.Order.asc(sorts[i]));
			}
			else {
				orders.add(Sort.Order.desc(sorts[i]));
			}
		}

		return Sort.by(orders);
	}

	private static final Log _log = LogFactory.getLog(BQMembershipDog.class);

	@Autowired
	private BQMembershipChangeDog _bqMembershipChangeDog;

	@Autowired
	private BQMembershipIndividualDog _bqMembershipIndividualDog;

	@Autowired
	private BQMembershipRepository _bqMembershipRepository;

	@Autowired
	private ObjectMapper _objectMapper;

	@Autowired
	private TimeZoneDog _timeZoneDog;

}