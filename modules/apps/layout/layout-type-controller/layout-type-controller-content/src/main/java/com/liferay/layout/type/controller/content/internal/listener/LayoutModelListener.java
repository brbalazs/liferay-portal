/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.layout.type.controller.content.internal.listener;

import com.liferay.fragment.model.FragmentEntryLink;
import com.liferay.fragment.service.FragmentEntryLinkLocalService;
import com.liferay.layout.page.template.model.LayoutPageTemplateEntry;
import com.liferay.layout.type.controller.content.internal.constants.ContentLayoutTypeControllerConstants;
import com.liferay.portal.kernel.exception.ModelListenerException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.BaseModelListener;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.ModelListener;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.UnicodeProperties;

import java.util.List;
import java.util.Objects;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Jürgen Kappler
 */
@Component(service = ModelListener.class)
public class LayoutModelListener extends BaseModelListener<Layout> {

	@Override
	public void onAfterCreate(Layout layout) throws ModelListenerException {
		if (!_isContentLayout(layout)) {
			return;
		}

		UnicodeProperties typeSettingsProperties =
			layout.getTypeSettingsProperties();

		long layoutPageTemplateEntryId = GetterUtil.getLong(
			typeSettingsProperties.get("layoutPageTemplateEntryId"));

		List<FragmentEntryLink> fragmentEntryLinks =
			_fragmentEntryLinkLocalService.getFragmentEntryLinks(
				layout.getGroupId(),
				_portal.getClassNameId(LayoutPageTemplateEntry.class.getName()),
				layoutPageTemplateEntryId);

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		try {
			for (FragmentEntryLink fragmentEntryLink : fragmentEntryLinks) {
				_fragmentEntryLinkLocalService.addFragmentEntryLink(
					fragmentEntryLink.getUserId(),
					fragmentEntryLink.getGroupId(),
					fragmentEntryLink.getFragmentEntryLinkId(),
					fragmentEntryLink.getFragmentEntryId(),
					_portal.getClassNameId(Layout.class.getName()),
					layout.getPlid(), fragmentEntryLink.getCss(),
					fragmentEntryLink.getHtml(), fragmentEntryLink.getJs(),
					fragmentEntryLink.getEditableValues(),
					fragmentEntryLink.getPosition(), serviceContext);
			}
		}
		catch (PortalException pe) {
			throw new ModelListenerException(pe);
		}

		_reindexLayout(layout);
	}

	@Override
	public void onAfterUpdate(Layout layout) throws ModelListenerException {
		if (!_isContentLayout(layout)) {
			return;
		}

		_reindexLayout(layout);
	}

	@Override
	public void onBeforeRemove(Layout layout) throws ModelListenerException {
		if (!_isContentLayout(layout)) {
			return;
		}

		_fragmentEntryLinkLocalService.
			deleteLayoutPageTemplateEntryFragmentEntryLinks(
				layout.getGroupId(),
				_portal.getClassNameId(Layout.class.getName()),
				layout.getPlid());

		try {
			Indexer indexer = IndexerRegistryUtil.getIndexer(Layout.class);

			indexer.delete(layout);
		}
		catch (PortalException pe) {
			if (_log.isDebugEnabled()) {
				_log.debug(pe, pe);
			}

			throw new ModelListenerException(pe);
		}
	}

	private boolean _isContentLayout(Layout layout) {
		if (Objects.equals(
				layout.getType(),
				ContentLayoutTypeControllerConstants.LAYOUT_TYPE_CONTENT)) {

			return true;
		}

		return false;
	}

	private void _reindexLayout(Layout layout) {
		Indexer indexer = IndexerRegistryUtil.getIndexer(Layout.class);

		try {
			indexer.reindex(layout);
		}
		catch (SearchException se) {
			if (_log.isDebugEnabled()) {
				_log.debug("Unable to reindex layout " + layout.getPlid(), se);
			}

			throw new ModelListenerException(se);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		LayoutModelListener.class);

	@Reference
	private FragmentEntryLinkLocalService _fragmentEntryLinkLocalService;

	@Reference
	private Portal _portal;

}