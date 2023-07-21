/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portlet.internal;

import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.portlet.LiferayPortletURL;
import com.liferay.portal.kernel.portlet.PortletURLFactory;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.service.PortletLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.MimeResponse;
import javax.portlet.PortletRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Brian Wing Shun Chan
 * @author Neil Griffin
 */
public class PortletURLFactoryImpl implements PortletURLFactory {

	@Override
	public LiferayPortletURL create(
		HttpServletRequest request, Portlet portlet, Layout layout,
		String lifecycle) {

		return _create(request, portlet, layout, lifecycle, null);
	}

	@Override
	public LiferayPortletURL create(
		HttpServletRequest request, Portlet portlet, Layout layout,
		String lifecycle, MimeResponse.Copy copy) {

		return _create(request, portlet, layout, lifecycle, copy);
	}

	@Override
	public LiferayPortletURL create(
		HttpServletRequest request, Portlet portlet, String lifecycle) {

		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		Layout layout = themeDisplay.getLayout();

		if (layout == null) {
			layout = _getLayout(
				(Layout)request.getAttribute(WebKeys.LAYOUT),
				themeDisplay.getPlid());
		}

		return _create(request, portlet, layout, lifecycle, null);
	}

	@Override
	public LiferayPortletURL create(
		HttpServletRequest request, String portletId, Layout layout,
		String lifecycle) {

		return _create(
			request,
			PortletLocalServiceUtil.getPortletById(
				PortalUtil.getCompanyId(request), portletId),
			layout, lifecycle, null);
	}

	@Override
	public LiferayPortletURL create(
		HttpServletRequest request, String portletId, long plid,
		String lifecycle) {

		return _create(
			request,
			PortletLocalServiceUtil.getPortletById(
				PortalUtil.getCompanyId(request), portletId),
			_getLayout((Layout)request.getAttribute(WebKeys.LAYOUT), plid),
			lifecycle, null);
	}

	@Override
	public LiferayPortletURL create(
		HttpServletRequest request, String portletId, String lifecycle) {

		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		Layout layout = themeDisplay.getLayout();

		if (layout == null) {
			layout = _getLayout(
				(Layout)request.getAttribute(WebKeys.LAYOUT),
				themeDisplay.getPlid());
		}

		return _create(
			request,
			PortletLocalServiceUtil.getPortletById(
				PortalUtil.getCompanyId(request), portletId),
			layout, lifecycle, null);
	}

	@Override
	public LiferayPortletURL create(
		PortletRequest portletRequest, Portlet portlet, Layout layout,
		String lifecycle) {

		return _create(portletRequest, portlet, layout, lifecycle, null);
	}

	@Override
	public LiferayPortletURL create(
		PortletRequest portletRequest, Portlet portlet, long plid,
		String lifecycle) {

		return _create(
			portletRequest, portlet,
			_getLayout(
				(Layout)portletRequest.getAttribute(WebKeys.LAYOUT), plid),
			lifecycle, null);
	}

	@Override
	public LiferayPortletURL create(
		PortletRequest portletRequest, Portlet portlet, long plid,
		String lifecycle, MimeResponse.Copy copy) {

		return _create(
			portletRequest, portlet,
			_getLayout(
				(Layout)portletRequest.getAttribute(WebKeys.LAYOUT), plid),
			lifecycle, copy);
	}

	@Override
	public LiferayPortletURL create(
		PortletRequest portletRequest, String portletId, Layout layout,
		String lifecycle) {

		return _create(
			portletRequest,
			PortletLocalServiceUtil.getPortletById(
				PortalUtil.getCompanyId(portletRequest), portletId),
			layout, lifecycle, null);
	}

	@Override
	public LiferayPortletURL create(
		PortletRequest portletRequest, String portletId, long plid,
		String lifecycle) {

		return _create(
			portletRequest, portletId,
			_getLayout(
				(Layout)portletRequest.getAttribute(WebKeys.LAYOUT), plid),
			lifecycle, null);
	}

	@Override
	public LiferayPortletURL create(
		PortletRequest portletRequest, String portletId, long plid,
		String lifecycle, MimeResponse.Copy copy) {

		return _create(
			portletRequest, portletId,
			_getLayout(
				(Layout)portletRequest.getAttribute(WebKeys.LAYOUT), plid),
			lifecycle, copy);
	}

	@Override
	public LiferayPortletURL create(
		PortletRequest portletRequest, String portletId, String lifecycle) {

		ThemeDisplay themeDisplay = (ThemeDisplay)portletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		Layout layout = themeDisplay.getLayout();

		if (layout == null) {
			layout = _getLayout(
				(Layout)portletRequest.getAttribute(WebKeys.LAYOUT),
				themeDisplay.getPlid());
		}

		return _create(portletRequest, portletId, layout, lifecycle, null);
	}

	private LiferayPortletURL _create(
		HttpServletRequest request, Portlet portlet, Layout layout,
		String lifecycle, MimeResponse.Copy copy) {

		if (PortletRequest.ACTION_PHASE.equals(lifecycle)) {
			return new ActionURLImpl(request, portlet, layout, lifecycle, copy);
		}
		else if (PortletRequest.RENDER_PHASE.equals(lifecycle)) {
			return new RenderURLImpl(request, portlet, layout, lifecycle, copy);
		}

		return new PortletURLImpl(request, portlet, layout, lifecycle, copy);
	}

	private LiferayPortletURL _create(
		PortletRequest portletRequest, Portlet portlet, Layout layout,
		String lifecycle, MimeResponse.Copy copy) {

		if (PortletRequest.ACTION_PHASE.equals(lifecycle)) {
			return new ActionURLImpl(
				portletRequest, portlet, layout, lifecycle, copy);
		}
		else if (PortletRequest.RENDER_PHASE.equals(lifecycle)) {
			return new RenderURLImpl(
				portletRequest, portlet, layout, lifecycle, copy);
		}

		return new PortletURLImpl(
			portletRequest, portlet, layout, lifecycle, copy);
	}

	private LiferayPortletURL _create(
		PortletRequest portletRequest, String portletId, Layout layout,
		String lifecycle, MimeResponse.Copy copy) {

		return _create(
			portletRequest,
			PortletLocalServiceUtil.getPortletById(
				PortalUtil.getCompanyId(portletRequest), portletId),
			layout, lifecycle, copy);
	}

	private Layout _getLayout(Layout layout, long plid) {
		if ((layout != null) && (layout.getPlid() == plid)) {
			return layout;
		}

		if (plid > 0) {
			return LayoutLocalServiceUtil.fetchLayout(plid);
		}

		return null;
	}

}