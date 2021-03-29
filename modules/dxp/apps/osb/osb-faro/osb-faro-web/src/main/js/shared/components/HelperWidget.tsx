import Button from 'shared/components/Button';
import React from 'react';

const HelperWidget = () => (
	<div className='helper-widget-wrapper'>
		<Button
			aria-label={Liferay.Language.get('help')}
			borderless
			className='helper-button'
			display='defaut'
			icon='ac-question-mark'
			iconAlignment='right'
			size='sm'
		/>
	</div>
);

export default HelperWidget;
