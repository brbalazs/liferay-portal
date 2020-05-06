import Icon from 'shared/components/Icon';
import Item from '../components/Item';
import React from 'react';
import Row from '../components/Row';

class IconKit extends React.Component {
	render() {
		return (
			<div
				className={
					this.props.className ? ` ${this.props.className}` : ''
				}
			>
				<Row>
					{Icon.SIZES.map((size, index) => (
						<Item key={index}>
							<Icon size={size} symbol='dxp-contacts' />
						</Item>
					))}
				</Row>

				<Row>
					{Icon.SIZES.map((size, index) => (
						<Item key={index}>
							<Icon
								monospaced
								size={size}
								symbol='dxp-contacts'
							/>
						</Item>
					))}
				</Row>
			</div>
		);
	}
}

export default IconKit;
