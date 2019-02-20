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

package com.liferay.commerce.data.integration.manager.internal.process.type;

import com.liferay.commerce.data.integration.manager.process.type.ProcessTypeJSPContributor;
import com.liferay.commerce.data.integration.manager.process.type.ProcessTypeJSPContributorRegistry;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * @author Riccardo Ferrari
 */
@Component(immediate = true, service = ProcessTypeJSPContributorRegistry.class)
public class ProcessTypeJSPContributorRegistryImpl
	implements ProcessTypeJSPContributorRegistry {

	@Override
	public ProcessTypeJSPContributor getProcessTypeJSPContributor(String key) {
		ProcessTypeJSPContributor service = _serviceTrackerMap.getService(key);

		if (_log.isDebugEnabled()) {
			if (service != null) {
				_log.debug("Returning JSPContributor reference for: " + key);
			}
			else {
				_log.debug("No JSPContributor for: " + key);
			}
		}

		return _serviceTrackerMap.getService(key);
	}

	@Override
	public List<ProcessTypeJSPContributor> getProcessTypeJSPContributors() {
		List<ProcessTypeJSPContributor> processTypeJSPContributors =
			new ArrayList<>();

		for (String key : _serviceTrackerMap.keySet()) {
			processTypeJSPContributors.add(_serviceTrackerMap.getService(key));
		}

		return Collections.unmodifiableList(processTypeJSPContributors);
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_serviceTrackerMap = ServiceTrackerMapFactory.openSingleValueMap(
			bundleContext, ProcessTypeJSPContributor.class,
			"commerce.data.integration.process.type.key");
	}

	@Deactivate
	protected void deactivate() {
		_serviceTrackerMap.close();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ProcessTypeJSPContributorRegistryImpl.class);

	private ServiceTrackerMap<String, ProcessTypeJSPContributor>
		_serviceTrackerMap;

}