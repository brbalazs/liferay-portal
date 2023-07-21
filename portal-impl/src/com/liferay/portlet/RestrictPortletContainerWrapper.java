/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portlet;

import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.portlet.ActionResult;
import com.liferay.portal.kernel.portlet.PortletContainer;
import com.liferay.portal.kernel.portlet.PortletContainerException;
import com.liferay.portal.kernel.portlet.RestrictPortletServletRequest;
import com.liferay.portal.kernel.util.ServerDetector;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.util.PropsValues;

import java.util.List;
import java.util.Map;

import javax.portlet.Event;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Shuyang Zhou
 */
public class RestrictPortletContainerWrapper implements PortletContainer {

	public static PortletContainer createRestrictPortletContainerWrapper(
		PortletContainer portletContainer) {

		if ((PropsValues.LAYOUT_PARALLEL_RENDER_ENABLE &&
			 ServerDetector.isTomcat()) ||
			PropsValues.PORTLET_CONTAINER_RESTRICT) {

			portletContainer = new RestrictPortletContainerWrapper(
				portletContainer);
		}

		return portletContainer;
	}

	public RestrictPortletContainerWrapper(PortletContainer portletContainer) {
		_portletContainer = portletContainer;
	}

	@Override
	public void preparePortlet(HttpServletRequest request, Portlet portlet)
		throws PortletContainerException {

		_portletContainer.preparePortlet(request, portlet);
	}

	@Override
	public ActionResult processAction(
			HttpServletRequest request, HttpServletResponse response,
			Portlet portlet)
		throws PortletContainerException {

		RestrictPortletServletRequest restrictPortletServletRequest =
			new RestrictPortletServletRequest(request);

		try {
			return _portletContainer.processAction(request, response, portlet);
		}
		finally {
			restrictPortletServletRequest.mergeSharedAttributes();
		}
	}

	@Override
	public List<Event> processEvent(
			HttpServletRequest request, HttpServletResponse response,
			Portlet portlet, Layout layout, Event event)
		throws PortletContainerException {

		RestrictPortletServletRequest restrictPortletServletRequest =
			new RestrictPortletServletRequest(request);

		try {
			return _portletContainer.processEvent(
				request, response, portlet, layout, event);
		}
		finally {
			restrictPortletServletRequest.mergeSharedAttributes();
		}
	}

	@Override
	public void processPublicRenderParameters(
		HttpServletRequest request, Layout layout) {

		_portletContainer.processPublicRenderParameters(request, layout);
	}

	@Override
	public void processPublicRenderParameters(
		HttpServletRequest request, Layout layout, Portlet portlet) {

		_portletContainer.processPublicRenderParameters(
			request, layout, portlet);
	}

	@Override
	public void render(
			HttpServletRequest request, HttpServletResponse response,
			Portlet portlet)
		throws PortletContainerException {

		_render(
			request,
			() -> _portletContainer.render(request, response, portlet));
	}

	@Override
	public void renderHeaders(
			HttpServletRequest request, HttpServletResponse response,
			Portlet portlet)
		throws PortletContainerException {

		_render(
			request,
			() -> _portletContainer.renderHeaders(request, response, portlet));
	}

	@Override
	public void serveResource(
			HttpServletRequest request, HttpServletResponse response,
			Portlet portlet)
		throws PortletContainerException {

		RestrictPortletServletRequest restrictPortletServletRequest =
			new RestrictPortletServletRequest(request);

		try {
			_portletContainer.serveResource(request, response, portlet);
		}
		catch (Exception e) {
			throw new PortletContainerException(e);
		}
		finally {
			restrictPortletServletRequest.mergeSharedAttributes();
		}
	}

	private void _render(HttpServletRequest request, Renderable renderable)
		throws PortletContainerException {

		RestrictPortletServletRequest restrictPortletServletRequest = null;

		if (request instanceof RestrictPortletServletRequest) {
			restrictPortletServletRequest =
				(RestrictPortletServletRequest)request;

			Map<String, Object> attributes =
				restrictPortletServletRequest.getAttributes();

			if (attributes.containsKey(WebKeys.RENDER_PORTLET)) {
				restrictPortletServletRequest =
					new RestrictPortletServletRequest(request);
			}
		}
		else {
			restrictPortletServletRequest = new RestrictPortletServletRequest(
				request);
		}

		try {
			renderable.render();
		}
		finally {
			restrictPortletServletRequest.removeAttribute(WebKeys.RENDER_PATH);
			restrictPortletServletRequest.removeAttribute(
				WebKeys.RENDER_PORTLET_COLUMN_COUNT);
			restrictPortletServletRequest.removeAttribute(
				WebKeys.RENDER_PORTLET_COLUMN_ID);
			restrictPortletServletRequest.removeAttribute(
				WebKeys.RENDER_PORTLET_COLUMN_POS);

			// Don't merge when parallel rendering a portlet. The caller (worker
			// thread) should decide whether or not to merge shared attributes.
			// If we did merge here and the caller cancelled the parallel
			// rendering, then we would have corrupted the set of shared
			// attributes. The only safe way to merge shared attributes is for
			// the caller to merge after it has the render result.

			Object lock = request.getAttribute(
				WebKeys.PARALLEL_RENDERING_MERGE_LOCK);

			if (lock == null) {
				restrictPortletServletRequest.mergeSharedAttributes();
			}
		}
	}

	private final PortletContainer _portletContainer;

	@FunctionalInterface
	private interface Renderable {

		public void render() throws PortletContainerException;

	}

}