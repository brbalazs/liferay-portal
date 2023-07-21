/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.asset.taglib.internal.util;

import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.PredicateFilter;

import java.util.List;

/**
 * @author Eudaldo Alonso
 */
public class AssetVocabularyUtil {

	public static List<AssetVocabulary> filterVocabularies(
		List<AssetVocabulary> vocabularies, String className,
		final long classTypePK) {

		final long classNameId = PortalUtil.getClassNameId(className);

		PredicateFilter<AssetVocabulary> predicateFilter =
			new PredicateFilter<AssetVocabulary>() {

				@Override
				public boolean filter(AssetVocabulary assetVocabulary) {
					return assetVocabulary.
						isAssociatedToClassNameIdAndClassTypePK(
							classNameId, classTypePK);
				}

			};

		return ListUtil.filter(vocabularies, predicateFilter);
	}

}