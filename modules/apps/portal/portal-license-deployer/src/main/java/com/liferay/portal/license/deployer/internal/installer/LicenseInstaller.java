/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.license.deployer.internal.installer;

import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.license.util.LicenseManagerUtil;
import com.liferay.portal.kernel.module.framework.ModuleServiceLifecycle;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.SAXReaderUtil;

import java.io.File;

import org.apache.felix.fileinstall.ArtifactInstaller;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Amos Fong
 */
@Component(immediate = true, service = ArtifactInstaller.class)
public class LicenseInstaller implements ArtifactInstaller {

	@Override
	public boolean canHandle(File artifact) {
		String extension = FileUtil.getExtension(artifact.getName());

		if (!extension.equals("xml")) {
			return false;
		}

		try {
			String content = FileUtil.read(artifact);

			Document document = SAXReaderUtil.read(content);

			Element rootElement = document.getRootElement();

			String rootElementName = rootElement.getName();

			if (rootElementName.equals("license") ||
				rootElementName.equals("licenses")) {

				return true;
			}
		}
		catch (Exception e) {
		}

		return false;
	}

	@Override
	public void install(File file) throws Exception {
		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		String content = FileUtil.read(file);

		jsonObject.put("licenseXML", content);

		LicenseManagerUtil.registerLicense(jsonObject);
	}

	@Override
	public void uninstall(File file) throws Exception {
	}

	@Override
	public void update(File file) throws Exception {
	}

	@Reference(target = ModuleServiceLifecycle.PORTAL_INITIALIZED)
	private ModuleServiceLifecycle _moduleServiceLifecycle;

}