/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.test.rule.callback;

import com.liferay.portal.kernel.test.randomizerbumpers.UniqueStringRandomizerBumper;
import com.liferay.portal.kernel.test.rule.callback.BaseTestCallback;

import org.junit.runner.Description;

/**
 * @author Shuyang Zhou
 */
public class UniqueStringRandomizerBumperTestCallback
	extends BaseTestCallback<Object, Object> {

	public static final UniqueStringRandomizerBumperTestCallback INSTANCE =
		new UniqueStringRandomizerBumperTestCallback();

	@Override
	public Object beforeClass(Description description) {
		UniqueStringRandomizerBumper.reset();

		return null;
	}

	private UniqueStringRandomizerBumperTestCallback() {
	}

}