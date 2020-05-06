import getCN from 'classnames';
import Icon from '../Icon';
import omitDefinedProps from 'shared/util/omitDefinedProps';
import React from 'react';
import {PropTypes} from 'prop-types';

export default class Label extends React.Component {
	static defaultProps = {
		required: false
	};

	static propTypes = {
		info: PropTypes.string,
		required: PropTypes.bool
	};

	render() {
		const {children, className, info, required, ...otherProps} = this.props;

		let tooltipProps = {};

		if (info) {
			tooltipProps = {
				'data-tooltip': true,
				title: info
			};
		}

		return (
			<label
				{...omitDefinedProps(otherProps, Label.propTypes)}
				{...tooltipProps}
				className={getCN(
					'form-control-label',
					'label-root',
					className,
					{
						required
					}
				)}
			>
				{children}

				{info && (
					<Icon className='info' symbol='question-circle-full' />
				)}
			</label>
		);
	}
}
