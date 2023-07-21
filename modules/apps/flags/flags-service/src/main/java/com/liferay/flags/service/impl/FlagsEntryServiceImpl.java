/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.flags.service.impl;

import com.liferay.flags.internal.messaging.FlagsRequest;
import com.liferay.flags.service.base.FlagsEntryServiceBaseImpl;
import com.liferay.portal.kernel.exception.EmailAddressException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.jsonwebservice.JSONWebService;
import com.liferay.portal.kernel.jsonwebservice.JSONWebServiceMode;
import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.Validator;

/**
 * @author Julio Camarero
 */
public class FlagsEntryServiceImpl extends FlagsEntryServiceBaseImpl {

	@JSONWebService(mode = JSONWebServiceMode.IGNORE)
	@Override
	public void addEntry(
			String className, long classPK, String reporterEmailAddress,
			long reportedUserId, String contentTitle, String contentURL,
			String reason, ServiceContext serviceContext)
		throws PortalException {

		if (!Validator.isEmailAddress(reporterEmailAddress)) {
			throw new EmailAddressException();
		}

		FlagsRequest flagsRequest = new FlagsRequest(
			className, classPK, reporterEmailAddress, reportedUserId,
			contentTitle, contentURL, reason, serviceContext);

		MessageBusUtil.sendMessage(DestinationNames.FLAGS, flagsRequest);
	}

}