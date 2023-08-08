/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.messaging.model;

import java.util.Map;

/**
 * @author Robson Pastor
 */
public class Message<T> {

	public Message(
		String ackId, Map<String, String> attributes, String id, T object) {

		_ackId = ackId;
		_attributes = attributes;
		_id = id;
		_object = object;
	}

	public String getAckId() {
		return _ackId;
	}

	public Map<String, String> getAttributes() {
		return _attributes;
	}

	public String getId() {
		return _id;
	}

	public T getObject() {
		return _object;
	}

	private final String _ackId;
	private final Map<String, String> _attributes;
	private final String _id;
	private final T _object;

}