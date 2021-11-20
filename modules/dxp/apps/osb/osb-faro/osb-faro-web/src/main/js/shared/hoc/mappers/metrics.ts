import Constants from 'shared/util/constants';
import {formatItem, getVariables, safeResultToProps} from 'shared/util/mappers';
import {get, isEmpty} from 'lodash';
import {
	getVariableDefinitions,
	GQLQuery,
	removeUnusedVariables
} from 'shared/util/graphql';

const {
	pagination: {cur: defaultPage, delta: defaultDelta}
} = Constants;

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
			column: get(query, 'field', field),
			type: get(query, 'sortOrder', sortOrder.toUpperCase())
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
