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
 * Provides the local service utility for FaroPreferences. This utility wraps
 * <code>com.liferay.osb.faro.service.impl.FaroPreferencesLocalServiceImpl</code> and
 * is an access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author Matthew Kong
 * @see FaroPreferencesLocalService
 * @generated
 */
public class FaroPreferencesLocalServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.osb.faro.service.impl.FaroPreferencesLocalServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * Adds the faro preferences to the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect FaroPreferencesLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param faroPreferences the faro preferences
	 * @return the faro preferences that was added
	 */
	public static com.liferay.osb.faro.model.FaroPreferences addFaroPreferences(
		com.liferay.osb.faro.model.FaroPreferences faroPreferences) {

		return getService().addFaroPreferences(faroPreferences);
	}

	/**
	 * Creates a new faro preferences with the primary key. Does not add the faro preferences to the database.
	 *
	 * @param faroPreferencesId the primary key for the new faro preferences
	 * @return the new faro preferences
	 */
	public static com.liferay.osb.faro.model.FaroPreferences
		createFaroPreferences(long faroPreferencesId) {

		return getService().createFaroPreferences(faroPreferencesId);
	}

	/**
	 * Deletes the faro preferences from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect FaroPreferencesLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param faroPreferences the faro preferences
	 * @return the faro preferences that was removed
	 */
	public static com.liferay.osb.faro.model.FaroPreferences
		deleteFaroPreferences(
			com.liferay.osb.faro.model.FaroPreferences faroPreferences) {

		return getService().deleteFaroPreferences(faroPreferences);
	}

	/**
	 * Deletes the faro preferences with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect FaroPreferencesLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param faroPreferencesId the primary key of the faro preferences
	 * @return the faro preferences that was removed
	 * @throws PortalException if a faro preferences with the primary key could not be found
	 */
	public static com.liferay.osb.faro.model.FaroPreferences
			deleteFaroPreferences(long faroPreferencesId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deleteFaroPreferences(faroPreferencesId);
	}

	public static com.liferay.osb.faro.model.FaroPreferences
		deleteFaroPreferences(long groupId, long ownerId) {

		return getService().deleteFaroPreferences(groupId, ownerId);
	}

	public static void deleteFaroPreferencesByGroupId(long groupId) {
		getService().deleteFaroPreferencesByGroupId(groupId);
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
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.osb.faro.model.impl.FaroPreferencesModelImpl</code>.
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
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.osb.faro.model.impl.FaroPreferencesModelImpl</code>.
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

	public static com.liferay.osb.faro.model.FaroPreferences
		fetchFaroPreferences(long faroPreferencesId) {

		return getService().fetchFaroPreferences(faroPreferencesId);
	}

	public static com.liferay.osb.faro.model.FaroPreferences
		fetchFaroPreferences(long groupId, long ownerId) {

		return getService().fetchFaroPreferences(groupId, ownerId);
	}

	public static com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return getService().getActionableDynamicQuery();
	}

	/**
	 * Returns the faro preferences with the primary key.
	 *
	 * @param faroPreferencesId the primary key of the faro preferences
	 * @return the faro preferences
	 * @throws PortalException if a faro preferences with the primary key could not be found
	 */
	public static com.liferay.osb.faro.model.FaroPreferences getFaroPreferences(
			long faroPreferencesId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getFaroPreferences(faroPreferencesId);
	}

	/**
	 * Returns a range of all the faro preferenceses.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.osb.faro.model.impl.FaroPreferencesModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of faro preferenceses
	 * @param end the upper bound of the range of faro preferenceses (not inclusive)
	 * @return the range of faro preferenceses
	 */
	public static java.util.List<com.liferay.osb.faro.model.FaroPreferences>
		getFaroPreferenceses(int start, int end) {

		return getService().getFaroPreferenceses(start, end);
	}

	/**
	 * Returns the number of faro preferenceses.
	 *
	 * @return the number of faro preferenceses
	 */
	public static int getFaroPreferencesesCount() {
		return getService().getFaroPreferencesesCount();
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

	public static com.liferay.osb.faro.model.FaroPreferences savePreferences(
			long userId, long groupId, long ownerId, String preferences)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().savePreferences(
			userId, groupId, ownerId, preferences);
	}

	/**
	 * Updates the faro preferences in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect FaroPreferencesLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param faroPreferences the faro preferences
	 * @return the faro preferences that was updated
	 */
	public static com.liferay.osb.faro.model.FaroPreferences
		updateFaroPreferences(
			com.liferay.osb.faro.model.FaroPreferences faroPreferences) {

		return getService().updateFaroPreferences(faroPreferences);
	}

	public static FaroPreferencesLocalService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker
		<FaroPreferencesLocalService, FaroPreferencesLocalService>
			_serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(
			FaroPreferencesLocalService.class);

		ServiceTracker<FaroPreferencesLocalService, FaroPreferencesLocalService>
			serviceTracker =
				new ServiceTracker
					<FaroPreferencesLocalService, FaroPreferencesLocalService>(
						bundle.getBundleContext(),
						FaroPreferencesLocalService.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}