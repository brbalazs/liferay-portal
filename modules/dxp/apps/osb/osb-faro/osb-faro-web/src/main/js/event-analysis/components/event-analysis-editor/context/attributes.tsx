import React, {createContext, useReducer} from 'react';
import {Attribute, Breakdown, Filter} from 'event-analysis/utils/types';
import {deletePropertyFromObject} from 'shared/util/object';
import {moveItem, replaceAtIndex} from 'shared/util/array';

export enum ActionTypes {
	AddBreakdown = 'ADD_BREAKDOWN',
	AddFilter = 'ADD_FILTER',
	DeleteBreakdown = 'DELETE_BREAKDOWN',
	DeleteFilter = 'DELETE_FILTER',
	DeleteAllAttributes = 'DELETE_ALL_ATTRIBUTES',
	EditBreakdown = 'EDIT_BREAKDOWN',
	EditFilter = 'EDIT_FILTER',
	MoveBreakdown = 'MOVE_BREAKDOWN',
	MoveFilter = 'MOVE_FILTER'
}

export type AddBreakdown = (params: {
	attribute: Attribute;
	attributeId: string;
	breakdown: Breakdown;
}) => void;

export type AddFilter = (params: {
	attribute: Attribute;
	attributeId: string;
	filter: Filter;
}) => void;

export type DeleteAllAttributes = () => void;

export type DeleteBreakdown = (params: {attributeId: string}) => void;
export type DeleteFilter = (params: {attributeId: string}) => void;

export type EditBreakdown = (params: {
	attribute: Attribute;
	attributeId: string;
	breakdown: Breakdown;
	oldAttributeId?: string;
}) => void;

export type EditFilter = (params: {
	attribute: Attribute;
	attributeId: string;
	filter: Filter;
	oldAttributeId?: string;
}) => void;

export type MoveBreakdown = (params: {from: number; to: number}) => void;
export type MoveFilter = (params: {from: number; to: number}) => void;

type AttributesState = {
	attributes: {[key: string]: Attribute};
	breakdownOrder: string[];
	breakdowns: {[key: string]: Breakdown};
	filterOrder: string[];
	filters: {[key: string]: Filter};
};

export const AttributesContext = createContext<
	AttributesState & {
		addBreakdown?: AddBreakdown;
		addFilter?: AddFilter;
		deleteAllAttributes?: DeleteAllAttributes;
		deleteBreakdown?: DeleteBreakdown;
		deleteFilter?: DeleteFilter;
		editBreakdown?: EditBreakdown;
		editFilter?: EditFilter;
		moveBreakdown?: MoveBreakdown;
		moveFilter?: MoveFilter;
	}
>({
	attributes: {},
	breakdownOrder: [],
	breakdowns: {},
	filterOrder: [],
	filters: {}
});

type Action = {
	payload: {
		attribute?: Attribute;
		attributeId?: string;
		breakdown?: Breakdown;
		filter?: Filter;
		from?: number;
		oldAttributeId?: string;
		to?: number;
	};
	type: ActionTypes;
};

