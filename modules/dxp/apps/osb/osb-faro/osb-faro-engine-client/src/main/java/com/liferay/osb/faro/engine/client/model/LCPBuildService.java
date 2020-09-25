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

package com.liferay.osb.faro.engine.client.model;

import java.util.Date;

/**
 * @author Matthew Kong
 */
public class LCPBuildService {

	public String getBranch() {
		return _branch;
	}

	public String getBuildGroupUid() {
		return _buildGroupUid;
	}

	public Date getCreatedAt() {
		return _createdAt;
	}

	public String getGroupUid() {
		return _groupUid;
	}

	public String getImage() {
		return _image;
	}

	public String getProjectId() {
		return _projectId;
	}

	public String getProjectUid() {
		return _projectUid;
	}

	public String getServiceId() {
		return _serviceId;
	}

	public String getStatus() {
		return _status;
	}

	public void setBranch(String branch) {
		_branch = branch;
	}

	public void setBuildGroupUid(String buildGroupUid) {
		_buildGroupUid = buildGroupUid;
	}

	public void setCreatedAt(Date createdAt) {
		_createdAt = createdAt;
	}

	public void setGroupUid(String groupUid) {
		_groupUid = groupUid;
	}

	public void setImage(String image) {
		_image = image;
	}

	public void setProjectId(String projectId) {
		_projectId = projectId;
	}

	public void setProjectUid(String projectUid) {
		_projectUid = projectUid;
	}

	public void setServiceId(String serviceId) {
		_serviceId = serviceId;
	}

	public void setStatus(String status) {
		_status = status;
	}

	private String _branch;
	private String _buildGroupUid;
	private Date _createdAt;
	private String _groupUid;
	private String _image;
	private String _projectId;
	private String _projectUid;
	private String _serviceId;
	private String _status;

}