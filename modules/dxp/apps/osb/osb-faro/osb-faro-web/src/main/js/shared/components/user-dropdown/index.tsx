import ClayDropDown, {Align} from '@clayui/drop-down';
import ClayIcon from '@clayui/icon';
import getCN from 'classnames';
import Item from './Item';
import React, {useEffect, useRef, useState} from 'react';
import Sticker from '../Sticker';
import {CSSTransition, TransitionGroup} from 'react-transition-group';
import {Menus} from './types';

function getInitials(name = '') {
	const nameArray = name.split(' ', 3);

	return nameArray
		.reduce((acc, val) => acc + val.substring(0, 1), '')
		.toUpperCase();
}

interface IUserDropdownProps extends React.HTMLAttributes<HTMLElement> {
	alignmentPosition?: React.ComponentProps<
		typeof ClayDropDown
	>['alignmentPosition'];
	containerElement?: React.ComponentProps<
		typeof ClayDropDown
	>['containerElement'];
	initialActiveMenu: string;
	menus: Menus;
	showCaret?: boolean;
	userName: string;
}

interface ILabelProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
	showCaret?: boolean;
	userName: string;
}

const userDropDown: React.FC<IUserDropdownProps> = ({
	alignmentPosition = Align.RightCenter,
	className,
	containerElement: ContainerElement = 'div',
	initialActiveMenu,
	menus,
	showCaret = false,
	userName
}: IUserDropdownProps) => {
	const [active, setActive] = useState(false);
	const [activeMenu, setActiveMenu] = useState(initialActiveMenu);

	useEffect(() => {
		setActiveMenu(last(history));
	}, [history]);

	const triggerElementRef = useRef(null);
	const menuElementRef = useRef(null);

	const handleActive = () => {
		setActive(!active);
	};

	const menuIds = Object.keys(menus);

	return (
		<>
			<ContainerElement className={className}>
				<Label
					onClick={handleActive}
					ref={triggerElementRef}
					showCaret={showCaret}
					userName={userName}
				/>
			</ContainerElement>

			<ClayDropDown.Menu
				active={active}
				alignElementRef={triggerElementRef}
				alignmentPosition={alignmentPosition}
				className='user-menu-dropdown'
				onSetActive={setActive}
				ref={menuElementRef}
			>
				{menuIds.map(menuId => (
					<ClayDropDown.ItemList
						className={getCN('menu-list', {
							active: activeMenu === menuId
						})}
						key={menuId}
					>
						{menus[menuId].map(({items, subheaderLabel}, i) => (
							<ClayDropDown.Group header={subheaderLabel} key={i}>
								{items.map(
									(
										{childMenuId, onClick, ...otherProps},
										i
									) => (
										<Item
											{...otherProps}
											key={i}
											onClick={() => {
												childMenuId &&
													setActiveMenu(childMenuId);

												onClick && onClick();
											}}
										/>
									)
								)}
							</ClayDropDown.Group>
						))}
					</ClayDropDown.ItemList>
				))}
			</ClayDropDown.Menu>
		</>
	);
};

const Label = React.forwardRef<HTMLButtonElement, ILabelProps>(
	({className, showCaret, userName, ...otherProps}, ref) => (
		<button
			className={getCN(
				'user-menu button-root btn btn-unstyled trigger',
				className
			)}
			ref={ref}
			type='button'
			{...otherProps}
		>
			<div className='text-truncate'>
				<Sticker circle className='avatar'>
					{getInitials(userName)}
				</Sticker>

				<span className='user-name'>{userName}</span>

				{showCaret && (
					<ClayIcon className='caret' symbol='caret-bottom' />
				)}
			</div>
		</button>
	)
);

export {Menus};
export default userDropDown;
