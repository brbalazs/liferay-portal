/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository;

import com.liferay.osb.asah.common.entity.Project;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author André Miranda
 */
public interface ProjectRepository extends Repository<Project, String> {

	@Modifying
	@Query("UPDATE Project SET version = :version WHERE id = :id")
	public void updateVersion(
		@Param("id") String id, @Param("version") String version);

}