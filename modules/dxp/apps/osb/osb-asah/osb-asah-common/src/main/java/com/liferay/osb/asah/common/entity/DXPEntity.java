/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.util.BeanUtils;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import org.json.JSONObject;

import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author Matthew Kong
 */
@Table
public class DXPEntity implements Persistable<Long> {

	public DXPEntity() {
	}

	public DXPEntity(Long dataSourceId, JSONObject fieldsJSONObject) {
		_dataSourceId = dataSourceId;
		_fieldsJSONObject = fieldsJSONObject;
	}

	public DXPEntity(Map<String, Object> source) {
		BeanUtils.copyProperties(source, this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof DXPEntity)) {
			return false;
		}

		DXPEntity dxpEntity = (DXPEntity)obj;

		if (Objects.equals(_dataSourceId, dxpEntity._dataSourceId) &&
			Objects.equals(_id, dxpEntity._id) &&
			Objects.equals(
				JSONUtil.toMap(_fieldsJSONObject),
				JSONUtil.toMap(dxpEntity._fieldsJSONObject)) &&
			Objects.equals(getType(), dxpEntity.getType())) {

			return true;
		}

		return false;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonAlias("osbAsahDataSourceId")
	public Long getDataSourceId() {
		return _dataSourceId;
	}

	public String getDataSourceName() {
		return _dataSourceName;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@Column("fields")
	@JsonProperty("fields")
	public JSONObject getFieldsJSONObject() {
		return _fieldsJSONObject;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@Id
	@JsonSerialize(using = ToStringSerializer.class)
	@Override
	public Long getId() {
		return _id;
	}

	@JsonIgnore
	public String getIdFieldValue() {
		DXPEntity.Type type = getType();

		Object value = _fieldsJSONObject.opt(type.getIdFieldName());

		if (value == null) {
			return null;
		}

		return String.valueOf(value);
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	public Date getModifiedDate() {
		if (_modifiedDate == null) {
			return null;
		}

		return new Date(_modifiedDate.getTime());
	}

	public String getName() {
		if (_name == null) {
			if (getType() == Type.USER) {
				return _fieldsJSONObject.optString("firstName") + " " +
					_fieldsJSONObject.optString("lastName");
			}

			return _fieldsJSONObject.optString("name", null);
		}

		return _name;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public DXPEntity.Type getType() {
		return _type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			_dataSourceId, _id, JSONUtil.toMap(_fieldsJSONObject), getType());
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		if ((_id == null) || ((_isNew != null) && _isNew)) {
			return true;
		}

		return false;
	}

	public void setDataSourceId(Long dataSourceId) {
		_dataSourceId = dataSourceId;
	}

	public void setDataSourceName(String dataSourceName) {
		_dataSourceName = dataSourceName;
	}

	public void setFieldsJSONObject(JSONObject fieldsJSONObject) {
		_fieldsJSONObject = fieldsJSONObject;
	}

	public void setId(Long id) {
		_id = id;
	}

	public void setIsNew(Boolean isNew) {
		_isNew = isNew;
	}

	public void setModifiedDate(Date modifiedDate) {
		if (modifiedDate != null) {
			_modifiedDate = new Date(modifiedDate.getTime());
		}
	}

	public void setName(String name) {
		_name = name;
	}

	public void setType(DXPEntity.Type type) {
		_type = type;
	}

	public enum Type {

		ANALYTICS_DELETE_MESSAGE(
			Type.CLASS_NAME_ANALYTICS_DELETE_MESSAGE, null, null, null, null),
		EXPANDO_COLUMN(
			Type.CLASS_NAME_EXPANDO_COLUMN, null, "name", null, null),
		GROUP(
			Type.CLASS_NAME_GROUP, "groups", "groupId", "groupIds",
			"referencedGroupIds"),
		ORGANIZATION(
			Type.CLASS_NAME_ORGANIZATION, "organizations", "organizationId",
			"organizationIds", "referencedOrganizationIds"),
		ROLE(
			Type.CLASS_NAME_ROLE, "roles", "roleId", "roleIds",
			"referencedRoleIds"),
		TEAM(
			Type.CLASS_NAME_TEAM, "teams", "teamId", "teamIds",
			"referencedTeamIds"),
		USER(
			Type.CLASS_NAME_USER, "users", "userId", "userId",
			"referencedUserIds"),
		USER_FIELD(Type.CLASS_NAME_USER_FIELD, null, null, null, null),
		USER_GROUP(
			Type.CLASS_NAME_USER_GROUP, "user-groups", "userGroupId",
			"userGroupIds", "referencedUserGroupIds");

		public static final String CLASS_NAME_ANALYTICS_DELETE_MESSAGE =
			"com.liferay.analytics.message.storage.model." +
				"AnalyticsDeleteMessage";

		public static final String CLASS_NAME_CONTACT =
			"com.liferay.portal.kernel.model.Contact";

		public static final String CLASS_NAME_EXPANDO_COLUMN =
			"com.liferay.expando.kernel.model.ExpandoColumn";

		public static final String CLASS_NAME_GROUP =
			"com.liferay.portal.kernel.model.Group";

		public static final String CLASS_NAME_ORGANIZATION =
			"com.liferay.portal.kernel.model.Organization";

		public static final String CLASS_NAME_ROLE =
			"com.liferay.portal.kernel.model.Role";

		public static final String CLASS_NAME_TEAM =
			"com.liferay.portal.kernel.model.Team";

		public static final String CLASS_NAME_USER =
			"com.liferay.portal.kernel.model.User";

		public static final String CLASS_NAME_USER_FIELD =
			"com.liferay.portal.kernel.model.User.field";

		public static final String CLASS_NAME_USER_GROUP =
			"com.liferay.portal.kernel.model.UserGroup";

		public static Type of(String className) {
			if (className.equals(CLASS_NAME_EXPANDO_COLUMN)) {
				return Type.EXPANDO_COLUMN;
			}

			if (className.equals(CLASS_NAME_GROUP)) {
				return Type.GROUP;
			}

			if (className.equals(CLASS_NAME_ORGANIZATION)) {
				return Type.ORGANIZATION;
			}

			if (className.equals(CLASS_NAME_ROLE)) {
				return Type.ROLE;
			}

			if (className.equals(CLASS_NAME_TEAM)) {
				return Type.TEAM;
			}

			if (className.equals(CLASS_NAME_USER)) {
				return Type.USER;
			}

			if (className.equals(CLASS_NAME_USER_FIELD)) {
				return Type.USER_FIELD;
			}

			if (className.equals(CLASS_NAME_USER_GROUP)) {
				return Type.USER_GROUP;
			}

			return null;
		}

		public static Type ofCollectionName(String collectionName) {
			if (collectionName.equals("groups")) {
				return of(CLASS_NAME_GROUP);
			}

			if (collectionName.equals("organizations")) {
				return of(CLASS_NAME_ORGANIZATION);
			}

			if (collectionName.equals("roles")) {
				return of(CLASS_NAME_ROLE);
			}

			if (collectionName.equals("teams")) {
				return of(CLASS_NAME_TEAM);
			}

			if (collectionName.equals("user-groups")) {
				return of(CLASS_NAME_USER_GROUP);
			}

			if (collectionName.equals("users")) {
				return of(CLASS_NAME_USER);
			}

			return null;
		}

		public static Type ofIndividualFieldName(String individualFieldName) {
			if (individualFieldName.equals("groupIds")) {
				return of(CLASS_NAME_GROUP);
			}

			if (individualFieldName.equals("organizationIds")) {
				return of(CLASS_NAME_ORGANIZATION);
			}

			if (individualFieldName.equals("roleIds")) {
				return of(CLASS_NAME_ROLE);
			}

			if (individualFieldName.equals("teamIds")) {
				return of(CLASS_NAME_TEAM);
			}

			if (individualFieldName.equals("userGroupIds")) {
				return of(CLASS_NAME_USER_GROUP);
			}

			if (individualFieldName.equals("userId")) {
				return of(CLASS_NAME_USER);
			}

			return null;
		}

		public String getClassName() {
			return _className;
		}

		public String getCollectionName() {
			return _collectionName;
		}

		public String getIdFieldName() {
			return _idFieldName;
		}

		public String getIndividualFieldName() {
			return _individualFieldName;
		}

		public String getIndividualSegmentFieldName() {
			return _individualSegmentFieldName;
		}

		public boolean isExpandoColumn() {
			if (equals(Type.EXPANDO_COLUMN)) {
				return true;
			}

			return false;
		}

		public boolean isGroup() {
			if (equals(Type.GROUP)) {
				return true;
			}

			return false;
		}

		public boolean isOrganization() {
			if (equals(Type.ORGANIZATION)) {
				return true;
			}

			return false;
		}

		public boolean isRole() {
			if (equals(Type.ROLE)) {
				return true;
			}

			return false;
		}

		public boolean isTeam() {
			if (equals(Type.TEAM)) {
				return true;
			}

			return false;
		}

		public boolean isUser() {
			if (equals(Type.USER)) {
				return true;
			}

			return false;
		}

		public boolean isUserField() {
			if (equals(Type.USER_FIELD)) {
				return true;
			}

			return false;
		}

		public boolean isUserGroup() {
			if (equals(Type.USER_GROUP)) {
				return true;
			}

			return false;
		}

		private Type(
			String className, String collectionName, String idFieldName,
			String individualFieldName, String individualSegmentFieldName) {

			_className = className;
			_collectionName = collectionName;
			_idFieldName = idFieldName;
			_individualFieldName = individualFieldName;
			_individualSegmentFieldName = individualSegmentFieldName;
		}

		private final String _className;
		private final String _collectionName;
		private final String _idFieldName;
		private final String _individualFieldName;
		private final String _individualSegmentFieldName;

	}

	@Transient
	private Long _dataSourceId;

	@Transient
	private String _dataSourceName;

	@Transient
	private JSONObject _fieldsJSONObject = new JSONObject();

	@Transient
	private Long _id;

	@Transient
	private Boolean _isNew;

	@Transient
	private Date _modifiedDate;

	@Transient
	private String _name;

	@Transient
	private Type _type;

}