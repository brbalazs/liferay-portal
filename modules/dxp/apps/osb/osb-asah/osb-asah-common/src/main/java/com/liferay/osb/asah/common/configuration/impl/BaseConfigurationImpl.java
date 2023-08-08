/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.configuration.impl;

import com.liferay.osb.asah.common.configuration.Configuration;

import java.util.Objects;

/**
 * @author Brian Wing Shun Chan
 */
public abstract class BaseConfigurationImpl implements Configuration {

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof BaseConfigurationImpl)) {
			return false;
		}

		BaseConfigurationImpl baseConfigurationImpl =
			(BaseConfigurationImpl)obj;

		if (Objects.equals(
				_dataSourceId, baseConfigurationImpl._dataSourceId) &&
			Objects.equals(
				_dataSourceState, baseConfigurationImpl._dataSourceState) &&
			Objects.equals(
				_dataSourceStatus, baseConfigurationImpl._dataSourceStatus) &&
			Objects.equals(_projectId, baseConfigurationImpl._projectId)) {

			return true;
		}

		return false;
	}

	@Override
	public String getDataSourceId() {
		return _dataSourceId;
	}

	@Override
	public String getDataSourceState() {
		return _dataSourceState;
	}

	@Override
	public String getDataSourceStatus() {
		return _dataSourceStatus;
	}

	@Override
	public String getProjectId() {
		return _projectId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			_dataSourceId, _dataSourceState, _dataSourceStatus, _projectId);
	}

	@Override
	public void setDataSourceId(String dataSourceId) {
		_dataSourceId = dataSourceId;
	}

	@Override
	public void setDataSourceState(String dataSourceState) {
		_dataSourceState = dataSourceState;
	}

	@Override
	public void setDataSourceStatus(String dataSourceStatus) {
		_dataSourceStatus = dataSourceStatus;
	}

	@Override
	public void setProjectId(String projectId) {
		_projectId = projectId;
	}

	private String _dataSourceId;
	private String _dataSourceState;
	private String _dataSourceStatus;
	private String _projectId;

}