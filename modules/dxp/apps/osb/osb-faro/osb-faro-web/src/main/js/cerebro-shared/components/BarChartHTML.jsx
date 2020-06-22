import autobind from 'autobind-decorator';
import Circle from 'shared/components/Circle';
import getCN from 'classnames';
import Icon from 'shared/components/Icon';
import React from 'react';
import ReactDOM from 'react-dom';
import TooltipChart, {
	TOOLTIP_PROPTYPES
} from 'cerebro-shared/components/TooltipChart';
import {getAxisMeasuresFromData} from 'shared/util/charts';
import {getPercentage} from 'shared/util/util';
import {hasChanges} from 'shared/util/react';
import {PropTypes} from 'prop-types';
import {toRounded, toThousands} from 'shared/util/numbers';

const CLASSNAME = 'analytics-bar-chart-html';

const LABEL_TYPE = PropTypes.oneOfType([PropTypes.string, PropTypes.func]);

const COLUMN_SHAPE = PropTypes.arrayOf(
	PropTypes.shape({
		color: PropTypes.string,
		icon: PropTypes.string,
		label: LABEL_TYPE
	})
);

const GRID_SHAPE = PropTypes.shape({
	formatter: PropTypes.func,
	maxValue: PropTypes.number,
	minValue: PropTypes.number,
	precision: PropTypes.number,
	show: PropTypes.bool,
	type: PropTypes.oneOf(['number', 'percentage'])
});

const PROGRESS_SHAPE = PropTypes.arrayOf(
	PropTypes.shape({
		color: PropTypes.string.isRequired,
		value: PropTypes.oneOfType([PropTypes.number, PropTypes.string])
			.isRequired
	})
);

const ITEMS_SHAPE = PropTypes.arrayOf(
	PropTypes.shape({
		columns: COLUMN_SHAPE,
		expanded: PropTypes.bool,
		intervals: PropTypes.arrayOf(
			PropTypes.shape({
				end: PropTypes.number,
				start: PropTypes.number
			})
		),
		items: PropTypes.array,
		progress: PROGRESS_SHAPE,
		showControls: PropTypes.bool,
		tooltip: PropTypes.shape(TOOLTIP_PROPTYPES)
	}).isRequired
);

class BarChartHTML extends React.Component {
	static defaultProps = {
		disableScroll: false,
		formatSpacement: true,
		grid: {
			formatter: value => value,
			precision: 1,
			show: false,
			type: 'number'
		}
	};

	static propTypes = {
		disableScroll: PropTypes.bool,
		formatSpacement: PropTypes.bool,
		grid: GRID_SHAPE,
		header: COLUMN_SHAPE,
		items: ITEMS_SHAPE
	};

	state = {
		items: [],
		showArrowDownIcon: false,
		showList: false,
		tooltip: {
			header: [],
			position: {
				left: 0,
				top: 0
			},
			rows: [],
			show: false
		}
	};

	constructor(props) {
		super(props);

		this.state = {
			...this.state,
			items: props.items
		};

		this._groupItemsRef = React.createRef();
		this._tooltipRef = React.createRef();
	}

	componentDidUpdate(prevProps) {
		if (hasChanges(prevProps, this.props, 'items')) {
			this.setState({
				items: this.props.items,
				showArrowDownIcon: this.showArrowDownIcon(
					this._groupItemsRef.current
				)
			});
		}
	}

	@autobind
	handleClickToggleList({currentTarget}) {
		const {items} = this.state;
		const {index} = currentTarget.dataset;

		items[index].expanded = !items[index].expanded;

		this.setState({items});
	}

	@autobind
	handleScrollItems({target}) {
		this.setState({
			showArrowDownIcon: this.showArrowDownIcon(target)
		});
	}

	@autobind
	handleMouseEnterItem({header, rows}) {
		this.setState({
			tooltip: {
				...this.state.tooltip,
				header,
				rows,
				show: true
			}
		});
	}

