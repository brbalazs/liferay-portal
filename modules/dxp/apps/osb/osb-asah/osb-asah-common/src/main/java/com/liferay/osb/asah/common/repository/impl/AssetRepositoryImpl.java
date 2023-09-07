/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.impl;

import com.liferay.osb.asah.common.entity.Asset;
import com.liferay.osb.asah.common.entity.AssetKeyword;
import com.liferay.osb.asah.common.filter.expression.FilterExpression;
import com.liferay.osb.asah.common.repository.CustomAssetRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.SelectJoinStep;
import org.jooq.SelectSelectStep;
import org.jooq.Table;
import org.jooq.impl.DSL;

import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

/**
 * @author Marcellus Tavares
 */
public class AssetRepositoryImpl
	extends BaseRepository implements CustomAssetRepository {

	public AssetRepositoryImpl(DSLContext dslContext) {
		_dslContext = dslContext;
	}

	@Override
	public long countByAssetTypeAndFilterStringAndKeywords(
		String assetType, String filterString, @Nullable String keywords) {

		SelectSelectStep<Record1<Integer>> selectSelectStep =
			_dslContext.selectCount();

		return selectSelectStep.from(
			_getAssetTable(assetType, filterString, keywords)
		).fetchOptional(
			0, Long.class
		).orElse(
			0L
		);
	}

	@Override
	public List<Asset> findByAssetTypeAndFilterStringAndKeywords(
		String assetType, String filterString, String keywords,
		Pageable pageable) {

		SelectSelectStep<Record> selectSelectStep = _dslContext.select();

		SelectJoinStep<Record> selectJoinStep = selectSelectStep.from(
			_getAssetTable(assetType, filterString, keywords));

		return selectJoinStep.orderBy(
			getSortFields(pageable.getSort(), null)
		).limit(
			pageable.getPageSize()
		).offset(
			pageable.getOffset()
		).fetch(
			this::_toAsset
		);
	}

	@Override
	public List<Asset> findByChannelIds(
		List<Long> channelIds, Pageable pageable) {

		SelectSelectStep<Record> selectSelectStep = _dslContext.selectDistinct(
			DSL.table(
				"Asset"
			).asterisk());

		return selectSelectStep.from(
			"Asset"
		).where(
			DSL.condition(
				String.format(
					"channelIds && '{%s}'", StringUtils.join(channelIds, ",")))
		).orderBy(
			getSortFields(pageable.getSort(), null)
		).limit(
			pageable.getPageSize()
		).offset(
			pageable.getOffset()
		).fetch(
		).map(
			this::_toAsset
		);
	}

	private boolean _containsAssetKeywordFilter(String filterString) {
		if (StringUtils.contains(filterString, "keywords/")) {
			return true;
		}

		return false;
	}

	private List<AssetKeyword> _findAssetKeywordsByAssetId(Long assetId) {
		SelectSelectStep<Record> selectSelectStep = _dslContext.select();

		return selectSelectStep.from(
			"AssetKeyword"
		).where(
			DSL.field(
				"assetId"
			).eq(
				assetId
			)
		).orderBy(
			DSL.field("keyword")
		).fetch(
			record -> new AssetKeyword(record.intoMap())
		);
	}

	private Table<Record> _getAssetTable(
		String assetType, String filterString, String keyword) {

		SelectSelectStep<Record> selectSelectStep = _dslContext.selectDistinct(
			DSL.table(
				"Asset"
			).asterisk());

		SelectJoinStep<Record> selectJoinStep = selectSelectStep.from("Asset");

		if (_containsAssetKeywordFilter(filterString)) {
			selectJoinStep = selectJoinStep.join(
				DSL.table(
					"assetkeyword"
				).as(
					"keywords"
				)
			).on(
				DSL.field(
					"id"
				).eq(
					DSL.field("keywords.assetid")
				)
			);
		}

		return selectJoinStep.where(
			_getConditions(null, assetType, filterString, keyword)
		).asTable();
	}

	private List<Condition> _getConditions(
		Long assetId, String assetType, String filterString, String keyword) {

		List<Condition> conditions = new ArrayList<>();

		if (assetId != null) {
			conditions.add(
				DSL.field(
					"id"
				).gt(
					assetId
				));
		}

		if (StringUtils.isNotBlank(assetType)) {
			conditions.add(
				DSL.field(
					"assetType"
				).eq(
					assetType
				));
		}

		if (StringUtils.isNotBlank(keyword)) {
			conditions.add(
				DSL.or(
					DSL.field(
						"canonicalURL"
					).containsIgnoreCase(
						keyword
					),
					DSL.field(
						"description"
					).containsIgnoreCase(
						keyword
					),
					DSL.field(
						"title"
					).containsIgnoreCase(
						keyword
					),
					DSL.field(
						"url"
					).containsIgnoreCase(
						keyword
					)));
		}

		if (StringUtils.isNotBlank(filterString)) {
			FilterExpression filterExpression = new FilterExpression(
				null, filterString);

			conditions.add(filterExpression.getCondition());
		}

		return conditions;
	}

	private Asset _toAsset(Record assetRecord) {
		Asset asset = new Asset(assetRecord.intoMap());

		List<AssetKeyword> assetKeywords = _findAssetKeywordsByAssetId(
			asset.getId());

		if (!assetKeywords.isEmpty()) {
			asset.setAssetKeywords(new HashSet<>(assetKeywords));
		}

		return asset;
	}

	private final DSLContext _dslContext;

}