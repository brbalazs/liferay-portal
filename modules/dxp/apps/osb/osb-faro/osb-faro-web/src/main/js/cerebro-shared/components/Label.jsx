import autobind from 'autobind-decorator';
import Button from 'shared/components/Button';
import getCN from 'classnames';
import omitDefinedProps from 'shared/util/omitDefinedProps';
import React from 'react';
import {PropTypes} from 'prop-types';

/**
 * Metal Clay Label component.
 */
class Label extends React.Component {
	static defaultProps = {
		closeable: false,
		display: 'secondary'
	};

	/**
	 * State definition.
	 * @static
	 * @type {!Object}
	 */
	static propTypes = {
		/**
		 * CSS classes to be applied to the element.
		 * @instance
		 * @memberof Label
		 * @type {?string|undefined}
		 * @default undefined
		 */
		className: PropTypes.string,

		/**
		 * True or false to activate the close button.
		 * @instance
		 * @memberof Label
		 * @type {?bool}
		 * @default false
		 */
		closeable: PropTypes.bool,

		/**
		 * @instance
		 * @memberOf Label
		 * @type {object}
		 * @default undefined
		 */
		data: PropTypes.object,

		/**
		 * Label display color.
		 * @instance
		 * @memberof Label
		 * @type {?string}
		 * @default secondary
		 */
		display: PropTypes.oneOf([
			'danger',
			'info',
			'secondary',
			'success',
			'warning'
		]),

		/**
		 * Id to be applied to the element.
		 * @instance
		 * @memberof Label
		 * @type {?string|undefined}
		 * @default undefined
		 */
		id: PropTypes.string,

		/**
		 * The label of the badge element.
		 * @instance
		 * @memberof Label
		 * @type {?string|undefined}
		 * @default undefined
		 */
		label: PropTypes.string,

		/**
		 * Callback for when Label is
		 * removed/clicked on.
		 */
		onRemove: PropTypes.func,

		/**
		 * The size of the label element.
		 * @instance
		 * @memberof Label
		 * @type {?string|undefined}
		 * @default undefined
		 */
		size: PropTypes.oneOf(['lg']),

		/**
		 * The path to the SVG spritemap file containing the icons.
		 * @instance
		 * @memberof Label
		 * @type {?string|undefined}
		 * @default undefined
		 */
		spritemap: PropTypes.string
	};

	/**
	 * Handle Click
	 */
	@autobind
	handleClick() {
		const {onRemove} = this.props;

		onRemove && onRemove(this.props.data);
	}

	/**
	 * Lifecycle Render - ReactJS
	 */
	render() {
		const {
			className,
			closeable,
			display,
			id,
			label,
			size,
			...otherProps
		} = this.props;

		const classes = getCN('label', `label-${display}`, className, {
			['label-dismissible']: closeable,
			[`label-${size}`]: size
		});

		return (
			<span
				{...omitDefinedProps(otherProps, Label.propTypes)}
				className={classes}
				id={id}
			>
				<span className='label-item label-item-expand'>{label}</span>

				{closeable && (
					<span className='label-item label-item-after'>
						<Button
							aria-label='Close'
							className='close'
							display='unstyled'
							icon='times'
							iconAlignment='right'
							onClick={this.handleClick}
						/>
					</span>
				)}
			</span>
		);
	}
}

export {Label};
export default Label;
