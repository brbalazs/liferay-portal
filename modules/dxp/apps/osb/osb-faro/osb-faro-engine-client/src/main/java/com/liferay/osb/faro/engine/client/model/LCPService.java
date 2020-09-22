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
public class LCPService {

	public Date getCreatedAt() {
		return _createdAt;
	}

	public String getDeployGroupUid() {
		return _deployGroupUid;
	}

	public String getGroupUid() {
		return _groupUid;
	}

	public String getHealth() {
		return _health;
	}

	public String getId() {
		return _id;
	}

	public String getImageHint() {
		return _imageHint;
	}

	public LoadBalancer getLoadBalancer() {
		return _loadBalancer;
	}

	public String getProjectId() {
		return _projectId;
	}

	public String getServiceId() {
		return _serviceId;
	}

	public boolean isReady() {
		return _ready;
	}

	public void setCreatedAt(Date createdAt) {
		_createdAt = createdAt;
	}

	public void setDeployGroupUid(String deployGroupUid) {
		_deployGroupUid = deployGroupUid;
	}

	public void setGroupUid(String groupUid) {
		_groupUid = groupUid;
	}

	public void setHealth(String health) {
		_health = health;
	}

	public void setId(String id) {
		_id = id;
	}

	public void setImageHint(String imageHint) {
		_imageHint = imageHint;
	}

	public void setLoadBalancer(LoadBalancer loadBalancer) {
		_loadBalancer = loadBalancer;
	}

	public void setProjectId(String projectId) {
		_projectId = projectId;
	}

	public void setReady(boolean ready) {
		_ready = ready;
	}

	public void setServiceId(String serviceId) {
		_serviceId = serviceId;
	}

	public class LoadBalancer {

		public String getTargetPort() {
			return _targetPort;
		}

		public void setTargetPort(String targetPort) {
			_targetPort = targetPort;
		}

		private String _targetPort;

	}

	private Date _createdAt;
	private String _deployGroupUid;
	private String _groupUid;
	private String _health;
	private String _id;
	private String _imageHint;
	private LoadBalancer _loadBalancer;
	private String _projectId;
	private boolean _ready;
	private String _serviceId;

}