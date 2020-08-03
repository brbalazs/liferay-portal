/* eslint-disable jsx-a11y/anchor-is-valid */

import autobind from 'autobind-decorator';
import Button from 'shared/components/Button';
import getCN from 'classnames';
import Icon from 'shared/components/Icon';
import omitDefinedProps from 'shared/util/omitDefinedProps';
import Overlay, {ALIGNMENTS} from 'shared/components/Overlay';
import React from 'react';
import {hasChanges} from 'shared/util/react';
import {Link} from 'react-router-dom';
import {PropTypes} from 'prop-types';

const CLASSNAME = 'analytics-dropdown';

const ITEM_SHAPE = {
	active: PropTypes.bool,
	description: PropTypes.string,
	disabled: PropTypes.bool,
	href: PropTypes.string,
	icon: PropTypes.string,
	label: PropTypes.string.isRequired,
	separator: PropTypes.bool,
	value: PropTypes.string
};

/**
 * Dropdown Item
 * @class
 */
export class DropdownItem extends React.Component {
	static propTypes = ITEM_SHAPE;

	/**
	 * Handles click to select item.
	 * @param {!Event} event
	 */
	@autobind
	handleClickItem(value) {
		const {onClick} = this.props;

		onClick && onClick(value);
	}

	render() {
		const {
			active,
			description,
			disabled,
			href,
			icon,
			label,
			separator,
			value
		} = this.props;

		const classes = getCN('dropdown-item', {
			['active']: active,
			['disabled']: disabled
		});

		if (!separator) {
			return (
				<li>
					{href ? (
						<Link
							className={classes}
							data-value={value}
							onClick={() => this.handleClickItem(value)}
							to={href}
						>
							{icon && (
								<div className='dropdown-item-indicator'>
									<Icon symbol={icon} />
								</div>
							)}

							<>
								{label}

								{description && (
									<div className={`${CLASSNAME}-description`}>
										{description}
									</div>
								)}
							</>
						</Link>
					) : (
						<a
							className={classes}
							data-value={value}
							href='javascript:;'
							onClick={() => this.handleClickItem(value)}
						>
							{icon && (
								<div className='dropdown-item-indicator'>
									<Icon symbol={icon} />
								</div>
							)}

							{description ? (
								<>
									{label}

									<div className={`${CLASSNAME}-description`}>
										{description}
									</div>
								</>
							) : (
								label
							)}
						</a>
					)}
				</li>
			);
		} else {
			return <li className='dropdown-divider' />;
		}
	}
}

/**
 * Dropdown
 * @class
 */
class Dropdown extends React.Component {
	static defaultProps = {
		active: false,
		align: 'bottomLeft',
		buttonProps: {display: 'secondary'},
		disabled: false,
		display: 'primary',
		forceAlignment: false,
		items: [],
		value: ''
	};

	static propTypes = {
		/**
		 * Flag to indicate if dropdown is open
		 * @type {?bool}
		 * @default false
		 */
		active: PropTypes.bool,

		align: PropTypes.oneOf(ALIGNMENTS),

		buttonProps: PropTypes.object,

		/**
		 * CSS classes to be applied to the element.
		 * @type {?string|undefined}
		 * @default undefined
		 */
		className: PropTypes.string,

		/**
		 * Flag to indicate if menu is disabled
		 * @type {?bool}
		 * @default false
		 */
		disabled: PropTypes.bool,

		/**
		 * The css class that colors the button. Display `unstyled` is only for internal
		 * purposes.
		 * @type {?string|undefined}
		 * @default primary
		 */
		display: PropTypes.oneOf(['link', 'primary', 'secondary', 'unstyled']),

		forceAlignment: PropTypes.bool,

		/**
		 * List of menu items.
		 * @type {!array}
		 * @default undefined
		 */
		items: PropTypes.arrayOf(PropTypes.shape(ITEM_SHAPE)),

		/**
		 * Position in which item icons will be placed. Needed if any item has icons.
		 * @type {?string|undefined}
		 * @default undefined
		 */
		itemsIconAlignment: PropTypes.oneOf(['left', 'right']),

		/**
		 * A prop callback for when state value changes
		 */
		onValueChange: PropTypes.func,

		/**
		 * The size of the button
		 * @type {string}
		 * @default undefined
		 */
		size: PropTypes.string,

		/**
		 * The label of the button content.
		 * @type {?string}
		 * @default undefined
		 */
		value: PropTypes.string
	};

	state = {
		active: false,
		items: [],
		value: ''
	};

	componentDidUpdate(prevProps) {
		if (hasChanges(prevProps, this.props, 'value')) {
			this.setState({
				value: this.props.value
			});
		}

		if (hasChanges(prevProps, this.props, 'items')) {
			this.setState({
				items: this.props.items
			});
		}
	}

	/**
	 * Closes the dropdown.
	 * @protected
	 */
	closeDropdown() {
		this.setState({
			active: false
		});
	}

	/**
	 * Toggle dropdown.
	 */
	toggle() {
		if (!this.state.active) {
			this.setState({
				active: true
			});
		} else {
			this.setState({
				active: false
			});
		}
	}

	/**
	 * Get Label For Value
	 * @param {string} value
	 */
	getLabelForValue(value) {
		const {items} = this.props;

		let selectedItem = items.find(item => item.value === value);

		if (!selectedItem) selectedItem = items[0];

		return (selectedItem && selectedItem.label) || '';
	}

	/**
	 * Handles click button dropdown.
	 * @param {!Event} event
	 * @protected
	 */
	@autobind
	handleClickButton() {
		this.toggle();
	}

	@autobind
	handleClickItem(value) {
		const {items, onValueChange} = this.props;

		onValueChange && onValueChange(value);

		this.setState({
			active: false,
			items: items.map(item => ({
				...item,
				active: item.value === value
			})),
			value
		});
	}

	/**
	 * Handle Outside Click
	 */
	@autobind
	handleOutsideClick() {
		this.setState({
			active: false
		});
	}

	/**
	 * Lifecycle Render - ReactJS
	 */
	render() {
		const {
			align,
			buttonProps,
			className,
			disabled,
			fixedValue,
			forceAlignment,
			items,
			itemsIconAlignment,
			size,
			value,
			...otherProps
		} = this.props;

		const {active} = this.state;

		const classesBtn = getCN(
			'btn-sm dropdown-toggle',
			buttonProps.className
		);

		const classesUl = getCN('dropdown-menu', {
			'dropdown-menu-indicator-end': itemsIconAlignment == 'right',
			'dropdown-menu-indicator-start': itemsIconAlignment == 'left',
			['show']: active
		});

		const mainClasses = getCN(CLASSNAME, 'dropdown btn-group', className);

		return (
			<div
				{...omitDefinedProps(otherProps, Dropdown.propTypes)}
				className={mainClasses}
			>
				{items && (
					<Overlay
						active={active}
						alignment={align}
						containerClass='dropdown dropdown-root'
						forceAlignment={forceAlignment}
						onOutsideClick={this.handleOutsideClick}
					>
						<Button
							{...buttonProps}
							className={classesBtn}
							disabled={disabled}
							icon='caret-bottom'
							iconAlignment='right'
							onClick={this.handleClickButton}
							size={size}
						>
							{fixedValue
								? fixedValue
								: this.getLabelForValue(value)}
						</Button>

						<div className={classesUl}>
							{items.map((item, index) => (
								<DropdownItem
									{...item}
									key={index}
									onClick={this.handleClickItem}
								/>
							))}
						</div>
					</Overlay>
				)}
			</div>
		);
	}
}

export {Dropdown};
export default Dropdown;
