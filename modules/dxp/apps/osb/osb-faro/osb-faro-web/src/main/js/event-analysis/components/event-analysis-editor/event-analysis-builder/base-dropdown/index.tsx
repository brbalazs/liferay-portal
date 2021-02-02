import CardTabs, {CardTabSizes} from 'shared/components/CardTabs';
import ClayDropdown, {Align} from '@clayui/drop-down';
import React, {useState} from 'react';
import SearchableList from './SearchableList';

interface IBaseDropdownProps extends React.HTMLAttributes<HTMLDivElement> {
	activeTabId: string;
	tabs: {
		onClick: () => void;
		tabId: string;
		title: string;
	}[];
	title: string;
	trigger: React.ReactElement;
}

const BaseDropdown: React.FC<IBaseDropdownProps> = ({
	activeTabId,
	children,
	tabs,
	title,
	trigger
}) => {
	const [active, setActive] = useState(false);

	return (
		<ClayDropdown
			active={active}
			alignmentPosition={Align.RightTop}
			menuElementAttrs={{className: 'event-analysis-dropdown-menu-root'}}
			onActiveChange={setActive}
			trigger={trigger}
		>
			<div className='event-analysis-dropdown-header'>{title}</div>

			<CardTabs
				activeTabId={activeTabId}
				className='event-type-selector'
				size={CardTabSizes.Small}
				tabs={tabs}
			/>

			{(children as (bag: any) => React.ReactNode)({active, setActive})}
		</ClayDropdown>
	);
};

export default Object.assign(BaseDropdown, {
	SearchableList
});
