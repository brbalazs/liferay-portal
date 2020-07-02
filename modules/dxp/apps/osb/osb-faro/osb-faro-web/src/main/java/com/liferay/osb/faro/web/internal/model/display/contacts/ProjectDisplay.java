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

package com.liferay.osb.faro.web.internal.model.display.contacts;

import com.liferay.osb.faro.engine.client.CerebroEngineClient;
import com.liferay.osb.faro.engine.client.ContactsEngineClient;
import com.liferay.osb.faro.model.FaroProject;
import com.liferay.osb.faro.provisioning.client.ProvisioningClient;
import com.liferay.osb.faro.provisioning.client.model.OSBAccountEntry;
import com.liferay.osb.faro.service.FaroProjectLocalServiceUtil;
import com.liferay.osb.faro.web.internal.constants.ProjectConstants;
import com.liferay.osb.faro.web.internal.model.display.main.FaroSubscriptionDisplay;
import com.liferay.osb.faro.web.internal.util.JSONUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.kernel.util.Validator;

import java.util.Date;

/**
 * @author Matthew Kong
 */
@SuppressWarnings({"FieldCanBeLocal", "UnusedDeclaration"})
public class ProjectDisplay {

	public ProjectDisplay() {
	}

	public ProjectDisplay(FaroProject faroProject) {
		this(faroProject, null);
	}

	public ProjectDisplay(
		FaroProject faroProject, CerebroEngineClient cerebroEngineClient,
		ContactsEngineClient contactsEngineClient,
		ProvisioningClient provisioningClient) {

		this(faroProject);

		_faroSubscriptionDisplay = getFaroSubscriptionDisplay(
			faroProject, cerebroEngineClient, contactsEngineClient,
			provisioningClient);
	}

	public ProjectDisplay(FaroProject faroProject, String friendlyURL) {
		_accountKey = faroProject.getAccountKey();
		_accountName = faroProject.getAccountName();
		_corpProjectName = faroProject.getCorpProjectName();
		_corpProjectUuid = faroProject.getCorpProjectUuid();

		try {
			_faroSubscriptionDisplay = JSONUtil.readValue(
				faroProject.getSubscription(), FaroSubscriptionDisplay.class);
		}
		catch (Exception e) {
			_log.error(e, e);
		}

		_friendlyURL = friendlyURL;
		_groupId = faroProject.getGroupId();
		_name = faroProject.getName();
		_recommendationsEnabled = faroProject.isRecommendationsEnabled();
		_serverLocation = faroProject.getServerLocation();
		_state = faroProject.getState();
		_userId = faroProject.getUserId();
	}

	public ProjectDisplay(OSBAccountEntry osbAccountEntry) {
		_accountKey = osbAccountEntry.getDossieraAccountKey();
		_accountName = osbAccountEntry.getCorpEntryName();
		_corpProjectName = osbAccountEntry.getName();
		_corpProjectUuid = osbAccountEntry.getCorpProjectUuid();
		_faroSubscriptionDisplay = new FaroSubscriptionDisplay(osbAccountEntry);
	}

	public String getCorpProjectUuid() {
		return _corpProjectUuid;
	}

	public FaroSubscriptionDisplay getFaroSubscriptionDisplay() {
		return _faroSubscriptionDisplay;
	}

	public String getState() {
		return _state;
	}

	public void setFriendlyURL(String friendlyURL) {
		_friendlyURL = friendlyURL;
	}

	public void setState(String state) {
		_state = state;
	}

	public void setStateEndDate(Date stateEndDate) {
		_stateEndDate = stateEndDate;
	}

	public void setStateStartDate(Date stateStartDate) {
		_stateStartDate = stateStartDate;
	}

	protected FaroSubscriptionDisplay getFaroSubscriptionDisplay(
		FaroProject faroProject, CerebroEngineClient cerebroEngineClient,
		ContactsEngineClient contactsEngineClient,
		ProvisioningClient provisioningClient) {

		FaroSubscriptionDisplay faroSubscriptionDisplay = null;

		if (Validator.isNotNull(faroProject.getCorpProjectUuid()) &&
			(Validator.isNull(faroProject.getSubscription()) ||
			 ((System.currentTimeMillis() - faroProject.getModifiedTime()) >
				 Time.DAY))) {

			try {
				faroSubscriptionDisplay = new FaroSubscriptionDisplay(
					provisioningClient.getOSBAccountEntry(
						faroProject.getCorpProjectUuid()));

				FaroProjectLocalServiceUtil.updateSubscription(
					faroProject.getFaroProjectId(),
					JSONUtil.writeValueAsString(faroSubscriptionDisplay));
			}
			catch (Exception e) {
				_log.error(e, e);
			}
		}

		if (faroSubscriptionDisplay == null) {
			try {
				faroSubscriptionDisplay = JSONUtil.readValue(
					faroProject.getSubscription(),
					FaroSubscriptionDisplay.class);
			}
			catch (Exception e) {
				_log.error(e, e);

				return null;
			}
		}

		try {
			faroSubscriptionDisplay.setCounts(
				faroProject, cerebroEngineClient, contactsEngineClient);
		}
		catch (Exception e) {
			_state = ProjectConstants.STATE_UNAVAILABLE;
		}

		return faroSubscriptionDisplay;
	}

	private static final Log _log = LogFactoryUtil.getLog(ProjectDisplay.class);

	private String _accountKey;
	private String _accountName;
	private String _corpProjectName;
	private String _corpProjectUuid;
	private FaroSubscriptionDisplay _faroSubscriptionDisplay;
	private String _friendlyURL;
	private long _groupId;
	private String _name;
	private boolean _recommendationsEnabled;
	private String _serverLocation;
	private String _state;
	private Date _stateEndDate;
	private Date _stateStartDate;
	private long _userId;

}