/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.deploy.hot.bundle.servicewrapperregistry;

import com.liferay.portal.kernel.model.EmailAddress;
import com.liferay.portal.kernel.service.EmailAddressLocalService;
import com.liferay.portal.kernel.service.EmailAddressLocalServiceWrapper;
import com.liferay.portal.kernel.service.ServiceWrapper;

import org.osgi.service.component.annotations.Component;

/**
 * @author Manuel de la Peña
 * @author Miguel Pastor
 */
@Component(immediate = true, service = ServiceWrapper.class)
public class TestEmailLocalServiceWrapper
	extends EmailAddressLocalServiceWrapper {

	public TestEmailLocalServiceWrapper() {
		super(null);
	}

	public TestEmailLocalServiceWrapper(
		EmailAddressLocalService emailAddressService) {

		super(emailAddressService);
	}

	@Override
	public EmailAddress getEmailAddress(long emailAddressId) {
		EmailAddress emailAddress = createEmailAddress(1);

		emailAddress.setAddress("email@liferay.com");

		return emailAddress;
	}

}