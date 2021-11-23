import FaroConstants, {OrderByDirections} from 'shared/util/constants';
import {
	ACCOUNT_NAME,
	ACTIVITIES_COUNT,
	buildOrderByFields,
	createOrderByField,
	FAMILY_NAME,
	getDefaultSortOrder,
	GIVEN_NAME,
	invertOrder,
	NAME
} from '../pagination';

const {
	orderAscending,
	orderDefault,
	orderDescending
} = FaroConstants.pagination;

describe('pagination', () => {
	describe('buildOrderByFields', () => {
		it('should build an array of orderByField objects for an individual name', () => {
			expect(
				buildOrderByFields(
					{field: NAME, sortOrder: orderDescending},
					'individuals'
				)
			).toEqual([
				{
					fieldName: GIVEN_NAME,
					orderBy: orderDescending,
					system: false
				},
				{
					fieldName: FAMILY_NAME,
					orderBy: orderDescending,
					system: false
				}
			]);
		});

		it('should build an array of orderByField objects for a segment name', () => {
			expect(
				buildOrderByFields(
					{field: NAME, sortOrder: orderDescending},
					'segments'
				)
			).toEqual([
				{
					fieldName: NAME,
					orderBy: orderDescending,
					system: true
				}
			]);
		});

		it('should build an array of orderByField objects for an account name', () => {
			expect(
				buildOrderByFields(
					{field: NAME, sortOrder: orderDescending},
					'accounts'
				)
			).toEqual([
				{
					fieldName: ACCOUNT_NAME,
					orderBy: orderDescending,
					system: false
				}
			]);
		});

		it('should build an array of orderByField objects', () => {
			expect(
				buildOrderByFields({
					field: ACTIVITIES_COUNT,
					sortOrder: orderDescending
				})
			).toEqual([
				{
					fieldName: ACTIVITIES_COUNT,
					orderBy: orderDescending,
					system: true
				}
			]);
		});
	});

	describe('createOrderByField', () => {
		it('should create an orderByField object', () => {
			expect(createOrderByField(ACCOUNT_NAME, orderDescending)).toEqual({
				fieldName: ACCOUNT_NAME,
				orderBy: orderDescending,
				system: false
			});
		});

		it('should create an orderByField object w/ system as true if fieldName is a system field', () => {
			expect(
				createOrderByField(ACTIVITIES_COUNT, orderDescending)
			).toEqual({
				fieldName: ACTIVITIES_COUNT,
				orderBy: orderDescending,
				system: true
			});
		});
	});

	describe('invertOrder', () => {
		it('should return the opposite order from what was received', () => {
			expect(invertOrder(orderAscending)).toEqual(orderDescending);

			expect(invertOrder(orderDescending)).toEqual(orderAscending);
		});

		it('should return the default order is the current order is falsey', () => {
			const result = invertOrder(null);

			expect(result).toEqual(orderDefault);
		});
	});

	describe('getDefaultSortOrder', () => {
		it('should return orderDescending for a fieldName in the INVERTED_SORT_FIELDS array', () => {
			expect(getDefaultSortOrder(ACTIVITIES_COUNT)).toEqual(
				OrderByDirections.Descending
			);
		});

		it('should return orderAscending for a fieldName NOT in the INVERTED_SORT_FIELDS array', () => {
			expect(getDefaultSortOrder(ACCOUNT_NAME)).toEqual(
				OrderByDirections.Ascending
			);
		});
	});
});
