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
import com.liferay.portal.kernel.util.Validator;

import java.net.URI;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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

	@GET
	@Path("/export/{type}")
	public Object get(
			@QueryParam("fromDate") String fromDateString,
			@Context GroupInfo groupInfo,
			@QueryParam("toDate") String toDateString,
			@PathParam("type") String type)
		throws Exception {

		if (Validator.isBlank(fromDateString) ||
			Validator.isBlank(toDateString)) {

			return _createResponse(
				String.format(
					"\"fromDate\" and \"toDate\" query parameters are " +
						"mandatory and must be ISO 8601 compliant (%s)",
					_ISO_8601_FORMAT),
				Response.Status.BAD_REQUEST);
		}

		Date fromDate;
		Date toDate;

		try {
			fromDate = _toUTCDate(fromDateString);
			toDate = _toUTCDate(toDateString);
		}
		catch (Exception exception) {
			return _createResponse(
				String.format(
					"Both dates in range must be ISO 8601 compliant (%s)",
					_ISO_8601_FORMAT),
				Response.Status.BAD_REQUEST);
		}

		if (fromDate.after(toDate)) {
			return _createResponse(
				"Wrong range date. \"fromDate\" cannot be after \"toDate\"",
				Response.Status.BAD_REQUEST);
		}

		FaroProject faroProject =
			faroProjectLocalService.getFaroProjectByGroupId(
				groupInfo.getGroupId());

		String path = "/api/reports/export/" + type;

		Map<String, Object> responseMap;

		Map<String, List<String>> queryParameters =
			new HashMap<String, List<String>>() {
				{
					put("fromDate", Collections.singletonList(fromDateString));
					put("toDate", Collections.singletonList(toDateString));
				}
			};

		try {
			responseMap = contactsEngineClient.get(
				faroProject, Collections.emptyMap(), path, queryParameters,
				Map.class);
		}
		catch (Exception exception) {
			return _createResponse(
				"An internal problem happened when trying to reach our" +
					" services",
				Response.Status.INTERNAL_SERVER_ERROR);
		}

		String status = MapUtil.getString(responseMap, "status");

		if (!Objects.equals(status, "COMPLETED")) {
			return _createResponse(responseMap, Response.Status.OK);
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
					String.format("%s/file", path), queryParameters,
					outputStream);
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

	private Map<String, String> _createHeaders(URI baseURI) {
		return new HashMap<String, String>() {
			{
				put("X-Forwarded-Host", baseURI.getHost());
				put("X-Forwarded-Port", String.valueOf(baseURI.getPort()));
				put("X-Forwarded-Proto", baseURI.getScheme());
			}
		};
	}

	private Response _createResponse(
		Map<String, Object> responseMap, Response.Status responseStatus) {

		Map<String, String> stringMap = new HashMap<>();

		String createdDateString = MapUtil.getString(
			responseMap, "createdDate");

		if (!Validator.isBlank(createdDateString)) {
			stringMap.put("createdDate", createdDateString);
		}

		String fromDateString = MapUtil.getString(responseMap, "fromDate");

		if (!Validator.isBlank(fromDateString)) {
			stringMap.put("fromDate", fromDateString);
		}

		String startedDateString = MapUtil.getString(
			responseMap, "startedDate");

		if (!Validator.isBlank(startedDateString)) {
			stringMap.put("startedDate", startedDateString);
		}

		String status = MapUtil.getString(responseMap, "status");

		if (!Validator.isBlank(status)) {
			stringMap.put("status", status);
		}
		else if (responseStatus != Response.Status.OK) {
			stringMap.put("status", "ERROR");
		}

		String message = MapUtil.getString(responseMap, "message");

		if (!Validator.isBlank(message)) {
			stringMap.put("message", message);
		}
		else if (status.equals("PENDING")) {
			String previousStatus = MapUtil.getString(
				responseMap, "previousStatus");

			if (!Validator.isBlank(previousStatus) &&
				previousStatus.equals("ERROR")) {

				stringMap.put(
					"message",
					"The last data export for this date range and type " +
						"failed. A new data export file will be created. " +
							"Please come back later.");
			}
			else {
				stringMap.put(
					"message",
					"A new data export file for this date range and type " +
						"will be created. Please come back later.");
			}
		}
		else if (status.equals("RUNNING")) {
			stringMap.put(
				"message",
				"The data export file for this date range and type is being" +
					" created. Please come back later.");
		}
		else if (status.equals("ERROR")) {
			stringMap.put(
				"message",
				"The last data export for this date range and type failed. A " +
					"new data export file will be created. Please come back " +
						"later.");
		}

		String toDateString = MapUtil.getString(responseMap, "toDate");

		if (!Validator.isBlank(toDateString)) {
			stringMap.put("toDate", toDateString);
		}

		String type = MapUtil.getString(responseMap, "type");

		if (!Validator.isBlank(type)) {
			stringMap.put("type", type);
		}

		Response.ResponseBuilder responseBuilder = Response.status(
			responseStatus);

		responseBuilder.entity(stringMap);

		return responseBuilder.build();
	}

	private Response _createResponse(
		String message, Response.Status responseStatus) {

		Map<String, Object> responseMap = new HashMap<String, Object>() {
			{
				put("message", message);
			}
		};

		return _createResponse(responseMap, responseStatus);
	}

	private Date _toUTCDate(String dateString) {
		LocalDateTime localDateTime = LocalDateTime.parse(
			dateString, _dateTimeFormatter);

		ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneOffset.UTC);

		return Date.from(zonedDateTime.toInstant());
	}

	private static final String _ISO_8601_FORMAT =
		"yyyy-MM-dd'T'HH:mm[:ss.SSS'Z']";

	private static final Log _log = LogFactoryUtil.getLog(
		ReportController.class);

	private static final DateTimeFormatter _dateTimeFormatter =
		DateTimeFormatter.ofPattern(_ISO_8601_FORMAT);

}