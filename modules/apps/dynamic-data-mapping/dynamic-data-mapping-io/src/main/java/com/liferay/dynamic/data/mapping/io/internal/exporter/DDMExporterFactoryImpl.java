/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.io.internal.exporter;

import com.liferay.dynamic.data.mapping.io.exporter.DDMExporterFactory;
import com.liferay.dynamic.data.mapping.io.exporter.DDMFormExporter;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Marcellus Tavares
 */
@Component(immediate = true, service = DDMExporterFactory.class)
public class DDMExporterFactoryImpl implements DDMExporterFactory {

	@Override
	public Set<String> getAvailableFormats() {
		return Collections.unmodifiableSet(_ddmFormExporters.keySet());
	}

	@Override
	public Map<String, String> getAvailableFormatsMap() {
		Map<String, String> availableFormatsMap = new TreeMap<>();

		for (String format : getAvailableFormats()) {
			DDMFormExporter ddmFormExporter = getDDMFormExporter(format);

			availableFormatsMap.put(ddmFormExporter.getLabel(), format);
		}

		return availableFormatsMap;
	}

	@Override
	public DDMFormExporter getDDMFormExporter(String format) {
		DDMFormExporter ddmExporter = _ddmFormExporters.get(format);

		if (ddmExporter == null) {
			throw new IllegalArgumentException(
				"No DDM Form exporter exists for the format " + format);
		}

		return ddmExporter;
	}

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY
	)
	protected void addDDMFormExporter(DDMFormExporter ddmExporter) {
		_ddmFormExporters.put(ddmExporter.getFormat(), ddmExporter);
	}

	protected void removeDDMFormExporter(DDMFormExporter ddmExporter) {
		_ddmFormExporters.remove(ddmExporter.getFormat());
	}

	private final Map<String, DDMFormExporter> _ddmFormExporters =
		new ConcurrentHashMap<>();

}