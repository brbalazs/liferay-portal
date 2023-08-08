/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.model;

import java.util.Objects;

/**
 * @author Marcellus Tavares
 */
public class MembershipCountSnapshot {

	public MembershipCountSnapshot(
		long channelId, long identitiesCount, long individualsCount,
		long segmentId) {

		_channelId = channelId;
		_identitiesCount = identitiesCount;
		_individualsCount = individualsCount;
		_segmentId = segmentId;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if ((obj == null) || !(obj instanceof MembershipCountSnapshot)) {
			return false;
		}

		MembershipCountSnapshot membershipCountSnapshot =
			(MembershipCountSnapshot)obj;

		if (Objects.equals(_channelId, membershipCountSnapshot._channelId) &&
			Objects.equals(
				_identitiesCount, membershipCountSnapshot._identitiesCount) &&
			Objects.equals(
				_individualsCount, membershipCountSnapshot._individualsCount) &&
			Objects.equals(_segmentId, membershipCountSnapshot._segmentId)) {

			return true;
		}

		return false;
	}

	public long getChannelId() {
		return _channelId;
	}

	public long getIdentitiesCount() {
		return _identitiesCount;
	}

	public long getIndividualsCount() {
		return _individualsCount;
	}

	public long getSegmentId() {
		return _segmentId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			_channelId, _identitiesCount, _individualsCount, _segmentId);
	}

	private final long _channelId;
	private final long _identitiesCount;
	private final long _individualsCount;
	private final long _segmentId;

}