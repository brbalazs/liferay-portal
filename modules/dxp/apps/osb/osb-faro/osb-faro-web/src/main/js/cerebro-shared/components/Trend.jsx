import Icon from 'shared/components/Icon';
import React from 'react';
import {PropTypes} from 'prop-types';

const CLASSNAME = 'analytics-trend';

/**
 * Trend
 * @class
 */
class Trend extends React.Component {
	static propTypes = {
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

	/**
	 * Lifecycle Render - ReactJS
	 */
	render() {
		const {color, icon, label} = this.props;

		return (
			<div className={CLASSNAME} style={{color}}>
				{icon && <Icon symbol={icon} />}
				<span className={`${CLASSNAME}-percent mb-0`}>{label}</span>
			</div>
		);
	}
}

export default Trend;
