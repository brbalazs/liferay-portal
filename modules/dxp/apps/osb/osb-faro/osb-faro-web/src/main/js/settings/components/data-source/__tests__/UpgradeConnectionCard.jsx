import React from 'react';
import UpgradeConnectionCard from '../UpgradeConnectionCard';
import {cleanup, render} from '@testing-library/react';
import {noop} from 'lodash';

jest.unmock('react-dom');

const actionMock = {
	label: 'action test',
	onClick: () => noop
};

describe('UpgradeConnectionCard', () => {
	afterEach(cleanup);

	it('should render', () => {
		const {container} = render(
			<UpgradeConnectionCard
				action={actionMock}
				content='content test'
				title='title test'
			/>
		);

		expect(container).toHaveTextContent('action test');
		expect(container).toHaveTextContent('content test');
		expect(container).toHaveTextContent('title test');
	});
});
