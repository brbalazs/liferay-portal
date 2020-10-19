/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.portal.search.elasticsearch.cross.cluster.replication.internal.configuration;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.search.configuration.ElasticsearchConnectionConfigurationWrapper;

import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Bryan Engler
 */
@Component(
	configurationPid = "com.liferay.portal.search.elasticsearch.cross.cluster.replication.internal.configuration.ElasticsearchConnectionConfiguration",
	service = {}
)
public class ElasticsearchConnectionConfigurationWrapperHelper {

	@Activate
	protected void activate(Map<String, Object> properties) {
		ElasticsearchConnectionConfiguration
			elasticsearchConnectionConfiguration =
				ConfigurableUtil.createConfigurable(
					ElasticsearchConnectionConfiguration.class, properties);

		ElasticsearchConnectionConfigurationWrapperImpl
			elasticsearchConnectionConfigurationWrapperImpl =
				(ElasticsearchConnectionConfigurationWrapperImpl)
					elasticsearchConnectionConfigurationWrapper;

		elasticsearchConnectionConfigurationWrapperImpl.
			addElasticsearchConnectionConfiguration(
				elasticsearchConnectionConfiguration);
	}

	@Deactivate
	protected void deactivate(Map<String, Object> properties) {
		ElasticsearchConnectionConfiguration
			elasticsearchConnectionConfiguration =
				ConfigurableUtil.createConfigurable(
					ElasticsearchConnectionConfiguration.class, properties);

		ElasticsearchConnectionConfigurationWrapperImpl
			elasticsearchConnectionConfigurationWrapperImpl =
				(ElasticsearchConnectionConfigurationWrapperImpl)
					elasticsearchConnectionConfigurationWrapper;

		elasticsearchConnectionConfigurationWrapperImpl.
			removeElasticsearchConnectionConfiguration(
				elasticsearchConnectionConfiguration.connectionId());
	}

	@Reference
	protected ElasticsearchConnectionConfigurationWrapper
		elasticsearchConnectionConfigurationWrapper;

}