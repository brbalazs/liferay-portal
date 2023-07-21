/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.blogs.rest.internal.resource;

import com.liferay.blogs.model.BlogsEntry;
import com.liferay.blogs.rest.internal.model.BlogsEntryModel;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * @author Alejandro Hernández
 */
public class BlogsEntryResource {

	public BlogsEntryResource(BlogsEntry blogsEntry) {
		_blogsEntry = blogsEntry;
	}

	@GET
	@Path("/")
	@Produces("application/json")
	public BlogsEntryModel getBlogsEntryModel() {
		return new BlogsEntryModel(_blogsEntry);
	}

	private final BlogsEntry _blogsEntry;

}