import Dropdown, {DropdownItem} from '../Dropdown';
import React from 'react';
import {shallow} from 'enzyme';

const twoItems = [
	{
		label: 'Item 1',
		value: 'item1'
	},
	{
		label: 'Item 2',
		value: 'item2'
	}
];

describe('Dropdown', () => {
	it('should render', () => {
		const component = shallow(<Dropdown />);

		expect(component).toMatchSnapshot();
	});

	it('should render dropdown with items', () => {
		const component = shallow(<Dropdown items={twoItems} />);

		expect(component).toMatchSnapshot();
	});

	it('should render dropdown with button text', () => {
		const component = shallow(<Dropdown value='Dropdown Button' />);

		expect(component).toMatchSnapshot();
	});

	it('should render a disabled dropdown', () => {
		const component = shallow(<Dropdown disabled items={twoItems} />);

		expect(component).toMatchSnapshot();
	});

	it('should render w/ forceAlignment', () => {
		const component = shallow(<Dropdown forceAlignment items={twoItems} />);

		expect(component).toMatchSnapshot();
	});
});

describe('Dropdown Item', () => {
	it('should render', () => {
		const component = shallow(<DropdownItem />);

		expect(component).toMatchSnapshot();
	});

	it('should render as active', () => {
		const component = shallow(<DropdownItem active />);

		expect(component).toMatchSnapshot();
	});

	it('should render as disabled', () => {
		const component = shallow(<DropdownItem disabled />);

		expect(component).toMatchSnapshot();
	});

	it('should render w/ description', () => {
		const component = shallow(<DropdownItem description='Description' />);

		expect(component).toMatchSnapshot();
	});

	it('should render w/ label', () => {
		const component = shallow(<DropdownItem label='Label' />);

		expect(component).toMatchSnapshot();
	});

	it('should render w/ separator', () => {
		const component = shallow(<DropdownItem separator />);

		expect(component).toMatchSnapshot();
	});

	it('should render w/ icon', () => {
		const component = shallow(<DropdownItem icon='plus' />);

		expect(component).toMatchSnapshot();
	});

	it('should render w/ Link', () => {
		const component = shallow(<DropdownItem href='/touchpoints/123' />);

		expect(component).toMatchSnapshot();
	});
});
