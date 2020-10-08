import AssetEdge from './AssetEdge';
import React from 'react';
import Title from './Title';
import {ASSET_HEIGHT, SANKEY_COLORS} from '../../utils/sankey';
import {AssetNode, SankeyNode} from '../../utils/types';
import {CLASSNAME} from '../Sankey';
import {getWrappedText} from '../../utils/edges';
import {sub} from 'shared/util/lang';
import {toThousands} from 'shared/util/numbers';

interface ITouchpointLabelProps extends React.SVGAttributes<SVGElement> {
	activeIndex: number;
	expandedTouchpoint: SankeyNode;
	items: Array<AssetNode>;
	loading?: boolean;
	node: SankeyNode & {
		color?: string;
		views?: number;
		x: number;
		y: number;
	};
	setExpandedTouchpoint: (object) => void;
}

const TouchpointLabel: React.FC<ITouchpointLabelProps> = ({
	activeIndex,
	expandedTouchpoint,
	items,
	loading = false,
	node,
	setExpandedTouchpoint
}) => {
	const {
		index,
		name,
		url,
		views,
		wrappedText: {lines},
		y
	} = node;

	const handleShowMoreAssets = e => {
		const {nodeIndex} = e.currentTarget.dataset;
		const index = parseInt(nodeIndex);

		if (expandedTouchpoint && expandedTouchpoint.index === index) {
			return false;
		}

		setExpandedTouchpoint &&
			setExpandedTouchpoint({
				index,
				items
			});

		return false;
	};

	const handleCloseAssets = () => {
		setExpandedTouchpoint && setExpandedTouchpoint(null);
	};

	const renderGroupedInformation = () => (
		<>
			{/* Assets */}
			{items.map(({assetId, assetType, ...otherAssetProps}, index) => (
				<AssetEdge
					activeIndex={activeIndex}
					asset={{
						...otherAssetProps,
						id: assetId,
						type: assetType
					}}
					assetIndex={index}
					key={index}
					node={node}
					parentLines={lines.length}
				/>
			))}

			{/* Close Button*/}
			<g data-node-index={index} onClick={handleCloseAssets}>
				<TouchpointTitle
					activeIndex={activeIndex}
					hasOnClick
					heightOffset={lines.length > 1 ? -1 : 7}
					iconLetter='-'
					isCloseButton
					node={node}
					parentLines={lines.length}
					radius={9}
					textClass={`${CLASSNAME}-subtitle-show-link`}
					wrappedText={getWrappedText(
						Liferay.Language.get('close-list')
					)}
					y={node.y - 1 + 32 * (items.length + 1)}
				/>
			</g>
		</>
	);

	const renderSingleInformation = () => {
		const {index, name, url, y} = node;

		return (
			<>
				{loading && (
					<g>
						<TouchpointTitle
							activeIndex={activeIndex}
							iconLetter='+'
							node={node}
							parentLines={lines.length}
							radius={9}
							textClass={`${CLASSNAME}-subtitle-show-link`}
							wrappedText={getWrappedText(
								Liferay.Language.get('loading-assets')
							)}
							y={y + 28}
						/>
					</g>
				)}

				{items.length > 1 && name != Liferay.Language.get('others') && (
					<g data-node-index={index} onClick={handleShowMoreAssets}>
						<TouchpointTitle
							activeIndex={activeIndex}
							hasOnClick
							iconLetter='+'
							node={node}
							parentLines={lines.length}
							radius={9}
							textClass={`${CLASSNAME}-subtitle-show-link`}
							wrappedText={getWrappedText(
								sub(Liferay.Language.get('show-top-x-assets'), [
									items.length
								])
							)}
							y={y + 28}
						/>
					</g>
				)}

				{items.length == 1 && name != Liferay.Language.get('others') && (
					<g>
						<TouchpointTitle
							activeIndex={activeIndex}
							asset={{
								...items[0],
								id: items[0].assetId,
								type: items[0].assetType
							}}
							iconLetter='A'
							name={items[0].title}
							node={node}
							parentLines={lines.length}
							radius={9}
							textClass={`${CLASSNAME}-subtitle`}
							url={url}
							wrappedText={items[0].wrappedText}
							y={y + 28}
						/>
					</g>
				)}
			</>
		);
	};

	return (
		<>
			{/* total views */}
			<text
				className={`${CLASSNAME}-views`}
				fill={SANKEY_COLORS.views}
				x={25}
				y={y - (lines.length > 1 ? lines.length * 22 : 30)}
			>
				{`${toThousands(views)} ${Liferay.Language.get('views')}`}
			</text>

			<TouchpointTitle
				activeIndex={activeIndex}
				iconLetter='P'
				name={name}
				node={node}
				textClass={`${CLASSNAME}-title`}
				url={url}
				y={y}
			/>

			{expandedTouchpoint && expandedTouchpoint.index == index
				? renderGroupedInformation()
				: renderSingleInformation()}
		</>
	);
};

export default TouchpointLabel;
