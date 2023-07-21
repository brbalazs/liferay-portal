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
public class TilesDefsDescriptor extends SimpleXMLDescriptor {

	@Override
	public boolean canHandleType(String doctype, Document root) {
		return doctype.contains("tiles-config");
	}

	@Override
	public ElementIdentifier[] getElementsIdentifiedByAttribute() {
		return _ELEMENTS_IDENTIFIED_BY_ATTR;
	}

	@Override
	public String[] getRootChildrenOrder() {
		return _ROOT_CHILDREN_ORDER;
	}

	@Override
	public String[] getUniqueElements() {
		return _UNIQUE_ELEMENTS;
	}

	private static final ElementIdentifier[] _ELEMENTS_IDENTIFIED_BY_ATTR = {
		new ElementIdentifier("definition", "name")
	};

	private static final String[] _ROOT_CHILDREN_ORDER = {"definition"};

	private static final String[] _UNIQUE_ELEMENTS = {};

}