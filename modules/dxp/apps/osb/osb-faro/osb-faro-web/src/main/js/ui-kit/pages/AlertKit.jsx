import Alert from 'shared/components/Alert';
import Item from '../components/Item';
import React from 'react';
import {noop, values} from 'lodash';

export default class AlertKit extends React.Component {
	render() {
		return (
			<div
				className={
					this.props.className ? ` ${this.props.className}` : ''
				}
			>
				<div>
					{values(Alert.TYPES).map(type => (
						<Item key={type}>
							<Alert type={type}>{type}</Alert>
						</Item>
					))}
				</div>

				<Item>
					<Alert title='Basic Alert' type={Alert.TYPES.success}>
						{'This is a basic alert.'}
					</Alert>
				</Item>

				<Item>
					<Alert
						onClose={noop}
						title='Dismissable Alert'
						type={Alert.TYPES.success}
					>
						{'This is a dismissable alert.'}
					</Alert>
				</Item>

				<Item>
					<Alert title='Alert with a link' type={Alert.TYPES.success}>
						{'Check out this link '}
						<Alert.Link to='#'>{'click me'}</Alert.Link>
					</Alert>
				</Item>

				<Item>
					<Alert
						stripe
						title='Alert Stripe'
						type={Alert.TYPES.success}
					>
						{'Check out this Alert Stripe.'}
					</Alert>
				</Item>
			</div>
		);
	}
}
