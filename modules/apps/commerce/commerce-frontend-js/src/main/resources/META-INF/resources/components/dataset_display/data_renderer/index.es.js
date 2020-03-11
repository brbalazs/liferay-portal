/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

import {getJsModule} from '../../../utilities/index.es';
import ActionsLinkRenderer from './ActionLinkRenderer.es';
import ActionsDropdownRenderer from './ActionsDropdownRenderer.es';
import CheckboxRenderer from './CheckboxRenderer.es';
import DateRenderer from './DateRenderer.es';
import DefaultRenderer from './DefaultRenderer.es';
import ImageRenderer from './ImageRenderer.es';
import LabelRenderer from './LabelRenderer.es';
import LinkRenderer from './LinkRenderer.es';
import ModalLinkRenderer from './ModalLinkRenderer.es';
import QuantitySelectorRenderer from './QuantitySelectorRenderer.es';
import SidePanelLinkRenderer from './SidePanelLinkRenderer.es';
import TooltipPriceRenderer from './TooltipPriceRenderer.es';

const dataRenderers = {
	actionLink: ActionsLinkRenderer,
	actionsDropdown: ActionsDropdownRenderer,
	checkbox: CheckboxRenderer,
	date: DateRenderer,
	default: DefaultRenderer,
	image: ImageRenderer,
	label: LabelRenderer,
	link: LinkRenderer,
	modalLink: ModalLinkRenderer,
	quantitySelector: QuantitySelectorRenderer,
	sidePanelLink: SidePanelLinkRenderer,
	tooltipPrice: TooltipPriceRenderer
};

export function getDataRendererById(id) {
	return dataRenderers[id] || DefaultRenderer;
}

export const fetchedContentRenderers = [];

export function getDataRendererByUrl(url) {
	return new Promise((resolve, reject) => {
		const addedDataRenderer = fetchedContentRenderers.find(
			cr => cr.url === url
		);
		if (addedDataRenderer) {
			resolve(addedDataRenderer.component);
		}
		return getJsModule(url)
			.then(fetchedComponent => {
				fetchedContentRenderers.push({
					component: fetchedComponent,
					url
				});
				return resolve(fetchedComponent);
			})
			.catch(reject);
	});
}
