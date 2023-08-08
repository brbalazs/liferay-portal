/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.model;

/**
 * @author Inácio Nery
 */
public interface MetricType {

	public String getAggregationName();

	public String getFieldName();

	public String getName();

	public TrendClassification.Order getTrendClassificationOrder();

	public default boolean isFieldNumeric() {
		return true;
	}

}