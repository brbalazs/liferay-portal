import React from 'react';
import TableDate from '../TableData';
import {shallow} from 'enzyme';

describe('TableData', () => {
	it('should render', () => {
		const component = shallow(<TableDate />);

		expect(component).toMatchSnapshot();
	});

	it('should render with Link', () => {
		const component = shallow(<TableDate url='foo/bar' />);

		expect(component).toMatchSnapshot();
	});

	it('should render with Empty message', () => {
		const component = shallow(<TableDate emptyMessage='Empty Message' />);

		expect(component).toMatchSnapshot();
	});
});
