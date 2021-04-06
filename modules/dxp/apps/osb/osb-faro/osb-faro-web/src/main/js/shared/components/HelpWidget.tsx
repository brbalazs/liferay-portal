import Button from 'shared/components/Button';
import React from 'react';
import urlConstants from 'shared/util/url-constants';
import {Align, ClayDropDownWithItems} from '@clayui/drop-down';

// TODO: LRAC-7603 Create Help Modal
const DROPDOWN_ITEMS = [
	{
		label: Liferay.Language.get('report-an-issue'),
		onClick: () => {}
	},
	{
		href: urlConstants.HELP_CENTER,
		label: Liferay.Language.get('help-center'),
		target: '_blank'
	}
];

const HelpWidget = () => (
	<div className='help-widget-root'>
		<ClayDropDownWithItems
			alignmentPosition={Align.TopLeft}
			items={DROPDOWN_ITEMS}
			menuElementAttrs={{
				className: 'help-dropdown-root'
			}}
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
		/>
	</div>
);

export default HelpWidget;
