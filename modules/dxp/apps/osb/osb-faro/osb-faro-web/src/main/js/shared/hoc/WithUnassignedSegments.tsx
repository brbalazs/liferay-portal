import * as API from 'shared/api';
import React, {useEffect} from 'react';
import {
	ActionType,
	useUnassignedSegmentsContext
} from 'shared/context/unassignedSegments';
import {close, modalTypes, open} from 'shared/actions/modals';
import {connect} from 'react-redux';
import {Modal} from 'shared/types';
import {useChannelContext} from 'shared/context/channel';
import {useRequest} from 'shared/hooks';

interface IWrappedComponentProps {
	close: Modal.close;
	open: Modal.open;
}

const withUnassignedSegments = (
	WrappedComponent: React.ComponentType<IWrappedComponentProps>
) =>
	connect(
		null,
		{close, open}
	)(({close, groupId, open, ...otherProps}) => {
		const {
			unassignedSegmentsDispatch,
			unassignedSegmentsTriggered
		} = useUnassignedSegmentsContext();

		const {channels} = useChannelContext();

		const {data, error, loading} = useRequest(
			API.individualSegment.searchUnassigned,
			{
				delta: 10000,
				groupId
			}
		);

		useEffect(() => {
			if (data && !error) {
				const {items, total} = data;

				unassignedSegmentsDispatch({
					payload: items,
					type: ActionType.setSegments
				});

				if (
					!unassignedSegmentsTriggered &&
					!loading &&
					!!total &&
					!!channels.length
				) {
					open(
						modalTypes.UNASSIGNED_SEGMENTS_MODAL,
						{
							groupId,
							onClose: close
						},
						{closeOnBlur: false}
					);
					unassignedSegmentsDispatch({
						type: ActionType.setTriggered
					});
				}
			}
		}, [data, error, loading]);

		return <WrappedComponent groupId={groupId} {...otherProps} />;
	});

export default withUnassignedSegments;
