import React from 'react';
import Title, {ITitleProps} from './Title';
import {CLASSNAME, NodeSankey} from '../Sankey';

interface IAssetEdgeProps
	extends Omit<ITitleProps, 'iconLetter' | 'textClass' | 'y' | 'name'> {
	activeIndex: number;
	assetIndex: number;
	node: NodeSankey & {
		y: number;
	};
	handleShowMoreAssetsClick?: (object) => void;
}

const AssetEdge: React.FC<IAssetEdgeProps> = ({
	activeIndex,
	assetIndex,
	asset,
	handleShowMoreAssetsClick = () => {},
	node,
	parentLines,
	url
}) => {
	const {index, y} = node;
	const assetY = y - 1 + 32 * (assetIndex + 1);

	let heightOffset = 7;

	if (parentLines > 1) {
		heightOffset = -1;
	}

	return (
		<g
			data-node-index={index}
			key={assetIndex}
			onClick={handleShowMoreAssetsClick}
		>
			<Title
				activeIndex={activeIndex}
				asset={asset}
				hasOnClick
				heightOffset={heightOffset}
				iconLetter='A'
				name={asset.title}
				node={node}
				parentLines={parentLines}
				radius={9}
				textClass={`${CLASSNAME}-subtitle`}
				url={url}
				wrappedText={asset.wrappedText}
				y={assetY}
			/>
		</g>
	);
};

export default AssetEdge;
