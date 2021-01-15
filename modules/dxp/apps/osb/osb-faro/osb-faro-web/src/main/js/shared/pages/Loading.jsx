import getCN from 'classnames';
import React from 'react';
import Spinner from '../components/Spinner';

export default class Loading extends React.Component {
	render() {
		const {className, fadeIn = true} = this.props;

		return (
			<div className={getCN('loading-root', className)}>
				<Spinner fadeIn={fadeIn} />
			</div>
		);
	}
}
