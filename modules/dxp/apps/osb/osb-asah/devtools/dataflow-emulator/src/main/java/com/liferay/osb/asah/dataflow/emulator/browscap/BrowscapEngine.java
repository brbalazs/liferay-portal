/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.emulator.browscap;

import com.blueconic.browscap.BrowsCapField;
import com.blueconic.browscap.ParseException;
import com.blueconic.browscap.UserAgentParser;
import com.blueconic.browscap.UserAgentService;

import java.io.IOException;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.stereotype.Component;

/**
 * @author Inácio Nery
 * @author André Miranda
 */
@Component
public class BrowscapEngine {

	public BrowscapDevice getDevice(String userAgent) {
		return new BrowscapDevice(userAgent, _userAgentParser);
	}

	@PostConstruct
	protected void init() {
		try {
			UserAgentService userAgentService = new UserAgentService();

			_userAgentParser = userAgentService.loadParser(
				Arrays.asList(
					BrowsCapField.BROWSER, BrowsCapField.BROWSER_TYPE,
					BrowsCapField.DEVICE_TYPE, BrowsCapField.IS_CRAWLER,
					BrowsCapField.PLATFORM));
		}
		catch (IOException | ParseException exception) {
			_log.error("Unable to load Browscap parser data", exception);

			throw new IllegalStateException(exception);
		}
	}

	private static final Log _log = LogFactory.getLog(BrowscapEngine.class);

	private UserAgentParser _userAgentParser;

}