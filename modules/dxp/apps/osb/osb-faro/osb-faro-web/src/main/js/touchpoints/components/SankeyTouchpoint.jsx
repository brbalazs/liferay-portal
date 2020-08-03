/* eslint-disable jsx-a11y/no-static-element-interactions */
/* eslint-disable jsx-a11y/click-events-have-key-events */
/* eslint-disable jsx-a11y/anchor-is-valid */

import autobind from 'autobind-decorator';
import BasePage from 'shared/components/base-page';
import getCN from 'classnames';
import React from 'react';
import {Colors, nextColor} from 'shared/util/charts';
import {getPercentage, truncateText} from 'shared/util/util';
import {getUrl} from 'shared/util/urls';
import {Link} from 'react-router-dom';
import {pickBy} from 'lodash';
import {PropTypes} from 'prop-types';
import {Routes} from 'shared/util/router';
import {sub} from 'shared/util/lang';
import {textWrap} from 'd3plus-text';
import {toAssetDashboardRoute} from 'shared/util/router';
import {toRounded, toThousands, undoThousands} from 'shared/util/numbers';

const CLASSNAME = 'analytics-sankey';
const CLASSNAME_BOX = `${CLASSNAME}-parent`;

const SANKEY_COLORS = {
	bgDirectTraffic: '#50D2A0',
	bgGray: '#D7D7DA',
	bgInactive: '#DCDDE1',
	bgPage: '#F1F2F5',
	bgShapeMain: '#FFFFFF',
	directTraffic: '#000000',
	link: '#B5B5B5',
	title: '#000000',
	views: '#6C6C76'
};

/**
 * Sankey Touchpoint
 * @class
 */
class SankeyTouchpoint extends React.Component {
	static contextType = BasePage.Context;

	static defaultProps = {
		hasOnlyOneReferrer: false,
		isDirectAccess: false,
		isEmptyState: false,
		isMain: false,
		items: [],
		loading: true,
		nodePadding: 100,
		nodeWidth: 200,
		parentNodeHeight: 220
	};

	static propTypes = {
		activeIndex: PropTypes.number,
		calcExpandedTouchpointPositionFn: PropTypes.func,
		calcSankeyNodePositionFn: PropTypes.func,
		expandedTouchpointIndex: PropTypes.any,
		getDeltaYFn: PropTypes.func,
		hasOnlyOneReferrer: PropTypes.bool,
		isDirectAccess: PropTypes.bool,
		isEmptyState: PropTypes.bool,
		isMain: PropTypes.bool,
		items: PropTypes.array,
		loading: PropTypes.bool,
		node: PropTypes.shape({
			directAccessMetric: PropTypes.number,
			external: PropTypes.bool,
			indirectAccessMetric: PropTypes.number,
			name: PropTypes.string,
			url: PropTypes.string
		}).isRequired,
		nodePadding: PropTypes.any,
		nodeWidth: PropTypes.number,
		onMouseEnter: PropTypes.func,
		onMouseLeave: PropTypes.func,
		onTouchpointIndexChange: PropTypes.func,
		onTouchpointLoaded: PropTypes.func,
		parentNodeHeight: PropTypes.number,
		touchpointList: PropTypes.array
	};

	state = {
		showAssets: false
	};

	/**
	 * Set Items
	 * @param {array} items
	 */
	setItems(items) {
		if (items.length <= 5) {
			return items;
		}

		return items.slice(0, 5);
	}

	/**
	 * Get Node Color
	 * @param {object} node
	 */
	getNodeColor(node) {
		const {color, index} = node;
		const {activeIndex} = this.props;

		if (activeIndex > -1 && activeIndex !== index) {
			return SANKEY_COLORS.bgGray;
		}

		if (color) {
			return color;
		}

		if (this.isParentNode(node)) {
			return SANKEY_COLORS.bgDirectTraffic;
		}

		return nextColor(index);
	}

