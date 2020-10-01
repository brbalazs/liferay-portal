import Filter from '..';
import React from 'react';
import {shallow} from 'enzyme';

const MOCK_ITEMS = [
	{
		hasSearch: true,
		items: [
			{
				category: 'Location',
				checked: false,
				inputType: 'radio',
				label: 'Albania',
				value: '2'
			},
			{
				category: 'Location',
				checked: false,
				inputType: 'radio',
				label: 'Brazil',
				value: '2'
			},
			{
				category: 'Location',
				checked: false,
				inputType: 'radio',
				label: 'Jamaica',
				value: '2'
			},
			{
				category: 'Location',
				checked: false,
				inputType: 'radio',
				label: 'United States',
				value: '2'
			},
			{
				category: 'Location',
				checked: false,
				inputType: 'radio',
				label: 'Portugual',
				value: '2'
			}
		],
		label: 'Location',
		name: 'location',
		value: '156'
	},
	{
		hasSearch: false,
		items: [
			{
				category: 'Devices',
				checked: false,
				inputType: 'radio',
				label: 'Desktop',
				value: '9'
			},
			{
				category: 'Devices',
				checked: false,
				inputType: 'radio',
				label: 'EReader',
				value: '9'
			},
			{
				category: 'Devices',
				checked: false,
				inputType: 'radio',
				label: 'Mobile',
				value: '9'
			},
			{
				category: 'Devices',
				checked: false,
				inputType: 'radio',
				label: 'SmartPhone',
				value: '9'
			},
			{
				category: 'Devices',
				checked: false,
				inputType: 'radio',
				label: 'Tablet',
				value: '9'
			}
		],
		label: 'Devices',
		name: 'devices'
	}
];

describe('Filter', () => {
	it('should render', () => {
		const component = shallow(<Filter items={MOCK_ITEMS} />);

		expect(component).toMatchSnapshot();
	});

	it('should call onClick on handleClickApplyFilter', () => {
		const spy = jest.fn();

		const component = shallow(<Filter items={MOCK_ITEMS} onChange={spy} />);

		component.instance().handleClickApplyFilter();

		expect(spy).toBeCalled();
	});

	it('should call onClick on handleUpdateFilters', () => {
		const spy = jest.fn();

		const component = shallow(<Filter items={MOCK_ITEMS} onChange={spy} />);

		const appliedFilters = {test: 'test'};

		component.instance().handleUpdateFilters(appliedFilters);

		expect(spy).toBeCalledWith(appliedFilters);
	});
});
