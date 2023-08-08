/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.spring.condition;

import com.liferay.osb.asah.common.spring.annotation.ConditionalOnGoogleApplicationCredentials;

import java.util.Map;

import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author Marcellus Tavares
 */
public class OnGoogleApplicationCredentials extends SpringBootCondition {

	@Override
	public ConditionOutcome getMatchOutcome(
		ConditionContext conditionContext,
		AnnotatedTypeMetadata annotatedTypeMetadata) {

		Environment environment = conditionContext.getEnvironment();

		String googleApplicationCredentials = environment.getProperty(
			"GOOGLE_APPLICATION_CREDENTIALS");

		Boolean matchIfMissing =
			_getConditionalOnGoogleCredentialsAnnotationAttribute(
				annotatedTypeMetadata, "matchIfMissing");

		if (googleApplicationCredentials == null) {
			if (matchIfMissing) {
				return ConditionOutcome.match();
			}

			return ConditionOutcome.noMatch(ConditionMessage.empty());
		}

		if (matchIfMissing) {
			return ConditionOutcome.noMatch(ConditionMessage.empty());
		}

		return ConditionOutcome.match();
	}

	private <T> T _getConditionalOnGoogleCredentialsAnnotationAttribute(
		AnnotatedTypeMetadata annotatedTypeMetadata, String attributeName) {

		if (annotatedTypeMetadata != null) {
			Map<String, Object> annotationAttributes =
				annotatedTypeMetadata.getAnnotationAttributes(
					ConditionalOnGoogleApplicationCredentials.class.getName());

			if (annotationAttributes != null) {
				return (T)annotationAttributes.get(attributeName);
			}
		}

		return null;
	}

}