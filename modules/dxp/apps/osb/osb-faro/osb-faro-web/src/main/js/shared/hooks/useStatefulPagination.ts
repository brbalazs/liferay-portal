import Constants from 'shared/util/constants';
import {Map} from 'immutable';
import {NAME} from 'shared/util/pagination';
import {useReducer} from 'react';

export enum ActionType {
	resetPage = 'resetPage',
	setDelta = 'setDelta',
	setFilterBy = 'setFilterBy',
	setOrderBy = 'setOrderBy',
	setOrderByField = 'setOrderByField',
	setOrderByFields = 'setOrderByFields',
	setPage = 'setPage',
	setQuery = 'setQuery'
}

type OrderByFields = Array<{fieldName: string; orderBy: string}>;

type FilterBy = ReturnType<typeof Map>;

interface Action {
	payload?: any;
	type: ActionType;
}

interface State {
	delta: number;
	filterBy: FilterBy;
	orderBy: string;
	orderByField: string;
	orderByFields: OrderByFields;
	page: number;
	query: string;
}

interface statefulPaginationResult extends State {
	resetPage: () => void;
	setDelta: (delta: string) => void;
	setFilterBy: (filterBy: FilterBy) => void;
	setOrderBy: () => void;
	setOrderByField: (orderByField: string) => void;
	setOrderByFields: (orderByFieldsParam: {
		orderByFields: OrderByFields;
		orderParams: {field: string; sortOrder: string};
	}) => void;
	setPage: (page: string) => void;
	setQuery: (query: string) => void;
}

const {
	pagination: {
		cur: DEFAULT_PAGE,
		delta: DEFAULT_DELTA,
		orderAscending,
		orderDefault,
		orderDescending
	}
} = Constants;

const DEFAULT_PAGINATION_PROPS = {
	defaultDelta: DEFAULT_DELTA,
	defaultFilterBy: Map(),
	defaultOrderBy: orderDefault,
	defaultOrderByField: NAME,
	defaultOrderByFields: [
		{
			fieldName: NAME,
			orderBy: orderDefault
		}
	],
	defaultPage: DEFAULT_PAGE,
	defaultQuery: ''
};

const statefulPaginationReducer = (state: State, {payload, type}: Action) => {
	switch (type) {
		case 'resetPage':
			return {
				...state,
				page: DEFAULT_PAGE
			};
		case 'setDelta':
			return {
				...state,
				delta: payload,
				page: DEFAULT_PAGE
			};
		case 'setFilterBy':
			return {
				...state,
				filterBy: payload,
				page: DEFAULT_PAGE
			};
		case 'setOrderBy':
			return {
				...state,
				orderBy: payload,
				page: DEFAULT_PAGE
			};
		case 'setOrderByField':
			return {
				...state,
				orderByField: payload,
				page: DEFAULT_PAGE
			};
		case 'setOrderByFields':
			return {
				...state,
				orderBy: payload.orderBy,
				orderByField: payload.orderByField,
				orderByFields: payload.orderByFields,
				page: DEFAULT_PAGE
			};
		case 'setQuery':
			return {
				...state,
				page: DEFAULT_PAGE,
				query: payload
			};
		case 'setPage':
			return {
				...state,
				page: payload
			};
		default:
			return state;
	}
};

export default function useStatefulPagination(
	mapPropsFn = undefined,
	defaultPaginationProps = {}
): statefulPaginationResult {
	const paginationProps = {
		...DEFAULT_PAGINATION_PROPS,
		...defaultPaginationProps
	};

	const {
		defaultDelta,
		defaultFilterBy,
		defaultOrderBy,
		defaultOrderByField,
		defaultOrderByFields,
		defaultPage,
		defaultQuery
	} = paginationProps;

	const [state, setState] = useReducer(statefulPaginationReducer, {
		delta: defaultDelta,
		filterBy: defaultFilterBy,
		orderBy: defaultOrderBy,
		orderByField: defaultOrderByField,
		orderByFields: defaultOrderByFields,
		page: defaultPage,
		query: defaultQuery
	});

	const resetPage = (): void => {
		setState({
			type: ActionType.resetPage
		});
	};

	const setDelta = (delta: string): void => {
		setState({
			payload: delta,
			type: ActionType.setDelta
		});
	};

	const setFilterBy = (filterBy: string): void => {
		setState({
			payload: filterBy,
			type: ActionType.setFilterBy
		});
	};

	const setOrderBy = (): void => {
		const {orderBy} = state;

		setState({
			payload:
				orderBy === orderAscending ? orderDescending : orderAscending,
			type: ActionType.setOrderBy
		});
	};

	const setOrderByField = (orderByField): void => {
		setState({
			payload: orderByField,
			type: ActionType.setOrderByField
		});
	};

	const setOrderByFields = ({
		orderByFields,
		orderParams
	}: {
		orderByFields: OrderByFields;
		orderParams: {
			field: string;
			sortOrder: string;
		};
	}): void => {
		setState({
			payload: {
				orderBy: orderParams.sortOrder,
				orderByField: orderParams.field,
				orderByFields
			},
			type: ActionType.setOrderByFields
		});
	};

	const setPage = (page: string): void => {
		setState({
			payload: page,
			type: ActionType.setPage
		});
	};

	const setQuery = (query: string): void => {
		setState({
			payload: query,
			type: ActionType.setQuery
		});
	};

	const mappedProps = mapPropsFn ? mapPropsFn(state) : state;

	return {
		...mappedProps,
		resetPage,
		setDelta,
		setFilterBy,
		setOrderBy,
		setOrderByField,
		setOrderByFields,
		setPage,
		setQuery
	};
}
