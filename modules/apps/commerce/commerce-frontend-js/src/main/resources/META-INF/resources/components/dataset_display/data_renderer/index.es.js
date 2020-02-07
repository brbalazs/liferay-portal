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
import ActionsLink from './ActionLink.es';
import ActionsDropdown from './ActionsDropdown.es';
import Checkbox from './Checkbox.es';
import Default from './Default.es';
import Label from './Label.es';
import Link from './Link.es';
import ModalLink from './ModalLink.es';
import Image from './Image.es';
import QuantitySelector from './QuantitySelector.es';
import SidePanelLink from './SidePanelLink.es';
import TooltipPrice from './TooltipPrice.es';

const dataRenderers = {
	actionLink: ActionsLink,
	actionsDropdown: ActionsDropdown,
	checkbox: Checkbox,
	default: Default,
	label: Label,
	link: Link,
	modalLink: ModalLink,
	image: Image,
	quantitySelector: QuantitySelector,
	sidePanelLink: SidePanelLink,
	tooltipPrice: TooltipPrice
};

export function getDataRendererById(id) {
	return dataRenderers[id] || Default;
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
