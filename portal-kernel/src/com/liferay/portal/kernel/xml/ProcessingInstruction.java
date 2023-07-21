/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.xml;

import aQute.bnd.annotation.ProviderType;

import java.util.Map;

/**
 * @author Brian Wing Shun Chan
 */
@ProviderType
public interface ProcessingInstruction extends Node {

	public String getTarget();

	@Override
	public String getText();

	public String getValue(String name);

	public Map<String, String> getValues();

	public boolean removeValue(String name);

	public void setTarget(String target);

	public void setValue(String name, String value);

	public void setValues(Map<String, String> data);

}