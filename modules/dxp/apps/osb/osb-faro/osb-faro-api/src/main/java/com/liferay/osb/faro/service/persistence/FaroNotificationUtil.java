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

package com.liferay.osb.faro.service.persistence;

import com.liferay.osb.faro.model.FaroNotification;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.io.Serializable;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * The persistence utility for the faro notification service. This utility wraps <code>com.liferay.osb.faro.service.persistence.impl.FaroNotificationPersistenceImpl</code> and provides direct access to the database for CRUD operations. This utility should only be used by the service layer, as it must operate within a transaction. Never access this utility in a JSP, controller, model, or other front-end class.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Matthew Kong
 * @see FaroNotificationPersistence
 * @generated
 */
public class FaroNotificationUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#clearCache()
	 */
	public static void clearCache() {
		getPersistence().clearCache();
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#clearCache(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static void clearCache(FaroNotification faroNotification) {
		getPersistence().clearCache(faroNotification);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#countWithDynamicQuery(DynamicQuery)
	 */
	public static long countWithDynamicQuery(DynamicQuery dynamicQuery) {
		return getPersistence().countWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#fetchByPrimaryKeys(Set)
	 */
	public static Map<Serializable, FaroNotification> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {

		return getPersistence().fetchByPrimaryKeys(primaryKeys);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery)
	 */
	public static List<FaroNotification> findWithDynamicQuery(
		DynamicQuery dynamicQuery) {

		return getPersistence().findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int)
	 */
	public static List<FaroNotification> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return getPersistence().findWithDynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int, OrderByComparator)
	 */
	public static List<FaroNotification> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<FaroNotification> orderByComparator) {

		return getPersistence().findWithDynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static FaroNotification update(FaroNotification faroNotification) {
		return getPersistence().update(faroNotification);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel, ServiceContext)
	 */
	public static FaroNotification update(
		FaroNotification faroNotification, ServiceContext serviceContext) {

		return getPersistence().update(faroNotification, serviceContext);
	}

	/**
	 * Caches the faro notification in the entity cache if it is enabled.
	 *
	 * @param faroNotification the faro notification
	 */
	public static void cacheResult(FaroNotification faroNotification) {
		getPersistence().cacheResult(faroNotification);
	}

	/**
	 * Caches the faro notifications in the entity cache if it is enabled.
	 *
	 * @param faroNotifications the faro notifications
	 */
	public static void cacheResult(List<FaroNotification> faroNotifications) {
		getPersistence().cacheResult(faroNotifications);
	}

	/**
	 * Creates a new faro notification with the primary key. Does not add the faro notification to the database.
	 *
	 * @param faroNotificationId the primary key for the new faro notification
	 * @return the new faro notification
	 */
	public static FaroNotification create(long faroNotificationId) {
		return getPersistence().create(faroNotificationId);
	}

	/**
	 * Removes the faro notification with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param faroNotificationId the primary key of the faro notification
	 * @return the faro notification that was removed
	 * @throws NoSuchFaroNotificationException if a faro notification with the primary key could not be found
	 */
	public static FaroNotification remove(long faroNotificationId)
		throws com.liferay.osb.faro.exception.NoSuchFaroNotificationException {

		return getPersistence().remove(faroNotificationId);
	}

	public static FaroNotification updateImpl(
		FaroNotification faroNotification) {

		return getPersistence().updateImpl(faroNotification);
	}

	/**
	 * Returns the faro notification with the primary key or throws a <code>NoSuchFaroNotificationException</code> if it could not be found.
	 *
	 * @param faroNotificationId the primary key of the faro notification
	 * @return the faro notification
	 * @throws NoSuchFaroNotificationException if a faro notification with the primary key could not be found
	 */
	public static FaroNotification findByPrimaryKey(long faroNotificationId)
		throws com.liferay.osb.faro.exception.NoSuchFaroNotificationException {

		return getPersistence().findByPrimaryKey(faroNotificationId);
	}

	/**
	 * Returns the faro notification with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param faroNotificationId the primary key of the faro notification
	 * @return the faro notification, or <code>null</code> if a faro notification with the primary key could not be found
	 */
	public static FaroNotification fetchByPrimaryKey(long faroNotificationId) {
		return getPersistence().fetchByPrimaryKey(faroNotificationId);
	}

	/**
	 * Returns all the faro notifications.
	 *
	 * @return the faro notifications
	 */
	public static List<FaroNotification> findAll() {
		return getPersistence().findAll();
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
	public static List<FaroNotification> findAll(int start, int end) {
		return getPersistence().findAll(start, end);
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
	public static List<FaroNotification> findAll(
		int start, int end,
		OrderByComparator<FaroNotification> orderByComparator) {

		return getPersistence().findAll(start, end, orderByComparator);
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
	public static List<FaroNotification> findAll(
		int start, int end,
		OrderByComparator<FaroNotification> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findAll(
			start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Removes all the faro notifications from the database.
	 */
	public static void removeAll() {
		getPersistence().removeAll();
	}

	/**
	 * Returns the number of faro notifications.
	 *
	 * @return the number of faro notifications
	 */
	public static int countAll() {
		return getPersistence().countAll();
	}

	public static Set<String> getBadColumnNames() {
		return getPersistence().getBadColumnNames();
	}

	public static FaroNotificationPersistence getPersistence() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker
		<FaroNotificationPersistence, FaroNotificationPersistence>
			_serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(
			FaroNotificationPersistence.class);

		ServiceTracker<FaroNotificationPersistence, FaroNotificationPersistence>
			serviceTracker =
				new ServiceTracker
					<FaroNotificationPersistence, FaroNotificationPersistence>(
						bundle.getBundleContext(),
						FaroNotificationPersistence.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}