import * as API from 'shared/api';
import AssociatedSegmentsList from 'contacts/components/AssociatedSegmentsList';
import React, {useState} from 'react';
import {connect, ConnectedProps} from 'react-redux';
import {
	createOrderIOMap,
	getDefaultSortOrder,
	NAME
} from 'shared/util/pagination';
import {EntityTypes} from 'shared/util/constants';
import {RootState} from 'shared/store';
import {useQueryPagination} from 'shared/hooks';

const fetchAssociatedSegments = ({
	delta,
	groupId,
	id,
	orderIOMap,
	page,
	query,
	...otherParams
}) =>
	API.individualSegment.search({
		contactsEntityId: id,
		contactsEntityType: EntityTypes.Individual,
		delta,
		groupId,
		orderIOMap,
		page,
		query,
		...otherParams
	});

const connector = connect((store: RootState, {groupId}: {groupId: string}) => ({
	timeZoneId: store.getIn([
		'projects',
		groupId,
		'data',
		'timeZone',
		'timeZoneId'
	])
}));

type PropsFromRedux = ConnectedProps<typeof connector>;

interface IAssociatedSegmentsProps extends PropsFromRedux {
	channelId: string;
	groupId: string;
	id: string;
	timeZoneId: string;
}

const AssociatedSegments: React.FC<IAssociatedSegmentsProps> = ({
	channelId,
	groupId,
	id,
	timeZoneId
}) => {
	const {delta, orderIOMap, page, query} = useQueryPagination({
		initialOrderIOMap: createOrderIOMap(NAME, getDefaultSortOrder(NAME))
	});

	const [total, setTotal] = useState<number>(0);

	const segmentsDataSourceFn = dataSourceParams =>
		fetchAssociatedSegments({channelId, ...dataSourceParams}).then(
			response => {
				setTotal(response.total);

				return response;
			}
		);

	return (
		<AssociatedSegmentsList
			channelId={channelId}
			dataSourceFn={segmentsDataSourceFn}
			delta={delta}
			groupId={groupId}
			id={id}
			orderIOMap={orderIOMap}
			page={page}
			query={query}
			timeZoneId={timeZoneId}
			total={total}
		/>
	);
};

export default connector(AssociatedSegments);
