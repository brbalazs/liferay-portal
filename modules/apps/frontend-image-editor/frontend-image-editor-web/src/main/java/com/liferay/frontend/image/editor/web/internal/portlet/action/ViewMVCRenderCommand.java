/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.frontend.image.editor.web.internal.portlet.action;

import com.liferay.frontend.image.editor.capability.ImageEditorCapability;
import com.liferay.frontend.image.editor.web.internal.constants.ImageEditorPortletKeys;
import com.liferay.frontend.image.editor.web.internal.portlet.tracker.ImageEditorCapabilityTracker;
import com.liferay.frontend.image.editor.web.internal.portlet.tracker.ImageEditorCapabilityTracker.ImageEditorCapabilityDescriptor;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Bruno Basto
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + ImageEditorPortletKeys.IMAGE_EDITOR,
		"mvc.command.name=/", "mvc.command.name=/image_editor/view"
	},
	service = MVCRenderCommand.class
)
public class ViewMVCRenderCommand implements MVCRenderCommand {

	@Override
	public String render(
		RenderRequest renderRequest, RenderResponse renderResponse) {

		Template template = getTemplate(renderRequest);

		Map<String, Object> imageEditorCapabilitiesContext = new HashMap<>();

		imageEditorCapabilitiesContext.put(
			"tools", getImageEditorToolsContexts(renderRequest));

		template.put("imageEditorCapabilities", imageEditorCapabilitiesContext);

		String entityURL = ParamUtil.getString(renderRequest, "entityURL");

		template.put("image", entityURL);

		ThemeDisplay themeDisplay = (ThemeDisplay)renderRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		template.put("pathThemeImages", themeDisplay.getPathThemeImages());

		String eventName = ParamUtil.getString(renderRequest, "eventName");

		template.put("saveEventName", eventName);

		String saveFileName = ParamUtil.getString(
			renderRequest, "saveFileName");

		template.put("saveFileName", saveFileName);

		String saveMimeType = ParamUtil.getString(
			renderRequest, "saveMimeType");

		template.put("saveMimeType", saveMimeType);

		String saveParamName = ParamUtil.getString(
			renderRequest, "saveParamName");

		template.put("saveParamName", saveParamName);

		String saveURL = ParamUtil.getString(renderRequest, "saveURL");

		template.put("saveURL", saveURL);

		return "ImageEditor";
	}

	protected List<List<ImageEditorCapabilityDescriptor>>
		getImageEditorCapabilityDescriptorsList(
			List<ImageEditorCapabilityDescriptor>
				imageEditorCapabilityDescriptors) {

		Map<String, List<ImageEditorCapabilityDescriptor>>
			imageEditorCapabilityDescriptorsMap = new HashMap<>();

		for (ImageEditorCapabilityDescriptor imageEditorCapabilityDescriptor :
				imageEditorCapabilityDescriptors) {

			Map<String, Object> properties =
				imageEditorCapabilityDescriptor.getProperties();

			String category = GetterUtil.getString(
				properties.get(
					"com.liferay.frontend.image.editor.capability.category"));

			if (!imageEditorCapabilityDescriptorsMap.containsKey(category)) {
				imageEditorCapabilityDescriptorsMap.put(
					category, new ArrayList<ImageEditorCapabilityDescriptor>());
			}

			List<ImageEditorCapabilityDescriptor>
				curImageEditorCapabilityDescriptors =
					imageEditorCapabilityDescriptorsMap.get(category);

			curImageEditorCapabilityDescriptors.add(
				imageEditorCapabilityDescriptor);
		}

		return new ArrayList<>(imageEditorCapabilityDescriptorsMap.values());
	}

	protected List<Map<String, Object>> getImageEditorToolsContexts(
		RenderRequest renderRequest) {

		List<Map<String, Object>> imageEditorToolsContexts = new ArrayList<>();

		List<ImageEditorCapabilityDescriptor>
			toolImageEditorCapabilityDescriptors =
				_imageEditorCapabilityTracker.
					getImageEditorCapabilityDescriptors("tool");

		if (toolImageEditorCapabilityDescriptors == null) {
			return imageEditorToolsContexts;
		}

		ThemeDisplay themeDisplay = (ThemeDisplay)renderRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		List<List<ImageEditorCapabilityDescriptor>>
			imageEditorCapabilityDescriptorsList =
				getImageEditorCapabilityDescriptorsList(
					toolImageEditorCapabilityDescriptors);

		for (List<ImageEditorCapabilityDescriptor>
				imageEditorCapabilityDescriptors :
					imageEditorCapabilityDescriptorsList) {

			Map<String, Object> context = new HashMap<>();

			List<Map<String, Object>> controlContexts = new ArrayList<>();
			String icon = StringPool.BLANK;

			for (ImageEditorCapabilityDescriptor
					imageEditorCapabilityDescriptor :
						imageEditorCapabilityDescriptors) {

				Map<String, Object> controlContext = new HashMap<>();

				ImageEditorCapability imageEditorCapability =
					imageEditorCapabilityDescriptor.getImageEditorCapability();

				controlContext.put(
					"label",
					imageEditorCapability.getLabel(themeDisplay.getLocale()));

				ServletContext servletContext =
					imageEditorCapability.getServletContext();

				controlContext.put(
					"modulePath", servletContext.getContextPath());

				Map<String, Object> properties =
					imageEditorCapabilityDescriptor.getProperties();

				String variant = GetterUtil.getString(
					properties.get(
						"com.liferay.frontend.image.editor.capability." +
							"controls"));

				controlContext.put("variant", variant);

				HttpServletRequest request = _portal.getHttpServletRequest(
					renderRequest);

				imageEditorCapability.prepareContext(controlContext, request);

				controlContexts.add(controlContext);

				icon = GetterUtil.getString(
					properties.get(
						"com.liferay.frontend.image.editor.capability.icon"));
			}

			context.put("controls", controlContexts);
			context.put("icon", icon);

			imageEditorToolsContexts.add(context);
		}

		return imageEditorToolsContexts;
	}

	protected Template getTemplate(RenderRequest renderRequest) {
		return (Template)renderRequest.getAttribute(WebKeys.TEMPLATE);
	}

	@Reference
	private ImageEditorCapabilityTracker _imageEditorCapabilityTracker;

	@Reference
	private Portal _portal;

}