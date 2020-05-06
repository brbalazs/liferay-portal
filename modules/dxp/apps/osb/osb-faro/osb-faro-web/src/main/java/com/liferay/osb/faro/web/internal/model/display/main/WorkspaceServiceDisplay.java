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

package com.liferay.osb.faro.web.internal.model.display.main;

import com.liferay.osb.faro.engine.client.model.WorkspaceService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Shinn Lok
 */
@SuppressWarnings({"FieldCanBeLocal", "UnusedDeclaration"})
public class WorkspaceServiceDisplay {

	public WorkspaceServiceDisplay() {
	}

	public WorkspaceServiceDisplay(WorkspaceService workspaceService) {
		Matcher matcher = _pattern.matcher(workspaceService.getImageHint());

		if (matcher.find()) {
			_branch = matcher.group(3);
		}
		else {
			_branch = workspaceService.getImageHint();
		}

		_health = workspaceService.getHealth();
		_ready = workspaceService.isReady();
		_serviceId = workspaceService.getServiceId();
	}

	public String getBranch() {
		return _branch;
	}

	public String getHealth() {
		return _health;
	}

	public String getServiceId() {
		return _serviceId;
	}

	public boolean isReady() {
		return _ready;
	}

	public void setBranch(String branch) {
		_branch = branch;
	}

	public void setHealth(String health) {
		_health = health;
	}

	public void setReady(boolean ready) {
		_ready = ready;
	}

	public void setServiceId(String serviceId) {
		_serviceId = serviceId;
	}

	private static final Pattern _pattern = Pattern.compile(
		"(com-liferay-osb-asah-private:)([a-zA-z-]+)([a-zA-Z0-9-]+)");

	private String _branch;
	private String _health;
	private boolean _ready;
	private String _serviceId;

}