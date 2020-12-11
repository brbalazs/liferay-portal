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

package com.liferay.osb.faro.internal.upgrade.v11_0_0;

import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.StringBundler;

/**
 * @author Geyson Silva
 */
public class UpgradeFaroNotification extends UpgradeProcess {

	@Override
	protected void doUpgrade() throws Exception {
		runSQL(
			StringBundler.concat(
				"create table OSBFaro_FaroNotification (faroNotificationId ",
				"LONG not null primary key, groupId LONG, userId LONG, ",
				"createTime LONG, modifiedTime LONG, scope VARCHAR(75) null, ",
				"read_ BOOLEAN, type_ VARCHAR(75) null, subType VARCHAR(75) ",
				"null)"));
	}

}