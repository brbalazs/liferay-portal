import dateFns from 'date-fns';
import {
	CONJUNCTIONS,
	CUSTOM_FUNCTION_OPERATORS,
	isKnown,
	isUnknown,
	NOT_OPERATORS,
	PropertyTypes,
	SUPPORTED_OPERATORS_MAP
} from './constants';
import {Criteria, Criterion, CriterionGroup, Operator} from './types';
import {every, isBoolean, isString, isUndefined, map} from 'lodash';
import {FieldContexts, FieldOwnerTypes} from 'shared/util/constants';
import {getUid} from 'metal';
import {
	INDIVIDUAL_PROPERTIES,
	ORGANIZATION_PROPERTIES,
	SESSION_PROPERTIES,
	WEB_BEHAVIORS
} from './constants';
import {Map} from 'immutable';
import {Property} from 'shared/util/records';

const {
	ACCOUNTS_FILTER,
	ACTIVITIES_FILTER_BY_COUNT,
	INTERESTS_FILTER,
	ORGANIZATIONS_FILTER,
	SESSIONS_FILTER
} = CUSTOM_FUNCTION_OPERATORS;

const {
	NOT_ACCOUNTS_FILTER,
	NOT_ACTIVITIES_FILTER_BY_COUNT,
	NOT_ORGANIZATIONS_FILTER,
	NOT_SESSIONS_FILTER
} = NOT_OPERATORS;

const GROUP_ID_NAMESPACE = 'group_';
const ROW_ID_NAMESPACE = 'row_';

export const createInterestProperty = (name: string): Property =>
	new Property({
		entityName: Liferay.Language.get('individual'),
		label: name,
		name,
		propertyKey: 'interest',
		type: PropertyTypes.Interest
	});

/**
 * Creates a new group object with items.
 */
export const createNewGroup = (items: Criteria[]): CriterionGroup => ({
	conjunctionName: CONJUNCTIONS.AND,
	criteriaGroupId: generateGroupId(),
	items
});

/**
 * Generates a unique group id.
 */
export const generateGroupId = (): string => `${GROUP_ID_NAMESPACE}${getUid()}`;

/**
 * Generates a unique row id.
 */
export const generateRowId = (): string => `${ROW_ID_NAMESPACE}${getUid()}`;

/**
 * Gets a list of group ids from a criteria object.
 * Used for disallowing groups to be moved into its own deeper nested groups.
 * Example of returned value: ['group_02', 'group_03']
 */
export const getChildGroupIds = (criteria: Criteria): string[] => {
	let childGroupIds = [];

	if (isCriterionGroup(criteria) && criteria.items.length) {
		childGroupIds = criteria.items.reduce(
			(groupIdList, item) =>
				isCriterionGroup(item)
					? [
							...groupIdList,
							item.criteriaGroupId,
							...getChildGroupIds(item)
					  ]
					: groupIdList,
			[]
		);
	}

	return childGroupIds;
};

/**
 * Gets the property name from the propertyLabel string .
 */
export const getPropertyNameFromRaw = (propertyLabel: string = ''): string => {
	const properties = propertyLabel.split('/');

	return properties.length > 1 ? properties[1] : properties[0];
};

export const getPropertyContextFromRaw = (
	propertyLabel: string = ''
): string => {
	const properties = propertyLabel.split('/');

	return properties.length > 1 ? properties[0] : null;
};

/**
 * Gets the list of operators for a supported type.
 * Used for displaying the operators available for each criteria row.
 */
export const getSupportedOperatorsFromType = (type: string = ''): Operator[] =>
	SUPPORTED_OPERATORS_MAP[type.toLowerCase()] || [];

/**
 * Checks if value is a CriterionGroup.
 */
export const isCriterionGroup = (
	value: CriterionGroup | Criterion
): value is CriterionGroup =>
	!!value && (value as CriterionGroup).items !== undefined;

/**
 * Checks if value is an ImmutableMap
 */
export const isMap = (
	value: Map<string, any> | object
): value is Map<string, any> => Map.isMap(value as Map<string, any>);

/**
 * Checks if value is either isKnown or isUnknown.
 */
export const isOfKnownType = (key: string): boolean =>
	[isKnown, isUnknown].includes(key);

/**
 * Converts an object of key value pairs to a form data object for passing
 * into a fetch body.
 */
export const objectToFormData = (dataObject: object): FormData => {
	const formData = new FormData();

	Object.keys(dataObject).forEach(key => {
		formData.set(key, dataObject[key]);
	});

	return formData;
};

