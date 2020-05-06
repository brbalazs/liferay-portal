import Circle from 'shared/components/Circle';
import getCN from 'classnames';
import omitDefinedProps from 'shared/util/omitDefinedProps';
import React from 'react';
import {PropTypes} from 'prop-types';

const CLASSNAME = 'analytics-tooltip-chart';

const COLUMN_SHAPE = PropTypes.shape({
	/**
	 * @type {string}
	 * @default undefined
	 */
	align: PropTypes.string,

	/**
	 * @type {string}
	 * @default undefined
	 */
	className: PropTypes.string,

	/**
	 * @type {string}
	 * @default undefined
	 */
	color: PropTypes.string,

	/**
	 * @type {string|function}
	 * @default undefined
	 */
	label: PropTypes.oneOfType([PropTypes.string, PropTypes.func]),

	/**
	 * Indicate if text is truncated
	 * @type {boolean}
	 * @default false
	 */
	truncated: PropTypes.bool,

	/**
	 * @type {string}
	 * @default undefined
	 */
	weight: PropTypes.string,

	/**
	 * @type {string}
	 * @default undefined
	 */
	width: PropTypes.number
});

export const TOOLTIP_PROPTYPES = {
	/**
	 * @type {array}
	 * @default undefined
	 */
	header: PropTypes.arrayOf(COLUMN_SHAPE),

	/**
	 * @type {array}
	 * @default undefined
	 */
	rows: PropTypes.arrayOf(
		PropTypes.shape({
			columns: PropTypes.arrayOf(COLUMN_SHAPE)
		})
	)
};

/**
 * Body
 * @param {object} param0
 * @memberof TooltipTmpl component
 */
const Body = ({children, className = ''}) => (
	<tbody className={`${CLASSNAME}-body ${className}`}>{children}</tbody>
);

/**
 * Column
 * @param {object} param0
 * @memberof TooltipTmpl component
 */
class Column extends React.Component {
	static defaultProps = {
		align: 'left',
		truncated: false,
		weight: 'normal'
	};

	static propTypes = {
		/**
		 * Align text in a column
		 * @type {string}
		 * @default 'left'
		 */
		align: PropTypes.oneOf(['center', 'left', 'right']),

		/**
		 * Indicate if text is truncated
		 * @type {boolean}
		 * @default false
		 */
		truncated: PropTypes.bool,

		/**
		 * Weight of text in a column
		 * @type {string}
		 * @default 'normal'
		 */
		weight: PropTypes.oneOf(['light', 'normal', 'semibold', 'bold'])
	};

	/**
	 * Lifecycle Render - ReactJS
	 */
	render() {
		const {
			align,
			children,
			className,
			truncated,
			weight,
			...otherProps
		} = this.props;
		const classes = getCN(`${CLASSNAME}-column`, className, {
			[`text-${align}`]: align,
			[`font-weight-${weight}`]: weight
		});

		return (
			<td {...omitDefinedProps(otherProps, Column.propTypes)}>
				<div className={`${CLASSNAME}-content ${classes}`}>
					{truncated ? (
						<div className={`${CLASSNAME}-truncated`}>
							{children}
						</div>
					) : (
						children
					)}
				</div>
			</td>
		);
	}
}

/**
 * Header
 * @param {object} param0
 * @memberof TooltipTmpl component
 */
const Header = ({children, className = ''}) => (
	<thead className={`${CLASSNAME}-header ${className}`}>{children}</thead>
);

/**
 * Row
 * @param {object} param0
 * @memberof TooltipTmpl component
 */
const Row = ({children, className = ''}) => (
	<tr className={`${CLASSNAME}-row ${className}`}>{children}</tr>
);

/**
 * TooltipTmpl
 * @class
 * @memberof TooltipChart component
 */
class TooltipTmpl extends React.Component {
	/**
	 * Lifecycle Render - ReactJS
	 */
	render() {
		const {children, className} = this.props;
		return (
			<table className={getCN(CLASSNAME, className)}>{children}</table>
		);
	}
}

TooltipTmpl.Body = Body;
TooltipTmpl.Column = Column;
TooltipTmpl.Header = Header;
TooltipTmpl.Row = Row;

/**
 * Tooltip Chart
 * @class
 */
class TooltipChart extends React.Component {
	static propTypes = TOOLTIP_PROPTYPES;

	renderLabel(label) {
		if (typeof label === 'function') {
			return label();
		}

		return label;
	}

	/**
	 * Render Column
	 * @param {object} column
	 * @param {string} className
	 * @param {number} index
	 */
	renderColumn(column, className = '', index = 0) {
		return (
			<TooltipTmpl.Row className={className} key={`rows-${index}`}>
				{column.map(
					(
						{
							align,
							className,
							color,
							colspan,
							label,
							truncated,
							weight,
							width
						},
						index
					) => (
						<TooltipTmpl.Column
							align={align}
							className={className}
							colSpan={colspan}
							key={`column-${index}`}
							style={width && {minWidth: `${width}px`}}
							truncated={truncated}
							weight={weight}
						>
							{color && <Circle color={color} />}{' '}
							{this.renderLabel(label)}
						</TooltipTmpl.Column>
					)
				)}
			</TooltipTmpl.Row>
		);
	}

	/**
	 * Render Header
	 * @param {object} header
	 */
	renderHeader(header) {
		return (
			<TooltipTmpl.Header>{this.renderColumn(header)}</TooltipTmpl.Header>
		);
	}

	/**
	 * Render Header
	 * @param {object} rows
	 */
	renderRows(rows) {
		return (
			<TooltipTmpl.Body>
				{rows.map(({className, columns}, index) =>
					this.renderColumn(columns, className, index)
				)}
			</TooltipTmpl.Body>
		);
	}

	/**
	 * Lifecycle Render - ReactJS
	 */
	render() {
		const {className, header, rows} = this.props;

		return (
			<TooltipTmpl className={className}>
				{!!header && this.renderHeader(header)}
				{!!rows && this.renderRows(rows)}
			</TooltipTmpl>
		);
	}
}

export {TooltipChart, TooltipTmpl};
export default TooltipChart;
