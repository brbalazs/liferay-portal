import ActivitiesChart from 'contacts/components/ActivitiesChart';
import Button from 'shared/components/Button';
import Card from 'shared/components/Card';
import client from 'shared/apollo/client';
import DropdownRangeKey from 'shared/hoc/DropdownRangeKey';
import EmptyStateDashboard from 'shared/components/EmptyStateDashboard';
import EventMetricQuery, {
	EventMetricsData,
	EventMetricsVariables
} from 'shared/queries/EventMetricQuery';
import IntervalSelector from 'shared/components/IntervalSelector';
import moment from 'moment';
import React, {useCallback, useState} from 'react';
import SearchableVerticalTimeline from 'shared/components/SearchableVerticalTimeline';
import SearchInput from 'shared/components/SearchInput';
import UserSessionQuery, {
	UserSessionData,
	UserSessionVariables
} from 'shared/queries/UserSessionQuery';
import useSelectedPoint from 'shared/hooks/useSelectedPoint';
import useStatefulPagination from 'shared/hooks/useStatefulPagination';
import {
	FORMAT,
	formatUTCDate,
	getDateRangeLabel,
	getDateRangeLabelFromDate,
	getEndDate
} from 'shared/util/date';
import {
	formatSessions,
	getActivityLabel,
	VerticalTimelineHeader,
	VerticalTimelineSession
} from 'shared/util/activities';
import {getSafeRangeSelectors} from 'shared/util/util';
import {Individual} from 'shared/util/records';
import {Interval, RangeSelectors, SafeRangeSelectors} from 'shared/types';
import {isHourlyRangeKey} from 'shared/util/time';
import {isNil, omit} from 'lodash';
import {RangeKeyTimeRanges, SessionEntityTypes} from 'shared/util/constants';
import {sub} from 'shared/util/lang';
import {useRequest} from 'shared/hooks';
import {WrapSafeResults} from 'shared/hoc/util';

interface IProfileCardProps extends React.HTMLAttributes<HTMLElement> {
	channelId: string;
	entity: Individual;
	groupId: string;
	interval: Interval;
	onChangeInterval: (Interval) => void;
	onRangeSelectorsChange: (RangeSelectors) => void;
	rangeSelectors: RangeSelectors;
	tabId: string;
	timeZoneId: string;
}

const mapPropsFn = props => omit(props, 'onSearchValueChange');

const DEFAULT_SESSIONS_DELTA = 50;

