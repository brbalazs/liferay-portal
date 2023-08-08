/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.test.util.spring;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

/**
 * @author Alejo Ceballos
 */
public abstract class BaseOSBAsahTestExecutionListener
	extends AbstractTestExecutionListener {

	@Override
	public void beforeTestClass(TestContext testContext) throws Exception {
		ApplicationContext applicationContext =
			testContext.getApplicationContext();

		AutowireCapableBeanFactory autowireCapableBeanFactory =
			applicationContext.getAutowireCapableBeanFactory();

		autowireCapableBeanFactory.autowireBean(this);
	}

}