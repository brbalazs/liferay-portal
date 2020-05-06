import React from 'react';
import Sidebar from '../index';
import {shallow} from 'enzyme';
import {User} from 'shared/util/records';

const defaultProps = {
	activePathname: '',
	channelId: '123',
	currentUser: new User({emailAddress: 'test@test.com', name: 'Test Test'}),
	groupId: '23'
};

describe('Sidebar', () => {
	it('should render', () => {
		const component = shallow(<Sidebar {...defaultProps} />);
		expect(component).toMatchSnapshot();
	});

	it('should render as collapsed', () => {
		const component = shallow(<Sidebar {...defaultProps} collapsed />);
		expect(component.find('.sidebar-root.collapsed').exists()).toBe(true);
	});

	it('should have default values if not all language keys are passed', () => {
		const component = shallow(
			<Sidebar
				{...defaultProps}
				language={{
					accounts: 'Foo',
					assets: 'Bar'
				}}
			/>
		);

		expect(component).toMatchSnapshot();
	});

	it('should render with a specific sidebar id active', () => {
		const component = shallow(
			<Sidebar
				{...defaultProps}
				activePathname='/workspace/23/contacts/individuals'
			/>
		);
		expect(component).toMatchSnapshot();
	});
});
