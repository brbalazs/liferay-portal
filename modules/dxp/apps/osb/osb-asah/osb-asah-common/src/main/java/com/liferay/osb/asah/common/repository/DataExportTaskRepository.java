/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository;

import com.liferay.osb.asah.common.entity.DataExportTask;

import java.util.Date;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;

/**
 * @author Inácio Nery
 */
public interface DataExportTaskRepository
	extends Repository<DataExportTask, Long> {

	@Cacheable
	public List<DataExportTask> findByStatus(DataExportTask.Status status);

	@Cacheable
	public DataExportTask findFirstByFromDateAndToDateAndTypeOrderByIdDesc(
		Date fromDate, Date toDate, DataExportTask.Type type);

	@Cacheable
	public DataExportTask findFirstByTypeOrderByIdDesc(
		DataExportTask.Type type);

}