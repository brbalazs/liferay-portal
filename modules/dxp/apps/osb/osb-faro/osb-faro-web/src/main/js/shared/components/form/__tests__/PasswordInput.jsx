import PasswordInput from '../PasswordInput';
import React from 'react';
import {mockForm} from 'test/data';
import {shallow} from 'enzyme';

describe('PasswordInput', () => {
	it('should render', () => {
		const component = shallow(
			<PasswordInput field={{name: 'foo'}} form={mockForm()} />
		);
		expect(component).toMatchSnapshot();
	});

	it('should render with an input type of "password" if showPassword is false', () => {
		const component = shallow(
			<PasswordInput field={{name: 'foo'}} form={mockForm()} />
		);
		expect(component.props().type).toEqual('password');
	});

	it('should render with an input type of "text" if showPassword is true', () => {
		const component = shallow(
			<PasswordInput field={{name: 'foo'}} form={mockForm()} />
		);
		component.setState({showPassword: true});
		jest.runAllTimers();
		expect(component.render()).toMatchSnapshot();
	});
});
