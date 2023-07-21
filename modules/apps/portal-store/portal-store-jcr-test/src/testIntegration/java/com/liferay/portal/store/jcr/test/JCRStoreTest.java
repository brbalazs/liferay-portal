/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.store.jcr.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.document.library.kernel.store.Store;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.test.util.ConfigurationTestUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portlet.documentlibrary.store.test.BaseStoreTestCase;

import java.util.Dictionary;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.runner.RunWith;

import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

/**
 * @author Preston Crary
 * @author Manuel de la Peña
 */
@RunWith(Arquillian.class)
public class JCRStoreTest extends BaseStoreTestCase {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@BeforeClass
	public static void setUpClass() throws Exception {
		_configuration = _configurationAdmin.getConfiguration(
			"com.liferay.portal.store.jcr.configuration.JCRStoreConfiguration",
			StringPool.QUESTION);

		Dictionary<String, Object> properties = new HashMapDictionary<>();

		properties.put("initializeOnStartup", Boolean.TRUE);
		properties.put("jackrabbitConfigFilePath", "repository.xml");
		properties.put("jackrabbitCredentialsPassword", "none");
		properties.put("jackrabbitCredentialsUsername", "none");
		properties.put("jackrabbitRepositoryHome", "home");
		properties.put("jackrabbitRepositoryRoot", "data/jackrabbit");
		properties.put("moveVersionLabels", Boolean.FALSE);
		properties.put("nodeDocumentlibrary", "documentlibrary");
		properties.put("workspaceName", "liferay");
		properties.put("wrapSession", Boolean.TRUE);

		ConfigurationTestUtil.saveConfiguration(_configuration, properties);
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		ConfigurationTestUtil.deleteConfiguration(_configuration);
	}

	@Override
	protected Store getStore() {
		return _store;
	}

	private static Configuration _configuration;

	@Inject
	private static ConfigurationAdmin _configurationAdmin;

	@Inject(
		filter = "store.type=com.liferay.portal.store.jcr.JCRStore",
		type = Store.class
	)
	private Store _store;

}