import Constants from 'shared/util/constants';
import {get, isEmpty, isNil, reduce} from 'lodash';
import {
	getVariableDefinitions,
	GQLQuery,
	removeUnusedVariables
} from 'shared/util/graphql';
import {getVariables, safeResultToProps} from 'shared/util/mappers';

const {
	pagination: {cur: defaultPage, delta: defaultDelta}
} = Constants;

const formatItem = item =>
	reduce(
		item,
		(acc, val, key) => {
			if (val && !isNil(val.value)) {
				acc[key] = val.value;
			} else {
				acc[key] = val;
			}

			return acc;
		},
		{}
	);

type GraphQLOptions = {variables: {[key: string]: any}};

export const getMapPropsToOptions: (
	gqlQuery: GQLQuery,
	options?: object
) => (props: {[key: string]: any}) => GraphQLOptions = (
	gqlQuery,
	options = {}
) => ({
	defaultSort: {field, sortOrder},
	filters,
	rangeSelectors,
	router: {params, query}
}) => {
	const delta = parseInt(get(query, 'delta', defaultDelta));
	const page = parseInt(get(query, 'page', defaultPage));

	const {variables} = getVariables({
		filters,
		params,
		rangeSelectors
	});

	// LRAC-6976 POC TEMP
	const useDB = get(query, 'useDB', null) === 'true';

	let unfilteredVariables: any = {
		...variables,
		keywords: get(query, 'query', ''),
		size: delta,
		sort: {
			column: get(query, 'orderByField', field),
			type: get(query, 'orderBy', sortOrder).toUpperCase()
		},
		start: (page - 1) * delta,
		terms: get(params, 'interestId')
	};

	// LRAC-6976 POC TEMP
	if (useDB) {
		unfilteredVariables = {...unfilteredVariables, useDB};
	}

	const validVariables = gqlQuery ? getVariableDefinitions(gqlQuery) : [];

	return {
		variables: isEmpty(validVariables)
			? unfilteredVariables
			: removeUnusedVariables(unfilteredVariables, validVariables),
		...options
	};
};

export const getMapResultToProps = (
	getResults: (result: any) => {items: any; total: any}
) =>
	safeResultToProps(result => {
		const {items, total} = getResults(result);

		const formattedItems = items && items.map(formatItem);

		return {
			empty: !items.length,
			items: formattedItems,
			total
		};
	});

const getMetricsMapper = (
	getResults: (result: any) => {items: any; total: any},
	options: object = {},
	gqlQuery = null
) => ({
	options: getMapPropsToOptions(gqlQuery, options),
	props: getMapResultToProps(getResults)
});

export default getMetricsMapper;
