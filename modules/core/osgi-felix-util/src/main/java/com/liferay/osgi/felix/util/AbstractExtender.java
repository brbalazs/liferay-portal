/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osgi.felix.util;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;

/**
 * @author Shuyang Zhou
 */
public abstract class AbstractExtender
	extends org.apache.felix.utils.extender.AbstractExtender {

	public AbstractExtender() {
		setSynchronous(true);
	}

	@Override
	public void bundleChanged(BundleEvent event) {
		synchronized (this) {
			if (_stopped) {
				return;
			}

			if (event.getType() == BundleEvent.STOPPING) {
				BundleContext bundleContext = getBundleContext();

				Bundle bundle = event.getBundle();

				if (bundleContext == bundle.getBundleContext()) {
					bundleContext.removeBundleListener(this);

					_stopped = true;
				}
			}

			super.bundleChanged(event);
		}
	}

	@Override
	public final void setSynchronous(boolean synchronous) {
		super.setSynchronous(synchronous);
	}

	private boolean _stopped;

}