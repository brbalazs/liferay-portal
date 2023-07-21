/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.fragment.internal.processor;

import com.liferay.fragment.constants.FragmentEntryLinkConstants;
import com.liferay.fragment.model.FragmentEntryLink;
import com.liferay.fragment.processor.FragmentEntryProcessor;
import com.liferay.fragment.processor.FragmentEntryProcessorRegistry;
import com.liferay.osgi.service.tracker.collections.list.ServiceTrackerList;
import com.liferay.osgi.service.tracker.collections.list.ServiceTrackerListFactory;
import com.liferay.osgi.service.tracker.collections.map.PropertyServiceReferenceComparator;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;

import java.util.Collections;
import java.util.Locale;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Pavel Savinov
 */
@Component(immediate = true, service = FragmentEntryProcessorRegistry.class)
public class FragmentEntryProcessorRegistryImpl
	implements FragmentEntryProcessorRegistry {

	@Override
	public JSONObject getDefaultEditableValuesJSONObject(String html) {
		JSONObject jsonObject = _jsonFactory.createJSONObject();

		for (FragmentEntryProcessor fragmentEntryProcessor :
				_serviceTrackerList) {

			JSONObject defaultEditableValuesJSONObject =
				fragmentEntryProcessor.getDefaultEditableValuesJSONObject(html);

			if (defaultEditableValuesJSONObject != null) {
				Class<?> clazz = fragmentEntryProcessor.getClass();

				jsonObject.put(
					clazz.getName(), defaultEditableValuesJSONObject);
			}
		}

		return jsonObject;
	}

	@Override
	public String processFragmentEntryLinkHTML(
			FragmentEntryLink fragmentEntryLink)
		throws PortalException {

		return processFragmentEntryLinkHTML(
			fragmentEntryLink, FragmentEntryLinkConstants.EDIT);
	}

	@Override
	public String processFragmentEntryLinkHTML(
			FragmentEntryLink fragmentEntryLink, String mode, Locale locale)
		throws PortalException {

		String html = fragmentEntryLink.getHtml();

		for (FragmentEntryProcessor fragmentEntryProcessor :
				_serviceTrackerList) {

			html = fragmentEntryProcessor.processFragmentEntryLinkHTML(
				fragmentEntryLink, html, mode, locale);
		}

		return html;
	}

	@Override
	public void validateFragmentEntryHTML(String html) throws PortalException {
		for (FragmentEntryProcessor fragmentEntryProcessor :
				_serviceTrackerList) {

			fragmentEntryProcessor.validateFragmentEntryHTML(html);
		}
	}

	@Activate
	protected void activate(final BundleContext bundleContext) {
		_serviceTrackerList = ServiceTrackerListFactory.open(
			bundleContext, FragmentEntryProcessor.class,
			Collections.reverseOrder(
				new PropertyServiceReferenceComparator(
					"fragment.entry.processor.priority")));
	}

	@Deactivate
	protected void deactivate() {
		_serviceTrackerList.close();
	}

	@Reference
	private JSONFactory _jsonFactory;

	private ServiceTrackerList<FragmentEntryProcessor, FragmentEntryProcessor>
		_serviceTrackerList;

}