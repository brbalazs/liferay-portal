/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.commerce.pricing.service;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Provides the remote service utility for CommercePricingClassRel. This utility wraps
 * <code>com.liferay.commerce.pricing.service.impl.CommercePricingClassRelServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Riccardo Alberti
 * @see CommercePricingClassRelService
 * @generated
 */
public class CommercePricingClassRelServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.commerce.pricing.service.impl.CommercePricingClassRelServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static com.liferay.commerce.pricing.model.CommercePricingClassRel
			addCommerceDiscountRel(
				long commercePricingClassId, String className, long classPK,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addCommerceDiscountRel(
			commercePricingClassId, className, classPK, serviceContext);
	}

	public static void deleteCommerceDiscountRel(long commercePricingClassId)
		throws com.liferay.portal.kernel.exception.PortalException {

		getService().deleteCommerceDiscountRel(commercePricingClassId);
	}

	public static com.liferay.commerce.pricing.model.CommercePricingClassRel
			fetchCommerceDiscountRel(String className, long classPK)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().fetchCommerceDiscountRel(className, classPK);
	}

	public static long[] getClassPKs(
			long commercePricingClassId, String className)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getClassPKs(commercePricingClassId, className);
	}

	public static com.liferay.commerce.pricing.model.CommercePricingClassRel
			getCommerceDiscountRel(long commercePricingClassId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getCommerceDiscountRel(commercePricingClassId);
	}

	public static java.util.List
		<com.liferay.commerce.pricing.model.CommercePricingClassRel>
				getCommerceDiscountRels(
					long commercePricingClassId, String className)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getCommerceDiscountRels(
			commercePricingClassId, className);
	}

	public static java.util.List
		<com.liferay.commerce.pricing.model.CommercePricingClassRel>
				getCommerceDiscountRels(
					long commercePricingClassId, String className, int start,
					int end,
					com.liferay.portal.kernel.util.OrderByComparator
						<com.liferay.commerce.pricing.model.
							CommercePricingClassRel> orderByComparator)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getCommerceDiscountRels(
			commercePricingClassId, className, start, end, orderByComparator);
	}

	public static int getCommerceDiscountRelsCount(
			long commercePricingClassId, String className)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getCommerceDiscountRelsCount(
			commercePricingClassId, className);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static CommercePricingClassRelService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker
		<CommercePricingClassRelService, CommercePricingClassRelService>
			_serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(
			CommercePricingClassRelService.class);

		ServiceTracker
			<CommercePricingClassRelService, CommercePricingClassRelService>
				serviceTracker =
					new ServiceTracker
						<CommercePricingClassRelService,
						 CommercePricingClassRelService>(
							 bundle.getBundleContext(),
							 CommercePricingClassRelService.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}