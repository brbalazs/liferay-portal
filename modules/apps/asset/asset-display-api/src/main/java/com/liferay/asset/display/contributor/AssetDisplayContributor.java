/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.asset.display.contributor;

import com.liferay.asset.kernel.AssetRendererFactoryRegistryUtil;
import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.model.AssetRendererFactory;
import com.liferay.asset.kernel.model.ClassType;
import com.liferay.asset.kernel.model.ClassTypeField;
import com.liferay.asset.kernel.model.ClassTypeReader;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.PortalUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * @author Jürgen Kappler
 */
public interface AssetDisplayContributor {

	public Set<AssetDisplayField> getAssetDisplayFields(
			long classTypeId, Locale locale)
		throws PortalException;

	public Map<String, Object> getAssetDisplayFieldsValues(
			AssetEntry assetEntry, Locale locale)
		throws PortalException;

	public String getClassName();

	public default List<AssetDisplayField> getClassTypeFields(
			long classTypeId, Locale locale)
		throws PortalException {

		if (classTypeId == 0) {
			return Collections.emptyList();
		}

		AssetRendererFactory assetRendererFactory =
			AssetRendererFactoryRegistryUtil.getAssetRendererFactoryByClassName(
				getClassName());

		if ((assetRendererFactory == null) ||
			!assetRendererFactory.isSupportsClassTypes()) {

			return Collections.emptyList();
		}

		ClassTypeReader classTypeReader =
			assetRendererFactory.getClassTypeReader();

		ClassType classType = classTypeReader.getClassType(classTypeId, locale);

		if (classType == null) {
			return Collections.emptyList();
		}

		List<AssetDisplayField> classTypeFields = new ArrayList<>();

		for (ClassTypeField classTypeField : classType.getClassTypeFields()) {
			classTypeFields.add(
				new AssetDisplayField(
					classTypeField.getName(), classTypeField.getLabel(),
					classTypeField.getType()));
		}

		return classTypeFields;
	}

	public default List<ClassType> getClassTypes(long groupId, Locale locale)
		throws PortalException {

		AssetRendererFactory assetRendererFactory =
			AssetRendererFactoryRegistryUtil.getAssetRendererFactoryByClassName(
				getClassName());

		if ((assetRendererFactory == null) ||
			!assetRendererFactory.isSupportsClassTypes()) {

			return Collections.emptyList();
		}

		ClassTypeReader classTypeReader =
			assetRendererFactory.getClassTypeReader();

		return classTypeReader.getAvailableClassTypes(
			PortalUtil.getCurrentAndAncestorSiteGroupIds(groupId), locale);
	}

	public String getLabel(Locale locale);

}