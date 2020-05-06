import {mapPropsToOptions, mapResultToProps} from '../visitors-by-time-query';

describe('VisitorsByTimeQuery Mappers', () => {
	it('should map results to props', () => {
		const mockResult = {
			data: {siteVisitorHeatMap: []}
		};

		expect(mapResultToProps(mockResult)).toEqual(
			expect.objectContaining({data: expect.any(Array)})
		);
	});

	it('should map empty results', () => {
		const mockResult = {
			data: {siteVisitorHeatMap: [{value: 0}]}
		};

		expect(mapResultToProps(mockResult)).toEqual(
			expect.objectContaining({data: expect.any(Array), total: 0})
		);
	});

	it('should map props to options', () => {
		const mockProps = {
			rangeKey: '30',
			router: {params: {channelId: 123}},
			timezone: '+07:00'
		};

		expect(mapPropsToOptions(mockProps)).toEqual(
			expect.objectContaining({
				variables: {
					channelId: 123,
					rangeKey: parseInt('30'),
					timezone: '+07:00'
				}
			})
		);
	});
});
