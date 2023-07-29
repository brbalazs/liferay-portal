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

package com.liferay.osb.asah.common.model;

import com.liferay.osb.asah.common.entity.AuditEvent;

/**
 * @author Matthew Kong
 */
public enum DataControlTaskType {

	ACCESS(AuditEvent.Type.USER_ACCESS), DELETE(AuditEvent.Type.USER_DELETE),
	SUPPRESS(AuditEvent.Type.USER_SUPPRESS),
	UNSUPPRESS(AuditEvent.Type.USER_UNSUPPRESS);

	public AuditEvent.Type getAuditEventType() {
		return _auditEventType;
	}

	private DataControlTaskType(AuditEvent.Type auditEventType) {
		_auditEventType = auditEventType;
	}

	private final AuditEvent.Type _auditEventType;

}