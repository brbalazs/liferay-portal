import Constants from 'shared/util/constants';
import React, {createContext, useEffect, useReducer} from 'react';
import {
	convertFieldMappingsToProperties,
	getPropertyContextFromRaw,
	getPropertyNameFromRaw
} from '../utils/utils';
import {Map} from 'immutable';
import {Property, Segment} from 'shared/util/records';

const {fieldOwnerTypes} = Constants;

export enum ActionType {
	AddEntities = 'addEntities',
	AddEntity = 'addEntity',
	AddProperty = 'addProperty',
	ReplaceAll = 'replaceAll'
}

export const ACTION_TYPES: {[key: string]: ActionType} = {
	addEntities: ActionType.AddEntities,
	addEntity: ActionType.AddEntity,
	addProperty: ActionType.AddProperty
};

export enum EntityType {
	Assets = 'assets',
	Groups = 'groups',
	Organizations = 'organizations',
	Roles = 'roles',
	Teams = 'teams',
	UserGroups = 'user-groups',
	Users = 'users'
}

export type AddEntities = (params: {
	entityType: EntityType;
	payload: any[];
}) => void;
export type AddEntity = (params: {
	entityType: EntityType;
	payload: any;
}) => void;
export type AddProperty = (payload: Property) => void;

type ReferencedEntities = Map<string, Map<string, Map<string, any>>>;
type ReferencedProperties = Map<string, Map<string, Property>>;

export const ReferencedObjectsContext = createContext<{
	addEntities?: AddEntities;
	addEntity?: AddEntity;
	addProperty?: AddProperty;
	referencedEntities: ReferencedEntities;
	referencedProperties: ReferencedProperties;
}>({
	referencedEntities: Map(),
	referencedProperties: Map()
});

type Action = {
	entityType?: EntityType;
	payload: any;
	type: ActionType;
};

export const referencedPropertiesReducer = (
	state: ReferencedProperties,
	{payload, type}: Action
): ReferencedProperties => {
	switch (type) {
		case ActionType.AddProperty:
			if (
				[
					fieldOwnerTypes.account,
					fieldOwnerTypes.individual,
					fieldOwnerTypes.organization
				].includes(payload.propertyKey)
			) {
				return state.setIn(
					[
						payload.propertyKey,
						getPropertyContextFromRaw(payload.name),
						getPropertyNameFromRaw(payload.name)
					],
					payload
				);
			}

			return state;
		case ActionType.ReplaceAll:
			return payload;
		default:
			throw new Error('Unhandled action type: ${type}');
	}
};

export const referencedEntitiesReducer = (
	state: ReferencedEntities,
	{entityType, payload, type}: Action
): ReferencedEntities => {
	switch (type) {
		case ActionType.AddEntities:
			return state.mergeIn(
				[entityType],
				Map(payload.map(item => [item.get('id'), item]))
			);
		case ActionType.AddEntity:
			return state.setIn(
				[entityType, getPropertyNameFromRaw(payload.get('id'))],
				payload
			);
		case ActionType.ReplaceAll:
			return payload;
		default:
			throw new Error('Unhandled action type: ${type}');
	}
};

const createReferencedEntitiesIMapFromSegment = (
	segment: Segment
): ReferencedEntities => {
	const {referencedObjects} = segment;

	return Map({
		[EntityType.Assets]: referencedObjects.get('assets'),
		[EntityType.Groups]: referencedObjects.get('groups'),
		[EntityType.Organizations]: referencedObjects.get('organizations'),
		[EntityType.Roles]: referencedObjects.get('roles'),
		[EntityType.Teams]: referencedObjects.get('teams'),
		[EntityType.UserGroups]: referencedObjects.get('user-groups'),
		[EntityType.Users]: referencedObjects.get('users')
	});
};

export const ReferencedObjectsProvider = ({
	children,
	segment
}: {
	children: React.ReactNode;
	segment?: Segment;
}) => {
	const [referencedEntities, referencedEntitiesDispatch] = useReducer(
		referencedEntitiesReducer,
		segment
			? createReferencedEntitiesIMapFromSegment(segment)
			: Map<string, any>()
	);

	const [referencedProperties, referencedPropertiesDispatch] = useReducer(
		referencedPropertiesReducer,
		segment
			? (convertFieldMappingsToProperties(
					segment.getIn(['referencedObjects', 'fieldMappings'], Map())
			  ) as Map<string, Map<string, Property>>)
			: Map<string, any>()
	);

	useEffect(() => {
		referencedEntitiesDispatch({
			payload: segment
				? createReferencedEntitiesIMapFromSegment(segment)
				: Map(),

			type: ActionType.ReplaceAll
		});

		referencedPropertiesDispatch({
			payload: segment
				? (convertFieldMappingsToProperties(
						segment.getIn(
							['referencedObjects', 'fieldMappings'],
							Map()
						)
				  ) as Map<string, Map<string, Property>>)
				: Map(),
			type: ActionType.ReplaceAll
		});
	}, [segment]);

	return (
		<ReferencedObjectsContext.Provider
			value={{
				addEntities: ({
					entityType,
					payload
				}: {
					entityType: EntityType;
					payload: any[];
				}) => {
					referencedEntitiesDispatch({
						entityType,
						payload,
						type: ActionType.AddEntities
					});
				},
				addEntity: ({
					entityType,
					payload
				}: {
					entityType: EntityType;
					payload: any;
				}) => {
					referencedEntitiesDispatch({
						entityType,
						payload,
						type: ActionType.AddEntity
					});
				},
				addProperty: (payload: Property) =>
					referencedPropertiesDispatch({
						payload,
						type: ActionType.AddProperty
					}),
				referencedEntities,
				referencedProperties
			}}
		>
			{children}
		</ReferencedObjectsContext.Provider>
	);
};

export const withReferencedObjectsProvider = WrappedComponent => props => (
	<ReferencedObjectsProvider segment={props.segment}>
		<WrappedComponent {...props} />
	</ReferencedObjectsProvider>
);

export const withReferencedObjectsConsumer = WrappedComponent => props => (
	<ReferencedObjectsContext.Consumer>
		{referencedObjects => (
			<WrappedComponent {...props} {...referencedObjects} />
		)}
	</ReferencedObjectsContext.Consumer>
);
