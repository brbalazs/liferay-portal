import DirectAccessEdge from './edges/DirectAccessEdge';
import EmptyStateEdge from './edges/EmptyStateEdge';
import Loading from 'shared/pages/Loading';
import ParentTouchpointEdge from './edges/ParentTouchpointEdge';
import Paths from './Paths';
import React, {useEffect, useRef, useState} from 'react';
import TouchpointEdge from './edges/TouchpointEdge';
import TouchpointPathQuery from 'touchpoints/queries/TouchpointPathQuery';
import {AssetNode, Link, Node, SankeyNode} from '../utils/types';
import {
	getAssetsHeight,
	getBounds,
	getInternalData,
	getSankeyData,
	getSankeyHeight,
	isParentNode,
	SANKEY_OFFSET
} from '../utils/sankey';
import {getVariables} from 'shared/util/mappers';
import {RangeSelectors} from 'shared/types';
import {useQuery} from '@apollo/react-hooks';

export const CLASSNAME = 'analytics-sankey';

interface ISankeyProps extends React.HTMLAttributes<HTMLElement> {
	filters: object;
	loading: boolean;
	width: string;
	rangeSelectors: RangeSelectors;
	router: {
		params: object;
		query: object;
	};
}

const Sankey: React.FC<ISankeyProps> = ({
	filters,
	rangeSelectors,
	router,
	width = '100%'
}) => {
	const svgRef = useRef<any>();
	const [{links, nodes}, setData] = useState<{
		links: Array<Link>;
		nodes: Array<Node>;
	}>({links: [], nodes: []});

	const [activeIndex, setActiveIndex] = useState<number>(-1);
	const [expandedTouchpoint, setExpandedTouchpoint] = useState<
		NodeSankey & {items: Array<AssetNode>}
	>(undefined);

	const sankeyHeight = getSankeyHeight({nodes});
	const [{stageHeight, stageWidth}, setStageSize] = useState<{
		stageHeight: number;
		stageWidth: number;
	}>({stageHeight: sankeyHeight, stageWidth: undefined});

	const {variables} = getVariables({
		filters,
		params: router.params,
		rangeSelectors
	});

	const {loading} = useQuery(TouchpointPathQuery, {
		onCompleted: data => {
			const parsedData = getSankeyData(data, router);
			setData(parsedData);
		},
		variables
	});

	useEffect(() => {
		let extraHeight = 0;

		if (expandedTouchpoint) {
			extraHeight =
				getAssetsHeight(expandedTouchpoint.items) + SANKEY_OFFSET;
		}

		setStageSize(size => ({
			...size,
			stageHeight: sankeyHeight + extraHeight
		}));
	}, [expandedTouchpoint, sankeyHeight]);

	useEffect(() => {
		const {width: stageWidth} = getBounds(svgRef);
		setStageSize(size => ({...size, stageWidth}));
	}, [svgRef.current]);

	const internalData = getInternalData(
		{links, nodes},
		stageWidth,
		sankeyHeight
	);

	const parentNode = internalData.nodes.find(node => isParentNode(node));

	if (parentNode && !loading && svgRef.current) {
		if (parentNode.directAccessMetric === 0 && !links.length) {
			return (
				<div ref={svgRef} style={{width}}>
					<EmptyStateEdge node={parentNode} />
				</div>
			);
		} else if (links.length === 0) {
			return (
				<div ref={svgRef} style={{width}}>
					<DirectAccessEdge
						node={parentNode}
						rangeSelectors={rangeSelectors}
						router={router}
					/>
				</div>
			);
		} else {
			return (
				<svg
					className={CLASSNAME}
					height={stageHeight + SANKEY_OFFSET}
					ref={svgRef}
					width={width}
				>
					<g fill='none'>
						<Paths
							activeIndex={activeIndex}
							expandedTouchpoint={expandedTouchpoint}
							internalData={internalData}
							parentNode={parentNode}
							setActiveIndex={setActiveIndex}
						/>
					</g>
					<g className='svg'>
						{internalData.nodes
							.filter(node => !isParentNode(node))
							.map((node, index) => (
								<g
									className={`${CLASSNAME}-box`}
									key={`${index}_node`}
								>
									<TouchpointEdge
										activeIndex={activeIndex}
										expandedTouchpoint={expandedTouchpoint}
										node={node}
										rangeSelectors={rangeSelectors}
										router={router}
										setActiveIndex={setActiveIndex}
										setExpandedTouchpoint={
											setExpandedTouchpoint
										}
									/>
								</g>
							))}
						<g className={`${CLASSNAME}-box`}>
							<ParentTouchpointEdge
								activeIndex={activeIndex}
								expandedTouchpoint={expandedTouchpoint}
								hasOnlyOneReferrer={links.length === 1}
								node={parentNode}
								nodes={internalData.nodes}
								rangeSelectors={rangeSelectors}
								router={router}
								setActiveIndex={setActiveIndex}
								setExpandedTouchpoint={setExpandedTouchpoint}
							/>
						</g>
					</g>
				</svg>
			);
		}
	}
	return (
		<div ref={svgRef} style={{width}}>
			<Loading />
		</div>
	);
};

export default Sankey;
