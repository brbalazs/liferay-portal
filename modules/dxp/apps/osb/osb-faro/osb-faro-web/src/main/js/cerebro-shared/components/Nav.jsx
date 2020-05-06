import getCN from 'classnames';
import omitDefinedProps from 'shared/util/omitDefinedProps';
import React from 'react';
import {Link} from 'react-router-dom';
import {PropTypes} from 'prop-types';

/**
 * Item
 * @class
 * @memberof Nav component
 */
class Item extends React.Component {
	static defaultProps = {
		active: false,
		linkMonospaced: false
	};

	static propTypes = {
		/**
		 * @memberof Nav
		 * @type {boolean}
		 * @default false
		 */
		active: PropTypes.bool,

		/**
		 * @memberof Nav
		 * @type {string}
		 * @default undefined
		 */
		href: PropTypes.string,

		/**
		 * @memberof Nav
		 * @type {string}
		 * @default undefined
		 */
		label: PropTypes.string,

		/**
		 * @memberof Nav
		 * @type {boolean}
		 * @default false
		 */
		linkMonospaced: PropTypes.bool
	};

	/**
	 * Lifecycle Render - ReactJS
	 */
	render() {
		const {
			active,
			children,
			className,
			href,
			linkMonospaced,
			onClick,
			...otherProps
		} = this.props;

		let content = children;

		if (href) {
			const classes = getCN('nav-link', className, {
				active,
				'nav-link-monospaced': linkMonospaced
			});

			content = (
				<Link
					className={classes}
					onClick={onClick}
					role='tab'
					to={href}
				>
					{children}
				</Link>
			);
		}

		return (
			<li
				{...omitDefinedProps(otherProps, Item.propTypes)}
				className='nav-item'
				role='presentation'
			>
				{content}
			</li>
		);
	}
}

/**
 * Nav
 * @class
 */
class Nav extends React.Component {
	static defaultProps = {
		vertical: false
	};

	static propTypes = {
		/**
		 * @type {?string|undefined}
		 * @default undefined
		 */
		display: PropTypes.oneOf(['pills', 'tabs', 'underline']),

		/**
		 * @type {boolean}
		 * @default false
		 */
		vertical: PropTypes.bool
	};

	/**
	 * @inheritDoc
	 */
	render() {
		const {
			context: {navBar},
			props: {children, display, vertical}
		} = this;

		const classes = getCN('nav-root', {
			'flex-column': vertical,
			nav: !navBar,
			'navbar-nav': navBar,
			[`nav-${display}`]: display
		});

		return (
			<ul className={classes} role='tablist'>
				{children}
			</ul>
		);
	}
}

Nav.Item = Item;
export default Nav;
