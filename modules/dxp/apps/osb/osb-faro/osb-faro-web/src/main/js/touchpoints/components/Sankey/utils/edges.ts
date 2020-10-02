import {
	getParentNode,
	isParentNode,
	SANKEY_COLORS,
	SANKEY_NODE_WIDTH
} from './sankey';
import {
	getPercentage,
	getRangeSelectorsFromQuery,
	truncateText
} from 'shared/util/util';
import {getUrl} from 'shared/util/urls';
import {nextColor} from 'shared/util/charts';
import {NodeSankey} from '../components/Sankey';
import {pickBy} from 'lodash';
import {Routes, toAssetDashboardRoute} from 'shared/util/router';
import {textWrap} from 'd3plus-text';
import {toThousands, undoThousands} from 'shared/util/numbers';

export const assetTypeLabels = {
	blog: Liferay.Language.get('blogs'),
	custom: Liferay.Language.get('custom'),
	document: Liferay.Language.get('documents-and-media'),
	form: Liferay.Language.get('forms'),
	journal: Liferay.Language.get('web-content')
};

export function getAssetUrl(
	{id: assetId, title, type},
	touchpoint,
	{params, query}
) {
	const rangeSelectors = getRangeSelectorsFromQuery(query);

	return toAssetDashboardRoute(
		type,
		{
			...params,
			assetId,
			title,
			touchpoint
		},
		pickBy({...query, rangeKey: rangeSelectors.rangeKey})
	);
}

export function getTouchpointUrl(title, touchpoint, {params, query}) {
	const rangeSelectors = getRangeSelectorsFromQuery(query);

	const router = {
		params: {
			...params,
			title,
			touchpoint: encodeURIComponent(touchpoint)
		},
		query: {
			...query,
			rangeKey: rangeSelectors.rangeKey
		}
	};

	return getUrl(Routes.SITES_TOUCHPOINTS_OVERVIEW, router);
}

export function getSize(value) {
	if (isNaN(value)) {
		return 0;
	}

	return Math.sign(value) == 1 ? value : 0;
}

export function getTitleY({directAccessMetric}: NodeSankey, items) {
	let titleY = 0;

	if (items.length) {
		if (directAccessMetric === 0) {
			titleY = 220;
		} else {
			titleY = 183;
		}
	} else {
		if (directAccessMetric === 0) {
			titleY = 250;
		} else {
			titleY = 210;
		}
	}

	return titleY;
}

export function getTotalViews({
	directAccessMetric,
	indirectAccessMetric
}: NodeSankey) {
	return (
		undoThousands(toThousands(directAccessMetric)) +
		undoThousands(toThousands(indirectAccessMetric))
	);
}

export function getMarginY(touchpointList) {
	let marginY = 93;

	if (touchpointList.length && touchpointList.length <= 2) {
		marginY = 223;
	} else if (touchpointList.length && touchpointList.length == 3) {
		marginY = 183;
	}

	return marginY;
}

export function getNodeHeight({value}: NodeSankey, nodes: Array<NodeSankey>) {
	const parentNode = getParentNode(nodes);

	return (
		((parentNode.y1 - parentNode.y0) *
			getPercentage(value, parentNode.value)) /
		100
	);
}

export function getNodeColor(node, activeIndex) {
	const {color, index} = node;

	if (activeIndex > -1 && activeIndex !== index) {
		return SANKEY_COLORS.bgGray;
	}

	if (color) {
		return color;
	}

	if (isParentNode(node)) {
		return SANKEY_COLORS.bgDirectTraffic;
	}

	return nextColor(index);
}

export function getWrappedText(name, fontSize = 16) {
	const textWrapper = textWrap()
		.fontSize(fontSize)
		.height(55)
		.overflow(true)
		.width(SANKEY_NODE_WIDTH);

	const defaultCharacterLimit = 20;

	let wrappedText;

	try {
		wrappedText = textWrapper(name);
	} catch (e) {
		wrappedText = {
			lines: [truncateText(name, defaultCharacterLimit, '')],
			truncated: name.length > defaultCharacterLimit
		};
	}

	const constrainLastLineLength = lines => {
		const lastLine = lines[lines.length - 1];

		const linesWithoutLastLine = lines.slice(0, lines.length - 1);

		if (lastLine.length > defaultCharacterLimit && !/[\s]/.test(lastLine)) {
			return linesWithoutLastLine.concat(
				lastLine.substr(0, defaultCharacterLimit)
			);
		}

		return lines;
	};

	return {
		...wrappedText,
		lines: constrainLastLineLength(wrappedText.lines)
	};
}
