import Card from 'shared/components/Card';
import Constants, {
	jobRunFrequencies,
	jobRunStatuses
} from 'shared/util/constants';
import Label from 'shared/components/Label';
import moment from 'moment';
import React from 'react';
import RecommendationJobRunsQuery from '../queries/RecommendationJobRunsQuery';
import Table from 'shared/components/table';
import {compose} from 'redux';
import {getFormattedTitle} from 'shared/components/NoResultsDisplay';
import {getMapResultToProps} from 'shared/hoc/mappers/metrics';
import {graphql} from '@apollo/react-hoc';
import {
	JOB_RUN_FREQUENCIES_LABEL_MAP,
	JOB_RUN_STATUSES_DISPLAY_MAP,
	JOB_RUN_STATUSES_LABEL_MAP
} from '../utils/utils';
import {RouterType} from 'shared/types';
import {sub} from 'shared/util/lang';
import {withEmpty} from 'cerebro-shared/hocs/utils';
import {
	withError,
	withLoading,
	withPaginationBar,
	withStatefulPagination
} from 'shared/hoc';

const {
	pagination: {orderDescending}
} = Constants;

const DATE_FORMAT = 'MMM DD, YYYY';

const getContextItemCount = (contextItemKey: string) => (
	context: {key: string; value: any}[]
): number => {
	const contextItem = context.find(({key}) => key === contextItemKey);

	if (contextItem) {
		return contextItem.value.toLocaleString();
	}

	return 0;
};

interface IOutputVersionsCardProps {
	nextRunDate: string;
	router: RouterType;
	runFrequency: jobRunFrequencies;
}

const withData = () =>
	graphql(RecommendationJobRunsQuery, {
		options: ({
			delta,
			jobId,
			orderBy,
			orderByField,
			page
		}: {
			delta: number;
			jobId: string;
			orderBy: string;
			orderByField: string;
			page: number;
		}) => ({
			fetchPolicy: 'no-cache',
			variables: {
				jobId,
				size: delta,
				sort: {
					column: orderByField,
					type: orderBy.toUpperCase()
				},
				start: (page - 1) * delta
			}
		}),
		props: getMapResultToProps(({jobRuns: {jobRuns, total}}) => ({
			items: jobRuns,
			total
		}))
	});

const TableWithData = compose(
	withData(),
	withPaginationBar({defaultDelta: 5}),
	withLoading({alignCenter: true, page: false}),
	withError({page: false}),
	withEmpty({
		emptyTitle: getFormattedTitle(
			Liferay.Language.get('output-versions').toLowerCase()
		)
	})
)(Table);

const OutputVersionsListWithData = withStatefulPagination(
	TableWithData,
	{
		defaultDelta: 5,
		defaultOrderby: orderDescending,
		defaultOrderByField: 'completedDate'
	},
	({onOrderByFieldChange, ...otherProps}) => ({
		onSortChange: onOrderByFieldChange,
		...otherProps
	}),
	false
);

const OutputVersionsCard: React.FC<IOutputVersionsCardProps> = ({
	nextRunDate,
	router,
	runFrequency
}) => {
	const {
		params: {jobId}
	} = router;

	return (
		<Card className='output-versions-card-root'>
			<Card.Header className='d-flex justify-content-between'>
				<Card.Title>
					{Liferay.Language.get('output-versions')}
				</Card.Title>

				<div className='training-frequency'>
					{Liferay.Language.get('training-frequency')}

					<b>{JOB_RUN_FREQUENCIES_LABEL_MAP[runFrequency]}</b>

					<b>{`(${sub(Liferay.Language.get('next-x'), [
						moment(nextRunDate).toNow()
					])})`}</b>
				</div>
			</Card.Header>

			<Card.Body>
				<OutputVersionsListWithData
					columns={[
						{
							accessor: 'runDate',
							className: 'table-cell-expand',
							dataFormatter: val =>
								moment.utc(val).calendar(null, {
									lastDay: DATE_FORMAT,
									lastWeek: DATE_FORMAT,
									nextDay: DATE_FORMAT,
									nextWeek: DATE_FORMAT,
									sameDay: `[${Liferay.Language.get(
										'today'
									)}]`,
									sameElse: DATE_FORMAT
								}),
							label: Liferay.Language.get('training-date'),
							sortable: false,
							title: true
						},
						{
							accessor: 'context',
							className: 'table-column-text-end',
							dataFormatter: getContextItemCount(
								'userItemInteractionsDatasetCount'
							),
							label: Liferay.Language.get('events'),
							sortable: false
						},
						{
							accessor: 'context',
							className: 'table-column-text-end',
							dataFormatter: getContextItemCount(
								'itemsDatasetCount'
							),
							label: Liferay.Language.get('items'),
							sortable: false
						},
						{
							accessor: 'status',
							cellRenderer: ({
								className,
								data: {status}
							}: {
								className: string;
								data: {status: jobRunStatuses};
							}) => (
								<td className={className}>
									<Label
										className='status'
										display={
											JOB_RUN_STATUSES_DISPLAY_MAP[status]
										}
										size='lg'
										uppercase
									>
										{JOB_RUN_STATUSES_LABEL_MAP[status]}
									</Label>
								</td>
							),
							className: 'table-column-text-end',
							label: Liferay.Language.get('status'),
							sortable: false
						}
					]}
					jobId={jobId}
					noResultsProps={{spacer: true}}
					paginationProps={{showDeltaDropdown: false}}
					router={router}
				/>
			</Card.Body>
		</Card>
	);
};

export default OutputVersionsCard;
