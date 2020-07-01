import autobind from 'autobind-decorator';
import BasePage from 'shared/components/base-page';
import React from 'react';
import {isEmpty} from 'lodash';
import {linkHorizontal} from 'd3-shape';
import {nextColor} from 'shared/util/charts';
import {PropTypes} from 'prop-types';
import {sankey} from 'd3-sankey';

const CLASSNAME = 'analytics-sankey';

const SANKEY_COLORS = {
	bgDirectTraffic: '#50D2A0',
	bgGray: '#D7D7DA',
	bgPage: '#F1F2F5',
	bgShapeMain: '#FFFFFF',
	directTraffic: '#000000',
	link: '#B5B5B5',
	title: '#000000',
	views: '#6C6C76'
};

/**
 * Sankey
 * @class
 */
class Sankey extends React.Component {
	static contextType = BasePage.Context;

	static defaultProps = {
		baseHeight: 720,
		filters: {},
		nodePadding: 100,
		nodeWidth: 200,
		parentNodeHeight: 220
	};

	static propTypes = {
		baseHeight: PropTypes.number,
		data: PropTypes.shape({
			links: PropTypes.arrayOf(
				PropTypes.shape({
					source: PropTypes.number,
					target: PropTypes.number,
					value: PropTypes.number
				})
			).isRequired,
			nodes: PropTypes.arrayOf(
				PropTypes.shape({
					directAccessMetric: PropTypes.number,
					indirectAccessMetric: PropTypes.number,
					name: PropTypes.string,
					url: PropTypes.string
				})
			).isRequired
		}).isRequired,
		filters: PropTypes.object,
		height: PropTypes.any,
		nodePadding: PropTypes.any,
		nodeWidth: PropTypes.number,
		onHeightChange: PropTypes.func,
		parentNodeHeight: PropTypes.number,
		rangeSelectors: PropTypes.object.isRequired,
		renderTouchpointComponent: PropTypes.any,
		width: PropTypes.any
	};

	state = {
		activeIndex: -1,
		expandedTouchpoint: {},
		internalData: {
			links: [],
			nodes: []
		},
		loading: false,
		mainTouchpointItems: []
	};

	/**
	 * Lifecycle Constructor - ReactJS
	 */
	constructor(props) {
		super(props);

		this._elementRef = React.createRef();
		this._svgRef = React.createRef();
	}

	/**
	 * Lifecycle Component Did Mount - ReactJS
	 */
	componentDidMount() {
		const {data} = this.props;

		if (data && data.nodes && data.links) {
			this.setState({
				internalData: this.setInternalData(this.props.data)
			});
		}
	}

	/**
	 * Lifecycle UNSAFE Component Will Receive Props - ReactJS
	 */
	// eslint-disable-next-line camelcase
	UNSAFE_componentWillReceiveProps({data}) {
		const {expandedTouchpoint} = this.state;

		if (data !== this.props.data.newVal && isEmpty(expandedTouchpoint)) {
			this.loading();
		}

		if (this._elementRef.current && this.inDocument) {
			this.setState({
				internalData: this.setInternalData(this.props.data)
			});
		}
	}

	/**
	 * Lifecycle Component Did Update - ReactJS
	 */
	componentDidUpdate(prevProps) {
		const {
			props: {data, onHeightChange},
			state: {expandedTouchpoint}
		} = this;

		onHeightChange &&
			onHeightChange({
				isExpanded: expandedTouchpoint.index,
				sankeyElement: this._svgRef.current
			});

		if (prevProps.data !== data) {
			if (data && data.nodes && data.links) {
				this.setState({
					internalData: this.setInternalData(data)
				});
			}
		}
	}

	/**
	 * Get Bounds
	 */
	getBounds() {
		if (this._svgRef.current) {
			const bounds = this._svgRef.current.getBoundingClientRect();

			return bounds;
		}

		return {
			width: this.props.width
		};
	}

	/**
	 * Set Internal Data
	 * @param {object} data
	 */
	setInternalData(data) {
		const {baseHeight, nodePadding, nodeWidth} = this.props;
		const {links, nodes} = data;

		const bounds = this.getBounds();
		const {width} = bounds;

		let sankeyHeightBase = baseHeight;

		if (nodes.length && nodes.length <= 2) {
			sankeyHeightBase = 400;
		}

		const xMargin = 100;
		const yMargin = 60;

		const sankeyData = {
			links: links.map(link => ({...link})),
			nodes: nodes.map(node => ({...node}))
		};

		const makeSankey = sankey(sankeyData)
			.iterations(0)
			.nodeWidth(nodeWidth)
			.nodePadding(nodePadding)
			.extent([
				[xMargin, yMargin],
				[width - xMargin, sankeyHeightBase - yMargin]
			]);

		return makeSankey(sankeyData);
	}

