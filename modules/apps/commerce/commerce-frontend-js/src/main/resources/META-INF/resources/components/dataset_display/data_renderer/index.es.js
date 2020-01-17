import ActionsDropdown from './ActionsDropdown.es';
import Checkbox from './Checkbox.es';
import Default from './Default.es';
import Label from './Label.es';
import Link from './Link.es';
import ModalLink from './ModalLink.es';
import Picture from './Picture.es';
import SidePanelLink from './SidePanelLink.es';
import TooltipPrice from './TooltipPrice.es';

export const defaultRenderers = [
	{
		component: ActionsDropdown,
		id: "actionsDropdown",
	},
	{
		component: Checkbox,
		id: "checkbox",
	},
	{
		component: Default,
		id: "default",
	},
	{
		component: Label,
		id: "label",
	},
	{
		component: Link,
		id: "link",
	},
	{
		component: ModalLink,
		id: "modalLink",
	},
	{
		component: Picture,
		id: "picture",
	},
	{
		component: SidePanelLink,
		id: "sidePanelLink",
	},
	{
		component: TooltipPrice,
		id: "tooltipPrice",
	}
];

function getRenderersObjectMap(map= []) {
	return map.reduce(
		(acc, el) => ({
			...acc, 
			[el.id] : el.component
		}),
		{}
	)
}

export function getDataRenderers(customRenderers) {
	const defaultRenderersObject = getRenderersObjectMap(defaultRenderers);
	const customRenderersObject = getRenderersObjectMap(customRenderers);
	return {...defaultRenderersObject, ...customRenderersObject}
}

export function getDataRenderer(id, renderers) {
	return renderers[id] || Default;
}