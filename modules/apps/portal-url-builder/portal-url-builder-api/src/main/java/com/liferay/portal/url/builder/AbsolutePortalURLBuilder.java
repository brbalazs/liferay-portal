/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.url.builder;

import com.liferay.portal.kernel.model.portlet.PortletDependency;

/**
 * Provides a builder for constructing absolute URLs pointing to portal
 * resources.
 *
 * Depending on the kind of resource specified (determined by the forXXX()
 * method called) the algorithm to build the URL may be different. See the
 * documentation of each forXXX() method to get such information.
 *
 * In general, all URLs honor the proxy path when present unless the CDN host is
 * being used.
 *
 * @author Iván Zaera Avellón
 * @see    BuildableAbsolutePortalURLBuilder
 * @review
 */
public interface AbsolutePortalURLBuilder {

	/**
	 * Returns URLs for portal images. Image resources live in {@link
	 * com.liferay.portal.kernel.util.Portal#PATH_IMAGE}.
	 *
	 * Image resources are retrieved from the CDN host when present or from the
	 * Portal otherwise.
	 *
	 * @param  relativeURL the image's relative URL
	 * @return a builder that returns image URLs
	 * @review
	 */
	public ImageAbsolutePortalURLBuilder forImage(String relativeURL);

	/**
	 * Returns URLs for portal's main resources. Main resources live in {@link
	 * com.liferay.portal.kernel.util.Portal#PATH_MAIN}.
	 *
	 * Main resources are retrieved from the CDN host when present or from the
	 * Portal otherwise.
	 *
	 * @param  relativeURL the resource's relative URL
	 * @return a builder that returns main resource URLs
	 * @review
	 */
	public MainAbsolutePortalURLBuilder forMain(String relativeURL);

	/**
	 * Returns URLs for module resources. Module resources live in {@link
	 * com.liferay.portal.kernel.util.Portal#PATH_MODULE} + bundle's web
	 * context path.
	 *
	 * Module resources are retrieved from the CDN host when present or from the
	 * Portal otherwise.
	 *
	 * @param  relativeURL the resource's relative URL
	 * @return a builder that returns module resource URLs
	 * @review
	 */
	public ModuleAbsolutePortalURLBuilder forModule(String relativeURL);

	/**
	 * Returns URLs for portlet dependency resources. Portlet dependency
	 * resources live in the portal's root path.
	 *
	 * Portlet dependency resources are retrieved from the configured CSS or JS
	 * URN (see {@link PropsKeys#PORTLET_DEPENDENCY_CSS_URN} and
	 * {@link PropsKeys#PORTLET_DEPENDENCY_JAVASCRIPT_URN}) when present.
	 *
	 * If not present, the resource will be retrieved from the CDN host when
	 * present or the Portal itself otherwise.
	 *
	 * @param  portletDependency the portlet dependency resource
	 * @param  cssURN the URN for CSS portlet dependency resources
	 * @param  javaScriptURN the URN for JavaScript portlet dependency resources
	 * @return a builder that returns portlet dependency resource URLs
	 * @review
	 */
	public PortletDependencyAbsolutePortalURLBuilder forPortletDependency(
		PortletDependency portletDependency, String cssURN,
		String javaScriptURN);

	/**
	 * Returns URLs for arbitrary resources. Arbitrary resources live in the
	 * portal's root path.
	 *
	 * Arbitrary resources are retrieved from the CDN host when present or from
	 * the Portal otherwise.
	 *
	 * @param  relativeURL the resource's relative URL
	 * @return a builder that returns arbitrary resource URLs
	 */
	public ResourceAbsolutePortalURLBuilder forResource(String relativeURL);

	/**
	 * Returns absolute URLs without the CDN part.
	 *
	 * @return the same builder
	 * @see    com.liferay.portal.kernel.util.Portal#getCDNHost(
	 *         javax.servlet.http.HttpServletRequest)
	 */
	public AbsolutePortalURLBuilder ignoreCDNHost();

	/**
	 * Returns absolute URLs without the proxy part.
	 *
	 * @return the same builder
	 * @see    com.liferay.portal.kernel.util.Portal#getPathProxy()
	 */
	public AbsolutePortalURLBuilder ignorePathProxy();

}