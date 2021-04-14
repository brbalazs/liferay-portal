import Alert, {ALERT_CONFIG_MAP, AlertTypes} from './Alert';
import getCN from 'classnames';
import React from 'react';
import {Alert as AlertType} from 'shared/types';
import {connect} from 'react-redux';
import {CSSTransition, TransitionGroup} from 'react-transition-group';
import {List, Map} from 'immutable';
import {removeAlert} from '../actions/alerts';

const {danger, info, secondary, success, warning} = ALERT_CONFIG_MAP;

type AlertDisplaysType = {
	[alertType: string]: {
		iconSymbol?: string;
		title?: string;
		type?: AlertTypes;
	};
};

const ALERT_DISPLAYS: AlertDisplaysType = {
	[AlertType.Types.Default]: info,
	[AlertType.Types.Error]: danger,
	[AlertType.Types.Pending]: secondary,
	[AlertType.Types.Success]: success,
	[AlertType.Types.Warning]: warning
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
		<TransitionGroup>
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
						<CSSTransition
							appear
							classNames='transition-slide-up'
							key={id}
							timeout={{enter: 150, exit: 150}}
						>
							<Alert
								iconSymbol={symbol}
								id={id}
								onClose={removeAlert}
								title={label}
								type={display}
							>
								{List.isList(message)
									? message.toJS()
									: message}
							</Alert>
						</CSSTransition>
					);
				})
				.toArray()}
		</TransitionGroup>
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
