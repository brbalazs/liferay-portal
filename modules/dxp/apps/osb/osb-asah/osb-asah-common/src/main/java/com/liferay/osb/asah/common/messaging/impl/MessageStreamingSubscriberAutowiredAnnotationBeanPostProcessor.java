/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.messaging.impl;

import com.liferay.osb.asah.common.messaging.MessageBus;
import com.liferay.osb.asah.common.messaging.MessageStreamingSubscriber;

import java.lang.reflect.Field;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

/**
 * @author Robson Pastor
 */
@Component
public class MessageStreamingSubscriberAutowiredAnnotationBeanPostProcessor
	implements BeanPostProcessor {

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName)
		throws BeansException {

		return bean;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName)
		throws BeansException {

		ReflectionUtils.doWithFields(
			bean.getClass(),
			new MessageStreamingSubscriberAutowiredFieldCallback(
				bean, _messageBus));

		return bean;
	}

	@Autowired
	private MessageBus _messageBus;

	private static class MessageStreamingSubscriberAutowiredFieldCallback
		implements ReflectionUtils.FieldCallback {

		public MessageStreamingSubscriberAutowiredFieldCallback(
			Object bean, MessageBus messageBus) {

			_bean = bean;
			_messageBus = messageBus;
		}

		@Override
		public void doWith(Field field)
			throws IllegalAccessException, IllegalArgumentException {

			if (!field.isAnnotationPresent(
					MessageStreamingSubscriber.Autowired.class)) {

				return;
			}

			Class<?> fieldTypeClass = field.getType();

			if (!fieldTypeClass.equals(MessageStreamingSubscriber.class)) {
				throw new IllegalArgumentException(
					"Unable to autowire MessageStreamingSubscriber due to " +
						"inconsistent bean type");
			}

			MessageStreamingSubscriber.Autowired
				messageStreamingSubscriberAutowired = field.getAnnotation(
					MessageStreamingSubscriber.Autowired.class);

			Class<?> beanClass = _bean.getClass();

			MessageStreamingSubscriber messageStreamingSubscriber =
				_messageBus.registerMessageStreamingSubscriber(
					messageStreamingSubscriberAutowired.channel(),
					beanClass.getName());

			ReflectionUtils.makeAccessible(field);

			field.set(_bean, messageStreamingSubscriber);
		}

		private final Object _bean;
		private final MessageBus _messageBus;

	}

}