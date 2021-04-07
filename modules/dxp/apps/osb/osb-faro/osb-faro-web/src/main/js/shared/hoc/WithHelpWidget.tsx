import HelpWidget from 'shared/components/HelpWidget';
import React from 'react';
import {Modal} from 'shared/types';
import {PLANS} from 'shared/util/subscriptions';

interface IWrappedComponentProps {
	close: Modal.close;
	currentUserId: string;
	faroSubscriptionIMap: Map<string, any>;
	groupId: string;
	open: Modal.open;
	serverLocation: string;
	workspaceName: string;
}

const withHelpWidget = (
	WrappedComponent: React.ComponentType<IWrappedComponentProps>
) => props => {
	const basicTier =
		props.faroSubscriptionIMap.get('name') === PLANS.basic.name;

	return (
		<>
			<WrappedComponent {...props} />

			<HelpWidget showModal={basicTier} />
		</>
	);
};

export default withHelpWidget;
