/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.fabric.status;

import com.liferay.portal.kernel.test.rule.CodeCoverageAssertor;

import java.lang.management.PlatformManagedObject;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * @author Shuyang Zhou
 */
public class RemoteFabricStatusTest extends BaseFabricStatusTestCase {

	@ClassRule
	public static final CodeCoverageAssertor codeCoverageAssertor =
		CodeCoverageAssertor.INSTANCE;

	@Test
	public void testGetPlatformMXBeansIllegalMXBeanClass() {
		try {
			RemoteFabricStatus.getPlatformMXBeans(
				PlatformManagedObject.class,
				LocalFabricStatus.processCallableExecutor);

			Assert.fail();
		}
		catch (IllegalArgumentException iae) {
		}
	}

	@Test
	public void testObjectNames() {
		doTestObjectNames(
			new RemoteFabricStatus(LocalFabricStatus.processCallableExecutor));
	}

}