/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.workflow.kaleo.designer.web.internal.util;

import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.util.PredicateFilter;
import com.liferay.portal.workflow.kaleo.designer.web.internal.permission.KaleoDefinitionVersionPermission;
import com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion;

/**
 * @author Lino Alves
 */
public class KaleoDefinitionVersionViewPermissionPredicateFilter
	implements PredicateFilter<KaleoDefinitionVersion> {

	public KaleoDefinitionVersionViewPermissionPredicateFilter(
		PermissionChecker permissionChecker, long companyGroupId) {

		_permissionChecker = permissionChecker;
		_companyGroupId = companyGroupId;
	}

	@Override
	public boolean filter(KaleoDefinitionVersion kaleoDefinitionVersion) {
		return KaleoDefinitionVersionPermission.hasViewPermission(
			_permissionChecker, kaleoDefinitionVersion, _companyGroupId);
	}

	private final long _companyGroupId;
	private final PermissionChecker _permissionChecker;

}