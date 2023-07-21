/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portlet.social.service.impl.bundle.socialactivityinterpreterlocalserviceimpl;

import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.social.kernel.model.SocialActivity;
import com.liferay.social.kernel.model.SocialActivityFeedEntry;
import com.liferay.social.kernel.model.SocialActivityInterpreter;
import com.liferay.social.kernel.model.SocialActivitySet;

import org.osgi.service.component.annotations.Component;

/**
 * @author Philip Jones
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=SocialActivityInterpreterLocalServiceImplTest",
		"service.ranking:Integer=" + Integer.MAX_VALUE
	},
	service = SocialActivityInterpreter.class
)
public class TestSocialActivityInterpreter
	implements SocialActivityInterpreter {

	public static final String SELECTOR = "SELECTOR";

	@Override
	public String[] getClassNames() {
		return new String[] {TestSocialActivityInterpreter.class.getName()};
	}

	@Override
	public String getSelector() {
		return SELECTOR;
	}

	@Override
	public boolean hasPermission(
		PermissionChecker permissionChecker, SocialActivity activity,
		String actionId, ServiceContext serviceContext) {

		return false;
	}

	@Override
	public SocialActivityFeedEntry interpret(
		SocialActivity activity, ServiceContext serviceContext) {

		return null;
	}

	@Override
	public SocialActivityFeedEntry interpret(
		SocialActivitySet activitySet, ServiceContext serviceContext) {

		return null;
	}

	@Override
	public void updateActivitySet(long activityId) {
	}

}