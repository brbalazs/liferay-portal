/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.model;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Matthew Kong
 */
public class DXPEntityType {

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

	public static DXPEntityType of(String className) {
		if (className.equals(CLASS_NAME_EXPANDO_COLUMN)) {
			return new DXPEntityType(className, null, null, null, null);
		}

		if (className.equals(CLASS_NAME_GROUP)) {
			return new DXPEntityType(
				className, "groups", "groupId", "groupIds",
				"referencedGroupIds");
		}

		if (className.equals(CLASS_NAME_ORGANIZATION)) {
			return new DXPEntityType(
				className, "organizations", "organizationId", "organizationIds",
				"referencedOrganizationIds");
		}

		if (className.equals(CLASS_NAME_ROLE)) {
			return new DXPEntityType(
				className, "roles", "roleId", "roleIds", "referencedRoleIds");
		}

		if (className.equals(CLASS_NAME_TEAM)) {
			return new DXPEntityType(
				className, "teams", "teamId", "teamIds", "referencedTeamIds");
		}

		if (className.equals(CLASS_NAME_USER)) {
			return new DXPEntityType(
				className, "users", "userId", "userId", "referencedUserIds");
		}

		if (className.equals(CLASS_NAME_USER_FIELD)) {
			return new DXPEntityType(className, null, null, null, null);
		}

		if (className.equals(CLASS_NAME_USER_GROUP)) {
			return new DXPEntityType(
				className, "user-groups", "userGroupId", "userGroupIds",
				"referencedUserGroupIds");
		}

		return null;
	}

	public static DXPEntityType ofIndividualFieldName(
		String individualFieldName) {

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
		if (StringUtils.equals(
				_className, DXPEntityType.CLASS_NAME_EXPANDO_COLUMN)) {

			return true;
		}

		return false;
	}

	public boolean isGroup() {
		if (StringUtils.equals(_className, DXPEntityType.CLASS_NAME_GROUP)) {
			return true;
		}

		return false;
	}

	public boolean isOrganization() {
		if (StringUtils.equals(
				_className, DXPEntityType.CLASS_NAME_ORGANIZATION)) {

			return true;
		}

		return false;
	}

	public boolean isRole() {
		if (StringUtils.equals(_className, DXPEntityType.CLASS_NAME_ROLE)) {
			return true;
		}

		return false;
	}

	public boolean isTeam() {
		if (StringUtils.equals(_className, DXPEntityType.CLASS_NAME_TEAM)) {
			return true;
		}

		return false;
	}

	public boolean isUser() {
		if (StringUtils.equals(_className, DXPEntityType.CLASS_NAME_USER)) {
			return true;
		}

		return false;
	}

	public boolean isUserField() {
		if (StringUtils.equals(
				_className, DXPEntityType.CLASS_NAME_USER_FIELD)) {

			return true;
		}

		return false;
	}

	public boolean isUserGroup() {
		if (StringUtils.equals(
				_className, DXPEntityType.CLASS_NAME_USER_GROUP)) {

			return true;
		}

		return false;
	}

	private DXPEntityType(
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