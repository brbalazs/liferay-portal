/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.commerce.pricing.service.persistence.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.commerce.pricing.exception.NoSuchPricingClassRelException;
import com.liferay.commerce.pricing.model.CommercePricingClassRel;
import com.liferay.commerce.pricing.service.CommercePricingClassRelLocalServiceUtil;
import com.liferay.commerce.pricing.service.persistence.CommercePricingClassRelPersistence;
import com.liferay.commerce.pricing.service.persistence.CommercePricingClassRelUtil;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ProjectionFactoryUtil;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.util.IntegerWrapper;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.OrderByComparatorFactoryUtil;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PersistenceTestRule;
import com.liferay.portal.test.rule.TransactionalTestRule;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @generated
 */
@RunWith(Arquillian.class)
public class CommercePricingClassRelPersistenceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(), PersistenceTestRule.INSTANCE,
			new TransactionalTestRule(
				Propagation.REQUIRED, "com.liferay.commerce.pricing.service"));

	@Before
	public void setUp() {
		_persistence = CommercePricingClassRelUtil.getPersistence();

		Class<?> clazz = _persistence.getClass();

		_dynamicQueryClassLoader = clazz.getClassLoader();
	}

	@After
	public void tearDown() throws Exception {
		Iterator<CommercePricingClassRel> iterator =
			_commercePricingClassRels.iterator();

		while (iterator.hasNext()) {
			_persistence.remove(iterator.next());

			iterator.remove();
		}
	}

	@Test
	public void testCreate() throws Exception {
		long pk = RandomTestUtil.nextLong();

		CommercePricingClassRel commercePricingClassRel = _persistence.create(
			pk);

		Assert.assertNotNull(commercePricingClassRel);

		Assert.assertEquals(commercePricingClassRel.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		CommercePricingClassRel newCommercePricingClassRel =
			addCommercePricingClassRel();

		_persistence.remove(newCommercePricingClassRel);

		CommercePricingClassRel existingCommercePricingClassRel =
			_persistence.fetchByPrimaryKey(
				newCommercePricingClassRel.getPrimaryKey());

		Assert.assertNull(existingCommercePricingClassRel);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addCommercePricingClassRel();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = RandomTestUtil.nextLong();

		CommercePricingClassRel newCommercePricingClassRel =
			_persistence.create(pk);

		newCommercePricingClassRel.setCompanyId(RandomTestUtil.nextLong());

		newCommercePricingClassRel.setUserId(RandomTestUtil.nextLong());

		newCommercePricingClassRel.setUserName(RandomTestUtil.randomString());

		newCommercePricingClassRel.setCreateDate(RandomTestUtil.nextDate());

		newCommercePricingClassRel.setModifiedDate(RandomTestUtil.nextDate());

		newCommercePricingClassRel.setCommercePricingClassId(
			RandomTestUtil.nextLong());

		newCommercePricingClassRel.setClassNameId(RandomTestUtil.nextLong());

		newCommercePricingClassRel.setClassPK(RandomTestUtil.nextLong());

		_commercePricingClassRels.add(
			_persistence.update(newCommercePricingClassRel));

		CommercePricingClassRel existingCommercePricingClassRel =
			_persistence.findByPrimaryKey(
				newCommercePricingClassRel.getPrimaryKey());

		Assert.assertEquals(
			existingCommercePricingClassRel.getCommercePricingClassRelId(),
			newCommercePricingClassRel.getCommercePricingClassRelId());
		Assert.assertEquals(
			existingCommercePricingClassRel.getCompanyId(),
			newCommercePricingClassRel.getCompanyId());
		Assert.assertEquals(
			existingCommercePricingClassRel.getUserId(),
			newCommercePricingClassRel.getUserId());
		Assert.assertEquals(
			existingCommercePricingClassRel.getUserName(),
			newCommercePricingClassRel.getUserName());
		Assert.assertEquals(
			Time.getShortTimestamp(
				existingCommercePricingClassRel.getCreateDate()),
			Time.getShortTimestamp(newCommercePricingClassRel.getCreateDate()));
		Assert.assertEquals(
			Time.getShortTimestamp(
				existingCommercePricingClassRel.getModifiedDate()),
			Time.getShortTimestamp(
				newCommercePricingClassRel.getModifiedDate()));
		Assert.assertEquals(
			existingCommercePricingClassRel.getCommercePricingClassId(),
			newCommercePricingClassRel.getCommercePricingClassId());
		Assert.assertEquals(
			existingCommercePricingClassRel.getClassNameId(),
			newCommercePricingClassRel.getClassNameId());
		Assert.assertEquals(
			existingCommercePricingClassRel.getClassPK(),
			newCommercePricingClassRel.getClassPK());
	}

	@Test
	public void testCountByCommercePricingClassId() throws Exception {
		_persistence.countByCommercePricingClassId(RandomTestUtil.nextLong());

		_persistence.countByCommercePricingClassId(0L);
	}

	@Test
	public void testCountByCPC_CN() throws Exception {
		_persistence.countByCPC_CN(
			RandomTestUtil.nextLong(), RandomTestUtil.nextLong());

		_persistence.countByCPC_CN(0L, 0L);
	}

	@Test
	public void testCountByCN_CPK() throws Exception {
		_persistence.countByCN_CPK(
			RandomTestUtil.nextLong(), RandomTestUtil.nextLong());

		_persistence.countByCN_CPK(0L, 0L);
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		CommercePricingClassRel newCommercePricingClassRel =
			addCommercePricingClassRel();

		CommercePricingClassRel existingCommercePricingClassRel =
			_persistence.findByPrimaryKey(
				newCommercePricingClassRel.getPrimaryKey());

		Assert.assertEquals(
			existingCommercePricingClassRel, newCommercePricingClassRel);
	}

	@Test(expected = NoSuchPricingClassRelException.class)
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		_persistence.findByPrimaryKey(pk);
	}

	@Test
	public void testFindAll() throws Exception {
		_persistence.findAll(
			QueryUtil.ALL_POS, QueryUtil.ALL_POS, getOrderByComparator());
	}

	protected OrderByComparator<CommercePricingClassRel>
		getOrderByComparator() {

		return OrderByComparatorFactoryUtil.create(
			"CommercePricingClassRel", "commercePricingClassRelId", true,
			"companyId", true, "userId", true, "userName", true, "createDate",
			true, "modifiedDate", true, "commercePricingClassId", true,
			"classNameId", true, "classPK", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		CommercePricingClassRel newCommercePricingClassRel =
			addCommercePricingClassRel();

		CommercePricingClassRel existingCommercePricingClassRel =
			_persistence.fetchByPrimaryKey(
				newCommercePricingClassRel.getPrimaryKey());

		Assert.assertEquals(
			existingCommercePricingClassRel, newCommercePricingClassRel);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		CommercePricingClassRel missingCommercePricingClassRel =
			_persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingCommercePricingClassRel);
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereAllPrimaryKeysExist()
		throws Exception {

		CommercePricingClassRel newCommercePricingClassRel1 =
			addCommercePricingClassRel();
		CommercePricingClassRel newCommercePricingClassRel2 =
			addCommercePricingClassRel();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newCommercePricingClassRel1.getPrimaryKey());
		primaryKeys.add(newCommercePricingClassRel2.getPrimaryKey());

		Map<Serializable, CommercePricingClassRel> commercePricingClassRels =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(2, commercePricingClassRels.size());
		Assert.assertEquals(
			newCommercePricingClassRel1,
			commercePricingClassRels.get(
				newCommercePricingClassRel1.getPrimaryKey()));
		Assert.assertEquals(
			newCommercePricingClassRel2,
			commercePricingClassRels.get(
				newCommercePricingClassRel2.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereNoPrimaryKeysExist()
		throws Exception {

		long pk1 = RandomTestUtil.nextLong();

		long pk2 = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(pk1);
		primaryKeys.add(pk2);

		Map<Serializable, CommercePricingClassRel> commercePricingClassRels =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertTrue(commercePricingClassRels.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereSomePrimaryKeysExist()
		throws Exception {

		CommercePricingClassRel newCommercePricingClassRel =
			addCommercePricingClassRel();

		long pk = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newCommercePricingClassRel.getPrimaryKey());
		primaryKeys.add(pk);

		Map<Serializable, CommercePricingClassRel> commercePricingClassRels =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(1, commercePricingClassRels.size());
		Assert.assertEquals(
			newCommercePricingClassRel,
			commercePricingClassRels.get(
				newCommercePricingClassRel.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithNoPrimaryKeys() throws Exception {
		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		Map<Serializable, CommercePricingClassRel> commercePricingClassRels =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertTrue(commercePricingClassRels.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithOnePrimaryKey() throws Exception {
		CommercePricingClassRel newCommercePricingClassRel =
			addCommercePricingClassRel();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newCommercePricingClassRel.getPrimaryKey());

		Map<Serializable, CommercePricingClassRel> commercePricingClassRels =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(1, commercePricingClassRels.size());
		Assert.assertEquals(
			newCommercePricingClassRel,
			commercePricingClassRels.get(
				newCommercePricingClassRel.getPrimaryKey()));
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery =
			CommercePricingClassRelLocalServiceUtil.getActionableDynamicQuery();

		actionableDynamicQuery.setPerformActionMethod(
			new ActionableDynamicQuery.PerformActionMethod
				<CommercePricingClassRel>() {

				@Override
				public void performAction(
					CommercePricingClassRel commercePricingClassRel) {

					Assert.assertNotNull(commercePricingClassRel);

					count.increment();
				}

			});

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting() throws Exception {
		CommercePricingClassRel newCommercePricingClassRel =
			addCommercePricingClassRel();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			CommercePricingClassRel.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq(
				"commercePricingClassRelId",
				newCommercePricingClassRel.getCommercePricingClassRelId()));

		List<CommercePricingClassRel> result =
			_persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		CommercePricingClassRel existingCommercePricingClassRel = result.get(0);

		Assert.assertEquals(
			existingCommercePricingClassRel, newCommercePricingClassRel);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			CommercePricingClassRel.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq(
				"commercePricingClassRelId", RandomTestUtil.nextLong()));

		List<CommercePricingClassRel> result =
			_persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting() throws Exception {
		CommercePricingClassRel newCommercePricingClassRel =
			addCommercePricingClassRel();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			CommercePricingClassRel.class, _dynamicQueryClassLoader);

		dynamicQuery.setProjection(
			ProjectionFactoryUtil.property("commercePricingClassRelId"));

		Object newCommercePricingClassRelId =
			newCommercePricingClassRel.getCommercePricingClassRelId();

		dynamicQuery.add(
			RestrictionsFactoryUtil.in(
				"commercePricingClassRelId",
				new Object[] {newCommercePricingClassRelId}));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingCommercePricingClassRelId = result.get(0);

		Assert.assertEquals(
			existingCommercePricingClassRelId, newCommercePricingClassRelId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			CommercePricingClassRel.class, _dynamicQueryClassLoader);

		dynamicQuery.setProjection(
			ProjectionFactoryUtil.property("commercePricingClassRelId"));

		dynamicQuery.add(
			RestrictionsFactoryUtil.in(
				"commercePricingClassRelId",
				new Object[] {RandomTestUtil.nextLong()}));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	protected CommercePricingClassRel addCommercePricingClassRel()
		throws Exception {

		long pk = RandomTestUtil.nextLong();

		CommercePricingClassRel commercePricingClassRel = _persistence.create(
			pk);

		commercePricingClassRel.setCompanyId(RandomTestUtil.nextLong());

		commercePricingClassRel.setUserId(RandomTestUtil.nextLong());

		commercePricingClassRel.setUserName(RandomTestUtil.randomString());

		commercePricingClassRel.setCreateDate(RandomTestUtil.nextDate());

		commercePricingClassRel.setModifiedDate(RandomTestUtil.nextDate());

		commercePricingClassRel.setCommercePricingClassId(
			RandomTestUtil.nextLong());

		commercePricingClassRel.setClassNameId(RandomTestUtil.nextLong());

		commercePricingClassRel.setClassPK(RandomTestUtil.nextLong());

		_commercePricingClassRels.add(
			_persistence.update(commercePricingClassRel));

		return commercePricingClassRel;
	}

	private List<CommercePricingClassRel> _commercePricingClassRels =
		new ArrayList<CommercePricingClassRel>();
	private CommercePricingClassRelPersistence _persistence;
	private ClassLoader _dynamicQueryClassLoader;

}