	/**
	 * Get Wrapped Text
	 * @param {string} name
	 * @param {?number} fontSize
	 */
	getWrappedText(name, fontSize = 16) {
		const textWrapper = textWrap()
			.fontSize(fontSize)
			.height(55)
			.overflow(true)
			.width(this.props.nodeWidth);

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

			if (
				lastLine.length > defaultCharacterLimit &&
				!/[\s]/.test(lastLine)
			) {
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

	/**
	 * Render Information Template
	 * @param {object} param0
	 */
	renderInformationTemplate({
		asset,
		hasOnClick,
		heightOffset,
		iconLetter,
		isAnAsset = false,
		isClosed,
		name,
		node,
		parentLines,
		radius,
		textClass,
		url,
		y
	}) {
		return (
			<>
				{this.renderIconReference(
					{
						heightOffset,
						iconLetter,
						isClosed,
						name,
						parentLines,
						radius,
						y
					},
					node
				)}

				{this.renderSankeySVGTextTitle({
					asset,
					hasOnClick,
					isAnAsset,
					name,
					node,
					textClass,
					url,
					y
				})}
			</>
		);
	}

	/**
	 * Render Icon Reference
	 * @param {object} config
	 * @param {object} node
	 */
	renderIconReference(config, node) {
		const touchpointSize = 12;
		const {
			y,
			name,
			iconLetter,
			radius = touchpointSize,
			heightOffset = 0,
			parentLines = 1,
			isClosed
		} = config;
		const color = this.getNodeColor(node);
		const {lines} = this.getWrappedText(name);
		const offsetY = (lines.length - 1) * 20 * -1 - 6;
		const rectHeight = parentLines * 11 + heightOffset;
		const margin = radius == touchpointSize ? 0 : (lines.length - 1) * 8;

		const textClasses = getCN(`${CLASSNAME}-icon-reference`, {
			'analytics-sankey-close-list': isClosed,
			'analytics-sankey-icon-normal': radius == touchpointSize,
			'analytics-sankey-icon-small': radius != touchpointSize,
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

				{radius < touchpointSize && (
					<rect
						fill={color}
						height={rectHeight}
						lines={parentLines}
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
	}

	/**
	 * Return the formatted url from a asset
	 * @param {object} asset
	 * @returns {string} url
	 */
	getAssetUrl({id: assetId, title, type}, touchpoint) {
		const {
			context: {
				router: {params, query}
			},
			props: {rangeSelectors}
		} = this;

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

	/**
	 * Return the formatted url from a touchpoint
	 * @param {string} touchpoint
	 * @returns {string} url
	 */
	getTouchpointUrl(title, touchpoint) {
		const {
			context: {
				router: {params, query}
			},
			props: {rangeSelectors}
		} = this;

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

	/**
	 * Render Sankey SVG Text Title
	 * @param {object} param0
	 */
	renderSankeySVGTextTitle({
		asset,
		hasOnClick,
		isAnAsset,
		name,
		node,
		textClass,
		url: touchpointUrl,
		y
	}) {
		const {external, url: parentUrl} = node;
		const {lines, truncated} = this.getWrappedText(name);
		const fontSize = 16;
		const offsetY = (lines.length - 1) * fontSize * -1 - 10;
		const classes = getCN(textClass, {'text-truncated': lines.length > 1});
		const others = Liferay.Language.get('others').toLowerCase();

		let url;
		let isParentNode;

		if (this.isParentNode(node)) {
			isParentNode = node.name === name;
		}

		if (
			!external &&
			touchpointUrl &&
			!isParentNode &&
			touchpointUrl != others &&
			!isAnAsset
		) {
			url = this.getTouchpointUrl(name, touchpointUrl);
		} else if (isAnAsset) {
			url = this.getAssetUrl(asset, parentUrl);
		}

		const sankeySVGTextTitleTmpl = (
			<>
				<title>{name || Liferay.Language.get('untitled')}</title>
				{lines.map((line, index) => (
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
		);

		let retVal = sankeySVGTextTitleTmpl;

		if (hasOnClick) {
			retVal = <a href='javascript:;'>{sankeySVGTextTitleTmpl}</a>;
		} else if (url) {
			retVal = <Link to={url}>{sankeySVGTextTitleTmpl}</Link>;
		}

		return (
			<text className={classes} y={y}>
				{retVal}
			</text>
		);
	}

	/**
	 * Render Parent Node Rainbow
	 * @param {number} parentX
	 * @param {number} parentY
	 * @param {?number} opacity
	 */
	renderParentNodeRainbow(parentX, parentY, opacity = 1) {
		const {onMouseEnter, onMouseLeave, touchpointList} = this.props;
		const width = 200;

		let nodeY0 = parentY.y0;
		let previousNodeHeight = 0;
		let heightOffset = 0;

		if (touchpointList.length && touchpointList.length <= 3) {
			heightOffset = 1;
		}

		return touchpointList
			.filter(node => !this.isParentNode(node))
			.map(node => {
				const index = node.index;

				nodeY0 += previousNodeHeight;

				const nodeHeight = this.getNodeHeight(node);

				previousNodeHeight = nodeHeight;

				return (
					<svg key={`${index}${opacity}_barColor`}>
						<rect
							className={`${CLASSNAME}-node`}
							data-index={index}
							fill={this.getNodeColor(node)}
							fillOpacity={opacity}
							height={nodeHeight + heightOffset}
							onMouseEnter={onMouseEnter}
							onMouseLeave={onMouseLeave}
							width={width}
							x={parentX.x0}
							y={nodeY0}
						/>
						<line
							data-index={index}
							onMouseEnter={onMouseEnter}
							onMouseLeave={onMouseLeave}
							stroke={SANKEY_COLORS.bgPage}
							strokeOpacity={opacity}
							strokeWidth={2}
							x1={parentX.x0}
							x2={parentX.x1}
							y1={nodeY0}
							y2={nodeY0}
						/>
					</svg>
				);
			});
	}

	/**
	 * Render Node Label
	 * @param {object} node
	 */
	renderNodeLabel(node) {
		const {index, name, url, views, y} = node;
		const {expandedTouchpointIndex} = this.props;
		const {lines} = this.getWrappedText(name);
		let viewsY = 30;

		if (lines.length > 1) {
			viewsY = 22 * lines.length;
		}

		return (
			<>
				{/* total views */}
				<text
					className={`${CLASSNAME}-views`}
					fill={SANKEY_COLORS.views}
					x={25}
					y={y - viewsY}
				>
					{`${toThousands(views)} ${Liferay.Language.get('views')}`}
				</text>

				{this.renderInformationTemplate({
					heightOffset: 0,
					iconLetter: 'P',
					name,
					node,
					textClass: `${CLASSNAME}-title`,
					url,
					y
				})}

				{expandedTouchpointIndex == index
					? this.renderGroupedInformation(node, name)
					: this.renderSingleInformation(node)}
			</>
		);
	}

	/**
	 * Handle Show More Assets Click
	 * @param {object} e
	 */
	@autobind
	handleShowMoreAssetsClick(e) {
		const {
			expandedTouchpointIndex,
			items,
			onTouchpointIndexChange
		} = this.props;
		const {nodeIndex} = e.currentTarget.dataset;
		let index;

		e.preventDefault();

		if (expandedTouchpointIndex != nodeIndex) {
			index = parseInt(nodeIndex);
		}

		onTouchpointIndexChange &&
			onTouchpointIndexChange({
				index,
				items
			});

		return false;
	}

	/**
	 * Render Grouped Information
	 * @param {object} node
	 * @param {string} touchpointName
	 */
	renderGroupedInformation(node, touchpointName) {
		const {items} = this.props;
		const informationsList = [
			...items,
			{
				clickFn: this.handleShowMoreAssetsClick,
				hasOnClick: true,
				isClosed: true,
				title: Liferay.Language.get('close-list'),
				url: node.url
			}
		];

		return (
			<>
				{informationsList.map((information, index) =>
					this.renderTouchpointAsset(
						node,
						information,
						index,
						touchpointName
					)
				)}
			</>
		);
	}

	/**
	 * Render Single Information
	 * @param {object} node
	 */
	renderSingleInformation(node) {
		const {index, name, url, y} = node;
		const {items, loading} = this.props;
		const {lines} = this.getWrappedText(name);

		return (
			<>
				{loading && (
					<g>
						{this.renderInformationTemplate({
							heightOffset: 0,
							iconLetter: '+',
							name: Liferay.Language.get('loading-assets'),
							node,
							parentLines: lines.length,
							radius: 9,
							textClass: `${CLASSNAME}-subtitle-show-link`,
							y: y + 28
						})}
					</g>
				)}

				{items.length > 1 && name != Liferay.Language.get('others') && (
					<g
						data-node-index={index}
						onClick={this.handleShowMoreAssetsClick}
					>
						{this.renderInformationTemplate({
							hasOnClick: true,
							heightOffset: 0,
							iconLetter: '+',
							name: sub(
								Liferay.Language.get('show-top-x-assets'),
								[items.length]
							),
							node,
							parentLines: lines.length,
							radius: 9,
							textClass: `${CLASSNAME}-subtitle-show-link`,
							url,
							y: y + 28
						})}
					</g>
				)}

				{items.length == 1 && name != Liferay.Language.get('others') && (
					<g>
						{this.renderInformationTemplate({
							asset: {
								id: items[0].assetId,
								title: items[0].title,
								type: items[0].assetType
							},
							heightOffset: 0,
							iconLetter: 'A',
							isAnAsset: true,
							name: items[0].title,
							node,
							parentLines: lines.length,
							radius: 9,
							textClass: `${CLASSNAME}-subtitle`,
							url,
							y: y + 28
						})}
					</g>
				)}
			</>
		);
	}

	/**
	 * Render Touchpoint Asset
	 * @param {object} node
	 * @param {object} param1
	 * @param {number} assetIndex
	 * @param {string} touchpointName
	 */
	renderTouchpointAsset(
		node,
		{
			assetType,
			assetId,
			hasOnClick,
			title,
			url,
			clickFn = () => {},
			isClosed = false
		},
		assetIndex,
		touchpointName
	) {
		const {index, y} = node;
		const {lines} = this.getWrappedText(touchpointName);
		const assetY = y - 1 + 32 * (assetIndex + 1);

		let heightOffset = 7;

		if (lines.length > 1) {
			heightOffset = -1;
		}

		return (
			<g data-node-index={index} key={assetIndex} onClick={clickFn}>
				{this.renderInformationTemplate({
					asset: {
						id: assetId,
						title,
						type: assetType
					},
					hasOnClick,
					heightOffset,
					iconLetter: isClosed ? '-' : 'A',
					isAnAsset: assetId ? true : false,
					isClosed,
					name: title,
					node,
					parentLines: lines.length,
					radius: 9,
					textClass: `${CLASSNAME}-subtitle`,
					url,
					y: assetY
				})}
			</g>
		);
	}

	/**
	 * Render Touchpoint Item
	 */
	renderTouchpointItem() {
		const {
			calcExpandedTouchpointPositionFn,
			calcSankeyNodePositionFn,
			items,
			loading,
			node,
			onMouseEnter,
			onMouseLeave
		} = this.props;
		const {
			calculatedY0,
			index,
			value,
			x0,
			x1,
			y0,
			y1
		} = calcSankeyNodePositionFn(node, node.y0);
		const height = y1 - y0;
		const rectPosition = calcExpandedTouchpointPositionFn(node);

		let informationsY = 0;

		if (!items.length && !loading) {
			informationsY = 20;
		}

		return (
			<svg
				className={`${CLASSNAME}-node-group`}
				data-index={index}
				onMouseEnter={onMouseEnter}
				onMouseLeave={onMouseLeave}
				x={x0}
				y={calculatedY0 - 30}
			>
				{/* percentage bar color */}
				<rect
					className={`${CLASSNAME}-node`}
					fill={this.getNodeColor(node)}
					fillOpacity={1}
					height={y1 - y0}
					stroke='none'
					width={x1 - x0}
					y={rectPosition + 4}
				/>

				<g className={`${CLASSNAME}-information`}>
					{/* text title */}
					{this.renderNodeLabel({
						...node,
						views: value,
						x: x0,
						y: informationsY
					})}
				</g>

				{/* value */}
				<text
					className={`${CLASSNAME}-numbers-of-views`}
					dy={5}
					textAnchor='middle'
					x={100}
					y={rectPosition + 4 + height - height / 2}
				>
					{toThousands(node.value)}
				</text>
			</svg>
		);
	}

	/**
	 * Is Parent Node
	 * @param {object} node
	 */
	isParentNode(node) {
		return Object.prototype.hasOwnProperty.call(node, 'directAccessMetric');
	}

	/**
	 * Get Parent Node
	 */
	getParentNode() {
		const {touchpointList} = this.props;

		return touchpointList.find(node => this.isParentNode(node));
	}

	/**
	 * Get Node Height
	 * @param {object} param0
	 */
	getNodeHeight({value}) {
		const parentNode = this.getParentNode();
		const {touchpointList} = this.props;

		return (
			((parentNode.y1 - parentNode.y0) *
				getPercentage(
					value,
					touchpointList[touchpointList.length - 1].value
				)) /
			100
		);
	}

	/**
	 * Get Size
	 * @param {number} value
	 * @returns 0 when the number is negative.
	 */
	getSize(value) {
		if (isNaN(value)) {
			return 0;
		}

		return Math.sign(value) == 1 ? value : 0;
	}

	/**
	 * Get Title Y
	 * @param {object} node
	 * @return title Y value to be aligned
	 */
	getTitleY(node) {
		const {directAccessMetric} = node;
		const {items} = this.props;

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

	getTotalViews(node) {
		const {directAccessMetric, indirectAccessMetric} = node;

		return (
			undoThousands(toThousands(directAccessMetric)) +
			undoThousands(toThousands(indirectAccessMetric))
		);
	}

	getMarginY(touchpointList) {
		let marginY = 93;

		if (touchpointList.length && touchpointList.length <= 2) {
			marginY = 223;
		} else if (touchpointList.length && touchpointList.length == 3) {
			marginY = 183;
		}

		return marginY;
	}

	/**
	 * Render Parent Node
	 */
	renderParentNode() {
		const {hasOnlyOneReferrer} = this.props;
		const {
			calcExpandedTouchpointPositionFn,
			calcSankeyNodePositionFn,
			getDeltaYFn,
			node,
			touchpointList
		} = this.props;
		const {directAccessMetric, indirectAccessMetric, x0, x1, y0, y1} = node;
		const height = this.getSize(y1 - y0);
		const deltaY = this.getSize(getDeltaYFn());
		const rectPosition = this.getSize(
			calcExpandedTouchpointPositionFn(node) + 230
		);
		const {calculatedY0} = calcSankeyNodePositionFn(node, node.y0);
		const titleY = this.getTitleY(node);
		const totalViews = this.getTotalViews(node);
		const marginY = this.getMarginY(touchpointList);

		return (
			<svg x={x0} y={calculatedY0 - marginY}>
				{/* percentage bar color */}
				{this.renderParentNodeRainbow(
					{x0: 0, x1},
					{y0: rectPosition, y1: y1 + deltaY}
				)}

				{/* background white */}
				{!hasOnlyOneReferrer && (
					<rect
						className={`${CLASSNAME}-node`}
						fill={SANKEY_COLORS.bgShapeMain}
						fillOpacity={0.8}
						height={this.getSize(toRounded(height) - 20)}
						stroke='none'
						width={this.getSize(x1 - x0 - 20)}
						x={10}
						y={rectPosition + 10}
					/>
				)}

				{/* percentage bar color */}
				{this.renderParentNodeRainbow(
					{x0: 0, x1},
					{y0: rectPosition, y1: y1 + deltaY},
					0
				)}

				{directAccessMetric != 0 && (
					<g>
						{/* background direct traffic */}
						<rect
							className={`${CLASSNAME}-node`}
							fill={this.getNodeColor(node)}
							height={40}
							width={this.getSize(x1 - x0)}
							y={rectPosition - 41}
						/>

						{/* text direct traffic number */}
						<text
							className={`${CLASSNAME}-title`}
							dy={-14}
							fill={SANKEY_COLORS.directTraffic}
							textAnchor='middle'
							x={100}
							y={rectPosition}
						>
							{toThousands(directAccessMetric)}
						</text>

						<title>{Liferay.Language.get('direct-traffic')}</title>
					</g>
				)}

				{/* title */}
				<svg y={titleY}>
					<g>
						{this.renderNodeLabel({
							...node,
							color: Colors.gray,
							views: totalViews,
							x: 0,
							y: 0
						})}
					</g>
				</svg>

				{/* value */}
				<text
					className={`${CLASSNAME}-numbers-of-views`}
					dy={5}
					textAnchor='middle'
					x={100}
					y={rectPosition + 4 + height - height / 2}
				>
					{toThousands(indirectAccessMetric)}
				</text>
			</svg>
		);
	}

	/**
	 * Handle Click Show Assets
	 * @description toggle list view when it was clicked.
	 */
	@autobind
	handleToggleShowAssets() {
		this.setState({
			showAssets: !this.state.showAssets
		});
	}

	/**
	 * Render List Group
	 * @param {object} node
	 * @param {array} items list of assets relative to node
	 * @returns list of items
	 */
	renderListGroup(node, items) {
		const {showAssets} = this.state;
		const {url: parentUrl} = node;

		const assets = items.map(item => ({
			...item,
			id: item.assetId,
			type: item.assetType
		}));

		if (assets.length <= 1) {
			const asset = assets[0];
			const url = this.getAssetUrl(asset, parentUrl);

			return (
				<Link className='icon icon-asset' to={url}>
					<span className='text-truncate'>{asset.title}</span>
				</Link>
			);
		}

		return (
			<ul>
				{!showAssets ? (
					<li>
						<a
							className='action-link icon icon-plus'
							onClick={this.handleToggleShowAssets}
							to='javascript:;'
						>
							{sub(Liferay.Language.get('show-top-x-assets'), [
								assets.length
							])}
						</a>
					</li>
				) : (
					<>
						{assets.map((asset, index) => {
							const {title} = asset;
							const url = this.getAssetUrl(asset, parentUrl);

							return (
								<li key={index}>
									<Link className='icon icon-asset' to={url}>
										<span className='text-truncate'>
											{title}
										</span>
									</Link>
								</li>
							);
						})}

						<li>
							<a
								className='icon icon-minor action-link'
								onClick={this.handleToggleShowAssets}
								to='javascript:;'
							>
								{Liferay.Language.get('close-list')}
							</a>
						</li>
					</>
				)}
			</ul>
		);
	}

	/**
	 * Render Direct Access
	 * @description It is a part of empty state
	 * @returns HTML with direct access content
	 */
	renderDirectAccess(color) {
		return (
			<div className={CLASSNAME_BOX} ref='svg'>
				{this.renderBox(color)}
			</div>
		);
	}

	/**
	 * Render Empty State
	 * @description It is a part of empty state
	 * @returns HTML with empty state content
	 */
	renderEmptyState(color) {
		return (
			<div className={CLASSNAME_BOX} ref='svg'>
				{this.renderBox(color)}

				<div className={`${CLASSNAME_BOX}-text`}>
					<p className='mb-2'>
						{Liferay.Language.get(
							'no-data-found-for-the-selected-filter'
						)}
					</p>
					<p className='font-size-sm'>
						{Liferay.Language.get(
							'the-selected-filter-did-not-match-any-result'
						)}
					</p>
				</div>
			</div>
		);
	}

	/**
	 * Render Box
	 * @returns HTML with box content
	 */
	renderBox(color) {
		const {items, node} = this.props;
		const {directAccessMetric, name} = node;

		return (
			<div className={`${CLASSNAME_BOX}-box`}>
				<div className={`${CLASSNAME_BOX}-content`}>
					<div className={`${CLASSNAME_BOX}-value`}>
						{sub(Liferay.Language.get('x-views'), [
							toThousands(directAccessMetric)
						])}
					</div>
					<div className={`${CLASSNAME_BOX}-title`}>
						<span className='text-truncate'>
							{name || Liferay.Language.get('untitled')}
						</span>
					</div>
					{!!items.length && (
						<div className={`${CLASSNAME_BOX}-show-more`}>
							{this.renderListGroup(node, items)}
						</div>
					)}
				</div>

				<div
					className={`${CLASSNAME_BOX}-square`}
					style={{backgroundColor: color}}
				>
					{toThousands(directAccessMetric)}
				</div>
			</div>
		);
	}

	/**
	 * Lifecycle Render - ReactJS
	 */
	render() {
		const {
			isDirectAccess,
			isEmptyState,
			isMain,
			items,
			onTouchpointLoaded
		} = this.props;

		if (isDirectAccess) {
			return this.renderDirectAccess(SANKEY_COLORS.bgDirectTraffic);
		} else if (isEmptyState) {
			return this.renderEmptyState(SANKEY_COLORS.bgInactive);
		} else if (isMain) {
			onTouchpointLoaded({items});

			return this.renderParentNode();
		}

		return this.renderTouchpointItem();
	}
}

export default SankeyTouchpoint;
