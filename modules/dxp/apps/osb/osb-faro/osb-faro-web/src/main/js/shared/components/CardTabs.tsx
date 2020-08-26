import Button from 'shared/components/Button';
import getCN from 'classnames';
import React from 'react';

export enum ButtonDisplayMode {
	SPACED_BUTTONS = 'mdn-button-tab'
}

interface ICardTabsProps {
	activeTabId: number | string;
	buttonsDisplayMode?: ButtonDisplayMode;
	className?: string;
	onChange?: (tabId: string) => void;
	tabs: Array<any>;
}

const CardTabs: React.FC<ICardTabsProps> = ({
	activeTabId,
	buttonsDisplayMode,
	className,
	onChange,
	tabs
}) => {
	const handleEmitOnChange = (onClick, tabId): void => {
		onClick && onClick();
		onChange && onChange(tabId);
	};

	return (
		<ul className={getCN('card-tabs-root', className, buttonsDisplayMode)}>
			{tabs.map(({onClick, secondaryInfo, tabId, tabUrl, title}) => (
				<li
					className={getCN('card-tab', {
						active: activeTabId === tabId
					})}
					key={tabId}
				>
					<Button
						display='unstyled'
						href={tabUrl}
						onClick={() => handleEmitOnChange(onClick, tabId)}
					>
						<span className='title'>{title}</span>

						<div>{secondaryInfo}</div>
					</Button>
				</li>
			))}
		</ul>
	);
};

export default CardTabs;
