import React from 'react';
import {shallow} from 'enzyme';
import {User} from 'shared/util/records';
import {WorkspacesBasePage} from '../BasePage';

const currentUser = new User({
	emailAddress: 'test@test.com',
	name: 'Test Test'
});

describe('WorkspacesBasePage', () => {
	it('should render', () => {
		const component = shallow(
			<WorkspacesBasePage
				currentUser={currentUser}
				details={[
					<p key='1'>{'Test Details'}</p>,
					<p key='2'>{'More Test Details'}</p>
				]}
				title='Test Title'
			/>
		);

		expect(component).toMatchSnapshot();
	});

	it('should render when details is jsx', () => {
		const component = shallow(
			<WorkspacesBasePage
				currentUser={currentUser}
				details={<b>{'test'}</b>}
				title='Test Title'
			/>
		);
		expect(component).toMatchSnapshot();
	});

	it('should render when details is a string', () => {
		const component = shallow(
			<WorkspacesBasePage
				currentUser={currentUser}
				details='Test Details'
				title='Test Title'
			/>
		);
		expect(component).toMatchSnapshot();
	});

	it('should render with back button', () => {
		const component = shallow(
			<WorkspacesBasePage
				backLabel='Back to Test'
				backURL='#'
				currentUser={currentUser}
				details={['Test Details. ', 'More Test Details']}
				title='Test Title'
			/>
		);

		expect(component).toMatchSnapshot();
	});
});
