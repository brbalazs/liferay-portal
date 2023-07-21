/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.user.associated.data.web.internal.portlet.action;

import com.liferay.petra.function.UnsafeConsumer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.user.associated.data.anonymizer.UADAnonymizer;
import com.liferay.user.associated.data.display.UADDisplay;
import com.liferay.user.associated.data.web.internal.registry.UADRegistry;
import com.liferay.user.associated.data.web.internal.util.SelectedUserHelper;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.ActionRequest;

import org.osgi.service.component.annotations.Reference;

/**
 * @author Drew Brokke
 */
public abstract class BaseUADMVCActionCommand extends BaseMVCActionCommand {

	protected void doMultipleAction(
			ActionRequest actionRequest,
			UnsafeConsumer<Object, Exception> unsafeConsumer)
		throws Exception {

		for (Object entity : getEntities(actionRequest)) {
			unsafeConsumer.accept(entity);
		}
	}

	protected List<Object> getEntities(ActionRequest actionRequest)
		throws Exception {

		List<Object> entities = new ArrayList<>();

		String[] primaryKeys = ParamUtil.getStringValues(
			actionRequest, "primaryKeys");

		for (String primaryKey : primaryKeys) {
			UADDisplay uadDisplay = getUADDisplay(actionRequest);

			entities.add(uadDisplay.get(primaryKey));
		}

		return entities;
	}

	protected Object getEntity(ActionRequest actionRequest) throws Exception {
		UADDisplay uadDisplay = getUADDisplay(actionRequest);

		String primaryKey = ParamUtil.getString(actionRequest, "primaryKey");

		return uadDisplay.get(primaryKey);
	}

	protected User getSelectedUser(ActionRequest actionRequest)
		throws PortalException {

		return selectedUserHelper.getSelectedUser(actionRequest);
	}

	protected long getSelectedUserId(ActionRequest actionRequest)
		throws PortalException {

		return selectedUserHelper.getSelectedUserId(actionRequest);
	}

	protected UADAnonymizer getUADAnonymizer(ActionRequest actionRequest) {
		return uadRegistry.getUADAnonymizer(getUADRegistryKey(actionRequest));
	}

	protected UADDisplay getUADDisplay(ActionRequest actionRequest) {
		return uadRegistry.getUADDisplay(getUADRegistryKey(actionRequest));
	}

	protected String getUADRegistryKey(ActionRequest actionRequest) {
		return ParamUtil.getString(actionRequest, "uadRegistryKey");
	}

	@Reference
	protected SelectedUserHelper selectedUserHelper;

	@Reference
	protected UADRegistry uadRegistry;

}