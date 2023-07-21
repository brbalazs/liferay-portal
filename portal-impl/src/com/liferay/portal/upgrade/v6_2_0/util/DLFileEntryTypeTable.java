/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.upgrade.v6_2_0.util;

import java.sql.Types;

/**
 * @author	  Brian Wing Shun Chan
 * @generated
 */
public class DLFileEntryTypeTable {

	public static final String TABLE_NAME = "DLFileEntryType";

	public static final Object[][] TABLE_COLUMNS = {
		{"uuid_", Types.VARCHAR},
		{"fileEntryTypeId", Types.BIGINT},
		{"groupId", Types.BIGINT},
		{"companyId", Types.BIGINT},
		{"userId", Types.BIGINT},
		{"userName", Types.VARCHAR},
		{"createDate", Types.TIMESTAMP},
		{"modifiedDate", Types.TIMESTAMP},
		{"fileEntryTypeKey", Types.VARCHAR},
		{"name", Types.VARCHAR},
		{"description", Types.VARCHAR}
	};

	public static final String TABLE_SQL_CREATE = "create table DLFileEntryType (uuid_ VARCHAR(75) null,fileEntryTypeId LONG not null primary key,groupId LONG,companyId LONG,userId LONG,userName VARCHAR(75) null,createDate DATE null,modifiedDate DATE null,fileEntryTypeKey VARCHAR(75) null,name STRING null,description STRING null)";

	public static final String TABLE_SQL_DROP = "drop table DLFileEntryType";

	public static final String[] TABLE_SQL_ADD_INDEXES = {
		"create index IX_4501FD9C on DLFileEntryType (groupId)",
		"create unique index IX_5B6BEF5F on DLFileEntryType (groupId, fileEntryTypeKey)",
		"create index IX_90724726 on DLFileEntryType (uuid_)",
		"create index IX_5B03E942 on DLFileEntryType (uuid_, companyId)",
		"create unique index IX_1399D844 on DLFileEntryType (uuid_, groupId)"
	};

}