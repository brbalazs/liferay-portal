import Alert from 'shared/components/Alert';
import autobind from 'autobind-decorator';
import FaroConstants from 'shared/util/constants';
import moment from 'moment';
import React from 'react';
import {compose} from 'redux';
import {connect} from 'react-redux';
import {Project} from 'shared/util/records';
import {PropTypes} from 'prop-types';
import {setMaintenanceSeen} from 'shared/actions/maintenance-seen';
import {sub} from 'shared/util/lang';
import {withRouter} from 'react-router-dom';

const {projectStates} = FaroConstants;

export class MaintenanceAlert extends React.Component {
	static propTypes = {
		alertDismissed: PropTypes.bool.isRequired,
		currentUserId: PropTypes.string.isRequired,
		groupId: PropTypes.string.isRequired,
		project: PropTypes.instanceOf(Project).isRequired,
		setMaintenanceSeen: PropTypes.func.isRequired,
		stripe: PropTypes.bool
	};

	static defaultProps = {
		stripe: false
	};

	state = {
		showMessage: false
	};

	@autobind
	handleDismissClick() {
		const {
			currentUserId,
			groupId,
			project: {stateStartDate},
			setMaintenanceSeen
		} = this.props;

		setMaintenanceSeen({
			currentUserId,
			groupId,
			stateStartDate
		});
	}

	render() {
		const {
			alertDismissed,
			project: {state, stateStartDate},
			stripe
		} = this.props;

		const showAlert = state === projectStates.scheduled && !alertDismissed;

		return (
			<div
				className={`maintenance-alert-root${
					this.props.className ? ` ${this.props.className}` : ''
				}`}
			>
				{showAlert && (
					<Alert
						iconSymbol='warning-full'
						onClose={this.handleDismissClick}
						stripe={stripe}
						title={Liferay.Language.get('scheduled-maintenance')}
						type='warning'
					>
						{sub(
							Liferay.Language.get(
								'a-system-wide-maintenance-has-been-scheduled-to-take-place-on-x-at-x'
							),
							[
								moment(stateStartDate).format('ll'),
								moment(stateStartDate).format('LT')
							]
						)}
					</Alert>
				)}
			</div>
		);
	}
}

export const mapState = (
	store,
	{
		match: {
			params: {groupId}
		}
	}
) => {
	const currentUserId = store.getIn(['currentUser', 'data']);

	const project = store.getIn(['projects', groupId, 'data'], new Project());

	const prevStateStartDate = store.getIn([
		'maintenanceSeen',
		`${groupId}-${currentUserId}`
	]);

	return {
		alertDismissed: prevStateStartDate === project.stateStartDate,
		currentUserId,
		groupId,
		project: store.getIn(['projects', groupId, 'data'], new Project())
	};
};

export default compose(
	withRouter,
	connect(
		mapState,
		{setMaintenanceSeen}
	)
)(MaintenanceAlert);
