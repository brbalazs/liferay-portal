/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.contacts.demo.internal.data.creator;

import com.liferay.osb.faro.engine.client.ContactsEngineClient;
import com.liferay.osb.faro.model.FaroProject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Matthew Kong
 */
public class FaroInfoOrganizationsDataCreator extends DataCreator {

	public FaroInfoOrganizationsDataCreator(
		ContactsEngineClient contactsEngineClient, FaroProject faroProject) {

		super(
			contactsEngineClient, faroProject, "osbasahfaroinfo",
			"organizations");
	}

	@Override
	protected Map<String, Object> doCreate(Object[] params) {
		Map<String, Object> faroInfoOrganization = new HashMap<>();

		Map<String, Object> organization = (Map<String, Object>)params[0];

		faroInfoOrganization.put(
			"dataSourceId", organization.get("osbAsahDataSourceId"));

		faroInfoOrganization.put("dateCreated", formatDate(new Date()));
		faroInfoOrganization.put(
			"dateModified", organization.get("modifiedDate"));
		faroInfoOrganization.put("id", number.randomNumber(8, false));
		faroInfoOrganization.put("name", organization.get("name"));
		faroInfoOrganization.put(
			"nameTreePath", organization.get("nameTreePath"));
		faroInfoOrganization.put(
			"organizationPK", organization.get("organizationId"));
		faroInfoOrganization.put("parentName", organization.get("parentName"));
		faroInfoOrganization.put(
			"parentOrganizationPK", organization.get("parentOrganizationId"));
		faroInfoOrganization.put("type", organization.get("type"));

		return faroInfoOrganization;
	}

}