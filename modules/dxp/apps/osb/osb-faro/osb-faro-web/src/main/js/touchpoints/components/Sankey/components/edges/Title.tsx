import getCN from 'classnames';
import React from 'react';
import {AssetNode, SankeyNode} from '../../utils/types';
import {CLASSNAME} from '../Sankey';
import {getAssetUrl, getTouchpointUrl} from '../../utils/edges';
import {getNodeColor, isParentNode} from '../../utils/sankey';
import {isUndefined} from 'lodash';
import {Link} from 'react-router-dom';

const ICON_RADIUS_TOUCHPOINT_SIZE = 12;
const TITLE_FONT_SIZE = 16;

export interface ITitleProps extends IIconProps {
	asset?: AssetNode;
	hasOnClick?: boolean;
	node: SankeyNode;
	textClass: string;
	url?: string;
	wrappedText?: {
		lines: Array<string>;
		sentence: string;
		truncated: boolean;
	};
	y: number;
}

interface IIconProps extends React.SVGAttributes<SVGElement> {
	activeIndex: number;
	heightOffset?: number;
	iconLetter: string;
	isCloseButton?: boolean;
	node: NodeSankey;
	parentLines?: number;
	radius?: number;
	y: number;
}

const Title: React.FC<ITitleProps> = ({
	activeIndex,
	asset,
	hasOnClick = false,
	node,
	textClass,
	url: touchpointUrl,
	wrappedText,
	y,
	...iconProps
}) => {
	const {
		external,
		url: parentUrl,
		wrappedText: {lines, sentence, truncated}
	} = node;

	const classes = getCN(textClass, {'text-truncated': lines.length > 1});
	const others = Liferay.Language.get('others').toLowerCase();

	const offsetY = (lines.length - 1) * TITLE_FONT_SIZE * -1 - 10;
	const {router} = useContext(BasePage.Context);

	let url;
	let isParent;

	if (isParentNode(node)) {
		isParent = isUndefined(wrappedText);
	}

	if (
		!external &&
		touchpointUrl &&
		!isParent &&
		touchpointUrl != others &&
		!asset
	) {
		url = getTouchpointUrl(sentence, touchpointUrl, router);
	} else if (asset) {
		url = getAssetUrl(asset, parentUrl, router);
	}

	const Wrapper = ({children}) => {
		if (url) {
			return (
				<Link className={`${CLASSNAME}-text-button`} to={url}>
					{children}
				</Link>
			);
		} else if (hasOnClick) {
			return (
				<tspan className={`${CLASSNAME}-text-button`}>{children}</tspan>
			);
		}

		return <>{children}</>;
	};

	const linesToRender = wrappedText ? wrappedText.lines : lines;

	return (
		<>
			<Icon {...iconProps} activeIndex={activeIndex} node={node} y={y} />
			<text className={classes} y={y}>
				<Wrapper>
					<>
						<title>
							{(wrappedText && wrappedText.sentence) ||
								sentence ||
								Liferay.Language.get('untitled')}
						</title>
						{linesToRender.map((line, index) => (
							<tspan
								dx={25}
								dy={offsetY + index * 14}
								key={`${index}line`}
								x={0}
								y={y}
							>
								{line}
								{truncated && index === lines.length - 1 && (
									// eslint-disable-next-line
									<tspan>&#8230;</tspan>
								)}
							</tspan>
						))}
					</>
				</Wrapper>
			</text>
		</>
	);
};

export default Title;

const Icon: React.FC<IIconProps> = ({
	activeIndex,
	heightOffset = 0,
	iconLetter,
	isCloseButton = false,
	node,
	parentLines = 1,
	radius = ICON_RADIUS_TOUCHPOINT_SIZE,
	y
}) => {
	const {
		wrappedText: {lines}
	} = node;

	const color = getNodeColor(node, activeIndex);
	const offsetY = (lines.length - 1) * 20 * -1 - 6;
	const rectHeight = parentLines * 11 + heightOffset;
	const margin =
		radius == ICON_RADIUS_TOUCHPOINT_SIZE ? 0 : (lines.length - 1) * 8;

	const textClasses = getCN(`${CLASSNAME}-icon-reference`, {
		'analytics-sankey-close-list': isCloseButton,
		'analytics-sankey-icon-normal': radius == ICON_RADIUS_TOUCHPOINT_SIZE,
		'analytics-sankey-icon-small': radius != ICON_RADIUS_TOUCHPOINT_SIZE,
		'text-truncated': lines.length > 1
	});

	return (
		<>
			<circle
				cx={7}
				cy={y - 10 + margin + offsetY / 2}
				fill={color}
				r={radius}
			/>

			{/* rect between circles */}
			{radius < ICON_RADIUS_TOUCHPOINT_SIZE && (
				<rect
					fill={color}
					height={rectHeight}
					width='2'
					x={6}
					y={
						y -
						16 -
						12 * parentLines +
						margin -
						heightOffset +
						offsetY / 2
					}
				/>
			)}

			<text
				className={textClasses}
				x={8.5 - radius / 2}
				y={y - 11 + margin + offsetY / 2 + radius / 2}
			>
				{iconLetter}
			</text>
		</>
	);
};
