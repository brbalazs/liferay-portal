/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import com.liferay.osb.asah.common.entity.Project;

/**
 * @author Marcellus Tavares
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectDTO {

	public ProjectDTO() {
	}

	public ProjectDTO(Project project) {
		_id = project.getId();
		_version = project.getVersion();
	}

	public String getId() {
		return _id;
	}

	public String getVersion() {
		return _version;
	}

	public void setId(String id) {
		_id = id;
	}

	public void setVersion(String version) {
		_version = version;
	}

	private String _id;
	private String _version;

}