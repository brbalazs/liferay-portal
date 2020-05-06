import InviteUsersModal from '../InviteUsersModal';
import React from 'react';
import {noop} from 'lodash';
import {shallow} from 'enzyme';

describe('InviteUsersModal', () => {
	it('should render', () => {
		const component = shallow(<InviteUsersModal onClose={noop} />);
		expect(component).toMatchSnapshot();
	});
});
