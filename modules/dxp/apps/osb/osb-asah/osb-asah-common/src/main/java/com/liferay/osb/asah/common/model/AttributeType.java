/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.model;

/**
 * @author Leslie Wong
 */
public enum AttributeType {

	EVENT("name", "value", "id", "BQEventProperty"),
	INDIVIDUAL(null, null, "ownerId", "Field"),
	USER_SESSION(null, null, "individualId", "UserSession");

	public String getAttributeIdFieldName() {
		return _attributeIdFieldName;
	}

	public String getAttributeValueFieldName() {
		return _attributeValueFieldName;
	}

	public String getJoinFieldName() {
		return _joinFieldName;
	}

	public String getQualifiedAttributeIdFieldName(String tableName) {
		if (tableName == null) {
			tableName = _tableName;
		}

		return tableName.concat(
			"."
		).concat(
			_attributeIdFieldName
		);
	}

	public String getQualifiedAttributeValueFieldName(String tableName) {
		if (tableName == null) {
			tableName = _tableName;
		}

		return tableName.concat(
			"."
		).concat(
			_attributeValueFieldName
		);
	}

	public String getQualifiedJoinFieldName(String tableName) {
		if (tableName == null) {
			tableName = _tableName;
		}

		return tableName.concat(
			"."
		).concat(
			_joinFieldName
		);
	}

	public String getTableName() {
		return _tableName;
	}

	private AttributeType(
		String attributeIdFieldName, String attributeValueFieldName,
		String joinFieldName, String tableName) {

		_attributeIdFieldName = attributeIdFieldName;
		_attributeValueFieldName = attributeValueFieldName;
		_joinFieldName = joinFieldName;
		_tableName = tableName;
	}

	private final String _attributeIdFieldName;
	private final String _attributeValueFieldName;
	private final String _joinFieldName;
	private final String _tableName;

}