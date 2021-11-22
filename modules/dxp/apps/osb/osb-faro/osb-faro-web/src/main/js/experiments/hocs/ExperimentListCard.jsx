import Card from 'shared/components/Card';
import getColumns from './columns';
import React from 'react';
import URLConstants from 'shared/util/url-constants';
import {sub} from 'shared/util/lang';
import {withBaseResults} from 'shared/hoc';

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

export default ExperimentListCard;
