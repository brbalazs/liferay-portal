import FaroConstants from 'shared/util/constants';
import {
	getMapResultToProps,
	mapCardPropsToOptions,
	mapPropsToOptions
} from '../composition-query';

const {
	compositionTypes: {siteInterests}
} = FaroConstants;

const mockData = {
	siteInterests: {
		compositions: [{foo: 'bar'}],
		maxCount: 85,
		total: 123,
		totalCount: 321
	}
};

const mockProps = {
	rangeSelectors: {
		rangeKey: '90'
	},
	router: {
		query: {
			delta: '5',
			page: '2',
			rangeKey: '90'
		}
	}
};

describe('Composition Query Mapper', () => {
	describe('getMapResultToProps', () => {
		it('should map interests list query result to props', () => {
			expect(
				getMapResultToProps(siteInterests)({data: mockData})
			).toEqual(
				expect.objectContaining({
					items: expect.any(Array),
					maxCount: expect.any(Number),
					total: expect.any(Number),
					totalCount: expect.any(Number)
				})
			);
		});
	});

	describe('mapCardPropsToOptions', () => {
		it('should map interests list query card props to options', () => {
			const rangeKey = '30';
			const channelId = '321';

			expect(
				mapCardPropsToOptions({
					rangeSelectors: {
						rangeKey
					},
					router: {params: {channelId}}
				})
			).toEqual(
				expect.objectContaining({
					variables: {
						activeTabId: undefined,
						channelId,
						rangeEnd: null,
						rangeKey: parseInt(rangeKey),
						rangeStart: null,
						size: 5,
						start: 0
					}
				})
			);
		});
	});

	describe('mapPropsToOptions', () => {
		it('should map interests list query props to options', () => {
			const {
				query: {delta, rangeKey}
			} = mockProps.router;

			expect(mapPropsToOptions(mockProps)).toEqual(
				expect.objectContaining({
					variables: {
						channelId: undefined,
						rangeEnd: null,
						rangeKey: parseInt(rangeKey),
						rangeStart: null,
						size: parseInt(delta),
						start: 5
					}
				})
			);
		});
	});
});
