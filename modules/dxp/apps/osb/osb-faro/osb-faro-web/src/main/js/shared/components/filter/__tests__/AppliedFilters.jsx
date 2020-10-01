import AppliedFilters from '../AppliedFilters';
import React from 'react';
import {shallow} from 'enzyme';

const filters = {
	Devices: ['Desktop'],
	Location: ['Brazil']
};

describe('AppliedFilters', () => {
	it('should render', () => {
		jest.useFakeTimers();

		const component = shallow(<AppliedFilters filters={filters} />);

		jest.runAllTimers();

		expect(component).toMatchSnapshot();
	});

	it('should render with no filters applied', () => {
		jest.useFakeTimers();

		const component = shallow(<AppliedFilters />);

		jest.runAllTimers();

		expect(component).toMatchSnapshot();
	});

	it('should deactivate the filter when clicking on btn close label', () => {
		const spy = jest.fn();

		jest.useFakeTimers();

		const component = shallow(
			<AppliedFilters filters={filters} onChange={spy} />
		);

		component.instance().handleRemoveFilter({
			category: 'Location',
			label: 'Brazil'
		});

		jest.runAllTimers();

		expect(spy).toBeCalledWith({Devices: ['Desktop'], Location: []});
	});

	it('should deactivate all filters when clicking on "Clear Filter"', () => {
		const spy = jest.fn();

		jest.useFakeTimers();

		const component = shallow(
			<AppliedFilters filters={filters} onChange={spy} />
		);

		component.instance().handleRemoveAllFilters();

		jest.runAllTimers();

		expect(spy).toBeCalled();
	});
});