	@autobind
	handleMouseMoveItem(event) {
		if (!this.state.tooltip.show) return;

		const {clientHeight, clientWidth} = this._tooltipRef.current;
		const {left, top} = this.alignTooltip(event, clientWidth, clientHeight);

		this._tooltipRef.current.style.left = `${left}px`;
		this._tooltipRef.current.style.top = `${top}px`;
	}

	@autobind
	handleMouseLeaveItem() {
		this.setState({
			tooltip: {
				...this.state.tooltip,
				show: false
			}
		});
	}

	getIntervals() {
		const {maxValue, minValue} = this.props.grid;
		const {intervals} = getAxisMeasuresFromData([
			'data1',
			minValue,
			maxValue
		]);

		return intervals;
	}

	getProgressWidth(value) {
		const {show} = this.props.grid;
		let width = value;

		if (typeof value === 'number' && show) {
			const intervals = this.getIntervals();

			width = `${toRounded(
				getPercentage(value, intervals[intervals.length - 1])
			)}%`;
		}

		return width;
	}

	getIntervalWidth({end, start}) {
		const {show} = this.props.grid;
		let width = end - start;

		if (typeof start === 'number' && show) {
			const intervals = this.getIntervals();

			const startPosition = `${toRounded(
				getPercentage(start, intervals[intervals.length - 1])
			)}`;

			const endPosition = `${toRounded(
				getPercentage(end, intervals[intervals.length - 1])
			)}`;

			width = `${endPosition - startPosition}%`;
		}

		return width;
	}
	getIntervalStartPosition(start) {
		const {show} = this.props.grid;
		let startPosition = start;

		if (typeof start === 'number' && show) {
			const intervals = this.getIntervals();

			startPosition = `${toRounded(
				getPercentage(start, intervals[intervals.length - 1])
			)}%`;
		}

		return startPosition;
	}

	showArrowDownIcon(element) {
		const {clientHeight, offsetHeight, scrollHeight, scrollTop} = element;

		if (
			scrollHeight > clientHeight &&
			offsetHeight + scrollTop !== scrollHeight
		) {
			return true;
		}

		return false;
	}

	hasItems(items) {
		return items && !!items.length;
	}

	renderLabel(label) {
		if (typeof label === 'function') {
			return label();
		}

		return <span>{label}</span>;
	}

	renderIcon({color, icon}) {
		if (color) {
			return (
				<Circle color={color} size={32}>
					<Icon symbol={icon} />
				</Circle>
			);
		}

		return <Icon className={`${CLASSNAME}-icon`} symbol={icon} />;
	}

	renderColumn({color, icon, index, label}) {
		return (
			<div className={`${CLASSNAME}-column`} key={index}>
				{icon && this.renderIcon({color, icon})}
				<div className={'text-truncate w-100'}>
					{this.renderLabel(label)}
				</div>
			</div>
		);
	}

	renderHeader({
		columns,
		expanded,
		index,
		intervals,
		items,
		progress,
		showControls
	}) {
		return (
			<div className={`${CLASSNAME}-header`}>
				{progress && this.renderProgress(progress)}
				{intervals && this.renderInterval(intervals)}

				{this.hasItems(columns) && (
					<div>
						{columns.map((column, index) =>
							this.renderColumn({...column, index})
						)}
					</div>
				)}

				{this.hasItems(items) &&
					showControls &&
					this.renderButton(index, expanded)}
			</div>
		);
	}

	renderProgressItem({color, index, value}) {
		const width = this.getProgressWidth(value);

		return <div key={index} style={{backgroundColor: color, width}} />;
	}

	renderProgress(progress) {
		return (
			<div className={`${CLASSNAME}-progress`}>
				{this.hasItems(progress) &&
					progress.map((progress, index) =>
						this.renderProgressItem({...progress, index})
					)}
			</div>
		);
	}

	renderIntervalItem({end, index, start}) {
		const startPosition = this.getIntervalStartPosition(start);
		const width = this.getIntervalWidth({end, start});

		return <div key={index} style={{marginLeft: startPosition, width}} />;
	}

