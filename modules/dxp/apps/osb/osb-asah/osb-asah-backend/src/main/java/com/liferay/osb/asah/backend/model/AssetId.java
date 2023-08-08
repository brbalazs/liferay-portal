/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.model;

import java.util.Objects;

/**
 * @author Marcellus Tavares
 */
public class AssetId {

	public static AssetId of(String fieldName, String fieldValue) {
		return new AssetId(fieldName, fieldValue);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof AssetId)) {
			return false;
		}

		AssetId assetId = (AssetId)obj;

		if (Objects.equals(_fieldName, assetId._fieldName) &&
			Objects.equals(_fieldValue, assetId._fieldValue)) {

			return true;
		}

		return false;
	}

	public String getFieldName() {
		return _fieldName;
	}

	public String getFieldValue() {
		return _fieldValue;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_fieldName, _fieldValue);
	}

	private AssetId(String fieldName, String fieldValue) {
		_fieldName = fieldName;
		_fieldValue = fieldValue;
	}

	private final String _fieldName;
	private final String _fieldValue;

}