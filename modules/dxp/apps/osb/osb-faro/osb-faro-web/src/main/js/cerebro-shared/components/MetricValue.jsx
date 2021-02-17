import getCN from 'classnames';
import React, {Fragment} from 'react';
import {PropTypes} from 'prop-types';

const CLASSNAME = 'metric-value';

/**
 * Metric Value
 * @class
 */
class MetricValue extends React.Component {
	static defaultProps = {
		type: 'number'
	};

	static propTypes = {
		/**
		 * @type {string}
		 * @default number
		 */
		type: PropTypes.oneOf(['number', 'percentage', 'time', 'ratings']),

		/**
		 * @type {string}
		 * @default undefined
		 */
		value: PropTypes.string.isRequired
	};

	/**
	 * Get Regex Type
	 * @param {string} type
	 */
	getRegexType(type) {
		if (type === 'ratings') {
			return /([/][0-9]+)/g;
		} else {
			return /([a-zA-Z%])+/g;
		}
	}

	/**
	 * Format Metric Value
	 * @param {string} value
	 * @param {object} regex
	 */
	formatValue(value, regex) {
		const items = value.split(' ');

		return items.map((item, i) => {
			const [value, unit] = item.split(regex);

			return (
				<Fragment key={i}>
					{value}

					<span className={`${CLASSNAME}-letter`}>{unit} </span>
				</Fragment>
			);
		});
	}

	/**
	 * Lifecycle Render - ReactJS
	 */
	render() {
		const {className, type, value} = this.props;

		return (
			<div className={getCN(CLASSNAME, className)}>
				{this.formatValue(value, this.getRegexType(type))}
			</div>
		);
	}
}

export default MetricValue;
