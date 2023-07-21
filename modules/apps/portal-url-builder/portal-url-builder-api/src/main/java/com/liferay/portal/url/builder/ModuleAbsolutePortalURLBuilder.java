/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.url.builder;

/**
 * A builder that returns a module resource URL. Module resources live in {@link
 * com.liferay.portal.kernel.util.Portal#getPathModule()}.
 *
 * @author Iván Zaera Avellón
 * @review
 */
public interface ModuleAbsolutePortalURLBuilder
	extends BuildableAbsolutePortalURLBuilder {
}