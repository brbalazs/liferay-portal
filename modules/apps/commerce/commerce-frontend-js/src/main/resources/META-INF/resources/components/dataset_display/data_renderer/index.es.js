import ActionsDropdown from './ActionsDropdown.es';
import Checkbox from './Checkbox.es';
import Default from './Default.es';
import Label from './Label.es';
import Link from './Link.es';
import ModalLink from './ModalLink.es';
import Picture from './Picture.es';
import SidePanelLink from './SidePanelLink.es';
import TooltipPrice from './TooltipPrice.es';
import { getJsModule } from '../../../utilities/index.es';
import QuantitySelector from './QuantitySelector.es';

export const defaultRenderers = {
	actionsDropdown: ActionsDropdown,
	checkbox: Checkbox,
	default: Default,
	label: Label,
	link: Link,
	modalLink: ModalLink,
	picture: Picture,
	quantitySelector: QuantitySelector,
	sidePanelLink: SidePanelLink,
	tooltipPrice: TooltipPrice,
}

export const fetchedContentRenderers = [];

export function getDataRendererByUrl(url) {
	return new Promise((resolve, reject) => {
		const addedDataRenderer = fetchedContentRenderers.find(cr => cr.url === url);
		if(addedDataRenderer) {
			resolve(addedDataRenderer.component);
		}
		return getJsModule(url)
			.then((fetchedComponent) => {
				fetchedContentRenderers.push({
					component: fetchedComponent,
					url
				})
				return resolve(fetchedComponent);
			}).catch(reject)
	})
}

export function getDataRendererById(id) {
	return defaultRenderers[id] || Default;
}