import {
	createCustomValueMap,
	getCompleteDate,
	getFilterCriterionIMap,
	getIndexFromPropertyName,
	getOperator,
	getPropertyValue,
	removeItemsByIndex,
	setCompleteDate,
	setOperator,
	setPropertyValue
} from '../custom-inputs';
import {LAST_7_DAYS, RELATIONAL_OPERATORS} from '../../utils/constants';

const mockValue = createCustomValueMap([
	{
		key: 'criterionGroup',
		value: [
			{
				operatorName: RELATIONAL_OPERATORS.EQ,
				propertyName: 'context/city',
				value: 'foo'
			},
			{
				operatorName: RELATIONAL_OPERATORS.GT,
				propertyName: 'completeDate',
				value: LAST_7_DAYS
			}
		]
	}
]);

describe('Custom Inputs Util', () => {
	describe('createCustomValueMap', () => {
		it('should create an immutable valueIMap from a given param array', () => {
			expect(
				createCustomValueMap([
					{
						key: 'criterionGroup',
						value: [
							{
								operatorName: RELATIONAL_OPERATORS.EQ,
								propertyName: 'context/city',
								value: 'foo'
							},
							{
								operatorName: RELATIONAL_OPERATORS.GT,
								propertyName: 'completeDate',
								value: LAST_7_DAYS
							}
						]
					}
				])
			).toMatchSnapshot();
		});
	});

	describe('getFilterCriterionIMap', () => {
		it('should return the Filter Criterion Immutable Map', () => {
			expect(
				getFilterCriterionIMap(mockValue, 1).get('propertyName')
			).toBe('completeDate');
		});
	});

	describe('getIndexFromPropertyName', () => {
		it('should return the index of the first entry in the criterion list that matches the propertyName', () => {
			expect(getIndexFromPropertyName(mockValue, 'completeDate')).toBe(1);
		});
	});

	describe('getOperator', () => {
		it('should return the operator', () => {
			expect(getOperator(mockValue, 0)).toBe(RELATIONAL_OPERATORS.EQ);
		});
	});

	describe('getCompleteDate', () => {
		it('should return the time period', () => {
			expect(getCompleteDate(mockValue)).toBe('last7Days');
		});
	});

	describe('getPropertyValue', () => {
		it('should return the value', () => {
			expect(getPropertyValue(mockValue, 'value', 0)).toBe('foo');
		});
	});

	describe('removeItemsByIndex', () => {
		it('should remove items by index from the criteria list', () => {
			const indexToRemove = getIndexFromPropertyName(
				mockValue,
				'completeDate'
			);

			const updatedMockValue = removeItemsByIndex(mockValue, [
				indexToRemove
			]);

			expect(
				getIndexFromPropertyName(updatedMockValue, 'completeDate')
			).toBe(-1);
		});
	});

	describe('setOperator', () => {
		it('should set the operator', () => {
			const updatedValue = setOperator(
				mockValue,
				0,
				RELATIONAL_OPERATORS.NE
			);

			expect(getOperator(updatedValue, 0)).toBe(RELATIONAL_OPERATORS.NE);
		});
	});

	describe('setPropertyValue', () => {
		it('should update the value', () => {
			const newValue = 'new value foo';
			const updatedValue = setPropertyValue(
				mockValue,
				'value',
				0,
				newValue
			);

			expect(getPropertyValue(updatedValue, 'value', 0)).toBe(newValue);
		});
	});

	describe('setCompleteDate', () => {
		it('should update the time period', () => {
			const newTimePeriod = 'fooTimePeriod';
			const updatedValue = setCompleteDate(mockValue, newTimePeriod);

			expect(getCompleteDate(updatedValue)).toBe(newTimePeriod);
		});
	});
});