const ProfileCard: React.FC<IProfileCardProps> = ({
	channelId,
	entity: {id: entityId},
	groupId,
	interval,
	onChangeInterval,
	onRangeSelectorsChange,
	rangeSelectors,
	timeZoneId
}) => {
	const {
		query,
		resetPage,
		setQuery,
		...statefulPagination
	} = useStatefulPagination(mapPropsFn, {
		defaultDelta: DEFAULT_SESSIONS_DELTA
	});
	const {hasSelectedPoint, onPointSelect, selectedPoint} = useSelectedPoint();
	const [searchValue, setSearchValue] = useState<string>('');

	const getHistory = ({
		channelId,
		contactsEntityId,
		contactsEntityType,
		interval,
		query,
		rangeEnd,
		rangeKey,
		rangeStart
	}): Promise<{
		activityCount: number;
		activityHistory: {
			intervalInitDate: number;
			totalEvents: number;
			totalSessions: number;
		}[];
	}> =>
		client
			.query<EventMetricsData, EventMetricsVariables>({
				query: EventMetricQuery,
				variables: {
					channelId,
					entityId: contactsEntityId,
					entityType: contactsEntityType,
					interval,
					keywords: query,
					rangeEnd,
					rangeKey,
					rangeStart
				}
			})
			.then(({data: {eventMetric}}) => ({
				activityCount: eventMetric.totalEventsMetric?.value,
				activityHistory: eventMetric.totalEventsMetric.histogram.metrics?.map(
					({key, value}, index) => ({
						intervalInitDate: moment.utc(key).valueOf(),
						totalEvents: value,
						totalSessions:
							eventMetric.totalSessionsMetric.histogram.metrics?.[
								index
							].value
					})
				)
			}));

	const {data: activityData, error, loading, refetch} = useRequest({
		dataSourceFn: getHistory,
		variables: {
			channelId,
			contactsEntityId: entityId,
			contactsEntityType: SessionEntityTypes.Individual,
			interval,
			query,
			...getSafeRangeSelectors(rangeSelectors)
		}
	});

	const getDateRange = ({
		rangeEnd,
		rangeKey,
		rangeStart
	}: RangeSelectors): SafeRangeSelectors => {
		const {intervalInitDate} =
			activityData?.activityHistory[selectedPoint] || {};
		const endDate = getEndDate(intervalInitDate, interval);

		const hasSelectedDate = !isNil(endDate) && !isNil(intervalInitDate);

		return getSafeRangeSelectors(
			hasSelectedDate
				? {
						rangeEnd: formatUTCDate(
							getEndDate(intervalInitDate, interval),
							FORMAT
						),
						rangeKey,
						rangeStart: formatUTCDate(intervalInitDate, FORMAT)
				  }
				: {rangeEnd, rangeKey, rangeStart}
		);
	};

	const {rangeEnd, rangeKey, rangeStart} = getDateRange(rangeSelectors);

	const getSessions = useCallback(
		({
			channelId,
			contactsEntityId,
			delta,
			page,
			query,
			rangeEnd,
			rangeKey,
			rangeStart
		}: {
			channelId: string;
			contactsEntityId: string;
			delta: number;
			page: number;
			query: string;
			rangeEnd: string;
			rangeKey: RangeKeyTimeRanges;
			rangeStart: string;
		}): Promise<{
			items: (VerticalTimelineHeader | VerticalTimelineSession)[];
			total: number;
		}> =>
			client
				.query<UserSessionData, UserSessionVariables>({
					query: UserSessionQuery,
					variables: {
						channelId,
						entityId: contactsEntityId,
						entityType: SessionEntityTypes.Individual,
						keywords: query,
						page: page - 1,
						rangeEnd,
						rangeKey,
						rangeStart,
						size: delta
					}
				})
				.then(
					({
						data: {
							eventsByUserSessions: {totalEvents, userSessions}
						}
					}) => ({
						items: formatSessions(userSessions),
						total: totalEvents
					})
				),
		[query, rangeEnd, rangeKey, rangeStart]
	);

	const handleChangeCustomRange = (rangeSelectors: RangeSelectors) => {
		onRangeSelectorsChange(rangeSelectors);
		onPointSelect(null);
	};

	const handleChangeInterval = (interval: Interval) => {
		onChangeInterval(interval);
		onPointSelect(null);
	};

	const handleChartSelect = ({index}: {index: number}) => {
		resetPage();
		onPointSelect(index);
	};

	const handleClearSelection = () => {
		resetPage();
		onPointSelect(null);
	};

	const handleQuery = (query: string) => {
		setQuery(query);
		setSearchValue(query);
	};

	const selected = hasSelectedPoint || selectedPoint;

	const {intervalInitDate, totalEvents = 0} =
		activityData?.activityHistory[selectedPoint] || {};

	const date = selected
		? getDateRangeLabelFromDate(intervalInitDate, interval)
		: getDateRangeLabel(
				activityData?.activityHistory,
				interval,
				'intervalInitDate'
		  );

	const statefulProps = {
		...statefulPagination,
		onOrderByFieldChange: statefulPagination.setOrderByFields,
		onOrderByFieldsChange: statefulPagination.setOrderByFields,
		onSearchValueChange: handleQuery,
		paginationProps: {
			onDeltaChange: statefulPagination.setDelta,
			onPageChange: statefulPagination.setPage
		},
		toolbarProps: {
			onFilterByChange: statefulPagination.setFilterBy,
			onOrderByFieldChange: statefulPagination.setOrderByField,
			onOrderClick: statefulPagination.setOrderBy,
			showSearch: false
		}
	};

	return (
		<WrapSafeResults
			className='flex-grow-1'
			error={error}
			errorProps={{
				className: 'flex-grow-1',
				onReload: refetch
			}}
			loading={loading}
			page={false}
			pageDisplay={false}
		>
			<Card.Body>
				<div className='align-items-center d-flex justify-content-end mt-3'>
					<SearchInput
						autoFocus
						className='search-input mr-3'
						onChange={setSearchValue}
						onSubmit={handleQuery}
						placeholder={Liferay.Language.get('search')}
						value={searchValue}
					/>

					<IntervalSelector
						activeInterval={interval}
						className='mr-3'
						disabled={isHourlyRangeKey(rangeSelectors.rangeKey)}
						onChange={handleChangeInterval}
					/>

					<DropdownRangeKey
						legacy={false}
						onChange={handleChangeCustomRange}
						rangeSelectors={rangeSelectors}
					/>
				</div>

				<div className='individuals-activities-chart'>
					<ActivitiesChart
						alwaysShowSelectedTooltip
						hasSelectedPoint={hasSelectedPoint}
						history={activityData?.activityHistory}
						interval={interval}
						onPointSelect={handleChartSelect}
						rangeSelectors={rangeSelectors}
						selectedPoint={selectedPoint}
					/>

					<div className='selected-info'>
						<div className='activities-date d-flex align-items-baseline'>
							<h4>
								{activityData?.activityHistory?.length
									? sub(
											Liferay.Language.get(
												'individuals-events-x'
											),
											[date]
									  )
									: Liferay.Language.get(
											'individuals-events'
									  )}
							</h4>

							{selected && (
								<Button
									display='link'
									onClick={handleClearSelection}
									size='sm'
								>
									{Liferay.Language.get(
										'clear-date-selection'
									)}
								</Button>
							)}
						</div>

						<div className='details'>
							{getActivityLabel(
								(selected
									? totalEvents
									: activityData?.activityCount
								)?.toLocaleString()
							)}
						</div>
					</div>
				</div>
			</Card.Body>

			<SearchableVerticalTimeline
				dataSourceFn={getSessions}
				dataSourceParams={{
					channelId,
					contactsEntityId: entityId,
					groupId,
					rangeEnd,
					rangeKey,
					rangeStart
				}}
				initialExpanded={false}
				noResultsRenderer={() => (
					<EmptyStateDashboard
						description={
							<>
								<p className='mb-1'>
									{Liferay.Language.get(
										'try-a-different-date-range-or-search-term'
									)}
								</p>
							</>
						}
						symbol='ac-satellite'
						title={Liferay.Language.get('no-events-found')}
					/>
				)}
				query={query}
				timeZoneId={timeZoneId}
				{...statefulProps}
			/>
		</WrapSafeResults>
	);
};

export default ProfileCard;