	renderInterval(intervals) {
		return (
			<div className={`${CLASSNAME}-interval`}>
				{this.hasItems(intervals) &&
					intervals.map((interval, index) =>
						this.renderIntervalItem({...interval, index})
					)}
			</div>
		);
	}
	renderItem({
		columns,
		expanded,
		index,
		intervals,
		items,
		progress,
		showControls,
		tooltip
	}) {
		let params = {
			className: `${CLASSNAME}-item`,
			key: index
		};

		if (tooltip) {
			params = {
				...params,
				onBlur: () => false,
				onFocus: () => false,
				onMouseEnter: () => this.handleMouseEnterItem(tooltip),
				onMouseLeave: this.handleMouseLeaveItem,
				onMouseMove: this.handleMouseMoveItem
			};
		}

		return (
			<li {...params}>
				{this.renderHeader({
					columns,
					expanded,
					index,
					intervals,
					items,
					progress,
					showControls
				})}

				{this.hasItems(items) && expanded && this.renderItems(items)}
			</li>
		);
	}

	renderButton(index, expanded) {
		return (
			<button
				className={`${CLASSNAME}-button`}
				data-index={index}
				onClick={this.handleClickToggleList}
			>
				<Icon className={'icon'} symbol={expanded ? 'hr' : 'plus'} />
			</button>
		);
	}

	renderItems(items) {
		return (
			<ul className={`${CLASSNAME}-items`}>
				{this.hasItems(items) &&
					items.map(
						(
							{
								columns,
								expanded,
								intervals,
								items,
								progress,
								showControls,
								tooltip
							},
							index
						) =>
							this.renderItem({
								columns,
								expanded,
								index,
								intervals,
								items,
								progress,
								showControls,
								tooltip
							})
					)}
			</ul>
		);
	}

	alignTooltip({pageX, pageY}, width, height) {
		const arrowPopoverSize = 6;
		const tooltipDistance = 15;

		return {
			left: pageX - width / 2,
			top: pageY - height - arrowPopoverSize - tooltipDistance
		};
	}

	renderTooltip() {
		const {header, position, rows} = this.state.tooltip;
		const {left, top} = position;

		return ReactDOM.createPortal(
			<div
				className={`${CLASSNAME}-tooltip bb-tooltip-container`}
				ref={this._tooltipRef}
				style={{left, top}}
			>
				<TooltipChart header={header} rows={rows} />
			</div>,
			document.querySelector('body.dxp')
		);
	}

	renderArrowDownIcon() {
		return (
			<Icon className={'icon text-l-secondary '} symbol={'angle-down'} />
		);
	}

	renderGrid() {
		const intervals = this.getIntervals();

		return (
			<div className={`${CLASSNAME}-grid`}>
				{intervals.map((interval, index) => (
					<div className={`${CLASSNAME}-grid-item`} key={index}>
						{this.renderGridContent(interval)}
					</div>
				))}
			</div>
		);
	}

	renderGridContent(value) {
		const {formatter, precision, type} = this.props.grid;

		if (type === 'percentage') {
			return <span>{`${toRounded(value, precision)}%`}</span>;
		}

		return <span>{formatter(toThousands(value))}</span>;
	}

	render() {
		const {
			disableScroll,
			formatSpacement,
			grid: {show: showGrid},
			header
		} = this.props;
		const {items, showArrowDownIcon, tooltip} = this.state;

		const classes = getCN(CLASSNAME, {
			'disable-scroll': disableScroll,
			'format-spacement': formatSpacement
		});

		return (
			<div className={classes}>
				{showGrid && this.renderGrid()}

				{header && this.renderHeader({columns: header})}

				<div
					className={`${CLASSNAME}-group-items`}
					onScroll={this.handleScrollItems}
					ref={this._groupItemsRef}
				>
					{this.renderItems(items)}
				</div>

				{showArrowDownIcon && this.renderArrowDownIcon()}

				{tooltip.show && this.renderTooltip(tooltip)}
			</div>
		);
	}
}

export default BarChartHTML;
