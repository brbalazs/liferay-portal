/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.batch.curator.spring.scheduling;

import com.liferay.osb.asah.common.spring.scheduling.BaseSchedulingConfigurer;

import org.springframework.context.annotation.Configuration;

/**
 * @author Vishal Reddy
 */
@Configuration
public class OSBAsahBatchCuratorSchedulingConfigurer
	extends BaseSchedulingConfigurer {

	public OSBAsahBatchCuratorSchedulingConfigurer() {
		super(13);
	}

}