/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.util.xml.descriptor;

import com.liferay.util.xml.ElementIdentifier;

import org.dom4j.Document;

/**
 * @author Jorge Ferrer
 */
public class StrutsConfigDescriptor extends SimpleXMLDescriptor {

	@Override
	public boolean canHandleType(String doctype, Document root) {
		return doctype.contains("struts-config");
	}

	@Override
	public ElementIdentifier[] getElementsIdentifiedByAttribute() {
		return _ELEMENTS_IDENTIFIED_BY_ATTR;
	}

	@Override
	public String[] getJoinableElements() {
		return _JOINABLE_ELEMENTS;
	}

	@Override
	public String[] getRootChildrenOrder() {
		return _ROOT_ORDERED_CHILDREN;
	}

	@Override
	public String[] getUniqueElements() {
		return _UNIQUE_ELEMENTS;
	}

	private static final ElementIdentifier[] _ELEMENTS_IDENTIFIED_BY_ATTR = {
		new ElementIdentifier("forward", "name"),
		new ElementIdentifier("action", "path"),
		new ElementIdentifier("data-source", "id"),
		new ElementIdentifier("form-bean", "name")
	};

	private static final String[] _JOINABLE_ELEMENTS = {
		"data-sources", "form-beans", "global-exceptions", "global-forwards",
		"action-mappings"
	};

	private static final String[] _ROOT_ORDERED_CHILDREN = {
		"data-sources", "form-beans", "global-exceptions", "global-forwards",
		"action-mappings", "controller", "message-resources", "plug-in"
	};

	private static final String[] _UNIQUE_ELEMENTS = {
		"data-sources", "form-beans", "global-exceptions", "global-forwards",
		"action-mappings", "controller"
	};

}