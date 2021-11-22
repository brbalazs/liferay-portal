import Card from 'shared/components/Card';
import getColumns from './columns';
import React from 'react';
import URLConstants from 'shared/util/url-constants';
import {
	compose,
	withBaseResults,
	withQueryPagination,
	withQueryRangeSelectors
} from 'shared/hoc';
import {createOrderIOMap, MODIFIED_DATE} from 'shared/util/pagination';
import {sub} from 'shared/util/lang';

const ExperimentListCard = props => {
	const {experiments, timeZoneId, ...otherProps} = props;

	const withData = () => WrappedComponent => props => (
		<WrappedComponent {...props} {...otherProps} items={experiments} />
	);

	const TableWithData = withBaseResults(withData, {
		emptyDescription: sub(
			Liferay.Language.get('empty-message-lists'),
			[
				<a
					href={URLConstants.DocumentationLink}
					key='DOCUMENTATION'
					target='_blank'
				>
					{Liferay.Language.get('documentation').toLowerCase()}
				</a>
			],
			false
		),
		emptyTitle: Liferay.Language.get('empty-title-experiments'),
		getColumns: () => getColumns(timeZoneId),
		rowIdentifier: 'id',
		showDropdownRangeKey: false
	});

	return (
		<Card className='experiments-root' pageDisplay>
			<TableWithData
				{...props}
				entityLabel={Liferay.Language.get('tests')}
			/>
		</Card>
	);
};

export default compose(
	withQueryPagination({initialOrderIOMap: createOrderIOMap(MODIFIED_DATE)}),
	withQueryRangeSelectors({})
)(ExperimentListCard);
