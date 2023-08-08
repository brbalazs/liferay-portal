/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.spring.cache.test;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * @author Inácio Nery
 */
@Component
public class Bar {

	@Cacheable
	public Integer getIncrementCount() {
		return _count++;
	}

	private int _count;

}