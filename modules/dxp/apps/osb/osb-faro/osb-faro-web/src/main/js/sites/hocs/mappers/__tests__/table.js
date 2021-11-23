import getTableMapper from '../table';

describe('TableMapper', () => {
	const mapper = getTableMapper(result => result);

	const mockData = [{foo: 'bar'}];

	it('should map results to props', () => {
		expect(
			mapper.props({
				data: mockData,
				ownProps: {}
			})
		).toEqual(expect.objectContaining({items: mockData}));
	});

	it('should map props to options', () => {
		const mockTabId = 'fooTabId';

		const mockProps = {
			activeTabId: mockTabId,
			rangeKey: '30',
			router: {params: {channelId: 123}},
			tabConfig: [{tabId: mockTabId}]
		};

		expect(mapper.options(mockProps)).toMatchSnapshot();
	});
});
