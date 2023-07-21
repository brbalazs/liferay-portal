/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.fragment.demo.internal;

import com.liferay.fragment.demo.data.creator.FragmentCollectionDemoDataCreator;
import com.liferay.fragment.demo.data.creator.FragmentEntryDemoDataCreator;
import com.liferay.fragment.model.FragmentCollection;
import com.liferay.portal.instance.lifecycle.BasePortalInstanceLifecycleListener;
import com.liferay.portal.instance.lifecycle.PortalInstanceLifecycleListener;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.module.framework.ModuleServiceLifecycle;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.users.admin.demo.data.creator.SiteAdminUserDemoDataCreator;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Jürgen Kappler
 */
@Component(immediate = true, service = PortalInstanceLifecycleListener.class)
public class FragmentDemo extends BasePortalInstanceLifecycleListener {

	@Override
	public void portalInstanceRegistered(Company company) throws Exception {
		Group group = _groupLocalService.getGroup(
			company.getCompanyId(), "Guest");

		User user = _siteAdminUserDemoDataCreator.create(
			group.getGroupId(), "sharon.choi@liferay.com");

		FragmentCollection fragmentCollection =
			_fragmentCollectionDemoDataCreator.create(
				user.getUserId(), group.getGroupId(), "Demo Collection");

		for (int i = 0; i < 5; i++) {
			_fragmentEntryDemoDataCreator.create(
				user.getUserId(), group.getGroupId(),
				fragmentCollection.getFragmentCollectionId());
		}
	}

	@Reference(target = ModuleServiceLifecycle.PORTAL_INITIALIZED, unbind = "-")
	protected void setModuleServiceLifecycle(
		ModuleServiceLifecycle moduleServiceLifecycle) {
	}

	@Reference
	private FragmentCollectionDemoDataCreator
		_fragmentCollectionDemoDataCreator;

	@Reference
	private FragmentEntryDemoDataCreator _fragmentEntryDemoDataCreator;

	@Reference
	private GroupLocalService _groupLocalService;

	@Reference
	private SiteAdminUserDemoDataCreator _siteAdminUserDemoDataCreator;

}