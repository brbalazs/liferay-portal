/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Marcellus Tavares
 */
public enum AssetType {

	BLOG("blog"), CUSTOM("custom"), DOCUMENT("document"), FORM("form"),
	INDIVIDUAL_METRIC("individualMetric"), JOURNAL("journal"), PAGE("page"),
	SITE("site");

	public static AssetType of(String value) {
		return Optional.ofNullable(
			_assetTypes.get(value)
		).orElseThrow(
			IllegalArgumentException::new
		);
	}

	public String getValue() {
		return _value;
	}

	private AssetType(String value) {
		_value = value;
	}

	private static final Map<String, AssetType> _assetTypes = new HashMap<>();

	static {
		for (AssetType assetType : values()) {
			_assetTypes.put(assetType.getValue(), assetType);
		}
	}

	private final String _value;

}