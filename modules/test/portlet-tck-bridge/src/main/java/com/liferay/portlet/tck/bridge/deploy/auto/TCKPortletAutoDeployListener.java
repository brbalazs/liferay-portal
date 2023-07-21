/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portlet.tck.bridge.deploy.auto;

import com.liferay.portal.deploy.auto.PortletAutoDeployListener;
import com.liferay.portal.kernel.deploy.auto.AutoDeployException;
import com.liferay.portal.kernel.deploy.auto.AutoDeployListener;

import java.io.File;

import org.osgi.service.component.annotations.Component;

/**
 * @author Miguel Pastor
 */
@Component(immediate = true, service = AutoDeployListener.class)
public class TCKPortletAutoDeployListener extends PortletAutoDeployListener {

	@Override
	protected String getSuccessMessage(File file) {
		return "TCK portlets for " + file.getPath() + " copied successfully";
	}

	@Override
	protected boolean isDeployable(File file) throws AutoDeployException {
		String fileName = file.getName();

		if (super.isDeployable(file) && fileName.startsWith("portlet_jp_")) {
			return true;
		}

		return false;
	}

}