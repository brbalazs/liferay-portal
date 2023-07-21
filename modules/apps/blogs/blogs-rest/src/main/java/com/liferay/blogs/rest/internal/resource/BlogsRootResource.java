/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.blogs.rest.internal.resource;

import com.liferay.blogs.exception.NoSuchEntryException;
import com.liferay.blogs.model.BlogsEntry;
import com.liferay.blogs.service.BlogsEntryService;
import com.liferay.portal.kernel.exception.PortalException;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alejandro Hernández
 */
@Component(immediate = true, service = BlogsRootResource.class)
@Path("/")
public class BlogsRootResource {

	@Path("/{entryId}")
	public BlogsEntryResource getBlogsEntryResource(
			@PathParam("entryId") long entryId)
		throws PortalException {

		BlogsEntry blogsEntry = null;

		try {
			blogsEntry = _blogsEntryService.getEntry(entryId);
		}
		catch (NoSuchEntryException nsee) {
			throw new NotFoundException(nsee);
		}

		return new BlogsEntryResource(blogsEntry);
	}

	@Reference
	private BlogsEntryService _blogsEntryService;

}