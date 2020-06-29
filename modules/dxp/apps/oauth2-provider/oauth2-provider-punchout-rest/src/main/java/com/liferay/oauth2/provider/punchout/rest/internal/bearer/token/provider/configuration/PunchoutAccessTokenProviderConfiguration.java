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

package com.liferay.oauth2.provider.punchout.rest.internal.bearer.token.provider.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Jaclyn Ong
 */
@ExtendedObjectClassDefinition(category = "oauth2")
@Meta.OCD(
	id = "com.liferay.oauth2.provider.punchout.rest.internal.bearer.token.provider.configuration.PunchoutAccessTokenProviderConfiguration",
	localization = "content/Language",
	name = "punchout-access-token-provider-configuration-name"
)
public interface PunchoutAccessTokenProviderConfiguration {

	@Meta.AD(
		deflt = "15", description = "access-token-expires-in-description",
		id = "access.token.expires.in", name = "access-token-expires-in",
		required = false
	)
	public int accessTokenExpiresIn();

	@Meta.AD(
		deflt = "8", description = "access-token-key-byte-size-description",
		id = "access.token.key.byte.size", name = "access-token-key-byte-size",
		required = false
	)
	public int accessTokenKeyByteSize();

}