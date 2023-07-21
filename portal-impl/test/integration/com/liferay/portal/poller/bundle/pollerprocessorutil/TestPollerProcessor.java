/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.poller.bundle.pollerprocessorutil;

import com.liferay.portal.kernel.poller.PollerProcessor;
import com.liferay.portal.kernel.poller.PollerRequest;
import com.liferay.portal.kernel.poller.PollerResponse;

import org.osgi.service.component.annotations.Component;

/**
 * @author Philip Jones
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=PollerProcessorUtilTest",
		"service.ranking:Integer=" + Integer.MAX_VALUE
	},
	service = PollerProcessor.class
)
public class TestPollerProcessor implements PollerProcessor {

	@Override
	public PollerResponse receive(PollerRequest pollerRequest) {
		return null;
	}

	@Override
	public void send(PollerRequest pollerRequest) {
	}

}