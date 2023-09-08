/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.batch.curator.bot.nanite;

import com.liferay.osb.asah.common.dog.AuditEventDog;
import com.liferay.osb.asah.common.dog.ChannelDog;
import com.liferay.osb.asah.common.entity.AuditEvent;
import com.liferay.osb.asah.common.json.JSONUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author Rachael Koestartyo
 */
@Component
public class DeleteChannelsNanite extends BaseNanite {

	@Override
	public boolean isLogRunEnabled() {
		return true;
	}

	@Override
	public void run(JSONObject contextJSONObject) throws Exception {
		Set<Long> channelIds = JSONUtil.toLongSet(
			contextJSONObject.getJSONArray("channelIds"));

		_channelDog.deleteChannels(channelIds);

		_auditEventDog.addAuditEvent(
			String.format("Deleted channels %s", channelIds),
			AuditEvent.Type.CHANNEL_DELETE,
			(String)contextJSONObject.get("userId"),
			(String)contextJSONObject.get("userName"));
	}

	@Override
	protected Log getLog() {
		return LogFactory.getLog(DeleteChannelsNanite.class);
	}

	@Autowired
	private AuditEventDog _auditEventDog;

	@Autowired
	private ChannelDog _channelDog;

}