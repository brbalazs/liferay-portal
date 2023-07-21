/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.store.cmis.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Manuel de la Peña
 */
@ExtendedObjectClassDefinition(category = "file-storage")
@Meta.OCD(
	id = "com.liferay.portal.store.cmis.configuration.CMISStoreConfiguration",
	localization = "content/Language", name = "cmis-store-configuration-name"
)
public interface CMISStoreConfiguration {

	@Meta.AD(
		deflt = "http://localhost:8080/alfresco/service/api/cmis",
		name = "repository-url"
	)
	public String repositoryUrl();

	@Meta.AD(deflt = "none", name = "credentials-username")
	public String credentialsUsername();

	@Meta.AD(deflt = "none", name = "credentials-password")
	public String credentialsPassword();

	@Meta.AD(deflt = "Liferay Home", name = "system-root-dir")
	public String systemRootDir();

}