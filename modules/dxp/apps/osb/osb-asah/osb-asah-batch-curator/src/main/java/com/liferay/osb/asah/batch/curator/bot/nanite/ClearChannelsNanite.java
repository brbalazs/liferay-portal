/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.batch.curator.bot.nanite;

import com.liferay.osb.asah.common.dog.ChannelDog;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.spring.annotation.CacheEvict;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author André Miranda
 */
@Component
public class ClearChannelsNanite extends BaseNanite {

	@Override
	public boolean isLogRunEnabled() {
		return true;
	}

	@CacheEvict(evictAll = true)
	@Override
	public void run(JSONObject contextJSONObject) throws Exception {
		_channelDog.clearChannels(
			JSONUtil.toLongSet(contextJSONObject.getJSONArray("channelIds")),
			true, String.valueOf(contextJSONObject.get("userId")),
			String.valueOf(contextJSONObject.get("userName")));
	}

	@Override
	protected Log getLog() {
		return LogFactory.getLog(ClearChannelsNanite.class);
	}

	@Autowired
	private ChannelDog _channelDog;

}