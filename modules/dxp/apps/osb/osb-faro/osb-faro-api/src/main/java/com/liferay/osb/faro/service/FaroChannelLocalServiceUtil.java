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
 * Provides the local service utility for FaroChannel. This utility wraps
 * <code>com.liferay.osb.faro.service.impl.FaroChannelLocalServiceImpl</code> and
 * is an access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author Matthew Kong
 * @see FaroChannelLocalService
 * @generated
 */
public class FaroChannelLocalServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.osb.faro.service.impl.FaroChannelLocalServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * Adds the faro channel to the database. Also notifies the appropriate model listeners.
	 *
	 * @param faroChannel the faro channel
	 * @return the faro channel that was added
	 */
	public static com.liferay.osb.faro.model.FaroChannel addFaroChannel(
		com.liferay.osb.faro.model.FaroChannel faroChannel) {

		return getService().addFaroChannel(faroChannel);
	}

	public static com.liferay.osb.faro.model.FaroChannel addFaroChannel(
			long userId, String name, String channelId, long workspaceGroupId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addFaroChannel(
			userId, name, channelId, workspaceGroupId);
	}

	public static void addUsers(
			long companyId, String channelId,
			java.util.List<Long> invitedUserIds, long userId)
		throws com.liferay.portal.kernel.exception.PortalException {

		getService().addUsers(companyId, channelId, invitedUserIds, userId);
	}

	public static int countFaroUsers(
			String channelId, boolean available, String query,
			java.util.List<Integer> statuses)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().countFaroUsers(
			channelId, available, query, statuses);
	}

	/**
	 * Creates a new faro channel with the primary key. Does not add the faro channel to the database.
	 *
	 * @param faroChannelId the primary key for the new faro channel
	 * @return the new faro channel
	 */
	public static com.liferay.osb.faro.model.FaroChannel createFaroChannel(
		long faroChannelId) {

		return getService().createFaroChannel(faroChannelId);
	}

	/**
	 * Deletes the faro channel from the database. Also notifies the appropriate model listeners.
	 *
	 * @param faroChannel the faro channel
	 * @return the faro channel that was removed
	 * @throws PortalException
	 */
	public static com.liferay.osb.faro.model.FaroChannel deleteFaroChannel(
			com.liferay.osb.faro.model.FaroChannel faroChannel)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deleteFaroChannel(faroChannel);
	}

	/**
	 * Deletes the faro channel with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param faroChannelId the primary key of the faro channel
	 * @return the faro channel that was removed
	 * @throws PortalException if a faro channel with the primary key could not be found
	 */
	public static com.liferay.osb.faro.model.FaroChannel deleteFaroChannel(
			long faroChannelId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deleteFaroChannel(faroChannelId);
	}

	public static com.liferay.osb.faro.model.FaroChannel deleteFaroChannel(
			String channelId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deleteFaroChannel(channelId);
	}

	public static void deleteFaroChannels(long workspaceGroupId)
		throws com.liferay.portal.kernel.exception.PortalException {

		getService().deleteFaroChannels(workspaceGroupId);
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
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.osb.faro.model.impl.FaroChannelModelImpl</code>.
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
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.osb.faro.model.impl.FaroChannelModelImpl</code>.
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

	public static com.liferay.osb.faro.model.FaroChannel fetchFaroChannel(
		long faroChannelId) {

		return getService().fetchFaroChannel(faroChannelId);
	}

	public static java.util.List<com.liferay.osb.faro.model.FaroUser>
			findFaroUsers(
				String channelId, boolean available, String query,
				java.util.List<Integer> statuses, int start, int end,
				com.liferay.portal.kernel.util.OrderByComparator
					<com.liferay.osb.faro.model.FaroUser> orderByComparator)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().findFaroUsers(
			channelId, available, query, statuses, start, end,
			orderByComparator);
	}

	public static com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return getService().getActionableDynamicQuery();
	}

	/**
	 * Returns the faro channel with the primary key.
	 *
	 * @param faroChannelId the primary key of the faro channel
	 * @return the faro channel
	 * @throws PortalException if a faro channel with the primary key could not be found
	 */
	public static com.liferay.osb.faro.model.FaroChannel getFaroChannel(
			long faroChannelId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getFaroChannel(faroChannelId);
	}

	public static com.liferay.osb.faro.model.FaroChannel getFaroChannel(
			String channelId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getFaroChannel(channelId);
	}

	/**
	 * Returns a range of all the faro channels.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.osb.faro.model.impl.FaroChannelModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of faro channels
	 * @param end the upper bound of the range of faro channels (not inclusive)
	 * @return the range of faro channels
	 */
	public static java.util.List<com.liferay.osb.faro.model.FaroChannel>
		getFaroChannels(int start, int end) {

		return getService().getFaroChannels(start, end);
	}

	/**
	 * Returns the number of faro channels.
	 *
	 * @return the number of faro channels
	 */
	public static int getFaroChannelsCount() {
		return getService().getFaroChannelsCount();
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

	public static void removeUsers(
			String channelId, java.util.List<Long> userIds)
		throws com.liferay.portal.kernel.exception.PortalException {

		getService().removeUsers(channelId, userIds);
	}

	public static java.util.List<com.liferay.osb.faro.model.FaroChannel> search(
		long groupId, String query, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator
			<com.liferay.osb.faro.model.FaroChannel> orderByComparator) {

		return getService().search(
			groupId, query, start, end, orderByComparator);
	}

	public static int searchCount(long groupId, String query) {
		return getService().searchCount(groupId, query);
	}

	/**
	 * Updates the faro channel in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * @param faroChannel the faro channel
	 * @return the faro channel that was updated
	 */
	public static com.liferay.osb.faro.model.FaroChannel updateFaroChannel(
		com.liferay.osb.faro.model.FaroChannel faroChannel) {

		return getService().updateFaroChannel(faroChannel);
	}

	public static FaroChannelLocalService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker
		<FaroChannelLocalService, FaroChannelLocalService> _serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(FaroChannelLocalService.class);

		ServiceTracker<FaroChannelLocalService, FaroChannelLocalService>
			serviceTracker =
				new ServiceTracker
					<FaroChannelLocalService, FaroChannelLocalService>(
						bundle.getBundleContext(),
						FaroChannelLocalService.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}