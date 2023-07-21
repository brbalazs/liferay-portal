/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.configuration.metatype.definitions.annotations.internal;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;
import com.liferay.portal.configuration.metatype.definitions.ExtendedAttributeDefinition;
import com.liferay.portal.kernel.util.StringUtil;

import java.io.IOException;
import java.io.InputStream;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.osgi.framework.Bundle;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

/**
 * @author Iván Zaera
 */
public class AnnotationsExtendedObjectClassDefinition
	implements com.liferay.portal.configuration.metatype.definitions.
				   ExtendedObjectClassDefinition {

	public AnnotationsExtendedObjectClassDefinition(
		Bundle bundle, ObjectClassDefinition objectClassDefinition) {

		_objectClassDefinition = objectClassDefinition;

		_loadConfigurationBeanClass(bundle);

		if (_configurationBeanClass != null) {
			_processExtendedMetatypeFields();
		}
	}

	@Override
	public ExtendedAttributeDefinition[] getAttributeDefinitions(int filter) {
		ExtendedAttributeDefinition[] extendedAttributeDefinitions =
			_extendedAttributeDefinitions.get(filter);

		if (extendedAttributeDefinitions != null) {
			return extendedAttributeDefinitions;
		}

		AttributeDefinition[] attributeDefinitions =
			_objectClassDefinition.getAttributeDefinitions(filter);

		extendedAttributeDefinitions =
			new ExtendedAttributeDefinition[attributeDefinitions.length];

		for (int i = 0; i < attributeDefinitions.length; i++) {
			extendedAttributeDefinitions[i] =
				new AnnotationsExtendedAttributeDefinition(
					_configurationBeanClass, attributeDefinitions[i]);
		}

		_extendedAttributeDefinitions.put(filter, extendedAttributeDefinitions);

		return extendedAttributeDefinitions;
	}

	@Override
	public String getDescription() {
		return _objectClassDefinition.getDescription();
	}

	@Override
	public Map<String, String> getExtensionAttributes(String uri) {
		Map<String, String> extensionAttributes = _extensionAttributes.get(uri);

		if (extensionAttributes == null) {
			extensionAttributes = Collections.emptyMap();
		}

		return extensionAttributes;
	}

	@Override
	public Set<String> getExtensionUris() {
		return _extensionAttributes.keySet();
	}

	@Override
	public InputStream getIcon(int size) throws IOException {
		return _objectClassDefinition.getIcon(size);
	}

	@Override
	public String getID() {
		return _objectClassDefinition.getID();
	}

	@Override
	public String getName() {
		return _objectClassDefinition.getName();
	}

	private void _loadConfigurationBeanClass(Bundle bundle) {
		try {
			BundleWiring bundleWiring = bundle.adapt(BundleWiring.class);

			ClassLoader classLoader = bundleWiring.getClassLoader();

			_configurationBeanClass = classLoader.loadClass(
				_objectClassDefinition.getID());
		}
		catch (ClassNotFoundException cnfe) {
		}
	}

	private void _processExtendedMetatypeFields() {
		ExtendedObjectClassDefinition extendedObjectClassDefinition =
			_configurationBeanClass.getAnnotation(
				ExtendedObjectClassDefinition.class);

		if (extendedObjectClassDefinition != null) {
			Map<String, String> map = new HashMap<>();

			map.put("category", extendedObjectClassDefinition.category());
			map.put(
				"description-arguments",
				StringUtil.merge(
					extendedObjectClassDefinition.descriptionArguments()));
			map.put(
				"factoryInstanceLabelAttribute",
				extendedObjectClassDefinition.factoryInstanceLabelAttribute());
			map.put(
				"generateUI",
				Boolean.toString(extendedObjectClassDefinition.generateUI()));
			map.put(
				"name-arguments",
				StringUtil.merge(
					extendedObjectClassDefinition.nameArguments()));

			ExtendedObjectClassDefinition.Scope scope =
				extendedObjectClassDefinition.scope();

			map.put("scope", scope.toString());

			_extensionAttributes.put(
				ExtendedObjectClassDefinition.XML_NAMESPACE, map);
		}
	}

	private Class<?> _configurationBeanClass;
	private final Map<Integer, ExtendedAttributeDefinition[]>
		_extendedAttributeDefinitions = new HashMap<>();
	private final Map<String, Map<String, String>> _extensionAttributes =
		new HashMap<>();
	private final ObjectClassDefinition _objectClassDefinition;

}