import React from 'react';
import UserActionsRenderer from '../UserActionsRenderer';
import {shallow} from 'enzyme';
import {User} from 'shared/util/records';

describe('UserActionsRenderer', () => {
	it('should render', () => {
		const component = shallow(
			<UserActionsRenderer currentUserId={1} data={new User({id: 2})} />
		);

		expect(component).toMatchSnapshot();
	});

	it('should render with save and cancel buttons because editing is true', () => {
		const component = shallow(
			<UserActionsRenderer
				currentUserId={1}
				data={new User({id: 2})}
				editing
			/>
		);

		expect(component).toMatchSnapshot();
	});

	it('should render without row actions for the current user', () => {
		const component = shallow(
			<UserActionsRenderer
				currentUserId={1}
				data={new User({id: 1})}
				editing
			/>
		);

		expect(component.find('Button').length).toBe(0);
	});
});
