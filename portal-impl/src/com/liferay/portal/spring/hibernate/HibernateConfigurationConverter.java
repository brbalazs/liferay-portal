/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.spring.hibernate;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Converter;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.UnsecureSAXReaderUtil;

import java.util.Iterator;
import java.util.Map;

/**
 * <p>
 * See https://issues.liferay.com/browse/LPS-5363.
 * </p>
 *
 * @author Brian Wing Shun Chan
 */
public class HibernateConfigurationConverter implements Converter<String> {

	@Override
	public String convert(String input) {
		String output = input;

		try {
			output = doConvert(input);
		}
		catch (Exception e) {
			_log.error(e, e);
		}

		return output;
	}

	public void setClassNames(Map<String, String> classNames) {
		_classNames = classNames;
	}

	protected String doConvert(String input) throws Exception {
		if ((_classNames == null) || _classNames.isEmpty()) {
			return input;
		}

		Document document = UnsecureSAXReaderUtil.read(input);

		Element rootElement = document.getRootElement();

		Iterator<Element> itr = rootElement.elementIterator("class");

		while (itr.hasNext()) {
			Element classElement = itr.next();

			String oldName = classElement.attributeValue("name");

			String newName = _classNames.get(oldName);

			if (newName != null) {
				classElement.addAttribute("name", newName);
			}
		}

		return document.asXML();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		HibernateConfigurationConverter.class);

	private Map<String, String> _classNames;

}