/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.messaging.proxy;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBus;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.messaging.MessageListener;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.Validator;

/**
 * @author Micha Kiener
 * @author Michael C. Han
 * @author Brian Wing Shun Chan
 * @author Igor Spasic
 */
public class ProxyMessageListener implements MessageListener {

	@Override
	public void receive(Message message) {
		ProxyResponse proxyResponse = new ProxyResponse();

		try {
			Object payload = message.getPayload();

			if (payload == null) {
				throw new Exception("Payload is null");
			}
			else if (!(payload instanceof ProxyRequest)) {
				throw new Exception(
					StringBundler.concat(
						"Payload ", String.valueOf(payload.getClass()),
						" is not of type ", ProxyRequest.class.getName()));
			}

			ProxyRequest proxyRequest = (ProxyRequest)payload;

			if (!proxyRequest.isSynchronous()) {
				MessageValuesThreadLocal.populateThreadLocalsFromMessage(
					message);
			}

			Object result = proxyRequest.execute(_manager);

			proxyResponse.setResult(result);
		}
		catch (Exception e) {
			proxyResponse.setException(e);
		}
		finally {
			String responseDestinationName =
				message.getResponseDestinationName();

			Exception proxyResponseException = proxyResponse.getException();

			if (Validator.isNotNull(responseDestinationName)) {
				Message responseMessage = MessageBusUtil.createResponseMessage(
					message);

				responseMessage.setPayload(proxyResponse);

				if (_log.isDebugEnabled() && (proxyResponseException != null)) {
					_log.debug(proxyResponseException, proxyResponseException);
				}

				_messageBus.sendMessage(
					responseDestinationName, responseMessage);
			}
			else {
				if (proxyResponseException != null) {
					if (_log.isWarnEnabled()) {
						_log.warn(
							proxyResponseException, proxyResponseException);
					}
				}

				message.setResponse(proxyResponse);
			}
		}
	}

	public void setManager(Object manager) {
		_manager = manager;
	}

	public void setMessageBus(MessageBus messageBus) {
		_messageBus = messageBus;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ProxyMessageListener.class);

	private Object _manager;
	private MessageBus _messageBus;

}