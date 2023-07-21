/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.frontend.js.loader.modules.extender.npm;

import org.osgi.framework.Bundle;

/**
 * This is an interface for registering OSGi services that get notified whenever
 * the {@link NPMRegistry} is updated.
 *
 * @author Iván Zaera
 * @review
 */
public interface JSBundleTracker {

	public void addedJSBundle(
		JSBundle jsBundle, Bundle bundle, NPMRegistry npmRegistry);

	public void removedJSBundle(
		JSBundle jsBundle, Bundle bundle, NPMRegistry npmRegistry);

}