/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.portlet.bundle.resourcebundletracker;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.osgi.service.component.annotations.Component;

/**
 * @author Manuel de la Peña
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + TestPortlet.PORTLET_NAME, "language.id=es_ES",
		"service.ranking:Integer=100"
	},
	service = ResourceBundle.class
)
public class TestResourceBundle extends ResourceBundle {

	public TestResourceBundle() {
		_map.put("a", "un");
		_map.put("is", "es");
		_map.put("resourcebundle", "paquete de recursos");
		_map.put(
			"resourcebundle-extension-key",
			"clave de extensión del paquete de recursos");
		_map.put("test", "prueba");
		_map.put("this", "esto");
	}

	@Override
	public Enumeration<String> getKeys() {
		return Collections.enumeration(_map.keySet());
	}

	@Override
	protected Object handleGetObject(String key) {
		if (key == null) {
			throw new IllegalArgumentException(
				"The key does not exist in this resource bundle");
		}

		return _map.get(key);
	}

	private final Map<String, String> _map = new HashMap<>();

}