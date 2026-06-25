/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.document.library.internal.security.permission.resource;

import com.liferay.document.library.constants.DLPortletKeys;
import com.liferay.exportimport.kernel.staging.permission.StagingPermission;
import com.liferay.osgi.service.tracker.collections.list.ServiceTrackerList;
import com.liferay.osgi.service.tracker.collections.list.ServiceTrackerListFactory;
import com.liferay.portal.kernel.security.permission.resource.BasePortletResourcePermissionWrapper;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermission;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermissionFactory;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermissionLogic;
import com.liferay.portal.kernel.security.permission.resource.StagedPortletPermissionLogic;
import com.liferay.portlet.documentlibrary.constants.DLConstants;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Preston Crary
 */
@Component(
	property = "resource.name=" + DLConstants.RESOURCE_NAME,
	service = PortletResourcePermission.class
)
public class DLPortletResourcePermissionWrapper
	extends BasePortletResourcePermissionWrapper {

	@Activate
	protected void activate(BundleContext bundleContext) {
		_serviceTrackerList = ServiceTrackerListFactory.open(
			bundleContext, PortletResourcePermissionLogic.class,
			"(resource.name=" + DLConstants.RESOURCE_NAME + ")");
	}

	@Deactivate
	protected void deactivate() {
		_serviceTrackerList.close();
	}

	@Override
	protected PortletResourcePermission doGetPortletResourcePermission() {
		List<PortletResourcePermissionLogic> portletResourcePermissionLogics =
			new ArrayList<>();

		_serviceTrackerList.forEach(portletResourcePermissionLogics::add);

		portletResourcePermissionLogics.add(
			new StagedPortletPermissionLogic(
				_stagingPermission, DLPortletKeys.DOCUMENT_LIBRARY_ADMIN));

		return PortletResourcePermissionFactory.create(
			DLConstants.RESOURCE_NAME,
			portletResourcePermissionLogics.toArray(
				new PortletResourcePermissionLogic[0]));
	}

	private ServiceTrackerList<PortletResourcePermissionLogic>
		_serviceTrackerList;

	@Reference
	private StagingPermission _stagingPermission;

}