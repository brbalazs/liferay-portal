import InviteUsersModal from '../InviteUsersModal';
import React from 'react';
import {noop} from 'lodash';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

describe('InviteUsersModal', () => {
	it('should render', () => {
		const {container} = render(<InviteUsersModal onClose={noop} />);

		expect(container).toMatchSnapshot();
	});
});
