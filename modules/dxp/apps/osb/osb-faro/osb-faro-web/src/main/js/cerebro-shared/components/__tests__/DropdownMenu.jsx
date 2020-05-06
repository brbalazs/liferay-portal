import DropdownMenu, {InputItem, OptionItem} from '../DropdownMenu';
import React from 'react';
import {shallow} from 'enzyme';

const MOCK_ITEMS = [
	{
		items: [
			{
				category: 'category1',
				checked: true,
				inputType: 'radio',
				label: 'label a',
				value: '1'
			},
			{
				category: 'category1',
				checked: false,
				inputType: 'radio',
				label: 'label b',
				value: '2'
			},
			{
				category: 'category1',
				checked: false,
				inputType: 'radio',
				label: 'label c',
				value: '100'
			},
			{
				category: 'category1',
				checked: false,
				inputType: 'radio',
				label: 'label d',
				value: '50'
			}
		],
		label: 'Location',
		name: 'location',
		value: '100'
	},
	{
		items: [
			{
				category: 'category2',
				checked: true,
				inputType: 'radio',
				items: [
					{
						category: 'category2',
						checked: true,
						inputType: 'radio',
						label: 'label a',
						value: '1'
					},
					{
						category: 'category2',
						checked: false,
						inputType: 'radio',
						label: 'label b',
						value: '2'
					},
					{
						category: 'category2',
						checked: false,
						inputType: 'radio',
						label: 'label c',
						value: '100'
					},
					{
						category: 'category2',
						checked: false,
						inputType: 'radio',
						label: 'label d',
						value: '50'
					}
				],
				label: 'label a',
				value: '1'
			},
			{
				category: 'category2',
				checked: false,
				inputType: 'radio',
				label: 'label b',
				value: '2'
			},
			{
				category: 'category2',
				checked: false,
				inputType: 'radio',
				label: 'label c',
				value: '100'
			},
			{
				category: 'category2',
				checked: false,
				inputType: 'radio',
				label: 'label d',
				value: '50'
			}
		],
		label: 'Devices',
		name: 'devices',
		value: '100'
	}
];

describe('DropdownMenu', () => {
	it('should render', () => {
		const component = shallow(<DropdownMenu />);

		expect(component).toMatchSnapshot();
	});

	it('should render w/ search', () => {
		const component = shallow(<DropdownMenu hasSearch />);

		expect(component).toMatchSnapshot();
	});

	it('should render w/ items', () => {
		const component = shallow(<DropdownMenu items={MOCK_ITEMS} />);

		expect(component).toMatchSnapshot();
	});
});

describe('InputItem', () => {
	it('should render', () => {
		const component = shallow(<InputItem item={MOCK_ITEMS[0]} />);

		expect(component).toMatchSnapshot();
	});
});

describe('OptionItem', () => {
	it('should render', () => {
		const component = shallow(<OptionItem item={MOCK_ITEMS[0]} />);

		expect(component).toMatchSnapshot();
	});
});
