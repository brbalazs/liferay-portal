/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.lar.xstream.bundle.xstreamconverterregistryutil;

import com.liferay.exportimport.kernel.xstream.XStreamConverter;
import com.liferay.exportimport.kernel.xstream.XStreamHierarchicalStreamReader;
import com.liferay.exportimport.kernel.xstream.XStreamHierarchicalStreamWriter;
import com.liferay.exportimport.kernel.xstream.XStreamMarshallingContext;
import com.liferay.exportimport.kernel.xstream.XStreamUnmarshallingContext;

import org.osgi.service.component.annotations.Component;

/**
 * @author Peter Fellwock
 */
@Component(
	immediate = true, property = "service.ranking:Integer=" + Integer.MAX_VALUE,
	service = XStreamConverter.class
)
public class TestXStreamConverter implements XStreamConverter {

	@Override
	public boolean canConvert(Class<?> clazz) {
		return false;
	}

	@Override
	public void marshal(
		Object object,
		XStreamHierarchicalStreamWriter xStreamHierarchicalStreamWriter,
		XStreamMarshallingContext xStreamMarshallingContext) {
	}

	@Override
	public Object unmarshal(
		XStreamHierarchicalStreamReader xStreamHierarchicalStreamReader,
		XStreamUnmarshallingContext xStreamUnmarshallingContext) {

		return null;
	}

}