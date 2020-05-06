import Criteria, {criteriaFromJS} from '../Criteria';
import FaroConstants from 'shared/util/constants';
import {List} from 'immutable';

const {clauseOperators, criterionTypes} = FaroConstants;

const {1: behaviorBefore, 13: behaviorEquals} = clauseOperators;

const BEHAVIOR_LOGICAL_ID = 20;

describe('Criteria', () => {
	it('should return a new Criteria', () => {
		const criteria = new Criteria();

		expect(criteria).toBeTruthy();
	});

	it('should return a new criteria with a list of children criterias', () => {
		const criteriaChildren1 = new Criteria({id: 1});
		const criteriaChildren2 = new Criteria({id: 2});

		const criteriaList = List(criteriaChildren1, criteriaChildren2);

		const criteria = new Criteria({childContactsCriterion: criteriaList});

		expect(criteria.childContactsCriterion.size).toEqual(criteriaList.size);
	});

	it('should updates the operatorId and the Criterion Type', () => {
		const criteria = new Criteria({operatorId: behaviorEquals.id});
		expect(criteria.type).toEqual(criterionTypes.behavior);

		const criteriaUpdates = criteria.setOperatorId(BEHAVIOR_LOGICAL_ID);
		expect(criteriaUpdates.type).toEqual(criterionTypes.logical);
	});

	it('should return a JSON of a new Criteria', () => {
		const criteria = new Criteria();
		const criteriaJSON = criteria.toJSON();

		expect(criteriaJSON).toBeObject();
	});

	describe('getCriterionType', () => {
		it('should be a behavior type when the operator is a behaviorEquals', () => {
			const criteria = new Criteria({operatorId: behaviorEquals.id});

			expect(criteria.type).toEqual(criterionTypes.behavior);
		});

		it('should be a demographic type when the operator is a behaviorBefore', () => {
			const criteria = new Criteria({operatorId: behaviorBefore.id});

			expect(criteria.type).toEqual(criterionTypes.demographic);
		});

		it('should be a logical type when the operator is a behaviorLogical', () => {
			const criteria = new Criteria({operatorId: BEHAVIOR_LOGICAL_ID});

			expect(criteria.type).toEqual(criterionTypes.logical);
		});
	});

	describe('criteriaFromJS', () => {
		it('should render a criteria from a plain javascript object', () => {
			const criteria = new Criteria();

			expect(criteriaFromJS(criteria)).toBeTruthy();
		});
	});
});
