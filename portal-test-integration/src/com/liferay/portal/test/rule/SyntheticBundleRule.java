/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.test.rule;

import com.liferay.portal.kernel.test.rule.BaseTestRule;
import com.liferay.portal.test.rule.callback.SyntheticBundleTestCallback;

/**
 * Creates and installs a bundle from the named sub-package of a test class.
 *
 * <p>
 * For example, if the test class is <code>bar.FooTest</code>, invoking
 * <code>new SyntheticBundleRule("fee")</code> on <code>FooTest</code> creates a
 * bundle of the contents of the <code>bar.fee</code> package. This sub-package
 * should also contain a <code>bnd.bnd</code> file that describes the bundle.
 * When writing the bnd file, you can use the <code>${bundle.package}</code>
 * property to simplify the contents of the file.
 * </p>
 *
 * @author Raymond Augé
 */
public class SyntheticBundleRule extends BaseTestRule<Long, Long> {

	public SyntheticBundleRule(String bundlePackage) {
		super(new SyntheticBundleTestCallback(bundlePackage));
	}

}