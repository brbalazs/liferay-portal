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

package com.liferay.osb.faro.web.internal.controller.api;

import com.liferay.osb.faro.model.FaroProject;
import com.liferay.osb.faro.util.FaroThreadLocal;
import com.liferay.osb.faro.web.internal.context.GroupInfo;
import com.liferay.osb.faro.web.internal.controller.BaseFaroController;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.MapUtil;

import java.net.URI;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;

import org.osgi.service.component.annotations.Component;

/**
 * @author Marcellus Tavares
 */
@Component(immediate = true, service = ReportController.class)
@Path("/reports")
@Produces(MediaType.APPLICATION_JSON)
public class ReportController extends BaseFaroController {

	@GET
	@Path("/export/{type}")
	public Object get(
			@Context GroupInfo groupInfo, @PathParam("type") String type)
		throws Exception {

		FaroProject faroProject =
			faroProjectLocalService.getFaroProjectByGroupId(
				groupInfo.getGroupId());

		Map<String, Object> responseMap = contactsEngineClient.get(
			faroProject, Collections.emptyMap(), "/api/reports/export/" + type,
			Collections.emptyMap(), Map.class);

		String status = MapUtil.getString(responseMap, "status");

		if (!Objects.equals(status, "COMPLETED")) {
			return MapUtil.fromArray(
				"message",
				"The data export file is being created. Please come back " +
					"later.");
		}

		StreamingOutput streamingOutput = outputStream -> {
			try {
				FaroThreadLocal.setCacheEnabled(false);

				contactsEngineClient.getToOutputStream(
					faroProject,
					new HashMap<String, String>() {
						{
							put("Accept", "application/octet-stream, */*");
						}
					},
					String.format("/api/reports/export/%s/file", type),
					Collections.emptyMap(), outputStream);
			}
			catch (Exception exception) {
				_log.error(exception, exception);
			}

			outputStream.flush();
		};

		return Response.ok(
			streamingOutput
		).build();
	}

	@GET
	@Path("{any:(?!/export.*).*}")
	public Map<Object, Object> get(
			@Context GroupInfo groupInfo, @Context UriInfo uriInfo)
		throws Exception {

		FaroProject faroProject =
			faroProjectLocalService.getFaroProjectByGroupId(
				groupInfo.getGroupId());

		return contactsEngineClient.get(
			faroProject, _createHeaders(uriInfo.getBaseUri()),
			"/api/" + uriInfo.getPath(), uriInfo.getQueryParameters(),
			Map.class);
	}

	private Map<String, String> _createHeaders(URI baseURI) {
		return new HashMap<String, String>() {
			{
				put("X-Forwarded-Host", baseURI.getHost());
				put("X-Forwarded-Port", String.valueOf(baseURI.getPort()));
				put("X-Forwarded-Proto", baseURI.getScheme());
			}
		};
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ReportController.class);

}