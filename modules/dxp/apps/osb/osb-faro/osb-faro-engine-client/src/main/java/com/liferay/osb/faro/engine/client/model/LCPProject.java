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
		Cluster cluster = Cluster.fromString(_cluster);

		if (cluster != null) {
			return cluster.getProjectId();
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

		EU("ac-europe", "europe-west2-c1"),
		EU2("ac-europe2", "europe-west3-c1"),
		SA("ac-southamerica", "southamerica-east1-c1"),
		US("ac-us", "us-west1-c1");

		public static Cluster fromString(String value) {
			if (StringUtil.equals(value, Cluster.EU._value)) {
				return Cluster.EU;
			}
			else if (StringUtil.equals(value, Cluster.EU2._value)) {
				return Cluster.EU2;
			}
			else if (StringUtil.equals(value, Cluster.SA._value)) {
				return Cluster.SA;
			}
			else if (StringUtil.equals(value, Cluster.US._value)) {
				return Cluster.US;
			}

			return null;
		}

		public String getBaseURL() {
			return _baseURL;
		}

		public String getProjectId() {
			return _projectId;
		}

		@Override
		public String toString() {
			return _value;
		}

		private Cluster(String projectId, String value) {
			_projectId = projectId;
			_value = value;

			_baseURL = String.format(
				"https://%s-{service}.lfr.cloud/", projectId);
		}

		private final String _baseURL;
		private final String _projectId;
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