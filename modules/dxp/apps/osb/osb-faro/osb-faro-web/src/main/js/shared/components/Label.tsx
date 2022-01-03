import autobind from 'autobind-decorator';
import Button from 'shared/components/Button';
import getCN from 'classnames';
import Icon from 'shared/components/Icon';
import omitDefinedProps from 'shared/util/omitDefinedProps';
import React from 'react';
import {PropTypes} from 'prop-types';

const DISPLAYS = [
	'primary',
	'secondary',
	'success',
	'info',
	'warning',
	'danger',
	'light',
	'dark'
];

const SIZES = ['lg'];

class Label extends React.Component {
	static defaultProps = {
		uppercase: false
	};

	static propTypes = {
		display: PropTypes.oneOf(DISPLAYS),
		index: PropTypes.number,
		onRemove: PropTypes.func,
		size: PropTypes.oneOf(SIZES),
		uppercase: PropTypes.bool
	};

	@autobind
	handleRemove() {
		const {index, onRemove} = this.props;

		onRemove && onRemove(index);
	}

	render() {
		const {
			children,
			className,
			display,
			onRemove,
			size,
			uppercase,
			...otherProps
		} = this.props;

		const classes = getCN('label', 'label-root', className, {
			[`label-${display}`]: display,
			[`label-${size}`]: size,
			'label-dismissible': onRemove,
			['label-uppercase']: uppercase
		});

		return (
			<div
				{...omitDefinedProps(otherProps, Label.propTypes)}
				className={classes}
			>
				<span className='label-item'>{children}</span>

				{onRemove && (
					<span className='label-item label-item-after'>
						<Button
							aria-label={Liferay.Language.get('close')}
							className='close'
							onClick={this.handleRemove}
						>
							<Icon size='sm' symbol='times' />
						</Button>
					</span>
				)}
			</div>
		);
	}
}

Label.DISPLAYS = DISPLAYS;
Label.SIZES = SIZES;
export default Label;
