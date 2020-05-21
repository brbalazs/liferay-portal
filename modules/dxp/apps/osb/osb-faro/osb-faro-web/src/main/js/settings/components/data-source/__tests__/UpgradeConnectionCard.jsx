import React from 'react';
import UpgradeConnectionCard from '../UpgradeConnectionCard';
import {cleanup, render} from '@testing-library/react';
import {noop} from 'lodash';

jest.unmock('react-dom');

const actionMock = {
	label: 'test',
	onClick: () => noop
};
describe('UpgradeConnectionCard', () => {
	afterEach(cleanup);

	it('should render', () => {
		const {container} = render(
			<UpgradeConnectionCard action={actionMock} />
		);

		expect(container).toHaveTextContent('test');
	});
});
