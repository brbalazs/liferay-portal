/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.spring.cache;

import java.io.Serializable;

/**
 * @author Inácio Nery
 */
public class OSBAsahCacheMessage implements Serializable {

	public OSBAsahCacheMessage() {
	}

	public OSBAsahCacheMessage(String hostAddress, Object key, String name) {
		_hostAddress = hostAddress;
		_key = key;
		_name = name;
	}

	public String getHostAddress() {
		return _hostAddress;
	}

	public Object getKey() {
		return _key;
	}

	public String getName() {
		return _name;
	}

	public void setHostAddress(String hostAddress) {
		_hostAddress = hostAddress;
	}

	public void setKey(Object key) {
		_key = key;
	}

	public void setName(String name) {
		_name = name;
	}

	private String _hostAddress;
	private Object _key;
	private String _name;

}