	/**
	 * Get Delta Y
	 */
	@autobind
	getDeltaY() {
		const {parentNodeHeight} = this.props;
		const parentNode = this.getParentNode();
		const currentCenter = parentNode.y0 + parentNodeHeight / 2;

		return Math.abs(this.getCenterY() - currentCenter);
	}

	/**
	 * Render Paths
	 */
	renderPaths() {
		const {
			expandedTouchpoint,
			internalData,
			mainTouchpointItems
		} = this.state;
		const margin = 20;
		const mainTouchpoint =
			internalData.nodes[internalData.nodes.length - 1];

		let marginY = 0;

		if (!internalData.nodes.length) return;

		let offsetY1 =
			this.calcSankeyNodePositionByAssetList(
				mainTouchpoint,
				mainTouchpoint.y0,
				mainTouchpointItems
			).calculatedY0 -
			margin * 4.15;

		if (expandedTouchpoint.index == mainTouchpoint.index) {
			offsetY1 =
				this.calcExpandedTouchpointPosition(
					internalData.nodes[internalData.nodes.length - 1]
				) - margin;
		}

		return internalData.links.map((link, index) => {
			const node = internalData.nodes[index];
			const {source, target, width, y0, y1} = link;

			let {calculatedY0} = this.calcSankeyNodePositionByAssetList(
				node,
				link.y0
			);

			if (index == expandedTouchpoint.index) {
				calculatedY0 =
					y0 + this.calcExpandedTouchpointPosition(node) + 3;
			}

			if (internalData.nodes.length && internalData.nodes.length <= 2) {
				marginY = 31;
			} else if (
				internalData.nodes.length &&
				internalData.nodes.length == 3
			) {
				marginY = -89;
			}

			const sankeyLinkHorizontal = linkHorizontal()
				.source(() => [source.x1, calculatedY0 + 4])
				.target(() => [
					target.x0,
					y1 + offsetY1 + this.getDeltaY() + marginY
				]);

			return (
				<path
					className={`${CLASSNAME}-path`}
					d={sankeyLinkHorizontal()}
					data-index={index}
					key={`${index}_path`}
					onMouseEnter={this.handleMouseEnter}
					onMouseLeave={this.handleMouseLeave}
					stroke={this.getNodeColor(node)}
					strokeOpacity={0.4}
					strokeWidth={Math.max(1, width)}
				/>
			);
		});
	}

