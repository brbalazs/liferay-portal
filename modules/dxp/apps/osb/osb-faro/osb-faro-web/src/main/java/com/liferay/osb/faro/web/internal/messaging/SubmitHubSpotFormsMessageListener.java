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

package com.liferay.osb.faro.web.internal.messaging;

import com.liferay.osb.faro.constants.FaroProjectConstants;
import com.liferay.osb.faro.engine.client.CerebroEngineClient;
import com.liferay.osb.faro.engine.client.ContactsEngineClient;
import com.liferay.osb.faro.engine.client.HubSpotEngineClient;
import com.liferay.osb.faro.model.FaroProject;
import com.liferay.osb.faro.model.FaroUser;
import com.liferay.osb.faro.provisioning.client.constants.ProductConstants;
import com.liferay.osb.faro.service.FaroProjectLocalService;
import com.liferay.osb.faro.service.FaroUserLocalService;
import com.liferay.osb.faro.web.internal.model.display.main.FaroSubscriptionDisplay;
import com.liferay.osb.faro.web.internal.util.JSONUtil;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.BaseMessageListener;
import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.scheduler.SchedulerEngineHelper;
import com.liferay.portal.kernel.scheduler.SchedulerEntryImpl;
import com.liferay.portal.kernel.scheduler.Trigger;
import com.liferay.portal.kernel.scheduler.TriggerFactory;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Time;

import java.util.Date;
import java.util.Optional;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Matthew Kong
 */
@Component(immediate = true, service = SubmitHubSpotFormsMessageListener.class)
public class SubmitHubSpotFormsMessageListener extends BaseMessageListener {

	@Activate
	protected void activate() {
		Class<?> clazz = getClass();

		Trigger trigger = _triggerFactory.createTrigger(
			clazz.getName(), clazz.getName(), new Date(), null, "0 0 0 * * ?");

		_schedulerEngineHelper.register(
			this, new SchedulerEntryImpl(clazz.getName(), trigger),
			DestinationNames.SCHEDULER_DISPATCH);
	}

	@Deactivate
	protected void deactivate() {
		_schedulerEngineHelper.unregister(this);
	}

	@Override
	protected void doReceive(Message message) {
		long now = System.currentTimeMillis();

		for (FaroProject faroProject :
				_faroProjectLocalService.getFaroProjects(
					QueryUtil.ALL_POS, QueryUtil.ALL_POS)) {

			if (!StringUtil.equals(
					faroProject.getState(), FaroProjectConstants.STATE_READY)) {

				continue;
			}

			try {
				FaroSubscriptionDisplay faroSubscriptionDisplay =
					JSONUtil.readValue(
						faroProject.getSubscription(),
						FaroSubscriptionDisplay.class);

				FaroUser faroUser = _faroUserLocalService.getFaroUser(
					faroProject.getGroupId(), faroProject.getUserId());

				long pageViews = _cerebroEngineClient.getPageViews(
					faroProject,
					Optional.of(faroSubscriptionDisplay.getStartDate()),
					Optional.of(new Date()));

				_hubSpotEngineClient.submitUsageForm(
					faroProject, faroUser,
					GetterUtil.getDouble(pageViews) /
						faroSubscriptionDisplay.getPageViewsLimit());

				if (StringUtil.equals(
						faroSubscriptionDisplay.getName(),
						ProductConstants.BASIC_PRODUCT_NAME)) {

					long time = now - faroProject.getLastAccessTime();

					if ((time <= Time.DAY) ||
						((time >= (Time.DAY * 28)) &&
						 (time < (Time.DAY * 29)))) {

						_hubSpotEngineClient.submitWorkspaceExpirationForm(
							faroProject, faroUser);
					}
				}
			}
			catch (Exception exception) {
				if (_log.isInfoEnabled()) {
					_log.info(
						"Failed to submit HubSpot form: " +
							faroProject.getGroupId());
				}
			}
		}
	}

	@Modified
	protected void modified() {
		deactivate();

		activate();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SubmitHubSpotFormsMessageListener.class);

	@Reference(
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY
	)
	private volatile CerebroEngineClient _cerebroEngineClient;

	@Reference(
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY
	)
	private volatile ContactsEngineClient _contactsEngineClient;

	@Reference
	private FaroProjectLocalService _faroProjectLocalService;

	@Reference
	private FaroUserLocalService _faroUserLocalService;

	@Reference(
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY
	)
	private volatile HubSpotEngineClient _hubSpotEngineClient;

	@Reference
	private SchedulerEngineHelper _schedulerEngineHelper;

	@Reference
	private TriggerFactory _triggerFactory;

	@Reference
	private UserLocalService _userLocalService;

}