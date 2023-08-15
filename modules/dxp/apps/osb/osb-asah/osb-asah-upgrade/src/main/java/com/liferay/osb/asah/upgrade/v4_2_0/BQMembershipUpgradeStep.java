/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.upgrade.v4_2_0;

import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.Field;
import com.google.cloud.bigquery.LegacySQLTypeName;
import com.google.cloud.bigquery.Schema;
import com.google.cloud.bigquery.StandardTableDefinition;
import com.google.cloud.bigquery.Table;
import com.google.cloud.bigquery.TableDefinition;

import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;
import com.liferay.osb.asah.common.util.SetUtil;
import com.liferay.osb.asah.upgrade.UpgradeStep;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 */
@Component
public class BQMembershipUpgradeStep implements UpgradeStep {

	@Override
	public void upgrade(String version) {
		_updateTableFields(
			Arrays.asList(
				Field.newBuilder(
					"channelId", LegacySQLTypeName.INTEGER
				).setMode(
					Field.Mode.NULLABLE
				).build()),
			"membershipchange");

		_updateTableFields(
			Arrays.asList(
				Field.newBuilder(
					"channelId", LegacySQLTypeName.INTEGER
				).setMode(
					Field.Mode.NULLABLE
				).build()),
			"membershipindividual");

		if (_log.isInfoEnabled()) {
			_log.info("BigQuery successfully upgraded to schema 4.2.0");
		}
	}

	private List<Field> _getTableFields(Table table) {
		TableDefinition definition = table.getDefinition();

		Schema currentSchema = definition.getSchema();

		return new ArrayList<>(currentSchema.getFields());
	}

	@PostConstruct
	private void _init() {
		BigQueryOptions bigQueryOptions = BigQueryOptions.getDefaultInstance();

		_bigQuery = bigQueryOptions.getService();
	}

	private void _updateTableFields(List<Field> newFields, String tableName) {
		List<Field> fields = new ArrayList<>();

		fields.addAll(newFields);

		Set<String> newFieldNames = SetUtil.map(newFields, Field::getName);

		Table table = _bigQuery.getTable(
			ProjectIdThreadLocal.getProjectId(), tableName);

		for (Field field : _getTableFields(table)) {
			if (newFieldNames.contains(field.getName())) {
				continue;
			}

			fields.add(field);
		}

		Table.Builder builder = table.toBuilder();

		builder = builder.setDefinition(
			StandardTableDefinition.of(Schema.of(fields)));

		table = builder.build();

		table.update();
	}

	private static final Log _log = LogFactory.getLog(
		BQMembershipUpgradeStep.class);

	private BigQuery _bigQuery;

}