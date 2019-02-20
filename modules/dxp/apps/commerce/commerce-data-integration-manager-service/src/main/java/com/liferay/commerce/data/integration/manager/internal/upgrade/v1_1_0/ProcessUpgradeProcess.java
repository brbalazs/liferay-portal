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

package com.liferay.commerce.data.integration.manager.internal.upgrade.v1_1_0;

import com.liferay.commerce.data.integration.manager.model.impl.ProcessModelImpl;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;

/**
 * @author Riccardo Ferrari
 */
public class ProcessUpgradeProcess extends UpgradeProcess {

	public ProcessUpgradeProcess() {
		_columnName = "contextProperties";
		_columnType = "TEXT";
		_entityClass = ProcessModelImpl.class;
		_tableName = ProcessModelImpl.TABLE_NAME;
	}

	@Override
	protected void doUpgrade() throws Exception {
		_addColumn();
	}

	private void _addColumn() throws Exception {
		if (_log.isInfoEnabled()) {
			_log.info(
				String.format(
					"Adding column %s to table %s", _columnName, _tableName));
		}

		if (!hasColumn(_tableName, _columnName)) {
			alter(
				_entityClass,
				new AlterTableAddColumn(
					_columnName + StringPool.SPACE + _columnType));
		}
		else if (_log.isInfoEnabled()) {
			_log.info(
				String.format(
					"Column %s already exists on table %s", _columnName,
					_tableName));
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ProcessUpgradeProcess.class);

	private final String _columnName;
	private final String _columnType;
	private final Class _entityClass;
	private final String _tableName;

}