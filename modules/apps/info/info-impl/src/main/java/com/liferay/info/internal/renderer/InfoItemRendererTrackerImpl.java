/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.info.internal.renderer;

import com.liferay.info.internal.util.GenericsUtil;
import com.liferay.info.renderer.InfoItemRenderer;
import com.liferay.info.renderer.InfoItemRendererTracker;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 * @author Jorge Ferrer
 */
@Component(immediate = true, service = InfoItemRendererTracker.class)
public class InfoItemRendererTrackerImpl implements InfoItemRendererTracker {

	@Override
	public InfoItemRenderer getInfoItemRenderer(String key) {
		if (Validator.isNull(key)) {
			return null;
		}

		return _infoItemRenderers.get(key);
	}

	@Override
	public List<InfoItemRenderer> getInfoItemRenderers() {
		return new ArrayList<>(_infoItemRenderers.values());
	}

	@Override
	public List<InfoItemRenderer> getInfoItemRenderers(String itemClassName) {
		List<InfoItemRenderer> infoItemRenderers =
			_itemClassNameInfoItemRenderers.get(itemClassName);

		if (infoItemRenderers != null) {
			return new ArrayList<>(infoItemRenderers);
		}

		return Collections.emptyList();
	}

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC
	)
	protected void setInfoItemRenderer(InfoItemRenderer infoItemRenderer) {
		_infoItemRenderers.put(infoItemRenderer.getKey(), infoItemRenderer);

		List<InfoItemRenderer> itemClassInfoItemRenderers =
			_itemClassNameInfoItemRenderers.computeIfAbsent(
				GenericsUtil.getItemClassName(infoItemRenderer),
				itemClass -> new ArrayList<>());

		itemClassInfoItemRenderers.add(infoItemRenderer);
	}

	protected void unsetInfoItemRenderer(InfoItemRenderer infoItemRenderer) {
		_infoItemRenderers.remove(infoItemRenderer.getKey());

		List<InfoItemRenderer> itemClassInfoItemRenderers =
			_itemClassNameInfoItemRenderers.get(
				GenericsUtil.getItemClass(infoItemRenderer));

		if (itemClassInfoItemRenderers != null) {
			_itemClassNameInfoItemRenderers.remove(infoItemRenderer);
		}
	}

	private final Map<String, InfoItemRenderer> _infoItemRenderers =
		new ConcurrentHashMap<>();
	private final Map<String, List<InfoItemRenderer>>
		_itemClassNameInfoItemRenderers = new ConcurrentHashMap<>();

}