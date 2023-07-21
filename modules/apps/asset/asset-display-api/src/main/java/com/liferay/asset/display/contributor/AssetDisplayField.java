/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.asset.display.contributor;

import com.liferay.petra.lang.HashUtil;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;

import java.util.Objects;

/**
 * @author Jürgen Kappler
 */
public class AssetDisplayField {

	public AssetDisplayField(String key, String label) {
		this(key, label, _TYPE);
	}

	public AssetDisplayField(String key, String label, String type) {
		_key = key;
		_label = label;
		_type = type;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof AssetDisplayField)) {
			return false;
		}

		AssetDisplayField assetDisplayField = (AssetDisplayField)obj;

		if (Objects.equals(_key, assetDisplayField._key) &&
			Objects.equals(_label, assetDisplayField._label) &&
			Objects.equals(_type, assetDisplayField._type)) {

			return true;
		}

		return false;
	}

	public String getKey() {
		return _key;
	}

	public String getLabel() {
		return _label;
	}

	public String getType() {
		return _type;
	}

	@Override
	public int hashCode() {
		int hash = HashUtil.hash(0, _key);

		hash = HashUtil.hash(hash, _label);

		return HashUtil.hash(hash, _type);
	}

	public JSONObject toJSONObject() {
		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		jsonObject.put("key", getKey());
		jsonObject.put("label", getLabel());
		jsonObject.put("type", getType());

		return jsonObject;
	}

	private static final String _TYPE = "text";

	private final String _key;
	private final String _label;
	private final String _type;

}