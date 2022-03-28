import Icon from 'shared/components/Icon';
import PropTypes from 'prop-types';
import React from 'react';

const CLASSNAME = 'analytics-trend';

const Trend = ({color, icon, label}) => (
	<div className={CLASSNAME} style={{color}}>
		{icon && <Icon symbol={icon} />}
		<span className={`${CLASSNAME}-percent mb-0`}>{label}</span>
	</div>
);

Trend.propTypes = {
	/**
	 * @type {string}
	 * @default undefined
	 */
	color: PropTypes.string,

	/**
	 * @type {string}
	 * @default undefined
	 */
	icon: PropTypes.string,

	/**
	 * @type {string}
	 * @default undefined
	 */
	label: PropTypes.string
};

export default Trend;
