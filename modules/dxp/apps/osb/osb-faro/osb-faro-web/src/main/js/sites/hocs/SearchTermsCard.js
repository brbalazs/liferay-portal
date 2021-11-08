import BasePage from 'shared/components/base-page';
import CardWithRangeKey from 'shared/hoc/CardWithRangeKey';
import React, {useContext} from 'react';
import SearchTermsQuery from 'shared/queries/SearchTermsQuery';
import {compositionListColumns} from 'shared/util/table-columns';
import {CompositionTypes} from 'shared/util/constants';
import {
	getMapResultToProps,
	mapCardPropsToOptions
} from './mappers/composition-query';
import {graphql} from '@apollo/react-hoc';
import {withTableData} from 'shared/hoc';

const withData = () =>
	graphql(SearchTermsQuery, {
		options: mapCardPropsToOptions,
		props: getMapResultToProps(CompositionTypes.SearchTerms)
	});

const TableWithData = withTableData(withData, {
	getColumns: ({maxCount, totalCount}) => [
		compositionListColumns.getRelativeMetricBar({
			label: `${Liferay.Language.get(
				'search-query'
			)} | ${Liferay.Language.get('searches')}`,
			maxCount,
			showName: true,
			totalCount
		}),
		compositionListColumns.getPercentOf({
			metricName: Liferay.Language.get('searches'),
			totalCount
		})
	],
	rowIdentifier: 'name'
});

const SearchTermsCard = props => {
	const {router} = useContext(BasePage.Context);

	return (
		<CardWithRangeKey
			className='search-terms-card-root'
			label={Liferay.Language.get('search-terms')}
			legacyDropdownRangeKey={false}
		>
			{({rangeSelectors}) => (
				<TableWithData
					rangeSelectors={rangeSelectors}
					router={router}
					rowBordered={false}
					{...props}
				/>
			)}
		</CardWithRangeKey>
	);
};

export default SearchTermsCard;
