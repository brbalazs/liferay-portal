/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.template.bundle.templatemanagerutil;

import com.liferay.portal.kernel.template.TemplateResource;

import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Reader;

/**
 * @author Philip Jones
 */
public class TestTemplateResource implements TemplateResource {

	public static final String TEST_TEMPLATE_RESOURCE_TEMPLATE_ID =
		"TEST_TEMPLATE_RESOURCE_TEMPLATE_ID";

	@Override
	public long getLastModified() {
		return 0;
	}

	@Override
	public Reader getReader() {
		return null;
	}

	@Override
	public String getTemplateId() {
		return TEST_TEMPLATE_RESOURCE_TEMPLATE_ID;
	}

	@Override
	public void readExternal(ObjectInput objectInput) {
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput) {
	}

}