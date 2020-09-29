import * as API from 'shared/api';
import AssociatedSegmentsList from 'contacts/components/AssociatedSegmentsList';
import autobind from 'autobind-decorator';
import FaroConstants from 'shared/util/constants';
import omitDefinedProps from 'shared/util/omitDefinedProps';
import React from 'react';
import {connect} from 'react-redux';
import {PropTypes} from 'prop-types';

const {entityTypes} = FaroConstants;

function fetchAssociatedSegments({id, orderBy, orderByField, ...otherData}) {
	return API.individualSegment.search({
		...otherData,
		contactsEntityId: id,
		contactsEntityType: entityTypes.account,
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
		groupId: PropTypes.string.isRequired,
		id: PropTypes.string.isRequired,
		timeZoneId: PropTypes.string.isRequired
	};

	state = {
		total: 0
	};

	@autobind
	segmentsDataSourceFn(dataSourceParams) {
		return fetchAssociatedSegments(dataSourceParams).then(response => {
			this.setState({
				total: response.total
			});

			return response;
		});
	}

	render() {
		const {
			props: {groupId, id, timeZoneId, ...otherProps},
			state: {total}
		} = this;

		return (
			<AssociatedSegmentsList
				{...omitDefinedProps(otherProps, AssociatedSegments.propTypes)}
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
