/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
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