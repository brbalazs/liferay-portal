import React from 'react';
import {PropTypes} from 'prop-types';

class Cell extends React.Component {
	static propTypes = {
		title: PropTypes.bool
	};

	render() {
		const {children, title} = this.props;

		return (
			<td
				className={
					this.props.className ? ` ${this.props.className}` : ''
				}
			>
				{title ? (
					<h4 className='table-title text-truncate'>{children}</h4>
				) : (
					children
				)}
			</td>
		);
	}
}

export default Cell;
