/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog;

import com.liferay.osb.asah.common.entity.CustomAssetDashboard;
import com.liferay.osb.asah.common.model.Sort;
import com.liferay.osb.asah.common.repository.CustomAssetDashboardRepository;

import java.io.IOException;
import java.io.InputStream;

import java.util.Date;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;

import org.json.JSONObject;
import org.json.JSONTokener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;

/**
 * @author André Miranda
 */
@Component
public class CustomAssetDashboardDog {

	public CustomAssetDashboard fetchCustomAssetDashboard(
		String customAssetDashboardId) {

		Optional<CustomAssetDashboard> customAssetDashboardOptional =
			_customAssetDashboardRepository.findById(customAssetDashboardId);

		return customAssetDashboardOptional.orElse(null);
	}

	public Page<CustomAssetDashboard> getCustomAssetDashboardPage(
		Long channelId, String keywords, int page, int size, Sort sort) {

		PageRequest pageRequest = PageRequest.of(page, size, sort);

		return PageableExecutionUtils.getPage(
			_customAssetDashboardRepository.searchCustomAssetDashboards(
				channelId, keywords, pageRequest),
			pageRequest,
			() -> _customAssetDashboardRepository.countCustomAssetDashboards(
				channelId, keywords));
	}

	public CustomAssetDashboard updateCustomAssetDashboard(
		String customAssetDashboardId, String definition,
		String modifiedByUserId, String modifiedByUserName) {

		_dashboardDefinitionSchema.validate(new JSONObject(definition));

		CustomAssetDashboard customAssetDashboard = fetchCustomAssetDashboard(
			customAssetDashboardId);

		if (customAssetDashboard == null) {
			return null;
		}

		customAssetDashboard.setDefinition(definition);
		customAssetDashboard.setModifiedByUserId(modifiedByUserId);
		customAssetDashboard.setModifiedByUserName(modifiedByUserName);
		customAssetDashboard.setModifiedDate(new Date());

		return _customAssetDashboardRepository.save(customAssetDashboard);
	}

	@PostConstruct
	private void _init() {
		Class<?> clazz = getClass();

		try (InputStream inputStream = clazz.getResourceAsStream(
				"custom_asset_dashboard_definition_schema.json")) {

			_dashboardDefinitionSchema = SchemaLoader.load(
				new JSONObject(new JSONTokener(inputStream)));
		}
		catch (IOException ioException) {
			_log.error(ioException, ioException);

			throw new IllegalStateException(
				"Unable to read custom asset dashboard definition schema",
				ioException);
		}
	}

	private static final Log _log = LogFactory.getLog(
		CustomAssetDashboardDog.class);

	@Autowired
	private CustomAssetDashboardRepository _customAssetDashboardRepository;

	private Schema _dashboardDefinitionSchema;

}