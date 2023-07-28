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

package com.liferay.osb.asah.common.dog;

import com.liferay.osb.asah.common.entity.AuditEvent;
import com.liferay.osb.asah.common.repository.AuditEventRepository;
import com.liferay.osb.asah.common.util.TimeOrderedUuidGenerator;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 */
@Component
public class AuditEventDog {

	public AuditEvent addAuditEvent(
		String context, AuditEvent.Type type, String userId, String userName) {

		AuditEvent auditEvent = new AuditEvent();

		auditEvent.setContext(context);
		auditEvent.setCreateDate(new Date());
		auditEvent.setId(_timeOrderedUuidGenerator.generateIdAsLong());
		auditEvent.setIsNew(Boolean.TRUE);
		auditEvent.setType(type);
		auditEvent.setUserId(userId);
		auditEvent.setUserName(userName);

		return _auditEventRepository.save(auditEvent);
	}

	@Autowired
	private AuditEventRepository _auditEventRepository;

	private final TimeOrderedUuidGenerator _timeOrderedUuidGenerator =
		new TimeOrderedUuidGenerator();

}