/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.osb.asah.upgrade.v4_1_2;

import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.Field;
import com.google.cloud.bigquery.LegacySQLTypeName;
import com.google.cloud.bigquery.Schema;
import com.google.cloud.bigquery.StandardTableDefinition;
import com.google.cloud.bigquery.Table;
import com.google.cloud.bigquery.TableDefinition;

import com.liferay.osb.asah.common.bigquery.BigQuerySchemaManager;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Marcos Martins
 */
@Component
public class PageUpgradeStep implements UpgradeStep {

	@Override
	public void upgrade(String version) {
		_bigQuerySchemaManager.createOrReplaceView(
			ProjectIdThreadLocal.getProjectId(), "pagehourly");

		if (_log.isInfoEnabled()) {
			_log.info("Page Hourly view successfully updated");
		}

		_updateTableFields(
			Arrays.asList(
				Field.newBuilder(
					"description", LegacySQLTypeName.STRING
				).setMode(
					Field.Mode.NULLABLE
				).build()),
			"pagedaily");

		if (_log.isInfoEnabled()) {
			_log.info("BigQuery successfully upgraded to schema 4.1.2");
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

	private static final Log _log = LogFactory.getLog(PageUpgradeStep.class);

	private BigQuery _bigQuery;

	@Autowired
	private BigQuerySchemaManager _bigQuerySchemaManager;

}