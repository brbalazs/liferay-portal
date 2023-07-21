/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.search;

import aQute.bnd.annotation.ProviderType;

import java.util.List;

/**
 * @author Michael C. Han
 */
@ProviderType
public interface RelatedEntryIndexerRegistry {

	public List<RelatedEntryIndexer> getRelatedEntryIndexers();

	public List<RelatedEntryIndexer> getRelatedEntryIndexers(Class clazz);

	public List<RelatedEntryIndexer> getRelatedEntryIndexers(String className);

}