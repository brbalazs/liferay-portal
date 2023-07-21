/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.internal.upgrade.v1_1_1;

import com.liferay.dynamic.data.mapping.io.DDMFormJSONDeserializer;
import com.liferay.dynamic.data.mapping.io.DDMFormJSONSerializer;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.dao.jdbc.AutoBatchPreparedStatementUtil;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.GetterUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.Map;

/**
 * @author Rafael Praxedes
 */
public class UpgradeDDMFormFieldSettings extends UpgradeProcess {

	public UpgradeDDMFormFieldSettings(
		DDMFormJSONDeserializer ddmFormJSONDeserializer,
		DDMFormJSONSerializer ddmFormJSONSerializer) {

		_ddmFormJSONDeserializer = ddmFormJSONDeserializer;
		_ddmFormJSONSerializer = ddmFormJSONSerializer;
	}

	@Override
	protected void doUpgrade() throws Exception {
		StringBundler sb = new StringBundler(5);

		sb.append("select DDMStructure.structureId, DDMStructure.definition ");
		sb.append("from DDLRecordSet inner join DDMStructure on ");
		sb.append("DDLRecordSet.DDMStructureId = DDMStructure.structureId ");
		sb.append("where DDLRecordSet.scope = ? and DDMStructure.definition ");
		sb.append("like ?");

		try (PreparedStatement ps1 = connection.prepareStatement(sb.toString());
			PreparedStatement ps2 =
				AutoBatchPreparedStatementUtil.concurrentAutoBatch(
					connection,
					"update DDMStructure set definition = ? where " +
						"structureId = ?")) {

			ps1.setInt(1, _SCOPE_FORMS);
			ps1.setString(2, "%ddmDataProviderInstanceId%");

			try (ResultSet rs = ps1.executeQuery()) {
				while (rs.next()) {
					String definition = rs.getString(2);

					String newDefinition = upgradeRecordSetStructure(
						definition);

					ps2.setString(1, newDefinition);

					long structureId = rs.getLong(1);

					ps2.setLong(2, structureId);

					ps2.addBatch();
				}

				ps2.executeBatch();
			}
		}
	}

	protected String upgradeRecordSetStructure(String definition)
		throws Exception {

		DDMForm ddmForm = _ddmFormJSONDeserializer.deserialize(definition);

		for (DDMFormField ddmFormField : ddmForm.getDDMFormFields()) {
			Map<String, Object> properties = ddmFormField.getProperties();

			if (properties.containsKey("ddmDataProviderInstanceId")) {
				String ddmDataProviderInstanceId = GetterUtil.getString(
					properties.get("ddmDataProviderInstanceId"));

				properties.put(
					"ddmDataProviderInstanceId",
					"[\"" + ddmDataProviderInstanceId + "\"]");

				properties.put(
					"ddmDataProviderInstanceOutput", "[\"Default-Output\"]");
			}
		}

		return _ddmFormJSONSerializer.serialize(ddmForm);
	}

	private static final int _SCOPE_FORMS = 2;

	private final DDMFormJSONDeserializer _ddmFormJSONDeserializer;
	private final DDMFormJSONSerializer _ddmFormJSONSerializer;

}