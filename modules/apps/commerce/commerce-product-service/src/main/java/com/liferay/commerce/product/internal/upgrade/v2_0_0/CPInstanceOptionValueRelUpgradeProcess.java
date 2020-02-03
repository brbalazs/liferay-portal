/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.commerce.product.internal.upgrade.v2_0_0;

import com.liferay.commerce.product.model.impl.CPInstanceOptionValueRelModelImpl;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.jdbc.AutoBatchPreparedStatementUtil;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;

import java.io.IOException;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Matija Petanjek
 */
public class CPInstanceOptionValueRelUpgradeProcess extends UpgradeProcess {

	public CPInstanceOptionValueRelUpgradeProcess(JSONFactory jsonFactory) {
		_jsonFactory = jsonFactory;
	}

	@Override
	protected void doUpgrade() throws Exception {
		if (!hasTable(CPInstanceOptionValueRelModelImpl.TABLE_NAME)) {
			_createCPInstanceOptionValueRelTable();
			_createCPInstanceOptionValueRelTableIndexes();
		}

		_importContentFromCPInstanceJsonField();
	}

	private void _createCPInstanceOptionValueRelTable()
		throws IOException, SQLException {

		runSQL(CPInstanceOptionValueRelModelImpl.TABLE_SQL_CREATE);
	}

	private void _createCPInstanceOptionValueRelTableIndexes()
		throws IOException, SQLException {

		runSQL(
			"create unique index IX_4BFAB7E7 on CPInstanceOptionValueRel (" +
				"CPDefinitionOptionRelId, CPDefinitionOptionValueRelId, " +
					"CPInstanceId)");

		runSQL(
			"create index IX_2C714896 on CPInstanceOptionValueRel (" +
				"CPInstanceId)");

		runSQL(
			"create index IX_F6E24C79 on CPInstanceOptionValueRel (" +
				"uuid_, companyId)");

		runSQL(
			"create unique index IX_AF559D3B on CPInstanceOptionValueRel (" +
				"uuid_, groupId)");
	}

	private long _getCPDefinitionOptionRelId(
			long cpDefinitionId, String cpDefinitionOptionRelKey)
		throws SQLException {

		try (Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(
				StringBundler.concat(
					"select CPDefinitionOptionRelId from ",
					"CPDefinitionOptionRel where CPDefinitionId = ",
					cpDefinitionId, " and key_ = '", cpDefinitionOptionRelKey,
					StringPool.APOSTROPHE))) {

			rs.next();

			return rs.getLong("CPDefinitionOptionRelId");
		}
	}

	private long _getCPDefinitionOptionValueRelId(
			long cpDefinitionOptionRelId, String cpDefinitionOptionValueKey)
		throws SQLException {

		try (Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(
				StringBundler.concat(
					"select CPDefinitionOptionValueRelId from ",
					"CPDefinitionOptionValueRel where CPDefinitionOptionRelId ",
					"= ", cpDefinitionOptionRelId, " and key_ = '",
					cpDefinitionOptionValueKey, StringPool.APOSTROPHE))) {

			rs.next();

			return rs.getLong("CPDefinitionOptionValueRelId");
		}
	}

	private void _importContentFromCPInstanceJsonField()
		throws JSONException, SQLException {

		String insertCPInstanceOptionValueRelSQL = StringBundler.concat(
			"insert into CPInstanceOptionValueRel(uuid_, ",
			"CPInstanceOptionValueRelId, groupId, companyId, userId, ",
			"userName, createDate, modifiedDate, CPDefinitionOptionRelId, ",
			"CPDefinitionOptionValueRelId, CPInstanceId) values (?, ?, ?, ?, ",
			"?, ?, ?, ?, ?, ?, ?)");

		try (PreparedStatement ps =
				AutoBatchPreparedStatementUtil.concurrentAutoBatch(
					connection, insertCPInstanceOptionValueRelSQL);
			Statement s = connection.createStatement(
				ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = s.executeQuery(
				"select CPInstanceId, groupId, companyId, userId, userName, " +
					"CPDefinitionId, json from CPInstance")) {

			while (rs.next()) {
				_insertCPInstanceOptionValueRelEntries(ps, rs);

				ps.executeBatch();
			}
		}
	}

	private void _insertCPInstanceOptionValueRelEntries(
			PreparedStatement ps, ResultSet rs)
		throws JSONException, SQLException {

		long groupId = rs.getLong("groupId");
		long companyId = rs.getLong("companyId");
		long userId = rs.getLong("userId");

		String userName = rs.getString("userName");

		long cpInstanceId = rs.getLong("CPInstanceId");
		long cpDefinitionId = rs.getLong("CPDefinitionId");

		String json = rs.getString("json");

		Map<String, String> processedCPInstanceOptions = new HashMap<>();

		JSONArray jsonArray = _jsonFactory.createJSONArray(json);

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);

			String cpDefinitionOptionRelKey = jsonObject.getString("key");

			long cpDefinitionOptionRelId = _getCPDefinitionOptionRelId(
				cpDefinitionId, cpDefinitionOptionRelKey);

			JSONArray cpDefinitionOptionValueRelJSONArray =
				jsonObject.getJSONArray("value");

			for (int j = 0; j < cpDefinitionOptionValueRelJSONArray.length();
				 j++) {

				String cpDefinitionOptionValueRelKey =
					cpDefinitionOptionValueRelJSONArray.getString(j);

				if (_isDuplicatedCPInstanceOption(
						processedCPInstanceOptions, cpDefinitionOptionRelKey,
						cpDefinitionOptionValueRelKey)) {

					continue;
				}

				processedCPInstanceOptions.put(
					cpDefinitionOptionRelKey, cpDefinitionOptionValueRelKey);

				long cpDefinitionOptionValueRelId =
					_getCPDefinitionOptionValueRelId(
						cpDefinitionOptionRelId, cpDefinitionOptionValueRelKey);

				String uuid = PortalUUIDUtil.generate();
				long cpInstanceOptionValueRelId = increment();

				ps.setString(1, uuid);
				ps.setLong(2, cpInstanceOptionValueRelId);
				ps.setLong(3, groupId);
				ps.setLong(4, companyId);
				ps.setLong(5, userId);
				ps.setString(6, userName);

				Date now = new Date(System.currentTimeMillis());

				ps.setDate(7, now);
				ps.setDate(8, now);

				ps.setLong(9, cpDefinitionOptionRelId);
				ps.setLong(10, cpDefinitionOptionValueRelId);
				ps.setLong(11, cpInstanceId);

				ps.addBatch();
			}
		}
	}

	private boolean _isDuplicatedCPInstanceOption(
		Map<String, String> processedCPInstanceOptions,
		String cpDefinitionOptionRelKey, String cpDefinitionOptionValueKey) {

		String processedCPDefinitionOptionValueKey =
			processedCPInstanceOptions.get(cpDefinitionOptionRelKey);

		if ((processedCPDefinitionOptionValueKey != null) &&
			processedCPDefinitionOptionValueKey.equals(
				cpDefinitionOptionValueKey)) {

			return true;
		}

		return false;
	}

	private final JSONFactory _jsonFactory;

}