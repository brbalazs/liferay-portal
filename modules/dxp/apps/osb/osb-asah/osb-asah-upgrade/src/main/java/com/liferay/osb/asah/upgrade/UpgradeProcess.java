/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.upgrade;

import com.liferay.osb.asah.common.util.ReleaseInfo;

import java.io.Serializable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Marcellus Tavares
 */
public class UpgradeProcess {

	public void addUpgradeSteps(
		String fromVersionString, String toVersionString,
		UpgradeStep... upgradeSteps) {

		VersionComparator versionComparator = new VersionComparator();

		int compare = versionComparator.compare(
			toVersionString, ReleaseInfo.getVersion());

		if (compare > 0) {
			return;
		}

		_upgradeStepsMap.put(
			fromVersionString,
			Pair.of(toVersionString, Arrays.asList(upgradeSteps)));
	}

	public String getMaxVersionString() {
		Collection<Pair<String, List<UpgradeStep>>> values =
			_upgradeStepsMap.values();

		Stream<Pair<String, List<UpgradeStep>>> stream = values.stream();

		return stream.map(
			Pair::getLeft
		).max(
			new VersionComparator()
		).orElseThrow(
			IllegalStateException::new
		);
	}

	public String getToVersionString(String fromVersionString) {
		return Optional.ofNullable(
			_upgradeStepsMap.get(fromVersionString)
		).map(
			Pair::getLeft
		).orElseThrow(
			IllegalArgumentException::new
		);
	}

	public List<UpgradeStep> getUpgradeSteps(String fromVersionString) {
		return Optional.ofNullable(
			_upgradeStepsMap.get(fromVersionString)
		).map(
			Pair::getRight
		).orElseGet(
			Collections::emptyList
		);
	}

	private final Map<String, Pair<String, List<UpgradeStep>>>
		_upgradeStepsMap = new HashMap<>();

	private static class VersionComparator
		implements Comparator<String>, Serializable {

		@Override
		public int compare(String versionString1, String versionString2) {
			int value = 0;

			int[] versionParts1 = _split(versionString1);
			int[] versionParts2 = _split(versionString2);

			int max = Math.max(versionParts1.length, versionParts2.length);

			for (int i = 0; i < max; i++) {
				int v1 = 0;

				if (i < versionParts1.length) {
					v1 = versionParts1[i];
				}

				int v2 = 0;

				if (i < versionParts2.length) {
					v2 = versionParts2[i];
				}

				value = Integer.compare(v1, v2);

				if (value != 0) {
					return value;
				}
			}

			return value;
		}

		private int[] _split(String versionString) {
			String[] array = StringUtils.split(versionString, ".");

			int[] newArray = new int[array.length];

			for (int i = 0; i < newArray.length; i++) {
				int value = 0;

				if (i < array.length) {
					try {
						value = Integer.parseInt(array[i]);
					}
					catch (Exception exception) {
						if (_log.isWarnEnabled()) {
							_log.warn(
								"Unable to parse " + array[i] + " to integer");
						}
					}
				}

				newArray[i] = value;
			}

			return newArray;
		}

		private static final Log _log = LogFactory.getLog(
			VersionComparator.class);

	}

}