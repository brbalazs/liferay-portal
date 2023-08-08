/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.test.util.spring;

import com.liferay.osb.asah.common.util.ArrayUtil;
import com.liferay.osb.asah.test.util.configuration.JDBCTestConfiguration;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.ClassDescriptor;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.ClassOrdererContext;

import org.springframework.context.annotation.Import;

/**
 * @author Inácio Nery
 */
public class OSBAsahClassOrderer implements ClassOrderer {

	@Override
	public void orderClasses(ClassOrdererContext classOrdererContext) {
		List<? extends ClassDescriptor> classDescriptors =
			classOrdererContext.getClassDescriptors();

		Comparator<ClassDescriptor> annotationComparator =
			Comparator.comparingInt(
				classDescriptor -> {
					Optional<Import> importOptional =
						classDescriptor.findAnnotation(Import.class);

					if (importOptional.isPresent() &&
						ArrayUtil.contains(
							importOptional.map(
								Import::value
							).get(),
							JDBCTestConfiguration.class)) {

						return 2;
					}

					return 1;
				});

		Comparator<ClassDescriptor> nameComparator = Comparator.comparing(
			classDescriptor -> {
				Class<?> testClass = classDescriptor.getTestClass();

				return testClass.getName();
			});

		Collections.sort(
			classDescriptors,
			annotationComparator.thenComparing(nameComparator));
	}

}