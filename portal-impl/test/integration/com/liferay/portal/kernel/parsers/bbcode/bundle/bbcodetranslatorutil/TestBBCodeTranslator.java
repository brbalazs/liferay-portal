/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.parsers.bbcode.bundle.bbcodetranslatorutil;

import com.liferay.portal.kernel.parsers.bbcode.BBCodeTranslator;

import org.osgi.service.component.annotations.Component;

/**
 * @author Peter Fellwock
 */
@Component(
	immediate = true, property = "service.ranking:Integer=" + Integer.MAX_VALUE,
	service = BBCodeTranslator.class
)
public class TestBBCodeTranslator implements BBCodeTranslator {

	public static final String END_TAG = "</TestBBCcodeTranslator>";

	public static final String START_TAG = "<TestBBCcodeTranslator>";

	@Override
	public String[] getEmoticonDescriptions() {
		return new String[] {"1", "2", "3"};
	}

	@Override
	public String[] getEmoticonFiles() {
		return new String[] {"1", "2"};
	}

	@Override
	public String[][] getEmoticons() {
		return null;
	}

	@Override
	public String[] getEmoticonSymbols() {
		return new String[] {"1", "2", "3", "4"};
	}

	@Override
	public String getHTML(String bbcode) {
		return START_TAG + bbcode + END_TAG;
	}

	@Override
	public String parse(String message) {
		if (message.equals(START_TAG)) {
			return END_TAG;
		}

		return START_TAG;
	}

}