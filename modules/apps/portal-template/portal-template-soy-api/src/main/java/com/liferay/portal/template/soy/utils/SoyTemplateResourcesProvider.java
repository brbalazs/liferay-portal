/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.template.soy.utils;

import com.liferay.portal.kernel.template.TemplateResource;

import java.util.List;

import org.osgi.framework.Bundle;

/**
 * @author Miguel Pastor
 * @deprecated As of Mueller (7.2.x), see {@link
 *             com.liferay.portal.template.soy.util.SoyTemplateResourcesProvider}
 */
@Deprecated
public interface SoyTemplateResourcesProvider {

	public List<TemplateResource> getAllTemplateResources();

	public List<TemplateResource> getBundleTemplateResources(
		Bundle bundle, String templatePath);

}