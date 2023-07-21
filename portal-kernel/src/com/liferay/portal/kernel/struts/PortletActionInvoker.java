/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.struts;

import com.liferay.petra.lang.ClassResolverUtil;
import com.liferay.portal.kernel.util.MethodKey;
import com.liferay.portal.kernel.util.PortalClassInvoker;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;

/**
 * @author Brian Wing Shun Chan
 * @deprecated As of Judson (7.1.x), with no direct replacement
 */
@Deprecated
public class PortletActionInvoker {

	public static void processAction(
			String className, PortletConfig portletConfig,
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		ClassLoader portalClassLoader = PortalClassLoaderUtil.getClassLoader();

		MethodKey methodKey = new MethodKey(
			ClassResolverUtil.resolve(className, portalClassLoader),
			"processAction",
			new Class<?>[] {
				ClassResolverUtil.resolve(
					"org.apache.struts.action.ActionMapping",
					portalClassLoader),
				ClassResolverUtil.resolve(
					"org.apache.struts.action.ActionForm", portalClassLoader),
				PortletConfig.class, ActionRequest.class, ActionResponse.class
			});

		PortalClassInvoker.invoke(
			methodKey, null, null, portletConfig, actionRequest,
			actionResponse);
	}

}