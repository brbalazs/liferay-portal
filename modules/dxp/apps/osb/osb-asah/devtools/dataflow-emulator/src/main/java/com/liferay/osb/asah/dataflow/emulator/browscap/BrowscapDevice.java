/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.emulator.browscap;

import com.blueconic.browscap.BrowsCapField;
import com.blueconic.browscap.Capabilities;
import com.blueconic.browscap.UserAgentParser;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Inácio Nery
 * @author André Miranda
 */
public class BrowscapDevice {

	public BrowscapDevice(String userAgent, UserAgentParser userAgentParser) {
		_capabilities = userAgentParser.parse(userAgent);

		_userAgent = userAgent;
	}

	public String getBrowserName() {
		if (_userAgent.matches(".*Edge?/.*")) {
			return "Edge";
		}

		return _capabilities.getBrowser();
	}

	public String getCrawler() {
		Map<BrowsCapField, String> values = _capabilities.getValues();

		return StringUtils.capitalize(values.get(BrowsCapField.IS_CRAWLER));
	}

	public String getDeviceType() {
		String deviceType = _capabilities.getDeviceType();

		return _replacements.getOrDefault(deviceType, deviceType);
	}

	public String getPlatformName() {
		return _capabilities.getPlatform();
	}

	private final Capabilities _capabilities;
	private final Map<String, String> _replacements =
		new HashMap<String, String>() {
			{
				put("Mobile Device", "Mobile");
				put("Mobile Phone", "SmartPhone");
			}
		};
	private final String _userAgent;

}