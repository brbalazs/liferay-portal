/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.contacts.demo.internal.data.creator;

import com.liferay.osb.faro.engine.client.ContactsEngineClient;
import com.liferay.osb.faro.model.FaroProject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Matthew Kong
 */
public class LiferayRolesDataCreator extends DataCreator {

	public LiferayRolesDataCreator(
		ContactsEngineClient contactsEngineClient, FaroProject faroProject,
		String dataSourceId) {

		super(contactsEngineClient, faroProject, "osbasahdxpraw", "roles");

		_dataSourceId = dataSourceId;
	}

	@Override
	public String getClassName() {
		return "com.liferay.portal.kernel.model.Role";
	}

	@Override
	public String getClassPKFieldName() {
		return "roleId";
	}

	@Override
	protected Map<String, Object> doCreate(Object[] params) {
		Map<String, Object> role = new HashMap<>();

		role.put("dataSourceId", _dataSourceId);
		role.put(
			"fields",
			new HashMap<String, Object>() {
				{
					put("name", job.position());
					put("roleId", number.randomNumber(8, false));
				}
			});

		role.put("id", number.randomNumber(8, false));

		return role;
	}

	private final String _dataSourceId;

}