import * as API from 'shared/api';
import AssociatedSegmentsList from 'contacts/components/AssociatedSegmentsList';
import autobind from 'autobind-decorator';
import FaroConstants from 'shared/util/constants';
import omitDefinedProps from 'shared/util/omitDefinedProps';
import React from 'react';
import {Individual} from 'shared/util/records';
import {PropTypes} from 'prop-types';

const {entityTypes} = FaroConstants;

function fetchAssociatedSegments({id, orderBy, orderByField, ...otherData}) {
	return API.individualSegment.search({
		...otherData,
		contactsEntityId: id,
		contactsEntityType: entityTypes.individual,
		orderByFields: [
			{
				fieldName: orderByField,
				orderBy,
				system: true
			}
		]
	});
}

export default class AssociatedSegments extends React.Component {
	static propTypes = {
		channelId: PropTypes.string,
		groupId: PropTypes.string.isRequired,
		id: PropTypes.string.isRequired,
		individual: PropTypes.instanceOf(Individual).isRequired
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
			props: {channelId, groupId, id, ...otherProps},
			state: {total}
		} = this;

		return (
			<AssociatedSegmentsList
				{...omitDefinedProps(otherProps, AssociatedSegments.propTypes)}
				channelId={channelId}
				dataSourceFn={this.segmentsDataSourceFn}
				groupId={groupId}
				id={id}
				total={total}
			/>
		);
	}
}
