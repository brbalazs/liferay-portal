/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.backend.dog.CustomAssetDashboardDog;
import com.liferay.osb.asah.backend.dto.CustomAssetDashboardDTO;
import com.liferay.osb.asah.common.entity.CustomAssetDashboard;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author André Miranda
 */
@Component
@GraphQLTypeWiring(fieldName = "dashboard", typeName = "MutationType")
public class CustomAssetDashboardMutationDataFetcher
	implements DataFetcher<CustomAssetDashboardDTO> {

	@Override
	public CustomAssetDashboardDTO get(
		DataFetchingEnvironment dataFetchingEnvironment) {

		String dashboardId = dataFetchingEnvironment.getArgument("dashboardId");
		String definition = dataFetchingEnvironment.getArgument("definition");
		String modifiedByUserId = dataFetchingEnvironment.getArgument(
			"modifiedByUserId");
		String modifiedByUserName = dataFetchingEnvironment.getArgument(
			"modifiedByUserName");

		CustomAssetDashboard customAssetDashboard =
			_customAssetDashboardDog.updateCustomAssetDashboard(
				dashboardId, definition, modifiedByUserId, modifiedByUserName);

		if (customAssetDashboard == null) {
			return null;
		}

		return new CustomAssetDashboardDTO(customAssetDashboard);
	}

	@Autowired
	private CustomAssetDashboardDog _customAssetDashboardDog;

}