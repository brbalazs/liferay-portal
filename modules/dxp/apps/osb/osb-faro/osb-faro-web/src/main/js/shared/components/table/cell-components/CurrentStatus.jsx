import React from 'react';
import {PropTypes} from 'prop-types';

export default class CurrentStatus extends React.Component {
	static propTypes = {
		data: PropTypes.shape({
			currentMember: PropTypes.bool
		}).isRequired
	};

	render() {
		const {
			data: {currentMember}
		} = this.props;

		return (
			<td
				className={
					this.props.className ? ` ${this.props.className}` : ''
				}
			>
				{currentMember
					? Liferay.Language.get('member')
					: Liferay.Language.get('non-member')}
			</td>
		);
	}
}
