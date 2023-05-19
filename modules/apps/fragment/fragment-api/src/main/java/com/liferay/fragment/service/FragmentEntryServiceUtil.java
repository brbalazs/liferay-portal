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

package com.liferay.fragment.service;

import com.liferay.fragment.model.FragmentEntry;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.util.List;

/**
 * Provides the remote service utility for FragmentEntry. This utility wraps
 * <code>com.liferay.fragment.service.impl.FragmentEntryServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Brian Wing Shun Chan
 * @see FragmentEntryService
 * @generated
 */
public class FragmentEntryServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.fragment.service.impl.FragmentEntryServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static FragmentEntry addFragmentEntry(
			long groupId, long fragmentCollectionId, String name, int status,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws PortalException {

		return getService().addFragmentEntry(
			groupId, fragmentCollectionId, name, status, serviceContext);
	}

	public static FragmentEntry addFragmentEntry(
			long groupId, long fragmentCollectionId, String fragmentEntryKey,
			String name, int status,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws PortalException {

		return getService().addFragmentEntry(
			groupId, fragmentCollectionId, fragmentEntryKey, name, status,
			serviceContext);
	}

	public static FragmentEntry addFragmentEntry(
			long groupId, long fragmentCollectionId, String name, String css,
			String html, String js, int status,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws PortalException {

		return getService().addFragmentEntry(
			groupId, fragmentCollectionId, name, css, html, js, status,
			serviceContext);
	}

	public static FragmentEntry addFragmentEntry(
			long groupId, long fragmentCollectionId, String fragmentEntryKey,
			String name, String css, String html, String js, int status,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws PortalException {

		return getService().addFragmentEntry(
			groupId, fragmentCollectionId, fragmentEntryKey, name, css, html,
			js, status, serviceContext);
	}

	public static void deleteFragmentEntries(long[] fragmentEntriesIds)
		throws PortalException {

		getService().deleteFragmentEntries(fragmentEntriesIds);
	}

	public static FragmentEntry deleteFragmentEntry(long fragmentEntryId)
		throws PortalException {

		return getService().deleteFragmentEntry(fragmentEntryId);
	}

	public static FragmentEntry fetchFragmentEntry(long fragmentEntryId)
		throws PortalException {

		return getService().fetchFragmentEntry(fragmentEntryId);
	}

	public static int getFragmentCollectionsCount(
		long groupId, long fragmentCollectionId) {

		return getService().getFragmentCollectionsCount(
			groupId, fragmentCollectionId);
	}

	public static int getFragmentCollectionsCount(
		long groupId, long fragmentCollectionId, int status) {

		return getService().getFragmentCollectionsCount(
			groupId, fragmentCollectionId, status);
	}

	public static int getFragmentCollectionsCount(
		long groupId, long fragmentCollectionId, String name) {

		return getService().getFragmentCollectionsCount(
			groupId, fragmentCollectionId, name);
	}

	public static int getFragmentCollectionsCount(
		long groupId, long fragmentCollectionId, String name, int status) {

		return getService().getFragmentCollectionsCount(
			groupId, fragmentCollectionId, name, status);
	}

	public static List<FragmentEntry> getFragmentEntries(
		long fragmentCollectionId) {

		return getService().getFragmentEntries(fragmentCollectionId);
	}

	public static List<FragmentEntry> getFragmentEntries(
		long groupId, long fragmentCollectionId, int status) {

		return getService().getFragmentEntries(
			groupId, fragmentCollectionId, status);
	}

	public static List<FragmentEntry> getFragmentEntries(
		long groupId, long fragmentCollectionId, int start, int end) {

		return getService().getFragmentEntries(
			groupId, fragmentCollectionId, start, end);
	}

	public static List<FragmentEntry> getFragmentEntries(
		long groupId, long fragmentCollectionId, int status, int start, int end,
		OrderByComparator<FragmentEntry> orderByComparator) {

		return getService().getFragmentEntries(
			groupId, fragmentCollectionId, status, start, end,
			orderByComparator);
	}

	public static List<FragmentEntry> getFragmentEntries(
		long groupId, long fragmentCollectionId, int start, int end,
		OrderByComparator<FragmentEntry> orderByComparator) {

		return getService().getFragmentEntries(
			groupId, fragmentCollectionId, start, end, orderByComparator);
	}

	public static List<FragmentEntry> getFragmentEntries(
		long groupId, long fragmentCollectionId, String name, int status,
		int start, int end,
		OrderByComparator<FragmentEntry> orderByComparator) {

		return getService().getFragmentEntries(
			groupId, fragmentCollectionId, name, status, start, end,
			orderByComparator);
	}

	public static List<FragmentEntry> getFragmentEntries(
		long groupId, long fragmentCollectionId, String name, int start,
		int end, OrderByComparator<FragmentEntry> orderByComparator) {

		return getService().getFragmentEntries(
			groupId, fragmentCollectionId, name, start, end, orderByComparator);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static String[] getTempFileNames(long groupId, String folderName)
		throws PortalException {

		return getService().getTempFileNames(groupId, folderName);
	}

	public static FragmentEntry updateFragmentEntry(
			long fragmentEntryId, long previewFileEntryId)
		throws PortalException {

		return getService().updateFragmentEntry(
			fragmentEntryId, previewFileEntryId);
	}

	public static FragmentEntry updateFragmentEntry(
			long fragmentEntryId, String name)
		throws PortalException {

		return getService().updateFragmentEntry(fragmentEntryId, name);
	}

	public static FragmentEntry updateFragmentEntry(
			long fragmentEntryId, String name, String css, String html,
			String js, int status)
		throws PortalException {

		return getService().updateFragmentEntry(
			fragmentEntryId, name, css, html, js, status);
	}

	public static FragmentEntry updateFragmentEntry(
			long fragmentEntryId, String name, String css, String html,
			String js, long previewFileEntryId, int status)
		throws PortalException {

		return getService().updateFragmentEntry(
			fragmentEntryId, name, css, html, js, previewFileEntryId, status);
	}

	public static FragmentEntryService getService() {
		return _service;
	}

	public static void setService(FragmentEntryService service) {
		_service = service;
	}

	private static volatile FragmentEntryService _service;

}