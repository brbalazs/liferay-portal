/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog.test;

import com.liferay.osb.asah.common.OSBAsahCommonSpringTestContext;
import com.liferay.osb.asah.common.dog.AuditEventDog;
import com.liferay.osb.asah.common.entity.AuditEvent;
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

	@Autowired
	private AuditEventDog _auditEventDog;

}