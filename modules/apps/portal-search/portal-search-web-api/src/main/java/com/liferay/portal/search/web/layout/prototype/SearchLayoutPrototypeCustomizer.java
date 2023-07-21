/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.web.layout.prototype;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.model.Layout;

/**
 * @author Michael C. Han
 */
@ProviderType
public interface SearchLayoutPrototypeCustomizer {

	public void customize(Layout layout) throws Exception;

	public String getLayoutTemplateId();

}