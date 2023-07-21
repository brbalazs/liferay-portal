/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.template.velocity.internal;

import com.liferay.portal.kernel.template.TemplateConstants;
import com.liferay.portal.template.ClassLoaderResourceParser;
import com.liferay.portal.template.TemplateResourceParser;

import org.osgi.service.component.annotations.Component;

/**
 * @author Raymond Augé
 */
@Component(
	immediate = true, property = "lang.type=" + TemplateConstants.LANG_TYPE_VM,
	service = TemplateResourceParser.class
)
public class VelocityBundleResourceParser extends ClassLoaderResourceParser {
}