import ActivatingDisplay from 'shared/components/workspaces/ActivatingDisplay';
import FaroConstants from 'shared/util/constants';
import React from 'react';
import SuccessDisplay from 'shared/components/workspaces/SuccessDisplay';
import withAction from './WithAction';
import WorkspaceNotFound from 'shared/pages/WorkspaceNotFound';
import WorkspacesErrorDisplay from 'shared/components/workspaces/ErrorDisplay';
import {compose} from 'redux';
import {fetchProjectState} from '../actions/projects';
import {LocalStorageMechanism, Storage} from 'metal-storage';

const {projectStates} = FaroConstants;

/**
 * HOC for conditionally rendering SettingUpWorkspace.
 * If the project state is not ready, we will render SettingUpWorkspace.
 * @returns {Function} - The new component
 */
export default compose(
	withAction(
		({groupId}) => fetchProjectState({groupId}),
		(state, {groupId}) => state.getIn(['projectStates', groupId]),
		{
			propName: 'projectState',
			renderErrorPage: props => <WorkspaceNotFound {...props} />
		}
	),
	WrappedComponent => ({className, groupId, projectState, ...otherProps}) => {
		const {faroSubscription} = projectState;
		const storage = new Storage(new LocalStorageMechanism());
		const currentSubscriptions = storage.get('subscriptions') || {};

		storage.set('activeWorkspaceId', Number(groupId));
		storage.set('subscriptions', {
			...currentSubscriptions,
			[groupId]: faroSubscription.get('name')
		});

		if (window.setStatus && window.$zopim && window.$zopim.livechat) {
			window.setStatus();
		}

		switch (projectState.state) {
			case projectStates.ready:
			case projectStates.scheduled:
				return (
					<WrappedComponent
						className={className}
						groupId={groupId}
						{...otherProps}
					/>
				);

			case projectStates.deactivated:
			case projectStates.maintenance:
			case projectStates.unavailable:
				return (
					<WorkspacesErrorDisplay
						className={className}
						errorType={projectState.state}
					/>
				);

			case projectStates.activating:
				return <ActivatingDisplay groupId={projectState.groupId} />;

			default:
				return (
					<SuccessDisplay
						friendlyURL={
							projectState.friendlyURL ||
							`/${projectState.groupId}`
						}
					/>
				);
		}
	}
);
