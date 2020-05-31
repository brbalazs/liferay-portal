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

import com.liferay.portal.kernel.util.StringUtil;

import java.util.Date;

/**
 * @author Matthew Kong
 */
public class LCPProject {

	public long getBuildGroupUidCounter() {
		return _buildGroupUidCounter;
	}

	public String getCluster() {
		return _cluster;
	}

	public Date getCreatedAt() {
		return _createdAt;
	}

	public String getESProjectId() {
		if (StringUtil.equals(_cluster, Cluster.EU.toString())) {
			return "ac-europe";
		}
		else if (StringUtil.equals(_cluster, Cluster.EU2.toString())) {
			return "ac-europe2";
		}
		else if (StringUtil.equals(_cluster, Cluster.SA.toString())) {
			return "ac-southamerica";
		}
		else if (StringUtil.equals(_cluster, Cluster.US.toString())) {
			return "ac-us";
		}

		return null;
	}

	public String getHealth() {
		return _health;
	}

	public String getId() {
		return _id;
	}

	public String getLoadBalancerIp() {
		return _loadBalancerIp;
	}

	public String getOwnerId() {
		return _ownerId;
	}

	public String getProjectId() {
		return _projectId;
	}

	public String getStatus() {
		return _status;
	}

	public void setBuildGroupUidCounter(long buildGroupUidCounter) {
		_buildGroupUidCounter = buildGroupUidCounter;
	}

	public void setCluster(String cluster) {
		_cluster = cluster;
	}

	public void setCreatedAt(Date createdAt) {
		_createdAt = createdAt;
	}

	public void setHealth(String health) {
		_health = health;
	}

	public void setId(String id) {
		_id = id;
	}

	public void setLoadBalancerIp(String loadBalancerIp) {
		_loadBalancerIp = loadBalancerIp;
	}

	public void setOwnerId(String ownerId) {
		_ownerId = ownerId;
	}

	public void setProjectId(String projectId) {
		_projectId = projectId;
	}

	public void setStatus(String status) {
		_status = status;
	}

	public enum Cluster {

		EU("europe-west2-c1"), EU2("europe-west3-c1"),
		SA("southamerica-east1-c1"), US("us-west1-c1");

		@Override
		public String toString() {
			return _value;
		}

		private Cluster(String value) {
			_value = value;
		}

		private final String _value;

	}

	private long _buildGroupUidCounter;
	private String _cluster;
	private Date _createdAt;
	private String _health;
	private String _id;
	private String _loadBalancerIp;
	private String _ownerId;
	private String _projectId;
	private String _status;

}