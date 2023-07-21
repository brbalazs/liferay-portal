/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.contacts.demo.internal.data.creator;

import com.liferay.osb.faro.engine.client.ContactsEngineClient;
import com.liferay.osb.faro.engine.client.model.IndividualSegmentMembershipChange;
import com.liferay.osb.faro.model.FaroProject;
import com.liferay.portal.kernel.util.Time;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Matthew Kong
 */
public class MembershipChangesDataCreator extends DataCreator {

	public MembershipChangesDataCreator(
		ContactsEngineClient contactsEngineClient, FaroProject faroProject) {

		super(
			contactsEngineClient, faroProject, "osbasahfaroinfo",
			"membership-changes");
	}

	@Override
	protected Map<String, Object> doCreate(Object[] params) {
		Map<String, Object> membershipChange = new HashMap<>();

		IndividualSegmentMembershipChange individualSegmentMembershipChange =
			(IndividualSegmentMembershipChange)params[0];

		Date dateChanged = individualSegmentMembershipChange.getDateChanged();

		membershipChange.put(
			"dateChanged",
			formatDate(new Date(dateChanged.getTime() - Time.MONTH)));

		Date dateFirst = individualSegmentMembershipChange.getDateFirst();

		membershipChange.put(
			"dateFirst",
			formatDate(new Date(dateFirst.getTime() - Time.MONTH)));

		membershipChange.put("id", individualSegmentMembershipChange.getId());
		membershipChange.put(
			"individualDeleted",
			individualSegmentMembershipChange.isIndividualDeleted());
		membershipChange.put(
			"individualEmail",
			individualSegmentMembershipChange.getIndividualEmail());
		membershipChange.put(
			"individualId",
			individualSegmentMembershipChange.getIndividualId());
		membershipChange.put(
			"individualName",
			individualSegmentMembershipChange.getIndividualName());
		membershipChange.put(
			"individualsCount",
			individualSegmentMembershipChange.getIndividualsCount());
		membershipChange.put(
			"individualSegmentId",
			individualSegmentMembershipChange.getIndividualSegmentId());
		membershipChange.put(
			"operation", individualSegmentMembershipChange.getOperation());

		return membershipChange;
	}

}