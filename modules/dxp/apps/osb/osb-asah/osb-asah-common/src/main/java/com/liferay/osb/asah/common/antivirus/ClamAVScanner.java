/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.antivirus;

import com.liferay.osb.asah.common.spring.http.exception.OSBAsahException;
import com.liferay.osb.asah.common.spring.http.exception.OSBAsahVirusFoundException;

import fi.solita.clamav.ClamAVClient;
import fi.solita.clamav.ClamAVSizeLimitException;

import java.io.IOException;
import java.io.InputStream;

import java.nio.charset.StandardCharsets;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 */
@Component
@ConditionalOnProperty(
	havingValue = "true", value = "osb.asah.antivirus.enabled"
)
public class ClamAVScanner {

	public void scan(InputStream inputStream) {
		try {
			byte[] reply = _clamAVClient.scan(inputStream);

			if (!ClamAVClient.isCleanReply(reply)) {
				throw new OSBAsahVirusFoundException(_getVirusName(reply));
			}
		}
		catch (ClamAVSizeLimitException | IOException exception) {
			throw new OSBAsahException(
				HttpStatus.BAD_REQUEST, "Unable to scan for viruses",
				exception);
		}
	}

	private String _getVirusName(byte[] reply) {
		String virusName = new String(reply, StandardCharsets.US_ASCII);

		return virusName.substring(
			"stream: ".length(), virusName.length() - (" FOUND".length() + 1));
	}

	@PostConstruct
	private void _init() {
		_clamAVClient = new ClamAVClient(
			_clamAVHostname, _clamAVPort, _clamAVTimeout);
	}

	private ClamAVClient _clamAVClient;

	@Value("${osb.asah.clamav.hostname:osbasahclamav}")
	private String _clamAVHostname;

	@Value("${osb.asah.clamav.port:3310}")
	private int _clamAVPort;

	@Value("${osb.asah.clamav.timeout:10000}")
	private int _clamAVTimeout;

}