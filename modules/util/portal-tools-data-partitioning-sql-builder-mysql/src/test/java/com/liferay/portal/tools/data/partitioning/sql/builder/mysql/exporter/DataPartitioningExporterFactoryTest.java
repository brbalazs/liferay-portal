/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.tools.data.partitioning.sql.builder.mysql.exporter;

import com.liferay.portal.tools.data.partitioning.sql.builder.exporter.DataPartitioningExporter;
import com.liferay.portal.tools.data.partitioning.sql.builder.exporter.DataPartitioningExporterFactory;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Manuel de la Peña
 */
public class DataPartitioningExporterFactoryTest {

	@Test
	public void testGetDataPartitioningExporterReturnsMySQLProvider()
		throws Exception {

		DataPartitioningExporter dataPartitioningExporter =
			DataPartitioningExporterFactory.getDataPartitioningExporter();

		Class<MySQLDataPartitioningExporter> clazz =
			MySQLDataPartitioningExporter.class;

		Assert.assertTrue(clazz.isInstance(dataPartitioningExporter));
	}

}