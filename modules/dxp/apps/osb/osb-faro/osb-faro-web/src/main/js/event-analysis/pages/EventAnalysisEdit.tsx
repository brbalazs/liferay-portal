import BaseEventAnalysisPage from './BaseEventAnalysisPage';
import ErrorPage from 'shared/pages/ErrorPage';
import React from 'react';
import Spinner from 'shared/components/Spinner';
import {Attribute, Breakdown, Event, Filter} from 'event-analysis/utils/types';
import {AttributesProvider} from '../components/event-analysis-editor/context/attributes';
import {AttributesState} from '../components/event-analysis-editor/context/attributes';
import {
	EventAnalysisData,
	EventAnalysisQuery,
	EventAnalysisVariables
} from '../queries/EventAnalysisQuery';
import {RangeKeyTimeRanges} from 'shared/util/constants';
import {RangeSelectors} from 'shared/types';
import {Routes, toRoute} from 'shared/util/router';
import {useMemo} from 'react';
import {useParams} from 'react-router-dom';
import {useQuery} from '@apollo/react-hooks';

function normalizeItems<T extends {id: string}>(data: T[]): {[key: string]: T} {
	return data.reduce((acc, item) => {
		// @ts-ignore property __typename is coming from GraphQL
		delete item.__typename;

		return {
			...acc,
			[item.id]: item
		};
	}, {});
}

function getItems<T>(items: T[], key: string): Array<T & {id: string}> {
	return items.map(item => ({
		...item,
		id: `${key}-${Date.now()}-${item['attributeId']}`
	}));
}

function normalizeRangeSelectors(
	rangeSelectors: RangeSelectors
): RangeSelectors {
	const {rangeEnd, rangeStart} = rangeSelectors;

	if (rangeEnd && rangeStart) {
		return {
			...rangeSelectors,
			rangeKey: RangeKeyTimeRanges.CustomRange
		};
	}

	return rangeSelectors;
}

interface BreakdownWithId extends Breakdown {
	id: string;
}

interface FilterWithId extends Filter {
	id: string;
}

type FormattedData = {
	attributesState: AttributesState;
	compareToPrevious: boolean;
	event: Event;
	name: string;
	rangeSelectors: RangeSelectors;
};

const EventAnalysisEdit: React.FC<React.HTMLAttributes<HTMLElement>> = () => {
	const {channelId, groupId, id: eventAnalysisId} = useParams();
	const {data, error, loading} = useQuery<
		EventAnalysisData,
		EventAnalysisVariables
	>(EventAnalysisQuery, {
		variables: {
			eventAnalysisId
		}
	});

	const formattedData = useMemo<FormattedData>(() => {
		if (data) {
			const {
				eventAnalysis: {
					compareToPrevious,
					eventAnalysisBreakdowns,
					eventAnalysisFilters,
					name,
					rangeEnd,
					rangeKey,
					rangeStart,
					referencedObjects: {
						eventAttributeDefinitions,
						eventDefinition
					}
				}
			} = data;

			const breakdowns: BreakdownWithId[] = getItems<Breakdown>(
				eventAnalysisBreakdowns,
				'breakdown'
			);
			const filters: FilterWithId[] = getItems<Filter>(
				eventAnalysisFilters,
				'filter'
			);

			return {
				attributesState: {
					attributes: normalizeItems<Attribute>(
						eventAttributeDefinitions
					),
					breakdownOrder: breakdowns.map(({id}) => id),
					breakdowns: normalizeItems<BreakdownWithId>(breakdowns),
					filterOrder: filters.map(({id}) => id),
					filters: normalizeItems<FilterWithId>(filters)
				},
				compareToPrevious,
				event: eventDefinition,
				name,
				rangeSelectors: normalizeRangeSelectors({
					rangeEnd,
					rangeKey,
					rangeStart
				})
			};
		}
	}, [data]);

	if (loading) {
		return <Spinner alignCenter key='LOADING_DISPLAY' />;
	}

	if (error) {
		return (
			<ErrorPage
				href={toRoute(Routes.EVENT_ANALYSIS, {
					channelId,
					groupId
				})}
				linkLabel={Liferay.Language.get('go-to-event-analysis')}
				message={Liferay.Language.get(
					'the-analysis-you-are-looking-for-does-not-exist'
				)}
				subtitle={Liferay.Language.get('analysis-not-found')}
			/>
		);
	}

	const {attributesState, ...otherData} = formattedData;

	return (
		<AttributesProvider initialState={attributesState}>
			<BaseEventAnalysisPage {...otherData} />
		</AttributesProvider>
	);
};

export default EventAnalysisEdit;
