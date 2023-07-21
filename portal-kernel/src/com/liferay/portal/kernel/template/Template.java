/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.template;

import aQute.bnd.annotation.ProviderType;

import java.io.Writer;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Tina Tian
 */
@ProviderType
public interface Template extends Map<String, Object> {

	public void doProcessTemplate(Writer writer) throws Exception;

	public Object get(String key);

	public String[] getKeys();

	public void prepare(HttpServletRequest request);

	public void prepareTaglib(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse);

	public void processTemplate(Writer writer) throws TemplateException;

}