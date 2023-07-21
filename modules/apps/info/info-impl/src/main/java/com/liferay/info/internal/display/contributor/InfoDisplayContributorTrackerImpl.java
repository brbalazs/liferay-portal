/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.info.internal.display.contributor;

import com.liferay.info.display.contributor.InfoDisplayContributor;
import com.liferay.info.display.contributor.InfoDisplayContributorTracker;

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
@Component(immediate = true, service = InfoDisplayContributorTracker.class)
public class InfoDisplayContributorTrackerImpl
	implements InfoDisplayContributorTracker {

	@Override
	public InfoDisplayContributor getInfoDisplayContributor(String className) {
		return _infoDisplayContributor.get(className);
	}

	@Override
	public InfoDisplayContributor getInfoDisplayContributorByURLSeparator(
		String urlSeparator) {

		return _infoDisplayContributorByURLSeparator.get(urlSeparator);
	}

	@Override
	public List<InfoDisplayContributor> getInfoDisplayContributors() {
		return new ArrayList(_infoDisplayContributor.values());
	}

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC
	)
	protected void setInfoDisplayContributor(
		InfoDisplayContributor infoDisplayContributor) {

		_infoDisplayContributor.put(
			infoDisplayContributor.getClassName(), infoDisplayContributor);
		_infoDisplayContributorByURLSeparator.put(
			infoDisplayContributor.getInfoURLSeparator(),
			infoDisplayContributor);
	}

	protected void unsetInfoDisplayContributor(
		InfoDisplayContributor infoDisplayContributor) {

		_infoDisplayContributor.remove(infoDisplayContributor.getClassName());
		_infoDisplayContributorByURLSeparator.remove(
			infoDisplayContributor.getInfoURLSeparator());
	}

	private final Map<String, InfoDisplayContributor> _infoDisplayContributor =
		new ConcurrentHashMap<>();
	private final Map<String, InfoDisplayContributor>
		_infoDisplayContributorByURLSeparator = new ConcurrentHashMap<>();

}