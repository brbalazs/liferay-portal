/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.model;

import java.util.Objects;

/**
 * @author Matthew Kong
 */
public class Composition {

	public Composition() {
	}

	public Composition(long count, String name) {
		_count = count;
		_name = name;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof Composition)) {
			return false;
		}

		Composition composition = (Composition)obj;

		if (Objects.equals(_name, composition._name) &&
			Objects.equals(_count, composition._count)) {

			return true;
		}

		return false;
	}

	public long getCount() {
		return _count;
	}

	public String getName() {
		return _name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_name, _count);
	}

	public void setCount(long count) {
		_count = count;
	}

	public void setName(String name) {
		_name = name;
	}

	private long _count;
	private String _name;

}