/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.entity;

import com.liferay.osb.asah.common.spring.annotation.BigQueryColumn;
import com.liferay.osb.asah.common.util.BeanUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Marcellus Tavares
 */
public class BQMembershipIndividual {

	public BQMembershipIndividual() {
	}

	public BQMembershipIndividual(Map<String, Object> source) {
		BeanUtils.copyProperties(source, this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof BQMembershipIndividual)) {
			return false;
		}

		BQMembershipIndividual bqMembershipIndividual =
			(BQMembershipIndividual)obj;

		if (Objects.equals(
				_dataSourceIdentities,
				bqMembershipIndividual._dataSourceIdentities) &&
			Objects.equals(
				_dataSourceUUIDs, bqMembershipIndividual._dataSourceUUIDs) &&
			Objects.equals(
				_individualId, bqMembershipIndividual._individualId) &&
			Objects.equals(
				_modifiedDate, bqMembershipIndividual._modifiedDate) &&
			Objects.equals(_segmentId, bqMembershipIndividual._segmentId)) {

			return true;
		}

		return false;
	}

	@BigQueryColumn
	public List<DataSourceIdentity> getDataSourceIdentities() {
		return _dataSourceIdentities;
	}

	@BigQueryColumn
	public List<DataSourceUUID> getDataSourceUUIDs() {
		return _dataSourceUUIDs;
	}

	@BigQueryColumn
	public String getIndividualId() {
		return _individualId;
	}

	@BigQueryColumn
	public Date getModifiedDate() {
		if (_modifiedDate == null) {
			return null;
		}

		return new Date(_modifiedDate.getTime());
	}

	@BigQueryColumn
	public Long getSegmentId() {
		return _segmentId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_individualId, _modifiedDate, _segmentId);
	}

	public void setDataSourceIdentities(
		List<DataSourceIdentity> dataSourceIdentities) {

		_dataSourceIdentities = dataSourceIdentities;
	}

	public void setDataSourceUUIDs(List<DataSourceUUID> dataSourceUUIDs) {
		_dataSourceUUIDs = dataSourceUUIDs;
	}

	public void setIndividualId(String individualId) {
		_individualId = individualId;
	}

	public void setModifiedDate(Date modifiedDate) {
		if (modifiedDate != null) {
			_modifiedDate = new Date(modifiedDate.getTime());
		}
	}

	public void setSegmentId(Long segmentId) {
		_segmentId = segmentId;
	}

	public static class DataSourceIdentity {

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}

			if (!(obj instanceof DataSourceIdentity)) {
				return false;
			}

			DataSourceIdentity dataSourceIdentity = (DataSourceIdentity)obj;

			if (Objects.equals(
					_dataSourceId, dataSourceIdentity._dataSourceId) &&
				Objects.equals(_identityIds, dataSourceIdentity._identityIds)) {

				return true;
			}

			return false;
		}

		@BigQueryColumn
		public Long getDataSourceId() {
			return _dataSourceId;
		}

		@BigQueryColumn
		public List<String> getIdentityIds() {
			return _identityIds;
		}

		@Override
		public int hashCode() {
			return Objects.hash(_dataSourceId, _identityIds);
		}

		public void setDataSourceId(Long dataSourceId) {
			_dataSourceId = dataSourceId;
		}

		public void setIdentityIds(List<String> identityIds) {
			_identityIds = identityIds;
		}

		private Long _dataSourceId;
		private List<String> _identityIds;

	}

	public static class DataSourceUUID {

		public DataSourceUUID() {
		}

		public DataSourceUUID(Long dataSourceId, String uuid) {
			_dataSourceId = dataSourceId;
			_uuid = uuid;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}

			if (!(obj instanceof DataSourceUUID)) {
				return false;
			}

			DataSourceUUID dataSourceUUID = (DataSourceUUID)obj;

			if (Objects.equals(_dataSourceId, dataSourceUUID._dataSourceId) &&
				Objects.equals(_uuid, dataSourceUUID._uuid)) {

				return true;
			}

			return false;
		}

		@BigQueryColumn
		public Long getDataSourceId() {
			return _dataSourceId;
		}

		@BigQueryColumn
		public String getUuid() {
			return _uuid;
		}

		@Override
		public int hashCode() {
			return Objects.hash(_dataSourceId, _uuid);
		}

		public void setDataSourceId(Long dataSourceId) {
			_dataSourceId = dataSourceId;
		}

		public void setUuid(String uuid) {
			_uuid = uuid;
		}

		private Long _dataSourceId;
		private String _uuid;

	}

	private List<DataSourceIdentity> _dataSourceIdentities;
	private List<DataSourceUUID> _dataSourceUUIDs;
	private String _individualId;
	private Date _modifiedDate;
	private Long _segmentId;

}