/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.blogs.rest.internal.application;

import com.liferay.blogs.rest.internal.resource.BlogsRootResource;
import com.liferay.portal.kernel.util.SetUtil;

import java.util.Collections;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alejandro Hernández
 */
@ApplicationPath("/")
@Component(immediate = true, service = Application.class)
public class BlogsJaxRsApplication extends Application {

	@Override
	public Set<Object> getSingletons() {
		return SetUtil.fromCollection(
			Collections.singletonList(_blogsRootResource));
	}

	@Reference
	private BlogsRootResource _blogsRootResource;

}