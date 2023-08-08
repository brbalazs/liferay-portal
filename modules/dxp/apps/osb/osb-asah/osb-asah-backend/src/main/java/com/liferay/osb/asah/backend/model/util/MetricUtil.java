/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.model.util;

import com.liferay.osb.asah.backend.model.Metric;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import java.lang.reflect.Method;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author André Miranda
 */
public class MetricUtil {

	public static Set<Metric> getAvailableMetrics(Object metric) {
		Set<Metric> availableMetrics = new HashSet<>();

		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(metric.getClass());

			for (PropertyDescriptor propertyDescriptor :
					beanInfo.getPropertyDescriptors()) {

				Method readMethod = propertyDescriptor.getReadMethod();

				if ((readMethod != null) &&
					Objects.equals(
						propertyDescriptor.getPropertyType(), Metric.class)) {

					availableMetrics.add((Metric)readMethod.invoke(metric));
				}
			}
		}
		catch (IntrospectionException | ReflectiveOperationException
					exception) {

			_log.error(exception, exception);
		}

		return availableMetrics;
	}

	private static final Log _log = LogFactory.getLog(MetricUtil.class);

}