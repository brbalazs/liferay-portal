import getCN from 'classnames';
import React from 'react';
import Spinner from '../components/Spinner';

export default class Loading extends React.Component {
	render() {
		const {className} = this.props;

		return (
			<div className={getCN('loading-root', className)}>
				<Spinner fadeIn />
			</div>
		);
	}
}
