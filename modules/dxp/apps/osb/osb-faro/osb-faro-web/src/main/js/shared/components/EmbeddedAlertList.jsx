import Alert, {ALERT_CONFIG_MAP} from 'shared/components/Alert';
import getCN from 'classnames';
import React from 'react';
import {PropTypes} from 'prop-types';

export default class EmbeddedAlertList extends React.Component {
	static defaultProps = {
		alerts: []
	};

	static propTypes = {
		alerts: PropTypes.array
	};

	render() {
		const {alerts, className} = this.props;

		return (
			<div className={getCN('embedded-alert-list-root', className)}>
				{alerts.map(({alertType, message, ...otherParams}, i) => {
					const alertConfig = alertType
						? ALERT_CONFIG_MAP[alertType]
						: {};

					return (
						<Alert {...alertConfig} key={i} {...otherParams}>
							{message}
						</Alert>
					);
				})}
			</div>
		);
	}
}
