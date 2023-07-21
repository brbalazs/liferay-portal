/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.contacts.demo.internal.data.creator;

import com.liferay.osb.faro.engine.client.ContactsEngineClient;
import com.liferay.osb.faro.model.FaroProject;
import com.liferay.portal.kernel.util.Time;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Matthew Kong
 */
public class LiferayExperimentsDataCreator extends DataCreator {

	public LiferayExperimentsDataCreator(
		ContactsEngineClient contactsEngineClient, FaroProject faroProject,
		String channelId, String dataSourceId) {

		super(
			contactsEngineClient, faroProject, "osbasahfaroinfo",
			"experiments");

		_channelId = channelId;
		_dataSourceId = dataSourceId;
	}

	@Override
	protected Map<String, Object> doCreate(Object[] params) {
		Map<String, Object> experiment = new HashMap<>();

		experiment.put("channelId", _channelId);
		experiment.put("confidenceLevel", 95);

		String dateString = formatDate(
			new Date(
				System.currentTimeMillis() -
					(number.numberBetween(0, 100) * Time.DAY)));

		experiment.put("createDate", dateString);

		experiment.put("dataSourceId", _dataSourceId);
		experiment.put("dxpExperienceId", "DEFAULT");
		experiment.put("dxpExperienceName", "Default");
		experiment.put("dxpGroupId", number.randomNumber(8, false));
		experiment.put("dxpLayoutId", internet.uuid());
		experiment.put("dxpSegmentId", "DEFAULT");
		experiment.put("dxpSegmentName", "Anyone");
		experiment.put(
			"dxpVariants",
			Arrays.asList(
				new HashMap() {
					{
						put("changes", 0);
						put("control", true);
						put("dxpVariantId", "DEFAULT");
						put("dxpVariantName", "Control");
						put("trafficSplit", 34);
					}
				},
				new HashMap() {
					{
						put("changes", 0);
						put("control", false);
						put(
							"dxpVariantId",
							String.valueOf(number.randomNumber(8, false)));
						put("dxpVariantName", company.buzzword());
						put("trafficSplit", 33);
					}
				},
				new HashMap() {
					{
						put("changes", 0);
						put("control", false);
						put(
							"dxpVariantId",
							String.valueOf(number.randomNumber(8, false)));
						put("dxpVariantName", company.buzzword());
						put("trafficSplit", 33);
					}
				}));
		experiment.put(
			"goal",
			new HashMap<String, String>() {
				{
					put("metric", "BOUNCE_RATE");
					put("target", "");
				}
			});
		experiment.put("modifiedDate", dateString);

		Map<String, Object> pageContext = (Map<String, Object>)params[0];

		experiment.put("name", pageContext.get("title") + " Testing");
		experiment.put("pageURL", pageContext.get("canonicalUrl"));

		experiment.put("startedDate", dateString);
		experiment.put("status", "RUNNING");
		experiment.put("type", "AB");

		return experiment;
	}

	private final String _channelId;
	private final String _dataSourceId;

}