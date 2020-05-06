/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.osb.faro.engine.client.web.util;

import com.github.fge.uritemplate.URITemplate;
import com.github.fge.uritemplate.vars.VariableMap;
import com.github.fge.uritemplate.vars.VariableMapBuilder;

import java.net.URI;

import java.util.Collection;
import java.util.Map;

import org.springframework.web.util.DefaultUriTemplateHandler;

/**
 * @author Shinn Lok
 */
public class UriTemplateHandler extends DefaultUriTemplateHandler {

	@Override
	public URI expand(String uriTemplateString, Map<String, ?> uriVariables) {
		try {
			URITemplate uriTemplate = new URITemplate(uriTemplateString);

			VariableMapBuilder variableMapBuilder = VariableMap.newBuilder();

			for (Map.Entry<String, ?> entry : uriVariables.entrySet()) {
				Object value = entry.getValue();

				if (value == null) {
					continue;
				}

				if (value instanceof Collection<?>) {
					variableMapBuilder.addListValue(
						entry.getKey(), (Collection<?>)value);
				}
				else {
					variableMapBuilder.addScalarValue(entry.getKey(), value);
				}
			}

			return uriTemplate.toURI(variableMapBuilder.freeze());
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}