/**
 * Parse an activityKey string into an object.
 */
export const parseActivityKey = (
	activityKey: string = ''
): {eventId: string; id: string; objectType: string} => {
	const [objectType, eventId, id] = activityKey.split('#');

	return {eventId, id, objectType};
};

/**
 * Returns a YYYY-MM-DD date
 * based on a JS Date object
 *
 * @export
 */
export const jsDatetoYYYYMMDD = (dateJsObject: Date): string => {
	const DATE_FORMAT = 'YYYY-MM-DD';
	return dateFns.format(dateJsObject, DATE_FORMAT);
};

/**
 * Finds the matching property based on its Criterion.
 */
export const findPropertyByCriterion = (
	criterion: Criterion,
	referencedPropertiesIMap: Map<string, Map<string, Property>>
): Property => {
	const {operatorName, propertyName, type, value} = criterion;

	if (
		[ACTIVITIES_FILTER_BY_COUNT, NOT_ACTIVITIES_FILTER_BY_COUNT].includes(
			operatorName
		)
	) {
		const {eventId = propertyName} = parseActivityKey(
			(value as Map<string, any>).getIn(
				['criterionGroup', 'items', 0, 'value'],
				''
			)
		);

		return WEB_BEHAVIORS.find(({name}) => name === eventId);
	} else if ([ACCOUNTS_FILTER, NOT_ACCOUNTS_FILTER].includes(operatorName)) {
		return referencedPropertiesIMap.getIn(
			[
				'account',
				getPropertyContextFromRaw(propertyName),
				getPropertyNameFromRaw(propertyName)
			],
			''
		);
	} else if (
		[NOT_ORGANIZATIONS_FILTER, ORGANIZATIONS_FILTER].includes(operatorName)
	) {
		if (getPropertyContextFromRaw(propertyName) !== FieldContexts.Custom) {
			return ORGANIZATION_PROPERTIES.find(
				({name}) => name === propertyName
			);
		}

		return referencedPropertiesIMap.getIn(
			[
				'organization',
				getPropertyContextFromRaw(propertyName),
				getPropertyNameFromRaw(propertyName)
			],
			''
		);
	} else if (
		[SESSIONS_FILTER, NOT_SESSIONS_FILTER].includes(operatorName) ||
		type === PropertyTypes.SessionDateTime
	) {
		return SESSION_PROPERTIES.find(({name}) => name === propertyName);
	} else if (operatorName === INTERESTS_FILTER) {
		return createInterestProperty(propertyName);
	} else if (INDIVIDUAL_PROPERTIES.find(({name}) => name === propertyName)) {
		return INDIVIDUAL_PROPERTIES.find(({name}) => name === propertyName);
	} else {
		return referencedPropertiesIMap.getIn(
			[
				'individual',
				getPropertyContextFromRaw(propertyName),
				getPropertyNameFromRaw(propertyName)
			],
			''
		);
	}
};

export const convertFieldMappingToAccountProperty = (
	fieldMapping:
		| Map<string, any>
		| {
				context: string;
				displayName: string;
				id: string;
				name: string;
				rawType: string;
		  }
): Property => {
	const context = isMap(fieldMapping)
		? fieldMapping.get('context')
		: fieldMapping.context;
	const displayName = isMap(fieldMapping)
		? fieldMapping.get('displayName')
		: fieldMapping.displayName;
	const id = isMap(fieldMapping) ? fieldMapping.get('id') : fieldMapping.id;
	const name = isMap(fieldMapping)
		? fieldMapping.get('name')
		: fieldMapping.name;
	const type = isMap(fieldMapping)
		? fieldMapping.get('rawType')
		: fieldMapping.rawType;

	const CUSTOM_REGEX = /custom-/;

	return new Property({
		entityName: Liferay.Language.get('account'),
		id,
		label: displayName || name,
		name: context ? `${context}/${name}/value` : name,
		propertyKey: FieldOwnerTypes.Account,
		type: CUSTOM_REGEX.test(type)
			? type.toLowerCase()
			: `account-${type.toLowerCase()}`
	});
};

