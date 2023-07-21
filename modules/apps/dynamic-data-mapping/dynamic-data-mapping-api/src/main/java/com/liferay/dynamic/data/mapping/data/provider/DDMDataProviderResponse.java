/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.data.provider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Leonardo Barros
 * @author Ethan Bustad
 */
public class DDMDataProviderResponse {

	public static DDMDataProviderResponse error(Status status) {
		return new DDMDataProviderResponse(status, Collections.emptyList());
	}

	public static DDMDataProviderResponse of(
		DDMDataProviderResponseOutput... ddmDataProviderResponseOutputs) {

		return new DDMDataProviderResponse(
			Status.OK, Arrays.asList(ddmDataProviderResponseOutputs));
	}

	public DDMDataProviderResponseOutput get(String name) {
		return _dataMap.get(name);
	}

	public Map<String, DDMDataProviderResponseOutput> getDataMap() {
		return Collections.unmodifiableMap(_dataMap);
	}

	public Status getStatus() {
		return _status;
	}

	public int size() {
		return _dataMap.size();
	}

	public static class Builder {

		public static Builder newBuilder() {
			return new Builder();
		}

		public DDMDataProviderResponse build() {
			return new DDMDataProviderResponse(
				_status, _ddmDataProviderResponseOutputs);
		}

		public Builder withOutput(String name, Object value) {
			_ddmDataProviderResponseOutputs.add(
				DDMDataProviderResponseOutput.of(name, null, value));

			return this;
		}

		public Builder withOutput(String name, String type, Object value) {
			_ddmDataProviderResponseOutputs.add(
				DDMDataProviderResponseOutput.of(name, type, value));

			return this;
		}

		public Builder withStatus(
			DDMDataProviderResponseStatus ddmDataProviderResponseStatus) {

			_status = Status.valueOf(ddmDataProviderResponseStatus);

			return this;
		}

		private Builder() {
		}

		private final List<DDMDataProviderResponseOutput>
			_ddmDataProviderResponseOutputs = new ArrayList<>();
		private Status _status = Status.OK;

	}

	public enum Status {

		OK, SERVICE_UNAVAILABLE, SHORTCIRCUIT, TIMEOUT, UNAUTHORIZED,
		UNKNOWN_ERROR;

		public static Status valueOf(
			DDMDataProviderResponseStatus ddmDataProviderResponseStatus) {

			if (ddmDataProviderResponseStatus ==
					DDMDataProviderResponseStatus.OK) {

				return Status.OK;
			}
			else if (ddmDataProviderResponseStatus ==
						DDMDataProviderResponseStatus.SERVICE_UNAVAILABLE) {

				return Status.SERVICE_UNAVAILABLE;
			}
			else if (ddmDataProviderResponseStatus ==
						DDMDataProviderResponseStatus.SHORT_CIRCUIT) {

				return Status.SHORTCIRCUIT;
			}
			else if (ddmDataProviderResponseStatus ==
						DDMDataProviderResponseStatus.TIMEOUT) {

				return Status.TIMEOUT;
			}
			else if (ddmDataProviderResponseStatus ==
						DDMDataProviderResponseStatus.UNAUTHORIZED) {

				return Status.UNAUTHORIZED;
			}

			return Status.UNKNOWN_ERROR;
		}

	}

	private DDMDataProviderResponse(
		Status status,
		List<DDMDataProviderResponseOutput> ddmDataProviderResponseOutputs) {

		_status = status;

		ddmDataProviderResponseOutputs.forEach(
			ddmDataProviderResponseOutput -> _dataMap.put(
				ddmDataProviderResponseOutput.getName(),
				ddmDataProviderResponseOutput));
	}

	private final Map<String, DDMDataProviderResponseOutput> _dataMap =
		new HashMap<>();
	private final Status _status;

}