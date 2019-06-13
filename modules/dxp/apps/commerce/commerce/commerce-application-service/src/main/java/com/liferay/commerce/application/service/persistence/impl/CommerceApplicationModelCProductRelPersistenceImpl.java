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

package com.liferay.commerce.application.service.persistence.impl;

import aQute.bnd.annotation.ProviderType;

import com.liferay.commerce.application.exception.NoSuchApplicationModelCProductRelException;
import com.liferay.commerce.application.model.CommerceApplicationModelCProductRel;
import com.liferay.commerce.application.model.impl.CommerceApplicationModelCProductRelImpl;
import com.liferay.commerce.application.model.impl.CommerceApplicationModelCProductRelModelImpl;
import com.liferay.commerce.application.service.persistence.CommerceApplicationModelCProductRelPersistence;

import com.liferay.portal.kernel.dao.orm.EntityCache;
import com.liferay.portal.kernel.dao.orm.FinderCache;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.persistence.CompanyProvider;
import com.liferay.portal.kernel.service.persistence.CompanyProviderWrapper;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.spring.extender.service.ServiceReference;

import java.io.Serializable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The persistence implementation for the commerce application model c product rel service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Luca Pellizzon
 * @see CommerceApplicationModelCProductRelPersistence
 * @see com.liferay.commerce.application.service.persistence.CommerceApplicationModelCProductRelUtil
 * @generated
 */
