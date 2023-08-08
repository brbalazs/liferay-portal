/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dto;

import com.liferay.osb.asah.common.entity.BQOrganization;
import com.liferay.osb.asah.common.graphql.GraphQLType;
import com.liferay.osb.asah.common.util.StringUtil;

/**
 * @author Marcos Martins
 */
@GraphQLType
public class DXPOrganizationDTO extends DXPEntityDTO {

	public DXPOrganizationDTO(BQOrganization bqOrganization) {
		super(bqOrganization);

		_organizationPK = StringUtil.get(
			bqOrganization.getOrganizationId(), null);
		_parentName = bqOrganization.getParentOrganizationName();
		_parentOrganizationPK = StringUtil.get(
			bqOrganization.getParentOrganizationId(), null);
		_treePath = bqOrganization.getTreePath();
		_type = StringUtil.get(bqOrganization.getType());
	}

	public String getOrganizationPK() {
		return _organizationPK;
	}

	public String getParentName() {
		return _parentName;
	}

	public String getParentOrganizationPK() {
		return _parentOrganizationPK;
	}

	public String getTreePath() {
		return _treePath;
	}

	public String getType() {
		return _type;
	}

	private final String _organizationPK;
	private final String _parentName;
	private final String _parentOrganizationPK;
	private final String _treePath;
	private final String _type;

}