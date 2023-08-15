/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.upgrade.v4_2_0;

import com.liferay.osb.asah.common.bigquery.BigQuerySchemaManager;
import com.liferay.osb.asah.common.dog.AsahMarkerDog;
import com.liferay.osb.asah.common.entity.AsahMarker;
import com.liferay.osb.asah.common.entity.Suppression;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.repository.SuppressionRepository;
import com.liferay.osb.asah.common.util.ListUtil;
import com.liferay.osb.asah.common.util.MapUtil;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;
import com.liferay.osb.asah.upgrade.UpgradeStep;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import javax.sql.DataSource;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 */
@Component
public class SuppressionUpgradeStep implements UpgradeStep {

	@Override
	public void upgrade(String version) {
		_bigQuerySchemaManager.createTable(
			ProjectIdThreadLocal.getProjectId(), "suppression");

		long lastInsertedSuppressionId = _getLastInsertedSuppressionId();

		while (true) {
			List<Map<String, Object>> suppressionRecords =
				_namedParameterJdbcTemplate.queryForList(
					"SELECT * FROM Suppression WHERE id > :id ORDER BY id " +
						"ASC LIMIT 50",
					Collections.singletonMap("id", lastInsertedSuppressionId));

			if (suppressionRecords.isEmpty()) {
				break;
			}

			for (Map<String, Object> suppressionRecord : suppressionRecords) {
				suppressionRecord.put("hidden", Boolean.FALSE);
			}

			_suppressionRepository.insertAll(
				ListUtil.map(suppressionRecords, Suppression::new));

			Map<String, Object> lastSuppressionRecord = suppressionRecords.get(
				suppressionRecords.size() - 1);

			lastInsertedSuppressionId = MapUtil.getLong(
				lastSuppressionRecord, "id");

			_updateLastInsertedSuppressionId(lastInsertedSuppressionId);
		}
	}

	private long _getLastInsertedSuppressionId() {
		Class<?> clazz = getClass();

		AsahMarker asahMarker = _asahMarkerDog.fetchAsahMarker(
			clazz.getSimpleName());

		if (asahMarker == null) {
			asahMarker = new AsahMarker();

			asahMarker.setContextJSONObject(
				JSONUtil.put("lastInsertedSuppressionId", 0));
			asahMarker.setId(clazz.getSimpleName());
			asahMarker.setIsNew(Boolean.TRUE);

			_asahMarkerDog.addAsahMarker(asahMarker);

			return 0;
		}

		JSONObject contextJSONObject = asahMarker.getContextJSONObject();

		return contextJSONObject.getLong("lastInsertedSuppressionId");
	}

	@PostConstruct
	private void _init() {
		_namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
			_dataSource);
	}

	private void _updateLastInsertedSuppressionId(
		long lastInsertedSuppressionId) {

		Class<?> clazz = getClass();

		AsahMarker asahMarker = _asahMarkerDog.getAsahMarker(
			clazz.getSimpleName());

		asahMarker.setContextJSONObject(
			JSONUtil.put(
				"lastInsertedSuppressionId", lastInsertedSuppressionId));

		_asahMarkerDog.updateAsahMarker(asahMarker);
	}

	@Autowired
	private AsahMarkerDog _asahMarkerDog;

	@Autowired
	private BigQuerySchemaManager _bigQuerySchemaManager;

	@Autowired
	private DataSource _dataSource;

	private NamedParameterJdbcTemplate _namedParameterJdbcTemplate;

	@Autowired
	private SuppressionRepository _suppressionRepository;

}