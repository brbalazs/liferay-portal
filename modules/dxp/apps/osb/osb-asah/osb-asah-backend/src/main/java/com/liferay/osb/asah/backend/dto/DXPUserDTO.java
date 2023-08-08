/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dto;

import com.liferay.osb.asah.common.entity.BQUser;
import com.liferay.osb.asah.common.graphql.GraphQLType;

/**
 * @author Marcos Martins
 */
@GraphQLType
public class DXPUserDTO extends DXPEntityDTO {

	public DXPUserDTO(BQUser bqUser) {
		super(bqUser);

		_screenName = bqUser.getScreenName();
	}

	public String getScreenName() {
		return _screenName;
	}

	private final String _screenName;

}