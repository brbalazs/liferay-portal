/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.batch.curator.bot.scheduling;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.spring.annotation.ConditionalOnGoogleApplicationCredentials;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 */
@Component
@ConditionalOnGoogleApplicationCredentials
@Profile("!test")
public class DXPBatchEntitiesScheduler {

	@Scheduled(fixedDelay = DateUtil.MINUTE * 1)
	public void runDXPBatchEntitiesNanite() {
		_asahTaskManager.runNanitesForAllProjects("DXPBatchEntitiesNanite");
	}

	@Autowired
	private AsahTaskManager _asahTaskManager;

}