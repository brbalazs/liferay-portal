import React, {Component} from 'react';
import PropTypes from 'prop-types';
import getCN from 'classnames';

import Icon from './Icon';

class NoResultsDisplay extends Component {
	render() {
		const {
			horizontal,
			icon,
			message,
			multiplier,
			size,
			title
		} = this.props;

		const backgroundClass = getCN('no-results-background', `${size}-${multiplier}x`);
		const classes = getCN('no-results-container text-center', {horizontal});

		return (
			<div className={classes}>
				<div className={backgroundClass}>
					<Icon multiplier={multiplier} name={icon} size={size}/>
				</div>

				{title && !horizontal &&
				<div className="title">
					{title}
				</div>
				}

				{message &&
				<div className="message">
					{message}
				</div>
				}
			</div>
		);
	}
}

NoResultsDisplay.defaultProps = {
	horizontal: false,
	icon: 'streams',
	multiplier: 2,
	size: 'large',

};

NoResultsDisplay.propTypes = {
	horizontal: PropTypes.bool,
	icon: PropTypes.string,
	message: PropTypes.any,
	multiplier: PropTypes.number,
	size: PropTypes.oneOf([
		'large',
		'small',
		'smallest'
	]),
	title: PropTypes.oneOfType([
		PropTypes.array,
		PropTypes.string
	])
};

export default NoResultsDisplay;
