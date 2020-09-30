import GeoLocation from '../GeoMapCard';
import React from 'react';
import {shallow} from 'enzyme';
jest.unmock('clay-charts');

const data = [
	{
		group: 'United States',
		id: 'United States',
		name: 'United States',
		total: 6911,
		value: '37.7'
	},
	{
		group: 'Brazil',
		id: 'Brazil',
		name: 'Brazil',
		total: 6274,
		value: '34.3'
	},
	{
		group: 'India',
		id: 'India',
		name: 'India',
		total: 574,
		value: '3.1'
	},
	{
		group: 'Spain',
		id: 'Spain',
		name: 'Spain',
		total: 490,
		value: '2.7'
	},
	{
		group: 'Italy',
		id: 'Italy',
		name: 'Italy',
		total: 463,
		value: '2.5'
	},
	{
		color: '#CCCCCC',
		group: 'Others',
		id: 'others',
		name: 'Others',
		total: 3603,
		value: '19.7'
	}
];

const props = {
	countries: data,
	data,
	filters: {},
	loading: false
};
describe('GeoMapCard', () => {
	let component;

	afterEach(() => {
		if (component) {
			component.unmount();
		}
	});

	it('should render GeoMap', () => {
		const component = shallow(<GeoLocation {...props} />);
		expect(component).toMatchSnapshot();
	});

	it('should render GeoMap again when data changes', () => {
		const component = shallow(<GeoLocation {...props} />);

		const newData = [
			{
				group: 'Canada',
				id: 'Canada',
				name: 'Canada',
				total: 100,
				value: '100'
			}
		];

		component.setProps({
			countries: newData,
			data: newData
		});
		expect(component).toMatchSnapshot();
	});

	it('should get select path based on country name', async() => {
		const component = await shallow(<GeoLocation {...props} />);
		const instance = component.instance();

		const spainPath = {
			group: 'Spain',
			id: 'Spain',
			name: 'Spain',
			total: 490,
			value: '2.7'
		};

		const pathSelected = instance.getPathSelected('Spain');
		expect(pathSelected.properties.name).toBe(spainPath.name);
	});

	it('should render component list with lighten-item class when mouseover on table line', () => {
		const component = shallow(<GeoLocation {...props} />);
		component
			.find('.analytics-geomap-table > tbody > tr')
			.first()
			.simulate('mouseover');
		expect(component).toMatchSnapshot();
	});

	it('should render component list without lighten-item class when mouseleave on table line', () => {
		const component = shallow(<GeoLocation {...props} />);
		component
			.find('.analytics-geomap-table > tbody > tr')
			.first()
			.simulate('mouseover');
		component
			.find('.analytics-geomap-table > tbody > tr')
			.first()
			.simulate('mouseleave');

		expect(component).toMatchSnapshot();
	});

	it('should render component with empty message', () => {
		const component = shallow(<GeoLocation {...props} empty />);
		expect(component).toMatchSnapshot();
	});
});
