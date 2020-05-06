import Alert, {ALERT_CONFIG_MAP} from './Alert';
import getCN from 'classnames';
import React from 'react';
import {Alert as AlertType} from 'shared/types';
import {alertTypes, removeAlert} from '../actions/alerts';
import {connect} from 'react-redux';
import {CSSTransitionGroup} from 'react-transition-group';
import {List, Map} from 'immutable';

const {danger, info, secondary, success, warning} = ALERT_CONFIG_MAP;

type AlertDisplaysType = {
	[alertType: string]: {
		display?: string;
		iconSymbol?: string;
		title?: string;
		type?: string;
	};
};

const ALERT_DISPLAYS: AlertDisplaysType = {
	[alertTypes.DEFAULT]: info,
	[alertTypes.ERROR]: danger,
	[alertTypes.PENDING]: secondary,
	[alertTypes.SUCCESS]: success,
	[alertTypes.WARNING]: warning
};

interface IAlertFeedProps extends React.HTMLAttributes<HTMLElement> {
	alertsIMap: Map<string, Map<string, any>>;
	modalActive?: boolean;
	removeAlert: AlertType.RemoveAlert;
}

export const AlertFeed: React.FC<IAlertFeedProps> = ({
	alertsIMap,
	className,
	modalActive = false,
	removeAlert
}) => (
	<div
		className={getCN(className, 'alert-feed-root alert-notifications', {
			'modal-active': modalActive
		})}
	>
		<CSSTransitionGroup
			transitionAppear
			transitionAppearTimeout={150}
			transitionEnterTimeout={150}
			transitionLeaveTimeout={150}
			transitionName='transition-slide-up'
		>
			{alertsIMap
				.map(alertIMap => {
					const {
						iconSymbol: symbol,
						title: label,
						type: display
					} = ALERT_DISPLAYS[alertIMap.get('alertType')];

					const id = alertIMap.get('id');
					const message = alertIMap.get('message');

					return (
						<Alert
							iconSymbol={symbol}
							id={id}
							key={id}
							notification={alertIMap.get('notification')}
							onClose={removeAlert}
							title={label}
							type={display}
						>
							{List.isList(message) ? message.toJS() : message}
						</Alert>
					);
				})
				.toArray()}
		</CSSTransitionGroup>
	</div>
);

export default connect(
	state => ({
		alertsIMap: state.get('alerts'),
		modalActive: state.get('modals').size > 0
	}),
	{
		removeAlert
	}
)(AlertFeed);
