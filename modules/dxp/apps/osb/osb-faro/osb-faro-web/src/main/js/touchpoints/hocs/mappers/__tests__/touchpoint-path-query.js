import {mapPropsToOptions, mapResultToProps} from '../touchpoint-path-query';

const data = {
	page: {
		directAccessMetric: {
			value: 1537.0
		},
		indirectAccessMetric: {
			value: 13212.0
		},
		pageReferrerMetrics: [
			{
				accessMetric: {
					value: 4502.0
				},
				external: false,
				referrer: 'https://www.liferay.com/',
				title: null
			},
			{
				accessMetric: {
					value: 1262.0
				},
				external: true,
				referrer: 'https://www.google.com/',
				title: null
			},
			{
				accessMetric: {
					value: 483.0
				},
				external: true,
				referrer: 'https://www.google.co.in/',
				title: null
			},
			{
				accessMetric: {
					value: 240.0
				},
				external: false,
				referrer: 'https://www.liferay.com/downloads',
				title: null
			},
			{
				accessMetric: {
					value: 124.0
				},
				external: false,
				referrer: 'https://www.liferay.com/digital-experience-platform',
				title: null
			},
			{
				accessMetric: {
					value: 22.0
				},
				external: false,
				referrer: 'https://www.liferay.com/digital-experience-platform',
				title: null
			},
			{
				accessMetric: {
					value: 12.0
				},
				external: false,
				referrer: 'https://www.liferay.com/thanks-for-downloading',
				title: null
			},
			{
				accessMetric: {
					value: 100
				},
				external: false,
				referrer: 'others',
				title: 'Others'
			}
		],
		title: null,
		viewsMetric: {
			value: 2000
		}
	}
};

const context = {
	router: {
		params: {
			title: 'Liferay',
			touchpoint: 'https://liferay.com'
		}
	}
};

const props = {
	filters: {
		devices: ['Desktop'],
		location: ['Brazil']
	},
	rangeSelectors: {rangeKey: '7'},
	router: context.router
};

describe('TouchpointPathQuery Mappers', () => {
	it('should extract percent and total from result', () => {
		const result = mapResultToProps({data, ownProps: props});

		expect(result).toMatchSnapshot();
	});

	it('should include country and device in options', () => {
		expect(mapPropsToOptions(props)).toEqual({
			variables: {
				devices: 'Desktop',
				location: 'Brazil',
				rangeEnd: null,
				rangeKey: 7,
				rangeStart: null,
				title: 'Liferay',
				touchpoint: 'https://liferay.com'
			}
		});
	});

	it('should include country and device in options without filters', () => {
		expect(mapPropsToOptions({...props, filters: {}})).toEqual({
			variables: {
				devices: 'Any',
				location: 'Any',
				rangeEnd: null,
				rangeKey: 7,
				rangeStart: null,
				title: 'Liferay',
				touchpoint: 'https://liferay.com'
			}
		});
	});
});
