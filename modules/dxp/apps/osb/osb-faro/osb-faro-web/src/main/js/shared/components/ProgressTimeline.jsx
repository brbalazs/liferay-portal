import getCN from 'classnames';
import Icon from './Icon';
import React from 'react';
import {Link} from 'react-router-dom';
import {PropTypes} from 'prop-types';

class ProgressTimeline extends React.Component {
	static propTypes = {
		activeIndex: PropTypes.number,
		className: PropTypes.string,
		items: PropTypes.array
	};

	render() {
		const {activeIndex, className, items} = this.props;

		return (
			<div className={getCN('timeline-root', className)}>
				{items.map(({href, title}, i) => {
					const active = activeIndex === i;
					const previousStep = activeIndex > i;

					const step = i + 1;

					return [
						<Link
							className={getCN('step', {
								active,
								'previous-step': previousStep
							})}
							key={i}
							to={href || ''}
						>
							<div className='title'>{title}</div>

							<div className='circle'>
								{!previousStep && step}

								{previousStep && <Icon symbol='check' />}
							</div>
						</Link>,
						items.length !== i + 1 && (
							<span
								className={getCN('bar', {filled: previousStep})}
								key={`bar-${i}`}
							/>
						)
					];
				})}
			</div>
		);
	}
}

export default ProgressTimeline;
