/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.sanitizer.bundle.sanitizerimpl;

import com.liferay.portal.kernel.sanitizer.BaseSanitizer;
import com.liferay.portal.kernel.sanitizer.Sanitizer;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Peter Fellwock
 */
@Component(
	immediate = true, property = "service.ranking:Integer=" + Integer.MAX_VALUE,
	service = Sanitizer.class
)
public class TestSanitizer extends BaseSanitizer {

	@Override
	public String sanitize(
		long companyId, long groupId, long userId, String className,
		long classPK, String contentType, String[] modes, String content,
		Map<String, Object> options) {

		_atomicBoolean.set(Boolean.TRUE);

		return companyId + ":" + groupId;
	}

	@Reference(target = "(test=AtomicState)")
	protected void setAtomicBoolean(AtomicBoolean atomicBoolean) {
		_atomicBoolean = atomicBoolean;
	}

	private AtomicBoolean _atomicBoolean;

}