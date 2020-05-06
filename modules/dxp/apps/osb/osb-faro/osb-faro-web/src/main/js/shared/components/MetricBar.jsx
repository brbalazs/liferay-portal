import getCN from 'classnames';
import React from 'react';
import {PropTypes} from 'prop-types';
import {round} from 'lodash';

export const DISPLAYS = ['danger', 'primary', 'warning'];

export const SIZES = ['xs', 'sm', 'md', 'lg', 'default'];

export default class MetricBar extends React.Component {
	static defaultProps = {
		percent: 0,
		size: 'default'
	};

	static propTypes = {
		display: PropTypes.oneOf(DISPLAYS),
		percent: PropTypes.number,
		size: PropTypes.oneOf(SIZES)
	};

	render() {
		const {children, className, display, percent, size} = this.props;

		const barClasses = getCN('bar', {
			[`bar-${display}`]: display,
			[`bar-${size}`]: size
		});

		return (
			<div className={getCN('metric-bar-root', {className})}>
				<div
					className={barClasses}
					style={{width: `${round(percent * 100)}%`}}
				/>

				{children && (
					<div className='info-wrapper align-items-center d-flex justify-content-between'>
						{children}
					</div>
				)}
			</div>
		);
	}
}
