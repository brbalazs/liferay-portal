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
 * Provides the local service utility for FaroProject. This utility wraps
 * <code>com.liferay.osb.faro.service.impl.FaroProjectLocalServiceImpl</code> and
 * is an access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author Matthew Kong
 * @see FaroProjectLocalService
 * @generated
 */
public class FaroProjectLocalServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.osb.faro.service.impl.FaroProjectLocalServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * Adds the faro project to the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect FaroProjectLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param faroProject the faro project
	 * @return the faro project that was added
	 */
	public static com.liferay.osb.faro.model.FaroProject addFaroProject(
		com.liferay.osb.faro.model.FaroProject faroProject) {

		return getService().addFaroProject(faroProject);
	}

	public static com.liferay.osb.faro.model.FaroProject addFaroProject(
			long userId, String name, String accountKey, String accountName,
			String corpProjectName, String corpProjectUuid,
			java.util.List<String> emailAddressDomains, String friendlyURL,
			String serverLocation, String services, String state,
			String subscription, String weDeployKey)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addFaroProject(
			userId, name, accountKey, accountName, corpProjectName,
			corpProjectUuid, emailAddressDomains, friendlyURL, serverLocation,
			services, state, subscription, weDeployKey);
	}

	/**
	 * Creates a new faro project with the primary key. Does not add the faro project to the database.
	 *
	 * @param faroProjectId the primary key for the new faro project
	 * @return the new faro project
	 */
	public static com.liferay.osb.faro.model.FaroProject createFaroProject(
		long faroProjectId) {

		return getService().createFaroProject(faroProjectId);
	}

	/**
	 * Deletes the faro project from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect FaroProjectLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param faroProject the faro project
	 * @return the faro project that was removed
	 */
	public static com.liferay.osb.faro.model.FaroProject deleteFaroProject(
		com.liferay.osb.faro.model.FaroProject faroProject) {

		return getService().deleteFaroProject(faroProject);
	}

	/**
	 * Deletes the faro project with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect FaroProjectLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param faroProjectId the primary key of the faro project
	 * @return the faro project that was removed
	 * @throws PortalException if a faro project with the primary key could not be found
	 */
	public static com.liferay.osb.faro.model.FaroProject deleteFaroProject(
			long faroProjectId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deleteFaroProject(faroProjectId);
	}

	public static com.liferay.osb.faro.model.FaroProject
			deleteFaroProjectByGroupId(long groupId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deleteFaroProjectByGroupId(groupId);
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
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.osb.faro.model.impl.FaroProjectModelImpl</code>.
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
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.osb.faro.model.impl.FaroProjectModelImpl</code>.
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

	public static com.liferay.osb.faro.model.FaroProject fetchFaroProject(
		long faroProjectId) {

		return getService().fetchFaroProject(faroProjectId);
	}

	public static com.liferay.osb.faro.model.FaroProject
		fetchFaroProjectByCorpProjectUuid(String corpProjectUuid) {

		return getService().fetchFaroProjectByCorpProjectUuid(corpProjectUuid);
	}

	public static com.liferay.osb.faro.model.FaroProject
		fetchFaroProjectByGroupId(long groupId) {

		return getService().fetchFaroProjectByGroupId(groupId);
	}

	public static com.liferay.osb.faro.model.FaroProject
		fetchFaroProjectByWeDeployKey(String weDeployKey) {

		return getService().fetchFaroProjectByWeDeployKey(weDeployKey);
	}

	public static com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return getService().getActionableDynamicQuery();
	}

	/**
	 * Returns the faro project with the primary key.
	 *
	 * @param faroProjectId the primary key of the faro project
	 * @return the faro project
	 * @throws PortalException if a faro project with the primary key could not be found
	 */
	public static com.liferay.osb.faro.model.FaroProject getFaroProject(
			long faroProjectId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getFaroProject(faroProjectId);
	}

	public static com.liferay.osb.faro.model.FaroProject
			getFaroProjectByGroupId(long groupId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getFaroProjectByGroupId(groupId);
	}

	public static com.liferay.osb.faro.model.FaroProject
			getFaroProjectByWeDeployKey(String weDeployKey)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getFaroProjectByWeDeployKey(weDeployKey);
	}

	/**
	 * Returns a range of all the faro projects.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.osb.faro.model.impl.FaroProjectModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of faro projects
	 * @param end the upper bound of the range of faro projects (not inclusive)
	 * @return the range of faro projects
	 */
	public static java.util.List<com.liferay.osb.faro.model.FaroProject>
		getFaroProjects(int start, int end) {

		return getService().getFaroProjects(start, end);
	}

	public static java.util.List<com.liferay.osb.faro.model.FaroProject>
		getFaroProjects(String serverLocation) {

		return getService().getFaroProjects(serverLocation);
	}

	public static java.util.List<com.liferay.osb.faro.model.FaroProject>
		getFaroProjectsByEmailAddressDomain(String emailAddressDomains) {

		return getService().getFaroProjectsByEmailAddressDomain(
			emailAddressDomains);
	}

	public static java.util.List<com.liferay.osb.faro.model.FaroProject>
		getFaroProjectsByUserId(long userId) {

		return getService().getFaroProjectsByUserId(userId);
	}

	/**
	 * Returns the number of faro projects.
	 *
	 * @return the number of faro projects
	 */
	public static int getFaroProjectsCount() {
		return getService().getFaroProjectsCount();
	}

	public static
		com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery
			getIndexableActionableDynamicQuery() {

		return getService().getIndexableActionableDynamicQuery();
	}

	public static java.util.List<com.liferay.osb.faro.model.FaroProject>
			getJoinableFaroProjects(com.liferay.portal.kernel.model.User user)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getJoinableFaroProjects(user);
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

	public static void sendCreatedWorkspaceEmail(String weDeployKey)
		throws Exception {

		getService().sendCreatedWorkspaceEmail(weDeployKey);
	}

	/**
	 * Updates the faro project in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect FaroProjectLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param faroProject the faro project
	 * @return the faro project that was updated
	 */
	public static com.liferay.osb.faro.model.FaroProject updateFaroProject(
		com.liferay.osb.faro.model.FaroProject faroProject) {

		return getService().updateFaroProject(faroProject);
	}

	public static com.liferay.osb.faro.model.FaroProject updateState(
		long faroProjectId, String state) {

		return getService().updateState(faroProjectId, state);
	}

	public static com.liferay.osb.faro.model.FaroProject updateSubscription(
		long faroProjectId, String subscription) {

		return getService().updateSubscription(faroProjectId, subscription);
	}

	public static FaroProjectLocalService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker
		<FaroProjectLocalService, FaroProjectLocalService> _serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(FaroProjectLocalService.class);

		ServiceTracker<FaroProjectLocalService, FaroProjectLocalService>
			serviceTracker =
				new ServiceTracker
					<FaroProjectLocalService, FaroProjectLocalService>(
						bundle.getBundleContext(),
						FaroProjectLocalService.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}