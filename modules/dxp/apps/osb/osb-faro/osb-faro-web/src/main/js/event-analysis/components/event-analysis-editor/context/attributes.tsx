import React, {createContext, useReducer} from 'react';
import {Attribute, Breakdown, Filter} from '../types';
import {deletePropertyFromObject} from 'shared/util/object';
import {moveItem, replaceAtIndex} from 'shared/util/array';

export enum ActionTypes {
	AddAttribute = 'addAttribute',
	DeleteAttribute = 'DeleteAttribute',
	DeleteAllAttributes = 'DeleteAllAttributes',
	EditAttribute = 'editAttribute',
	MoveAttribute = 'moveAttribute'
}

export type AddAttribute = (params: {
	attribute: Attribute;
	attributeId: string;
	breakdown: Breakdown;
	filter?: Filter;
}) => void;

export type DeleteAllAttributes = () => void;

export type DeleteAttribute = (params: {attributeId: string}) => void;

export type EditAttribute = (params: {
	attribute: Attribute;
	attributeId: string;
	breakdown: Breakdown;
	filter?: Filter;
	oldAttributeId?: string;
}) => void;

export type MoveAttribute = (params: {from: number; to: number}) => void;

type AttributesState = {
	attributes: {[key: string]: Attribute};
	breakdowns: {[key: string]: Breakdown};
	filters: {[key: string]: Filter};
	order: string[];
};

export const AttributesContext = createContext<
	AttributesState & {
		addAttribute?: AddAttribute;
		deleteAttribute?: DeleteAttribute;
		editAttribute?: EditAttribute;
		moveAttribute?: MoveAttribute;
	}
>({
	attributes: {},
	breakdowns: {},
	filters: {},
	order: []
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
	[ActionTypes.AddAttribute]: (
		{attributes, breakdowns, filters, order}: AttributesState,
		{payload: {attribute, attributeId, breakdown, filter} = {}}: Action
	): AttributesState => ({
		attributes: Object.assign({[attributeId]: attribute}, attributes),
		breakdowns: Object.assign({[attributeId]: breakdown}, breakdowns),
		filters: filter
			? Object.assign({[attributeId]: filter}, filters)
			: filters,
		order: [...order, attributeId]
	}),
	[ActionTypes.DeleteAllAttributes]: () => ({
		attributes: {},
		breakdowns: {},
		filters: {},
		order: []
	}),
	[ActionTypes.DeleteAttribute]: (
		{attributes, breakdowns, filters, order}: AttributesState,
		{payload: {attributeId} = {}}: Action
	) => ({
		attributes: deletePropertyFromObject(attributeId, attributes),
		breakdowns: deletePropertyFromObject(attributeId, breakdowns),
		filters: deletePropertyFromObject(attributeId, filters),
		order: order.filter(id => id !== attributeId)
	}),
	[ActionTypes.EditAttribute]: (
		{attributes, breakdowns, filters, order}: AttributesState,
		{
			payload: {
				attribute,
				attributeId,
				breakdown,
				filter,
				oldAttributeId
			} = {}
		}: Action
	) => ({
		attributes: Object.assign(
			deletePropertyFromObject(oldAttributeId, attributes),
			{
				[attributeId]: attribute
			}
		),
		breakdowns: Object.assign(
			deletePropertyFromObject(oldAttributeId, breakdowns),
			{
				[attributeId]: breakdown
			}
		),
		filters: filter
			? Object.assign(deletePropertyFromObject(oldAttributeId, filters), {
					[attributeId]: filter
			  })
			: deletePropertyFromObject(oldAttributeId, filters),
		order: replaceAtIndex(
			[...order],
			order.findIndex(id => id === oldAttributeId),
			attributeId
		)
	}),
	[ActionTypes.MoveAttribute]: (
		{attributes, breakdowns, filters, order}: AttributesState,
		{payload: {from, to} = {}}: Action
	) => ({
		attributes,
		breakdowns,
		filters,
		order: moveItem([...order], from, to)
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
	breakdowns: {},
	filters: {},
	order: []
};

const AttributesProvider = ({children}: {children: React.ReactNode}) => {
	const [
		{attributes, breakdowns, filters, order},
		attributesDispatch
	] = useReducer(attributesReducer, defaultState);

	const contextValue: {
		addAttribute: AddAttribute;
		attributes: {[key: string]: Attribute};
		breakdowns: {[key: string]: Breakdown};
		deleteAllAttributes: DeleteAllAttributes;
		deleteAttribute: DeleteAttribute;
		editAttribute: EditAttribute;
		filters: {[key: string]: Filter};
		moveAttribute: MoveAttribute;
		order: string[];
	} = {
		addAttribute: payload =>
			attributesDispatch({
				payload,
				type: ActionTypes.AddAttribute
			}),
		attributes,
		breakdowns,
		deleteAllAttributes: () =>
			attributesDispatch({
				payload: {},
				type: ActionTypes.DeleteAllAttributes
			}),
		deleteAttribute: payload =>
			attributesDispatch({payload, type: ActionTypes.DeleteAttribute}),
		editAttribute: payload =>
			attributesDispatch({
				payload,
				type: ActionTypes.EditAttribute
			}),
		filters,
		moveAttribute: payload =>
			attributesDispatch({payload, type: ActionTypes.MoveAttribute}),
		order
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