	/**
	 * Get Node Color
	 * @param {array} node
	 */
	getNodeColor(node) {
		const {color, index} = node;
		const {activeIndex} = this.state;

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
	 * Handle Mouse Enter
	 * @param {object} param0
	 */
	@autobind
	handleMouseEnter({currentTarget}) {
		this.setState({
			activeIndex: parseInt(currentTarget.dataset.index)
		});
	}

	/**
	 * Handle Mouse Leave
	 */
	@autobind
	handleMouseLeave() {
		this.setState({
			activeIndex: -1
		});
	}

	/**
	 * Calculate Sankey Node Position By AssetList
	 * @param {object} node
	 * @param {number} y0
	 */
	@autobind
	calcSankeyNodePositionByAssetList(node, y0) {
		const margin = 13;
		const {expandedTouchpoint} = this.state;
		let assetsHeight;

		if (
			typeof expandedTouchpoint.index == 'number' &&
			node.index > expandedTouchpoint.index
		) {
			assetsHeight = (expandedTouchpoint.items.length + 1) * 31.6;
			assetsHeight += margin;
		} else {
			assetsHeight = 20;
			assetsHeight += margin;
		}

		const calculatedPosition = y0 + assetsHeight;

		return {
			calculatedY0: calculatedPosition,
			...node
		};
	}

	/**
	 * Calculate Expanded Touchpoint Position
	 * @param {object} param0
	 */
	@autobind
	calcExpandedTouchpointPosition({index}) {
		const {expandedTouchpoint} = this.state;

		let y = 30;

		if (index == expandedTouchpoint.index) {
			y = expandedTouchpoint.items.length * 32;
			y += 40;
		}

		return y;
	}

	/**
	 * Handle Change Touchpoint Index
	 * @param {object} param0
	 */
	@autobind
	handleChangeTouchpointIndex({index, items}) {
		this.setState({
			expandedTouchpoint: {
				index,
				items
			}
		});

		return false;
	}

	/**
	 * Get Center Y
	 */
	getCenterY() {
		const {nodes} = this.state.internalData;

		const minAndMax = nodes.reduce((result, {y0, y1}) => {
			result[0] =
				y0 < result[0] || result[0] === undefined ? y0 : result[0];
			result[1] =
				y1 > result[1] || result[1] === undefined ? y1 : result[1];
			return result;
		}, []);

		return (minAndMax[0] + minAndMax[1]) / 2;
	}

	/**
	 * Get Parent Node
	 */
	getParentNode() {
		const {nodes} = this.state.internalData;

		return nodes.find(node => this.isParentNode(node));
	}

	/**
	 * Is Parent Node
	 * @param {object} node
	 */
	isParentNode(node) {
		return Object.prototype.hasOwnProperty.call(node, 'directAccessMetric');
	}

	/**
	 * Change Path State
	 * @param {object} param0
	 */
	@autobind
	changePathState({items}) {
		const {mainTouchpointItems} = this.state;

		if (!mainTouchpointItems) {
			this.setState({
				mainTouchpointItems: items
			});
		}
	}

	/**
	 * Render Touchpoint Items
	 * @param {array} nodes
	 * @param {object} currentNode
	 */
	renderTouchpointItems(nodes, currentNode, links = []) {
		const {activeIndex, expandedTouchpoint} = this.state;
		const {rangeSelectors, renderTouchpointComponent} = this.props;
		const {router} = this.context;

		const touchpointProps = {
			activeIndex,
			calcExpandedTouchpointPositionFn: this
				.calcExpandedTouchpointPosition,
			calcSankeyNodePositionFn: this.calcSankeyNodePositionByAssetList,
			expandedTouchpointIndex: expandedTouchpoint.index,
			getDeltaYFn: this.getDeltaY,
			node: currentNode,
			onMouseEnter: this.handleMouseEnter,
			onMouseLeave: this.handleMouseLeave,
			onTouchpointIndexChange: this.handleChangeTouchpointIndex,
			onTouchpointLoaded: this.changePathState,
			rangeSelectors,
			router,
			touchpoint: currentNode.url,
			touchpointList: nodes
		};

		if (this.isParentNode(currentNode)) {
			return renderTouchpointComponent({
				...touchpointProps,
				hasOnlyOneReferrer: links.length === 1,
				isMain: true,
				key: currentNode.url,
				update: true
			});
		}

		return renderTouchpointComponent({
			...touchpointProps,
			key: currentNode.url
		});
	}

	renderTouchpointDirectAccess(node) {
		const {rangeSelectors, renderTouchpointComponent} = this.props;
		const {router} = this.context;

		return renderTouchpointComponent({
			calcExpandedTouchpointPositionFn: this
				.calcExpandedTouchpointPosition,
			calcSankeyNodePositionFn: this.calcSankeyNodePositionByAssetList,
			getDeltaYFn: this.getDeltaY,
			isDirectAccess: true,
			isMain: true,
			key: node.url,
			node,
			rangeSelectors,
			router,
			touchpoint: node.url,
			touchpointList: [],
			update: true
		});
	}

	renderEmptyState(node) {
		const {rangeSelectors, renderTouchpointComponent} = this.props;
		const {router} = this.context;

		return renderTouchpointComponent({
			isEmptyState: true,
			node,
			rangeSelectors,
			router
		});
	}

	/**
	 * Render Nodes
	 */
	renderNodes() {
		const {internalData} = this.state;
		const {links, nodes} = internalData;

		return nodes.map((node, index) => {
			const {x0, x1, y0, y1} = node;

			return (
				<g className={`${CLASSNAME}-box`} key={`${index}_node`}>
					{this.renderTouchpointItems(
						nodes,
						{
							...node,
							x0: x0 || 0,
							x1: x1 || 0,
							y0: y0 || 0,
							y1: y1 || 0
						},
						links
					)}
				</g>
			);
		});
	}

	/**
	 * Get Sankey Height
	 * @param {number} height
	 */
	getSankeyHeight(height) {
		const {internalData} = this.state;

		if (
			height < 720 &&
			internalData.nodes.length &&
			internalData.nodes.length > 2
		) {
			return 720;
		}

		return height;
	}

	/**
	 * Loading
	 */
	loading() {
		this.setState({
			loading: true
		});

		setTimeout(() => {
			this.setState({
				loading: false
			});
		}, 125);
	}

	/**
	 * Lifecycle Render - ReactJS
	 */
	render() {
		const {loading, mainTouchpointItems} = this.state;
		const {data, height, width} = this.props;
		const {links, nodes} = data;
		const sankeyHeight = this.getSankeyHeight(height);
		const parentNode = nodes.find(node => this.isParentNode(node));

		if (parentNode && !loading) {
			if (parentNode.directAccessMetric === 0 && !links.length) {
				return (
					<div ref={this._svgRef} width={width}>
						{this.renderEmptyState(parentNode)}
					</div>
				);
			} else if (links.length === 0) {
				return (
					<div ref={this._svgRef} width={width}>
						{this.renderTouchpointDirectAccess(parentNode)}
					</div>
				);
			} else {
				return (
					<svg
						className={CLASSNAME}
						height={sankeyHeight}
						ref={this._svgRef}
						width={width}
					>
						{mainTouchpointItems && (
							<g fill={'none'} ref={'linksGroup'}>
								{this.renderPaths()}
							</g>
						)}
						<g className={'svg'}>{this.renderNodes()}</g>
					</svg>
				);
			}
		}

		return <div ref={this._svgRef} width={width} />;
	}
}

export default Sankey;
