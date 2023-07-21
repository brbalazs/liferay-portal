/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.push.notifications.service.impl;

import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.push.notifications.constants.PushNotificationsDestinationNames;
import com.liferay.push.notifications.model.PushNotificationsDevice;
import com.liferay.push.notifications.sender.BaseResponse;
import com.liferay.push.notifications.sender.PushNotificationsSender;
import com.liferay.push.notifications.service.base.PushNotificationsDeviceLocalServiceBaseImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

/**
 * @author Silvio Santos
 * @author Bruno Farache
 */
public class PushNotificationsDeviceLocalServiceImpl
	extends PushNotificationsDeviceLocalServiceBaseImpl {

	@Override
	public PushNotificationsDevice addPushNotificationsDevice(
			long userId, String platform, String token)
		throws PortalException {

		User user = userLocalService.getUser(userId);

		long pushNotificationsDeviceId = counterLocalService.increment();

		PushNotificationsDevice pushNotificationsDevice =
			pushNotificationsDevicePersistence.create(
				pushNotificationsDeviceId);

		pushNotificationsDevice.setCompanyId(user.getCompanyId());
		pushNotificationsDevice.setUserId(user.getUserId());
		pushNotificationsDevice.setCreateDate(new Date());
		pushNotificationsDevice.setPlatform(platform);
		pushNotificationsDevice.setToken(token);

		return pushNotificationsDevicePersistence.update(
			pushNotificationsDevice);
	}

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();

		Bundle bundle = FrameworkUtil.getBundle(
			PushNotificationsDeviceLocalServiceImpl.class);

		BundleContext bundleContext = bundle.getBundleContext();

		_serviceTrackerMap = ServiceTrackerMapFactory.openSingleValueMap(
			bundleContext, PushNotificationsSender.class, "platform");
	}

	@Override
	public PushNotificationsDevice deletePushNotificationsDevice(String token)
		throws PortalException {

		PushNotificationsDevice pushNotificationsDevice =
			pushNotificationsDevicePersistence.findByToken(token);

		pushNotificationsDevicePersistence.remove(pushNotificationsDevice);

		return pushNotificationsDevice;
	}

	@Override
	public void destroy() {
		super.destroy();

		_serviceTrackerMap.close();
	}

	@Override
	public List<PushNotificationsDevice> getPushNotificationsDevices(
		int start, int end,
		OrderByComparator<PushNotificationsDevice> orderByComparator) {

		return pushNotificationsDevicePersistence.findAll(
			start, end, orderByComparator);
	}

	@Override
	public void sendPushNotification(
			long[] toUserIds, JSONObject payloadJSONObject)
		throws PortalException {

		for (String platform : _serviceTrackerMap.keySet()) {
			List<String> tokens = new ArrayList<>();

			List<PushNotificationsDevice> pushNotificationsDevices =
				pushNotificationsDevicePersistence.findByU_P(
					toUserIds, platform, QueryUtil.ALL_POS, QueryUtil.ALL_POS);

			for (PushNotificationsDevice pushNotificationsDevice :
					pushNotificationsDevices) {

				tokens.add(pushNotificationsDevice.getToken());
			}

			if (tokens.isEmpty()) {
				continue;
			}

			sendPushNotification(platform, tokens, payloadJSONObject);
		}
	}

	@Override
	public void sendPushNotification(
			String platform, List<String> tokens, JSONObject payloadJSONObject)
		throws PortalException {

		PushNotificationsSender pushNotificationsSender =
			_serviceTrackerMap.getService(platform);

		if (pushNotificationsSender == null) {
			return;
		}

		Exception exception = null;

		try {
			pushNotificationsSender.send(tokens, payloadJSONObject);
		}
		catch (PortalException pe) {
			exception = pe;

			throw pe;
		}
		catch (Exception e) {
			exception = e;

			throw new PortalException(e);
		}
		finally {
			if (exception != null) {
				MessageBusUtil.sendMessage(
					PushNotificationsDestinationNames.
						PUSH_NOTIFICATION_RESPONSE,
					new BaseResponse(platform, exception));
			}
		}
	}

	@Override
	public void updateToken(String oldToken, String newToken)
		throws PortalException {

		PushNotificationsDevice oldPushNotificationsDevice =
			deletePushNotificationsDevice(oldToken);

		PushNotificationsDevice newPushNotificationsDevice =
			pushNotificationsDevicePersistence.fetchByToken(newToken);

		if (newPushNotificationsDevice == null) {
			addPushNotificationsDevice(
				oldPushNotificationsDevice.getUserId(),
				oldPushNotificationsDevice.getPlatform(), newToken);
		}
	}

	private ServiceTrackerMap<String, PushNotificationsSender>
		_serviceTrackerMap;

}