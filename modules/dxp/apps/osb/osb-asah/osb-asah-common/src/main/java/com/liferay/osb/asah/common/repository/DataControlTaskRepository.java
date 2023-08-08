/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository;

import com.liferay.osb.asah.common.entity.DataControlTask;

import java.util.List;

/**
 * @author Marcellus Tavares
 */
public interface DataControlTaskRepository
	extends CustomDataControlTaskRepository, Repository<DataControlTask, Long> {

	public Boolean existsByBatchIdAndStatusIn(
		Long batchId, List<String> status);

	public Boolean existsByEmailAddressAndStatusAndType(
		String emailAddress, String status, String type);

	public DataControlTask findByIdAndStatus(Long id, String status);

}