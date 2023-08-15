/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog;

import com.liferay.osb.asah.common.entity.Suppression;
import com.liferay.osb.asah.common.model.Sort;
import com.liferay.osb.asah.common.repository.SuppressionRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;

/**
 * @author Matthew Kong
 */
@Component
public class SuppressionDog {

	public Suppression addSuppression(
		Long dataControlTaskBatchId, Date dataControlTaskCreateDate,
		String emailAddress) {

		Suppression suppression = new Suppression();

		suppression.setCreateDate(new Date());
		suppression.setDataControlTaskBatchId(dataControlTaskBatchId);
		suppression.setDataControlTaskCreateDate(dataControlTaskCreateDate);
		suppression.setEmailAddress(emailAddress);

		return _suppressionRepository.insert(suppression);
	}

	public void deleteByEmailAddress(String emailAddress) {
		_suppressionRepository.deleteByEmailAddress(emailAddress);
	}

	public Suppression fetchSuppression(String emailAddress) {
		Optional<Suppression> suppressionOptional =
			_suppressionRepository.findByEmailAddress(emailAddress);

		return suppressionOptional.orElse(null);
	}

	public Page<Suppression> getSuppressionPage(
		String emailAddress, int page, int size, Sort sort) {

		PageRequest pageRequest = PageRequest.of(page, size, sort);

		return PageableExecutionUtils.getPage(
			_suppressionRepository.getSuppressions(emailAddress, pageRequest),
			pageRequest,
			() -> _suppressionRepository.countSuppressions(emailAddress));
	}

	public List<Suppression> getSuppressions(String filterString) {
		return _suppressionRepository.getSuppressions(filterString);
	}

	public void hideSuppressionByEmailAddress(String emailAddress) {
		_suppressionRepository.hideSuppression(emailAddress);
	}

	@Autowired
	private SuppressionRepository _suppressionRepository;

}