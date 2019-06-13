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

import com.liferay.commerce.bom.exception.NoSuchBOMFolderException;
import com.liferay.commerce.bom.model.CommerceBOMFolder;
import com.liferay.commerce.bom.model.impl.CommerceBOMFolderImpl;
import com.liferay.commerce.bom.model.impl.CommerceBOMFolderModelImpl;
import com.liferay.commerce.bom.service.persistence.CommerceBOMFolderPersistence;

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
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.spring.extender.service.ServiceReference;

import java.io.Serializable;

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
 * The persistence implementation for the commerce bom folder service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Luca Pellizzon
 * @see CommerceBOMFolderPersistence
 * @see com.liferay.commerce.bom.service.persistence.CommerceBOMFolderUtil
 * @generated
 */
@ProviderType
public class CommerceBOMFolderPersistenceImpl extends BasePersistenceImpl<CommerceBOMFolder>
	implements CommerceBOMFolderPersistence {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link CommerceBOMFolderUtil} to access the commerce bom folder persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY = CommerceBOMFolderImpl.class.getName();
	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List1";
	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List2";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_ALL = new FinderPath(CommerceBOMFolderModelImpl.ENTITY_CACHE_ENABLED,
			CommerceBOMFolderModelImpl.FINDER_CACHE_ENABLED,
			CommerceBOMFolderImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL = new FinderPath(CommerceBOMFolderModelImpl.ENTITY_CACHE_ENABLED,
			CommerceBOMFolderModelImpl.FINDER_CACHE_ENABLED,
			CommerceBOMFolderImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(CommerceBOMFolderModelImpl.ENTITY_CACHE_ENABLED,
			CommerceBOMFolderModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll", new String[0]);

	public CommerceBOMFolderPersistenceImpl() {
		setModelClass(CommerceBOMFolder.class);
	}

	/**
	 * Caches the commerce bom folder in the entity cache if it is enabled.
	 *
	 * @param commerceBOMFolder the commerce bom folder
	 */
	@Override
	public void cacheResult(CommerceBOMFolder commerceBOMFolder) {
		entityCache.putResult(CommerceBOMFolderModelImpl.ENTITY_CACHE_ENABLED,
			CommerceBOMFolderImpl.class, commerceBOMFolder.getPrimaryKey(),
			commerceBOMFolder);

		commerceBOMFolder.resetOriginalValues();
	}

	/**
	 * Caches the commerce bom folders in the entity cache if it is enabled.
	 *
	 * @param commerceBOMFolders the commerce bom folders
	 */
	@Override
	public void cacheResult(List<CommerceBOMFolder> commerceBOMFolders) {
		for (CommerceBOMFolder commerceBOMFolder : commerceBOMFolders) {
			if (entityCache.getResult(
						CommerceBOMFolderModelImpl.ENTITY_CACHE_ENABLED,
						CommerceBOMFolderImpl.class,
						commerceBOMFolder.getPrimaryKey()) == null) {
				cacheResult(commerceBOMFolder);
			}
			else {
				commerceBOMFolder.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all commerce bom folders.
	 *
	 * <p>
	 * The {@link EntityCache} and {@link FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		entityCache.clearCache(CommerceBOMFolderImpl.class);

		finderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the commerce bom folder.
	 *
	 * <p>
	 * The {@link EntityCache} and {@link FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(CommerceBOMFolder commerceBOMFolder) {
		entityCache.removeResult(CommerceBOMFolderModelImpl.ENTITY_CACHE_ENABLED,
			CommerceBOMFolderImpl.class, commerceBOMFolder.getPrimaryKey());

		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@Override
	public void clearCache(List<CommerceBOMFolder> commerceBOMFolders) {
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (CommerceBOMFolder commerceBOMFolder : commerceBOMFolders) {
			entityCache.removeResult(CommerceBOMFolderModelImpl.ENTITY_CACHE_ENABLED,
				CommerceBOMFolderImpl.class, commerceBOMFolder.getPrimaryKey());
		}
	}

	/**
	 * Creates a new commerce bom folder with the primary key. Does not add the commerce bom folder to the database.
	 *
	 * @param commerceBOMFolderId the primary key for the new commerce bom folder
	 * @return the new commerce bom folder
	 */
	@Override
	public CommerceBOMFolder create(long commerceBOMFolderId) {
		CommerceBOMFolder commerceBOMFolder = new CommerceBOMFolderImpl();

		commerceBOMFolder.setNew(true);
		commerceBOMFolder.setPrimaryKey(commerceBOMFolderId);

		commerceBOMFolder.setCompanyId(companyProvider.getCompanyId());

		return commerceBOMFolder;
	}

	/**
	 * Removes the commerce bom folder with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param commerceBOMFolderId the primary key of the commerce bom folder
	 * @return the commerce bom folder that was removed
	 * @throws NoSuchBOMFolderException if a commerce bom folder with the primary key could not be found
	 */
	@Override
	public CommerceBOMFolder remove(long commerceBOMFolderId)
		throws NoSuchBOMFolderException {
		return remove((Serializable)commerceBOMFolderId);
	}

	/**
	 * Removes the commerce bom folder with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the commerce bom folder
	 * @return the commerce bom folder that was removed
	 * @throws NoSuchBOMFolderException if a commerce bom folder with the primary key could not be found
	 */
	@Override
	public CommerceBOMFolder remove(Serializable primaryKey)
		throws NoSuchBOMFolderException {
		Session session = null;

		try {
			session = openSession();

			CommerceBOMFolder commerceBOMFolder = (CommerceBOMFolder)session.get(CommerceBOMFolderImpl.class,
					primaryKey);

			if (commerceBOMFolder == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchBOMFolderException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
					primaryKey);
			}

			return remove(commerceBOMFolder);
		}
		catch (NoSuchBOMFolderException nsee) {
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
	protected CommerceBOMFolder removeImpl(CommerceBOMFolder commerceBOMFolder) {
		Session session = null;

		try {
			session = openSession();

			if (!session.contains(commerceBOMFolder)) {
				commerceBOMFolder = (CommerceBOMFolder)session.get(CommerceBOMFolderImpl.class,
						commerceBOMFolder.getPrimaryKeyObj());
			}

			if (commerceBOMFolder != null) {
				session.delete(commerceBOMFolder);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		if (commerceBOMFolder != null) {
			clearCache(commerceBOMFolder);
		}

		return commerceBOMFolder;
	}

	@Override
	public CommerceBOMFolder updateImpl(CommerceBOMFolder commerceBOMFolder) {
		boolean isNew = commerceBOMFolder.isNew();

		if (!(commerceBOMFolder instanceof CommerceBOMFolderModelImpl)) {
			InvocationHandler invocationHandler = null;

			if (ProxyUtil.isProxyClass(commerceBOMFolder.getClass())) {
				invocationHandler = ProxyUtil.getInvocationHandler(commerceBOMFolder);

				throw new IllegalArgumentException(
					"Implement ModelWrapper in commerceBOMFolder proxy " +
					invocationHandler.getClass());
			}

			throw new IllegalArgumentException(
				"Implement ModelWrapper in custom CommerceBOMFolder implementation " +
				commerceBOMFolder.getClass());
		}

		CommerceBOMFolderModelImpl commerceBOMFolderModelImpl = (CommerceBOMFolderModelImpl)commerceBOMFolder;

		ServiceContext serviceContext = ServiceContextThreadLocal.getServiceContext();

		Date now = new Date();

		if (isNew && (commerceBOMFolder.getCreateDate() == null)) {
			if (serviceContext == null) {
				commerceBOMFolder.setCreateDate(now);
			}
			else {
				commerceBOMFolder.setCreateDate(serviceContext.getCreateDate(
						now));
			}
		}

		if (!commerceBOMFolderModelImpl.hasSetModifiedDate()) {
			if (serviceContext == null) {
				commerceBOMFolder.setModifiedDate(now);
			}
			else {
				commerceBOMFolder.setModifiedDate(serviceContext.getModifiedDate(
						now));
			}
		}

		Session session = null;

		try {
			session = openSession();

			if (commerceBOMFolder.isNew()) {
				session.save(commerceBOMFolder);

				commerceBOMFolder.setNew(false);
			}
			else {
				commerceBOMFolder = (CommerceBOMFolder)session.merge(commerceBOMFolder);
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

		entityCache.putResult(CommerceBOMFolderModelImpl.ENTITY_CACHE_ENABLED,
			CommerceBOMFolderImpl.class, commerceBOMFolder.getPrimaryKey(),
			commerceBOMFolder, false);

		commerceBOMFolder.resetOriginalValues();

		return commerceBOMFolder;
	}

	/**
	 * Returns the commerce bom folder with the primary key or throws a {@link com.liferay.portal.kernel.exception.NoSuchModelException} if it could not be found.
	 *
	 * @param primaryKey the primary key of the commerce bom folder
	 * @return the commerce bom folder
	 * @throws NoSuchBOMFolderException if a commerce bom folder with the primary key could not be found
	 */
	@Override
	public CommerceBOMFolder findByPrimaryKey(Serializable primaryKey)
		throws NoSuchBOMFolderException {
		CommerceBOMFolder commerceBOMFolder = fetchByPrimaryKey(primaryKey);

		if (commerceBOMFolder == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchBOMFolderException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
				primaryKey);
		}

		return commerceBOMFolder;
	}

	/**
	 * Returns the commerce bom folder with the primary key or throws a {@link NoSuchBOMFolderException} if it could not be found.
	 *
	 * @param commerceBOMFolderId the primary key of the commerce bom folder
	 * @return the commerce bom folder
	 * @throws NoSuchBOMFolderException if a commerce bom folder with the primary key could not be found
	 */
	@Override
	public CommerceBOMFolder findByPrimaryKey(long commerceBOMFolderId)
		throws NoSuchBOMFolderException {
		return findByPrimaryKey((Serializable)commerceBOMFolderId);
	}

	/**
	 * Returns the commerce bom folder with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the commerce bom folder
	 * @return the commerce bom folder, or <code>null</code> if a commerce bom folder with the primary key could not be found
	 */
	@Override
	public CommerceBOMFolder fetchByPrimaryKey(Serializable primaryKey) {
		Serializable serializable = entityCache.getResult(CommerceBOMFolderModelImpl.ENTITY_CACHE_ENABLED,
				CommerceBOMFolderImpl.class, primaryKey);

		if (serializable == nullModel) {
			return null;
		}

		CommerceBOMFolder commerceBOMFolder = (CommerceBOMFolder)serializable;

		if (commerceBOMFolder == null) {
			Session session = null;

			try {
				session = openSession();

				commerceBOMFolder = (CommerceBOMFolder)session.get(CommerceBOMFolderImpl.class,
						primaryKey);

				if (commerceBOMFolder != null) {
					cacheResult(commerceBOMFolder);
				}
				else {
					entityCache.putResult(CommerceBOMFolderModelImpl.ENTITY_CACHE_ENABLED,
						CommerceBOMFolderImpl.class, primaryKey, nullModel);
				}
			}
			catch (Exception e) {
				entityCache.removeResult(CommerceBOMFolderModelImpl.ENTITY_CACHE_ENABLED,
					CommerceBOMFolderImpl.class, primaryKey);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return commerceBOMFolder;
	}

	/**
	 * Returns the commerce bom folder with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param commerceBOMFolderId the primary key of the commerce bom folder
	 * @return the commerce bom folder, or <code>null</code> if a commerce bom folder with the primary key could not be found
	 */
	@Override
	public CommerceBOMFolder fetchByPrimaryKey(long commerceBOMFolderId) {
		return fetchByPrimaryKey((Serializable)commerceBOMFolderId);
	}

	@Override
	public Map<Serializable, CommerceBOMFolder> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {
		if (primaryKeys.isEmpty()) {
			return Collections.emptyMap();
		}

		Map<Serializable, CommerceBOMFolder> map = new HashMap<Serializable, CommerceBOMFolder>();

		if (primaryKeys.size() == 1) {
			Iterator<Serializable> iterator = primaryKeys.iterator();

			Serializable primaryKey = iterator.next();

			CommerceBOMFolder commerceBOMFolder = fetchByPrimaryKey(primaryKey);

			if (commerceBOMFolder != null) {
				map.put(primaryKey, commerceBOMFolder);
			}

			return map;
		}

		Set<Serializable> uncachedPrimaryKeys = null;

		for (Serializable primaryKey : primaryKeys) {
			Serializable serializable = entityCache.getResult(CommerceBOMFolderModelImpl.ENTITY_CACHE_ENABLED,
					CommerceBOMFolderImpl.class, primaryKey);

			if (serializable != nullModel) {
				if (serializable == null) {
					if (uncachedPrimaryKeys == null) {
						uncachedPrimaryKeys = new HashSet<Serializable>();
					}

					uncachedPrimaryKeys.add(primaryKey);
				}
				else {
					map.put(primaryKey, (CommerceBOMFolder)serializable);
				}
			}
		}

		if (uncachedPrimaryKeys == null) {
			return map;
		}

		StringBundler query = new StringBundler((uncachedPrimaryKeys.size() * 2) +
				1);

		query.append(_SQL_SELECT_COMMERCEBOMFOLDER_WHERE_PKS_IN);

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

			for (CommerceBOMFolder commerceBOMFolder : (List<CommerceBOMFolder>)q.list()) {
				map.put(commerceBOMFolder.getPrimaryKeyObj(), commerceBOMFolder);

				cacheResult(commerceBOMFolder);

				uncachedPrimaryKeys.remove(commerceBOMFolder.getPrimaryKeyObj());
			}

			for (Serializable primaryKey : uncachedPrimaryKeys) {
				entityCache.putResult(CommerceBOMFolderModelImpl.ENTITY_CACHE_ENABLED,
					CommerceBOMFolderImpl.class, primaryKey, nullModel);
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
	 * Returns all the commerce bom folders.
	 *
	 * @return the commerce bom folders
	 */
	@Override
	public List<CommerceBOMFolder> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the commerce bom folders.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link CommerceBOMFolderModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of commerce bom folders
	 * @param end the upper bound of the range of commerce bom folders (not inclusive)
	 * @return the range of commerce bom folders
	 */
	@Override
	public List<CommerceBOMFolder> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the commerce bom folders.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link CommerceBOMFolderModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of commerce bom folders
	 * @param end the upper bound of the range of commerce bom folders (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of commerce bom folders
	 */
	@Override
	public List<CommerceBOMFolder> findAll(int start, int end,
		OrderByComparator<CommerceBOMFolder> orderByComparator) {
		return findAll(start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the commerce bom folders.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link CommerceBOMFolderModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of commerce bom folders
	 * @param end the upper bound of the range of commerce bom folders (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param retrieveFromCache whether to retrieve from the finder cache
	 * @return the ordered range of commerce bom folders
	 */
	@Override
	public List<CommerceBOMFolder> findAll(int start, int end,
		OrderByComparator<CommerceBOMFolder> orderByComparator,
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

		List<CommerceBOMFolder> list = null;

		if (retrieveFromCache) {
			list = (List<CommerceBOMFolder>)finderCache.getResult(finderPath,
					finderArgs, this);
		}

		if (list == null) {
			StringBundler query = null;
			String sql = null;

			if (orderByComparator != null) {
				query = new StringBundler(2 +
						(orderByComparator.getOrderByFields().length * 2));

				query.append(_SQL_SELECT_COMMERCEBOMFOLDER);

				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);

				sql = query.toString();
			}
			else {
				sql = _SQL_SELECT_COMMERCEBOMFOLDER;

				if (pagination) {
					sql = sql.concat(CommerceBOMFolderModelImpl.ORDER_BY_JPQL);
				}
			}

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				if (!pagination) {
					list = (List<CommerceBOMFolder>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<CommerceBOMFolder>)QueryUtil.list(q,
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
	 * Removes all the commerce bom folders from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (CommerceBOMFolder commerceBOMFolder : findAll()) {
			remove(commerceBOMFolder);
		}
	}

	/**
	 * Returns the number of commerce bom folders.
	 *
	 * @return the number of commerce bom folders
	 */
	@Override
	public int countAll() {
		Long count = (Long)finderCache.getResult(FINDER_PATH_COUNT_ALL,
				FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(_SQL_COUNT_COMMERCEBOMFOLDER);

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
	protected Map<String, Integer> getTableColumnsMap() {
		return CommerceBOMFolderModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the commerce bom folder persistence.
	 */
	public void afterPropertiesSet() {
	}

	public void destroy() {
		entityCache.removeCache(CommerceBOMFolderImpl.class.getName());
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
	private static final String _SQL_SELECT_COMMERCEBOMFOLDER = "SELECT commerceBOMFolder FROM CommerceBOMFolder commerceBOMFolder";
	private static final String _SQL_SELECT_COMMERCEBOMFOLDER_WHERE_PKS_IN = "SELECT commerceBOMFolder FROM CommerceBOMFolder commerceBOMFolder WHERE commerceBOMFolderId IN (";
	private static final String _SQL_COUNT_COMMERCEBOMFOLDER = "SELECT COUNT(commerceBOMFolder) FROM CommerceBOMFolder commerceBOMFolder";
	private static final String _ORDER_BY_ENTITY_ALIAS = "commerceBOMFolder.";
	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY = "No CommerceBOMFolder exists with the primary key ";
	private static final Log _log = LogFactoryUtil.getLog(CommerceBOMFolderPersistenceImpl.class);
}