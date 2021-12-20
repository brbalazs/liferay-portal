import EventAnalysisQuery, {
	EventAnalysisData,
	EventAnalysisVariables
} from '../queries/EventAnalysisQuery';
import {ApolloError} from 'apollo-client';
import {Attribute, Breakdown, Event, Filter} from 'event-analysis/utils/types';
import {AttributesState} from '../components/event-analysis-editor/context/attributes';
import {RangeKeyTimeRanges} from 'shared/util/constants';
import {RangeSelectors} from 'shared/types';
import {useMemo} from 'react';
import {useQuery} from '@apollo/react-hooks';

function normalizeItems<T>(data: T[]): {[key: string]: T} {
	return data.reduce((acc, item) => {
		// @ts-ignore property __typename is coming from GraphQL
		delete item.__typename;

		return {
			...acc,
			[item['id']]: item
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

type UseEventAnalysisData = (
	eventAnalysisId: string
) => {
	attributesState: AttributesState;
	compareToPrevious: boolean;
	error: ApolloError;
	event: Event;
	loading: boolean;
	name: string;
	rangeSelectors: RangeSelectors;
};

const useEventAnalysisData: UseEventAnalysisData = eventAnalysisId => {
	const {data, error, loading} = useQuery<
		EventAnalysisData,
		EventAnalysisVariables
	>(EventAnalysisQuery, {
		variables: {
			eventAnalysisId
		}
	});

	const eventAnalysisData = useMemo(() => {
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

			const breakdowns = getItems<Breakdown>(
				eventAnalysisBreakdowns,
				'breakdown'
			);
			const filters = getItems<Filter>(eventAnalysisFilters, 'filter');

			return {
				attributesState: {
					attributes: normalizeItems<Attribute>(
						eventAttributeDefinitions
					),
					breakdownOrder: breakdowns.map(({id}) => id),
					breakdowns: normalizeItems<Breakdown>(breakdowns),
					filterOrder: filters.map(({id}) => id),
					filters: normalizeItems<Filter>(filters)
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

	return {
		error,
		...eventAnalysisData,
		loading
	};
};

export default useEventAnalysisData;
