import BasePage from 'shared/components/base-page';
import CardWithRangeKey from 'shared/hoc/CardWithRangeKey';
import FaroConstants from 'shared/util/constants';
import React, {useContext} from 'react';
import SearchTermsQuery from 'sites/queries/SearchTermsQuery';
import {compositionListColumns} from 'shared/util/table-columns';
import {
	getMapResultToProps,
	mapCardPropsToOptions
} from './mappers/composition-query';
import {graphql} from '@apollo/react-hoc';
import {withTableData} from 'shared/hoc';

const {
	compositionTypes: {searchTerms}
} = FaroConstants;

const withData = () =>
	graphql(SearchTermsQuery, {
		options: mapCardPropsToOptions,
		props: getMapResultToProps(searchTerms)
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
