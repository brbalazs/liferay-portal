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

package com.liferay.commerce.bom.service.persistence.impl;

import aQute.bnd.annotation.ProviderType;

import com.liferay.commerce.bom.exception.NoSuchBOMEntryException;
import com.liferay.commerce.bom.model.CommerceBOMEntry;
import com.liferay.commerce.bom.model.impl.CommerceBOMEntryImpl;
import com.liferay.commerce.bom.model.impl.CommerceBOMEntryModelImpl;
import com.liferay.commerce.bom.service.persistence.CommerceBOMEntryPersistence;

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
 * The persistence implementation for the commerce bom entry service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Luca Pellizzon
 * @see CommerceBOMEntryPersistence
 * @see com.liferay.commerce.bom.service.persistence.CommerceBOMEntryUtil
 * @generated
 */
@ProviderType
public class CommerceBOMEntryPersistenceImpl extends BasePersistenceImpl<CommerceBOMEntry>
	implements CommerceBOMEntryPersistence {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link CommerceBOMEntryUtil} to access the commerce bom entry persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY = CommerceBOMEntryImpl.class.getName();
	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List1";
	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List2";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_ALL = new FinderPath(CommerceBOMEntryModelImpl.ENTITY_CACHE_ENABLED,
			CommerceBOMEntryModelImpl.FINDER_CACHE_ENABLED,
			CommerceBOMEntryImpl.class, FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL = new FinderPath(CommerceBOMEntryModelImpl.ENTITY_CACHE_ENABLED,
			CommerceBOMEntryModelImpl.FINDER_CACHE_ENABLED,
			CommerceBOMEntryImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(CommerceBOMEntryModelImpl.ENTITY_CACHE_ENABLED,
			CommerceBOMEntryModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll", new String[0]);

	public CommerceBOMEntryPersistenceImpl() {
		setModelClass(CommerceBOMEntry.class);

		try {
			Field field = BasePersistenceImpl.class.getDeclaredField(
					"_dbColumnNames");

			field.setAccessible(true);

			Map<String, String> dbColumnNames = new HashMap<String, String>();

			dbColumnNames.put("number", "number_");

			field.set(this, dbColumnNames);
		}
		catch (Exception e) {
			if (_log.isDebugEnabled()) {
				_log.debug(e, e);
			}
		}
	}

	/**
	 * Caches the commerce bom entry in the entity cache if it is enabled.
	 *
	 * @param commerceBOMEntry the commerce bom entry
	 */
	@Override
	public void cacheResult(CommerceBOMEntry commerceBOMEntry) {
		entityCache.putResult(CommerceBOMEntryModelImpl.ENTITY_CACHE_ENABLED,
			CommerceBOMEntryImpl.class, commerceBOMEntry.getPrimaryKey(),
			commerceBOMEntry);

		commerceBOMEntry.resetOriginalValues();
	}

	/**
	 * Caches the commerce bom entries in the entity cache if it is enabled.
	 *
	 * @param commerceBOMEntries the commerce bom entries
	 */
	@Override
	public void cacheResult(List<CommerceBOMEntry> commerceBOMEntries) {
		for (CommerceBOMEntry commerceBOMEntry : commerceBOMEntries) {
			if (entityCache.getResult(
						CommerceBOMEntryModelImpl.ENTITY_CACHE_ENABLED,
						CommerceBOMEntryImpl.class,
						commerceBOMEntry.getPrimaryKey()) == null) {
				cacheResult(commerceBOMEntry);
			}
			else {
				commerceBOMEntry.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all commerce bom entries.
	 *
	 * <p>
	 * The {@link EntityCache} and {@link FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		entityCache.clearCache(CommerceBOMEntryImpl.class);

		finderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the commerce bom entry.
	 *
	 * <p>
	 * The {@link EntityCache} and {@link FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(CommerceBOMEntry commerceBOMEntry) {
		entityCache.removeResult(CommerceBOMEntryModelImpl.ENTITY_CACHE_ENABLED,
			CommerceBOMEntryImpl.class, commerceBOMEntry.getPrimaryKey());

		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@Override
	public void clearCache(List<CommerceBOMEntry> commerceBOMEntries) {
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (CommerceBOMEntry commerceBOMEntry : commerceBOMEntries) {
			entityCache.removeResult(CommerceBOMEntryModelImpl.ENTITY_CACHE_ENABLED,
				CommerceBOMEntryImpl.class, commerceBOMEntry.getPrimaryKey());
		}
	}

	/**
	 * Creates a new commerce bom entry with the primary key. Does not add the commerce bom entry to the database.
	 *
	 * @param commerceBOMEntryId the primary key for the new commerce bom entry
	 * @return the new commerce bom entry
	 */
	@Override
	public CommerceBOMEntry create(long commerceBOMEntryId) {
		CommerceBOMEntry commerceBOMEntry = new CommerceBOMEntryImpl();

		commerceBOMEntry.setNew(true);
		commerceBOMEntry.setPrimaryKey(commerceBOMEntryId);

		commerceBOMEntry.setCompanyId(companyProvider.getCompanyId());

		return commerceBOMEntry;
	}

	/**
	 * Removes the commerce bom entry with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param commerceBOMEntryId the primary key of the commerce bom entry
	 * @return the commerce bom entry that was removed
	 * @throws NoSuchBOMEntryException if a commerce bom entry with the primary key could not be found
	 */
	@Override
	public CommerceBOMEntry remove(long commerceBOMEntryId)
		throws NoSuchBOMEntryException {
		return remove((Serializable)commerceBOMEntryId);
	}

	/**
	 * Removes the commerce bom entry with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the commerce bom entry
	 * @return the commerce bom entry that was removed
	 * @throws NoSuchBOMEntryException if a commerce bom entry with the primary key could not be found
	 */
	@Override
	public CommerceBOMEntry remove(Serializable primaryKey)
		throws NoSuchBOMEntryException {
		Session session = null;

		try {
			session = openSession();

			CommerceBOMEntry commerceBOMEntry = (CommerceBOMEntry)session.get(CommerceBOMEntryImpl.class,
					primaryKey);

			if (commerceBOMEntry == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchBOMEntryException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
					primaryKey);
			}

			return remove(commerceBOMEntry);
		}
		catch (NoSuchBOMEntryException nsee) {
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
	protected CommerceBOMEntry removeImpl(CommerceBOMEntry commerceBOMEntry) {
		Session session = null;

		try {
			session = openSession();

			if (!session.contains(commerceBOMEntry)) {
				commerceBOMEntry = (CommerceBOMEntry)session.get(CommerceBOMEntryImpl.class,
						commerceBOMEntry.getPrimaryKeyObj());
			}

			if (commerceBOMEntry != null) {
				session.delete(commerceBOMEntry);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		if (commerceBOMEntry != null) {
			clearCache(commerceBOMEntry);
		}

		return commerceBOMEntry;
	}

	@Override
	public CommerceBOMEntry updateImpl(CommerceBOMEntry commerceBOMEntry) {
		boolean isNew = commerceBOMEntry.isNew();

		if (!(commerceBOMEntry instanceof CommerceBOMEntryModelImpl)) {
			InvocationHandler invocationHandler = null;

			if (ProxyUtil.isProxyClass(commerceBOMEntry.getClass())) {
				invocationHandler = ProxyUtil.getInvocationHandler(commerceBOMEntry);

				throw new IllegalArgumentException(
					"Implement ModelWrapper in commerceBOMEntry proxy " +
					invocationHandler.getClass());
			}

			throw new IllegalArgumentException(
				"Implement ModelWrapper in custom CommerceBOMEntry implementation " +
				commerceBOMEntry.getClass());
		}

		CommerceBOMEntryModelImpl commerceBOMEntryModelImpl = (CommerceBOMEntryModelImpl)commerceBOMEntry;

		ServiceContext serviceContext = ServiceContextThreadLocal.getServiceContext();

		Date now = new Date();

		if (isNew && (commerceBOMEntry.getCreateDate() == null)) {
			if (serviceContext == null) {
				commerceBOMEntry.setCreateDate(now);
			}
			else {
				commerceBOMEntry.setCreateDate(serviceContext.getCreateDate(now));
			}
		}

		if (!commerceBOMEntryModelImpl.hasSetModifiedDate()) {
			if (serviceContext == null) {
				commerceBOMEntry.setModifiedDate(now);
			}
			else {
				commerceBOMEntry.setModifiedDate(serviceContext.getModifiedDate(
						now));
			}
		}

		Session session = null;

		try {
			session = openSession();

			if (commerceBOMEntry.isNew()) {
				session.save(commerceBOMEntry);

				commerceBOMEntry.setNew(false);
			}
			else {
				commerceBOMEntry = (CommerceBOMEntry)session.merge(commerceBOMEntry);
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

		entityCache.putResult(CommerceBOMEntryModelImpl.ENTITY_CACHE_ENABLED,
			CommerceBOMEntryImpl.class, commerceBOMEntry.getPrimaryKey(),
			commerceBOMEntry, false);

		commerceBOMEntry.resetOriginalValues();

		return commerceBOMEntry;
	}

	/**
	 * Returns the commerce bom entry with the primary key or throws a {@link com.liferay.portal.kernel.exception.NoSuchModelException} if it could not be found.
	 *
	 * @param primaryKey the primary key of the commerce bom entry
	 * @return the commerce bom entry
	 * @throws NoSuchBOMEntryException if a commerce bom entry with the primary key could not be found
	 */
	@Override
	public CommerceBOMEntry findByPrimaryKey(Serializable primaryKey)
		throws NoSuchBOMEntryException {
		CommerceBOMEntry commerceBOMEntry = fetchByPrimaryKey(primaryKey);

		if (commerceBOMEntry == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchBOMEntryException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
				primaryKey);
		}

		return commerceBOMEntry;
	}

	/**
	 * Returns the commerce bom entry with the primary key or throws a {@link NoSuchBOMEntryException} if it could not be found.
	 *
	 * @param commerceBOMEntryId the primary key of the commerce bom entry
	 * @return the commerce bom entry
	 * @throws NoSuchBOMEntryException if a commerce bom entry with the primary key could not be found
	 */
	@Override
	public CommerceBOMEntry findByPrimaryKey(long commerceBOMEntryId)
		throws NoSuchBOMEntryException {
		return findByPrimaryKey((Serializable)commerceBOMEntryId);
	}

	/**
	 * Returns the commerce bom entry with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the commerce bom entry
	 * @return the commerce bom entry, or <code>null</code> if a commerce bom entry with the primary key could not be found
	 */
	@Override
	public CommerceBOMEntry fetchByPrimaryKey(Serializable primaryKey) {
		Serializable serializable = entityCache.getResult(CommerceBOMEntryModelImpl.ENTITY_CACHE_ENABLED,
				CommerceBOMEntryImpl.class, primaryKey);

		if (serializable == nullModel) {
			return null;
		}

		CommerceBOMEntry commerceBOMEntry = (CommerceBOMEntry)serializable;

		if (commerceBOMEntry == null) {
			Session session = null;

			try {
				session = openSession();

				commerceBOMEntry = (CommerceBOMEntry)session.get(CommerceBOMEntryImpl.class,
						primaryKey);

				if (commerceBOMEntry != null) {
					cacheResult(commerceBOMEntry);
				}
				else {
					entityCache.putResult(CommerceBOMEntryModelImpl.ENTITY_CACHE_ENABLED,
						CommerceBOMEntryImpl.class, primaryKey, nullModel);
				}
			}
			catch (Exception e) {
				entityCache.removeResult(CommerceBOMEntryModelImpl.ENTITY_CACHE_ENABLED,
					CommerceBOMEntryImpl.class, primaryKey);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return commerceBOMEntry;
	}

	/**
	 * Returns the commerce bom entry with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param commerceBOMEntryId the primary key of the commerce bom entry
	 * @return the commerce bom entry, or <code>null</code> if a commerce bom entry with the primary key could not be found
	 */
	@Override
	public CommerceBOMEntry fetchByPrimaryKey(long commerceBOMEntryId) {
		return fetchByPrimaryKey((Serializable)commerceBOMEntryId);
	}

	@Override
	public Map<Serializable, CommerceBOMEntry> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {
		if (primaryKeys.isEmpty()) {
			return Collections.emptyMap();
		}

		Map<Serializable, CommerceBOMEntry> map = new HashMap<Serializable, CommerceBOMEntry>();

		if (primaryKeys.size() == 1) {
			Iterator<Serializable> iterator = primaryKeys.iterator();

			Serializable primaryKey = iterator.next();

			CommerceBOMEntry commerceBOMEntry = fetchByPrimaryKey(primaryKey);

			if (commerceBOMEntry != null) {
				map.put(primaryKey, commerceBOMEntry);
			}

			return map;
		}

		Set<Serializable> uncachedPrimaryKeys = null;

		for (Serializable primaryKey : primaryKeys) {
			Serializable serializable = entityCache.getResult(CommerceBOMEntryModelImpl.ENTITY_CACHE_ENABLED,
					CommerceBOMEntryImpl.class, primaryKey);

			if (serializable != nullModel) {
				if (serializable == null) {
					if (uncachedPrimaryKeys == null) {
						uncachedPrimaryKeys = new HashSet<Serializable>();
					}

					uncachedPrimaryKeys.add(primaryKey);
				}
				else {
					map.put(primaryKey, (CommerceBOMEntry)serializable);
				}
			}
		}

		if (uncachedPrimaryKeys == null) {
			return map;
		}

		StringBundler query = new StringBundler((uncachedPrimaryKeys.size() * 2) +
				1);

		query.append(_SQL_SELECT_COMMERCEBOMENTRY_WHERE_PKS_IN);

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

			for (CommerceBOMEntry commerceBOMEntry : (List<CommerceBOMEntry>)q.list()) {
				map.put(commerceBOMEntry.getPrimaryKeyObj(), commerceBOMEntry);

				cacheResult(commerceBOMEntry);

				uncachedPrimaryKeys.remove(commerceBOMEntry.getPrimaryKeyObj());
			}

			for (Serializable primaryKey : uncachedPrimaryKeys) {
				entityCache.putResult(CommerceBOMEntryModelImpl.ENTITY_CACHE_ENABLED,
					CommerceBOMEntryImpl.class, primaryKey, nullModel);
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
	 * Returns all the commerce bom entries.
	 *
	 * @return the commerce bom entries
	 */
	@Override
	public List<CommerceBOMEntry> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the commerce bom entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link CommerceBOMEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of commerce bom entries
	 * @param end the upper bound of the range of commerce bom entries (not inclusive)
	 * @return the range of commerce bom entries
	 */
	@Override
	public List<CommerceBOMEntry> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the commerce bom entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link CommerceBOMEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of commerce bom entries
	 * @param end the upper bound of the range of commerce bom entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of commerce bom entries
	 */
	@Override
	public List<CommerceBOMEntry> findAll(int start, int end,
		OrderByComparator<CommerceBOMEntry> orderByComparator) {
		return findAll(start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the commerce bom entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link CommerceBOMEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of commerce bom entries
	 * @param end the upper bound of the range of commerce bom entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param retrieveFromCache whether to retrieve from the finder cache
	 * @return the ordered range of commerce bom entries
	 */
	@Override
	public List<CommerceBOMEntry> findAll(int start, int end,
		OrderByComparator<CommerceBOMEntry> orderByComparator,
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

		List<CommerceBOMEntry> list = null;

		if (retrieveFromCache) {
			list = (List<CommerceBOMEntry>)finderCache.getResult(finderPath,
					finderArgs, this);
		}

		if (list == null) {
			StringBundler query = null;
			String sql = null;

			if (orderByComparator != null) {
				query = new StringBundler(2 +
						(orderByComparator.getOrderByFields().length * 2));

				query.append(_SQL_SELECT_COMMERCEBOMENTRY);

				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);

				sql = query.toString();
			}
			else {
				sql = _SQL_SELECT_COMMERCEBOMENTRY;

				if (pagination) {
					sql = sql.concat(CommerceBOMEntryModelImpl.ORDER_BY_JPQL);
				}
			}

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				if (!pagination) {
					list = (List<CommerceBOMEntry>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<CommerceBOMEntry>)QueryUtil.list(q,
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
	 * Removes all the commerce bom entries from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (CommerceBOMEntry commerceBOMEntry : findAll()) {
			remove(commerceBOMEntry);
		}
	}

	/**
	 * Returns the number of commerce bom entries.
	 *
	 * @return the number of commerce bom entries
	 */
	@Override
	public int countAll() {
		Long count = (Long)finderCache.getResult(FINDER_PATH_COUNT_ALL,
				FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(_SQL_COUNT_COMMERCEBOMENTRY);

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
		return CommerceBOMEntryModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the commerce bom entry persistence.
	 */
	public void afterPropertiesSet() {
	}

	public void destroy() {
		entityCache.removeCache(CommerceBOMEntryImpl.class.getName());
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
	private static final String _SQL_SELECT_COMMERCEBOMENTRY = "SELECT commerceBOMEntry FROM CommerceBOMEntry commerceBOMEntry";
	private static final String _SQL_SELECT_COMMERCEBOMENTRY_WHERE_PKS_IN = "SELECT commerceBOMEntry FROM CommerceBOMEntry commerceBOMEntry WHERE commerceBOMEntryId IN (";
	private static final String _SQL_COUNT_COMMERCEBOMENTRY = "SELECT COUNT(commerceBOMEntry) FROM CommerceBOMEntry commerceBOMEntry";
	private static final String _ORDER_BY_ENTITY_ALIAS = "commerceBOMEntry.";
	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY = "No CommerceBOMEntry exists with the primary key ";
	private static final Log _log = LogFactoryUtil.getLog(CommerceBOMEntryPersistenceImpl.class);
	private static final Set<String> _badColumnNames = SetUtil.fromArray(new String[] {
				"number"
			});
}