import BasePage from 'shared/components/base-page';
import Button from 'shared/components/Button';
import Card from 'shared/components/Card';
import CardWithRangeKey from 'shared/hoc/CardWithRangeKey';
import FaroConstants from 'shared/util/constants';
import Icon from 'shared/components/Icon';
import InterestsQuery from 'sites/queries/InterestsQuery';
import React, {useContext} from 'react';
import {compositionListColumns} from 'shared/util/table-columns';
import {
	getMapResultToProps,
	mapCardPropsToOptions
} from './mappers/composition-query';
import {graphql} from '@apollo/react-hoc';
import {Routes, setUriQueryValues, toRoute} from 'shared/util/router';
import {withTableData} from 'shared/hoc';

const {
	compositionTypes: {siteInterests}
} = FaroConstants;

const withData = () =>
	graphql(InterestsQuery, {
		options: mapCardPropsToOptions,
		props: getMapResultToProps(siteInterests)
	});

const TableWithData = withTableData(withData, {
	getColumns: ({maxCount, totalCount}) => [
		compositionListColumns.getRelativeMetricBar({
			label: `${Liferay.Language.get(
				'interest-topic'
			)} | ${Liferay.Language.get('sessions')}`,
			maxCount,
			showName: true,
			totalCount
		}),
		compositionListColumns.getPercentOf({
			metricName: Liferay.Language.get('sessions'),
			totalCount
		})
	],
	rowIdentifier: 'name'
});

const InterestsCard = props => {
	const {router} = useContext(BasePage.Context);

	const {
		params: {channelId, groupId}
	} = router;

	return (
		<CardWithRangeKey
			className='interests-card-root'
			label={Liferay.Language.get('interests')}
			legacyDropdownRangeKey={false}
		>
			{({rangeSelectors}) => (
				<>
					<TableWithData
						rangeSelectors={rangeSelectors}
						router={router}
						rowBordered={false}
						{...props}
					/>

					<Card.Footer>
						<Button
							display='link'
							href={setUriQueryValues(
								rangeSelectors,
								toRoute(Routes.SITES_INTERESTS, {
									channelId,
									groupId
								})
							)}
							size='sm'
						>
							{Liferay.Language.get('all-interests')}

							<Icon symbol='angle-right' />
						</Button>
					</Card.Footer>
				</>
			)}
		</CardWithRangeKey>
	);
};

export default InterestsCard;
