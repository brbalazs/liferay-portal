import * as API from 'shared/api';
import ActivitiesChart from '../../../components/ActivitiesChart';
import Button from 'shared/components/Button';
import Card from 'shared/components/Card';
import client from 'shared/apollo/client';
import DropdownRangeKey from 'shared/hoc/DropdownRangeKey';
import IntervalSelector from 'shared/components/IntervalSelector';
import React, {useState} from 'react';
import SearchableVerticalTimeline from 'shared/components/SearchableVerticalTimeline';
import SearchInput from 'shared/components/SearchInput';
import UserSessionQuery, {
	UserSessionData,
	UserSessionVariables
} from 'shared/queries/UserSessionQuery';
import useSelectedPoint from 'shared/hooks/useSelectedPoint';
import useStatefulPagination from 'shared/hooks/useStatefulPagination';
import {EntityTypes, SessionEntityTypes} from 'shared/util/constants';
import {
	formatSessions,
	getActivityLabel,
	getSafeRangeKey,
	INTERVAL_MAP
} from 'shared/util/activities';
import {
	getDateRangeLabel,
	getDateRangeLabelFromDate,
	getEndDate,
	getFirstDate,
	getLastDate
} from 'shared/util/date';
import {getSafeChange} from 'shared/util/change';
import {getSafeRangeSelectors} from 'shared/util/util';
import {Individual} from 'shared/util/records';
import {Interval, RangeSelectors} from 'shared/types';
import {isHourlyRangeKey} from 'shared/util/time';
import {omit} from 'lodash';
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
	} = useStatefulPagination(mapPropsFn);
	const {hasSelectedPoint, onPointSelect, selectedPoint} = useSelectedPoint();
	const [searchValue, setSearchValue] = useState<string>('');

	const handleQuery = (query: string) => {
		setSearchValue(query);
		setQuery(query);
	};

	const getActivities = ({
		channelId,
		contactsEntityId,
		delta,
		page,
		query
	}) => {
		const {rangeEnd, rangeKey, rangeStart} = getSafeRangeSelectors(
			rangeSelectors
		);

		return client
			.query<UserSessionData, UserSessionVariables>({
				query: UserSessionQuery,
				variables: {
					channelId,
					entityId: contactsEntityId,
					entityType: SessionEntityTypes.Individual,
					keywords: query,
					page: page - 1,
					rangeEnd: rangeEnd || null,
					rangeKey: Number(rangeKey),
					rangeStart: rangeStart || null,
					size: delta
				}
			})
			.then(({data: {userSessions}}) => ({
				items: formatSessions(userSessions)
			}));
	};

	const {data: activityData, error, loading, refetch} = useRequest({
		dataSourceFn: API.activities.fetchHistory,
		normalize: ({
			activityAggregations: activityHistory,
			change: activityChange,
			count: activityCount
		}) => ({
			activityChange: getSafeChange(activityChange),
			activityCount,
			activityHistory
		}),
		variables: {
			channelId,
			contactsEntityId: entityId,
			contactsEntityType: EntityTypes.Individual,
			groupId,
			interval: INTERVAL_MAP[interval],
			max: getSafeRangeKey(rangeSelectors?.rangeKey),
			...rangeSelectors
		}
	});

	const handleChangeCustomRange = rangeSelectors => {
		setTimeRangeType(TIMERANGE_TYPE_MAP.TIME_RANGE);
		onRangeSelectorsChange(rangeSelectors);
		onPointSelect(null);
	};

	const handleChartSelect = ({index}) => {
		resetPage();
		onPointSelect(index);
	};

	const handleClearSelection = () => {
		resetPage();
		onPointSelect(null);
	};

	const getDateRange = () => {
		if (!hasSelectedPoint) {
			return {
				endDate: getLastDate(
					activityData?.activityHistory,
					interval,
					'intervalInitDate'
				),
				startDate: getFirstDate(
					activityData?.activityHistory,
					'intervalInitDate'
				)
			};
		}

		const {intervalInitDate} =
			activityData?.activityHistory[selectedPoint] || {};

		return {
			endDate: getEndDate(intervalInitDate, interval),
			startDate: intervalInitDate
		};
	};

	const selected = hasSelectedPoint || selectedPoint;

	const {intervalInitDate, totalElements = 0} =
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
						onChange={onChangeInterval}
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
									? totalElements
									: activityData?.activityCount
								)?.toLocaleString()
							)}
						</div>
					</div>
				</div>
			</Card.Body>

			<SearchableVerticalTimeline
				dataSourceFn={getActivities}
				dataSourceParams={{
					...getDateRange(),
					channelId,
					contactsEntityId: entityId,
					groupId
				}}
				entityLabel={Liferay.Language.get('activities')}
				initialExpanded={false}
				query={query}
				timeZoneId={timeZoneId}
				{...statefulProps}
			/>
		</WrapSafeResults>
	);
};

export default ProfileCard;
