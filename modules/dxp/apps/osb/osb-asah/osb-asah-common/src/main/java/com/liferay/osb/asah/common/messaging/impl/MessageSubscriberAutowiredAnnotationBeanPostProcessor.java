/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.messaging.impl;

import com.liferay.osb.asah.common.messaging.MessageBus;
import com.liferay.osb.asah.common.messaging.MessageSubscriber;

import java.lang.reflect.Field;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

/**
 * @author Marcellus Tavares
 */
@Component
public class MessageSubscriberAutowiredAnnotationBeanPostProcessor
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
			new MessageSubscriberAutowiredFieldCallback(bean, _messageBus));

		return bean;
	}

	@Autowired
	private MessageBus _messageBus;

	private static class MessageSubscriberAutowiredFieldCallback
		implements ReflectionUtils.FieldCallback {

		public MessageSubscriberAutowiredFieldCallback(
			Object bean, MessageBus messageBus) {

			_bean = bean;
			_messageBus = messageBus;
		}

		@Override
		public void doWith(Field field)
			throws IllegalAccessException, IllegalArgumentException {

			if (!field.isAnnotationPresent(MessageSubscriber.Autowired.class)) {
				return;
			}

			Class<?> fieldTypeClass = field.getType();

			if (!fieldTypeClass.equals(MessageSubscriber.class)) {
				throw new IllegalArgumentException(
					"Unable to autowire MessageSubscriber due to " +
						"inconsistent bean type");
			}

			MessageSubscriber.Autowired messageSubscriberAutowired =
				field.getAnnotation(MessageSubscriber.Autowired.class);

			Class<?> beanClass = _bean.getClass();

			MessageSubscriber messageSubscriber =
				_messageBus.registerMessageSubscriber(
					messageSubscriberAutowired.channel(), beanClass.getName());

			ReflectionUtils.makeAccessible(field);

			field.set(_bean, messageSubscriber);
		}

		private final Object _bean;
		private final MessageBus _messageBus;

	}

}