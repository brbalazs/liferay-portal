/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
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