import getCN from 'classnames';
import PropTypes from 'prop-types';
import React, {Fragment} from 'react';

const CLASSNAME = 'metric-value';

const MetricValue = ({className, type, value}) => {
	const getRegexType = type => {
		if (type === 'ratings') {
			return /([/][0-9]+)/g;
		} else {
			return /([a-zA-Z%])+/g;
		}
	};

	const formatValue = (value, regex) => {
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
	};

	return (
		<div className={getCN(CLASSNAME, className)}>
			{formatValue(value, getRegexType(type))}
		</div>
	);
};

MetricValue.defaultProps = {
	type: 'number'
};

MetricValue.propTypes = {
	className: PropTypes.string,

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

export default MetricValue;
