import getSessionMapper from '../experiment-session-variants-mapper';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

const DATA_MOCK = {
	experiment: {
		dxpVariants: [
			{
				control: true,
				dxpVariantName: 'Helen Lamb',
				sessionsHistogram: [
					{
						key: '2018-05-17T00:00',
						value: 206,
						valueKey: '2018-05-17T00:00'
					},
					{
						key: '2018-05-18T00:00',
						value: 91,
						valueKey: '2018-05-18T00:00'
					},
					{
						key: '2018-05-19T00:00',
						value: 189,
						valueKey: '2018-05-19T00:00'
					}
				]
			},
			{
				control: false,
				dxpVariantName: 'Alma Hale',
				sessionsHistogram: [
					{
						key: '2018-05-17T00:00',
						value: 137,
						valueKey: '2018-05-17T00:00'
					},
					{
						key: '2018-05-18T00:00',
						value: 234,
						valueKey: '2018-05-18T00:00'
					},
					{
						key: '2018-05-19T00:00',
						value: 393,
						valueKey: '2018-05-19T00:00'
					}
				]
			},
			{
				control: false,
				dxpVariantName: 'Beatrice Bowman',
				sessionsHistogram: [
					{
						key: '2018-05-17T00:00',
						value: 428,
						valueKey: '2018-05-17T00:00'
					},
					{
						key: '2018-05-18T00:00',
						value: 168,
						valueKey: '2018-05-18T00:00'
					},
					{
						key: '2018-05-19T00:00',
						value: 299,
						valueKey: '2018-05-19T00:00'
					}
				]
			},
			{
				control: false,
				dxpVariantName: 'Lottie Newman',
				sessionsHistogram: [
					{
						key: '2018-05-17T00:00',
						value: 343,
						valueKey: '2018-05-17T00:00'
					},
					{
						key: '2018-05-18T00:00',
						value: 67,
						valueKey: '2018-05-18T00:00'
					},
					{
						key: '2018-05-19T00:00',
						value: 66,
						valueKey: '2018-05-19T00:00'
					}
				]
			}
		]
	}
};

describe('Experiment Session Variants Mapper', () => {
	const mapper = getSessionMapper(DATA_MOCK);

	it('should return formatted data', () => {
		expect(mapper).toMatchSnapshot();
	});

	it('should group values from data', () => {
		expect(mapper.data[0].data).toEqual([206, 91, 189]);
		expect(mapper.data[1].data).toEqual([137, 234, 393]);
		expect(mapper.data[2].data).toEqual([428, 168, 299]);
		expect(mapper.data[3].data).toEqual([343, 67, 66]);
	});

	it('should format number', () => {
		expect(mapper.format(1000)).toEqual('1K');
	});

	it('should get intervals', () => {
		expect(mapper.intervals).toEqual([
			new Date('2018-05-17T00:00:00.000Z'),
			new Date('2018-05-18T00:00:00.000Z'),
			new Date('2018-05-19T00:00:00.000Z')
		]);
	});

	it('should render Tooltip', () => {
		const {container} = render(
			mapper.Tooltip({
				dataPoint: [
					{
						id: 'data1',
						value: 1500,
						x: new Date('2018-05-17T00:00:00.000Z')
					}
				]
			})
		);
		expect(container).toMatchSnapshot();
	});

	it('should return empty true when sessionHistogram to be null', () => {
		expect(getSessionMapper({experiment: {dxpVariants: null}})).toEqual({
			empty: true
		});
	});

	it('should return empty true when sessionHistogram to be an empty array', () => {
		expect(getSessionMapper({experiment: {dxpVariants: []}})).toEqual({
			empty: true
		});
	});
});
