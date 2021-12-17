import EventAnalysisQuery, {
	EventAnalysisData,
	EventAnalysisVariables
} from '../queries/EventAnalysisQuery';
import {
	Attribute,
	Attributes,
	Breakdown,
	Breakdowns,
	Event,
	Filter,
	Filters
} from 'event-analysis/utils/types';
import {AttributesState} from '../components/event-analysis-editor/context/attributes';
import {useMemo} from 'react';
import {useQuery} from '@apollo/react-hooks';

type NormalizeItems<T> = (array: T[]) => {[key: string]: T};

const normalizeItems: NormalizeItems<Filter | Breakdown | Attribute> = data =>
	data.reduce((acc, item) => {
		// @ts-ignore property __typename is coming from GraphQL
		delete item.__typename;

		return {
			...acc,
			[item['id']]: item
		};
	}, {});

function getItems<T>(items: T[], key: string): Array<T & {id: string}> {
	return items.map(item => ({
		...item,
		id: `${key}-${Date.now()}-${item['attributeId']}`
	}));
}

type UseEventAnalysisData = (
	eventAnalysisId: string
) => {
	attributesState: AttributesState;
	compareToPrevious: boolean;
	event: Event;
	loading: boolean;
	name: string;
};

const useEventAnalysisData: UseEventAnalysisData = eventAnalysisId => {
	const {data, loading} = useQuery<EventAnalysisData, EventAnalysisVariables>(
		EventAnalysisQuery,
		{
			variables: {
				eventAnalysisId
			}
		}
	);

	const eventAnalysisData = useMemo(() => {
		if (data) {
			const {
				eventAnalysis: {
					compareToPrevious,
					eventAnalysisBreakdowns,
					eventAnalysisFilters,
					eventAnalysisId,
					name,
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
					attributes: normalizeItems(
						eventAttributeDefinitions
					) as Attributes,
					breakdownOrder: breakdowns.map(({id}) => id),
					breakdowns: normalizeItems(breakdowns) as Breakdowns,
					filterOrder: filters.map(({id}) => id),
					filters: normalizeItems(filters) as Filters
				},
				compareToPrevious,
				event: eventDefinition,
				eventAnalysisId,
				name
			};
		}
	}, [data]);

	return {
		...eventAnalysisData,
		loading
	};
};

export default useEventAnalysisData;
