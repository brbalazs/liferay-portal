/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.upgrade.v4_3_1;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.dog.AsahTaskDog;
import com.liferay.osb.asah.common.entity.AsahTask;
import com.liferay.osb.asah.common.repository.AsahTaskRepository;
import com.liferay.osb.asah.upgrade.UpgradeStep;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Leslie Wong
 */
@Component
public class DeleteChannelsAsahTaskUpgradeStep implements UpgradeStep {

	@Override
	public void upgrade(String version) throws Exception {
		List<AsahTask> asahTasks = _asahTaskDog.getAsahTasks(
			"DeleteChannelsNanite");

		List<AsahTask> modifiedAsahTasks = new ArrayList<>();

		for (AsahTask asahTask : asahTasks) {
			JSONObject contextJSONObject = asahTask.getContextJSONObject();

			if (!contextJSONObject.has("createDate")) {
				contextJSONObject.put("createDate", DateUtil.newDayDate());

				modifiedAsahTasks.add(asahTask);
			}
		}

		if (!modifiedAsahTasks.isEmpty()) {
			_asahTaskRepository.saveAll(modifiedAsahTasks);
		}
	}

	@Autowired
	private AsahTaskDog _asahTaskDog;

	@Autowired
	private AsahTaskRepository _asahTaskRepository;

}