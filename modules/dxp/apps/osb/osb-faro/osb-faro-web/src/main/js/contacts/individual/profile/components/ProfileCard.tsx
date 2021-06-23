import * as API from 'shared/api';
import ActivitiesChart from '../../../components/ActivitiesChart';
import Button from 'shared/components/Button';
import Card from 'shared/components/Card';
import Constants, {EntityTypes} from 'shared/util/constants';
import DropdownRangeKey from 'shared/hoc/DropdownRangeKey';
import IntervalSelector from 'shared/components/IntervalSelector';
import React, {useState} from 'react';
import SearchableVerticalTimeline from 'shared/components/SearchableVerticalTimeline';
import SearchInput from 'shared/components/SearchInput';
import useSelectedPoint from 'shared/hooks/useSelectedPoint';
import useStatefulPagination from 'shared/hooks/useStatefulPagination';
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
import {Individual} from 'shared/util/records';
import {Interval, RangeSelectors} from 'shared/types';
import {isHourlyRangeKey} from 'shared/util/time';
import {omit} from 'lodash';
import {START_TIME} from 'shared/util/pagination';
import {sub} from 'shared/util/lang';
import {useRequest} from 'shared/hooks';
import {WrapSafeResults} from 'shared/hoc/util';

const {
	pagination: {orderDescending}
} = Constants;

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

function getActivities(params) {
	const {
		channelId,
		contactsEntityId,
		delta,
		endDate,
		groupId,
		page,
		query,
		startDate
	} = params;

	return API.activities
		.fetchGroup({
			channelId,
			contactsEntityId,
			contactsEntityType: EntityTypes.Individual,
			cur: page,
			delta,
			endDate,
			groupId,
			orderByFields: [{fieldName: START_TIME, orderBy: orderDescending}],
			query,
			startDate
		})
		.then(({items, total}) => ({
			items: formatSessions(items, groupId, channelId),
			total
		}));
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
			max: getSafeRangeKey(rangeSelectors.rangeKey),
			...rangeSelectors
		}
	});

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
		<Card.Body>
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
						onChange={onRangeSelectorsChange}
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
												'individuals-activities-x'
											),
											[date]
									  )
									: Liferay.Language.get(
											'individuals-activities'
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

				<SearchableVerticalTimeline
					dataSourceFn={getActivities}
					dataSourceParams={{
						...getDateRange(),
						channelId,
						contactsEntityId: entityId,
						groupId
					}}
					entityLabel={Liferay.Language.get('activities')}
					headerLabels={{
						count: Liferay.Language.get('activity-count'),
						label: Liferay.Language.get('time'),
						title: Liferay.Language.get('session')
					}}
					initialExpanded={false}
					query={query}
					timeZoneId={timeZoneId}
					{...statefulProps}
				/>
			</WrapSafeResults>
		</Card.Body>
	);
};

export default ProfileCard;
