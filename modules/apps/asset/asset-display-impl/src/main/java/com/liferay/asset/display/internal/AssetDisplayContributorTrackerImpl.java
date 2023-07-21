/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.asset.display.internal;

import com.liferay.asset.display.contributor.AssetDisplayContributor;
import com.liferay.asset.display.contributor.AssetDisplayContributorTracker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 * @author Jürgen Kappler
 */
@Component(immediate = true, service = AssetDisplayContributorTracker.class)
public class AssetDisplayContributorTrackerImpl
	implements AssetDisplayContributorTracker {

	@Override
	public AssetDisplayContributor getAssetDisplayContributor(
		String className) {

		return _assetDisplayContributor.get(className);
	}

	@Override
	public List<AssetDisplayContributor> getAssetDisplayContributors() {
		return new ArrayList(_assetDisplayContributor.values());
	}

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC
	)
	protected void setAssetDisplayContributor(
		AssetDisplayContributor assetDisplayContributor) {

		_assetDisplayContributor.put(
			assetDisplayContributor.getClassName(), assetDisplayContributor);
	}

	protected void unsetAssetDisplayContributor(
		AssetDisplayContributor assetDisplayContributor) {

		_assetDisplayContributor.remove(assetDisplayContributor.getClassName());
	}

	private final Map<String, AssetDisplayContributor>
		_assetDisplayContributor = new ConcurrentHashMap<>();

}