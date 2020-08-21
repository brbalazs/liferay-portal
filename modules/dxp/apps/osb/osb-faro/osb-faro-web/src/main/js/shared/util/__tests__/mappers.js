import {getVariables, safeResultToProps} from '../mappers';

const mapResultToProps = safeResultToProps(({myData}) => {
	const {myValue} = myData;

	return {
		myValue
	};
});

describe('safeResultToProps', () => {
	it("should not extract data when there's an error in the result", () => {
		const props = mapResultToProps({
			data: {
				error: {
					message: 'fake error message'
				}
			}
		});

		expect(props).toEqual({
			error: {
				message: 'fake error message'
			}
		});
	});

	it('should not extract data while result is still loading', () => {
		const props = mapResultToProps({
			data: {
				loading: true
			}
		});

		expect(props).toEqual({
			loading: true
		});
	});

	it('should return with an error when exception is thrown', () => {
		const error = new Error('error');
		const errorMapResultToProps = safeResultToProps(() => {
			throw error;
		});

		const props = errorMapResultToProps({
			data: {}
		});

		expect(props).toEqual({
			error
		});
	});
});

describe('getVariables', () => {
	const filters = {
		devices: ['Desktop'],
		location: ['Brazil']
	};

	const params = {
		assetId: '12345',
		title: 'Liferay',
		touchpoint: 'Any'
	};

	const rangeKey = '7';

	it('should include variables passing all necessary parameters', () => {
		const variables = getVariables({
			filters,
			params,
			rangeSelectors: {rangeKey}
		});

		expect(variables).toEqual({
			variables: {
				assetId: '12345',
				devices: 'Desktop',
				location: 'Brazil',
				rangeEnd: null,
				rangeKey: 7,
				rangeStart: null,
				title: 'Liferay',
				touchpoint: null
			}
		});
	});

	it('should include variables without filter parameter', () => {
		const variables = getVariables({params, rangeSelectors: {rangeKey}});

		expect(variables).toEqual({
			variables: {
				assetId: '12345',
				rangeEnd: null,
				rangeKey: 7,
				rangeStart: null,
				title: 'Liferay',
				touchpoint: null
			}
		});
	});

	it('should include variables passing filter parameter as empty object', () => {
		const variables = getVariables({
			filters: {},
			params,
			rangeSelectors: {rangeKey}
		});

		expect(variables).toEqual({
			variables: {
				assetId: '12345',
				devices: 'Any',
				location: 'Any',
				rangeEnd: null,
				rangeKey: 7,
				rangeStart: null,
				title: 'Liferay',
				touchpoint: null
			}
		});
	});

	it('should include variables without params parameter', () => {
		const variables = getVariables({
			filters,
			params: {touchpoint: 'https://liferay.com'},
			rangeSelectors: {rangeKey}
		});

		expect(variables).toEqual({
			variables: {
				devices: 'Desktop',
				location: 'Brazil',
				rangeEnd: null,
				rangeKey: 7,
				rangeStart: null,
				title: '',
				touchpoint: 'https://liferay.com'
			}
		});
	});

	it('should include variables passing params parameter without assetId', () => {
		const variables = getVariables({
			filters,
			params: {title: 'Liferay', touchpoint: 'https://liferay.com'},
			rangeSelectors: {rangeKey}
		});

		expect(variables).toEqual({
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

	it('should include variables passing params parameter without title', () => {
		const variables = getVariables({
			filters,
			params: {assetId: '12345', touchpoint: 'Any'},
			rangeSelectors: {rangeKey}
		});

		expect(variables).toEqual({
			variables: {
				assetId: '12345',
				devices: 'Desktop',
				location: 'Brazil',
				rangeEnd: null,
				rangeKey: 7,
				rangeStart: null,
				title: '',
				touchpoint: null
			}
		});
	});

	it('should include interval when returning variables', () => {
		const {variables} = getVariables({
			filters,
			interval: 'foo',
			params: {},
			rangeSelectors: {rangeKey}
		});

		expect(variables.interval).toBe('foo');
	});

	it('should include channelId in the variables object if it was passed', () => {
		const {variables} = getVariables({
			filters,
			params: {assetId: '12345', channelId: '12345'},
			rangeSelectors: {rangeKey}
		});

		expect(variables.channelId).toEqual('12345');
	});
});
