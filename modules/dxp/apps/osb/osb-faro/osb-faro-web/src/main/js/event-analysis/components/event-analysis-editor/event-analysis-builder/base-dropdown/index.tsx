import ClayDropdown, {Align} from '@clayui/drop-down';
import Header from './Header';
import React, {useState} from 'react';
import SearchableList from './SearchableList';

interface IBaseDropdownProps extends React.HTMLAttributes<HTMLDivElement> {
	trigger: React.ReactElement;
}

const BaseDropdown: React.FC<IBaseDropdownProps> = ({children, trigger}) => {
	const [active, setActive] = useState(false);

	return (
		<ClayDropdown
			active={active}
			alignmentPosition={Align.RightTop}
			menuElementAttrs={{className: 'event-analysis-dropdown-menu-root'}}
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
