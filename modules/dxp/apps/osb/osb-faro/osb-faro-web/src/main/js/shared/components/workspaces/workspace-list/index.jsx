import FaroConstants from 'shared/util/constants';
import getCN from 'classnames';
import React from 'react';
import WorkspaceListItem from './ListItem';
import {getPlanLabel} from 'shared/util/subscriptions';
import {noop} from 'lodash';
import {Project} from 'shared/util/records';
import {PropTypes} from 'prop-types';
import {Routes, toRoute} from 'shared/util/router';

const {
	dataSourceStates: {unconfigured}
} = FaroConstants;

export default class WorkspaceList extends React.Component {
	static defaultProps = {
		checkDisabled: noop,
		displayAccountHeaders: false,
		displayPlanInfo: false,
		isJoinableProjects: false
	};

	static propTypes = {
		accounts: PropTypes.arrayOf(
			PropTypes.shape({
				accountName: PropTypes.string,
				projects: PropTypes.arrayOf(PropTypes.instanceOf(Project))
			})
		),
		checkDisabled: PropTypes.func,
		displayAccountHeaders: PropTypes.bool,
		displayPlanInfo: PropTypes.bool,
		isJoinableProjects: PropTypes.bool
	};

	getRoute({corpProjectUuid, friendlyURL, groupId, state}) {
		if (friendlyURL) {
			return toRoute(Routes.WORKSPACE_WITH_ID, {
				groupId: friendlyURL.replace('/', '')
			});
		} else if (groupId && state !== unconfigured) {
			return toRoute(Routes.WORKSPACE_WITH_ID, {
				groupId
			});
		}

		return toRoute(Routes.WORKSPACE_ADD_WITH_CORP_PROJECT_UUID, {
			corpProjectUuid
		});
	}

	render() {
		const {
			accounts,
			checkDisabled,
			className,
			displayPlanInfo,
			isJoinableProjects
		} = this.props;

		return (
			<div className={getCN('workspace-list-root', className)}>
				<ul className={getCN('list-group', className)}>
					{accounts.map(project => {
						const {
							className,
							corpProjectName,
							faroSubscription,
							groupId,
							name,
							requested,
							state
						} = project;

						return (
							<WorkspaceListItem
								accountName={name}
								className={className}
								configured={state !== unconfigured}
								corpProjectName={corpProjectName}
								disabled={checkDisabled(project)}
								groupId={groupId}
								href={this.getRoute(project)}
								isJoinableProjects={isJoinableProjects}
								key={name}
								name={name}
								planInfo={
									displayPlanInfo
										? `${getPlanLabel(
												faroSubscription.get('name')
										  )}`
										: ''
								}
								projectState={state}
								requested={requested}
							/>
						);
					})}
				</ul>
			</div>
		);
	}
}
