/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.osb.faro.web.internal.model.display.contacts;

import com.liferay.osb.faro.model.FaroNotification;

/**
 * @author Geyson Silva
 */
@SuppressWarnings({"FieldCanBeLocal", "UnusedDeclaration"})
public class NotificationDisplay {

	public NotificationDisplay() {
	}

	public NotificationDisplay(FaroNotification faroNotification) {
		_groupId = faroNotification.getGroupId();
		_id = faroNotification.getFaroNotificationId();
		_read = faroNotification.getRead();
		_type = faroNotification.getType();
		_subType = faroNotification.getSubType();
		_userId = faroNotification.getUserId();
	}

	private long _groupId;
	private long _id;
	private boolean _read;
	private String _subType;
	private String _type;
	private long _userId;

}