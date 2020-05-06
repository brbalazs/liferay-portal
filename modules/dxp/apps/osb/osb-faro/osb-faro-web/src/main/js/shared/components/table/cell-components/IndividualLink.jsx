import NameCell from './Name';
import React from 'react';
import {get} from 'lodash';
import {isBlank} from 'shared/util/util';
import {PropTypes} from 'prop-types';
import {Routes, toRoute} from 'shared/util/router';

export default class IndividualLinkCell extends React.Component {
	static propTypes = {
		channelId: PropTypes.string,
		data: PropTypes.shape({
			id: PropTypes.string,
			individualDeleted: PropTypes.bool,
			individualEmail: PropTypes.string,
			individualId: PropTypes.string,
			individualName: PropTypes.string,
			name: PropTypes.string
		}).isRequired,
		disabled: PropTypes.bool,
		groupId: PropTypes.string.isRequired
	};

	render() {
		const {channelId, className, data, disabled, groupId} = this.props;

		const id = data.individualId || data.ownerId || data.id;
		const name = data.name || data.individualName || '-';
		const email =
			get(data, ['properties', 'email']) ||
			data.individualEmail ||
			data.emailAddress;
		const anonymous = isBlank(email);

		return (
			<NameCell
				className={className}
				data={{...data, id, name}}
				disabled={data.individualDeleted || anonymous || disabled}
				routeFn={({data: {id}}) =>
					toRoute(Routes.CONTACTS_INDIVIDUAL, {
						channelId,
						groupId,
						id
					})
				}
			/>
		);
	}
}
