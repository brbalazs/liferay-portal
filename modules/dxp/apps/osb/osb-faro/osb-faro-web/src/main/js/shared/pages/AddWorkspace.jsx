import AddWorkspaceForm from 'shared/components/workspaces/AddWorkspaceForm';
import autobind from 'autobind-decorator';
import getCN from 'classnames';
import React from 'react';
import WorkspacesBasePage from 'shared/components/workspaces/BasePage';
import {addAlert, alertTypes} from 'shared/actions/alerts';
import {compose, optional, redirectIf, withProject} from 'shared/hoc';
import {connect} from 'react-redux';
import {createProject, createTrialProject} from 'shared/actions/projects';
import {Project} from '../util/records';
import {PropTypes} from 'prop-types';
import {Redirect} from 'react-router';
import {Routes, toRoute} from 'shared/util/router';

export const routingFn = ({project}) => {
	if (project && project.groupId) {
		return toRoute(Routes.WORKSPACE_WITH_ID, {groupId: project.groupId});
	} else {
		return null;
	}
};

export class AddWorkspace extends React.Component {
	state = {
		redirectToWorkspace: false
	};

	static propTypes = {
		addAlert: PropTypes.func,
		createProject: PropTypes.func,
		history: PropTypes.object.isRequired,
		project: PropTypes.instanceOf(Project)
	};

	@autobind
	handleSubmit({emailAddressDomains, friendlyURL, name, serverLocation}) {
		const {
			addAlert,
			corpProjectUuid,
			createProject,
			createTrialProject
		} = this.props;

		const createFn = corpProjectUuid ? createProject : createTrialProject;

		return createFn({
			corpProjectUuid,
			emailAddressDomains,
			friendlyURL: friendlyURL && `/${friendlyURL}`,
			name,
			serverLocation
		})
			.then(({payload: {friendlyURL, groupId}}) => {
				this.setState({
					friendlyURL: friendlyURL
						? friendlyURL.replace('/', '')
						: groupId,
					redirectToWorkspace: true
				});

				addAlert({
					alertType: alertTypes.SUCCESS,
					message: Liferay.Language.get('success')
				});
			})
			.catch(error => {
				if (!error.field) {
					addAlert({
						alertType: alertTypes.ERROR,
						message: error.message,
						timeout: false
					});
				}

				return Promise.reject(error);
			});
	}

	render() {
		const {
			props: {className, project},
			state: {friendlyURL, redirectToWorkspace}
		} = this;

		return (
			<div
				className={getCN('add-workspace-root', className)}
				key='AddWorkspace'
			>
				{redirectToWorkspace ? (
					<Redirect
						to={toRoute(Routes.WORKSPACE_WITH_ID, {
							groupId: friendlyURL
						})}
					/>
				) : (
					<WorkspacesBasePage
						title={Liferay.Language.get('new-workspace')}
					>
						<AddWorkspaceForm
							onSubmit={this.handleSubmit}
							project={project}
						/>
					</WorkspacesBasePage>
				)}
			</div>
		);
	}
}

export default compose(
	connect(
		null,
		{addAlert, createProject, createTrialProject}
	),
	optional(withProject, {idPropName: 'corpProjectUuid'}),
	redirectIf(routingFn)
)(AddWorkspace);
