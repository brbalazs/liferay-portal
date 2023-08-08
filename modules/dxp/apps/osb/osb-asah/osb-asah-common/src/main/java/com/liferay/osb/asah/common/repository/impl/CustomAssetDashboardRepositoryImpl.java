/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.impl;

import com.liferay.osb.asah.common.entity.CustomAssetDashboard;
import com.liferay.osb.asah.common.repository.CustomCustomAssetDashboardRepository;
import com.liferay.osb.asah.common.util.QueryUtil;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.SelectSelectStep;
import org.jooq.impl.DSL;

import org.springframework.data.domain.Pageable;

/**
 * @author André Miranda
 */
public class CustomAssetDashboardRepositoryImpl
	extends BaseRepository implements CustomCustomAssetDashboardRepository {

	public CustomAssetDashboardRepositoryImpl(DSLContext dslContext) {
		_dslContext = dslContext;
	}

	@Override
	public long countCustomAssetDashboards(Long channelId, String keywords) {
		SelectSelectStep<Record1<Integer>> selectSelectStep =
			_dslContext.selectCount();

		return selectSelectStep.from(
			"CustomAssetDashboard"
		).where(
			_getConditions(channelId, keywords)
		).fetchOptional(
			0, Long.class
		).orElse(
			0L
		);
	}

	@Override
	public List<CustomAssetDashboard> searchCustomAssetDashboards(
		Long channelId, String keywords, Pageable pageable) {

		SelectSelectStep<Record> selectSelectStep = _dslContext.select();

		return selectSelectStep.from(
			"CustomAssetDashboard"
		).where(
			_getConditions(channelId, keywords)
		).orderBy(
			getSortFields(pageable.getSort(), null)
		).limit(
			pageable.getPageSize()
		).offset(
			pageable.getOffset()
		).fetch(
			record -> new CustomAssetDashboard(record.intoMap())
		);
	}

	private List<Condition> _getConditions(Long channelId, String keywords) {
		List<Condition> conditions = new ArrayList<>();

		if (channelId != null) {
			Field<Object> field = DSL.field("channelId");

			conditions.add(field.eq(channelId));
		}

		if (StringUtils.isNotEmpty(keywords)) {
			Field<Object> field = DSL.field("assetTitle");

			conditions.add(
				field.likeIgnoreCase(
					String.format(
						"%%%s%%", QueryUtil.escapeKeywords(keywords))));
		}

		return conditions;
	}

	private final DSLContext _dslContext;

}