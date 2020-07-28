/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.search.configuration;

import org.osgi.annotation.versioning.ProviderType;

/**
 * @author Bryan Engler
 */
@ProviderType
public interface ElasticsearchConnectionConfigurationWrapper {

	public String getCertificateFormat(String connectionId);

	public String getClientTransportNodesSamplerInterval(String connectionId);

	public String getClientTransportPingTimeout(String connectionId);

	public String getClusterName(String connectionId);

	public String getNetworkHostAddress(String connectionId);

	public String getPassword(String connectionId);

	public String[] getSslCertificateAuthoritiesPaths(String connectionId);

	public String getSslCertificatePath(String connectionId);

	public String getSslKeyPath(String connectionId);

	public String getSslKeystorePassword(String connectionId);

	public String getSslKeystorePath(String connectionId);

	public String getSslTruststorePassword(String connectionId);

	public String getSslTruststorePath(String connectionId);

	public String[] getTransportAddresses(String connectionId);

	public String getTransportSSLVerificationMode(String connectionId);

	public String getUsername(String connectionId);

	public boolean isAuthenticationEnabled(String connectionId);

	public boolean isClientTransportIgnoreClusterName(String connectionId);

	public boolean isClientTransportSniff(String connectionId);

	public boolean isTransportSSLEnabled(String connectionId);

}