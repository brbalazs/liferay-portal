/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.osb.asah.common.entity.BQSession;
import com.liferay.osb.asah.common.repository.BQSessionRepository;
import com.liferay.osb.asah.common.repository.helper.FilterHelper;
import com.liferay.osb.asah.common.spring.annotation.VisibleForTestingOnly;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;

/**
 * @author Alejo Ceballos
 * @author Marcos Martins
 */
@Component
public class UserSessionDog {

	@VisibleForTestingOnly
	public BQSession addBQSession(
		long channelId, String id, Date sessionEndDate, Date sessionStartDate) {

		BQSession bqSession = new BQSession();

		bqSession.setChannelId(channelId);
		bqSession.setId(id);
		bqSession.setSessionEnd(sessionEndDate);
		bqSession.setSessionStart(sessionStartDate);

		return _bqSessionRepository.insert(bqSession);
	}

	public List<BQSession> findByIds(Collection<String> ids) {
		return _bqSessionRepository.findAllById(ids);
	}

	public Page<String> getBQSessionFieldValuePage(
		String fieldName, String filterString, int page, int size,
		String value) {

		FilterHelper filterHelper = new FilterHelper(filterString);
		PageRequest pageRequest = PageRequest.of(page, size);

		return PageableExecutionUtils.getPage(
			_bqSessionRepository.searchSessionFieldValues(
				fieldName, filterHelper, pageRequest, value),
			pageRequest,
			() -> _bqSessionRepository.countSessionFieldValues(
				fieldName, filterHelper, value));
	}

	public List<Long> getIndividualIds(String filterString) {
		List<Long> individualIds = new ArrayList<>();

		// TODO Search for individual ids

		return individualIds;
	}

	@Autowired
	private BQSessionRepository _bqSessionRepository;

	@Autowired
	private ObjectMapper _objectMapper;

}