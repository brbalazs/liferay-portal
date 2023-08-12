/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog;

import com.liferay.osb.asah.common.entity.BQIdentity;
import com.liferay.osb.asah.common.repository.BQIdentityRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 */
@Component
public class BQIdentityDog {

	public List<BQIdentity> getBQIdentities(List<String> bqIdentityIds) {
		return _bqIdentityRepository.findByIdIn(bqIdentityIds);
	}

	public List<String> getBQIdentityIds(String bqIndividualId) {
		return _bqIdentityRepository.getBQIdentityIds(bqIndividualId, false);
	}

	public List<String> getBQIdentityIdsIgnoreSuppresion(
		String bqIndividualId) {

		return _bqIdentityRepository.getBQIdentityIds(bqIndividualId, true);
	}

	public String getBQIndividualId(String bqIdentityId) {
		return _bqIdentityRepository.getBQIndividualId(bqIdentityId);
	}

	@Autowired
	private BQIdentityRepository _bqIdentityRepository;

}