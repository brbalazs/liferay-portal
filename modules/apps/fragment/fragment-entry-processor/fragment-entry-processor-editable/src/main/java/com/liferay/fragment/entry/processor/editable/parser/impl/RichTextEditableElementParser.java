/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.fragment.entry.processor.editable.parser.impl;

import com.liferay.fragment.entry.processor.editable.EditableFragmentEntryProcessor;
import com.liferay.fragment.entry.processor.editable.parser.EditableElementParser;
import com.liferay.portal.kernel.util.StringUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import org.osgi.service.component.annotations.Component;

/**
 * @author Eudaldo Alonso
 */
@Component(
	immediate = true, property = "type=rich-text",
	service = EditableElementParser.class
)
public class RichTextEditableElementParser implements EditableElementParser {

	@Override
	public String getFieldTemplate() {
		return _TMPL_VALIDATE_TEXT_FIELD;
	}

	@Override
	public String getValue(Element element) {
		return element.html();
	}

	@Override
	public void replace(Element element, String value) {
		Document document = Jsoup.parseBodyFragment(value);

		Document.OutputSettings outputSettings = new Document.OutputSettings();

		outputSettings.prettyPrint(false);

		document.outputSettings(outputSettings);

		element.html(document.html());
	}

	private static final String _TMPL_VALIDATE_TEXT_FIELD = StringUtil.read(
		EditableFragmentEntryProcessor.class,
		"/META-INF/resources/fragment/entry/processor/editable" +
			"/text_field_template.tmpl");

}