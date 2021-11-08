import Card from 'shared/components/Card';
import Constants, {CompositionTypes} from 'shared/util/constants';
import DropdownRangeKey from 'shared/hoc/DropdownRangeKey';
import InterestsQuery from 'shared/queries/InterestsQuery';
import React from 'react';
import {compose} from 'redux';
import {compositionListColumns} from 'shared/util/table-columns';
import {
	getMapResultToProps,
	mapPropsToOptions
} from './mappers/composition-query';
import {getRangeSelectorsFromQuery} from 'shared/util/util';
import {graphql} from '@apollo/react-hoc';
import {pickBy} from 'lodash';
import {Routes, setUriQueryValues, toRoute} from 'shared/util/router';
import {sub} from 'shared/util/lang';
import {useChannelContext} from 'shared/context/channel';
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
		maxCount,
		rangeSelectors,
		router: {
			params: {channelId, groupId}
		},
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

const Interests = ({history, router}) => {
	const {selectedChannel} = useChannelContext();

	const {
		query: {delta, page}
	} = router;

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

	const rangeSelectors = getRangeSelectorsFromQuery(router.query);

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
				delta={delta}
				page={page}
				rangeSelectors={rangeSelectors}
				router={router}
				rowBordered={false}
			/>
		</Card>
	);
};

export default withHistory(Interests);
