import Constants from 'shared/util/constants';
import {hasChanges} from 'shared/util/react';
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

interface Payload {
	delta?: number;
	filterBy?: ReturnType<typeof Map>;
	orderBy?: string;
	orderByField?: string;
	orderByFields?: {
		fieldName: string;
		orderBy: string;
	}[];
	page?: number;
	query?: string;
}

interface statefulPaginationResult extends Payload {
	resetPage: () => void;
	setDelta: (Payload) => void;
	setFilterBy: (Payload) => void;
	setOrderBy: (Payload) => void;
	setOrderByField: (Payload) => void;
	setOrderByFields: (Payload) => void;
	setPage: (Payload) => void;
	setQuery: (Payload) => void;
}

type Action = {
	payload?: Payload;
	type: ActionType;
};

type State = {
	delta: number;
	filterBy: ReturnType<typeof Map>;
	orderBy: string;
	orderByField: string;
	orderByFields: {
		fieldName: string;
		orderBy: string;
	}[];
	page: number;
	query: string;
};

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
		case 'setFilterBy':
		case 'setOrderBy':
		case 'setOrderByField':
		case 'setOrderByFields':
		case 'setQuery':
			return {
				...state,
				...payload,
				page: DEFAULT_PAGE
			};
		case 'setPage':
			return {
				...state,
				...payload
			};
		default:
			return state;
	}
};

export default function useStatefulPagination(
	mapPropsFn,
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

	const setDelta = (delta): void => {
		setState({
			payload: {delta},
			type: ActionType.setDelta
		});
	};

	const setFilterBy = (filterBy): void => {
		setState({
			payload: {filterBy},
			type: ActionType.setFilterBy
		});
	};

	const setOrderBy = (): void => {
		const {orderBy} = state;

		setState({
			payload: {
				orderBy:
					orderBy === orderAscending
						? orderDescending
						: orderAscending
			},
			type: ActionType.setOrderBy
		});
	};

	const setOrderByField = (orderByField): void => {
		setState({
			payload: {orderByField},
			type: ActionType.setOrderByField
		});
	};

	const setOrderByFields = ({orderByFields, orderParams}): void => {
		if (
			hasChanges(
				state,
				{
					orderBy: orderParams.sortOrder,
					orderByField: orderParams.field,
					orderByFields
				},
				'orderBy',
				'orderByField',
				'orderByFields'
			)
		) {
			setState({
				payload: {
					orderBy: orderParams.sortOrder,
					orderByField: orderParams.field,
					orderByFields
				},
				type: ActionType.setOrderByFields
			});
		}
	};

	const setPage = (page): void => {
		setState({
			payload: {page},
			type: ActionType.setPage
		});
	};

	const setQuery = (query): void => {
		setState({
			payload: {query},
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
