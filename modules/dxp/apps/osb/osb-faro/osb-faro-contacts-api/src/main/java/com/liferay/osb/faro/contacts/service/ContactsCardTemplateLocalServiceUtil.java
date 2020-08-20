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

package com.liferay.osb.faro.contacts.service;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Provides the local service utility for ContactsCardTemplate. This utility wraps
 * <code>com.liferay.osb.faro.contacts.service.impl.ContactsCardTemplateLocalServiceImpl</code> and
 * is an access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author Shinn Lok
 * @see ContactsCardTemplateLocalService
 * @generated
 */
public class ContactsCardTemplateLocalServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.osb.faro.contacts.service.impl.ContactsCardTemplateLocalServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * Adds the contacts card template to the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect ContactsCardTemplateLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param contactsCardTemplate the contacts card template
	 * @return the contacts card template that was added
	 */
	public static com.liferay.osb.faro.contacts.model.ContactsCardTemplate
		addContactsCardTemplate(
			com.liferay.osb.faro.contacts.model.ContactsCardTemplate
				contactsCardTemplate) {

		return getService().addContactsCardTemplate(contactsCardTemplate);
	}

	public static com.liferay.osb.faro.contacts.model.ContactsCardTemplate
		addContactsCardTemplate(
			long groupId, long userId, String name, String settings, int type) {

		return getService().addContactsCardTemplate(
			groupId, userId, name, settings, type);
	}

	/**
	 * Creates a new contacts card template with the primary key. Does not add the contacts card template to the database.
	 *
	 * @param contactsCardTemplateId the primary key for the new contacts card template
	 * @return the new contacts card template
	 */
	public static com.liferay.osb.faro.contacts.model.ContactsCardTemplate
		createContactsCardTemplate(long contactsCardTemplateId) {

		return getService().createContactsCardTemplate(contactsCardTemplateId);
	}

	/**
	 * Deletes the contacts card template from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect ContactsCardTemplateLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param contactsCardTemplate the contacts card template
	 * @return the contacts card template that was removed
	 */
	public static com.liferay.osb.faro.contacts.model.ContactsCardTemplate
		deleteContactsCardTemplate(
			com.liferay.osb.faro.contacts.model.ContactsCardTemplate
				contactsCardTemplate) {

		return getService().deleteContactsCardTemplate(contactsCardTemplate);
	}

	/**
	 * Deletes the contacts card template with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect ContactsCardTemplateLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param contactsCardTemplateId the primary key of the contacts card template
	 * @return the contacts card template that was removed
	 * @throws PortalException if a contacts card template with the primary key could not be found
	 */
	public static com.liferay.osb.faro.contacts.model.ContactsCardTemplate
			deleteContactsCardTemplate(long contactsCardTemplateId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deleteContactsCardTemplate(contactsCardTemplateId);
	}

	public static void deleteContactsCardTemplates(long groupId) {
		getService().deleteContactsCardTemplates(groupId);
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
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.osb.faro.contacts.model.impl.ContactsCardTemplateModelImpl</code>.
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
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.osb.faro.contacts.model.impl.ContactsCardTemplateModelImpl</code>.
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

	public static com.liferay.osb.faro.contacts.model.ContactsCardTemplate
		fetchContactsCardTemplate(long contactsCardTemplateId) {

		return getService().fetchContactsCardTemplate(contactsCardTemplateId);
	}

	public static com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return getService().getActionableDynamicQuery();
	}

	/**
	 * Returns the contacts card template with the primary key.
	 *
	 * @param contactsCardTemplateId the primary key of the contacts card template
	 * @return the contacts card template
	 * @throws PortalException if a contacts card template with the primary key could not be found
	 */
	public static com.liferay.osb.faro.contacts.model.ContactsCardTemplate
			getContactsCardTemplate(long contactsCardTemplateId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getContactsCardTemplate(contactsCardTemplateId);
	}

	/**
	 * Returns a range of all the contacts card templates.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.osb.faro.contacts.model.impl.ContactsCardTemplateModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of contacts card templates
	 * @param end the upper bound of the range of contacts card templates (not inclusive)
	 * @return the range of contacts card templates
	 */
	public static java.util.List
		<com.liferay.osb.faro.contacts.model.ContactsCardTemplate>
			getContactsCardTemplates(int start, int end) {

		return getService().getContactsCardTemplates(start, end);
	}

	public static java.util.List
		<com.liferay.osb.faro.contacts.model.ContactsCardTemplate>
			getContactsCardTemplates(String contactsCardTemplateIds) {

		return getService().getContactsCardTemplates(contactsCardTemplateIds);
	}

	/**
	 * Returns the number of contacts card templates.
	 *
	 * @return the number of contacts card templates
	 */
	public static int getContactsCardTemplatesCount() {
		return getService().getContactsCardTemplatesCount();
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
	 * Updates the contacts card template in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect ContactsCardTemplateLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param contactsCardTemplate the contacts card template
	 * @return the contacts card template that was updated
	 */
	public static com.liferay.osb.faro.contacts.model.ContactsCardTemplate
		updateContactsCardTemplate(
			com.liferay.osb.faro.contacts.model.ContactsCardTemplate
				contactsCardTemplate) {

		return getService().updateContactsCardTemplate(contactsCardTemplate);
	}

	public static com.liferay.osb.faro.contacts.model.ContactsCardTemplate
			updateContactsCardTemplate(
				long contactsCardTemplateId, String name, String settings,
				int type)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateContactsCardTemplate(
			contactsCardTemplateId, name, settings, type);
	}

	public static ContactsCardTemplateLocalService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker
		<ContactsCardTemplateLocalService, ContactsCardTemplateLocalService>
			_serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(
			ContactsCardTemplateLocalService.class);

		ServiceTracker
			<ContactsCardTemplateLocalService, ContactsCardTemplateLocalService>
				serviceTracker =
					new ServiceTracker
						<ContactsCardTemplateLocalService,
						 ContactsCardTemplateLocalService>(
							 bundle.getBundleContext(),
							 ContactsCardTemplateLocalService.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}