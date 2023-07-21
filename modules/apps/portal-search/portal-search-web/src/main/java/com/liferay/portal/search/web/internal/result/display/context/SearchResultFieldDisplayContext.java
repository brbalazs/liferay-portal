/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.web.internal.result.display.context;

import java.io.Serializable;

/**
 * @author André de Oliveira
 */
public class SearchResultFieldDisplayContext implements Serializable {

	public float getBoost() {
		return _boost;
	}

	public String getName() {
		return _name;
	}

	public String getValuesToString() {
		return _valuesToString;
	}

	public boolean isArray() {
		return _array;
	}

	public boolean isNumeric() {
		return _numeric;
	}

	public boolean isTokenized() {
		return _tokenized;
	}

	public void setArray(boolean array) {
		_array = array;
	}

	public void setBoost(float boost) {
		_boost = boost;
	}

	public void setName(String name) {
		_name = name;
	}

	public void setNumeric(boolean numeric) {
		_numeric = numeric;
	}

	public void setTokenized(boolean tokenized) {
		_tokenized = tokenized;
	}

	public void setValuesToString(String valuesToString) {
		_valuesToString = valuesToString;
	}

	private boolean _array;
	private float _boost;
	private String _name;
	private boolean _numeric;
	private boolean _tokenized;
	private String _valuesToString;

}