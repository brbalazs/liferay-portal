import getCN from 'classnames';
import React from 'react';
import {PropTypes} from 'prop-types';

export default class AccountNames extends React.Component {
	static propTypes = {
		data: PropTypes.object.isRequired
	};

	render() {
		const {
			className,
			data: {accountNames}
		} = this.props;

		return (
			<td className={getCN('name-cell-root', className)}>
				<div className='text-truncate'>
					{accountNames && accountNames.length
						? accountNames.join(', ')
						: '-'}
				</div>
			</td>
		);
	}
}
