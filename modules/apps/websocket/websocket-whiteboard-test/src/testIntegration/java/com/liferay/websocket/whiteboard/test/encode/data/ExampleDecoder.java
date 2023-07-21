/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.websocket.whiteboard.test.encode.data;

import java.io.StringReader;

import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

/**
 * @author Cristina González
 */
public class ExampleDecoder implements Decoder.Text<Example> {

	@Override
	public Example decode(String message) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Example.class);

			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

			StringReader stringReader = new StringReader(message);

			return (Example)unmarshaller.unmarshal(stringReader);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void destroy() {
	}

	@Override
	public void init(EndpointConfig endpointConfig) {
	}

	@Override
	public boolean willDecode(String message) {
		if (message != null) {
			return true;
		}

		return false;
	}

}