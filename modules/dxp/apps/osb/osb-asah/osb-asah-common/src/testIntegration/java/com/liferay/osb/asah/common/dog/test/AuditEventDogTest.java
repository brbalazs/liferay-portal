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

package com.liferay.osb.asah.common.dog.test;

import com.liferay.osb.asah.common.OSBAsahCommonSpringTestContext;
import com.liferay.osb.asah.common.dog.AuditEventDog;
import com.liferay.osb.asah.common.entity.AuditEvent;
import com.liferay.osb.asah.common.entity.DataControlTask;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Marcellus Tavares
 */
public class AuditEventDogTest
	implements OSBAsahCommonSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@Test
	public void testAddAuditEvent() {
		AuditEvent auditEvent = _auditEventDog.addAuditEvent(
			"123", AuditEvent.Type.CHANNEL_CLEAR, "1", "Test Test");

		Assertions.assertNotNull(auditEvent.getCreateDate());
		Assertions.assertNotNull(auditEvent.getId());
		Assertions.assertEquals(
			AuditEvent.Type.CHANNEL_CLEAR, auditEvent.getType());
		Assertions.assertEquals("1", auditEvent.getUserId());
		Assertions.assertEquals("Test Test", auditEvent.getUserName());
	}

	@Test
	public void testAddAuditEventUserAccess() {
		AuditEvent auditEvent = _auditEventDog.addAuditEvent(
			"123", DataControlTask.Type.ACCESS, "1", "Test Test");

		Assertions.assertNotNull(auditEvent.getCreateDate());
		Assertions.assertNotNull(auditEvent.getId());
		Assertions.assertEquals(
			AuditEvent.Type.USER_ACCESS, auditEvent.getType());
		Assertions.assertEquals("1", auditEvent.getUserId());
		Assertions.assertEquals("Test Test", auditEvent.getUserName());
	}

	@Test
	public void testAddAuditEventUserDelete() {
		AuditEvent auditEvent = _auditEventDog.addAuditEvent(
			"123", DataControlTask.Type.DELETE, "1", "Test Test");

		Assertions.assertNotNull(auditEvent.getCreateDate());
		Assertions.assertNotNull(auditEvent.getId());
		Assertions.assertEquals(
			AuditEvent.Type.USER_DELETE, auditEvent.getType());
		Assertions.assertEquals("1", auditEvent.getUserId());
		Assertions.assertEquals("Test Test", auditEvent.getUserName());
	}

	@Test
	public void testAddAuditEventUserSuppress() {
		AuditEvent auditEvent = _auditEventDog.addAuditEvent(
			"123", DataControlTask.Type.SUPPRESS, "1", "Test Test");

		Assertions.assertNotNull(auditEvent.getCreateDate());
		Assertions.assertNotNull(auditEvent.getId());
		Assertions.assertEquals(
			AuditEvent.Type.USER_SUPPRESS, auditEvent.getType());
		Assertions.assertEquals("1", auditEvent.getUserId());
		Assertions.assertEquals("Test Test", auditEvent.getUserName());
	}

	@Test
	public void testAddAuditEventUserUnsuppress() {
		AuditEvent auditEvent = _auditEventDog.addAuditEvent(
			"123", DataControlTask.Type.UNSUPPRESS, "1", "Test Test");

		Assertions.assertNotNull(auditEvent.getCreateDate());
		Assertions.assertNotNull(auditEvent.getId());
		Assertions.assertEquals(
			AuditEvent.Type.USER_UNSUPPRESS, auditEvent.getType());
		Assertions.assertEquals("1", auditEvent.getUserId());
		Assertions.assertEquals("Test Test", auditEvent.getUserName());
	}

	@Autowired
	private AuditEventDog _auditEventDog;

}