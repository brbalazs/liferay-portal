/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.websocket.whiteboard.test.encode.data;

import java.io.StringWriter;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

/**
 * @author Cristina González
 */
public class ExampleEncoder implements Encoder.Text<Example> {

	@Override
	public void destroy() {
	}

	@Override
	public String encode(Example example) throws EncodeException {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Example.class);

			Marshaller marshaller = jaxbContext.createMarshaller();

			StringWriter stringWriter = new StringWriter();

			marshaller.marshal(example, stringWriter);

			return stringWriter.toString();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void init(EndpointConfig endpointConfig) {
	}

}