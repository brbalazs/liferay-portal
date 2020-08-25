import AddWorkspaceForm from 'shared/components/workspaces/AddWorkspaceForm';
import autobind from 'autobind-decorator';
import FaroConstants from 'shared/util/constants';
import getCN from 'classnames';
import React from 'react';
import WorkspacesBasePage from 'shared/components/workspaces/BasePage';
import {addAlert, alertTypes} from 'shared/actions/alerts';
import {compose, optional, redirectIf, withProject} from 'shared/hoc';
import {
	configureProject,
	createProject,
	createTrialProject
} from 'shared/actions/projects';
import {connect} from 'react-redux';
import {Project} from '../util/records';
import {PropTypes} from 'prop-types';
import {Redirect} from 'react-router';
import {Routes, toRoute} from 'shared/util/router';

const {
	dataSourceStates: {unconfigured}
} = FaroConstants;

export const routingFn = ({project}) => {
	if (project && project.groupId && project.state !== unconfigured) {
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
			configureProject,
			corpProjectUuid,
			createProject,
			createTrialProject,
			project: {groupId, state}
		} = this.props;

		const params = {
			emailAddressDomains,
			friendlyURL: friendlyURL && `/${friendlyURL}`,
			name,
			...(state === unconfigured
				? {groupId}
				: {corpProjectUuid, serverLocation})
		};

		const createFn =
			state === unconfigured
				? configureProject
				: corpProjectUuid
				? createProject
				: createTrialProject;

		return createFn(params)
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
		{addAlert, configureProject, createProject, createTrialProject}
	),
	optional(withProject, {idPropName: 'corpProjectUuid'}),
	redirectIf(routingFn)
)(AddWorkspace);
