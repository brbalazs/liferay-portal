/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.executor;

import java.math.BigDecimal;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import org.jooq.Query;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.SelectFinalStep;

/**
 * @author Matthew Kong
 */
public interface QueryExecutor {

	public void queryExecute(Query query);

	public void queryExecute(String queryString);

	public boolean queryExists(
		SelectFinalStep<? extends Record> selectFinalStep);

	public BigDecimal queryForBigDecimal(
		SelectFinalStep<Record1<Number>> selectFinalStep);

	public <T> List<T> queryForList(
		Function<Map<String, Object>, T> rowMapperFunction,
		SelectFinalStep<? extends Record> selectFinalStep);

	public long queryForLong(SelectFinalStep<Record1<Integer>> selectFinalStep);

	public <T, R> Map<T, R> queryForMap(
		Function<Object, T> keyMapperFunction,
		SelectFinalStep<? extends Record> selectFinalStep,
		Function<Object, R> valueMapperFunction);

	public Map<String, Object> queryForMap(
		SelectFinalStep<? extends Record> selectFinalStep);

	public <T> Optional<T> queryForObject(
		Function<Map<String, Object>, T> rowMapperFunction,
		SelectFinalStep<? extends Record> selectFinalStep);

	public <T> Set<T> queryForSet(
		Function<Map<String, Object>, T> rowMapperFunction,
		SelectFinalStep<? extends Record> selectFinalStep);

}