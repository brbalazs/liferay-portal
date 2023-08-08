/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Marcellus Tavares
 */
public class DataControlTaskTest {

	@Test
	public void testAccessAuditEventType() {
		DataControlTask.Type dataControlTaskType = DataControlTask.Type.ACCESS;

		Assertions.assertEquals(
			AuditEvent.Type.USER_ACCESS,
			dataControlTaskType.getAuditEventType());
	}

	@Test
	public void testDeleteAuditEventType() {
		DataControlTask.Type dataControlTaskType = DataControlTask.Type.DELETE;

		Assertions.assertEquals(
			AuditEvent.Type.USER_DELETE,
			dataControlTaskType.getAuditEventType());
	}

	@Test
	public void testSuppressAuditEventType() {
		DataControlTask.Type dataControlTaskType =
			DataControlTask.Type.SUPPRESS;

		Assertions.assertEquals(
			AuditEvent.Type.USER_SUPPRESS,
			dataControlTaskType.getAuditEventType());
	}

	@Test
	public void testUnsuppressAuditEventType() {
		DataControlTask.Type dataControlTaskType =
			DataControlTask.Type.UNSUPPRESS;

		Assertions.assertEquals(
			AuditEvent.Type.USER_UNSUPPRESS,
			dataControlTaskType.getAuditEventType());
	}

}