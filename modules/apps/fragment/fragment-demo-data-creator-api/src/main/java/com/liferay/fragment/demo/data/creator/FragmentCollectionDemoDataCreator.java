/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.fragment.demo.data.creator;

import aQute.bnd.annotation.ProviderType;

import com.liferay.fragment.model.FragmentCollection;
import com.liferay.portal.kernel.exception.PortalException;

import java.io.IOException;

/**
 * @author Jürgen Kappler
 */
@ProviderType
public interface FragmentCollectionDemoDataCreator {

	public FragmentCollection create(long userId, long groupId, String name)
		throws IOException, PortalException;

	public void delete() throws PortalException;

}