@ProviderType
public class CommerceApplicationModelCProductRelPersistenceImpl
	extends BasePersistenceImpl<CommerceApplicationModelCProductRel>
	implements CommerceApplicationModelCProductRelPersistence {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link CommerceApplicationModelCProductRelUtil} to access the commerce application model c product rel persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY = CommerceApplicationModelCProductRelImpl.class.getName();
	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List1";
	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List2";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_ALL = new FinderPath(CommerceApplicationModelCProductRelModelImpl.ENTITY_CACHE_ENABLED,
			CommerceApplicationModelCProductRelModelImpl.FINDER_CACHE_ENABLED,
			CommerceApplicationModelCProductRelImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL = new FinderPath(CommerceApplicationModelCProductRelModelImpl.ENTITY_CACHE_ENABLED,
			CommerceApplicationModelCProductRelModelImpl.FINDER_CACHE_ENABLED,
			CommerceApplicationModelCProductRelImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(CommerceApplicationModelCProductRelModelImpl.ENTITY_CACHE_ENABLED,
			CommerceApplicationModelCProductRelModelImpl.FINDER_CACHE_ENABLED,
			Long.class, FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll",
			new String[0]);

	public CommerceApplicationModelCProductRelPersistenceImpl() {
		setModelClass(CommerceApplicationModelCProductRel.class);

		try {
			Field field = BasePersistenceImpl.class.getDeclaredField(
					"_dbColumnNames");

			field.setAccessible(true);

			Map<String, String> dbColumnNames = new HashMap<String, String>();

			dbColumnNames.put("commerceApplicationModelCProductRelId",
				"CAModelCProductRelId");

			field.set(this, dbColumnNames);
		}
		catch (Exception e) {
			if (_log.isDebugEnabled()) {
				_log.debug(e, e);
			}
		}
	}

	/**
	 * Caches the commerce application model c product rel in the entity cache if it is enabled.
	 *
	 * @param commerceApplicationModelCProductRel the commerce application model c product rel
	 */
	@Override
	public void cacheResult(
		CommerceApplicationModelCProductRel commerceApplicationModelCProductRel) {
		entityCache.putResult(CommerceApplicationModelCProductRelModelImpl.ENTITY_CACHE_ENABLED,
			CommerceApplicationModelCProductRelImpl.class,
			commerceApplicationModelCProductRel.getPrimaryKey(),
			commerceApplicationModelCProductRel);

		commerceApplicationModelCProductRel.resetOriginalValues();
	}

	/**
	 * Caches the commerce application model c product rels in the entity cache if it is enabled.
	 *
	 * @param commerceApplicationModelCProductRels the commerce application model c product rels
	 */
	@Override
	public void cacheResult(
		List<CommerceApplicationModelCProductRel> commerceApplicationModelCProductRels) {
		for (CommerceApplicationModelCProductRel commerceApplicationModelCProductRel : commerceApplicationModelCProductRels) {
			if (entityCache.getResult(
						CommerceApplicationModelCProductRelModelImpl.ENTITY_CACHE_ENABLED,
						CommerceApplicationModelCProductRelImpl.class,
						commerceApplicationModelCProductRel.getPrimaryKey()) == null) {
				cacheResult(commerceApplicationModelCProductRel);
			}
			else {
				commerceApplicationModelCProductRel.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all commerce application model c product rels.
	 *
	 * <p>
	 * The {@link EntityCache} and {@link FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		entityCache.clearCache(CommerceApplicationModelCProductRelImpl.class);

		finderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the commerce application model c product rel.
	 *
	 * <p>
	 * The {@link EntityCache} and {@link FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(
		CommerceApplicationModelCProductRel commerceApplicationModelCProductRel) {
		entityCache.removeResult(CommerceApplicationModelCProductRelModelImpl.ENTITY_CACHE_ENABLED,
			CommerceApplicationModelCProductRelImpl.class,
			commerceApplicationModelCProductRel.getPrimaryKey());

		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@Override
	public void clearCache(
		List<CommerceApplicationModelCProductRel> commerceApplicationModelCProductRels) {
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (CommerceApplicationModelCProductRel commerceApplicationModelCProductRel : commerceApplicationModelCProductRels) {
			entityCache.removeResult(CommerceApplicationModelCProductRelModelImpl.ENTITY_CACHE_ENABLED,
				CommerceApplicationModelCProductRelImpl.class,
				commerceApplicationModelCProductRel.getPrimaryKey());
		}
	}

	/**
	 * Creates a new commerce application model c product rel with the primary key. Does not add the commerce application model c product rel to the database.
	 *
	 * @param commerceApplicationModelCProductRelId the primary key for the new commerce application model c product rel
	 * @return the new commerce application model c product rel
	 */
	@Override
	public CommerceApplicationModelCProductRel create(
		long commerceApplicationModelCProductRelId) {
		CommerceApplicationModelCProductRel commerceApplicationModelCProductRel = new CommerceApplicationModelCProductRelImpl();

		commerceApplicationModelCProductRel.setNew(true);
		commerceApplicationModelCProductRel.setPrimaryKey(commerceApplicationModelCProductRelId);

		commerceApplicationModelCProductRel.setCompanyId(companyProvider.getCompanyId());

		return commerceApplicationModelCProductRel;
	}

	/**
	 * Removes the commerce application model c product rel with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param commerceApplicationModelCProductRelId the primary key of the commerce application model c product rel
	 * @return the commerce application model c product rel that was removed
	 * @throws NoSuchApplicationModelCProductRelException if a commerce application model c product rel with the primary key could not be found
	 */
	@Override
	public CommerceApplicationModelCProductRel remove(
		long commerceApplicationModelCProductRelId)
		throws NoSuchApplicationModelCProductRelException {
		return remove((Serializable)commerceApplicationModelCProductRelId);
	}

	/**
	 * Removes the commerce application model c product rel with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the commerce application model c product rel
	 * @return the commerce application model c product rel that was removed
	 * @throws NoSuchApplicationModelCProductRelException if a commerce application model c product rel with the primary key could not be found
	 */
	@Override
	public CommerceApplicationModelCProductRel remove(Serializable primaryKey)
		throws NoSuchApplicationModelCProductRelException {
		Session session = null;

		try {
			session = openSession();

			CommerceApplicationModelCProductRel commerceApplicationModelCProductRel =
				(CommerceApplicationModelCProductRel)session.get(CommerceApplicationModelCProductRelImpl.class,
					primaryKey);

			if (commerceApplicationModelCProductRel == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchApplicationModelCProductRelException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
					primaryKey);
			}

			return remove(commerceApplicationModelCProductRel);
		}
		catch (NoSuchApplicationModelCProductRelException nsee) {
			throw nsee;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	protected CommerceApplicationModelCProductRel removeImpl(
		CommerceApplicationModelCProductRel commerceApplicationModelCProductRel) {
		Session session = null;

		try {
			session = openSession();

			if (!session.contains(commerceApplicationModelCProductRel)) {
				commerceApplicationModelCProductRel = (CommerceApplicationModelCProductRel)session.get(CommerceApplicationModelCProductRelImpl.class,
						commerceApplicationModelCProductRel.getPrimaryKeyObj());
			}

			if (commerceApplicationModelCProductRel != null) {
				session.delete(commerceApplicationModelCProductRel);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		if (commerceApplicationModelCProductRel != null) {
			clearCache(commerceApplicationModelCProductRel);
		}

		return commerceApplicationModelCProductRel;
	}

	@Override
	public CommerceApplicationModelCProductRel updateImpl(
		CommerceApplicationModelCProductRel commerceApplicationModelCProductRel) {
		boolean isNew = commerceApplicationModelCProductRel.isNew();

		if (!(commerceApplicationModelCProductRel instanceof CommerceApplicationModelCProductRelModelImpl)) {
			InvocationHandler invocationHandler = null;

			if (ProxyUtil.isProxyClass(
						commerceApplicationModelCProductRel.getClass())) {
				invocationHandler = ProxyUtil.getInvocationHandler(commerceApplicationModelCProductRel);

				throw new IllegalArgumentException(
					"Implement ModelWrapper in commerceApplicationModelCProductRel proxy " +
					invocationHandler.getClass());
			}

			throw new IllegalArgumentException(
				"Implement ModelWrapper in custom CommerceApplicationModelCProductRel implementation " +
				commerceApplicationModelCProductRel.getClass());
		}

		CommerceApplicationModelCProductRelModelImpl commerceApplicationModelCProductRelModelImpl =
			(CommerceApplicationModelCProductRelModelImpl)commerceApplicationModelCProductRel;

		ServiceContext serviceContext = ServiceContextThreadLocal.getServiceContext();

		Date now = new Date();

		if (isNew &&
				(commerceApplicationModelCProductRel.getCreateDate() == null)) {
			if (serviceContext == null) {
				commerceApplicationModelCProductRel.setCreateDate(now);
			}
			else {
				commerceApplicationModelCProductRel.setCreateDate(serviceContext.getCreateDate(
						now));
			}
		}

		if (!commerceApplicationModelCProductRelModelImpl.hasSetModifiedDate()) {
			if (serviceContext == null) {
				commerceApplicationModelCProductRel.setModifiedDate(now);
			}
			else {
				commerceApplicationModelCProductRel.setModifiedDate(serviceContext.getModifiedDate(
						now));
			}
		}

		Session session = null;

		try {
			session = openSession();

			if (commerceApplicationModelCProductRel.isNew()) {
				session.save(commerceApplicationModelCProductRel);

				commerceApplicationModelCProductRel.setNew(false);
			}
			else {
				commerceApplicationModelCProductRel = (CommerceApplicationModelCProductRel)session.merge(commerceApplicationModelCProductRel);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);

		if (isNew) {
			finderCache.removeResult(FINDER_PATH_COUNT_ALL, FINDER_ARGS_EMPTY);
			finderCache.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL,
				FINDER_ARGS_EMPTY);
		}

		entityCache.putResult(CommerceApplicationModelCProductRelModelImpl.ENTITY_CACHE_ENABLED,
			CommerceApplicationModelCProductRelImpl.class,
			commerceApplicationModelCProductRel.getPrimaryKey(),
			commerceApplicationModelCProductRel, false);

		commerceApplicationModelCProductRel.resetOriginalValues();

		return commerceApplicationModelCProductRel;
	}

	/**
	 * Returns the commerce application model c product rel with the primary key or throws a {@link com.liferay.portal.kernel.exception.NoSuchModelException} if it could not be found.
	 *
	 * @param primaryKey the primary key of the commerce application model c product rel
	 * @return the commerce application model c product rel
	 * @throws NoSuchApplicationModelCProductRelException if a commerce application model c product rel with the primary key could not be found
	 */
	@Override
	public CommerceApplicationModelCProductRel findByPrimaryKey(
		Serializable primaryKey)
		throws NoSuchApplicationModelCProductRelException {
		CommerceApplicationModelCProductRel commerceApplicationModelCProductRel = fetchByPrimaryKey(primaryKey);

		if (commerceApplicationModelCProductRel == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchApplicationModelCProductRelException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
				primaryKey);
		}

		return commerceApplicationModelCProductRel;
	}

	/**
	 * Returns the commerce application model c product rel with the primary key or throws a {@link NoSuchApplicationModelCProductRelException} if it could not be found.
	 *
	 * @param commerceApplicationModelCProductRelId the primary key of the commerce application model c product rel
	 * @return the commerce application model c product rel
	 * @throws NoSuchApplicationModelCProductRelException if a commerce application model c product rel with the primary key could not be found
	 */
	@Override
	public CommerceApplicationModelCProductRel findByPrimaryKey(
		long commerceApplicationModelCProductRelId)
		throws NoSuchApplicationModelCProductRelException {
		return findByPrimaryKey((Serializable)commerceApplicationModelCProductRelId);
	}

	/**
	 * Returns the commerce application model c product rel with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the commerce application model c product rel
	 * @return the commerce application model c product rel, or <code>null</code> if a commerce application model c product rel with the primary key could not be found
	 */
	@Override
	public CommerceApplicationModelCProductRel fetchByPrimaryKey(
		Serializable primaryKey) {
		Serializable serializable = entityCache.getResult(CommerceApplicationModelCProductRelModelImpl.ENTITY_CACHE_ENABLED,
				CommerceApplicationModelCProductRelImpl.class, primaryKey);

		if (serializable == nullModel) {
			return null;
		}

		CommerceApplicationModelCProductRel commerceApplicationModelCProductRel = (CommerceApplicationModelCProductRel)serializable;

		if (commerceApplicationModelCProductRel == null) {
			Session session = null;

			try {
				session = openSession();

				commerceApplicationModelCProductRel = (CommerceApplicationModelCProductRel)session.get(CommerceApplicationModelCProductRelImpl.class,
						primaryKey);

				if (commerceApplicationModelCProductRel != null) {
					cacheResult(commerceApplicationModelCProductRel);
				}
				else {
					entityCache.putResult(CommerceApplicationModelCProductRelModelImpl.ENTITY_CACHE_ENABLED,
						CommerceApplicationModelCProductRelImpl.class,
						primaryKey, nullModel);
				}
			}
			catch (Exception e) {
				entityCache.removeResult(CommerceApplicationModelCProductRelModelImpl.ENTITY_CACHE_ENABLED,
					CommerceApplicationModelCProductRelImpl.class, primaryKey);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return commerceApplicationModelCProductRel;
	}

	/**
	 * Returns the commerce application model c product rel with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param commerceApplicationModelCProductRelId the primary key of the commerce application model c product rel
	 * @return the commerce application model c product rel, or <code>null</code> if a commerce application model c product rel with the primary key could not be found
	 */
	@Override
	public CommerceApplicationModelCProductRel fetchByPrimaryKey(
		long commerceApplicationModelCProductRelId) {
		return fetchByPrimaryKey((Serializable)commerceApplicationModelCProductRelId);
	}

	@Override
	public Map<Serializable, CommerceApplicationModelCProductRel> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {
		if (primaryKeys.isEmpty()) {
			return Collections.emptyMap();
		}

		Map<Serializable, CommerceApplicationModelCProductRel> map = new HashMap<Serializable, CommerceApplicationModelCProductRel>();

		if (primaryKeys.size() == 1) {
			Iterator<Serializable> iterator = primaryKeys.iterator();

			Serializable primaryKey = iterator.next();

			CommerceApplicationModelCProductRel commerceApplicationModelCProductRel =
				fetchByPrimaryKey(primaryKey);

			if (commerceApplicationModelCProductRel != null) {
				map.put(primaryKey, commerceApplicationModelCProductRel);
			}

			return map;
		}

		Set<Serializable> uncachedPrimaryKeys = null;

		for (Serializable primaryKey : primaryKeys) {
			Serializable serializable = entityCache.getResult(CommerceApplicationModelCProductRelModelImpl.ENTITY_CACHE_ENABLED,
					CommerceApplicationModelCProductRelImpl.class, primaryKey);

			if (serializable != nullModel) {
				if (serializable == null) {
					if (uncachedPrimaryKeys == null) {
						uncachedPrimaryKeys = new HashSet<Serializable>();
					}

					uncachedPrimaryKeys.add(primaryKey);
				}
				else {
					map.put(primaryKey,
						(CommerceApplicationModelCProductRel)serializable);
				}
			}
		}

		if (uncachedPrimaryKeys == null) {
			return map;
		}

		StringBundler query = new StringBundler((uncachedPrimaryKeys.size() * 2) +
				1);

		query.append(_SQL_SELECT_COMMERCEAPPLICATIONMODELCPRODUCTREL_WHERE_PKS_IN);

		for (Serializable primaryKey : uncachedPrimaryKeys) {
			query.append((long)primaryKey);

			query.append(",");
		}

		query.setIndex(query.index() - 1);

		query.append(")");

		String sql = query.toString();

		Session session = null;

		try {
			session = openSession();

			Query q = session.createQuery(sql);

			for (CommerceApplicationModelCProductRel commerceApplicationModelCProductRel : (List<CommerceApplicationModelCProductRel>)q.list()) {
				map.put(commerceApplicationModelCProductRel.getPrimaryKeyObj(),
					commerceApplicationModelCProductRel);

				cacheResult(commerceApplicationModelCProductRel);

				uncachedPrimaryKeys.remove(commerceApplicationModelCProductRel.getPrimaryKeyObj());
			}

			for (Serializable primaryKey : uncachedPrimaryKeys) {
				entityCache.putResult(CommerceApplicationModelCProductRelModelImpl.ENTITY_CACHE_ENABLED,
					CommerceApplicationModelCProductRelImpl.class, primaryKey,
					nullModel);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		return map;
	}

	/**
	 * Returns all the commerce application model c product rels.
	 *
	 * @return the commerce application model c product rels
	 */
	@Override
	public List<CommerceApplicationModelCProductRel> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the commerce application model c product rels.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link CommerceApplicationModelCProductRelModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of commerce application model c product rels
	 * @param end the upper bound of the range of commerce application model c product rels (not inclusive)
	 * @return the range of commerce application model c product rels
	 */
	@Override
	public List<CommerceApplicationModelCProductRel> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the commerce application model c product rels.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link CommerceApplicationModelCProductRelModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of commerce application model c product rels
	 * @param end the upper bound of the range of commerce application model c product rels (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of commerce application model c product rels
	 */
	@Override
	public List<CommerceApplicationModelCProductRel> findAll(int start,
		int end,
		OrderByComparator<CommerceApplicationModelCProductRel> orderByComparator) {
		return findAll(start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the commerce application model c product rels.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link CommerceApplicationModelCProductRelModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of commerce application model c product rels
	 * @param end the upper bound of the range of commerce application model c product rels (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param retrieveFromCache whether to retrieve from the finder cache
	 * @return the ordered range of commerce application model c product rels
	 */
	@Override
	public List<CommerceApplicationModelCProductRel> findAll(int start,
		int end,
		OrderByComparator<CommerceApplicationModelCProductRel> orderByComparator,
		boolean retrieveFromCache) {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL;
			finderArgs = FINDER_ARGS_EMPTY;
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_ALL;
			finderArgs = new Object[] { start, end, orderByComparator };
		}

		List<CommerceApplicationModelCProductRel> list = null;

		if (retrieveFromCache) {
			list = (List<CommerceApplicationModelCProductRel>)finderCache.getResult(finderPath,
					finderArgs, this);
		}

		if (list == null) {
			StringBundler query = null;
			String sql = null;

			if (orderByComparator != null) {
				query = new StringBundler(2 +
						(orderByComparator.getOrderByFields().length * 2));

				query.append(_SQL_SELECT_COMMERCEAPPLICATIONMODELCPRODUCTREL);

				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);

				sql = query.toString();
			}
			else {
				sql = _SQL_SELECT_COMMERCEAPPLICATIONMODELCPRODUCTREL;

				if (pagination) {
					sql = sql.concat(CommerceApplicationModelCProductRelModelImpl.ORDER_BY_JPQL);
				}
			}

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				if (!pagination) {
					list = (List<CommerceApplicationModelCProductRel>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<CommerceApplicationModelCProductRel>)QueryUtil.list(q,
							getDialect(), start, end);
				}

				cacheResult(list);

				finderCache.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Removes all the commerce application model c product rels from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (CommerceApplicationModelCProductRel commerceApplicationModelCProductRel : findAll()) {
			remove(commerceApplicationModelCProductRel);
		}
	}

	/**
	 * Returns the number of commerce application model c product rels.
	 *
	 * @return the number of commerce application model c product rels
	 */
	@Override
	public int countAll() {
		Long count = (Long)finderCache.getResult(FINDER_PATH_COUNT_ALL,
				FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(_SQL_COUNT_COMMERCEAPPLICATIONMODELCPRODUCTREL);

				count = (Long)q.uniqueResult();

				finderCache.putResult(FINDER_PATH_COUNT_ALL, FINDER_ARGS_EMPTY,
					count);
			}
			catch (Exception e) {
				finderCache.removeResult(FINDER_PATH_COUNT_ALL,
					FINDER_ARGS_EMPTY);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	@Override
	public Set<String> getBadColumnNames() {
		return _badColumnNames;
	}

	@Override
	protected Map<String, Integer> getTableColumnsMap() {
		return CommerceApplicationModelCProductRelModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the commerce application model c product rel persistence.
	 */
	public void afterPropertiesSet() {
	}

	public void destroy() {
		entityCache.removeCache(CommerceApplicationModelCProductRelImpl.class.getName());
		finderCache.removeCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.removeCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@ServiceReference(type = CompanyProviderWrapper.class)
	protected CompanyProvider companyProvider;
	@ServiceReference(type = EntityCache.class)
	protected EntityCache entityCache;
	@ServiceReference(type = FinderCache.class)
	protected FinderCache finderCache;
	private static final String _SQL_SELECT_COMMERCEAPPLICATIONMODELCPRODUCTREL = "SELECT commerceApplicationModelCProductRel FROM CommerceApplicationModelCProductRel commerceApplicationModelCProductRel";
	private static final String _SQL_SELECT_COMMERCEAPPLICATIONMODELCPRODUCTREL_WHERE_PKS_IN =
		"SELECT commerceApplicationModelCProductRel FROM CommerceApplicationModelCProductRel commerceApplicationModelCProductRel WHERE CAModelCProductRelId IN (";
	private static final String _SQL_COUNT_COMMERCEAPPLICATIONMODELCPRODUCTREL = "SELECT COUNT(commerceApplicationModelCProductRel) FROM CommerceApplicationModelCProductRel commerceApplicationModelCProductRel";
	private static final String _ORDER_BY_ENTITY_ALIAS = "commerceApplicationModelCProductRel.";
	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY = "No CommerceApplicationModelCProductRel exists with the primary key ";
	private static final Log _log = LogFactoryUtil.getLog(CommerceApplicationModelCProductRelPersistenceImpl.class);
	private static final Set<String> _badColumnNames = SetUtil.fromArray(new String[] {
				"commerceApplicationModelCProductRelId"
			});
}