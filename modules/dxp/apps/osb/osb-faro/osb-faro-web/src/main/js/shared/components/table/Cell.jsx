import React from 'react';
import {PropTypes} from 'prop-types';

class Cell extends React.Component {
	static propTypes = {
		title: PropTypes.bool
	};

	render() {
		const {children, className, title} = this.props;

		return (
			<td className={className}>
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
