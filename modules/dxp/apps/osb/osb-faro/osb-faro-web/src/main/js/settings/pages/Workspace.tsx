import * as API from 'shared/api';
import AddWorkspaceForm from 'shared/components/workspaces/AddWorkspaceForm';
import BasePage from 'settings/components/BasePage';
import Promise from 'metal-promise';
import React from 'react';
import {addAlert, alertTypes} from 'shared/actions/alerts';
import {compose, withCurrentUser, withHistory, withQuery} from 'shared/hoc';
import {connect} from 'react-redux';
import {Project, User} from 'shared/util/records';
import {Routes, toRoute} from 'shared/util/router';
import {updateProject} from 'shared/actions/projects';
import {withProject} from 'shared/hoc/WithProject';

type History = {
	push: (path: string) => void;
};

interface IWorkspaceProps extends React.HTMLAttributes<HTMLElement> {
	addAlert: (object) => Promise<any>;
	currentUser: User;
	emailAddressDomains: string[];
	groupId: string;
	project: Project;
	history: History;
	updateProject: (object) => Promise<any>;
}

export const Workspace: React.FC<IWorkspaceProps> = ({
	addAlert,
	currentUser,
	emailAddressDomains,
	groupId,
	history,
	project,
	updateProject
}) => {
	const handleSubmit = ({emailAddressDomains, friendlyURL, name}) =>
		updateProject({
			emailAddressDomains,
			friendlyURL,
			groupId,
			name
		})
			.then(() => {
				if (friendlyURL !== groupId) {
					history.push(
						toRoute(Routes.SETTINGS_WORKSPACE, {
							groupId: friendlyURL || project.groupId
						})
					);
				}

				addAlert({
					alertType: alertTypes.SUCCESS,
					message: Liferay.Language.get('workspace-settings-saved')
				});
			})
			.catch(error => {
				if (!error.field) {
					addAlert({
						alertType: alertTypes.ERROR,
						message: Liferay.Language.get('unknown-error'),
						timeout: false
					});
				}

				return Promise.reject(error);
			});

	return (
		<BasePage
			backURL={toRoute(Routes.SETTINGS_ADD_DATA_SOURCE, {
				groupId
			})}
			className='workspace-settings'
			groupId={groupId}
			key='workspaceSettingsPage'
			pageDescription={Liferay.Language.get(
				'here-you-can-view-and-change-your-workspace-settings.-you-can-only-set-your-friendly-workspace-url-once.-data-center-location-cannot-be-changed'
			)}
			pageTitle={Liferay.Language.get('workspace-settings')}
		>
			<AddWorkspaceForm
				className='add-workspace-root col-lg-7 pl-0'
				disabled={!currentUser.isAdmin()}
				emailAddressDomains={emailAddressDomains}
				onSubmit={handleSubmit}
				project={project}
			/>
		</BasePage>
	);
};

export default compose(
	connect(
		null,
		{
			addAlert,
			updateProject
		}
	),
	withCurrentUser,
	withHistory,
	withProject(true),
	withQuery(
		({groupId}) =>
			API.projects.fetchEmailAddressDomains({
				groupId
			}),
		val => val,
		({data, error}) => ({
			emailAddressDomains: error ? [] : data
		})
	)
)(Workspace);