export const convertFieldMappingToIndividualProperty = (
	fieldMapping:
		| Map<string, any>
		| {
				context: string;
				displayName: string;
				id: string;
				name: string;
				ownerType: string;
				rawType: string;
				type: string;
		  }
): Property => {
	const context = isMap(fieldMapping)
		? fieldMapping.get('context')
		: fieldMapping.context;
	const displayName = isMap(fieldMapping)
		? fieldMapping.get('displayName')
		: fieldMapping.displayName;
	const id = isMap(fieldMapping) ? fieldMapping.get('id') : fieldMapping.id;
	const name = isMap(fieldMapping)
		? fieldMapping.get('name')
		: fieldMapping.name;
	const type = isMap(fieldMapping)
		? fieldMapping.get('rawType')
		: fieldMapping.rawType;

	return new Property({
		entityName: Liferay.Language.get('individual'),
		id,
		label: displayName || name,
		name: context ? `${context}/${name}/value` : name,
		propertyKey: FieldOwnerTypes.Individual,
		type: type.toLowerCase()
	});
};

export const convertFieldMappingToOrganizationProperty = (
	fieldMapping:
		| Map<string, any>
		| {
				context: string;
				displayName: string;
				id: string;
				name: string;
				ownerType: string;
				rawType: string;
				type: string;
		  }
): Property => {
	const context = isMap(fieldMapping)
		? fieldMapping.get('context')
		: fieldMapping.context;
	const displayName = isMap(fieldMapping)
		? fieldMapping.get('displayName')
		: fieldMapping.displayName;
	const id = isMap(fieldMapping) ? fieldMapping.get('id') : fieldMapping.id;
	const name = isMap(fieldMapping)
		? fieldMapping.get('name')
		: fieldMapping.name;
	const type = isMap(fieldMapping)
		? fieldMapping.get('rawType')
		: fieldMapping.rawType;

	return new Property({
		entityName: Liferay.Language.get('organization'),
		id,
		label: displayName || name,
		name: context ? `${context}/${name}/value` : name,
		propertyKey: FieldOwnerTypes.Organization,
		type: `organization-${type.toLowerCase()}` as PropertyTypes
	});
};

export const convertFieldMappingsToProperties = (
	fieldMappingsIMap: Map<
		string,
		Map<string, Map<string, Map<string, any>>>
	> = Map()
): Map<string, Map<string, Map<string, Property>>> =>
	fieldMappingsIMap.map((ownerTypeGroup, key) => {
		let conversionFn;

		if (key === FieldOwnerTypes.Account) {
			conversionFn = convertFieldMappingToAccountProperty;
		} else if (key === FieldOwnerTypes.Individual) {
			conversionFn = convertFieldMappingToIndividualProperty;
		} else if (key === FieldOwnerTypes.Organization) {
			conversionFn = convertFieldMappingToOrganizationProperty;
		}

		if (conversionFn) {
			return ownerTypeGroup.map(contextGroup =>
				contextGroup.reduce(
					(acc, fieldMappingIMap, key) =>
						acc.set(key, conversionFn(fieldMappingIMap)),
					Map()
				)
			);
		}
	}) as Map<string, Map<string, Map<string, Property>>>;

/**
 * Check to see if the value is a valid input value.
 * The input value cannot be an empty string or undefined.
 * @returns {boolean}
 */
export const isValid = (value: any): boolean =>
	!(isUndefined(value) || (isString(value) && !value.length));

/**
 * Recursively check through all criterions and invalidates those
 * that do not have a matching property
 */
export const invalidateCriterionWithMissingProperty = (
	criteria: Criteria,
	referencedPropertiesIMap: Map<string, Property>
) => {
	if (isCriterionGroup(criteria)) {
		const {items} = criteria;

		if (items.length) {
			return {
				...criteria,
				items: items.map(criterion =>
					invalidateCriterionWithMissingProperty(
						criterion,
						referencedPropertiesIMap
					)
				)
			};
		}
	} else {
		if (findPropertyByCriterion(criteria, referencedPropertiesIMap)) {
			return criteria;
		}

		return {
			...criteria,
			valid: isBoolean(criteria.valid)
				? false
				: map(criteria.valid, () => false)
		};
	}
};

/**
 * Recursively check through all criteria to see if they're valid.
 */
export const validateSegmentInputs = (criteria: Criteria): boolean => {
	if (isCriterionGroup(criteria)) {
		const {items} = criteria;

		if (items.length) {
			return items.map(validateSegmentInputs).every(Boolean);
		}
	} else if (criteria) {
		if (isBoolean(criteria.valid)) {
			return criteria.valid;
		}

		return every(criteria.valid, Boolean);
	}
};
