/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.remote.jaxrs.whiteboard.test.internal.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * @author Carlos Sierra Andrés
 */
public class Addon {

	@GET
	@Path("/addon")
	public String addon() {
		return "addon";
	}

}