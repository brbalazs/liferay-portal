/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.entity;

import com.liferay.osb.asah.common.util.BeanUtils;

import java.util.Map;
import java.util.Objects;

import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author Marcellus Tavares
 */
@Table
public class AssetKeyword {

	public AssetKeyword() {
	}

	public AssetKeyword(Map<String, Object> source) {
		BeanUtils.copyProperties(source, this);
	}

	public AssetKeyword(String keyword, String type) {
		_keyword = keyword;
		_type = type;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof AssetKeyword)) {
			return false;
		}

		AssetKeyword assetKeyword = (AssetKeyword)obj;

		if (Objects.equals(_keyword, assetKeyword._keyword) &&
			Objects.equals(_type, assetKeyword._type)) {

			return true;
		}

		return false;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getKeyword() {
		return _keyword;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getType() {
		return _type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_keyword, _type);
	}

	public void setKeyword(String keyword) {
		_keyword = keyword;
	}

	public void setType(String type) {
		_type = type;
	}

	@Transient
	private String _keyword;

	@Transient
	private String _type;

}