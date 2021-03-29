import Button from 'shared/components/Button';
import React from 'react';
import {Modal} from 'shared/types';

interface IWrappedComponentProps {
	close: Modal.close;
	currentUserId: string;
	faroSubscriptionIMap: Map<string, any>;
	groupId: string;
	open: Modal.open;
	serverLocation: string;
	workspaceName: string;
}

const HelpWidget = (
	WrappedComponent: React.ComponentType<IWrappedComponentProps>
) => props => (
	<>
		<WrappedComponent {...props} />
		<div className='helper-widget-wrapper'>
			<Button
				aria-label={Liferay.Language.get('help')}
				borderless
				className='button-helper'
				display='defaut'
				icon='ac-question-mark'
				iconAlignment='right'
				size='sm'
			/>
		</div>
	</>
);

export default HelpWidget;
