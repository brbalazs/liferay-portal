import ClayDropDown, {Align} from '@clayui/drop-down';
import ClayIcon from '@clayui/icon';
import getCN from 'classnames';
import Item from './Item';
import React, {useRef, useState} from 'react';
import Sticker from '../Sticker';

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
	menuItems: any;
	showCaret?: boolean;
	userName: string;
}

interface ILabelProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
	showCaret?: boolean;
	userName: string;
}

const userDropDown: React.FC<IUserDropdownProps> = (
	props: IUserDropdownProps
) => {
	const [active, setActive] = useState(false);
	const triggerElementRef = useRef(null);
	const menuElementRef = useRef(null);

	const handleActive = () => {
		setActive(!active);
	};

	const {
		className,
		containerElement: ContainerElement = 'div',
		menuItems,
		alignmentPosition = Align.RightCenter,
		showCaret = false,
		userName
	} = props;

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
				<ClayDropDown.ItemList>
					{menuItems.map(({items, subheaderLabel}, i) => (
						<ClayDropDown.Group header={subheaderLabel} key={i}>
							{items.map((props, i) => (
								<Item {...props} key={i} />
							))}
						</ClayDropDown.Group>
					))}
				</ClayDropDown.ItemList>
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

export default userDropDown;
