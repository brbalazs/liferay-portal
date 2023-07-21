/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.fragment.entry.processor.editable.parser;

import com.liferay.fragment.exception.FragmentEntryContentException;

import org.jsoup.nodes.Element;

/**
 * This service provides a utility to replace editable element value.
 *
 * @author Pavel Savinov
 */
public interface EditableElementParser {

	public String getFieldTemplate();

	public String getValue(Element element);

	/**
	 * Replaces editable element value with the provided one.
	 *
	 * @param element Editable element to replace
	 * @param value New element value
	 */
	public void replace(Element element, String value);

	/**
	 * Validates editable element
	 *
	 * @param  element Editable element to validate
	 * @throws FragmentEntryContentException In case of invalid editable element
	 */
	public default void validate(Element element)
		throws FragmentEntryContentException {
	}

}