import * as API from 'shared/api';
import AssociatedSegmentsList from 'contacts/components/AssociatedSegmentsList';
import autobind from 'autobind-decorator';
import omitDefinedProps from 'shared/util/omitDefinedProps';
import React from 'react';
import {connect} from 'react-redux';
import {EntityTypes} from 'shared/util/constants';
import {Individual} from 'shared/util/records';
import {PropTypes} from 'prop-types';

function fetchAssociatedSegments({id, orderBy, orderByField, ...otherData}) {
	return API.individualSegment.search({
		...otherData,
		contactsEntityId: id,
		contactsEntityType: EntityTypes.Individual,
		orderByFields: [
			{
				fieldName: orderByField,
				orderBy,
				system: true
			}
		]
	});
}

export class AssociatedSegments extends React.Component {
	static propTypes = {
		channelId: PropTypes.string,
		groupId: PropTypes.string.isRequired,
		id: PropTypes.string.isRequired,
		individual: PropTypes.instanceOf(Individual).isRequired,
		timeZoneId: PropTypes.string.isRequired
	};

	state = {
		total: 0
	};

	@autobind
	segmentsDataSourceFn(dataSourceParams) {
		const {channelId} = this.props;

		return fetchAssociatedSegments({channelId, ...dataSourceParams}).then(
			response => {
				this.setState({
					total: response.total
				});

				return response;
			}
		);
	}

	render() {
		const {
			props: {channelId, groupId, id, timeZoneId, ...otherProps},
			state: {total}
		} = this;

		return (
			<AssociatedSegmentsList
				{...omitDefinedProps(otherProps, AssociatedSegments.propTypes)}
				channelId={channelId}
				dataSourceFn={this.segmentsDataSourceFn}
				groupId={groupId}
				id={id}
				timeZoneId={timeZoneId}
				total={total}
			/>
		);
	}
}

export default connect((store, {groupId}) => ({
	timeZoneId: store.getIn([
		'projects',
		groupId,
		'data',
		'timeZone',
		'timeZoneId'
	])
}))(AssociatedSegments);
