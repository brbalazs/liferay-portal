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
public class LiferayGroupsDataCreator extends DataCreator {

	public LiferayGroupsDataCreator(
		ContactsEngineClient contactsEngineClient, FaroProject faroProject,
		String dataSourceId) {

		super(contactsEngineClient, faroProject, "osbasahdxpraw", "groups");

		_dataSourceId = dataSourceId;
	}

	@Override
	public String getClassName() {
		return "com.liferay.portal.kernel.model.Group";
	}

	@Override
	public String getClassPKFieldName() {
		return "groupId";
	}

	@Override
	protected Map<String, Object> doCreate(Object[] params) {
		Map<String, Object> group = new HashMap<>();

		group.put("dataSourceId", _dataSourceId);
		group.put(
			"fields",
			new HashMap<String, Object>() {
				{
					put("groupId", number.randomNumber(8, false));
					put("name", pokemon.name());
				}
			});

		group.put("id", number.randomNumber(8, false));

		return group;
	}

	private final String _dataSourceId;

}