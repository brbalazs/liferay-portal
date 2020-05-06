import FaroConstants from 'shared/util/constants';
import {fromJS, List, Record} from 'immutable';
import {isNil, startsWith, uniqueId} from 'lodash';

const {clauseOperators, criterionTypes} = FaroConstants;

const {13: behaviorEquals, 16: behaviorNotEquals} = clauseOperators;

const TEMP_ID_PREFIX = 'temp';

/**
 * Returns the matching criterion type for a given operator id.
 * @param {number} operatorId - The id of the operator. See
 * {@link FaroConstants#clauseOperators} and
 * {@link FaroConstants#criterionOperators}.
 * @returns {number} The matching type from {@link FaroConstants#criterionTypes}.
 */
function getCriterionType(operatorId) {
	if (isNil(operatorId)) {
		return null;
	} else if (isBehaviorOperator(operatorId)) {
		return criterionTypes.behavior;
	} else if (clauseOperators[operatorId]) {
		return criterionTypes.demographic;
	} else {
		return criterionTypes.logical;
	}
}

export function isBehaviorOperator(operatorId) {
	return (
		operatorId === behaviorEquals.id || operatorId === behaviorNotEquals.id
	);
}

/**
 * Returns a temporary id for a new {@link Criteria}. This is useful
 * when components are "keyed" based on the id of a criteria.
 * @returns {string}
 */
export function tempId() {
	return uniqueId(TEMP_ID_PREFIX);
}

/**
 * Used to create a criteria from a plain javascript object. This is
 * useful for converting the responses values from API calls.
 * @param {Object} criterias - A plain object matching the criteria fields.
 * @returns {Criteria}
 */
export function criteriaFromJS(criterias = {}) {
	const {
		childContactsCriterion = [],
		entity = null,
		id = 0,
		name = 'rootCriteria',
		operatorId = FaroConstants.criterionOperators.operatorOr
	} = criterias;

	return new Criteria({
		childContactsCriterion,
		entity,
		id,
		name,
		operatorId
	});
}

/**
 * Represents a criteria used to create a segment. It can also
 * be used with certain apis to provide a more advanced query
 * mechanism. Note that this data type forms a tree, where there
 * is a root critieria, that has child criteria, and so on. The leaves of
 * the tree can be identified by the Criteria whose
 * {@link Criteria#childContactsCriterion} are empty.
 * @class
 */
export default class Criteria extends (new Record({
	/**
	 * The criteria's children.
	 * @member {List.<Criteria>} Criteria#childContactsCriterion
	 */
	childContactsCriterion: new List(),

	/**
	 * An associated entity that varies, depending on the
	 * {@link Criteria#operatorId} selected. For example, if the
	 * {@link Criteria#type} is demographic, then this may be
	 * a field mapping.
	 * @member {Object} Criteria#entity
	 */
	entity: null,

	/**
	 * A Boolean representing whether the entity has been removed.
	 * For example, when a field-mapping or asset is deleted after
	 * a data source deletion.
	 */
	entityRemoved: false,

	/**
	 * The id of the criteria. Newly created criteria will have a
	 * temporary id assigned to them, which is replaced with null when
	 * the criteria is serialized to JSON.
	 * @member {number} Criteria#id
	 */
	id: null,

	/**
	 * The human readable name of the criteria. This is usually left blank.
	 * In previous iterations of the segment editor, it was possible to name
	 * a clause, but this is no longer possible. This field is still present
	 * in order to be api compatible.
	 * @member {string} Criteria#name
	 */
	name: '',

	/**
	 * Corresponds to the selected operator id from
	 * {@link FaroConstants#operatorIds}. Use {@link Criteria#setOperatorId}
	 * to update this field.
	 * @member {number}
	 */
	operatorId: null,

	/**
	 * The type of the criterion that should match the selected
	 * {@link Criteria#operatorId}. This is automatically updated when using
	 * {@link Criteria#setOperatorId}.
	 * @member {number}
	 */
	type: null,

	/**
	 * The "parameters" to the selected operator. Each operator takes a
	 * certain number of parameters, which can be viewed in it's "labels"
	 * field in {@link FaroConstants.clauseOperators}.
	 */
	values: new List()
})) {
	constructor({
		childContactsCriterion = new List(),
		entity,
		id = tempId(),
		operatorId,
		values = [],
		...rest
	} = {}) {
		super({
			...rest,
			childContactsCriterion: new List(childContactsCriterion).map(
				child => new Criteria(child)
			),
			entity: fromJS(entity),
			id,
			operatorId,
			type: getCriterionType(operatorId),
			values: fromJS(values)
		});
	}

	/**
	 * Similar to the normal clear method that all Records have,
	 * but also resets the id to its original, temporary id.
	 * @returns {Criteria}
	 */
	clear() {
		return super.clear().set('id', this.id);
	}

	/**
	 * Check if any Criteria contain a removed entity.
	 * Starts from the Root Criteria.
	 * @returns {Boolean} Whether any entities are removed in the Root Criteria.
	 */
	hasRemovedEntities() {
		return this.get('childContactsCriterion').some(criteriaGroup =>
			criteriaGroup
				.get('childContactsCriterion')
				.some(criteria => criteria.entityRemoved)
		);
	}

	/**
	 * Replaces any occurences of a temporary id for with null, which
	 * is what the API expects for new Criteria. This affects the
	 * Criteria and any of its descendants.
	 * @returns {Criteria}
	 */
	normalizeIds() {
		return this.update('id', id =>
			startsWith(id, TEMP_ID_PREFIX) ? null : id
		).update('childContactsCriterion', children =>
			children.map(child => child.normalizeIds())
		);
	}

	/**
	 * Sets the operatorId, and also updates type to the matching
	 * criteria type. This is the preferred way to updating the operator
	 * id of a Critera. Avoid calling set for this property directly.
	 * @param {number} operatorId - The operator id to be set.
	 * @returns {Criteria}
	 */
	setOperatorId(operatorId) {
		return this.merge({
			operatorId,
			type: getCriterionType(operatorId)
		});
	}

	/**
	 * Implements [toJSON]{@link https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/JSON/stringify#toJSON()_behavior}
	 * for {@link Criteria}, but also replaces all of the temp ids first.
	 * @returns {Object} A plain object of the same fields as a Criteria record.
	 */
	toJSON() {
		return this.normalizeIds().toJS();
	}
}
