import Input from '../Input';
import React from 'react';
import {mockForm} from 'test/data';
import {shallow} from 'enzyme';

describe('Input', () => {
	it('should render', () => {
		const component = shallow(
			<Input field={{name: 'foo'}} form={mockForm()} />
		);
		expect(component).toMatchSnapshot();
	});

	it('should render a masked input', () => {
		const component = shallow(
			<Input field={{name: 'foo'}} form={mockForm()} mask={[]} />
		);
		expect(component).toMatchSnapshot();
	});
});
