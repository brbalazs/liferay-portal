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

package com.liferay.osb.faro.web.internal.constants;

import com.liferay.osb.faro.engine.client.model.LCPProject;
import com.liferay.osb.faro.engine.client.model.Workspace;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Matthew Kong
 */
public class ProjectConstants {

	public static final String STATE_ACTIVATING = "ACTIVATING";

	public static final String STATE_DEACTIVATED = "DEACTIVATED";

	public static final String STATE_MAINTENANCE = "MAINTENANCE";

	public static final String STATE_NOT_READY = "NOT READY";

	public static final String STATE_READY = "READY";

	public static final String STATE_SCHEDULED = "SCHEDULED";

	public static final String STATE_UNAVAILABLE = "UNAVAILABLE";

	public static final String STATE_UNCONFIGURED = "UNCONFIGURED";

	public static Map<String, String> getLocations() {
		return _locations;
	}

	public static Map<String, String> getStates() {
		return _states;
	}

	private static final Map<String, String> _locations =
		new HashMap<String, String>() {
			{
				put("EU", LCPProject.Cluster.EU.toString());
				put("EU2", LCPProject.Cluster.EU2.toString());
				put("SA", LCPProject.Cluster.SA.toString());
				put("US", LCPProject.Cluster.US.toString());
			}
		};
	private static final Map<String, String> _states =
		new HashMap<String, String>() {
			{
				put("activating", STATE_ACTIVATING);
				put("autoRedeployFailed", Workspace.AUTO_REDEPLOY_FAILED);
				put("deactivated", STATE_DEACTIVATED);
				put("maintenance", STATE_MAINTENANCE);
				put("notReady", STATE_NOT_READY);
				put("ready", STATE_READY);
				put("scheduled", STATE_SCHEDULED);
				put("unavailable", STATE_UNAVAILABLE);
				put("unconfigured", STATE_UNCONFIGURED);
			}
		};

}