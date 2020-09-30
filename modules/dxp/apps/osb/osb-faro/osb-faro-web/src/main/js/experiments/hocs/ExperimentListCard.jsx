import Card from 'shared/components/Card';
import getColumns from './columns';
import React from 'react';
import urlConstants from 'shared/util/url-constants';
import {sub} from 'shared/util/lang';
import {withBaseResults} from 'shared/hoc';

const ExperimentListCard = props => {
	const {experiments, timeZoneId, ...otherProps} = props;

	const withData = () => WrappedComponent => props => (
		<WrappedComponent {...props} {...otherProps} items={experiments} />
	);

	const TableWithData = withBaseResults(withData, {
		defaultOrderByField: 'modifiedDate',
		emptyDescription: sub(
			Liferay.Language.get('empty-message-lists'),
			[
				<a
					href={urlConstants.DOCUMENTATION_LINK}
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
				entityLabel={Liferay.Language.get('tests')}
				{...props}
			/>
		</Card>
	);
};

export default ExperimentListCard;
