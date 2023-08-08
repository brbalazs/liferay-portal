/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.impl;

import com.liferay.osb.asah.common.entity.BQDataSourceUser;
import com.liferay.osb.asah.common.repository.CustomBQDataSourceUserRepository;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectSelectStep;
import org.jooq.impl.DSL;

/**
 * @author Ivica Cardic
 */
public class BQDataSourceUserRepositoryImpl
	extends BaseRepository implements CustomBQDataSourceUserRepository {

	public BQDataSourceUserRepositoryImpl(DSLContext dslContext) {
		_dslContext = dslContext;
	}

	@Override
	public List<BQDataSourceUser> findBQDataSourceUsersByUserEmailAddressHashed(
		String userEmailAddressHashed) {

		SelectSelectStep<Record> selectSelectStep = _dslContext.select(
			DSL.table(
				"BQDataSourceUser"
			).asterisk());

		return selectSelectStep.from(
			"BQDataSourceUser"
		).join(
			DSL.table(
				"BQUser"
			).as(
				"user"
			)
		).on(
			DSL.field(
				"userId"
			).eq(
				DSL.field("user.id")
			)
		).where(
			DSL.field(
				"user.emailAddressHashed"
			).eq(
				userEmailAddressHashed
			)
		).fetch(
		).map(
			record -> new BQDataSourceUser(record.intoMap())
		);
	}

	private final DSLContext _dslContext;

}