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