/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.fabric.netty.rpc;

import com.liferay.portal.fabric.netty.NettyTestUtil;
import com.liferay.portal.kernel.test.rule.CodeCoverageAssertor;

import io.netty.channel.embedded.EmbeddedChannel;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * @author Shuyang Zhou
 */
public class ChannelThreadLocalTest {

	@ClassRule
	public static final CodeCoverageAssertor codeCoverageAssertor =
		CodeCoverageAssertor.INSTANCE;

	@Test
	public void testConstructor() {
		new ChannelThreadLocal();
	}

	@Test
	public void testGetChannel() {
		try {
			ChannelThreadLocal.getChannel();
		}
		catch (IllegalStateException ise) {
			Assert.assertEquals("Channel is null", ise.getMessage());
		}

		EmbeddedChannel embeddedChannel =
			NettyTestUtil.createEmptyEmbeddedChannel();

		ChannelThreadLocal.setChannel(embeddedChannel);

		Assert.assertSame(embeddedChannel, ChannelThreadLocal.getChannel());

		ChannelThreadLocal.removeChannel();

		try {
			ChannelThreadLocal.getChannel();
		}
		catch (IllegalStateException ise) {
			Assert.assertEquals("Channel is null", ise.getMessage());
		}
	}

}