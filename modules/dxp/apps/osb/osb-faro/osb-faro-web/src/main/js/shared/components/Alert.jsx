import autobind from 'autobind-decorator';
import getCN from 'classnames';
import Icon from './Icon';
import PropTypes from 'prop-types';
import React from 'react';
import {Link} from 'react-router-dom';

export const ALERT_TYPE_MAP = {
	danger: 'danger',
	info: 'info',
	secondary: 'secondary',
	success: 'success',
	warning: 'warning'
};

const {danger, info, secondary, success, warning} = ALERT_TYPE_MAP;

export const ALERT_CONFIG_MAP = {
	[danger]: {
		iconSymbol: 'exclamation-full',
		title: Liferay.Language.get('error'),
		type: danger
	},
	[info]: {
		iconSymbol: 'info-circle',
		title: Liferay.Language.get('info'),
		type: info
	},
	[secondary]: {
		iconSymbol: 'info-circle',
		title: Liferay.Language.get('pending'),
		type: secondary
	},
	[success]: {
		iconSymbol: 'check-circle-full',
		title: Liferay.Language.get('success'),
		type: success
	},
	[warning]: {
		iconSymbol: 'warning-full',
		title: Liferay.Language.get('warning'),
		type: warning
	}
};

class AlertLink extends React.Component {
	render() {
		const {children, className, ...otherProps} = this.props;

		return (
			<Link className={getCN('alert-link', className)} {...otherProps}>
				{children}
			</Link>
		);
	}
}

export default class Alert extends React.Component {
	static defaultProps = ALERT_CONFIG_MAP.info;

	static propTypes = {
		iconSymbol: PropTypes.string,
		id: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
		onClose: PropTypes.func,
		stripe: PropTypes.bool,
		title: PropTypes.string,
		type: PropTypes.oneOf(Object.values(ALERT_TYPE_MAP))
	};

	@autobind
	handleClose() {
		const {id, onClose} = this.props;

		if (onClose) {
			onClose(id);
		}
	}

	render() {
		const {
			children,
			className,
			iconSymbol,
			onClose,
			stripe,
			title,
			type
		} = this.props;

		const classes = getCN(
			'alert',
			{
				'alert-dismissible': onClose,
				'alert-fluid': stripe,
				[`alert-${type}`]: type
			},
			className
		);

		let content = (
			<>
				{iconSymbol && (
					<span className='alert-indicator'>
						<Icon symbol={iconSymbol} />
					</span>
				)}

				{title && <strong className='lead'>{`${title}:`}</strong>}

				{children}

				{onClose && (
					<button className='close' onClick={this.handleClose}>
						<Icon symbol='times' />
					</button>
				)}
			</>
		);

		if (stripe) {
			content = (
				<div className='container' data-testid='stripe	'>
					{content}
				</div>
			);
		}

		return (
			<div className={classes} role='alert'>
				{content}
			</div>
		);
	}
}

Alert.Link = AlertLink;
Alert.TYPES = ALERT_TYPE_MAP;
