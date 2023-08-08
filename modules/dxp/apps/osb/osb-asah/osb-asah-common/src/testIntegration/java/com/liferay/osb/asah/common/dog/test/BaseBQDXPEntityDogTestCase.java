/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog.test;

import com.liferay.osb.asah.common.OSBAsahCommonSpringTestContext;
import com.liferay.osb.asah.common.entity.Channel;
import com.liferay.osb.asah.common.entity.ChannelDataSource;
import com.liferay.osb.asah.common.entity.DataSource;
import com.liferay.osb.asah.common.repository.ChannelRepository;
import com.liferay.osb.asah.common.repository.DataSourceRepository;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Marcos Martins
 */
public abstract class BaseBQDXPEntityDogTestCase
	implements OSBAsahCommonSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	public void setUp() {
		Channel channel = new Channel("channel");

		channel.setId(11L);
		channel.setIsNew(Boolean.TRUE);

		dataSource = new DataSource("Liferay Brazil");

		dataSource.setCredentialType("Token Authentication");
		dataSource.setFaroBackendSecuritySignature(
			"faroBackendSecuritySignature");
		dataSource.setId(123L);
		dataSource.setIsNew(Boolean.TRUE);
		dataSource.setProviderType("LIFERAY");
		dataSource.setState("READY");
		dataSource.setStatus("STARTED");
		dataSource.setURL("");

		dataSource = _dataSourceRepository.save(dataSource);

		channel.addChannelDataSource(
			new ChannelDataSource(null, dataSource.getId(), null));

		_channelRepository.save(channel);
	}

	protected DataSource dataSource;

	@Autowired
	private ChannelRepository _channelRepository;

	@Autowired
	private DataSourceRepository _dataSourceRepository;

}