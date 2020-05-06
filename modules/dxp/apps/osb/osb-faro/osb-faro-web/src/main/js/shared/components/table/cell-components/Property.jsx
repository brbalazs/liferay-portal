import React from 'react';
import {getSafeDisplayValue} from 'shared/util/util';
import {PropTypes} from 'prop-types';

export default class PropertyCell extends React.Component {
	static propTypes = {
		data: PropTypes.shape({
			name: PropTypes.string,
			value: PropTypes.string
		})
	};

	render() {
		const {name, value} = this.props.data;

		return (
			<td
				className={`property-cell${
					this.props.className ? ` ${this.props.className}` : ''
				}`}
			>
				<div className='name'>{name}</div>

				<div className='table-title'>{getSafeDisplayValue(value)}</div>
			</td>
		);
	}
}
