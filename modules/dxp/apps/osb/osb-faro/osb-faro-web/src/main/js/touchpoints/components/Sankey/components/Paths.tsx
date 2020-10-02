import React from 'react';
import {
	calcExpandedTouchpointBoxPosition,
	calcSankeyNodePosition,
	getCenterY,
	getDeltaY,
	getNodeColor
} from '../utils/sankey';
import {CLASSNAME, LinkSankey, NodeSankey} from './Sankey';
import {linkHorizontal} from 'd3-shape';

const SANKEY_PATHS_MARGIN = 20;

interface IPathsProps extends React.SVGProps<SVGPathElement> {
	activeIndex: number;
	expandedTouchpoint: NodeSankey;
	internalData: {
		links: Array<LinkSankey>;
		nodes: Array<NodeSankey>;
	};
	parentNode: NodeSankey;
	setActiveIndex: (index: number) => void;
}

const Paths: React.FC<IPathsProps> = ({
	activeIndex,
	expandedTouchpoint,
	internalData: {links, nodes},
	parentNode,
	setActiveIndex
}) => {
	const centerY = getCenterY({nodes});
	let marginY = 0;

	if (!nodes.length) return;

	let offsetY1 =
		calcSankeyNodePosition(expandedTouchpoint, parentNode, parentNode.y0) -
		SANKEY_PATHS_MARGIN * 4.15;

	if (expandedTouchpoint && expandedTouchpoint.index == parentNode.index) {
		offsetY1 =
			calcExpandedTouchpointBoxPosition(expandedTouchpoint, parentNode) -
			SANKEY_PATHS_MARGIN;
	}

	const handleMouseEnter = ({currentTarget}) => {
		setActiveIndex(parseInt(currentTarget.dataset.index));
	};

	const handleMouseLeave = () => {
		setActiveIndex(-1);
	};

	return (
		<>
			{links.map((link, index) => {
				const node = nodes[index];
				const {source, target, width, y0, y1} = link;

				let calculatedY0 = calcSankeyNodePosition(
					expandedTouchpoint,
					node,
					y0
				);

				if (expandedTouchpoint && index == expandedTouchpoint.index) {
					calculatedY0 =
						y0 +
						calcExpandedTouchpointBoxPosition(
							expandedTouchpoint,
							node
						) +
						8;
				}

				if (nodes.length && nodes.length <= 2) {
					marginY = 31;
				} else if (nodes.length && nodes.length == 3) {
					marginY = -89;
				}

				const sankeyLinkHorizontal = linkHorizontal()
					.source(() => [source.x1, calculatedY0])
					.target(() => [
						target.x0,
						y1 + offsetY1 + getDeltaY(parentNode, centerY) + marginY
					]);

				return (
					<path
						className={`${CLASSNAME}-path`}
						d={sankeyLinkHorizontal()}
						data-index={index}
						key={`${index}_path`}
						onMouseEnter={handleMouseEnter}
						onMouseLeave={handleMouseLeave}
						stroke={getNodeColor(node, activeIndex)}
						strokeOpacity={0.4}
						strokeWidth={Math.max(1, width)}
					/>
				);
			})}
		</>
	);
};

export default Paths;
