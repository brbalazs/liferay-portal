import Card from 'shared/components/Card';
import Constants, {CompositionTypes} from 'shared/util/constants';
import DropdownRangeKey from 'shared/hoc/DropdownRangeKey';
import InterestsQuery from 'shared/queries/InterestsQuery';
import React from 'react';
import {compose} from 'redux';
import {compositionListColumns} from 'shared/util/table-columns';
import {COUNT, createOrderIOMap} from 'shared/util/pagination';
import {
	getMapResultToProps,
	mapPropsToOptions
} from './mappers/composition-query';
import {graphql} from '@apollo/react-hoc';
import {pickBy} from 'lodash';
import {Routes, setUriQueryValues, toRoute} from 'shared/util/router';
import {sub} from 'shared/util/lang';
import {useChannelContext} from 'shared/context/channel';
import {useParams} from 'react-router-dom';
import {useQueryPagination, useQueryRangeSelectors} from 'shared/hooks';
import {withHistory, withPaginationBar, withTableData} from 'shared/hoc';

const {
	pagination: {cur: defaultPage, delta: defaultDelta}
} = Constants;

const withData = () =>
	compose(
		graphql(InterestsQuery, {
			options: mapPropsToOptions,
			props: getMapResultToProps(CompositionTypes.SiteInterests)
		}),
		withPaginationBar({defaultDelta})
	);

const TableWithData = withTableData(withData, {
	getColumns: ({
		channelId,
		groupId,
		maxCount,
		rangeSelectors,
		totalCount
	}) => [
		compositionListColumns.getName({
			label: Liferay.Language.get('topic'),
			routeFn: ({data: {name}}) =>
				name &&
				setUriQueryValues(
					pickBy({...rangeSelectors}),
					toRoute(Routes.SITES_INTEREST_DETAILS, {
						channelId,
						groupId,
						interestId: name
					})
				),
			sortable: false
		}),
		compositionListColumns.getRelativeMetricBar({
			label: Liferay.Language.get('sessions'),
			maxCount,
			totalCount
		}),
		compositionListColumns.getPercentOf({
			metricName: Liferay.Language.get('sessions'),
			totalCount
		})
	],
	rowIdentifier: 'name'
});

const Interests = ({history}) => {
	const {selectedChannel} = useChannelContext();
	const {channelId, groupId} = useParams();
	const {delta, orderIOMap, page} = useQueryPagination({
		initialOrderIOMap: createOrderIOMap(COUNT)
	});
	const rangeSelectors = useQueryRangeSelectors();

	const handleRangeKeyValueChange = ({rangeEnd, rangeKey, rangeStart}) =>
		history.push(
			setUriQueryValues(
				pickBy({
					page: defaultPage,
					rangeEnd,
					rangeKey,
					rangeStart
				})
			)
		);

	return (
		<Card className='sites-interests-root' pageDisplay>
			<Card.Header className='align-items-center d-flex justify-content-between'>
				{selectedChannel && (
					<Card.Title>
						{sub(Liferay.Language.get('interest-topics-on-x'), [
							selectedChannel.name
						])}
					</Card.Title>
				)}

				<DropdownRangeKey
					legacy={false}
					onChange={handleRangeKeyValueChange}
					rangeSelectors={rangeSelectors}
				/>
			</Card.Header>

			<TableWithData
				channelId={channelId}
				delta={delta}
				groupId={groupId}
				orderIOMap={orderIOMap}
				page={page}
				rangeSelectors={rangeSelectors}
				rowBordered={false}
			/>
		</Card>
	);
};

export default withHistory(Interests);
