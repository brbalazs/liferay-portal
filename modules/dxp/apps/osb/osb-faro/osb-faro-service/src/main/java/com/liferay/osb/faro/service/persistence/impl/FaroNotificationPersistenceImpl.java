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

package com.liferay.osb.faro.service.persistence.impl;

import com.liferay.osb.faro.exception.NoSuchFaroNotificationException;
import com.liferay.osb.faro.model.FaroNotification;
import com.liferay.osb.faro.model.impl.FaroNotificationImpl;
import com.liferay.osb.faro.model.impl.FaroNotificationModelImpl;
import com.liferay.osb.faro.service.persistence.FaroNotificationPersistence;
import com.liferay.portal.kernel.dao.orm.EntityCache;
import com.liferay.portal.kernel.dao.orm.FinderCache;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.spring.extender.service.ServiceReference;

import java.io.Serializable;

import java.lang.reflect.Field;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The persistence implementation for the faro notification service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Matthew Kong
 * @generated
 */
public class FaroNotificationPersistenceImpl
	extends BasePersistenceImpl<FaroNotification>
	implements FaroNotificationPersistence {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use <code>FaroNotificationUtil</code> to access the faro notification persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY =
		FaroNotificationImpl.class.getName();

	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List1";

	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List2";

	private FinderPath _finderPathWithPaginationFindAll;
	private FinderPath _finderPathWithoutPaginationFindAll;
	private FinderPath _finderPathCountAll;

	public FaroNotificationPersistenceImpl() {
		Map<String, String> dbColumnNames = new HashMap<String, String>();

		dbColumnNames.put("read", "read_");
		dbColumnNames.put("type", "type_");

		try {
			Field field = BasePersistenceImpl.class.getDeclaredField(
				"_dbColumnNames");

			field.setAccessible(true);

			field.set(this, dbColumnNames);
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(exception, exception);
			}
		}

		setModelClass(FaroNotification.class);
	}

	/**
	 * Caches the faro notification in the entity cache if it is enabled.
	 *
	 * @param faroNotification the faro notification
	 */
	@Override
	public void cacheResult(FaroNotification faroNotification) {
		entityCache.putResult(
			FaroNotificationModelImpl.ENTITY_CACHE_ENABLED,
			FaroNotificationImpl.class, faroNotification.getPrimaryKey(),
			faroNotification);

		faroNotification.resetOriginalValues();
	}

	/**
	 * Caches the faro notifications in the entity cache if it is enabled.
	 *
	 * @param faroNotifications the faro notifications
	 */
	@Override
	public void cacheResult(List<FaroNotification> faroNotifications) {
		for (FaroNotification faroNotification : faroNotifications) {
			if (entityCache.getResult(
					FaroNotificationModelImpl.ENTITY_CACHE_ENABLED,
					FaroNotificationImpl.class,
					faroNotification.getPrimaryKey()) == null) {

				cacheResult(faroNotification);
			}
			else {
				faroNotification.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all faro notifications.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		entityCache.clearCache(FaroNotificationImpl.class);

		finderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the faro notification.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(FaroNotification faroNotification) {
		entityCache.removeResult(
			FaroNotificationModelImpl.ENTITY_CACHE_ENABLED,
			FaroNotificationImpl.class, faroNotification.getPrimaryKey());

		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@Override
	public void clearCache(List<FaroNotification> faroNotifications) {
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (FaroNotification faroNotification : faroNotifications) {
			entityCache.removeResult(
				FaroNotificationModelImpl.ENTITY_CACHE_ENABLED,
				FaroNotificationImpl.class, faroNotification.getPrimaryKey());
		}
	}

	public void clearCache(Set<Serializable> primaryKeys) {
		finderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (Serializable primaryKey : primaryKeys) {
			entityCache.removeResult(
				FaroNotificationModelImpl.ENTITY_CACHE_ENABLED,
				FaroNotificationImpl.class, primaryKey);
		}
	}

	/**
	 * Creates a new faro notification with the primary key. Does not add the faro notification to the database.
	 *
	 * @param faroNotificationId the primary key for the new faro notification
	 * @return the new faro notification
	 */
	@Override
	public FaroNotification create(long faroNotificationId) {
		FaroNotification faroNotification = new FaroNotificationImpl();

		faroNotification.setNew(true);
		faroNotification.setPrimaryKey(faroNotificationId);

		return faroNotification;
	}

	/**
	 * Removes the faro notification with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param faroNotificationId the primary key of the faro notification
	 * @return the faro notification that was removed
	 * @throws NoSuchFaroNotificationException if a faro notification with the primary key could not be found
	 */
	@Override
	public FaroNotification remove(long faroNotificationId)
		throws NoSuchFaroNotificationException {

		return remove((Serializable)faroNotificationId);
	}

	/**
	 * Removes the faro notification with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the faro notification
	 * @return the faro notification that was removed
	 * @throws NoSuchFaroNotificationException if a faro notification with the primary key could not be found
	 */
	@Override
	public FaroNotification remove(Serializable primaryKey)
		throws NoSuchFaroNotificationException {

		Session session = null;

		try {
			session = openSession();

			FaroNotification faroNotification = (FaroNotification)session.get(
				FaroNotificationImpl.class, primaryKey);

			if (faroNotification == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchFaroNotificationException(
					_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			return remove(faroNotification);
		}
		catch (NoSuchFaroNotificationException noSuchEntityException) {
			throw noSuchEntityException;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	protected FaroNotification removeImpl(FaroNotification faroNotification) {
		Session session = null;

		try {
			session = openSession();

			if (!session.contains(faroNotification)) {
				faroNotification = (FaroNotification)session.get(
					FaroNotificationImpl.class,
					faroNotification.getPrimaryKeyObj());
			}

			if (faroNotification != null) {
				session.delete(faroNotification);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		if (faroNotification != null) {
			clearCache(faroNotification);
		}

		return faroNotification;
	}

	@Override
	public FaroNotification updateImpl(FaroNotification faroNotification) {
		boolean isNew = faroNotification.isNew();

		Session session = null;

		try {
			session = openSession();

			if (isNew) {
				session.save(faroNotification);

				faroNotification.setNew(false);
			}
			else {
				faroNotification = (FaroNotification)session.merge(
					faroNotification);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);

		if (isNew) {
			finderCache.removeResult(_finderPathCountAll, FINDER_ARGS_EMPTY);
			finderCache.removeResult(
				_finderPathWithoutPaginationFindAll, FINDER_ARGS_EMPTY);
		}

		entityCache.putResult(
			FaroNotificationModelImpl.ENTITY_CACHE_ENABLED,
			FaroNotificationImpl.class, faroNotification.getPrimaryKey(),
			faroNotification, false);

		faroNotification.resetOriginalValues();

		return faroNotification;
	}

	/**
	 * Returns the faro notification with the primary key or throws a <code>com.liferay.portal.kernel.exception.NoSuchModelException</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the faro notification
	 * @return the faro notification
	 * @throws NoSuchFaroNotificationException if a faro notification with the primary key could not be found
	 */
	@Override
	public FaroNotification findByPrimaryKey(Serializable primaryKey)
		throws NoSuchFaroNotificationException {

		FaroNotification faroNotification = fetchByPrimaryKey(primaryKey);

		if (faroNotification == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchFaroNotificationException(
				_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
		}

		return faroNotification;
	}

	/**
	 * Returns the faro notification with the primary key or throws a <code>NoSuchFaroNotificationException</code> if it could not be found.
	 *
	 * @param faroNotificationId the primary key of the faro notification
	 * @return the faro notification
	 * @throws NoSuchFaroNotificationException if a faro notification with the primary key could not be found
	 */
	@Override
	public FaroNotification findByPrimaryKey(long faroNotificationId)
		throws NoSuchFaroNotificationException {

		return findByPrimaryKey((Serializable)faroNotificationId);
	}

	/**
	 * Returns the faro notification with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the faro notification
	 * @return the faro notification, or <code>null</code> if a faro notification with the primary key could not be found
	 */
	@Override
	public FaroNotification fetchByPrimaryKey(Serializable primaryKey) {
		Serializable serializable = entityCache.getResult(
			FaroNotificationModelImpl.ENTITY_CACHE_ENABLED,
			FaroNotificationImpl.class, primaryKey);

		if (serializable == nullModel) {
			return null;
		}

		FaroNotification faroNotification = (FaroNotification)serializable;

		if (faroNotification == null) {
			Session session = null;

			try {
				session = openSession();

				faroNotification = (FaroNotification)session.get(
					FaroNotificationImpl.class, primaryKey);

				if (faroNotification != null) {
					cacheResult(faroNotification);
				}
				else {
					entityCache.putResult(
						FaroNotificationModelImpl.ENTITY_CACHE_ENABLED,
						FaroNotificationImpl.class, primaryKey, nullModel);
				}
			}
			catch (Exception exception) {
				entityCache.removeResult(
					FaroNotificationModelImpl.ENTITY_CACHE_ENABLED,
					FaroNotificationImpl.class, primaryKey);

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return faroNotification;
	}

	/**
	 * Returns the faro notification with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param faroNotificationId the primary key of the faro notification
	 * @return the faro notification, or <code>null</code> if a faro notification with the primary key could not be found
	 */
	@Override
	public FaroNotification fetchByPrimaryKey(long faroNotificationId) {
		return fetchByPrimaryKey((Serializable)faroNotificationId);
	}

	@Override
	public Map<Serializable, FaroNotification> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {

		if (primaryKeys.isEmpty()) {
			return Collections.emptyMap();
		}

		Map<Serializable, FaroNotification> map =
			new HashMap<Serializable, FaroNotification>();

		if (primaryKeys.size() == 1) {
			Iterator<Serializable> iterator = primaryKeys.iterator();

			Serializable primaryKey = iterator.next();

			FaroNotification faroNotification = fetchByPrimaryKey(primaryKey);

			if (faroNotification != null) {
				map.put(primaryKey, faroNotification);
			}

			return map;
		}

		Set<Serializable> uncachedPrimaryKeys = null;

		for (Serializable primaryKey : primaryKeys) {
			Serializable serializable = entityCache.getResult(
				FaroNotificationModelImpl.ENTITY_CACHE_ENABLED,
				FaroNotificationImpl.class, primaryKey);

			if (serializable != nullModel) {
				if (serializable == null) {
					if (uncachedPrimaryKeys == null) {
						uncachedPrimaryKeys = new HashSet<Serializable>();
					}

					uncachedPrimaryKeys.add(primaryKey);
				}
				else {
					map.put(primaryKey, (FaroNotification)serializable);
				}
			}
		}

		if (uncachedPrimaryKeys == null) {
			return map;
		}

		StringBundler sb = new StringBundler(
			(uncachedPrimaryKeys.size() * 2) + 1);

		sb.append(_SQL_SELECT_FARONOTIFICATION_WHERE_PKS_IN);

		for (Serializable primaryKey : uncachedPrimaryKeys) {
			sb.append((long)primaryKey);

			sb.append(",");
		}

		sb.setIndex(sb.index() - 1);

		sb.append(")");

		String sql = sb.toString();

		Session session = null;

		try {
			session = openSession();

			Query query = session.createQuery(sql);

			for (FaroNotification faroNotification :
					(List<FaroNotification>)query.list()) {

				map.put(faroNotification.getPrimaryKeyObj(), faroNotification);

				cacheResult(faroNotification);

				uncachedPrimaryKeys.remove(faroNotification.getPrimaryKeyObj());
			}

			for (Serializable primaryKey : uncachedPrimaryKeys) {
				entityCache.putResult(
					FaroNotificationModelImpl.ENTITY_CACHE_ENABLED,
					FaroNotificationImpl.class, primaryKey, nullModel);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		return map;
	}

	/**
	 * Returns all the faro notifications.
	 *
	 * @return the faro notifications
	 */
	@Override
	public List<FaroNotification> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the faro notifications.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>FaroNotificationModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of faro notifications
	 * @param end the upper bound of the range of faro notifications (not inclusive)
	 * @return the range of faro notifications
	 */
	@Override
	public List<FaroNotification> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the faro notifications.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>FaroNotificationModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of faro notifications
	 * @param end the upper bound of the range of faro notifications (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of faro notifications
	 */
	@Override
	public List<FaroNotification> findAll(
		int start, int end,
		OrderByComparator<FaroNotification> orderByComparator) {

		return findAll(start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the faro notifications.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>FaroNotificationModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of faro notifications
	 * @param end the upper bound of the range of faro notifications (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of faro notifications
	 */
	@Override
	public List<FaroNotification> findAll(
		int start, int end,
		OrderByComparator<FaroNotification> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindAll;
				finderArgs = FINDER_ARGS_EMPTY;
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindAll;
			finderArgs = new Object[] {start, end, orderByComparator};
		}

		List<FaroNotification> list = null;

		if (useFinderCache) {
			list = (List<FaroNotification>)finderCache.getResult(
				finderPath, finderArgs, this);
		}

		if (list == null) {
			StringBundler sb = null;
			String sql = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					2 + (orderByComparator.getOrderByFields().length * 2));

				sb.append(_SQL_SELECT_FARONOTIFICATION);

				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);

				sql = sb.toString();
			}
			else {
				sql = _SQL_SELECT_FARONOTIFICATION;

				sql = sql.concat(FaroNotificationModelImpl.ORDER_BY_JPQL);
			}

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				list = (List<FaroNotification>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				if (useFinderCache) {
					finderCache.removeResult(finderPath, finderArgs);
				}

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Removes all the faro notifications from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (FaroNotification faroNotification : findAll()) {
			remove(faroNotification);
		}
	}

	/**
	 * Returns the number of faro notifications.
	 *
	 * @return the number of faro notifications
	 */
	@Override
	public int countAll() {
		Long count = (Long)finderCache.getResult(
			_finderPathCountAll, FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(_SQL_COUNT_FARONOTIFICATION);

				count = (Long)query.uniqueResult();

				finderCache.putResult(
					_finderPathCountAll, FINDER_ARGS_EMPTY, count);
			}
			catch (Exception exception) {
				finderCache.removeResult(
					_finderPathCountAll, FINDER_ARGS_EMPTY);

				throw processException(exception);
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
		return FaroNotificationModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the faro notification persistence.
	 */
	public void afterPropertiesSet() {
		_finderPathWithPaginationFindAll = new FinderPath(
			FaroNotificationModelImpl.ENTITY_CACHE_ENABLED,
			FaroNotificationModelImpl.FINDER_CACHE_ENABLED,
			FaroNotificationImpl.class, FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findAll", new String[0]);

		_finderPathWithoutPaginationFindAll = new FinderPath(
			FaroNotificationModelImpl.ENTITY_CACHE_ENABLED,
			FaroNotificationModelImpl.FINDER_CACHE_ENABLED,
			FaroNotificationImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll",
			new String[0]);

		_finderPathCountAll = new FinderPath(
			FaroNotificationModelImpl.ENTITY_CACHE_ENABLED,
			FaroNotificationModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll",
			new String[0]);
	}

	public void destroy() {
		entityCache.removeCache(FaroNotificationImpl.class.getName());

		finderCache.removeCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.removeCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@ServiceReference(type = EntityCache.class)
	protected EntityCache entityCache;

	@ServiceReference(type = FinderCache.class)
	protected FinderCache finderCache;

	private static final String _SQL_SELECT_FARONOTIFICATION =
		"SELECT faroNotification FROM FaroNotification faroNotification";

	private static final String _SQL_SELECT_FARONOTIFICATION_WHERE_PKS_IN =
		"SELECT faroNotification FROM FaroNotification faroNotification WHERE faroNotificationId IN (";

	private static final String _SQL_COUNT_FARONOTIFICATION =
		"SELECT COUNT(faroNotification) FROM FaroNotification faroNotification";

	private static final String _ORDER_BY_ENTITY_ALIAS = "faroNotification.";

	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY =
		"No FaroNotification exists with the primary key ";

	private static final Log _log = LogFactoryUtil.getLog(
		FaroNotificationPersistenceImpl.class);

	private static final Set<String> _badColumnNames = SetUtil.fromArray(
		new String[] {"read", "type"});

}