import optional from '../Optional';
import React from 'react';
import {shallow} from 'enzyme';

describe('Optional', () => {
	it('should render the original component', () => {
		const fn = jest.fn();
		const hoc = () => fn;
		const Optional = optional(hoc)(jest.fn());
		shallow(<Optional id={null} />);
		expect(fn).not.toBeCalled();
	});

	it('should render the HOC component instead', () => {
		const hoc = jest.fn(() => () => 'hoc component');
		const Optional = optional(hoc)(jest.fn(() => 'wrapped component'));
		const component = shallow(<Optional id={23} />);
		expect(component.shallow().text()).toEqual('hoc component');
	});

	it('should allow the id prop name to be set', () => {
		const hoc = jest.fn(() => () => 'hoc component');
		const Optional = optional(hoc, {idPropName: 'foobar'})(
			jest.fn(() => 'wrapped component')
		);
		const component = shallow(<Optional foobar={23} />);
		expect(component.prop('foobar')).toEqual(23);
	});
});
