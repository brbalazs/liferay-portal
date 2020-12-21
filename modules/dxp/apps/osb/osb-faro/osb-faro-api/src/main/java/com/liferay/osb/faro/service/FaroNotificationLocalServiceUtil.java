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

package com.liferay.osb.faro.service;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Provides the local service utility for FaroNotification. This utility wraps
 * <code>com.liferay.osb.faro.service.impl.FaroNotificationLocalServiceImpl</code> and
 * is an access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author Matthew Kong
 * @see FaroNotificationLocalService
 * @generated
 */
public class FaroNotificationLocalServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.osb.faro.service.impl.FaroNotificationLocalServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * Adds the faro notification to the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect FaroNotificationLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param faroNotification the faro notification
	 * @return the faro notification that was added
	 */
	public static com.liferay.osb.faro.model.FaroNotification
		addFaroNotification(
			com.liferay.osb.faro.model.FaroNotification faroNotification) {

		return getService().addFaroNotification(faroNotification);
	}

	public static com.liferay.osb.faro.model.FaroNotification
		addFaroNotification(
			long userId, long groupId, long ownerId, String scope, String type,
			String subtype) {

		return getService().addFaroNotification(
			userId, groupId, ownerId, scope, type, subtype);
	}

	public static void clearDismissedNotifications() {
		getService().clearDismissedNotifications();
	}

	/**
	 * Creates a new faro notification with the primary key. Does not add the faro notification to the database.
	 *
	 * @param faroNotificationId the primary key for the new faro notification
	 * @return the new faro notification
	 */
	public static com.liferay.osb.faro.model.FaroNotification
		createFaroNotification(long faroNotificationId) {

		return getService().createFaroNotification(faroNotificationId);
	}

	/**
	 * Deletes the faro notification from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect FaroNotificationLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param faroNotification the faro notification
	 * @return the faro notification that was removed
	 */
	public static com.liferay.osb.faro.model.FaroNotification
		deleteFaroNotification(
			com.liferay.osb.faro.model.FaroNotification faroNotification) {

		return getService().deleteFaroNotification(faroNotification);
	}

	/**
	 * Deletes the faro notification with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect FaroNotificationLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param faroNotificationId the primary key of the faro notification
	 * @return the faro notification that was removed
	 * @throws PortalException if a faro notification with the primary key could not be found
	 */
	public static com.liferay.osb.faro.model.FaroNotification
			deleteFaroNotification(long faroNotificationId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deleteFaroNotification(faroNotificationId);
	}

	/**
	 * @throws PortalException
	 */
	public static com.liferay.portal.kernel.model.PersistedModel
			deletePersistedModel(
				com.liferay.portal.kernel.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deletePersistedModel(persistedModel);
	}

	public static com.liferay.portal.kernel.dao.orm.DynamicQuery
		dynamicQuery() {

		return getService().dynamicQuery();
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	public static <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return getService().dynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.osb.faro.model.impl.FaroNotificationModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	public static <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) {

		return getService().dynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.osb.faro.model.impl.FaroNotificationModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	public static <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<T> orderByComparator) {

		return getService().dynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	public static long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return getService().dynamicQueryCount(dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	public static long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection) {

		return getService().dynamicQueryCount(dynamicQuery, projection);
	}

	public static com.liferay.osb.faro.model.FaroNotification
		fetchFaroNotification(long faroNotificationId) {

		return getService().fetchFaroNotification(faroNotificationId);
	}

	public static java.util.List<com.liferay.osb.faro.model.FaroNotification>
		findFaroNotificationsLast30Days(long groupId, long userId) {

		return getService().findFaroNotificationsLast30Days(groupId, userId);
	}

	public static com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return getService().getActionableDynamicQuery();
	}

	/**
	 * Returns the faro notification with the primary key.
	 *
	 * @param faroNotificationId the primary key of the faro notification
	 * @return the faro notification
	 * @throws PortalException if a faro notification with the primary key could not be found
	 */
	public static com.liferay.osb.faro.model.FaroNotification
			getFaroNotification(long faroNotificationId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getFaroNotification(faroNotificationId);
	}

	/**
	 * Returns a range of all the faro notifications.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.osb.faro.model.impl.FaroNotificationModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of faro notifications
	 * @param end the upper bound of the range of faro notifications (not inclusive)
	 * @return the range of faro notifications
	 */
	public static java.util.List<com.liferay.osb.faro.model.FaroNotification>
		getFaroNotifications(int start, int end) {

		return getService().getFaroNotifications(start, end);
	}

	/**
	 * Returns the number of faro notifications.
	 *
	 * @return the number of faro notifications
	 */
	public static int getFaroNotificationsCount() {
		return getService().getFaroNotificationsCount();
	}

	public static
		com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery
			getIndexableActionableDynamicQuery() {

		return getService().getIndexableActionableDynamicQuery();
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	/**
	 * @throws PortalException
	 */
	public static com.liferay.portal.kernel.model.PersistedModel
			getPersistedModel(java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getPersistedModel(primaryKeyObj);
	}

	/**
	 * Updates the faro notification in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect FaroNotificationLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param faroNotification the faro notification
	 * @return the faro notification that was updated
	 */
	public static com.liferay.osb.faro.model.FaroNotification
		updateFaroNotification(
			com.liferay.osb.faro.model.FaroNotification faroNotification) {

		return getService().updateFaroNotification(faroNotification);
	}

	public static FaroNotificationLocalService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker
		<FaroNotificationLocalService, FaroNotificationLocalService>
			_serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(
			FaroNotificationLocalService.class);

		ServiceTracker
			<FaroNotificationLocalService, FaroNotificationLocalService>
				serviceTracker =
					new ServiceTracker
						<FaroNotificationLocalService,
						 FaroNotificationLocalService>(
							 bundle.getBundleContext(),
							 FaroNotificationLocalService.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}