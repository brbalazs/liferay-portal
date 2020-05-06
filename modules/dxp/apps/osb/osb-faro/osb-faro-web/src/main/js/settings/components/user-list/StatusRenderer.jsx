import FaroConstants from 'shared/util/constants';
import Label from 'shared/components/Label';
import React from 'react';
import {PropTypes} from 'prop-types';

const {userStatuses} = FaroConstants;

const STATUS_LANG_MAP = {
	[userStatuses.approved]: Liferay.Language.get('approved'),
	[userStatuses.pending]: Liferay.Language.get('pending')
};

class StatusRenderer extends React.Component {
	static defaultProps = {
		data: {}
	};

	static propTypes = {
		data: PropTypes.object
	};

	getDisplayType(status) {
		switch (status) {
			case userStatuses.approved:
				return 'success';
			case userStatuses.pending:
				return 'warning';
			default:
				return '';
		}
	}

	render() {
		const {
			data: {status}
		} = this.props;

		const displayType = this.getDisplayType(status);

		return (
			<td
				className={
					this.props.className ? ` ${this.props.className}` : ''
				}
			>
				<Label display={displayType}>{STATUS_LANG_MAP[status]}</Label>
			</td>
		);
	}
}

export default StatusRenderer;
