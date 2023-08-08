/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository;

import com.liferay.osb.asah.common.entity.AsahTask;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;

/**
 * @author André Miranda
 */
public interface AsahTaskRepository extends Repository<AsahTask, Long> {

	@Cacheable
	public List<AsahTask> findByClassName(String className);

	@Cacheable
	public List<AsahTask> findByCronExpressionIsNotNull();

	@Cacheable
	public List<AsahTask> findByCronExpressionIsNull(Pageable pageable);

}