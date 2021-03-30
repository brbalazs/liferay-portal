import Button from 'shared/components/Button';
import React from 'react';

const HelpWidget = () => (
	<div className='help-widget-wrapper'>
		<Button
			aria-label={Liferay.Language.get('help')}
			borderless
			className='help-button'
			display='defaut'
			icon='ac-question-mark'
			iconAlignment='right'
			size='sm'
		/>
	</div>
);

export default HelpWidget;
