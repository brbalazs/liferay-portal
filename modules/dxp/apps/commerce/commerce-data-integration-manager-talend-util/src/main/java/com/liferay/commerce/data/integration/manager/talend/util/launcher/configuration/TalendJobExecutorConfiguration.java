/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.commerce.data.integration.manager.talend.util.launcher.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author guywandji
 */
@ExtendedObjectClassDefinition(
	category = "data-integration",
	scope = ExtendedObjectClassDefinition.Scope.SYSTEM
)
@Meta.OCD(
	id = "com.liferay.commerce.data.integration.manager.talend.util.launcher.configuration.TalendJobExecutorConfiguration",
	localization = "content/Language",
	name = "talend-job-launcher-configuration-name"
)
public interface TalendJobExecutorConfiguration {

	@Meta.AD(deflt = "-Xms256M", name = "xms-arg", required = false)
	public String xms();

	@Meta.AD(deflt = "-Xmx1024M", name = "xmx-arg", required = false)
	public String xmx();

	@Meta.AD(deflt = "/lib", name = "lib-folder-name", required = false)
	public String libFolderName();

	@Meta.AD(deflt = "\"$@\"", name = "end-command-string", required = false)
	public String endCommandChar();

	@Meta.AD(deflt = "Default", name = "context", required = false)
	public String context();

}