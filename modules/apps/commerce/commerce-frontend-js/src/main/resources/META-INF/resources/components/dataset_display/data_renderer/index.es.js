import ActionsDropdown from './ActionsDropdown.es';
import Checkbox from './Checkbox.es';
import Default from './Default.es';
import Label from './Label.es';
import Link from './Link.es';
import ModalLink from './ModalLink.es';
import Picture from './Picture.es';
import QuantitySelector from './QuantitySelector.es';
import SidePanelLink from './SidePanelLink.es';
import TooltipPrice from './TooltipPrice.es';
import { getJsModule } from '../../../utilities/index.es'

const dataRenderers = {
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

export function getDataRendererById(id) {
	return dataRenderers[id] || Default;
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