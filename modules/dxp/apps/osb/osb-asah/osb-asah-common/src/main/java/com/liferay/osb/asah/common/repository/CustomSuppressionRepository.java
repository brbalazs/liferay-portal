/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
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