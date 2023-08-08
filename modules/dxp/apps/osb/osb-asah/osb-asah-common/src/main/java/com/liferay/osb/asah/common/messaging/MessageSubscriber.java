/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.messaging;

import com.liferay.osb.asah.common.function.UnsafeFunction;
import com.liferay.osb.asah.common.messaging.model.Message;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import java.util.List;

/**
 * @author Marcellus Tavares
 */
public interface MessageSubscriber {

	public <T> List<Message<T>> pullMessages(
			int maxMessages,
			UnsafeFunction<String, T, Exception> modelMapperUnsafeFunction)
		throws Exception;

	public <T> void registerException(Exception exception, Message<T> message);

	public void sendAckIds(List<String> ackIds);

	@Documented
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface Autowired {

		public Channel channel();

	}

}