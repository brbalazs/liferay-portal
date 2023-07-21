/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.contacts.demo.internal.data.creator;

import com.liferay.osb.faro.engine.client.ContactsEngineClient;
import com.liferay.osb.faro.model.FaroProject;
import com.liferay.petra.string.StringPool;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Matthew Kong
 */
public class LiferayOrganizationsDataCreator extends DataCreator {

	public LiferayOrganizationsDataCreator(
		ContactsEngineClient contactsEngineClient, FaroProject faroProject,
		String dataSourceId) {

		super(
			contactsEngineClient, faroProject, "osbasahdxpraw",
			"organizations");

		_dataSourceId = dataSourceId;

		_faroInfoOrganizationsDataCreator =
			new FaroInfoOrganizationsDataCreator(
				contactsEngineClient, faroProject);
	}

	@Override
	public void execute() {
		super.execute();

		for (Object object : getObjects()) {
			_faroInfoOrganizationsDataCreator.create(new Object[] {object});
		}

		_faroInfoOrganizationsDataCreator.execute();
	}

	@Override
	public String getClassName() {
		return "com.liferay.portal.kernel.model.Organization";
	}

	@Override
	public String getClassPKFieldName() {
		return "organizationId";
	}

	@Override
	protected Map<String, Object> doCreate(Object[] params) {
		Map<String, Object> organization = new HashMap<>();

		organization.put("id", number.randomNumber(8, false));
		organization.put("modifiedDate", formatDate(new Date()));

		String name = (String)params[0];

		organization.put("name", name);

		Map<String, Object> parentOrganization = (Map<String, Object>)params[1];

		organization.put(
			"nameTreePath", _getNameTreePath(name, parentOrganization));

		organization.put("organizationId", number.randomNumber(8, false));
		organization.put("osbAsahDataSourceId", _dataSourceId);
		organization.put(
			"parentName",
			parentOrganization.getOrDefault("name", StringPool.BLANK));
		organization.put(
			"parentOrganizationId",
			parentOrganization.getOrDefault(
				"organizationId", StringPool.BLANK));
		organization.put("type", "organization");

		return organization;
	}

	private String _getNameTreePath(
		String name, Map<String, Object> parentOrganization) {

		if (parentOrganization.isEmpty()) {
			return name;
		}

		return parentOrganization.get("nameTreePath") + " > " + name;
	}

	private final String _dataSourceId;
	private final FaroInfoOrganizationsDataCreator
		_faroInfoOrganizationsDataCreator;

}