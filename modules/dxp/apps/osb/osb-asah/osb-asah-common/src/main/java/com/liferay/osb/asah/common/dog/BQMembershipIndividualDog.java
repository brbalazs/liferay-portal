/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog;

import com.liferay.osb.asah.common.dog.util.SortUtil;
import com.liferay.osb.asah.common.entity.BQMembershipIndividual;
import com.liferay.osb.asah.common.repository.BQMembershipIndividualRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 */
@Component
public class BQMembershipIndividualDog {

	public Page<BQMembershipIndividual> getMembershipIndividualPage(
		int page, Long segmentId, int size, @Nullable String[] sorts) {

		PageRequest pageRequest = PageRequest.of(
			page, size,
			SortUtil.getSort(Sort.by(Sort.Order.desc("modifiedDate")), sorts));

		return PageableExecutionUtils.getPage(
			_bqMembershipIndividualRepository.getMembershipIndividuals(
				pageRequest, segmentId),
			pageRequest,
			() -> _bqMembershipIndividualRepository.countMembershipIndividuals(
				segmentId));
	}

	public void updateMembershipIndividuals() {
		_bqMembershipIndividualRepository.updateMembershipIndividuals();
	}

	public void updateMembershipIndividuals(Long segmentId) {
		_bqMembershipIndividualRepository.updateMembershipIndividuals(
			segmentId);
	}

	@Autowired
	private BQMembershipIndividualRepository _bqMembershipIndividualRepository;

}