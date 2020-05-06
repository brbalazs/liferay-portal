import React from 'react';

class Item extends React.Component {
	render() {
		return (
			<div
				className={`kit-item-root${
					this.props.className ? ` ${this.props.className}` : ''
				}`}
			>
				{this.props.children}
			</div>
		);
	}
}

export default Item;