const actionHandlers = {
	[ActionTypes.AddBreakdown]: (
		{
			attributes,
			breakdownOrder,
			breakdowns,
			filterOrder,
			filters
		}: AttributesState,
		{payload: {attribute, attributeId, breakdown} = {}}: Action
	): AttributesState => ({
		attributes: Object.assign({[attributeId]: attribute}, attributes),
		breakdownOrder: [...breakdownOrder, attributeId],
		breakdowns: Object.assign({[attributeId]: breakdown}, breakdowns),
		filterOrder,
		filters
	}),
	[ActionTypes.AddFilter]: (
		{
			attributes,
			breakdownOrder,
			breakdowns,
			filterOrder,
			filters
		}: AttributesState,
		{payload: {attribute, attributeId, filter} = {}}: Action
	): AttributesState => ({
		attributes: Object.assign({[attributeId]: attribute}, attributes),
		breakdownOrder,
		breakdowns,
		filterOrder: [...filterOrder, attributeId],
		filters: Object.assign({[attributeId]: filter}, filters)
	}),
	[ActionTypes.DeleteAllAttributes]: (): AttributesState => ({
		attributes: {},
		breakdownOrder: [],
		breakdowns: {},
		filterOrder: [],
		filters: {}
	}),
	[ActionTypes.DeleteBreakdown]: (
		{
			attributes,
			breakdownOrder,
			breakdowns,
			filterOrder,
			filters
		}: AttributesState,
		{payload: {attributeId} = {}}: Action
	): AttributesState => ({
		attributes: filters[attributeId]
			? attributes
			: deletePropertyFromObject(attributeId, attributes),
		breakdownOrder: breakdownOrder.filter(id => id !== attributeId),
		breakdowns: deletePropertyFromObject(attributeId, breakdowns),
		filterOrder,
		filters
	}),
	[ActionTypes.DeleteFilter]: (
		{
			attributes,
			breakdownOrder,
			breakdowns,
			filterOrder,
			filters
		}: AttributesState,
		{payload: {attributeId} = {}}: Action
	): AttributesState => ({
		attributes: breakdowns[attributeId]
			? attributes
			: deletePropertyFromObject(attributeId, attributes),
		breakdownOrder: breakdownOrder.filter(id => id !== attributeId),
		breakdowns,
		filterOrder,
		filters: deletePropertyFromObject(attributeId, filters)
	}),
	[ActionTypes.EditBreakdown]: (
		{
			attributes,
			breakdownOrder,
			breakdowns,
			filterOrder,
			filters
		}: AttributesState,
		{
			payload: {attribute, attributeId, breakdown, oldAttributeId} = {}
		}: Action
	): AttributesState => ({
		attributes: Object.assign(
			filters[attributeId]
				? attributes
				: deletePropertyFromObject(oldAttributeId, attributes),
			{
				[attributeId]: attribute
			}
		),
		breakdownOrder: replaceAtIndex(
			[...breakdownOrder],
			breakdownOrder.findIndex(id => id === oldAttributeId),
			attributeId
		),
		breakdowns: Object.assign(
			deletePropertyFromObject(oldAttributeId, breakdowns),
			{
				[attributeId]: breakdown
			}
		),
		filterOrder,
		filters
	}),
	[ActionTypes.EditFilter]: (
		{
			attributes,
			breakdownOrder,
			breakdowns,
			filterOrder,
			filters
		}: AttributesState,
		{payload: {attribute, attributeId, filter, oldAttributeId} = {}}: Action
	): AttributesState => ({
		attributes: Object.assign(
			breakdowns[attributeId]
				? attributes
				: deletePropertyFromObject(oldAttributeId, attributes),
			{
				[attributeId]: attribute
			}
		),
		breakdownOrder,
		breakdowns,
		filterOrder: replaceAtIndex(
			[...filterOrder],
			filterOrder.findIndex(id => id === oldAttributeId),
			attributeId
		),
		filters: Object.assign(
			deletePropertyFromObject(oldAttributeId, filters),
			{
				[attributeId]: filter
			}
		)
	}),
	[ActionTypes.MoveBreakdown]: (
		{
			attributes,
			breakdownOrder,
			breakdowns,
			filterOrder,
			filters
		}: AttributesState,
		{payload: {from, to} = {}}: Action
	): AttributesState => ({
		attributes,
		breakdownOrder: moveItem([...breakdownOrder], from, to),
		breakdowns,
		filterOrder,
		filters
	}),
	[ActionTypes.MoveFilter]: (
		{
			attributes,
			breakdownOrder,
			breakdowns,
			filterOrder,
			filters
		}: AttributesState,
		{payload: {from, to} = {}}: Action
	): AttributesState => ({
		attributes,
		breakdownOrder,
		breakdowns,
		filterOrder: moveItem([...filterOrder], from, to),
		filters
	})
};

export const attributesReducer = (
	state: AttributesState,
	action: Action
): AttributesState => {
	const handlerFn = actionHandlers[action.type];

	if (handlerFn) {
		return handlerFn(state, action);
	}

	throw new Error('Unhandled action type: ${type}');
};

const defaultState = {
	attributes: {},
	breakdownOrder: [],
	breakdowns: {},
	filterOrder: [],
	filters: {}
};

const AttributesProvider = ({children}: {children: React.ReactNode}) => {
	const [
		{attributes, breakdownOrder, breakdowns, filterOrder, filters},
		attributesDispatch
	] = useReducer(attributesReducer, defaultState);

	const contextValue: {
		addBreakdown: AddBreakdown;
		addFilter: AddFilter;
		attributes: {[key: string]: Attribute};
		breakdownOrder: string[];
		breakdowns: {[key: string]: Breakdown};
		deleteAllAttributes: DeleteAllAttributes;
		deleteBreakdown: DeleteBreakdown;
		deleteFilter: DeleteFilter;
		editBreakdown: EditBreakdown;
		editFilter: EditFilter;
		filterOrder: string[];
		filters: {[key: string]: Filter};
		moveBreakdown: MoveBreakdown;
		moveFilter: MoveFilter;
	} = {
		addBreakdown: payload =>
			attributesDispatch({
				payload,
				type: ActionTypes.AddBreakdown
			}),
		addFilter: payload =>
			attributesDispatch({
				payload,
				type: ActionTypes.AddFilter
			}),
		attributes,
		breakdownOrder,
		breakdowns,
		deleteAllAttributes: () =>
			attributesDispatch({
				payload: {},
				type: ActionTypes.DeleteAllAttributes
			}),
		deleteBreakdown: payload =>
			attributesDispatch({payload, type: ActionTypes.DeleteBreakdown}),
		deleteFilter: payload =>
			attributesDispatch({payload, type: ActionTypes.DeleteFilter}),
		editBreakdown: payload =>
			attributesDispatch({
				payload,
				type: ActionTypes.EditBreakdown
			}),
		editFilter: payload =>
			attributesDispatch({
				payload,
				type: ActionTypes.EditFilter
			}),
		filterOrder,
		filters,
		moveBreakdown: payload =>
			attributesDispatch({payload, type: ActionTypes.MoveBreakdown}),
		moveFilter: payload =>
			attributesDispatch({payload, type: ActionTypes.MoveFilter})
	};

	return (
		<AttributesContext.Provider value={contextValue}>
			{children}
		</AttributesContext.Provider>
	);
};

export const withAttributesProvider = WrappedComponent => props => (
	<AttributesProvider>
		<WrappedComponent {...props} />
	</AttributesProvider>
);

export const withAttributesConsumer = WrappedComponent => props => (
	<AttributesContext.Consumer>
		{attributes => <WrappedComponent {...props} {...attributes} />}
	</AttributesContext.Consumer>
);
