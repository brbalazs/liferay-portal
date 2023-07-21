/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.document.library.web.internal.display.context;

import com.liferay.document.library.web.internal.util.DLTrashUtil;
import com.liferay.image.gallery.display.kernel.display.context.IGDisplayContextFactory;
import com.liferay.image.gallery.display.kernel.display.context.IGViewFileVersionDisplayContext;
import com.liferay.osgi.service.tracker.collections.list.ServiceTrackerList;
import com.liferay.osgi.service.tracker.collections.list.ServiceTrackerListFactory;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.repository.model.FileShortcut;
import com.liferay.portal.kernel.repository.model.FileVersion;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ResourceBundleLoader;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Iván Zaera
 */
@Component(service = IGDisplayContextProvider.class)
public class IGDisplayContextProvider {

	public IGViewFileVersionDisplayContext
		getIGViewFileVersionActionsDisplayContext(
			HttpServletRequest request, HttpServletResponse response,
			FileShortcut fileShortcut) {

		try {
			ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
				WebKeys.THEME_DISPLAY);

			ResourceBundle resourceBundle =
				_resourceBundleLoader.loadResourceBundle(
					themeDisplay.getLocale());

			IGViewFileVersionDisplayContext igViewFileVersionDisplayContext =
				new DefaultIGViewFileVersionDisplayContext(
					request, response, fileShortcut, resourceBundle,
					_dlTrashUtil);

			if (fileShortcut == null) {
				return igViewFileVersionDisplayContext;
			}

			for (IGDisplayContextFactory igDisplayContextFactory :
					_igDisplayContextFactories) {

				igViewFileVersionDisplayContext =
					igDisplayContextFactory.getIGViewFileVersionDisplayContext(
						igViewFileVersionDisplayContext, request, response,
						fileShortcut);
			}

			return igViewFileVersionDisplayContext;
		}
		catch (PortalException pe) {
			throw new SystemException(pe);
		}
	}

	public IGViewFileVersionDisplayContext
		getIGViewFileVersionActionsDisplayContext(
			HttpServletRequest request, HttpServletResponse response,
			FileVersion fileVersion) {

		try {
			ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
				WebKeys.THEME_DISPLAY);

			ResourceBundle resourceBundle =
				_resourceBundleLoader.loadResourceBundle(
					themeDisplay.getLocale());

			IGViewFileVersionDisplayContext igViewFileVersionDisplayContext =
				new DefaultIGViewFileVersionDisplayContext(
					request, response, fileVersion, resourceBundle,
					_dlTrashUtil);

			if (fileVersion == null) {
				return igViewFileVersionDisplayContext;
			}

			for (IGDisplayContextFactory igDisplayContextFactory :
					_igDisplayContextFactories) {

				igViewFileVersionDisplayContext =
					igDisplayContextFactory.getIGViewFileVersionDisplayContext(
						igViewFileVersionDisplayContext, request, response,
						fileVersion);
			}

			return igViewFileVersionDisplayContext;
		}
		catch (PortalException pe) {
			throw new SystemException(pe);
		}
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_igDisplayContextFactories = ServiceTrackerListFactory.open(
			bundleContext, IGDisplayContextFactory.class);
	}

	@Deactivate
	protected void deactivate(BundleContext bundleContext) {
		_igDisplayContextFactories.close();
	}

	@Reference
	private DLTrashUtil _dlTrashUtil;

	private ServiceTrackerList<IGDisplayContextFactory, IGDisplayContextFactory>
		_igDisplayContextFactories;

	@Reference(
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY,
		target = "(bundle.symbolic.name=com.liferay.document.library.web)"
	)
	private volatile ResourceBundleLoader _resourceBundleLoader;

}