/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.action;

import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.struts.JSONAction;
import com.liferay.ratings.kernel.model.RatingsStats;
import com.liferay.ratings.kernel.service.RatingsEntryServiceUtil;
import com.liferay.ratings.kernel.service.RatingsStatsLocalServiceUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * @author Brian Wing Shun Chan
 */
public class RateEntryAction extends JSONAction {

	@Override
	public String getJSON(
			ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String className = getClassName(request);
		long classPK = getClassPK(request);
		double score = ParamUtil.getDouble(request, "score");

		if (score == -1) {
			RatingsEntryServiceUtil.deleteEntry(className, classPK);
		}
		else {
			RatingsEntryServiceUtil.updateEntry(className, classPK, score);
		}

		RatingsStats stats = RatingsStatsLocalServiceUtil.fetchStats(
			className, classPK);

		double averageScore = 0.0;
		int totalEntries = 0;
		double totalScore = 0.0;

		if (stats != null) {
			averageScore = stats.getAverageScore();
			totalEntries = stats.getTotalEntries();
			totalScore = stats.getTotalScore();
		}

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		jsonObject.put("averageScore", averageScore);
		jsonObject.put("score", score);
		jsonObject.put("totalEntries", totalEntries);
		jsonObject.put("totalScore", totalScore);

		return jsonObject.toString();
	}

	protected String getClassName(HttpServletRequest request) {
		return ParamUtil.getString(request, "className");
	}

	protected long getClassPK(HttpServletRequest request) {
		return ParamUtil.getLong(request, "classPK");
	}

}