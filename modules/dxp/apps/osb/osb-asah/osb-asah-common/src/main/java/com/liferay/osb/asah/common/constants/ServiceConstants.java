/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.constants;

import com.liferay.osb.asah.common.util.SetUtil;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Brian Wing Shun Chan
 */
public class ServiceConstants {

	/**
	 * @deprecated As of 2.10.0, replaced by {@link ProjectThreadLocal#getProjectId()}}
	 */
	@Deprecated
	public static final String LCP_PROJECT_ID = System.getenv("LCP_PROJECT_ID");

	public static final String POSTGRESQL_SERVER_IP;

	public static final String URL_BACKEND;

	public static final String URL_BACKEND_INTERNAL;

	public static final String URL_BATCH_CURATOR;

	public static final String URL_BIG_QUERY;

	public static final String URL_EXTRACTOR;

	public static final String URL_FRONTEND;

	public static final String URL_PUBLISHER;

	public static final String URL_PUBSUB_EMULATOR;

	public static final String URL_REDIS;

	public static final String URL_STREAM_CURATOR;

	public static final Set<String> blockedAnonymousEventProjectIds;

	public static boolean isInternalServiceURL(String url) {
		return _internalServiceURLs.contains(url);
	}

	private static String _doGetURL(
		String serviceName, String port, boolean external) {

		serviceName = serviceName.replace("_", "");
		serviceName = serviceName.toLowerCase();

		String internalServiceURL = _setInternalURL(serviceName, port);

		if (external && (LCP_PROJECT_ID != null) &&
			(_LCP_SERVICE_DOMAIN != null)) {

			return "https://osbasah" + serviceName + "-" + LCP_PROJECT_ID +
				"." + _LCP_SERVICE_DOMAIN;
		}

		if (StringUtils.isNotEmpty(_DOMAIN_SUFFIX)) {
			return "https://osbasah" + serviceName + "-" + _DOMAIN_SUFFIX;
		}

		return internalServiceURL;
	}

	private static String _getBigQueryURL() {
		String bigQueryURL = System.getenv("OSB_ASAH_BIG_QUERY_URL");

		if (StringUtils.isNotEmpty(bigQueryURL)) {
			return bigQueryURL;
		}

		return "http://localhost:9050";
	}

	private static Set<String> _getBlockedAnonymousEventProjectIds() {
		String blockedAnonymousEventProjectIds = System.getenv(
			"OSB_ASAH_BLOCKED_ANONYMOUS_EVENT_PROJECT_IDS");

		if (_log.isInfoEnabled()) {
			_log.info(
				"Blocked Anonymous Event Project IDs: " +
					blockedAnonymousEventProjectIds);
		}

		if (blockedAnonymousEventProjectIds == null) {
			return Collections.emptySet();
		}

		return SetUtil.of(
			StringUtils.split(blockedAnonymousEventProjectIds, ","));
	}

	private static String _getPostgreSQLClusterURL() {
		String postgresServerIp = System.getenv("POSTGRESQL_SERVER_IP");

		if (StringUtils.isNotEmpty(postgresServerIp)) {
			return postgresServerIp;
		}

		return _LOCALHOST_IP;
	}

	private static String _getURL(
		String serviceName, String port, boolean external) {

		String url = _doGetURL(serviceName, port, external);

		String osbAsahURLKey = "OSB_ASAH_" + serviceName + "_URL";

		String overrideURL = System.getenv(osbAsahURLKey);

		if (overrideURL != null) {
			url = overrideURL;
		}

		if (_log.isInfoEnabled()) {
			_log.info(osbAsahURLKey + ": " + url);
		}

		return url;
	}

	private static String _setInternalURL(String serviceName, String port) {
		serviceName = serviceName.replace("_", "");
		serviceName = serviceName.toLowerCase();

		String internalServiceURL = "http://osbasah" + serviceName + ":" + port;

		_internalServiceURLs.add(internalServiceURL);

		return internalServiceURL;
	}

	private static final String _DOMAIN_SUFFIX = System.getenv(
		"OSB_ASAH_LCP_DOMAIN_SUFFIX");

	private static final String _LCP_SERVICE_DOMAIN = System.getenv(
		"LCP_SERVICE_DOMAIN");

	@SuppressWarnings("PMD.AvoidUsingHardCodedIP")
	private static final String _LOCALHOST_IP = "127.0.0.1";

	private static final Log _log = LogFactory.getLog(ServiceConstants.class);

	private static final Set<String> _internalServiceURLs;

	static {
		_internalServiceURLs = new HashSet<>();

		if (_log.isInfoEnabled()) {
			_log.info("LCP_PROJECT_ID: " + LCP_PROJECT_ID);
		}

		if (_log.isInfoEnabled()) {
			_log.info("OSB_ASAH_LCP_DOMAIN_SUFFIX: " + _DOMAIN_SUFFIX);
		}

		blockedAnonymousEventProjectIds = _getBlockedAnonymousEventProjectIds();

		POSTGRESQL_SERVER_IP = _getPostgreSQLClusterURL();

		URL_BACKEND = _getURL("BACKEND", "8080", true);
		URL_BACKEND_INTERNAL = _setInternalURL("BACKEND", "8080");
		URL_BATCH_CURATOR = _getURL("BATCH_CURATOR", "8080", false);
		URL_BIG_QUERY = _getBigQueryURL();
		URL_EXTRACTOR = _getURL("EXTRACTOR", "8080", false);
		URL_FRONTEND = System.getenv("OSB_FARO_FRONTEND_URL");
		URL_PUBLISHER = _getURL("PUBLISHER", "8080", true);
		URL_PUBSUB_EMULATOR = _getURL("PUBSUB_EMULATOR", "8095", false);
		URL_REDIS = _getURL("REDIS", "6379", false);
		URL_STREAM_CURATOR = _getURL("STREAM_CURATOR", "8080", false);
	}

}