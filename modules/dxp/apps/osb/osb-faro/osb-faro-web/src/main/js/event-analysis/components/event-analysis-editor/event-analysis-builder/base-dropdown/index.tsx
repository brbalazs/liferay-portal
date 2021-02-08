import ClayDropdown, {Align} from '@clayui/drop-down';
import getCN from 'classnames';
import Header from './Header';
import React, {useEffect, useState} from 'react';
import SearchableList from './SearchableList';

interface IBaseDropdownProps extends React.HTMLAttributes<HTMLDivElement> {
	trigger: React.ReactElement;
	onActiveChange?: (active: boolean) => void;
}

const BaseDropdown: React.FC<IBaseDropdownProps> = ({
	children,
	className,
	onActiveChange,
	trigger
}) => {
	const [active, setActive] = useState(false);

	useEffect(() => {
		if (onActiveChange) {
			onActiveChange(active);
		}
	}, [active]);

	return (
		<ClayDropdown
			active={active}
			alignmentPosition={Align.RightTop}
			menuElementAttrs={{
				className: getCN('base-dropdown-menu-root', className)
			}}
			onActiveChange={setActive}
			trigger={trigger}
		>
			{(children as (bag: any) => React.ReactNode)({active, setActive})}
		</ClayDropdown>
	);
};

export default Object.assign(BaseDropdown, {
	Header,
	SearchableList
});
