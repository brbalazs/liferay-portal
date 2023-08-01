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

package com.liferay.osb.asah.common.repository.impl;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.date.dog.util.TimeZoneDogUtil;
import com.liferay.osb.asah.common.entity.BQIdentityInterestScore;
import com.liferay.osb.asah.common.filter.expression.FilterExpression;
import com.liferay.osb.asah.common.model.Composition;
import com.liferay.osb.asah.common.model.CompositionResultBag;
import com.liferay.osb.asah.common.model.IdentityInterestScore;
import com.liferay.osb.asah.common.repository.CustomBQIdentityInterestScoreRepository;
import com.liferay.osb.asah.common.repository.executor.QueryExecutor;
import com.liferay.osb.asah.common.repository.helper.DSLHelper;

import java.math.BigDecimal;

import java.sql.Timestamp;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.DatePart;
import org.jooq.Field;
import org.jooq.InsertValuesStep6;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Select;
import org.jooq.SelectConditionStep;
import org.jooq.SelectJoinStep;
import org.jooq.SelectSelectStep;
import org.jooq.SortField;
import org.jooq.Table;
import org.jooq.WindowPartitionByStep;
import org.jooq.impl.DSL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;

/**
 * @author Robson Pastor
 */
public class BQIdentityInterestScoreRepositoryImpl
	extends BaseRepository implements CustomBQIdentityInterestScoreRepository {

	public BQIdentityInterestScoreRepositoryImpl(DSLContext dslContext) {
		_dslContext = dslContext;

		_fieldNames = new HashMap<String, String>() {
			{
				put("dateRecorded", "recordedDate");
				put("name", "keyword");
				put("ownerId", "individualId");
			}
		};
	}

	@Override
	public long count() {
		SelectSelectStep<Record1<Integer>> selectSelectStep =
			_dslContext.selectCount();

		return _queryExecutor.queryForLong(
			selectSelectStep.from("BQIdentityInterestScore"));
	}

	@Override
	public long countByChannelIdAndIndividualId(
		@Nullable Long channelId, String individualId) {

		SelectSelectStep<Record1<Integer>> selectSelectStep =
			_dslContext.selectCount();

		return _queryExecutor.queryForLong(
			selectSelectStep.from(
				DSL.table(
					"BQIdentityInterestScore"
				).as(
					"IdentityInterestScore"
				)
			).join(
				DSL.table(
					"BQIdentity"
				).as(
					"Identity"
				)
			).on(
				DSL.field(
					"IdentityInterestScore.identityId"
				).eq(
					DSL.field("Identity.id")
				)
			).where(
				_getConditions(channelId, individualId)
			));
	}

	@Override
	public long countByChannelIdAndIndividualIdAndKeywords(
		@Nullable Long channelId, @Nullable String individualId,
		@Nullable String keywords) {

		List<String> individualIds = null;

		if (StringUtils.isNotBlank(individualId)) {
			individualIds = Arrays.asList(individualId);
		}

		List<Condition> conditions = _getConditions(
			channelId, Boolean.TRUE, individualIds, keywords, null);

		conditions.add(
			DSL.field(
				"IdentityInterestPage.views", Long.class
			).gt(
				0L
			));

		return _queryExecutor.queryForLong(
			_dslContext.with(
				"DistinctIdentityInterestScore"
			).as(
				_dslContext.selectDistinct(
					DSL.field("individualId"),
					DSL.field("IdentityInterestScore.keyword")
				).from(
					DSL.table(
						"BQIdentityInterestScore"
					).as(
						"IdentityInterestScore"
					)
				).join(
					DSL.table(
						"BQIdentity"
					).as(
						"Identity"
					)
				).on(
					DSL.field(
						"IdentityInterestScore.identityId"
					).eq(
						DSL.field("Identity.id")
					)
				).join(
					DSL.table(
						"BQIdentityInterestPage"
					).as(
						"IdentityInterestPage"
					)
				).on(
					DSL.and(
						DSL.field(
							"IdentityInterestPage.identityId"
						).eq(
							DSL.field("Identity.id")
						),
						DSL.field(
							"LOWER(IdentityInterestPage.keyword)"
						).eq(
							DSL.field("LOWER(IdentityInterestScore.keyword)")
						))
				).where(
					conditions
				)
			).selectCount(
			).from(
				"DistinctIdentityInterestScore"
			));
	}

	@Override
	public long countByIndividualId(String individualId) {
		return _queryExecutor.queryForLong(
			_dslContext.selectCount(
			).from(
				DSL.table(
					"BQIdentityInterestScore"
				).as(
					"IdentityInterestScore"
				)
			).join(
				DSL.table(
					"BQIdentity"
				).as(
					"Identity"
				)
			).on(
				DSL.field(
					"IdentityInterestScore.identityId"
				).eq(
					DSL.field("Identity.id")
				)
			).where(
				DSL.and(
					DSL.field(
						"Identity.individualId"
					).eq(
						individualId
					),
					DSL.field(
						"recordedDate"
					).eq(
						_dslContext.select(
							DSL.max(DSL.field("recordedDate"))
						).from(
							DSL.table("BQIdentityInterestScore")
						)
					))
			));
	}

	public long countKeywords(@Nullable String keywords) {
		return _queryExecutor.queryForLong(
			_dslContext.select(
				DSL.countDistinct(DSL.field("LOWER(keyword)", String.class))
			).from(
				DSL.table(
					"BQIdentityInterestScore"
				).as(
					"IdentityInterestScore"
				)
			).where(
				_getConditions(null, null, null, keywords, null)
			));
	}

	@Override
	public void deleteByKeywordAndRecordedDateGreaterThanEqual(
		String keyword, Date recordedDate) {

		_queryExecutor.queryExecute(
			_dslContext.delete(
				DSL.table("BQIdentityInterestScore")
			).where(
				DSL.and(
					DSL.field(
						"LOWER(keyword)"
					).eq(
						StringUtils.lowerCase(keyword)
					),
					DSL.field(
						"recordedDate"
					).ge(
						DateUtil.toUTCString(
							recordedDate, DateUtil.PATTERN_SHORT)
					))
			));
	}

	@Override
	public void deleteByRecordedDate(Date recordedDate) {
		_queryExecutor.queryExecute(
			_dslContext.delete(
				DSL.table("BQIdentityInterestScore")
			).where(
				DSL.field(
					"recordedDate"
				).eq(
					DateUtil.toUTCString(recordedDate, DateUtil.PATTERN_SHORT)
				)
			));
	}

	@Override
	public void deleteByRecordedDateLessThanEqual(Date recordedDate) {
		_queryExecutor.queryExecute(
			_dslContext.delete(
				DSL.table("BQIdentityInterestScore")
			).where(
				DSL.field(
					"recordedDate"
				).le(
					DateUtil.toUTCString(recordedDate, DateUtil.PATTERN_SHORT)
				)
			));
	}

	@Override
	public List<BQIdentityInterestScore> findByChannelIdAndIndividualId(
		@Nullable Long channelId, String individualId, Pageable pageable) {

		return _queryExecutor.queryForList(
			BQIdentityInterestScore::new,
			_dslContext.selectDistinct(
				DSL.field(
					"IdentityInterestScore.channelId"
				).as(
					"channelId"
				),
				DSL.field(
					"IdentityInterestScore.identityId"
				).as(
					"identityId"
				),
				DSL.field(
					"IdentityInterestScore.interestScore"
				).as(
					"interestScore"
				),
				DSL.field(
					"IdentityInterestScore.interested"
				).as(
					"interested"
				),
				DSL.field(
					"LOWER(IdentityInterestScore.keyword)"
				).as(
					"keyword"
				),
				DSL.field(
					"IdentityInterestScore.recordedDate"
				).as(
					"recordedDate"
				)
			).from(
				DSL.table(
					"BQIdentityInterestScore"
				).as(
					"IdentityInterestScore"
				)
			).join(
				DSL.table(
					"BQIdentity"
				).as(
					"Identity"
				)
			).on(
				DSL.field(
					"IdentityInterestScore.identityId"
				).eq(
					DSL.field("Identity.id")
				)
			).where(
				_getConditions(channelId, individualId)
			).limit(
				pageable.getPageSize()
			).offset(
				pageable.getOffset()
			));
	}

	@Override
	public List<IdentityInterestScore>
		findByChannelIdAndIndividualIdAndKeywords(
			@Nullable Long channelId, @Nullable String individualId,
			@Nullable String keywords, Pageable pageable) {

		List<String> individualIds = null;

		if (StringUtils.isNotBlank(individualId)) {
			individualIds = Arrays.asList(individualId);
		}

		List<Condition> conditions = _getConditions(
			channelId, Boolean.TRUE, individualIds, keywords, null);

		conditions.add(
			DSL.field(
				"IdentityInterestPage.views", Long.class
			).gt(
				0L
			));

		return _queryExecutor.queryForList(
			record -> {
				BigDecimal contributingPagesCount = (BigDecimal)record.get(
					"contributingPagesCount");

				return new IdentityInterestScore(
					new BQIdentityInterestScore(record),
					contributingPagesCount.longValue(),
					(String)record.get("individualId"));
			},
			_dslContext.select(
				DSL.field(
					"COUNT(DISTINCT CONCAT(" +
						"IdentityInterestPage.canonicalUrl, " +
							"IdentityInterestPage.title))"
				).as(
					"contributingPagesCount"
				),
				DSL.max(
					DSL.field("IdentityInterestScore.interestScore")
				).as(
					"interestScore"
				),
				DSL.field(
					"LOWER(IdentityInterestScore.keyword)"
				).as(
					"keyword"
				),
				DSL.field(
					"IdentityInterestScore.recordedDate"
				).as(
					"recordedDate"
				),
				DSL.field(
					"Identity.individualId"
				).as(
					"individualId"
				)
			).from(
				DSL.table(
					"BQIdentityInterestScore"
				).as(
					"IdentityInterestScore"
				)
			).join(
				DSL.table(
					"BQIdentity"
				).as(
					"Identity"
				)
			).on(
				DSL.field(
					"IdentityInterestScore.identityId"
				).eq(
					DSL.field("Identity.id")
				)
			).join(
				DSL.table(
					"BQIdentityInterestPage"
				).as(
					"IdentityInterestPage"
				)
			).on(
				DSL.and(
					DSL.field(
						"IdentityInterestPage.identityId"
					).eq(
						DSL.field("Identity.id")
					),
					DSL.field(
						"LOWER(IdentityInterestPage.keyword)"
					).eq(
						DSL.field("LOWER(IdentityInterestScore.keyword)")
					))
			).where(
				conditions
			).groupBy(
				DSL.field("individualId"), DSL.field("keyword"),
				DSL.field("recordedDate")
			).orderBy(
				_getSortFields(pageable.getSort(), null)
			).limit(
				pageable.getPageSize()
			).offset(
				pageable.getOffset()
			));
	}

	@Override
	public List<BQIdentityInterestScore> findByIndividualId(
		String individualId, Pageable pageable) {

		return _queryExecutor.queryForList(
			BQIdentityInterestScore::new,
			_dslContext.selectDistinct(
				DSL.field(
					"IdentityInterestScore.identityId"
				).as(
					"identityId"
				),
				DSL.field(
					"IdentityInterestScore.interestScore"
				).as(
					"interestScore"
				),
				DSL.field(
					"IdentityInterestScore.interested"
				).as(
					"interested"
				),
				DSL.field(
					"LOWER(IdentityInterestScore.keyword)"
				).as(
					"keyword"
				),
				DSL.field(
					"IdentityInterestScore.recordedDate"
				).as(
					"recordedDate"
				)
			).from(
				DSL.table(
					"BQIdentityInterestScore"
				).as(
					"IdentityInterestScore"
				)
			).join(
				DSL.table(
					"BQIdentity"
				).as(
					"Identity"
				)
			).on(
				DSL.field(
					"IdentityInterestScore.identityId"
				).eq(
					DSL.field("Identity.id")
				)
			).where(
				DSL.and(
					DSL.field(
						"Identity.individualId"
					).eq(
						individualId
					),
					DSL.field(
						"recordedDate"
					).eq(
						_dslContext.select(
							DSL.max(DSL.field("recordedDate"))
						).from(
							DSL.table("BQIdentityInterestScore")
						)
					))
			).limit(
				pageable.getPageSize()
			).offset(
				pageable.getOffset()
			));
	}

	@Override
	public List<BQIdentityInterestScore>
		findByIndividualIdAndKeywordAndRecordedDateBetween(
			String individualId, String keyword, Date recordedDate1,
			Date recordedDate2) {

		return _queryExecutor.queryForList(
			BQIdentityInterestScore::new,
			_dslContext.selectDistinct(
				DSL.field(
					"IdentityInterestScore.channelId"
				).as(
					"channelId"
				),
				DSL.field(
					"IdentityInterestScore.identityId"
				).as(
					"identityId"
				),
				DSL.field(
					"IdentityInterestScore.interestScore"
				).as(
					"interestScore"
				),
				DSL.field(
					"IdentityInterestScore.interested"
				).as(
					"interested"
				),
				DSL.field(
					"LOWER(IdentityInterestScore.keyword)"
				).as(
					"keyword"
				),
				DSL.field(
					"IdentityInterestScore.recordedDate"
				).as(
					"recordedDate"
				)
			).from(
				DSL.table(
					"BQIdentityInterestScore"
				).as(
					"IdentityInterestScore"
				)
			).join(
				DSL.table(
					"BQIdentity"
				).as(
					"Identity"
				)
			).on(
				DSL.field(
					"IdentityInterestScore.identityId"
				).eq(
					DSL.field("Identity.id")
				)
			).where(
				DSL.field(
					"Identity.individualId"
				).eq(
					individualId
				)
			).and(
				DSL.field(
					"LOWER(IdentityInterestScore.keyword)"
				).eq(
					StringUtils.lowerCase(keyword)
				)
			).and(
				DSL.field(
					"IdentityInterestScore.recordedDate"
				).between(
					DateUtil.toUTCString(recordedDate1, DateUtil.PATTERN_SHORT),
					DateUtil.toUTCString(recordedDate2, DateUtil.PATTERN_SHORT)
				)
			));
	}

	@Override
	public List<BQIdentityInterestScore> findByRecordedDate(
		@Nullable Date recordedDate, int size) {

		SelectSelectStep<Record> selectSelectStep = _dslContext.select();

		return _queryExecutor.queryForList(
			BQIdentityInterestScore::new,
			selectSelectStep.from(
				DSL.table(
					"BQIdentityInterestScore"
				).as(
					"IdentityInterestScore"
				)
			).where(
				_getConditions(null, null, null, null, recordedDate)
			).orderBy(
				DSL.field("LOWER(keyword)")
			).limit(
				size
			));
	}

	@Override
	public List<String> findIndividualIdsByFilterStringAndIndividualId(
		@Nullable String filterString, String individualId) {

		Field<String> field = DSL.field("individualId", String.class);

		SelectJoinStep<Record1<String>> selectJoinStep = _dslContext.select(
			field
		).from(
			DSL.table(
				"BQIdentityInterestScore"
			).as(
				"IdentityInterestScore"
			)
		);

		List<String> individualIds = new ArrayList<>();

		if (individualId != null) {
			individualIds.add(individualId);
		}

		selectJoinStep = _getBQIdentitySelectJoinStep(
			individualIds, selectJoinStep);

		List<Condition> conditions = _getConditions(
			null, null, individualIds, null, null);

		if (!StringUtils.isEmpty(filterString)) {
			FilterExpression filterExpression = new FilterExpression(
				filterString);

			conditions.add(filterExpression.getCondition());
		}

		return _queryExecutor.queryForList(
			record -> (String)record.get("individualId"),
			selectJoinStep.where(conditions));
	}

	@Override
	public BQIdentityInterestScore getByIndividualIdAndKeywordAndRecordedDate(
		String individualId, String keyword, Date recordedDate) {

		SelectSelectStep selectSelectStep = _dslContext.selectDistinct(
			DSL.field(
				"IdentityInterestScore.channelId"
			).as(
				"channelId"
			),
			DSL.field(
				"IdentityInterestScore.identityId"
			).as(
				"identityId"
			),
			DSL.field(
				"IdentityInterestScore.interestScore"
			).as(
				"interestScore"
			),
			DSL.field(
				"IdentityInterestScore.interested"
			).as(
				"interested"
			),
			DSL.field(
				"LOWER(IdentityInterestScore.keyword)"
			).as(
				"keyword"
			),
			DSL.field(
				"IdentityInterestScore.recordedDate"
			).as(
				"recordedDate"
			));

		Optional<BQIdentityInterestScore> bqIdentityInterestScoreOptional =
			_queryExecutor.queryForObject(
				BQIdentityInterestScore::new,
				selectSelectStep.from(
					DSL.table(
						"BQIdentityInterestScore"
					).as(
						"IdentityInterestScore"
					)
				).join(
					DSL.table(
						"BQIdentity"
					).as(
						"Identity"
					)
				).on(
					DSL.field(
						"IdentityInterestScore.identityId"
					).eq(
						DSL.field("Identity.id")
					)
				).where(
					DSL.field(
						"Identity.individualId"
					).eq(
						individualId
					),
					DSL.field(
						"LOWER(IdentityInterestScore.keyword)"
					).eq(
						StringUtils.lowerCase(keyword)
					),
					DSL.field(
						"IdentityInterestScore.recordedDate"
					).eq(
						DateUtil.toUTCString(
							recordedDate, DateUtil.PATTERN_SHORT)
					)
				));

		return bqIdentityInterestScoreOptional.orElseThrow(
			IllegalArgumentException::new);
	}

	@Override
	public CompositionResultBag getInterestCompositionResultBag(
		boolean active, @Nullable Long channelId, @Nullable String keywords,
		@Nullable Long segmentId, Pageable pageable) {

		SelectJoinStep<Record2<String, String>> selectSelectStep =
			_dslContext.select(
				DSL.field("IdentityInterestScore.identityId", String.class),
				DSL.field(
					"LOWER(IdentityInterestScore.keyword)", String.class
				).as(
					"keyword"
				)
			).from(
				DSL.table(
					"BQIdentityInterestScore"
				).as(
					"IdentityInterestScore"
				)
			);

		List<Condition> conditions = new ArrayList<>();

		conditions.add(
			DSL.field(
				"IdentityInterestScore.recordedDate"
			).eq(
				_dslContext.select(
					DSL.max(DSL.field("recordedDate"))
				).from(
					DSL.table("BQIdentityInterestScore")
				)
			));

		if (channelId != null) {
			conditions.add(
				DSL.field(
					"IdentityInterestScore.channelId", Long.class
				).eq(
					channelId
				));
		}

		conditions.add(
			DSL.field(
				"IdentityInterestScore.interested", Boolean.class
			).eq(
				Boolean.TRUE
			));

		if (StringUtils.isNotBlank(keywords)) {
			conditions.add(_dslHelper.containsSubstring("keyword", keywords));
		}

		if (segmentId != null) {
			selectSelectStep = selectSelectStep.join(
				"BQMembership"
			).on(
				DSL.and(
					DSL.field(
						"IdentityInterestScore.identityId"
					).eq(
						DSL.field("BQMembership.identityId")
					),
					DSL.field(
						"BQMembership.segmentId", Long.class
					).eq(
						segmentId
					))
			);
		}

		Field<Integer> countField = DSL.countDistinct(
			DSL.coalesce(
				DSL.field("IdentityOverview.individualId"),
				DSL.field("IdentityOverview.identityId")));

		List<Map<String, Object>> records = _queryExecutor.queryForList(
			Function.identity(),
			_dslContext.with(
				"IdentityActivityOverview"
			).as(
				_getIdentityActivityOverview(active, segmentId)
			).with(
				"IdentityOverview"
			).as(
				_getIdentityOverview(channelId)
			).with(
				"KeywordIdentity"
			).as(
				selectSelectStep.where(conditions)
			).select(
				countField.as("count"),
				DSL.field(
					"keyword"
				).as(
					"keyword"
				),
				DSL.max(
					countField
				).over(
				).as(
					"maxCount"
				),
				DSL.count(
					DSL.field("LOWER(keyword)")
				).over(
				).as(
					"total"
				),
				DSL.max(
					DSL.field("IdentityOverview.totalCount")
				).as(
					"totalCount"
				)
			).from(
				DSL.table("KeywordIdentity")
			).join(
				DSL.table("IdentityOverview")
			).on(
				DSL.field(
					"KeywordIdentity.identityId"
				).eq(
					DSL.field("IdentityOverview.identityId")
				)
			).groupBy(
				DSL.field("keyword")
			).orderBy(
				_getSortFields(pageable.getSort(), null)
			).limit(
				pageable.getPageSize()
			).offset(
				pageable.getOffset()
			));

		BigDecimal maxCountBigDecimal = BigDecimal.ZERO;
		BigDecimal totalBigDecimal = BigDecimal.ZERO;
		BigDecimal totalCountBigDecimal = BigDecimal.ZERO;

		List<Composition> compositions = new ArrayList<>();

		for (int i = 0; i < records.size(); i++) {
			Map<String, Object> record = records.get(i);

			if (i == 0) {
				maxCountBigDecimal = new BigDecimal(
					String.valueOf(record.get("maxCount")));
				totalBigDecimal = new BigDecimal(
					String.valueOf(record.get("total")));
				totalCountBigDecimal = new BigDecimal(
					String.valueOf(record.get("totalCount")));
			}

			BigDecimal count = new BigDecimal(
				String.valueOf(record.get("count")));

			compositions.add(
				new Composition(
					count.longValue(), (String)record.get("keyword")));
		}

		return new CompositionResultBag(
			maxCountBigDecimal.longValue(), compositions,
			totalBigDecimal.longValue(), totalCountBigDecimal.longValue());
	}

	public List<String> getKeywords(
		@Nullable String keywords, Pageable pageable) {

		Field<String> field = DSL.field(
			"LOWER(keyword)", String.class
		).as(
			"keyword"
		);

		SelectSelectStep<Record1<String>> selectSelectStep =
			_dslContext.selectDistinct(field);

		return _queryExecutor.queryForList(
			record -> (String)record.get("keyword"),
			selectSelectStep.from(
				DSL.table(
					"BQIdentityInterestScore"
				).as(
					"IdentityInterestScore"
				)
			).where(
				_getConditions(null, null, null, keywords, null)
			).orderBy(
				field.asc()
			).limit(
				pageable.getPageSize()
			).offset(
				pageable.getOffset()
			));
	}

	@Override
	public List<String> getTopKeywordsByIndividualId(
		String individualId, int size) {

		SelectSelectStep<Record1<String>> selectSelectStep = _dslContext.select(
			DSL.field(
				"LOWER(keyword)", String.class
			).as(
				"keyword"
			));

		return _queryExecutor.queryForList(
			recordMap -> (String)recordMap.get("keyword"),
			selectSelectStep.from(
				DSL.table(
					"BQIdentityInterestScore"
				).as(
					"IdentityInterestScore"
				)
			).join(
				DSL.table(
					"BQIdentity"
				).as(
					"Identity"
				)
			).on(
				DSL.field(
					"IdentityInterestScore.identityId"
				).eq(
					DSL.field("Identity.id")
				)
			).where(
				DSL.and(
					DSL.field(
						"individualId"
					).equal(
						individualId
					),
					DSL.field(
						"recordedDate"
					).eq(
						_dslContext.select(
							DSL.max(DSL.field("recordedDate"))
						).from(
							DSL.table("BQIdentityInterestScore")
						)
					))
			).orderBy(
				DSL.field(
					"interestScore"
				).desc()
			).limit(
				size
			).offset(
				0
			));
	}

	@Override
	public List<Map<String, Object>> getTransformations(
		Date fromDate, @Nullable String filterString, String period,
		Date toDate) {

		Field<OffsetDateTime> periodField = _getPeriodField(period);

		SelectSelectStep<Record3<OffsetDateTime, BigDecimal, Integer>>
			selectSelectStep = _dslContext.select(
				periodField,
				DSL.avg(
					DSL.field("interestScore", Double.class)
				).as(
					"scoreAvg"
				),
				DSL.field(
					"COUNTIF(keyword IS NOT NULL)", Integer.class
				).as(
					"totalElements"
				));

		List<Condition> conditions = new ArrayList<>();

		conditions.add(
			DSL.field(
				"recordedDate"
			).equal(
				DSL.field("generatedDate")
			));

		if (StringUtils.isNotBlank(filterString)) {
			FilterExpression filterExpression = new FilterExpression(
				filterString);

			conditions.add(filterExpression.getCondition());
		}

		DatePart datePart = DatePart.DAY;

		if (period.equals("hour")) {
			datePart = DatePart.HOUR;
		}

		return _queryExecutor.queryForList(
			record -> new HashMap<String, Object>() {
				{
					put(
						"intervalInitDate",
						DateUtil.toUTCString(
							(Date)record.get("intervalInitDate")));
					put(
						"scoreAvg",
						_getAggregationValue(
							(BigDecimal)record.get("scoreAvg")));
					put("totalElements", record.get("totalElements"));
				}
			},
			selectSelectStep.from(
				_dslHelper.getTimeSeriesTable(
					datePart, new Timestamp(fromDate.getTime()),
					new Timestamp(toDate.getTime()))
			).leftOuterJoin(
				DSL.table(
					"BQIdentityInterestScore"
				).as(
					"IdentityInterestScore"
				)
			).on(
				conditions.toArray(new Condition[0])
			).groupBy(
				periodField
			).orderBy(
				periodField
			));
	}

	@Override
	public BQIdentityInterestScore insert(
		BQIdentityInterestScore bqIdentityInterestScore) {

		_queryExecutor.queryExecute(
			_dslContext.insertInto(
				DSL.table("BQIdentityInterestScore")
			).columns(
				DSL.field("channelId"), DSL.field("identityId"),
				DSL.field("interested", Boolean.class),
				DSL.field("interestScore", Double.class), DSL.field("keyword"),
				DSL.field("recordedDate", Object.class)
			).values(
				bqIdentityInterestScore.getChannelId(),
				bqIdentityInterestScore.getIdentityId(),
				bqIdentityInterestScore.getInterested(),
				bqIdentityInterestScore.getInterestScore(),
				bqIdentityInterestScore.getKeyword(),
				DateUtil.toUTCString(
					bqIdentityInterestScore.getRecordedDate(),
					DateUtil.PATTERN_SHORT)
			));

		return bqIdentityInterestScore;
	}

	@Override
	public void insertAll(
		List<BQIdentityInterestScore> bqIdentityInterestScores) {

		InsertValuesStep6<Record, Long, String, Boolean, Double, Object, Object>
			insertValuesStep6 = _dslContext.insertInto(
				DSL.table("BQIdentityInterestScore")
			).columns(
				DSL.field("channelId", Long.class),
				DSL.field("identityId", String.class),
				DSL.field("interested", Boolean.class),
				DSL.field("interestScore", Double.class), DSL.field("keyword"),
				DSL.field("recordedDate", Object.class)
			);

		for (BQIdentityInterestScore bqIdentityInterestScore :
				bqIdentityInterestScores) {

			insertValuesStep6 = insertValuesStep6.values(
				bqIdentityInterestScore.getChannelId(),
				bqIdentityInterestScore.getIdentityId(),
				bqIdentityInterestScore.getInterested(),
				bqIdentityInterestScore.getInterestScore(),
				bqIdentityInterestScore.getKeyword(),
				DateUtil.toUTCString(
					bqIdentityInterestScore.getRecordedDate(),
					DateUtil.PATTERN_SHORT));
		}

		_queryExecutor.queryExecute(insertValuesStep6);
	}

	private double _getAggregationValue(BigDecimal value) {
		if ((value == null) || (value.doubleValue() < 0)) {
			return 0;
		}

		return value.doubleValue();
	}

	private Condition _getBQIdentityCondition(@Nullable Long channelId) {
		if (channelId == null) {
			return DSL.noCondition();
		}

		return DSL.field(
			"IdentityActivityOverview.channelId"
		).eq(
			channelId
		);
	}

	private <T extends Record> SelectJoinStep<T> _getBQIdentitySelectJoinStep(
		List<String> individualIds, SelectJoinStep<T> selectJoinStep) {

		if ((individualIds != null) && !individualIds.isEmpty()) {
			selectJoinStep = selectJoinStep.join(
				DSL.table(
					"BQIdentity"
				).as(
					"Identity"
				)
			).on(
				DSL.field(
					"IdentityInterestScore.identityId"
				).eq(
					DSL.field("Identity.id")
				)
			);
		}

		return selectJoinStep;
	}

	private List<Condition> _getConditions(
		@Nullable Long channelId, @Nullable Boolean interested,
		@Nullable List<String> individualIds, @Nullable String keywords,
		@Nullable Date recordedDate) {

		List<Condition> conditions = new ArrayList<>();

		if (channelId != null) {
			conditions.add(
				DSL.field(
					"IdentityInterestScore.channelId", Long.class
				).eq(
					channelId
				));
		}

		if ((individualIds != null) && !individualIds.isEmpty()) {
			conditions.add(
				DSL.field(
					"individualId", String.class
				).in(
					individualIds
				));
		}

		if (interested != null) {
			conditions.add(
				DSL.field(
					"IdentityInterestScore.interested", Boolean.class
				).eq(
					interested
				));
		}

		if (StringUtils.isNotBlank(keywords)) {
			conditions.add(
				_dslHelper.containsSubstring(
					"IdentityInterestScore.keyword", keywords));
		}

		if (recordedDate != null) {
			conditions.add(
				DSL.field(
					"recordedDate"
				).eq(
					DateUtil.toUTCString(recordedDate, DateUtil.PATTERN_SHORT)
				));
		}
		else {
			conditions.add(
				DSL.field(
					"recordedDate"
				).eq(
					_getMaxRecordedDateSelect(channelId)
				));
		}

		return conditions;
	}

	private List<Condition> _getConditions(
		Long channelId, String individualId) {

		List<Condition> conditions = new ArrayList<>();

		if (channelId != null) {
			conditions.add(
				DSL.field(
					"IdentityInterestScore.channelId", Long.class
				).eq(
					channelId
				));
		}

		conditions.add(
			DSL.field(
				"Identity.individualId"
			).eq(
				individualId
			));

		conditions.add(
			DSL.field(
				"recordedDate"
			).eq(
				_getMaxRecordedDateSelect(channelId)
			));

		return conditions;
	}

	private Select<?> _getIdentityActivityOverview(
		boolean active, @Nullable Long segmentId) {

		Field<Object> channelIdField = DSL.field(
			"BQIdentityActivity.channelId");
		Field<Object> identityIdField = DSL.field(
			"BQIdentityActivity.identityId");

		SelectJoinStep<Record2<Object, Object>> selectJoinStep = DSL.select(
			channelIdField, identityIdField
		).from(
			"BQIdentityActivity"
		);

		if (segmentId != null) {
			selectJoinStep = selectJoinStep.join(
				"BQMembership"
			).on(
				DSL.and(
					DSL.field(
						"BQIdentityActivity.identityId"
					).eq(
						DSL.field("BQMembership.identityId")
					),
					DSL.field(
						"BQMembership.segmentId"
					).eq(
						segmentId
					))
			);
		}

		if (active) {
			LocalDateTime newDayLocalDateTime = DateUtil.newDayLocalDateTime(
				TimeZoneDogUtil.getZoneId());

			return selectJoinStep.where(
				DSL.field(
					"BQIdentityActivity.lastActivityDate"
				).ge(
					DateUtil.toUTCString(
						DateUtil.toUTCDate(newDayLocalDateTime.minusDays(30)),
						DateUtil.PATTERN_ISO_8601)
				)
			).groupBy(
				channelIdField, identityIdField
			);
		}

		return selectJoinStep.groupBy(channelIdField, identityIdField);
	}

	private SelectConditionStep<Record3<Object, Object, Integer>>
		_getIdentityOverview(Long channelId) {

		WindowPartitionByStep<Integer> windowPartitionByStep =
			DSL.countDistinct(
				DSL.coalesce(
					DSL.field("Identity.individualId"),
					DSL.field("Identity.id"))
			).over();

		return DSL.select(
			DSL.field(
				"Identity.id"
			).as(
				"identityId"
			),
			DSL.field(
				"Identity.individualId"
			).as(
				"individualId"
			),
			windowPartitionByStep.as("totalCount")
		).from(
			DSL.table(
				"BQIdentity"
			).as(
				"Identity"
			)
		).join(
			DSL.table(
				"IdentityActivityOverview"
			).as(
				"IdentityActivityOverview"
			)
		).on(
			DSL.field(
				"Identity.id"
			).eq(
				DSL.field("IdentityActivityOverview.identityId")
			)
		).where(
			_getBQIdentityCondition(channelId)
		);
	}

	private Select<Record1<Object>> _getMaxRecordedDateSelect(Long channelId) {
		SelectJoinStep<Record1<Object>> selectJoinStep = _dslContext.select(
			DSL.max(DSL.field("recordedDate"))
		).from(
			"BQIdentityInterestScore"
		);

		if (channelId == null) {
			return selectJoinStep;
		}

		return selectJoinStep.where(
			DSL.field(
				"channelId"
			).eq(
				channelId
			));
	}

	private Field _getPeriodField(String period) {
		if (!_validPeriods.containsKey(period)) {
			throw new IllegalArgumentException("Invalid period: " + period);
		}

		Field field = _dslHelper.dateTrunc(
			_validPeriods.get(period),
			DSL.field("generatedDate", OffsetDateTime.class));

		return field.as("intervalInitDate");
	}

	private Collection<SortField<?>> _getSortFields(Sort sort, Table<?> table) {
		List<Sort.Order> orders = new ArrayList<>();

		boolean orderedByKeyword = false;

		for (Sort.Order order : sort.toList()) {
			String fieldName = _fieldNames.getOrDefault(
				order.getProperty(), order.getProperty());

			if (fieldName.equals("keyword")) {
				fieldName = "LOWER(keyword)";
				orderedByKeyword = true;
			}

			orders.add(new Sort.Order(order.getDirection(), fieldName));
		}

		if (!orderedByKeyword) {
			orders.add(new Sort.Order(Sort.Direction.ASC, "LOWER(keyword)"));
		}

		return getSortFields(null, Sort.by(orders), table);
	}

	private final DSLContext _dslContext;

	@Autowired
	private DSLHelper _dslHelper;

	private final Map<String, String> _fieldNames;

	@Autowired
	private QueryExecutor _queryExecutor;

	private final Map<String, DatePart> _validPeriods =
		new HashMap<String, DatePart>() {
			{
				put("day", DatePart.DAY);
				put("hour", DatePart.HOUR);
				put("month", DatePart.MONTH);
				put("week", DatePart.WEEK);
			}
		};

}