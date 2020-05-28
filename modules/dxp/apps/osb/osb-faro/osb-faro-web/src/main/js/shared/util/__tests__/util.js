import dom from 'metal-dom';
import {
	formatStringToLowercase,
	getAlignPosition,
	getPercentage,
	getRangeKeyFromContext,
	getRangeKeyFromTimeRange,
	getRangeSelectorsFromQuery,
	getSafeDisplayValue,
	getSafeRangeSelectors,
	groupData,
	isBlank,
	isEllipisActive,
	truncateText
} from '../util';

describe('util', () => {
	describe('formatStringToLowercase', () => {
		it('should format a string to lowercase', () => {
			const text = '   THIS IS A NOT LOWERCASE TEXT   ';
			const lowercaseText = formatStringToLowercase(text);

			expect(lowercaseText).toEqual('this is a not lowercase text');
		});
	});

	describe('getAlignPosition', () => {
		let source;
		let target;

		afterEach(function() {
			dom.exitDocument(source);
			dom.exitDocument(target);
		});

		beforeEach(function() {
			dom.enterDocument(
				`<div id="source" class="popover clay-popover-top hide analytics-popover no-content">
				<div class="arrow"></div>
				<div class="popover-header">popover header</div>
			</div>`
			);

			dom.enterDocument(
				`<a id="target" data-title="https://www.liferay.com/products/" data-touchpoint="https://www.liferay.com/products/" href="/project/35317/pages/overview/https%3A%2F%2Fwww.liferay.com%2Fproducts" class="table-title">
				<h5 class="mb-1 text-truncate" ref="title">
					https://www.liferay.com/products/
				</h5>
			</a>`
			);

			source = dom.toElement('#source');
			target = dom.toElement('#target');
		});

		it('should return an align position top when it dont have a suggested position', () => {
			expect(getAlignPosition(source, target)).toEqual('top');
		});

		it('should return an align position bottom when it have a suggested position', () => {
			expect(getAlignPosition(source, target, 'bottom')).toEqual(
				'bottom'
			);
		});
	});

	describe('getPercentage', () => {
		it('should convert number to percent passing current number and total number', () => {
			const number1 = 50;
			const number2 = 1000;
			const percent = getPercentage(number1, number2);

			expect(percent).toEqual(5);
		});

		it('should return number 0 if number is invalid, passing current number and total number', () => {
			const number1 = 0;
			const number2 = 0;
			const percent = getPercentage(number1, number2);

			expect(percent).toEqual(0);
		});
	});

	describe('getRangeKeyFromContext', () => {
		it('should return the rangeKey from query passing context', () => {
			const context = {
				rangeKey: {
					defaultValue: '30'
				},
				router: {
					query: {
						rangeKey: '0'
					}
				}
			};

			expect(getRangeKeyFromContext(context)).toEqual('0');
		});

		it('should return the rangeKey from defaultValue passing context', () => {
			const context = {
				rangeKey: {
					defaultValue: '30'
				},
				router: {
					query: {}
				}
			};

			expect(getRangeKeyFromContext(context)).toEqual('30');
		});
	});

	describe('getRangeKeyFromTimeRange', () => {
		const timeRange = [
			{
				default: false,
				rangeKey: 7
			},
			{
				default: false,
				rangeKey: 0
			},
			{
				default: false,
				rangeKey: 28
			},
			{
				default: true,
				rangeKey: 30
			},
			{
				default: false,
				rangeKey: 90
			},
			{
				default: false,
				rangeKey: 1
			}
		];

		it('should return defaultValue and lastValue passing as parameter the timeFilter', () => {
			expect(getRangeKeyFromTimeRange(timeRange)).toEqual({
				defaultValue: '30',
				lastValue: '90'
			});
		});

		it('should return a default object when do not exist timeRange', () => {
			expect(getRangeKeyFromTimeRange()).toEqual({
				defaultValue: '30',
				lastValue: '90'
			});
		});
	});

	describe('getSafeDisplayValue', () => {
		it.each`
			value        | expected
			${0}         | ${0}
			${123}       | ${123}
			${undefined} | ${'-'}
			${null}      | ${'-'}
			${''}        | ${'-'}
			${'test'}    | ${'test'}
		`(
			'should return $expected if the value is $value',
			({expected, value}) => {
				expect(getSafeDisplayValue(value, '-')).toBe(expected);
			}
		);
	});

	describe('groupData', () => {
		it('should returns an array with the parsed data grouped with the max informed value', () => {
			const data = [
				{
					data: [
						{
							color: '#4B9BFF',
							id: 'data1',
							percentage: 68.42105263157895,
							type: 'Windows',
							views: 65
						},
						{
							color: '#FFB46E',
							id: 'data2',
							percentage: 14.736842105263156,
							type: 'macOS',
							views: 14
						},
						{
							color: '#FF5F5F',
							id: 'data3',
							percentage: 2.1052631578947367,
							type: 'Linux',
							views: 2
						},
						{
							color: '#50D2A0',
							id: 'data4',
							percentage: 2.1052631578947367,
							type: 'Mac OS X',
							views: 2
						},
						{
							color: '#FF73C3',
							id: 'data5',
							percentage: 2.1052631578947367,
							type: 'Unknown',
							views: 2
						},
						{
							color: '#9ce268',
							id: 'data6',
							percentage: 1.0526315789473684,
							type: 'Ubuntu',
							views: 1
						}
					],
					percentageOfTotal: 90.52631578947368,
					totalViews: 86,
					type: 'Desktop'
				},
				{
					data: [
						{
							color: '#4B9BFF',
							id: 'data1',
							percentage: 5.263157894736842,
							type: 'Android',
							views: 5
						},
						{
							color: '#FFB46E',
							id: 'data2',
							percentage: 3.1578947368421053,
							type: 'iOS',
							views: 3
						}
					],
					percentageOfTotal: 8.421052631578947,
					totalViews: 8,
					type: 'SmartPhone'
				},
				{
					data: [
						{
							id: 'data1',
							percentage: 1.0526315789473684,
							type: 'Windows',
							views: 1
						}
					],
					percentageOfTotal: 1.0526315789473684,
					totalViews: 1,
					type: 'Tablet'
				}
			];
			const max = 6;

			expect(groupData(data, max)).toEqual([
				{
					data: [
						{
							color: '#4B9BFF',
							id: 'data1',
							percentage: 68.42105263157895,
							type: 'Windows',
							views: 65
						},
						{
							color: '#FFB46E',
							id: 'data2',
							percentage: 14.736842105263156,
							type: 'macOS',
							views: 14
						},
						{
							color: '#FF5F5F',
							id: 'data3',
							percentage: 2.1052631578947367,
							type: 'Linux',
							views: 2
						},
						{
							color: '#50D2A0',
							id: 'data4',
							percentage: 2.1052631578947367,
							type: 'Mac OS X',
							views: 2
						},
						{
							color: '#FF73C3',
							id: 'data5',
							percentage: 2.1052631578947367,
							type: 'Unknown',
							views: 2
						},
						{
							color: '#9ce268',
							id: 'data6',
							percentage: 1.0526315789473684,
							type: 'Ubuntu',
							views: 1
						}
					],
					percentageOfTotal: 90.52631578947368,
					totalViews: 86,
					type: 'Desktop'
				},
				{
					data: [
						{
							color: '#4B9BFF',
							id: 'data1',
							percentage: 5.263157894736842,
							type: 'Android',
							views: 5
						},
						{
							color: '#FFB46E',
							id: 'data2',
							percentage: 3.1578947368421053,
							type: 'iOS',
							views: 3
						}
					],
					percentageOfTotal: 8.421052631578947,
					totalViews: 8,
					type: 'SmartPhone'
				},
				{
					data: [
						{
							id: 'data1',
							percentage: 1.0526315789473684,
							type: 'Windows',
							views: 1
						}
					],
					percentageOfTotal: 1.0526315789473684,
					totalViews: 1,
					type: 'Tablet'
				}
			]);
		});
	});

	describe('isBlank', () => {
		it.each`
			value        | expected
			${0}         | ${false}
			${123}       | ${false}
			${undefined} | ${true}
			${null}      | ${true}
			${''}        | ${true}
			${'test'}    | ${false}
		`(
			'should return $expected if the value is $value',
			({expected, value}) => {
				expect(isBlank(value)).toBe(expected);
			}
		);
	});

	describe('isEllipisActive', () => {
		it('should return true if is an ellipsis', () => {
			const event = {
				target: {
					offsetWidth: 100,
					scrollWidth: 200
				}
			};

			expect(isEllipisActive(event)).toBeTruthy();
		});
	});

	describe('truncateText', () => {
		it('should truncate the text', () => {
			const text = 'this is a text that should be truncate';
			const truncatedText = truncateText(text, 25);

			expect(truncatedText).toEqual('this is a text that sh...');
		});

		it('should truncate the text by adding a dot at the end of the text', () => {
			const text = 'this is a text that should be truncate';
			const truncatedText = truncateText(text, 25, '.');

			expect(truncatedText).toEqual('this is a text that shou.');
		});

		it('should truncate the text when it reaches 100 letters', () => {
			const text =
				'this is a text that should be truncate, this is a text that should be truncate, this is a text that should be truncate';
			const truncatedText = truncateText(text);

			expect(truncatedText).toEqual(
				'this is a text that should be truncate, this is a text that should be truncate, this is a text th...'
			);
		});

		it('should not truncate text', () => {
			const text = 'this is a not truncate text';
			const truncatedText = truncateText(text, 30);

			expect(truncatedText).toEqual('this is a not truncate text');
		});
	});

	describe('getRangeSelectorsFromQuery', () => {
		it.each`
			rangeEnd        | rangeKey    | rangeStart      | results
			${''}           | ${'30'}     | ${''}           | ${{rangeEnd: '', rangeKey: '30', rangeStart: ''}}
			${'null'}       | ${'90'}     | ${'null'}       | ${{rangeEnd: null, rangeKey: '90', rangeStart: null}}
			${'2020-04-04'} | ${'CUSTOM'} | ${'2020-04-01'} | ${{rangeEnd: '2020-04-04', rangeKey: 'CUSTOM', rangeStart: '2020-04-01'}}
		`(
			'should convert $rangeEnd, $rangeKey, & $rangeStart to $results',
			({rangeEnd, rangeKey, rangeStart, results}) => {
				expect(
					getRangeSelectorsFromQuery({rangeEnd, rangeKey, rangeStart})
				).toMatchObject(results);
			}
		);
	});

	describe('getSafeRangeSelectors', () => {
		it.each`
			rangeEnd        | rangeKey    | rangeStart      | results
			${''}           | ${'30'}     | ${''}           | ${{rangeEnd: null, rangeKey: 30, rangeStart: null}}
			${null}         | ${'90'}     | ${null}         | ${{rangeEnd: null, rangeKey: 90, rangeStart: null}}
			${'2020-04-04'} | ${'CUSTOM'} | ${'2020-04-01'} | ${{rangeEnd: '2020-04-04', rangeKey: null, rangeStart: '2020-04-01'}}
		`(
			'should convert $rangeEnd, $rangeKey, & $rangeStart to $results',
			({rangeEnd, rangeKey, rangeStart, results}) => {
				expect(
					getSafeRangeSelectors({rangeEnd, rangeKey, rangeStart})
				).toMatchObject(results);
			}
		);
	});
});
