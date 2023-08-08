/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.impl;

import com.liferay.osb.asah.common.entity.BQAccountEntry;
import com.liferay.osb.asah.common.repository.CustomBQAccountEntryRepository;
import com.liferay.osb.asah.common.repository.executor.QueryExecutor;

import java.util.Date;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;

/**
 * @author Marcellus Tavares
 */
public class BQAccountEntryRepositoryImpl
	implements CustomBQAccountEntryRepository {

	public BQAccountEntryRepositoryImpl(
		DSLContext dslContext, QueryExecutor queryExecutor) {

		_dslContext = dslContext;
		_queryExecutor = queryExecutor;
	}

	@Override
	public long count() {
		return _queryExecutor.queryForLong(
			_dslContext.selectCount(
			).from(
				DSL.table("BQAccountEntry")
			));
	}

	@Override
	public void deleteById(String id) {
		_queryExecutor.queryExecute(
			_dslContext.delete(
				DSL.table("BQAccountEntry")
			).where(
				DSL.field(
					"id"
				).eq(
					id
				)
			));
	}

	@Override
	public BQAccountEntry insert(BQAccountEntry bqAccountEntry) {
		_queryExecutor.queryExecute(
			_dslContext.insertInto(
				DSL.table("BQAccountEntry")
			).columns(
				DSL.field("accountEntryId", Long.class),
				DSL.field("createDate", Date.class),
				DSL.field("dataSourceId", Long.class),
				DSL.field("defaultCPaymentMethodKey"), DSL.field("description"),
				DSL.field("domains"), DSL.field("emailAddress"),
				DSL.field("id"), DSL.field("logoId"),
				DSL.field("modifiedDate", Date.class), DSL.field("name"),
				DSL.field("parentAccountEntryId"), DSL.field("status"),
				DSL.field("taxExemptionCode"), DSL.field("taxIdNumber"),
				DSL.field("type")
			).values(
				bqAccountEntry.getAccountEntryId(),
				bqAccountEntry.getCreateDate(),
				bqAccountEntry.getDataSourceId(),
				bqAccountEntry.getDefaultCPaymentMethodKey(),
				bqAccountEntry.getDescription(), bqAccountEntry.getDomains(),
				bqAccountEntry.getEmailAddress(), bqAccountEntry.getId(),
				bqAccountEntry.getLogoId(), bqAccountEntry.getModifiedDate(),
				bqAccountEntry.getName(),
				bqAccountEntry.getParentAccountEntryId(),
				bqAccountEntry.getStatus(),
				bqAccountEntry.getTaxExemptionCode(),
				bqAccountEntry.getTaxIdNumber(), bqAccountEntry.getType()
			));

		return bqAccountEntry;
	}

	private final DSLContext _dslContext;
	private final QueryExecutor _queryExecutor;

}