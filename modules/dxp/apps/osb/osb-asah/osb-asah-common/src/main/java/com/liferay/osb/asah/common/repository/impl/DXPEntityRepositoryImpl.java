/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.impl;

import com.liferay.osb.asah.common.entity.DXPEntity;
import com.liferay.osb.asah.common.repository.CustomDXPEntityRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.SelectConditionStep;
import org.jooq.SelectSelectStep;
import org.jooq.SortField;
import org.jooq.impl.DSL;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;

/**
 * @author Marcos Martins
 * @author Alejo Ceballos
 */
public class DXPEntityRepositoryImpl
	extends BaseRepository implements CustomDXPEntityRepository {

	public DXPEntityRepositoryImpl(DSLContext dslContext) {
		_dslContext = dslContext;
	}

	@Override
	public long count() {
		throw new UnsupportedOperationException();
	}

	@Override
	public long countByDataSourceIdsAndKeywordsAndType(
		List<Long> dataSourceIds, @Nullable String keywords,
		DXPEntity.Type type) {

		SelectSelectStep<Record1<Integer>> selectSelectStep =
			_dslContext.selectCount();

		return selectSelectStep.from(
			"DXPEntity"
		).where(
			_getConditions(dataSourceIds, keywords, type)
		).fetchOptional(
			0, Long.class
		).orElse(
			0L
		);
	}

	@Override
	public long countByModifiedDateBetweenAndType(
		Long dataSourceId, @Nullable Date modifiedDate1, Date modifiedDate2,
		DXPEntity.Type type) {

		SelectSelectStep<Record1<Integer>> selectSelectStep =
			_dslContext.selectCount();

		SelectConditionStep<Record1<Integer>> selectConditionStep =
			selectSelectStep.from(
				"DXPEntity"
			).where(
				DSL.field(
					"dataSourceId"
				).eq(
					dataSourceId
				),
				DSL.field(
					"type"
				).eq(
					type.toString()
				)
			);

		if (modifiedDate1 != null) {
			selectConditionStep = selectConditionStep.and(
				DSL.field(
					"modifiedDate"
				).ge(
					modifiedDate1
				));
		}

		selectConditionStep = selectConditionStep.and(
			DSL.field(
				"modifiedDate"
			).le(
				modifiedDate2
			));

		return selectConditionStep.fetchOptional(
			0, Long.class
		).orElse(
			0L
		);
	}

	@Override
	public void delete(DXPEntity dxpEntity) {
		if (dxpEntity.getId() == null) {
			return;
		}

		_dslContext.delete(
			DSL.table("DXPEntity")
		).where(
			DSL.field(
				"id"
			).eq(
				dxpEntity.getId()
			)
		).execute();
	}

	@Override
	public void deleteAll(Iterable<? extends DXPEntity> dxpEntities) {
		dxpEntities.forEach(this::delete);
	}

	@Override
	public void deleteByFieldNameAndFieldValueAndType(
		String fieldName, Object fieldValue, DXPEntity.Type type) {

		_dslContext.delete(
			DSL.table("DXPEntity")
		).where(
			DSL.field(
				"type"
			).eq(
				type.toString()
			)
		).and(
			_createCondition(fieldName, fieldValue)
		).execute();
	}

	@Override
	public void deleteById(Long id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteByType(DXPEntity.Type type) {
		_dslContext.delete(
			DSL.table("DXPEntity")
		).where(
			DSL.field(
				"type"
			).eq(
				type.toString()
			)
		).execute();
	}

	@Override
	public boolean existsById(Long id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterable<DXPEntity> findAll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Page<DXPEntity> findAll(Pageable pageable) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterable<DXPEntity> findAll(Sort sort) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterable<DXPEntity> findAllById(Iterable<Long> ids) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<DXPEntity> findByAfterAndFieldsAndType(
		@Nullable Long after, Map<String, Object> fields, int size,
		DXPEntity.Type type) {

		SelectSelectStep<Record> selectSelectStep = _dslContext.select();

		SelectConditionStep<Record> selectConditionStep = selectSelectStep.from(
			"DXPEntity"
		).where(
			DSL.field(
				"type"
			).eq(
				type.toString()
			)
		);

		for (Map.Entry<String, Object> field : fields.entrySet()) {
			selectConditionStep.and(
				_createCondition(field.getKey(), field.getValue()));
		}

		if (after != null) {
			selectConditionStep.and(
				DSL.field(
					"id"
				).greaterThan(
					after
				));
		}

		if (size > 0) {
			selectConditionStep.limit(size);
		}

		selectConditionStep.orderBy(DSL.field("id"));

		return selectConditionStep.fetch(
			record -> new DXPEntity(record.intoMap()));
	}

	@Override
	public List<DXPEntity> findByFieldsAndType(
		Map<String, Object> fields, DXPEntity.Type type) {

		SelectSelectStep<Record> selectSelectStep = _dslContext.select();

		SelectConditionStep<Record> selectConditionStep = selectSelectStep.from(
			"DXPEntity"
		).where(
			DSL.field(
				"type"
			).eq(
				type.toString()
			)
		);

		for (Map.Entry<String, Object> field : fields.entrySet()) {
			selectConditionStep.and(
				_createCondition(field.getKey(), field.getValue()));
		}

		selectConditionStep.orderBy(DSL.field("id"));

		return selectConditionStep.fetch(
			record -> new DXPEntity(record.intoMap()));
	}

	@Override
	public List<DXPEntity> findByMembershipClassNameAndMembershipId(
		String memebershipClassName, Long membershipId) {

		SelectSelectStep<Record> selectSelectStep = _dslContext.select();

		return selectSelectStep.from(
			"DXPEntity"
		).where(
			DSL.field(
				"type"
			).eq(
				DXPEntity.Type.USER.toString()
			)
		).and(
			DSL.field(
				"fields->'memberships'->>{0}", String.class,
				memebershipClassName
			).contains(
				String.valueOf(membershipId)
			)
		).fetch(
			record -> new DXPEntity(record.intoMap())
		);
	}

	@Override
	public List<DXPEntity> findByModifiedDateBetweenAndType(
		Long dataSourceId, @Nullable Date modifiedDate1, Date modifiedDate2,
		DXPEntity.Type type, Pageable pageable) {

		SelectSelectStep<Record> selectSelectStep = _dslContext.select();

		SelectConditionStep<Record> selectConditionStep = selectSelectStep.from(
			"DXPEntity"
		).where(
			DSL.field(
				"dataSourceId"
			).eq(
				dataSourceId
			),
			DSL.field(
				"type"
			).eq(
				type.toString()
			)
		);

		if (modifiedDate1 != null) {
			selectConditionStep = selectConditionStep.and(
				DSL.field(
					"modifiedDate"
				).ge(
					modifiedDate1
				));
		}

		selectConditionStep = selectConditionStep.and(
			DSL.field(
				"modifiedDate"
			).le(
				modifiedDate2
			));

		return selectConditionStep.orderBy(
			getSortFields(pageable.getSort(), null)
		).limit(
			pageable.getPageSize()
		).offset(
			pageable.getOffset()
		).fetch(
			record -> new DXPEntity(record.intoMap())
		);
	}

	@Override
	public List<DXPEntity> searchByDataSourceIdsAndKeywordsAndType(
		List<Long> dataSourceIds, @Nullable String keywords,
		DXPEntity.Type type, Pageable pageable) {

		SelectSelectStep<Record> selectSelectStep = _dslContext.select();

		return selectSelectStep.from(
			"DXPEntity"
		).where(
			_getConditions(dataSourceIds, keywords, type)
		).orderBy(
			_getSortFields(pageable.getSort(), type)
		).limit(
			pageable.getPageSize()
		).offset(
			pageable.getOffset()
		).fetch(
			record -> new DXPEntity(record.intoMap())
		);
	}

	private Condition _createCondition(String fieldKey, Object fieldValue) {
		Field<Object> field = DSL.field(_createFieldPath(fieldKey));

		if (StringUtils.startsWith(fieldKey, "fields.memberships.")) {
			return field.contains(fieldValue);
		}

		if (fieldValue instanceof Collection) {
			Collection<?> collection = (Collection<?>)fieldValue;

			Iterator<?> iterator = collection.iterator();

			Object element = iterator.next();

			if (element instanceof Integer || element instanceof Long) {
				Field<Long> longField = field.cast(Long.class);

				return longField.in(collection);
			}

			return field.in(collection);
		}

		Field<String> castField = field.cast(String.class);

		return castField.eq(DSL.cast(fieldValue, String.class));
	}

	private String _createFieldPath(String fieldName) {
		String actualFieldName = fieldName;
		String membershipField = null;

		if (StringUtils.startsWith(fieldName, "fields.memberships.")) {
			actualFieldName = "fields.memberships.%s";
			membershipField =
				StringUtils.splitByWholeSeparator(
					fieldName, "fields.memberships.")[0];
		}

		String[] keys = StringUtils.split(actualFieldName, '.');

		for (int idx = 1; idx < keys.length; idx++) {
			if (idx == (keys.length - 1)) {
				keys[idx] = "->>'" + keys[idx] + "'";
			}
			else {
				keys[idx] = "->'" + keys[idx] + "'";
			}
		}

		if (membershipField != null) {
			return String.format(String.join("", keys), membershipField);
		}

		return String.join("", keys);
	}

	private List<Condition> _getConditions(
		List<Long> dataSourceIds, String keywords, DXPEntity.Type type) {

		List<Condition> conditions = new ArrayList<>();

		conditions.add(
			DSL.field(
				"type"
			).eq(
				type.toString()
			));

		if (dataSourceIds == null) {
			if (type.isUser()) {
				conditions.add(
					DSL.or(
						DSL.field(
							"fields->>'firstName'", String.class
						).contains(
							keywords
						),
						DSL.field(
							"fields->>'lastName'", String.class
						).contains(
							keywords
						)));

				return conditions;
			}

			conditions.add(
				DSL.field(
					"fields->>'name'", String.class
				).contains(
					keywords
				));

			return conditions;
		}

		if ((type.isGroup() || type.isOrganization() || type.isRole() ||
			 type.isTeam() || type.isUserGroup()) &&
			StringUtils.isNotBlank(keywords)) {

			conditions.add(
				DSL.field(
					"fields->>'name'", String.class
				).contains(
					keywords
				));
		}
		else if (type.isUser() && StringUtils.isNotBlank(keywords)) {
			conditions.add(
				DSL.or(
					DSL.field(
						"fields->>'firstName'", String.class
					).containsIgnoreCase(
						keywords
					),
					DSL.field(
						"fields->>'lastName'", String.class
					).containsIgnoreCase(
						keywords
					)));
		}

		if (dataSourceIds.isEmpty()) {
			return conditions;
		}

		conditions.add(
			DSL.field(
				"dataSourceId"
			).in(
				dataSourceIds
			));

		return conditions;
	}

	private Collection<SortField<?>> _getSortFields(
		Sort sort, DXPEntity.Type type) {

		List<Sort.Order> orders = new ArrayList<>();

		for (Sort.Order order : sort) {
			String property = order.getProperty();

			if ((type == DXPEntity.Type.USER) &&
				StringUtils.endsWith(property, "name")) {

				orders.add(
					new Sort.Order(
						order.getDirection(),
						_createFieldPath("fields.firstName")));
				orders.add(
					new Sort.Order(
						order.getDirection(),
						_createFieldPath("fields.lastName")));

				continue;
			}

			if (!StringUtils.equalsIgnoreCase(property, "dataSourceName") &&
				!StringUtils.startsWith(property, "fields.")) {

				property = "fields." + property;
			}

			orders.add(
				new Sort.Order(
					order.getDirection(), _createFieldPath(property)));
		}

		return getSortFields(Sort.by(orders), null);
	}

	private final DSLContext _dslContext;

}