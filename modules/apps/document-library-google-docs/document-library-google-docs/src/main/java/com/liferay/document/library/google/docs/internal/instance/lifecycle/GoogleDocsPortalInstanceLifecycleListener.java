/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.document.library.google.docs.internal.instance.lifecycle;

import com.liferay.document.library.google.docs.internal.util.GoogleDocsDLFileEntryTypeHelper;
import com.liferay.document.library.kernel.service.DLFileEntryTypeLocalService;
import com.liferay.dynamic.data.mapping.io.DDMFormXSDDeserializer;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.dynamic.data.mapping.service.DDMStructureVersionLocalService;
import com.liferay.dynamic.data.mapping.util.DDM;
import com.liferay.dynamic.data.mapping.util.DDMStructurePermissionSupport;
import com.liferay.portal.instance.lifecycle.BasePortalInstanceLifecycleListener;
import com.liferay.portal.instance.lifecycle.PortalInstanceLifecycleListener;
import com.liferay.portal.kernel.exception.ModelListenerException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.service.UserLocalService;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Adolfo Pérez
 */
@Component(immediate = true, service = PortalInstanceLifecycleListener.class)
public class GoogleDocsPortalInstanceLifecycleListener
	extends BasePortalInstanceLifecycleListener {

	@Override
	public void portalInstanceRegistered(Company company) throws Exception {
		try {
			GoogleDocsDLFileEntryTypeHelper googleDocsDLFileEntryTypeHelper =
				new GoogleDocsDLFileEntryTypeHelper(
					company, _classNameLocalService, _ddm,
					_ddmFormXSDDeserializer, _ddmStructureLocalService,
					_dlFileEntryTypeLocalService, _userLocalService);

			googleDocsDLFileEntryTypeHelper.addGoogleDocsDLFileEntryType();
		}
		catch (PortalException pe) {
			throw new ModelListenerException(pe);
		}
	}

	@Reference(
		target = "(model.class.name=com.liferay.document.library.kernel.model.DLFileEntryMetadata)",
		unbind = "-"
	)
	protected void setDDMStructurePermissionSupport(
		DDMStructurePermissionSupport ddmStructurePermissionSupport) {
	}

	@Reference(unbind = "-")
	protected void setDDMStructureVersionLocalService(
		DDMStructureVersionLocalService ddmStructureVersionLocalService) {
	}

	@Reference
	private ClassNameLocalService _classNameLocalService;

	@Reference
	private DDM _ddm;

	@Reference
	private DDMFormXSDDeserializer _ddmFormXSDDeserializer;

	@Reference
	private DDMStructureLocalService _ddmStructureLocalService;

	@Reference
	private DLFileEntryTypeLocalService _dlFileEntryTypeLocalService;

	@Reference
	private UserLocalService _userLocalService;

}