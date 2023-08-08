/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository;

import com.liferay.osb.asah.common.entity.BQMembershipIndividual;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Modifying;

/**
 * @author Marcellus Tavares
 */
public interface CustomBQMembershipIndividualRepository {

	@Cacheable
	public long countMembershipIndividuals(Long segmentId);

	@Cacheable
	public List<BQMembershipIndividual> getMembershipIndividuals(
		Pageable pageable, Long segmentId);

	@CacheEvict(allEntries = true)
	@Modifying
	public void updateMembershipIndividuals();

	@CacheEvict(allEntries = true)
	@Modifying
	public void updateMembershipIndividuals(Long segmentId);

}