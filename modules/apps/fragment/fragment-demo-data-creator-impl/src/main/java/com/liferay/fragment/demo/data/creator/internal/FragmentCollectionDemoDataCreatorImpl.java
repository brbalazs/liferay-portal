/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.fragment.demo.data.creator.internal;

import com.liferay.fragment.demo.data.creator.FragmentCollectionDemoDataCreator;
import com.liferay.fragment.exception.NoSuchCollectionException;
import com.liferay.fragment.model.FragmentCollection;
import com.liferay.fragment.service.FragmentCollectionLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.ServiceContext;

import java.io.IOException;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Jürgen Kappler
 */
@Component(service = FragmentCollectionDemoDataCreator.class)
public class FragmentCollectionDemoDataCreatorImpl
	implements FragmentCollectionDemoDataCreator {

	@Override
	public FragmentCollection create(long userId, long groupId, String name)
		throws IOException, PortalException {

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setScopeGroupId(groupId);

		FragmentCollection fragmentCollection =
			_fragmentCollectionLocalService.addFragmentCollection(
				userId, groupId, name, null, serviceContext);

		_fragmentCollectionIds.add(
			fragmentCollection.getFragmentCollectionId());

		return fragmentCollection;
	}

	@Override
	public void delete() throws PortalException {
		for (long fragmentCollectionId : _fragmentCollectionIds) {
			try {
				_fragmentCollectionLocalService.deleteFragmentCollection(
					fragmentCollectionId);
			}
			catch (NoSuchCollectionException nsce) {
				if (_log.isWarnEnabled()) {
					_log.warn(nsce, nsce);
				}
			}

			_fragmentCollectionIds.remove(fragmentCollectionId);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		FragmentCollectionDemoDataCreatorImpl.class);

	private final List<Long> _fragmentCollectionIds =
		new CopyOnWriteArrayList<>();

	@Reference
	private FragmentCollectionLocalService _fragmentCollectionLocalService;

}