/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.model;

import com.liferay.osb.asah.common.entity.BQIdentityInterestScore;

import java.util.Date;
import java.util.Objects;

/**
 * @author Ivica Cardic
 */
public class IdentityInterestScore {

	public IdentityInterestScore(
		BQIdentityInterestScore bqIdentityInterestScore,
		Long contributingPagesCount, String individualId) {

		_contributingPagesCount = contributingPagesCount;
		_individualId = individualId;

		_identityId = bqIdentityInterestScore.getIdentityId();
		_interestScore = bqIdentityInterestScore.getInterestScore();
		_keyword = bqIdentityInterestScore.getKeyword();
		_recordedDate = bqIdentityInterestScore.getRecordedDate();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof IdentityInterestScore)) {
			return false;
		}

		IdentityInterestScore identityInterestScore =
			(IdentityInterestScore)obj;

		if (Objects.equals(
				_contributingPagesCount,
				identityInterestScore._contributingPagesCount) &&
			Objects.equals(_identityId, identityInterestScore._identityId) &&
			Objects.equals(
				_individualId, identityInterestScore._individualId) &&
			Objects.equals(
				_interestScore, identityInterestScore._interestScore) &&
			Objects.equals(_keyword, identityInterestScore._keyword) &&
			Objects.equals(
				_recordedDate, identityInterestScore._recordedDate)) {

			return true;
		}

		return false;
	}

	public Long getContributingPagesCount() {
		return _contributingPagesCount;
	}

	public String getIdentityId() {
		return _identityId;
	}

	public String getIndividualId() {
		return _individualId;
	}

	public Double getInterestScore() {
		return _interestScore;
	}

	public String getKeyword() {
		return _keyword;
	}

	public Date getRecordedDate() {
		if (_recordedDate == null) {
			return null;
		}

		return new Date(_recordedDate.getTime());
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			_contributingPagesCount, _identityId, _individualId, _interestScore,
			_keyword, _recordedDate);
	}

	private final Long _contributingPagesCount;
	private final String _identityId;
	private final String _individualId;
	private final Double _interestScore;
	private final String _keyword;
	private final Date _recordedDate;

}