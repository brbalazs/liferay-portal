/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.struts;

import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portlet.PortletURLImplWrapper;

import java.util.Map;

/**
 * @author Brian Wing Shun Chan
 */
public class StrutsActionPortletURL extends PortletURLImplWrapper {

	public StrutsActionPortletURL(
		LiferayPortletResponse liferayPortletResponse, long plid,
		String lifecycle) {

		super(liferayPortletResponse, plid, lifecycle);

		_portlet = liferayPortletResponse.getPortlet();

		_strutsPath =
			StringPool.SLASH + _portlet.getStrutsPath() + StringPool.SLASH;
	}

	@Override
	public void setParameter(String name, String value) {
		if (name.equals("struts_action") && !value.startsWith(_strutsPath)) {
			int pos = value.lastIndexOf(CharPool.SLASH);

			value = _strutsPath + value.substring(pos + 1);
		}

		super.setParameter(name, value);
	}

	@Override
	public void setParameters(Map<String, String[]> params) {
		for (Map.Entry<String, String[]> entry : params.entrySet()) {
			String name = entry.getKey();

			if (name.equals("struts_action")) {
				String[] values = entry.getValue();

				for (int i = 0; i < values.length; i++) {
					String value = values[i];

					if (!value.startsWith(_strutsPath)) {
						int pos = value.lastIndexOf(CharPool.SLASH);

						value = _strutsPath + value.substring(pos + 1);

						values[i] = value;
					}
				}
			}
		}

		super.setParameters(params);
	}

	private final Portlet _portlet;
	private final String _strutsPath;

}