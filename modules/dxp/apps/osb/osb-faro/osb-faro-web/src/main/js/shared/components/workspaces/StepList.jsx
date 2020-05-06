import getCN from 'classnames';
import React from 'react';
import {PropTypes} from 'prop-types';

export default class StepList extends React.Component {
	static defaultProps = {
		hideBullets: false,
		steps: []
	};

	static propTypes = {
		hideBullets: PropTypes.bool,
		secondaryInfo: PropTypes.string,
		steps: PropTypes.array
	};

	render() {
		const {hideBullets, secondaryInfo, steps} = this.props;

		return (
			<div
				className={
					getCN('step-list-root', {'hide-bullets': hideBullets}) +
					(this.props.className ? ` ${this.props.className}` : '')
				}
			>
				{secondaryInfo && <b>{secondaryInfo}</b>}

				<ul>
					{steps.map((step, i) => (
						<li key={i}>{step}</li>
					))}
				</ul>
			</div>
		);
	}
}
