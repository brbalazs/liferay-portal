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

package com.liferay.commerce.organization.web.internal.organization.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import com.liferay.commerce.frontend.Pagination;
import com.liferay.commerce.organization.web.internal.organization.model.OrganizationList;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.OrganizationConstants;
import com.liferay.portal.kernel.service.OrganizationService;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.WebKeys;

import javax.servlet.http.HttpServletRequest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alessio Antonio Rendina
 */
@Component(service = CommerceOrganizationResource.class)
public class CommerceOrganizationResource {

	@Consumes(MediaType.APPLICATION_JSON)
	@DELETE
	@Path("/organization/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteOrganization(
		@PathParam("id") long organizationId,
		@Context HttpServletRequest httpServletRequest,
		@Context Pagination pagination) {

		OrganizationList organizationList = null;

		try {
			long parentOrganizationId =
				OrganizationConstants.DEFAULT_PARENT_ORGANIZATION_ID;

			Organization curOrganization = _organizationService.getOrganization(
				organizationId);

			Organization parentOrganization =
				curOrganization.getParentOrganization();

			if (parentOrganization != null) {
				parentOrganizationId = parentOrganization.getOrganizationId();
			}

			_organizationService.deleteOrganization(organizationId);

			organizationList =
				_commerceOrganizationResourceUtil.getOrganizationList(
					curOrganization.getCompanyId(), parentOrganizationId,
					pagination);
		}
		catch (Exception e) {
			organizationList = new OrganizationList(
				StringUtil.split(e.getLocalizedMessage()));
		}

		return getResponse(organizationList);
	}

	@GET
	@Path("/organizations/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOrganizations(
		@PathParam("id") long parentOrganizationId,
		@Context HttpServletRequest httpServletRequest,
		@Context Pagination pagination) {

		OrganizationList organizationList = null;

		try {
			ThemeDisplay themeDisplay =
				(ThemeDisplay)httpServletRequest.getAttribute(
					WebKeys.THEME_DISPLAY);

			long companyId = themeDisplay.getCompanyId();

			if (parentOrganizationId >
					OrganizationConstants.DEFAULT_PARENT_ORGANIZATION_ID) {

				Organization parentOrganization =
					_organizationService.getOrganization(parentOrganizationId);

				companyId = parentOrganization.getCompanyId();
			}

			organizationList =
				_commerceOrganizationResourceUtil.getOrganizationList(
					companyId, parentOrganizationId, pagination);
		}
		catch (Exception e) {
			_log.error(e, e);

			organizationList = new OrganizationList(
				StringUtil.split(e.getLocalizedMessage()));
		}

		return getResponse(organizationList);
	}

	protected Response getResponse(Object object) {
		if (object == null) {
			return Response.status(
				Response.Status.NOT_FOUND
			).build();
		}

		try {
			String json = _OBJECT_MAPPER.writeValueAsString(object);

			return Response.ok(
				json, MediaType.APPLICATION_JSON
			).build();
		}
		catch (JsonProcessingException jpe) {
			_log.error(jpe, jpe);
		}

		return Response.status(
			Response.Status.NOT_FOUND
		).build();
	}

	private static final ObjectMapper _OBJECT_MAPPER = new ObjectMapper() {
		{
			configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
			disable(SerializationFeature.INDENT_OUTPUT);
		}
	};

	private static final Log _log = LogFactoryUtil.getLog(
		CommerceOrganizationResource.class);

	@Reference
	private CommerceOrganizationResourceUtil _commerceOrganizationResourceUtil;

	@Reference
	private OrganizationService _organizationService;

}