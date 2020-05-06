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

package com.liferay.osb.faro.web.internal.controller.contacts;

import com.liferay.osb.faro.engine.client.constants.FieldMappingConstants;
import com.liferay.osb.faro.engine.client.model.EngagementAggregation;
import com.liferay.osb.faro.engine.client.model.Results;
import com.liferay.osb.faro.engine.client.util.OrderByField;
import com.liferay.osb.faro.web.internal.constants.FaroConstants;
import com.liferay.osb.faro.web.internal.controller.BaseFaroController;
import com.liferay.osb.faro.web.internal.controller.FaroController;
import com.liferay.osb.faro.web.internal.model.display.FaroResultsDisplay;
import com.liferay.osb.faro.web.internal.model.display.contacts.EngagementHistoryDisplay;
import com.liferay.osb.faro.web.internal.param.FaroParam;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.RoleConstants;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.osgi.service.component.annotations.Component;

/**
 * @author Shinn Lok
 */
@Component(
	immediate = true,
	service = {EngagementController.class, FaroController.class}
)
@Path("/{groupId}/engagement")
@Produces(MediaType.APPLICATION_JSON)
public class EngagementController extends BaseFaroController {

	@GET
	@Path("/histories")
	@RolesAllowed(RoleConstants.SITE_MEMBER)
	public Map<String, EngagementHistoryDisplay> getHistories(
			@PathParam("groupId") long groupId,
			@QueryParam("individualIds")
				FaroParam<List<String>> individualIdsFaroParam,
			@QueryParam("interval") String interval, @QueryParam("max") int max)
		throws Exception {

		Map<String, EngagementHistoryDisplay> engagementHistoryDisplays =
			new HashMap<>();

		List<String> individualIds = individualIdsFaroParam.getValue();

		List<List<EngagementAggregation>> engagementAggregationsList =
			contactsEngineClient.getEngagementAggregationsList(
				faroProjectLocalService.getFaroProjectByGroupId(groupId),
				individualIds, FieldMappingConstants.OWNER_TYPE_INDIVIDUAL,
				interval, max + 1);

		for (int i = 0; i < individualIds.size(); i++) {
			List<EngagementAggregation> engagementAggregations =
				engagementAggregationsList.get(i);

			EngagementHistoryDisplay engagementHistoryDisplay = null;

			if (engagementAggregations.isEmpty()) {
				engagementHistoryDisplay = new EngagementHistoryDisplay();
			}
			else {
				engagementHistoryDisplay = new EngagementHistoryDisplay(
					engagementAggregations.subList(
						1, engagementAggregations.size()),
					engagementAggregations.get(0));
			}

			engagementHistoryDisplays.put(
				individualIds.get(i), engagementHistoryDisplay);
		}

		return engagementHistoryDisplays;
	}

	@GET
	@Path("/history")
	@RolesAllowed(RoleConstants.SITE_MEMBER)
	public EngagementHistoryDisplay getHistory(
			@PathParam("groupId") long groupId,
			@QueryParam("contactsEntityId") String contactsEntityId,
			@QueryParam("contactsEntityType") int contactsEntityType,
			@QueryParam("interval") String interval, @QueryParam("max") int max)
		throws Exception {

		Results<EngagementAggregation> results =
			contactsEngineClient.getEngagementAggregations(
				faroProjectLocalService.getFaroProjectByGroupId(groupId),
				contactsEntityId, getOwnerType(contactsEntityType), interval,
				max + 1);

		List<EngagementAggregation> engagementAggregations = results.getItems();

		if (engagementAggregations.isEmpty()) {
			return new EngagementHistoryDisplay();
		}

		return new EngagementHistoryDisplay(
			engagementAggregations.subList(1, engagementAggregations.size()),
			engagementAggregations.get(0));
	}

	@GET
	@RolesAllowed(RoleConstants.SITE_MEMBER)
	public FaroResultsDisplay search(
			@PathParam("groupId") long groupId,
			@QueryParam("contactsEntityId") String contactsEntityId,
			@QueryParam("contactsEntityType") int contactsEntityType,
			@QueryParam("query") String query,
			@DefaultValue(StringPool.BLANK) @QueryParam("startDate")
				FaroParam<Date> startDateFaroParam,
			@DefaultValue(StringPool.BLANK) @QueryParam("endDate")
				FaroParam<Date> endDateFaroParam,
			@QueryParam("cur") int cur, @QueryParam("delta") int delta,
			@DefaultValue(StringPool.BLANK) @QueryParam("orderByFields")
				FaroParam<List<OrderByField>> orderByFieldsFaroParam)
		throws Exception {

		return new FaroResultsDisplay(
			contactsEngineClient.getEngagements(
				faroProjectLocalService.getFaroProjectByGroupId(groupId),
				contactsEntityId, getOwnerType(contactsEntityType), query,
				startDateFaroParam.getValue(), endDateFaroParam.getValue(), cur,
				delta, orderByFieldsFaroParam.getValue()));
	}

	@Path("/search")
	@POST
	@RolesAllowed(RoleConstants.SITE_MEMBER)
	public FaroResultsDisplay searchByForm(
			@PathParam("groupId") long groupId,
			@FormParam("contactsEntityId") String contactsEntityId,
			@FormParam("contactsEntityType") int contactsEntityType,
			@FormParam("query") String query,
			@DefaultValue(StringPool.BLANK) @FormParam("startDate")
				FaroParam<Date> startDateFaroParam,
			@DefaultValue(StringPool.BLANK) @FormParam("endDate")
				FaroParam<Date> endDateFaroParam,
			@FormParam("cur") int cur, @FormParam("delta") int delta,
			@DefaultValue(StringPool.BLANK) @FormParam("orderByFields")
				FaroParam<List<OrderByField>> orderByFieldsFaroParam)
		throws Exception {

		return search(
			groupId, contactsEntityId, contactsEntityType, query,
			startDateFaroParam, endDateFaroParam, cur, delta,
			orderByFieldsFaroParam);
	}

	protected String getOwnerType(int type) {
		if (type == FaroConstants.TYPE_ACCOUNT) {
			return FieldMappingConstants.OWNER_TYPE_ACCOUNT;
		}
		else if (type == FaroConstants.TYPE_INDIVIDUAL) {
			return FieldMappingConstants.OWNER_TYPE_INDIVIDUAL;
		}

		return FieldMappingConstants.OWNER_TYPE_INDIVIDUAL_SEGMENT;
	}

}