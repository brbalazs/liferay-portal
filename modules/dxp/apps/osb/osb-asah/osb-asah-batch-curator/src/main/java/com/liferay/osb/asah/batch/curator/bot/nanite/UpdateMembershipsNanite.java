/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.batch.curator.bot.nanite;

import com.liferay.osb.asah.common.dog.BQMembershipChangeDog;
import com.liferay.osb.asah.common.dog.BQMembershipDog;
import com.liferay.osb.asah.common.dog.BQMembershipIndividualDog;
import com.liferay.osb.asah.common.dog.SegmentDog;
import com.liferay.osb.asah.common.entity.Segment;
import com.liferay.osb.asah.common.model.MembershipCountSnapshot;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 */
@Component
public class UpdateMembershipsNanite extends BaseNanite {

	@Override
	public boolean isLogRunEnabled() {
		return true;
	}

	@Override
	public void run(JSONObject contextJSONObject) {
		_updateDynamicSegmentMemberships();
		_updateStaticSegmentMembershipChanges();

		_updateMembershipIndividuals();
	}

	@Override
	protected Log getLog() {
		return _log;
	}

	private boolean _isProcessableSegment(Segment segment) {
		if (StringUtils.equalsIgnoreCase("DISABLED", segment.getState()) ||
			StringUtils.equalsIgnoreCase("INACTIVE", segment.getStatus())) {

			return false;
		}

		return true;
	}

	private void _updateDynamicSegmentMemberships() {
		int page = 0;

		while (true) {
			List<Segment> segments = _segmentDog.getSegments(
				page++, 50, Segment.Type.DYNAMIC);

			if (segments.isEmpty()) {
				break;
			}

			segments.forEach(this::_updateDynamicSegmentMemberships);
		}
	}

	private void _updateDynamicSegmentMemberships(Segment segment) {
		if (!_isProcessableSegment(segment)) {
			if (_log.isDebugEnabled()) {
				_log.debug(
					"Skipping disabled or inactive dynamic segment " +
						segment.getId());
			}

			return;
		}

		String filterString = segment.getFilter();

		try {
			_bqMembershipDog.updateBQMemberships(
				filterString, segment.getIncludeAnonymousUsers(), segment);

			if (_log.isInfoEnabled()) {
				_log.info(
					String.format(
						"Updated memberships successfully for segment ID %s " +
							"and filter %s",
						segment.getId(), filterString));
			}

			MembershipCountSnapshot membershipCountSnapshot =
				_bqMembershipDog.getMembershipCountSnapshot(segment);

			_bqMembershipChangeDog.addBQMembershipChange(
				membershipCountSnapshot);

			_segmentDog.updateSegmentMembershipCount(
				membershipCountSnapshot, segment);
		}
		catch (Exception exception) {
			_log.error(
				String.format(
					"Unable to update memberships for segment ID %s and " +
						"filter %s:%n%s",
					segment.getId(), filterString,
					StringUtils.join(
						Arrays.copyOf(
							ExceptionUtils.getStackFrames(exception), 10))));
		}
		finally {
			if (StringUtils.equalsIgnoreCase(
					segment.getState(), "IN_PROGRESS")) {

				_segmentDog.updateSegmentState(segment, "READY");
			}
		}
	}

	private void _updateMembershipIndividuals() {
		_bqMembershipIndividualDog.updateMembershipIndividuals();

		if (_log.isInfoEnabled()) {
			_log.info("Updated membership individuals successfully");
		}
	}

	private void _updateStaticSegmentMembershipChanges() {
		int page = 0;

		while (true) {
			List<Segment> segments = _segmentDog.getSegments(
				page++, 50, Segment.Type.STATIC);

			if (segments.isEmpty()) {
				break;
			}

			segments.forEach(this::_updateStaticSegmentMembershipChanges);
		}
	}

	private void _updateStaticSegmentMembershipChanges(Segment segment) {
		if (!_isProcessableSegment(segment)) {
			if (_log.isDebugEnabled()) {
				_log.debug(
					"Skipping disabled or inactive static segment " +
						segment.getId());
			}

			return;
		}

		Long segmentId = segment.getId();

		try {
			MembershipCountSnapshot membershipCountSnapshot =
				_bqMembershipDog.getMembershipCountSnapshot(segment);

			_bqMembershipChangeDog.addBQMembershipChange(
				membershipCountSnapshot);

			_segmentDog.updateSegmentMembershipCount(
				membershipCountSnapshot, segment);

			if (_log.isInfoEnabled()) {
				_log.info(
					String.format(
						"Updated membership changes successfully for segment " +
							"ID %s",
						segmentId));
			}
		}
		catch (Exception exception) {
			_log.error(
				String.format(
					"Unable to update membership changes for segment ID %s",
					segmentId));
		}
	}

	private static final Log _log = LogFactory.getLog(
		UpdateMembershipsNanite.class);

	@Autowired
	private BQMembershipChangeDog _bqMembershipChangeDog;

	@Autowired
	private BQMembershipDog _bqMembershipDog;

	@Autowired
	private BQMembershipIndividualDog _bqMembershipIndividualDog;

	@Autowired
	private SegmentDog _segmentDog;

}