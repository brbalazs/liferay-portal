/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository;

import com.liferay.osb.asah.common.entity.Suppression;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.lang.Nullable;

/**
 * @author Marcellus Tavares
 */
public interface CustomSuppressionRepository {

	public long countSuppressions(@Nullable String emailAddress);

	@Modifying
	public void deleteByEmailAddress(String emailAddress);

	public List<Suppression> getSuppressions(
		@Nullable String emailAddress, Pageable pageable);

	@Modifying
	public Suppression insert(Suppression suppression);

	public void insertAll(List<Suppression> suppressions);

}