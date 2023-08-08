/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.messaging;

import com.google.cloud.pubsub.v1.MessageReceiver;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Robson Pastor
 */
public interface MessageStreamingSubscriber {

	public void subscribe(
		long maxOutstandingMessages, MessageReceiver messageReceiver);

	@Documented
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface Autowired {

		public Channel channel();

	}

}