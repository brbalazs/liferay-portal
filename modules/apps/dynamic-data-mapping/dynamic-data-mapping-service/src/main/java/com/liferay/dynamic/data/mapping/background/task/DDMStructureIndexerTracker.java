/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.background.task;

import com.liferay.document.library.kernel.model.DLFileEntryMetadata;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerCustomizerFactory;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerCustomizerFactory.ServiceWrapper;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.DDMStructureIndexer;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistry;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Igor Fabiano Nazar
 * @author Lucas Marques de Paula
 */
@Component(immediate = true, service = DDMStructureIndexerTracker.class)
public class DDMStructureIndexerTracker {

	public DDMStructureIndexer getDDMStructureIndexer(String className)
		throws PortalException {

		if (className.equals(DLFileEntryMetadata.class.getName())) {
			Indexer<?> indexer = _indexerRegistry.nullSafeGetIndexer(className);

			return (DDMStructureIndexer)indexer;
		}

		ServiceWrapper<DDMStructureIndexer> serviceWrapper =
			_serviceTrackerMap.getService(className);

		if (serviceWrapper == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(
					"No dynamic data mapping structure indexer exists for " +
						className);
			}

			return null;
		}

		return serviceWrapper.getService();
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_serviceTrackerMap = ServiceTrackerMapFactory.openSingleValueMap(
			bundleContext, DDMStructureIndexer.class,
			"ddm.structure.indexer.class.name",
			ServiceTrackerCustomizerFactory.serviceWrapper(bundleContext));
	}

	@Deactivate
	protected void deactivate() {
		_serviceTrackerMap.close();
	}

	@Reference(unbind = "-")
	protected void setIndexerRegistry(IndexerRegistry indexerRegistry) {
		_indexerRegistry = indexerRegistry;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DDMStructureIndexerTracker.class);

	private IndexerRegistry _indexerRegistry;
	private ServiceTrackerMap<String, ServiceWrapper<DDMStructureIndexer>>
		_serviceTrackerMap;

}