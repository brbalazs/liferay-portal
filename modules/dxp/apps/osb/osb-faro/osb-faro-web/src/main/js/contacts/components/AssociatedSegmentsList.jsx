import Card from 'shared/components/Card';
import getCN from 'classnames';
import React from 'react';
import SearchableEntityTable from 'shared/components/SearchableEntityTable';
import {
	DATE_CREATED,
	INDIVIDUAL_COUNT,
	NAME,
	paginationConfig,
	paginationDefaults
} from 'shared/util/pagination';
import {getPluralMessage} from 'shared/util/lang';
import {PropTypes} from 'prop-types';
import {segmentsListColumns} from 'shared/util/table-columns';

export default class AssociatedSegmentsList extends React.Component {
	static defaultProps = {
		...paginationDefaults,
		orderByField: NAME
	};

	static propTypes = {
		...paginationConfig,
		channelId: PropTypes.string,
		dataSourceFn: PropTypes.func.isRequired,
		groupId: PropTypes.string.isRequired,
		id: PropTypes.string.isRequired,
		orderByField: PropTypes.string,
		timeZoneId: PropTypes.string,
		total: PropTypes.number
	};

	render() {
		const {
			channelId,
			className,
			dataSourceFn,
			delta,
			filterBy,
			groupId,
			id,
			orderBy,
			orderByField,
			page,
			query,
			timeZoneId,
			total
		} = this.props;

		return (
			<Card
				className={getCN('associated-segments-list-root', className)}
				pageDisplay
			>
				<Card.Header>
					<Card.Title>
						{Liferay.Language.get('associated-segments')}
					</Card.Title>

					<div className='secondary-info'>
						{getPluralMessage(
							Liferay.Language.get('x-segment'),
							Liferay.Language.get('x-segments'),
							total,
							false,
							[
								<b key='SEGMENT_TOTAL'>
									{total.toLocaleString()}
								</b>
							]
						)}
					</div>
				</Card.Header>

				<SearchableEntityTable
					columns={[
						segmentsListColumns.getName({channelId, groupId}),
						segmentsListColumns.individualCount,
						segmentsListColumns.individualAddedDate,
						segmentsListColumns.getDateCreated(timeZoneId)
					]}
					dataSourceFn={dataSourceFn}
					dataSourceParams={{channelId, groupId, id}}
					delta={delta}
					entityLabel={Liferay.Language.get('associated-segments')}
					filterBy={filterBy}
					orderBy={orderBy}
					orderByField={orderByField}
					orderByOptions={[
						{
							label: Liferay.Language.get('name'),
							value: NAME
						},
						{
							label: Liferay.Language.get('members'),
							value: INDIVIDUAL_COUNT
						},
						{
							label: Liferay.Language.get('date-created'),
							value: DATE_CREATED
						}
					]}
					page={page}
					query={query}
					rowIdentifier='id'
				/>
			</Card>
		);
	}
}
