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

import com.liferay.osb.faro.constants.FaroUserConstants;
import com.liferay.osb.faro.engine.client.HubSpotEngineClient;
import com.liferay.osb.faro.model.FaroUser;
import com.liferay.osb.faro.service.FaroProjectLocalService;
import com.liferay.osb.faro.service.FaroUserLocalService;
import com.liferay.portal.kernel.exception.ModelListenerException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.BaseModelListener;
import com.liferay.portal.kernel.model.ModelListener;
import com.liferay.portal.kernel.service.UserLocalService;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Matthew Kong
 */
@Component(immediate = true, service = ModelListener.class)
public class FaroUserModelListener extends BaseModelListener<FaroUser> {

	@Override
	public void onBeforeUpdate(FaroUser faroUser)
		throws ModelListenerException {

		try {
			FaroUser curFaroUser = _faroUserLocalService.getFaroUser(
				faroUser.getFaroUserId());

			if ((curFaroUser.getStatus() != FaroUserConstants.STATUS_PENDING) ||
				(faroUser.getStatus() != FaroUserConstants.STATUS_APPROVED)) {

				return;
			}

			_hubSpotEngineClient.submitWorkspaceUserForm(
				_faroProjectLocalService.getFaroProjectByGroupId(
					faroUser.getGroupId()),
				faroUser, false);
		}
		catch (Exception exception) {
			_log.error(
				"Unable to submit HubSpot form for " +
					faroUser.getEmailAddress(),
				exception);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		FaroUserModelListener.class);

	@Reference
	private FaroProjectLocalService _faroProjectLocalService;

	@Reference
	private FaroUserLocalService _faroUserLocalService;

	@Reference
	private HubSpotEngineClient _hubSpotEngineClient;

	@Reference
	private UserLocalService _userLocalService;

}