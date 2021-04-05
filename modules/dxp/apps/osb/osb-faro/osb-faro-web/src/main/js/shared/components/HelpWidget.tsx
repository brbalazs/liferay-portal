import Button from 'shared/components/Button';
import ClayDropDown, {Align} from '@clayui/drop-down';
import React, {useState} from 'react';
import urlConstants from 'shared/util/url-constants';

// TODO: LRAC-7603 Create Help Modal
const DROPDOWN_ITEMS = [
	{
		handleOnClick: () => {},
		label: Liferay.Language.get('report-an-issue')
	},
	{
		href: urlConstants.HELP_CENTER,
		label: Liferay.Language.get('help-center')
	}
];

const HelpWidget = () => {
	const [active, setActive] = useState(false);

	return (
		<div className='help-widget-root'>
			<ClayDropDown
				active={active}
				alignmentPosition={Align.TopLeft}
				menuElementAttrs={{
					className: 'help-dropdown-root'
				}}
				onActiveChange={setActive}
				trigger={
					<Button
						aria-label={Liferay.Language.get('help')}
						borderless
						className='help-button'
						display='defaut'
						icon='ac-question-mark'
						iconAlignment='right'
						size='sm'
					/>
				}
			>
				<ClayDropDown.ItemList>
					<ClayDropDown.Group>
						{DROPDOWN_ITEMS.map((item, i) => (
							<ClayDropDown.Item
								href={item.href}
								key={i}
								onClick={item.handleOnClick}
							>
								{item.label}
							</ClayDropDown.Item>
						))}
					</ClayDropDown.Group>
				</ClayDropDown.ItemList>
			</ClayDropDown>
		</div>
	);
};

export default HelpWidget;
