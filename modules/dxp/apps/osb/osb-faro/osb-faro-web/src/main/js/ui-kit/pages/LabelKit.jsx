import Item from '../components/Item';
import Label from 'shared/components/Label';
import React from 'react';
import Row from '../components/Row';

class LabelKit extends React.Component {
	render() {
		return (
			<div
				className={
					this.props.className ? ` ${this.props.className}` : ''
				}
			>
				<Row>
					{Label.DISPLAYS.map((display, index) => (
						<Item key={index}>
							<Label display={display}>{'Label'}</Label>
						</Item>
					))}
				</Row>
				<Row>
					{Label.DISPLAYS.map((display, index) => (
						<Item key={index}>
							<Label display={display}>{'Label'}</Label>
						</Item>
					))}
				</Row>
				<Row>
					{Label.SIZES.map((size, index) => (
						<Item key={index}>
							<Label size={size}>{'Label'}</Label>
						</Item>
					))}
				</Row>
			</div>
		);
	}
}

export default LabelKit;
